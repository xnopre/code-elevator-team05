package utils;

import static com.google.common.collect.Lists.newArrayList;
import static utils.Direction.DOWN;
import static utils.Direction.UP;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ElevatorState {

	private final Collection<Call> waitingCalls;

	private final CabinState[] cabinsStates;

	public ElevatorState(int cabinCount) {
		this.waitingCalls = newArrayList();
		cabinsStates = new CabinState[cabinCount];
		for (int i = 0; i < cabinCount; i++) {
			cabinsStates[i] = new CabinState();
			cabinsStates[i].setMustGoAtMiddleFloor(i % 2 == 0);
			cabinsStates[i].setCurrentDirection(i % 2 == 0 ? UP : DOWN);
		}
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

	public boolean mustGoAtMiddleFloor(int cabin) {
		return cabinsStates[cabin].mustGoAtMiddleFloor();
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
