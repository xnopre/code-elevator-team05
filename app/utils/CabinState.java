package utils;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

public class CabinState {

	private int currentFloor = 0;

	private final Collection<Integer> goRequests;

	private boolean opened = false;

	private Direction currentDirection;

	private boolean mustGoAtMiddleFloor;

	public CabinState() {
		goRequests = newArrayList();
	}

	public void incrementFloor() {
		currentFloor++;
	}

	public void decrementFloor() {
		currentFloor--;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public Direction getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(Direction newDirection) {
		currentDirection = newDirection;
	}

	public void addGoRequest(int floorToGo) {
		goRequests.add(floorToGo);
	}

	public void removeGoRequest(int floorToGo) {
		goRequests.remove(floorToGo);
	}

	public Collection<Integer> getGoRequests() {
		return goRequests;
	}

	public boolean mustGoAtMiddleFloor() {
		return mustGoAtMiddleFloor;
	}

	public void setMustGoAtMiddleFloor(boolean mustGoAtMiddleFloor) {
		this.mustGoAtMiddleFloor = mustGoAtMiddleFloor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentDirection == null) ? 0 : currentDirection.hashCode());
		result = prime * result + currentFloor;
		result = prime * result + ((goRequests == null) ? 0 : goRequests.hashCode());
		result = prime * result + (opened ? 1231 : 1237);
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
		CabinState other = (CabinState) obj;
		if (currentDirection != other.currentDirection) {
			return false;
		}
		if (currentFloor != other.currentFloor) {
			return false;
		}
		if (goRequests == null) {
			if (other.goRequests != null) {
				return false;
			}
		} else if (!goRequests.equals(other.goRequests)) {
			return false;
		}
		if (opened != other.opened) {
			return false;
		}
		return true;
	}

}
