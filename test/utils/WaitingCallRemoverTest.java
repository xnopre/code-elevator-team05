package utils;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static utils.Direction.DOWN;
import static utils.Direction.UP;
import static utils.StateBuilderFactory.call;

import org.junit.Test;

public class WaitingCallRemoverTest {

	private final StateManager mockStateManager = mock(StateManager.class);

	private final WaitingCallRemover waitingCallRemover = new WaitingCallRemover(mockStateManager);

	private final StateBuilderFactory stateBuilderFactory = new StateBuilderFactory(mockStateManager);

	@Test
	public void ensure_remove_good_waiting_call() {

		stateBuilderFactory.givenAnElevatorOpenedAtFloor(3).andWaitingCalls(call(3, DOWN), call(3, UP), call(4, UP), call(1, UP)).build();

		waitingCallRemover.removeAllCallsFromTheCurrentFloor();

		verify(mockStateManager).removeWaitingCall(3, UP);
		verify(mockStateManager).removeWaitingCall(3, DOWN);
		verify(mockStateManager, times(2)).removeWaitingCall(anyInt(), any(Direction.class));
	}

	@Test
	public void throw_exception_if_there_is_no_matching_call() {

		stateBuilderFactory.givenAnElevatorOpenedAtFloor(3).andWaitingCalls(call(5, UP), call(4, UP), call(1, UP)).build();

		try {
			waitingCallRemover.removeAllCallsFromTheCurrentFloor();
			fail("Must throw an exception");
		} catch (ElevatorException e) {
			assertEquals("No call found for current floor", e.getMessage());
		}
	}

}
