package generators;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static utils.Command.CLOSE;
import static utils.Command.NOTHING;
import static utils.Command.OPEN;
import static utils.Direction.UP;
import static utils.StateBuilderFactory.call;
import static utils.StateBuilderForTest.createStateAndDoReturnItByStateManager;

import org.junit.Test;

import utils.Command;
import utils.StateBuilderFactory;
import utils.StateManager;

public class BetterWaitingForTheBestCommandGeneratorTest {

	private final StateManager mockStateManager = mock(StateManager.class);

	private final BetterWaitingForTheBestCommandGenerator commandGenerator = new BetterWaitingForTheBestCommandGenerator(mockStateManager);

	private final StateBuilderFactory stateBuilderFactory = new StateBuilderFactory(mockStateManager);

	@Test
	public void command_is_nothing_without_state_command() {
		stateBuilderFactory.givenAnElevatorClosedAtFloor(0).andNoWaitingCalls();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThatNextCommandIs(nextCommand, NOTHING);
	}

	@Test
	public void command_is_open_if_call_at_same_floor_0() {
		stateBuilderFactory.givenAnElevatorClosedAtFloor(0).andWaitingCalls(call(0, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThatNextCommandIs(nextCommand, OPEN);
		assertThatElevatorIsOpened();
	}

	@Test
	public void command_is_open_if_call_at_same_floor_1() {
		stateBuilderFactory.givenAnElevatorClosedAtFloor(1).andWaitingCalls(call(2, UP), call(1, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThatNextCommandIs(nextCommand, OPEN);
	}

	@Test
	public void command_is_open_if_there_is_a_go_at_current_floor() {
		stateBuilderFactory.givenAnElevatorClosedAtFloor(4).andGoRequests(4);
		final Command nextCommand = commandGenerator.nextCommand();
		assertThatNextCommandIs(nextCommand, OPEN);
	}

	@Test
	public void command_is_nothing_if_is_open_and_receive_call_at_same_floor() {
		stateBuilderFactory.givenAnElevatorOpenedAtFloor(1).andWaitingCalls(call(1, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThatNextCommandIs(nextCommand, NOTHING);
	}

	@Test
	public void command_is_close_if_elevator_is_opened() {
		givenAnElevatorOpened();
		final Command nextCommand = commandGenerator.nextCommand();
		assertThatNextCommandIs(nextCommand, CLOSE);
		assertThatElevatorIsClosed();
	}

	@Test
	public void command_is_up_if_there_is_a_go_upstairs() {
		// TODO : optimisation : garder la direction en cours
		stateBuilderFactory.givenAnElevatorClosedAtFloor(2).andGoRequests(4);
		final Command nextCommand = commandGenerator.nextCommand();
		assertThatNextCommandIs(nextCommand, Command.UP);
		assertThatCurrentFloorIsIncremented();
	}

	@Test
	public void command_is_up_if_there_is_a_call_upstairs() {
		// TODO : optimisation : garder la direction en cours
		stateBuilderFactory.givenAnElevatorClosedAtFloor(2).andWaitingCalls(call(6, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThatNextCommandIs(nextCommand, Command.UP);
		assertThatCurrentFloorIsIncremented();
	}

	@Test
	public void command_is_down_if_there_is_a_go_downstairs() {
		// TODO : optimisation : garder la direction en cours
		stateBuilderFactory.givenAnElevatorClosedAtFloor(2).andGoRequests(0);
		final Command nextCommand = commandGenerator.nextCommand();
		assertThatNextCommandIs(nextCommand, Command.DOWN);
		assertThatCurrentFloorIsDecremented();
	}

	@Test
	public void command_is_down_if_there_is_a_call_downstairs() {
		// TODO : optimisation : garder la direction en cours
		stateBuilderFactory.givenAnElevatorClosedAtFloor(4).andWaitingCalls(call(1, UP));
		final Command nextCommand = commandGenerator.nextCommand();
		assertThatNextCommandIs(nextCommand, Command.DOWN);
		assertThatCurrentFloorIsDecremented();
	}

	// privates -------------------------------

	private void assertThatNextCommandIs(Command nextCommand, final Command expectedCommand) {
		assertThat(nextCommand).isEqualTo(expectedCommand);
	}

	private void assertThatElevatorIsOpened() {
		verify(mockStateManager).setOpened();
		verify(mockStateManager, times(0)).setClosed();
	}

	private void assertThatElevatorIsClosed() {
		verify(mockStateManager, times(0)).setOpened();
		verify(mockStateManager).setClosed();
	}

	private void givenAnElevatorOpened() {
		createStateAndDoReturnItByStateManager(mockStateManager, 0, true);
	}

	private void assertThatCurrentFloorIsIncremented() {
		verify(mockStateManager).incrementFloor();
	}

	private void assertThatCurrentFloorIsDecremented() {
		verify(mockStateManager).decrementFloor();
	}
}
