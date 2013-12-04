package utils;

public class StateManager {

	private ElevatorState currentState;

	private int cabinSize;

	public StateManager(int lowerFloor, int higherFloor, int cabinSize, int cabinCount) {
		reset(lowerFloor, higherFloor, cabinSize, cabinCount);
	}

	public void reset(int lowerFloor, int higherFloor, int cabinSize, int cabinCount) {
		currentState = new ElevatorState(cabinCount, lowerFloor, higherFloor);
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
		return currentState.isAtFirstFloor(cabin);
	}

	public boolean isAtLastFloor(int cabin) {
		return currentState.isAtLastFloor(cabin);
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
		return currentState.getFloorBoundaries();
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

	public int getRestingFloor(int cabin) {
		return currentState.getRestingFloor(cabin);
	}

	// privates ------------------------------------

	private boolean thereIsAsManyPassengersAsCabinSize(int cabin) {
		return countCurrentPassengers(cabin) >= cabinSize;
	}

	private int countCurrentPassengers(int cabin) {
		return currentState.getGoRequests(cabin).size();
	}

}
