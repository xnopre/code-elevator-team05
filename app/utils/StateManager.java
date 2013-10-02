package utils;

import java.util.Collection;

public class StateManager {

	public static final ElevatorState INITIAL_STATE = new ElevatorState();

	ElevatorState currentState = INITIAL_STATE;

	public void reset() {
		currentState = INITIAL_STATE;
	}

	public void storeCall(int atFloor, Direction to) {
		ensureNoPreviousCallAtThatFloorOrDie(atFloor);
		currentState = currentState.addWaitingCall(atFloor, to);
	}

	private void ensureNoPreviousCallAtThatFloorOrDie(int floor) {
		Collection<ElevatorCall> waitingCalls = currentState.getWaitingCalls();
		if (waitingCalls.contains(new ElevatorCall(floor, Direction.DOWN)))
			throw new OnlyOneCallPerFloorException(floor, waitingCalls);
		if (waitingCalls.contains(new ElevatorCall(floor, Direction.UP)))
			throw new OnlyOneCallPerFloorException(floor, waitingCalls);
	}

	public ElevatorState getCurrentState() {
		return currentState;
	}

}
