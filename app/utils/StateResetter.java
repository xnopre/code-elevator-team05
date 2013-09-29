package utils;

public class StateResetter {

	private final ElevatorState elevatorState;

	public StateResetter(ElevatorState elevatorState) {
		this.elevatorState = elevatorState;
	}

	public void reset() {
		elevatorState.currentFloor = 0;
		elevatorState.currentUsers = 0;
	}

}
