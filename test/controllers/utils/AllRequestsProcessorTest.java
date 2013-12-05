package controllers.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import generators.CommandGenerator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import utils.ElevatorState;
import utils.StateManager;
import utils.WaitingCallAndGoRemover;

public class AllRequestsProcessorTest {

	private StateManager mockStateManager;
	private WaitingCallAndGoRemover mockWaitingCallAndGoRemover;
	private CommandGenerator mockElevatorCommandGenerator;
	private AllRequestsProcessor allRequestsProcessor;
	private ElevatorState mockElevatorState;

	@Before
	public void setup() {
		mockStateManager = mock(StateManager.class);
		mockWaitingCallAndGoRemover = mock(WaitingCallAndGoRemover.class);
		mockElevatorCommandGenerator = mock(CommandGenerator.class);
		allRequestsProcessor = new AllRequestsProcessor(mockStateManager, mockWaitingCallAndGoRemover, mockElevatorCommandGenerator);
		mockElevatorState = mock(ElevatorState.class);
		when(mockStateManager.getCurrentState()).thenReturn(mockElevatorState);
	}

	@Test
	@Ignore
	public void request_userHasExited_must_remove_one_go() {
		when(mockElevatorState.getCurrentFloor(3)).thenReturn(5);

		allRequestsProcessor.userHasExited(3);

		verify(mockWaitingCallAndGoRemover).removeOneGoRequest(3);
	}

	@Test
	public void request_userHasExited_must_do_nothing() {

		allRequestsProcessor.userHasExited(3);

		verifyZeroInteractions(mockElevatorCommandGenerator);
		verifyZeroInteractions(mockStateManager);
		verifyZeroInteractions(mockWaitingCallAndGoRemover);
	}

	@Test
	@Ignore
	public void request_go_must_remove_one_waiting_call() {
		allRequestsProcessor.go(3, 5);

		verify(mockWaitingCallAndGoRemover).removeOneCallFromCurrentFloor(3, 5);
	}

	@Test
	public void request_go_must_do_nothing() {
		allRequestsProcessor.go(3, 5);

		verify(mockStateManager).storeGoRequest(3, 5);
		verify(mockStateManager).getCurrentState();
		verifyNoMoreInteractions(mockStateManager);
		verifyZeroInteractions(mockElevatorCommandGenerator);
		verifyZeroInteractions(mockWaitingCallAndGoRemover);
	}

}
