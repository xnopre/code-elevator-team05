package utils;

public class StateManager {

	private ElevatorState currentState;

	private final FloorBoundaries floorBoundaries = new FloorBoundaries(0, 5);

	private int cabinSize;

	public StateManager(int lowerFloor, int higherFloor, int cabinSize, int cabinCount) {
		reset(lowerFloor, higherFloor, cabinSize, cabinCount);
	}

	public void reset(int lowerFloor, int higherFloor, int cabinSize, int cabinCount) {
		currentState = new ElevatorState(cabinCount);
		floorBoundaries.setRange(lowerFloor, higherFloor);
		this.cabinSize = cabinSize;
	}

	public void storeWaitingCall(int atFloor, Direction to) {
		currentState.addWaitingCall(atFloor, to);
	}

	public void removeWaitingCall(int atFloor, Direction to) {
		currentState.removeWaitingCall(atFloor, to);
	}

	public void storeGoRequest(int cabin, int floorToGo) {
		currentState.addGoRequest(cabin, floorToGo);
	}

	public void removeGoRequest(int cabin, int floorToGo) {
		currentState.removeGoRequest(cabin, floorToGo);
	}

	public ElevatorState getCurrentState() {
		return currentState;
	}

	public void incrementFloor(int cabin) {
		if (isAtLastFloor(cabin)) {
			throw new UnreachableFloorException();
		}
		currentState.incrementFloor(cabin);
	}

	public void decrementFloor(int cabin) {
		if (isAtFirstFloor(cabin)) {
			throw new UnreachableFloorException();
		}
		currentState.decrementFloor(cabin);
	}

	public boolean isAtFirstFloor(int cabin) {
		return floorBoundaries.isAtFirstFloor(currentState.getCurrentFloor(cabin));
	}

	public boolean isAtLastFloor(int cabin) {
		return floorBoundaries.isAtLastFloor(currentState.getCurrentFloor(cabin));
	}

	public void setOpened(int cabin) {
		currentState.setOpened(cabin);
	}

	public void setClosed(int cabin) {
		currentState.setClosed(cabin);
	}

	public void setCurrentDirection(int cabin, Direction currentDirection) {
		currentState.setCurrentDirection(cabin, currentDirection);
	}

	public FloorBoundaries getFloorBoundaries() {
		return floorBoundaries;
	}

	public int getCabinCount() {
		return currentState.getCabinCount();
	}

	public int getCabinSize() {
		return cabinSize;
	}

	public boolean isCabinFull(int cabin) {
		return thereIsAsManyPassengersAsCabinSize(cabin);
	}

	// privates ------------------------------------

	private boolean thereIsAsManyPassengersAsCabinSize(int cabin) {
		return countCurrentPassengers(cabin) >= cabinSize;
	}

	private int countCurrentPassengers(int cabin) {
		return currentState.getGoRequests(cabin).size();
	}

}
