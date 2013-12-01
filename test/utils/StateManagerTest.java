package utils;

import static org.fest.assertions.Assertions.assertThat;
import static utils.Direction.DOWN;
import static utils.Direction.UP;

import java.util.Collection;

import org.junit.Test;

public class StateManagerTest {

	private final FloorBoundaries floorBoundaries = new FloorBoundaries(0, 5);
	private final StateManager stateManager = new StateManager(0, 5, 30, 1);

	@Test
	public void call_must_store_last_call() {

		stateManager.storeWaitingCall(3, DOWN);

		ElevatorState elevatorState = stateManager.getCurrentState();
		Collection<Call> calls = elevatorState.getWaitingCalls();
		assertThat(calls).containsOnly(new Call(3, DOWN));
	}

	@Test
	public void call_must_remember_all_specified_calls() {

		stateManager.storeWaitingCall(3, DOWN);
		stateManager.storeWaitingCall(1, UP);

		ElevatorState elevatorState = stateManager.getCurrentState();
		Collection<Call> calls = elevatorState.getWaitingCalls();
		assertThat(calls).containsOnly(new Call(3, DOWN), new Call(1, UP));
	}

	@Test
	public void ensure_reset_command_restore_initial_state() {
		stateManager.storeWaitingCall(3, DOWN);

		stateManager.reset(-4, 8, 5, 2);

		final ElevatorState expectedState = new ElevatorState(2);
		assertThat(stateManager.getCurrentState()).isEqualTo(expectedState);
		assertThat(stateManager.getFloorBoundaries()).isEqualTo(new FloorBoundaries(-4, 8));
		assertThat(stateManager.getCabinSize()).isEqualTo(5);
	}

	@Test
	public void ensure_floor_is_Incremented() {
		stateManager.incrementFloor(0);
		assertThat(stateManager.getCurrentState().getCurrentFloor(0)).isEqualTo(1);
	}

	@Test(expected = UnreachableFloorException.class)
	public void ensure_cant_go_down_first_floor() {
		stateManager.decrementFloor(0);
	}

	@Test(expected = UnreachableFloorException.class)
	public void ensure_cant_go_up_last_floor() {
		for (int i = 0; i < 19; i++) {
			stateManager.incrementFloor(0);
		}
		stateManager.incrementFloor(0);
	}

	@Test
	public void ensure_floor_is_decremented() {
		stateManager.incrementFloor(0);

		stateManager.decrementFloor(0);
		assertThat(stateManager.getCurrentState().getCurrentFloor(0)).isEqualTo(0);
	}

	@Test
	public void ensure_that_opened_state_is_stored() {
		stateManager.setClosed(0);
		stateManager.setOpened(0);
		stateManager.storeWaitingCall(3, DOWN);
		assertThat(stateManager.getCurrentState().isOpened(0)).isTrue();
		assertThat(stateManager.getCurrentState().isClosed(0)).isFalse();
	}

	@Test
	public void ensure_that_closed_state_is_stored() {
		stateManager.setOpened(0);
		stateManager.setClosed(0);
		stateManager.storeWaitingCall(3, DOWN);
		assertThat(stateManager.getCurrentState().isOpened(0)).isFalse();
		assertThat(stateManager.getCurrentState().isClosed(0)).isTrue();
	}

	@Test
	public void ensure_that_only_one_matching_call_is_removed() {
		stateManager.storeWaitingCall(3, DOWN);
		stateManager.storeWaitingCall(3, DOWN);
		stateManager.storeWaitingCall(3, UP);

		stateManager.removeWaitingCall(3, DOWN);
		assertThat(stateManager.getCurrentState().getWaitingCalls()).containsOnly(new Call(3, DOWN), new Call(3, UP));
	}

	@Test
	public void ensure_go_requests_are_stored() {
		assertThat(stateManager.getCurrentState().getGoRequests(0)).isEmpty();

		stateManager.storeGoRequest(0, 3);
		stateManager.storeGoRequest(0, 5);

		assertThat(stateManager.getCurrentState().getGoRequests(0)).containsOnly(3, 5);
	}

	@Test
	public void ensure_go_request_is_removed() {
		assertThat(stateManager.getCurrentState().getGoRequests(0)).isEmpty();
		stateManager.storeGoRequest(0, 7);
		stateManager.storeGoRequest(0, 3);
		stateManager.storeGoRequest(0, 5);

		stateManager.removeGoRequest(0, 3);

		assertThat(stateManager.getCurrentState().getGoRequests(0)).containsOnly(7, 5);
	}

	@Test
	public void ensure_no_problem_if_request_to_remove_unstored_request_floor() {
		assertThat(stateManager.getCurrentState().getGoRequests(0)).isEmpty();
		stateManager.storeGoRequest(0, 7);
		stateManager.storeGoRequest(0, 5);

		stateManager.removeGoRequest(0, 3);

		assertThat(stateManager.getCurrentState().getGoRequests(0)).containsOnly(7, 5);
	}

	@Test
	public void must_store_current_direction_up() {
		stateManager.setCurrentDirection(0, DOWN);
		stateManager.setCurrentDirection(0, UP);
		assertThat(stateManager.getCurrentState().getCurrentDirection(0)).isEqualTo(UP);
	}

	@Test
	public void must_store_current_direction_down() {
		stateManager.setCurrentDirection(0, UP);
		stateManager.setCurrentDirection(0, DOWN);
		assertThat(stateManager.getCurrentState().getCurrentDirection(0)).isEqualTo(DOWN);
	}

}
