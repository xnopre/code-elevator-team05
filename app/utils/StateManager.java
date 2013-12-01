package utils;

import java.util.HashSet;
import java.util.Set;

public class StateManager {

	private ElevatorState currentState = new ElevatorState();

	private final FloorBoundaries floorBoundaries;

	private int cabinSize;

	public StateManager(FloorBoundaries floorBoundaries) {
		this.floorBoundaries = floorBoundaries;
	}

	public void reset(int lowerFloor, int higherFloor, int cabinSize, int cabinCount) {
		currentState = new ElevatorState(0);
		floorBoundaries.setRange(lowerFloor, higherFloor);
		this.cabinSize = cabinSize;
	}

	public void storeWaitingCall(int atFloor, Direction to) {
		currentState.addWaitingCall(atFloor, to);
	}

	public void removeWaitingCall(int atFloor, Direction to) {
		currentState.removeWaitingCall(atFloor, to);
	}

	public void storeGoRequest(int floorToGo) {
		currentState.addGoRequest(floorToGo);
	}

	public void removeGoRequest(int floorToGo) {
		currentState.removeGoRequest(floorToGo);
	}

	public ElevatorState getCurrentState() {
		return currentState;
	}

	public void incrementFloor() {
		if (isAtLastFloor()) {
			throw new UnreachableFloorException();
		}
		currentState.incrementFloor();
	}

	public void decrementFloor() {
		if (isAtFirstFloor()) {
			throw new UnreachableFloorException();
		}
		currentState.decrementFloor();
	}

	public boolean isAtFirstFloor() {
		return floorBoundaries.isAtFirstFloor(currentState.getCurrentFloor());
	}

	public boolean isAtLastFloor() {
		return floorBoundaries.isAtLastFloor(currentState.getCurrentFloor());
	}

	public void setOpened() {
		currentState.setOpened();
	}

	public void setClosed() {
		currentState.setClosed();
	}

	public void setCurrentDirection(Direction currentDirection) {
		currentState.setCurrentDirection(currentDirection);
	}

	public FloorBoundaries getFloorBoundaries() {
		return floorBoundaries;
	}

	public boolean mustSkipExtraWaitingCalls() {
		if (thereIsAsManyPassengersAsCabinSize()) {
			return true;
		}
		if (thereAreMoreUniqueGoRequestThanFloorNumberDividedBy2()) {
			return true;
		}
		return false;
	}

	public int getCabinSize() {
		return cabinSize;
	}

	public boolean isCabinFull() {
		return thereIsAsManyPassengersAsCabinSize();
	}

	// privates ------------------------------------

	private boolean thereIsAsManyPassengersAsCabinSize() {
		return countCurrentPassengers() >= cabinSize;
	}

	private int countCurrentPassengers() {
		return currentState.getGoRequests().size();
	}

	private boolean thereAreMoreUniqueGoRequestThanFloorNumberDividedBy2() {
		final int floorsNumber = floorBoundaries.calculateFloorsNumber();
		final int floorsNumberThreshold = floorsNumber / 2;
		final boolean test = countUniqueGoRequests() > floorsNumberThreshold;
		return test;
	}

	private int countUniqueGoRequests() {
		Set<Integer> uniqueGoRequests = new HashSet<Integer>();
		for (Integer go : currentState.getGoRequests()) {
			uniqueGoRequests.add(go);
		}
		return uniqueGoRequests.size();
	}

}
