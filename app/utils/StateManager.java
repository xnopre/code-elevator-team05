package utils;

public class StateManager {

	public static final ElevatorState INITIAL_STATE = new ElevatorState();

	ElevatorState currentState = INITIAL_STATE;

	public void reset() {
		currentState = INITIAL_STATE;
	}

	public void storeCall(int atFloor, Direction to) {
		currentState = currentState.addWaitingCall(atFloor, to);
	}

	public ElevatorState getCurrentState() {
		return currentState;
	}

}
