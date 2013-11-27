package generators;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static utils.Command.CLOSE;
import static utils.Command.NOTHING;
import static utils.Command.OPEN_DOWN;
import static utils.Command.OPEN_UP;
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
		givenAnElevatorClosedAtFloor(3).andNoWaitingCalls().andMiddleFloorIs(3).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(NOTHING);
	}

	@Test
	public void command_is_nothing_if_is_open_and_receive_call_at_same_floor() {
		givenAnElevatorOpenedAtFloor(1).andWaitingCalls(call(1, UP)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(NOTHING);
	}

	@Test
	public void command_is_open_if_call_at_same_floor_with_other_direction_and_no_more_calls_or_go_in_current_direction_down() {
		givenAnElevatorClosedAtFloor(1).withDirection(DOWN).andWaitingCalls(call(1, UP), call(2, UP)).andGoRequests(2).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN_UP);
		assertThatElevatorIsOpened();
		assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(1, UP);
	}

	@Test
	public void command_is_open_if_call_at_same_floor_with_other_direction_and_no_more_calls_in_current_direction_up() {
		givenAnElevatorClosedAtFloor(4).withDirection(UP).andWaitingCalls(call(4, DOWN), call(2, DOWN)).andGoRequests(3).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN_DOWN);
		assertThatElevatorIsOpened();
		assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(4, DOWN);
	}

	@Test
	public void command_is_open_if_call_at_same_floor_going_down() {
		givenAnElevatorClosedAtFloor(1).withDirection(DOWN).andWaitingCalls(call(2, UP), call(1, DOWN)).andGoRequests(0).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN_DOWN);
		assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(1, DOWN);
	}

	@Test
	public void command_is_open_if_there_is_a_go_at_current_floor_going_down() {
		givenAnElevatorClosedAtFloor(4).withDirection(DOWN).andWaitingCalls(call(2, UP), call(2, DOWN), call(5, UP), call(5, DOWN)).andGoRequests(3, 4, 5)
				.build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN_DOWN);
		assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(4, DOWN);
	}

	// +2
	@Test
	public void command_is_open_if_call_at_same_floor_going_up() {
		givenAnElevatorClosedAtFloor(1).withDirection(UP).andWaitingCalls(call(0, UP), call(1, UP)).andGoRequests(2).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN_UP);
		assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(1, UP);
	}

	@Test
	public void command_is_open_if_there_is_a_go_at_current_floor_going_up() {
		givenAnElevatorClosedAtFloor(4).withDirection(UP).andWaitingCalls(call(2, UP), call(2, DOWN), call(5, UP), call(5, DOWN)).andGoRequests(3, 4, 5)
				.build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN_UP);
		assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(4, UP);
	}

	@Test
	public void command_is_up_even_if_there_is_a_call_at_current_floor_but_must_skip_waiting_calls() {
		givenAnElevatorClosedAtFloor(7).withDirection(UP).andWaitingCalls(call(7, UP)).andGoRequests(8).andMustSkipExtraWaitingCalls().build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP);
	}

	@Test
	public void command_is_down_even_if_there_is_a_call_at_current_floor_but_must_skip_waiting_calls() {
		givenAnElevatorClosedAtFloor(7).withDirection(DOWN).andWaitingCalls(call(7, UP)).andGoRequests(5).andMustSkipExtraWaitingCalls().build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN);
	}

	@Test
	public void command_is_close_if_elevator_is_opened() {
		givenAnElevatorOpened();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(CLOSE);
		assertThatElevatorIsClosed();
	}

	@Test
	public void command_is_up_if_there_is_a_go_upstairs_and_current_direction_is_up() {
		givenAnElevatorClosedAtFloor(2).withDirection(UP).andWaitingCalls(call(1, UP), call(2, DOWN)).andGoRequests(1, 4, 3).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP);
		assertThatCurrentFloorIsIncremented();
	}

	@Test
	public void command_is_up_if_there_is_a_go_upstairs_and_current_direction_is_down() {
		givenAnElevatorClosedAtFloor(2).withDirection(DOWN).andGoRequests(4).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP);
		assertThatCurrentFloorIsIncremented();
		assertThatCurrentDirectionIsSetTo(UP);
	}

	@Test
	public void command_is_up_if_there_is_a_call_upstairs_and_current_direction_is_up() {
		givenAnElevatorClosedAtFloor(2).withDirection(UP).andWaitingCalls(call(6, UP), call(2, DOWN), call(1, UP), call(1, DOWN)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP);
		assertThatCurrentFloorIsIncremented();
	}

	@Test
	public void command_is_up_if_there_is_a_call_upstairs_and_current_direction_is_down() {
		givenAnElevatorClosedAtFloor(2).withDirection(DOWN).andWaitingCalls(call(6, UP)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP);
		assertThatCurrentFloorIsIncremented();
		assertThatCurrentDirectionIsSetTo(UP);
	}

	@Test
	public void command_is_up_if_no_waiting_calls_and_no_go_requests_and_is_not_at_middle_floor() {
		givenAnElevatorClosedAtFloor(2).withDirection(DOWN).andNoWaitingCalls().andMiddleFloorIs(3).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP);
		assertThatCurrentFloorIsIncremented();
		assertThatCurrentDirectionIsSetTo(UP);
	}

	@Test
	public void command_is_down_if_no_waiting_calls_and_no_go_requests_and_is_not_at_middle_floor() {
		givenAnElevatorClosedAtFloor(6).withDirection(DOWN).andNoWaitingCalls().andMiddleFloorIs(3).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN);
		assertThatCurrentFloorIsDecremented();
		assertThatCurrentDirectionIsSetTo(DOWN);
	}

	@Test
	public void command_is_down_if_there_is_a_go_downstairs_and_current_direction_is_down() {
		givenAnElevatorClosedAtFloor(2).withDirection(DOWN).andWaitingCalls(call(5, DOWN), call(2, UP), call(1, DOWN)).andGoRequests(4, 0).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN);
		assertThatCurrentFloorIsDecremented();
		assertThatCurrentDirectionIsSetTo(DOWN);
	}

	@Test
	public void command_is_down_if_there_is_a_go_downstairs_and_current_direction_is_up() {
		givenAnElevatorClosedAtFloor(2).withDirection(UP).andGoRequests(1).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN);
		assertThatCurrentFloorIsDecremented();
		assertThatCurrentDirectionIsSetTo(DOWN);
	}

	@Test
	public void command_is_down_if_there_is_a_call_downstairs_and_current_direction_is_down() {
		givenAnElevatorClosedAtFloor(4).withDirection(DOWN).andWaitingCalls(call(5, UP), call(1, DOWN)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN);
		assertThatCurrentFloorIsDecremented();
	}

	@Test
	public void command_is_down_if_there_is_a_call_downstairs_and_current_direction_is_up() {
		givenAnElevatorClosedAtFloor(2).withDirection(UP).andWaitingCalls(call(1, UP)).build();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN);
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

	private void assertThatAllTheCallsAndGosForTheCurrentFloorAreRemoved(int currentFloor, Direction direction) {
		verify(mockWaitingCallAndGoRemover).removeAllCallsFromTheCurrentFloor(direction);
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

		public MyAssert is(Command expectedCommand) {
			Assertions.assertThat(command).isEqualTo(expectedCommand);
			return this;
		}
	}
}
