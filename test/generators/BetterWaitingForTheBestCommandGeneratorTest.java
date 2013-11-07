package generators;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.Command.CLOSE;
import static utils.Command.NOTHING;
import static utils.Command.OPEN;
import static utils.Direction.DOWN;
import static utils.Direction.UP;
import static utils.StateBuilderFactory.call;
import static utils.StateBuilderForTest.createStateAndDoReturnItByStateManager;

import org.fest.assertions.Assertions;
import org.junit.Test;

import utils.Command;
import utils.Direction;
import utils.SizeLimitedArrayList;
import utils.StateBuilderFactory;
import utils.StateBuilderForTest;
import utils.StateManager;
import utils.WaitingCallAndGoRemover;

public class BetterWaitingForTheBestCommandGeneratorTest {

	private final StateManager mockStateManager = mock(StateManager.class);

	private final WaitingCallAndGoRemover mockWaitingCallAndGoRemover = mock(WaitingCallAndGoRemover.class);

	private final BetterWaitingForTheBestCommandGenerator commandGenerator = new BetterWaitingForTheBestCommandGenerator(mockStateManager,
			mockWaitingCallAndGoRemover);

	private final StateBuilderFactory stateBuilderFactory = new StateBuilderFactory(mockStateManager);

	@Test
	public void command_is_nothing_without_state_command() {
		givenAnElevatorClosedAtFloor(0).andNoWaitingCalls().andIsAtMiddleFloor().build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(NOTHING).andIsStoredInHistory();
	}

	@Test
	public void command_is_nothing_if_is_open_and_receive_call_at_same_floor() {
		givenAnElevatorOpenedAtFloor(1).andWaitingCalls(call(1, UP)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(NOTHING).andIsStoredInHistory();
	}

	@Test
	public void command_is_open_if_call_at_same_floor_with_other_direction_and_no_more_calls_in_current_direction_down() {
		givenAnElevatorClosedAtFloor(1).withDirection(DOWN).andWaitingCalls(call(1, UP), call(2, UP)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN).andIsStoredInHistory();
		assertThatElevatorIsOpened();
		assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(1);
	}

	@Test
	public void command_is_open_if_call_at_same_floor_with_other_direction_and_no_more_calls_in_current_direction_up() {
		givenAnElevatorClosedAtFloor(4).withDirection(UP).andWaitingCalls(call(4, DOWN), call(2, DOWN)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN).andIsStoredInHistory();
		assertThatElevatorIsOpened();
		assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(4);
	}

	@Test
	public void command_is_open_if_call_at_same_floor_1() {
		givenAnElevatorClosedAtFloor(1).andWaitingCalls(call(2, UP), call(1, UP)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN).andIsStoredInHistory();
		assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(1);
	}

	@Test
	public void command_is_open_if_there_is_a_go_at_current_floor() {
		givenAnElevatorClosedAtFloor(4).andGoRequests(4).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN).andIsStoredInHistory();
		assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(4);
	}

	@Test
	public void command_is_close_if_elevator_is_opened() {
		givenAnElevatorOpened();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(CLOSE).andIsStoredInHistory();
		assertThatElevatorIsClosed();
	}

	@Test
	public void command_is_close_if_open_and_nothing_appends_after_3_nothing() {
		// Hack pour éviter de rester parfois bloquer sans vraiement trouver
		// d'explications ...
		when(mockStateManager.areThreeLastCommandEqualTo(NOTHING)).thenReturn(true);
		givenAnElevatorOpenedAtFloor(1).andWaitingCalls(call(1, UP)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(CLOSE).andIsStoredInHistory();
	}

	@Test
	public void command_is_up_if_there_is_a_go_upstairs_and_current_direction_is_up() {
		givenAnElevatorClosedAtFloor(2).withDirection(UP).andWaitingCalls(call(1, UP), call(2, DOWN)).andGoRequests(1, 4, 3).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP).andIsStoredInHistory();
		assertThatCurrentFloorIsIncremented();
	}

