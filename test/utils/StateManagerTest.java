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
	public void call_must_store_last_call() {

		stateManager.storeCall(3, DOWN);

		ElevatorState elevatorState = stateManager.getCurrentState();
		Collection<ElevatorCall> calls = elevatorState.getWaitingCalls();
		assertThat(calls).containsOnly(new ElevatorCall(3, DOWN));
	}

	@Test
	public void call_must_remember_all_specified_calls() {

		stateManager.storeCall(3, DOWN);
		stateManager.storeCall(1, UP);

		ElevatorState elevatorState = stateManager.getCurrentState();
		Collection<ElevatorCall> calls = elevatorState.getWaitingCalls();
		assertThat(calls).containsOnly(new ElevatorCall(3, DOWN),
				new ElevatorCall(1, UP));
	}

	@Test(expected = OnlyOneCallPerFloorException.class)
	public void ensure_that_only_one_call_per_floor_can_be_performed_even_if_for_other_direction() {
		stateManager.storeCall(3, DOWN);
		stateManager.storeCall(3, UP);
	}

	@Test(expected = OnlyOneCallPerFloorException.class)
	public void ensure_that_only_one_call_per_floor_can_be_performed() {
		stateManager.storeCall(3, DOWN);
		stateManager.storeCall(3, DOWN);
	}

	@Test
	public void initial_state_must_be_initial_state() {
		assertThat(stateManager.getCurrentState()).isSameAs(INITIAL_STATE);
	}

	@Test
	public void ensure_that_storing_a_call_change_intial_state() {
		stateManager.storeCall(3, DOWN);

		assertThat(stateManager.getCurrentState()).isNotEqualTo(
				StateManager.INITIAL_STATE);
	}

	@Test
	public void ensure_reset_command_restore_initial_state() {
		stateManager.storeCall(3, DOWN);

		stateManager.reset();

		assertThat(stateManager.getCurrentState()).isSameAs(
				StateManager.INITIAL_STATE);
	}

}
