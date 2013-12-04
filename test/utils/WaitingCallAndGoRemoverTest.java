package utils;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static utils.Direction.DOWN;
import static utils.Direction.UP;
import static utils.StateBuilderFactory.call;

import org.junit.Test;

public class WaitingCallAndGoRemoverTest {

	private final StateManager mockStateManager = mock(StateManager.class);

	private final WaitingCallAndGoRemover waitingCallAndGoRemover = new WaitingCallAndGoRemover(mockStateManager);

	private final StateBuilderFactory stateBuilderFactory = new StateBuilderFactory(mockStateManager);

	@Test
	public void ensure_remove_good_waiting_calls_for_direction_up() {

		stateBuilderFactory.givenAnElevatorOpenedAtFloor(3).withDirection(UP).andWaitingCalls(call(3, DOWN), call(3, UP), call(4, UP), call(1, UP)).build();

		waitingCallAndGoRemover.removeAllCallsFromTheCurrentFloor(UP);

		verify(mockStateManager).removeWaitingCall(3, UP);
		verify(mockStateManager, times(1)).removeWaitingCall(anyInt(), any(Direction.class));
	}

	@Test
	public void ensure_remove_good_waiting_calls_for_direction_down() {

		stateBuilderFactory.givenAnElevatorOpenedAtFloor(3).withDirection(DOWN).andWaitingCalls(call(3, DOWN), call(3, UP), call(4, UP), call(1, UP)).build();

		waitingCallAndGoRemover.removeAllCallsFromTheCurrentFloor(DOWN);

		verify(mockStateManager).removeWaitingCall(3, DOWN);
		verify(mockStateManager, times(1)).removeWaitingCall(anyInt(), any(Direction.class));
	}

	@Test
	public void ensure_remove_good_gos_for_direction_up() {

		stateBuilderFactory.givenAnElevatorOpenedAtFloor(3).andGoRequests(1, 2, 3, 5, 4, 3).build();

		waitingCallAndGoRemover.removeAllGosFromTheCurrentFloor(0);

		verify(mockStateManager, times(2)).removeGoRequest(0, 3);
		verify(mockStateManager, times(2)).removeGoRequest(anyInt(), anyInt());
	}

	@Test
	public void ensure_removeGoRequest_remove_good_data() {
		stateBuilderFactory.givenAnElevatorOpenedAtFloor(3).andGoRequests(1, 2, 3, 5, 4, 3).build();

		waitingCallAndGoRemover.removeGoRequest(0, 3);

		verify(mockStateManager, times(1)).removeGoRequest(0, 3);
		verify(mockStateManager, times(1)).removeGoRequest(anyInt(), anyInt());

	}

}
