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
		currentState = ElevatorState.Builder.from(currentState).addWaitingCall(atFloor, to).get();
	}

	public void removeWaitingCall(int atFloor, Direction to) {
		currentState = ElevatorState.Builder.from(currentState).removeWaitingCall(atFloor, to).get();
	}

	public void storeGoRequest(int floorToGo) {
		currentState = ElevatorState.Builder.from(currentState).addGoRequest(floorToGo).get();
	}

	public void removeGoRequest(int floorToGo) {
		currentState = ElevatorState.Builder.from(currentState).removeGoRequest(floorToGo).get();
	}

	public ElevatorState getCurrentState() {
		return currentState;
	}

	public void incrementFloor() {
		if (isAtLastFloor())
			throw new UnreachableFloorException();
		currentState = ElevatorState.Builder.from(currentState).incrementFloor().get();
	}

	public void decrementFloor() {
		if (isAtFirstFloor())
			throw new UnreachableFloorException();
		currentState = ElevatorState.Builder.from(currentState).decrementFloor().get();
	}

	public boolean isAtFirstFloor() {
		return floorBoundaries.isAtFirstFloor(currentState.getCurrentFloor());
	}

	public boolean isAtLastFloor() {
		return floorBoundaries.isAtLastFloor(currentState.getCurrentFloor());
	}

	public void setOpened() {
		currentState = ElevatorState.Builder.from(currentState).setOpened().get();
	}

	public void setClosed() {
		currentState = ElevatorState.Builder.from(currentState).setClosed().get();
	}

	public void setCurrentDirection(Direction currentDirection) {
		currentState = ElevatorState.Builder.from(currentState).setCurrentDirection(currentDirection).get();
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
