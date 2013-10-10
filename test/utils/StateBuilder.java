package utils;

import static org.mockito.Mockito.when;

public class StateBuilder {

	private final int floor;

	private final boolean opened;

	private final StateManager mockStateManager;

	public StateBuilder(StateManager mockStateManager, int floor, boolean opened) {
		this.mockStateManager = mockStateManager;
		this.floor = floor;
		this.opened = opened;
	}

	public void andNoWaitingCalls() {
		andWaitingCalls();
	}

	public void andWaitingCalls(Call... calls) {
		StateBuilder.createStateAndDoReturnItByStateManager(mockStateManager, floor, opened, calls);
	}

	public static void createStateAndDoReturnItByStateManager(StateManager mockStateManager, int floor, boolean opened, Call... calls) {
		final ElevatorState state = new ElevatorState(floor, opened, calls);
		when(mockStateManager.getCurrentState()).thenReturn(state);

	}

	public StateBuilder isGoingUp() {
		return this;
	}

	public StateBuilder isGoingDown() {
		return this;
	}

}