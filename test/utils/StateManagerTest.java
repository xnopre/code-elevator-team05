package utils;

import static org.fest.assertions.Assertions.assertThat;
import static utils.Direction.DOWN;
import static utils.Direction.UP;
import static utils.StateManager.INITIAL_STATE;

import java.util.Collection;

import org.junit.Test;

public class StateManagerTest {

	private final StateManager stateManager = new StateManager(
			new FloorBoundaries(0, 5));

	@Test
	public void call_must_store_last_call() {

		stateManager.storeCall(3, DOWN);

		ElevatorState elevatorState = stateManager.getCurrentState();
		Collection<Call> calls = elevatorState.getWaitingCalls();
		assertThat(calls).containsOnly(new Call(3, DOWN));
	}

	@Test
	public void call_must_remember_all_specified_calls() {

		stateManager.storeCall(3, DOWN);
		stateManager.storeCall(1, UP);

		ElevatorState elevatorState = stateManager.getCurrentState();
		Collection<Call> calls = elevatorState.getWaitingCalls();
		assertThat(calls).containsOnly(new Call(3, DOWN), new Call(1, UP));
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

	@Test
	public void ensure_floor_is_Incremented() {
		stateManager.incrementFloor();
		assertThat(stateManager.getCurrentState().getCurrentFloor()).isEqualTo(
				1);
	}

	@Test(expected = UnreachableFloorException.class)
	public void ensure_cant_go_down_first_floor() {
		stateManager.decrementFloor();
	}

	@Test(expected = UnreachableFloorException.class)
	public void ensure_cant_go_up_last_floor() {
		stateManager.incrementFloor();
		stateManager.incrementFloor();
		stateManager.incrementFloor();
		stateManager.incrementFloor();
		stateManager.incrementFloor();
		stateManager.incrementFloor();

	}

	@Test
	public void ensure_floor_is_Decremented() {
		stateManager.incrementFloor();

		stateManager.decrementFloor();
		assertThat(stateManager.getCurrentState().getCurrentFloor()).isEqualTo(
				0);
	}

}
