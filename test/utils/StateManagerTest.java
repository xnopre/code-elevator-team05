package utils;

import static org.fest.assertions.Assertions.assertThat;
import static utils.Direction.DOWN;
import static utils.Direction.UP;
import static utils.StateManager.INITIAL_STATE;

import java.util.Collection;

import org.junit.Test;

public class StateManagerTest {

	private final StateManager stateManager = new StateManager();

	@Test
	public void call_must_store_specified_call() {

		stateManager.storeCall(3, DOWN);
		stateManager.storeCall(1, UP);

		ElevatorState elevatorState = stateManager.getCurrentState();
		Collection<ElevatorCall> calls = elevatorState.getWaitingCalls();
		assertThat(calls).contains(new ElevatorCall(3, DOWN));
		assertThat(calls).contains(new ElevatorCall(1, UP));

	}

	@Test
	public void initial_state_must_be_initial_state() {
		assertThat(stateManager.getCurrentState()).isSameAs(INITIAL_STATE);
	}

	@Test
	public void getCurrentState_must_return_initial_state_adter_reset() {

		stateManager.storeCall(3, DOWN);
		stateManager.reset();
		ElevatorState state = stateManager.getCurrentState();

		assertThat(state).isSameAs(StateManager.INITIAL_STATE);
	}

}
