package utils;

import static com.google.common.collect.Lists.newArrayList;
import static utils.Direction.DOWN;
import static utils.Direction.UP;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ElevatorState {

	private final FloorBoundaries floorBoundaries = new FloorBoundaries(0, 5);

	private final Collection<Call> waitingCalls;

	private final CabinState[] cabinsStates;

	public ElevatorState(int cabinCount, int lowerFloor, int higherFloor) {
		floorBoundaries.setRange(lowerFloor, higherFloor);
		this.waitingCalls = newArrayList();
		cabinsStates = new CabinState[cabinCount];
		initializeCabinsStates(cabinCount, lowerFloor, higherFloor);
	}

	public FloorBoundaries getFloorBoundaries() {
		return floorBoundaries;
	}

	public boolean isAtFirstFloor(int cabin) {
		return floorBoundaries.isAtFirstFloor(getCurrentFloor(cabin));
	}

	public boolean isAtLastFloor(int cabin) {
		return floorBoundaries.isAtLastFloor(getCurrentFloor(cabin));
	}

	public int getCabinCount() {
		return cabinsStates.length;
	}

	public void addWaitingCall(int atFloor, Direction to) {
		waitingCalls.add(new Call(atFloor, to));
	}

	public void removeWaitingCall(int atFloor, Direction to) {
		waitingCalls.remove(new Call(atFloor, to));
	}

	public Collection<Call> getWaitingCalls() {
		return waitingCalls;
	}

	public void addGoRequest(int cabin, int floorToGo) {
		cabinsStates[cabin].addGoRequest(floorToGo);
	}

	public void removeGoRequest(int cabin, int floorToGo) {
		cabinsStates[cabin].removeGoRequest(floorToGo);
	}

	public Collection<Integer> getGoRequests(int cabin) {
		return cabinsStates[cabin].getGoRequests();
	}

	public void incrementFloor(int cabin) {
		cabinsStates[cabin].incrementFloor();
	}

	public void decrementFloor(int cabin) {
		cabinsStates[cabin].decrementFloor();
	}

	public void setCurrentFloor(int cabin, int currentFloor) {
		cabinsStates[cabin].setCurrentFloor(currentFloor);
	}

	public int getCurrentFloor(int cabin) {
		return cabinsStates[cabin].getCurrentFloor();
	}

	public void setOpened(int cabin) {
		cabinsStates[cabin].setOpened(true);
	}

	public boolean isOpened(int cabin) {
		return cabinsStates[cabin].isOpened();
	}

	public void setClosed(int cabin) {
		cabinsStates[cabin].setOpened(false);
	}

	public boolean isClosed(int cabin) {
		return !cabinsStates[cabin].isOpened();
	}

	public void setCurrentDirection(int cabin, Direction newDirection) {
		cabinsStates[cabin].setCurrentDirection(newDirection);
	}

	public Direction getCurrentDirection(int cabin) {
		return cabinsStates[cabin].getCurrentDirection();
	}

	public int getRestingFloor(int cabin) {
		return cabinsStates[cabin].getRestingFloor();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(cabinsStates);
		result = prime * result + ((waitingCalls == null) ? 0 : waitingCalls.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ElevatorState other = (ElevatorState) obj;
		if (!Arrays.equals(cabinsStates, other.cabinsStates)) {
			return false;
		}
		if (waitingCalls == null) {
			if (other.waitingCalls != null) {
				return false;
			}
		} else if (!waitingCalls.equals(other.waitingCalls)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private void initializeCabinsStates(int cabinCount, int lowerFloor, int higherFloor) {
		double floorNumberForEachCabin = (higherFloor - lowerFloor) / (double) cabinCount;
		for (int cabin = 0; cabin < cabinCount; cabin++) {
			cabinsStates[cabin] = new CabinState();
			cabinsStates[cabin].setRestingFloor(calculateRestingFloor(lowerFloor, cabin, floorNumberForEachCabin));
			cabinsStates[cabin].setCurrentDirection(cabin % 2 == 0 ? UP : DOWN);
		}
	}

	private int calculateRestingFloor(int lowerFloor, int cabin, double floorNumberForEachCabin) {
		return lowerFloor + (int) (floorNumberForEachCabin * (cabin + 0.5) + 0.5);
	}
}
