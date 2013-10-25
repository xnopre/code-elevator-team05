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
	public void ensure_remove_good_waiting_calls() {

		stateBuilderFactory.givenAnElevatorOpenedAtFloor(3).andWaitingCalls(call(3, DOWN), call(3, UP), call(4, UP), call(1, UP)).build();

		waitingCallAndGoRemover.removeAllCallsFromTheCurrentFloor();

		verify(mockStateManager).removeWaitingCall(3, UP);
		verify(mockStateManager).removeWaitingCall(3, DOWN);
		verify(mockStateManager, times(2)).removeWaitingCall(anyInt(), any(Direction.class));
	}

	@Test
	public void ensure_remove_good_gos() {

		stateBuilderFactory.givenAnElevatorOpenedAtFloor(3).andGoRequests(1, 2, 3, 5, 4, 3).build();

		waitingCallAndGoRemover.removeAllGosFromTheCurrentFloor();

		verify(mockStateManager, times(2)).removeGoRequest(3);
		verify(mockStateManager, times(2)).removeGoRequest(anyInt());
	}

}