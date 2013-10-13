package generators;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.Command.CLOSE;
import static utils.Command.NOTHING;
import static utils.Command.OPEN;
import static utils.Direction.UP;
import static utils.StateBuilderFactory.call;
import static utils.StateBuilderForTest.createStateAndDoReturnItByStateManager;

import org.fest.assertions.Assertions;
import org.junit.Test;

import utils.Command;
import utils.StateBuilderFactory;
import utils.StateBuilderForTest;
import utils.StateManager;

public class BetterWaitingForTheBestCommandGeneratorTest {

	private final StateManager mockStateManager = mock(StateManager.class);

	private final BetterWaitingForTheBestCommandGenerator commandGenerator = new BetterWaitingForTheBestCommandGenerator(mockStateManager);

	private final StateBuilderFactory stateBuilderFactory = new StateBuilderFactory(mockStateManager);

	@Test
	public void command_is_nothing_without_state_command() {
		givenAnElevatorClosedAtFloor(0).andNoWaitingCalls();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(NOTHING).andIsStoredInHistory();
	}

	@Test
	public void command_is_nothing_if_is_open_and_receive_call_at_same_floor() {
		givenAnElevatorOpenedAtFloor(1).andWaitingCalls(call(1, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(NOTHING).andIsStoredInHistory();
	}

	@Test
	public void command_is_open_if_call_at_same_floor_0() {
		givenAnElevatorClosedAtFloor(0).andWaitingCalls(call(0, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN).andIsStoredInHistory();
		assertThatElevatorIsOpened();
	}

	@Test
	public void command_is_open_if_call_at_same_floor_1() {
		givenAnElevatorClosedAtFloor(1).andWaitingCalls(call(2, UP), call(1, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN).andIsStoredInHistory();
	}

	@Test
	public void command_is_open_if_there_is_a_go_at_current_floor() {
		givenAnElevatorClosedAtFloor(4).andGoRequests(4);
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(OPEN).andIsStoredInHistory();
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
		givenAnElevatorOpenedAtFloor(1).andWaitingCalls(call(1, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(CLOSE).andIsStoredInHistory();
	}

	@Test
	public void command_is_up_if_there_is_a_go_upstairs() {
		// TODO : optimisation : garder la direction en cours
		givenAnElevatorClosedAtFloor(2).andGoRequests(4);
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP).andIsStoredInHistory();
		assertThatCurrentFloorIsIncremented();
	}

	@Test
	public void command_is_up_if_there_is_a_call_upstairs() {
		// TODO : optimisation : garder la direction en cours
		givenAnElevatorClosedAtFloor(2).andWaitingCalls(call(6, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.UP).andIsStoredInHistory();
		assertThatCurrentFloorIsIncremented();
	}

	@Test
	public void command_is_down_if_there_is_a_go_downstairs() {
		// TODO : optimisation : garder la direction en cours
		givenAnElevatorClosedAtFloor(2).andGoRequests(0);
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN).andIsStoredInHistory();
		assertThatCurrentFloorIsDecremented();
	}

	@Test
	public void command_is_down_if_there_is_a_call_downstairs() {
		// TODO : optimisation : garder la direction en cours
		givenAnElevatorClosedAtFloor(4).andWaitingCalls(call(1, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThat(nextCommand).is(Command.DOWN).andIsStoredInHistory();
		assertThatCurrentFloorIsDecremented();
	}

	// privates -------------------------------

	private StateBuilderForTest givenAnElevatorClosedAtFloor(final int floor) {
		return stateBuilderFactory.givenAnElevatorClosedAtFloor(floor);
	}

	private StateBuilderForTest givenAnElevatorOpenedAtFloor(final int floor) {
		return stateBuilderFactory.givenAnElevatorOpenedAtFloor(floor);
	}

	private void givenAnElevatorOpened() {
		createStateAndDoReturnItByStateManager(mockStateManager, 0, true);
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
