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
		currentState = ElevatorState.Builder.from(currentState)
				.addWaitingCall(atFloor, to).get();
	}

	public ElevatorState getCurrentState() {
		return currentState;
	}

	public void incrementFloor() {
		currentState = ElevatorState.Builder.from(currentState)
				.incrementFloor().get();
	}

}
