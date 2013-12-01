package utils;

import static com.google.common.collect.Lists.newArrayList;
import static utils.Direction.UP;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ElevatorState {

	private int currentFloor;

	private final Collection<Call> waitingCalls;

	private final Collection<Integer> goRequests;

	private boolean opened;

	private Direction currentDirection;

	public ElevatorState() {
		this(0);
	}

	public ElevatorState(int currentFloor) {
		this(currentFloor, false, UP, new ArrayList<Call>(), new ArrayList<Integer>());
	}

	public ElevatorState(int currentFloor, boolean opened, Direction currentDirection, Collection<Call> waitingCalls, Collection<Integer> goRequests) {
		this.currentFloor = currentFloor;
		this.opened = opened;
		this.currentDirection = currentDirection;
		this.waitingCalls = newArrayList(waitingCalls);
		this.goRequests = newArrayList(goRequests);
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

	public void addGoRequest(int floorToGo) {
		goRequests.add(floorToGo);
	}

	public void removeGoRequest(int floorToGo) {
		goRequests.remove(floorToGo);
	}

	public Collection<Integer> getGoRequests() {
		return goRequests;
	}

	public void incrementFloor() {
		currentFloor += 1;
	}

	public void decrementFloor() {
		currentFloor -= 1;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public void setOpened() {
		opened = true;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setClosed() {
		opened = false;
	}

	public boolean isClosed() {
		return !opened;
	}

	public void setCurrentDirection(Direction newDirection) {
		currentDirection = newDirection;
	}

	public Direction getCurrentDirection() {
		return currentDirection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentDirection == null) ? 0 : currentDirection.hashCode());
		result = prime * result + currentFloor;
		result = prime * result + ((goRequests == null) ? 0 : goRequests.hashCode());
		result = prime * result + (opened ? 1231 : 1237);
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

	// public static class Builder {
	//
	// private final ElevatorState initialState;
	// private int incr = 0;
	// private boolean opened;
	// private final List<Call> currentWaitingCalls;
	// private final List<Integer> currentGoRequests;
	// private Direction currentDirection;
	//
	// private Builder(ElevatorState state) {
	// this.initialState = state;
	// this.opened = state.isOpened();
	// this.currentDirection = state.getCurrentDirection();
	// currentWaitingCalls = newArrayList(state.getWaitingCalls());
	// currentGoRequests = newArrayList(state.getGoRequests());
	// }
	//
	// public static Builder from(ElevatorState state) {
	// return new Builder(state);
	// }
	//
	// public Builder addWaitingCall(int atFloor, Direction to) {
	// currentWaitingCalls.add(new Call(atFloor, to));
	// return this;
	// }
	//
	// public Builder removeWaitingCall(int atFloor, Direction to) {
	// currentWaitingCalls.remove(new Call(atFloor, to));
	// return this;
	// }
	//
	// public Builder addGoRequest(int floorToGo) {
	// currentGoRequests.add(floorToGo);
	// return this;
	// }
	//
	// public Builder removeGoRequest(int floorToGo) {
	// final int index = currentGoRequests.indexOf(floorToGo);
	// if (index != -1) {
	// currentGoRequests.remove(index);
	// }
	// return this;
	// }
	//
	// public Builder incrementFloor() {
	// incr += 1;
	// return this;
	// }
	//
	// public Builder decrementFloor() {
	// incr -= 1;
	// return this;
	// }
	//
	// public Builder setClosed() {
	// opened = false;
	// return this;
	// }
	//
	// public Builder setOpened() {
	// opened = true;
	// return this;
	// }
	//
	// public Builder setCurrentDirection(Direction currentDirection) {
	// this.currentDirection = currentDirection;
	// return this;
	// }
	//
	// public ElevatorState get() {
	// return new ElevatorState(initialState.getCurrentFloor() + incr, opened,
	// currentDirection, currentWaitingCalls, currentGoRequests);
	// }
	//
	// }

}