	@Test
	public void command_is_up_if_there_is_a_go_upstairs_and_current_direction_is_down() {
		givenAnElevatorClosedAtFloor(2).withDirection(DOWN).andGoRequests(4).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP).andIsStoredInHistory();
		assertThatCurrentFloorIsIncremented();
		assertThatCurrentDirectionIsSetTo(UP);
	}

	@Test
	public void command_is_up_if_there_is_a_call_upstairs_and_current_direction_is_up() {
		givenAnElevatorClosedAtFloor(2).withDirection(UP).andWaitingCalls(call(6, UP), call(2, DOWN), call(1, UP), call(1, DOWN)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP).andIsStoredInHistory();
		assertThatCurrentFloorIsIncremented();
	}

	@Test
	public void command_is_up_if_there_is_a_call_upstairs_and_current_direction_is_down() {
		givenAnElevatorClosedAtFloor(2).withDirection(DOWN).andWaitingCalls(call(6, UP)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP).andIsStoredInHistory();
		assertThatCurrentFloorIsIncremented();
		assertThatCurrentDirectionIsSetTo(UP);
	}

	@Test
	public void command_is_up_if_no_waiting_calls_and_no_go_requests_and_is_not_at_middle_floor() {
		givenAnElevatorClosedAtFloor(2).withDirection(DOWN).andNoWaitingCalls().andIsNotAtMiddleFloor().build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP).andIsStoredInHistory();
		assertThatCurrentFloorIsIncremented();
		assertThatCurrentDirectionIsSetTo(UP);
	}

	@Test
	public void command_is_down_if_there_is_a_go_downstairs_and_current_direction_is_down() {
		givenAnElevatorClosedAtFloor(2).withDirection(DOWN).andWaitingCalls(call(5, DOWN), call(2, UP), call(1, DOWN)).andGoRequests(4, 0).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN).andIsStoredInHistory();
		assertThatCurrentFloorIsDecremented();
	}

	@Test
	public void command_is_down_if_there_is_a_go_downstairs_and_current_direction_is_up() {
		givenAnElevatorClosedAtFloor(2).withDirection(UP).andGoRequests(1).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN).andIsStoredInHistory();
		assertThatCurrentFloorIsDecremented();
		assertThatCurrentDirectionIsSetTo(DOWN);
	}

	@Test
	public void command_is_down_if_there_is_a_call_downstairs_and_current_direction_is_down() {
		givenAnElevatorClosedAtFloor(4).withDirection(DOWN).andWaitingCalls(call(5, UP), call(1, DOWN)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN).andIsStoredInHistory();
		assertThatCurrentFloorIsDecremented();
	}

	@Test
	public void command_is_down_if_there_is_a_call_downstairs_and_current_direction_is_up() {
		givenAnElevatorClosedAtFloor(2).withDirection(UP).andWaitingCalls(call(1, UP)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN).andIsStoredInHistory();
		assertThatCurrentFloorIsDecremented();
		assertThatCurrentDirectionIsSetTo(DOWN);
	}

	// privates -------------------------------

	private StateBuilderForTest givenAnElevatorClosedAtFloor(final int floor) {
		return stateBuilderFactory.givenAnElevatorClosedAtFloor(floor);
	}

	private StateBuilderForTest givenAnElevatorOpenedAtFloor(final int floor) {
		return stateBuilderFactory.givenAnElevatorOpenedAtFloor(floor);
	}

	private void givenAnElevatorOpened() {
		createStateAndDoReturnItByStateManager(mockStateManager, 0, true, UP, null, null, new SizeLimitedArrayList<Command>(3));
	}

	private void assertThatElevatorIsOpened() {
		verify(mockStateManager).setOpened();
		verify(mockStateManager, times(0)).setClosed();
	}

	private void assertThatElevatorIsClosed() {
		verify(mockStateManager, times(0)).setOpened();
		verify(mockStateManager).setClosed();
	}

	private void assertThatCurrentFloorIsIncremented() {
		verify(mockStateManager).incrementFloor();
	}

	private void assertThatCurrentFloorIsDecremented() {
		verify(mockStateManager).decrementFloor();
	}

	private void assertThatCurrentDirectionIsSetTo(Direction direction) {
		verify(mockStateManager).setCurrentDirection(direction);
	}

	private void assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(int currentFloor) {
		verify(mockWaitingCallAndGoRemover).removeAllCallsFromTheCurrentFloor();
		verify(mockWaitingCallAndGoRemover).removeAllGosFromTheCurrentFloor();
	}

	private MyAssert assertThat(Command command) {
		return new MyAssert(command);
	}

	private class MyAssert {

		private final Command command;

		public MyAssert(Command command) {
			this.command = command;
		}

		public void andIsStoredInHistory() {
			verify(mockStateManager).storeCommandInHistory(command);
		}

		public MyAssert is(Command expectedCommand) {
			Assertions.assertThat(command).isEqualTo(expectedCommand);
			return this;
		}
	}
}
