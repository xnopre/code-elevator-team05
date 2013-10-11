package utils;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

public class StateBuilderForTest {

	private final int floor;

	private final boolean opened;

	private final StateManager mockStateManager;

	public StateBuilderForTest(StateManager mockStateManager, int floor, boolean opened) {
		this.mockStateManager = mockStateManager;
		this.floor = floor;
		this.opened = opened;
	}

	public void andNoWaitingCalls() {
		andWaitingCalls();
	}

	public void andWaitingCalls(Call... calls) {
		createStateAndDoReturnItByStateManager(mockStateManager, floor, opened, calls);
	}

	public void andGoRequests(Integer... floorToGo) {
		final ElevatorState state = new ElevatorState(floor, opened, new ArrayList<Call>(), newArrayList(floorToGo));
		when(mockStateManager.getCurrentState()).thenReturn(state);
	}

	public static void createStateAndDoReturnItByStateManager(StateManager mockStateManager, int floor, boolean opened, Call... calls) {
		final ElevatorState state = new ElevatorState(floor, opened, newArrayList(calls), new ArrayList<Integer>());
		when(mockStateManager.getCurrentState()).thenReturn(state);

	}

}