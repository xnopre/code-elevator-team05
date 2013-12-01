package utils;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utils.Direction.DOWN;
import static utils.Direction.UP;

import java.util.Collection;

import org.junit.Test;

public class StateManagerTest {

	private final FloorBoundaries floorBoundaries = new FloorBoundaries(0, 5);
	private final StateManager stateManager = new StateManager(floorBoundaries);

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

		final ElevatorState expectedState = new ElevatorState(0);
		assertThat(stateManager.getCurrentState()).isEqualTo(expectedState);
		assertThat(stateManager.getFloorBoundaries()).isEqualTo(new FloorBoundaries(-4, 8));
		assertThat(stateManager.getCabinSize()).isEqualTo(5);
	}

	@Test
	public void ensure_floor_is_Incremented() {
		stateManager.incrementFloor();
		assertThat(stateManager.getCurrentState().getCurrentFloor()).isEqualTo(1);
	}

	@Test(expected = UnreachableFloorException.class)
	public void ensure_cant_go_down_first_floor() {
		stateManager.decrementFloor();
	}

	@Test(expected = UnreachableFloorException.class)
	public void ensure_cant_go_up_last_floor() {
		for (int i = 0; i < 19; i++) {
			stateManager.incrementFloor();
		}
		stateManager.incrementFloor();
	}

	@Test
	public void ensure_floor_is_Decremented() {
		stateManager.incrementFloor();

		stateManager.decrementFloor();
		assertThat(stateManager.getCurrentState().getCurrentFloor()).isEqualTo(0);
	}

	@Test
	public void ensure_that_opened_state_is_stored() {
		stateManager.setClosed();
		stateManager.setOpened();
		stateManager.storeWaitingCall(3, DOWN);
		assertThat(stateManager.getCurrentState().isOpened()).isTrue();
		assertThat(stateManager.getCurrentState().isClosed()).isFalse();
	}

	@Test
	public void ensure_that_closed_state_is_stored() {
		stateManager.setOpened();
		stateManager.setClosed();
		stateManager.storeWaitingCall(3, DOWN);
		assertThat(stateManager.getCurrentState().isOpened()).isFalse();
		assertThat(stateManager.getCurrentState().isClosed()).isTrue();
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
		assertThat(stateManager.getCurrentState().getGoRequests()).isEmpty();

		stateManager.storeGoRequest(3);
		stateManager.storeGoRequest(5);

		assertThat(stateManager.getCurrentState().getGoRequests()).containsOnly(3, 5);
	}

	@Test
	public void ensure_go_request_is_removed() {
		assertThat(stateManager.getCurrentState().getGoRequests()).isEmpty();
		stateManager.storeGoRequest(7);
		stateManager.storeGoRequest(3);
		stateManager.storeGoRequest(5);

		stateManager.removeGoRequest(3);

		assertThat(stateManager.getCurrentState().getGoRequests()).containsOnly(7, 5);
	}

	@Test
	public void ensure_no_problem_if_request_to_remove_unstored_request_floor() {
		assertThat(stateManager.getCurrentState().getGoRequests()).isEmpty();
		stateManager.storeGoRequest(7);
		stateManager.storeGoRequest(5);

		stateManager.removeGoRequest(3);

		assertThat(stateManager.getCurrentState().getGoRequests()).containsOnly(7, 5);
	}

	@Test
	public void must_store_current_direction_up() {
		stateManager.setCurrentDirection(DOWN);
		stateManager.setCurrentDirection(UP);
		assertThat(stateManager.getCurrentState().getCurrentDirection()).isEqualTo(UP);
	}

	@Test
	public void must_store_current_direction_down() {
		stateManager.setCurrentDirection(UP);
		stateManager.setCurrentDirection(DOWN);
		assertThat(stateManager.getCurrentState().getCurrentDirection()).isEqualTo(DOWN);
	}

	@Test
	public void mustSkipExtraWaitingCalls_must_return_false_if_go_request_number_less_than_floors_numbers_devided_by_2() {
		stateManager.reset(0, 5, 99, 2);
		final int floorsNumber = floorBoundaries.calculateFloorsNumber();
		final int floorsNumberThreshold = floorsNumber / 2;
		for (int i = 0; i < floorsNumberThreshold + 1; i++) {
			stateManager.storeGoRequest(3);
			stateManager.storeGoRequest(5);
		}
		assertFalse(stateManager.mustSkipExtraWaitingCalls());
	}

	@Test
	public void mustSkipExtraWaitingCalls_must_return_true_if_cabin_is_full() {
		stateManager.reset(0, 5, 3, 2);
		stateManager.storeGoRequest(1);
		stateManager.storeGoRequest(1);
		stateManager.storeGoRequest(0);
		assertTrue(stateManager.mustSkipExtraWaitingCalls());
	}

	@Test
	public void mustSkipExtraWaitingCalls_must_return_true_if_go_request_number_more_than_floors_numbers_devided_by_2() {
		final int floorsNumber = floorBoundaries.calculateFloorsNumber();
		final int floorsNumberThreshold = floorsNumber / 2;
		for (int i = 0; i < floorsNumberThreshold + 1; i++) {
			stateManager.storeGoRequest(3 + i);
		}
		assertTrue(stateManager.mustSkipExtraWaitingCalls());
	}
}
