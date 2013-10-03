package utils;


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
		currentState = currentState.addWaitingCall(atFloor, to);
	}

	public ElevatorState getCurrentState() {
		return currentState;
	}

}
