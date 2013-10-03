package utils;

import java.util.Collection;

public class StateManager {

	public static final ElevatorState INITIAL_STATE = new ElevatorState();

	private ElevatorState currentState = INITIAL_STATE;

	private final FloorBoundaries floorBoundaries;

	public StateManager(FloorBoundaries floorBoundaries) {
		this.floorBoundaries = floorBoundaries;
	}

	public void reset() {
		currentState = INITIAL_STATE;
	}

	public void storeCall(int atFloor, Direction to) {
		ensureNoPreviousCallAtThatFloorOrDie(atFloor);
		currentState = currentState.addWaitingCall(atFloor, to);
	}

	private void ensureNoPreviousCallAtThatFloorOrDie(int floor) {
		Collection<Call> waitingCalls = currentState.getWaitingCalls();
		if (waitingCalls.contains(new Call(floor, Direction.DOWN)))
			throw new OnlyOneCallPerFloorException(floor, waitingCalls);
		if (waitingCalls.contains(new Call(floor, Direction.UP)))
			throw new OnlyOneCallPerFloorException(floor, waitingCalls);
	}

	public ElevatorState getCurrentState() {
		return currentState;
	}

}
