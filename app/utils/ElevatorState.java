package utils;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ElevatorState {

	private final int currentFloor;

	private final Collection<Call> waitingCalls;

	private final Collection<Integer> goRequests;

	private final boolean opened;

	public ElevatorState() {
		this(0, false, new ArrayList<Call>(), new ArrayList<Integer>());
	}

	public ElevatorState(int floor, boolean opened, Collection<Call> waitingCalls, Collection<Integer> goRequests) {
		currentFloor = floor;
		this.opened = opened;
		this.waitingCalls = newArrayList(waitingCalls);
		this.goRequests = newArrayList(goRequests);
	}

	public Collection<Call> getWaitingCalls() {
		return waitingCalls;
	}

	public Collection<Integer> getGoRequests() {
		return goRequests;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public boolean isOpened() {
		return opened;
	}

	public boolean isClosed() {
		return !opened;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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

	public static class Builder {

		private final ElevatorState initialState;
		private int incr = 0;
		private boolean opened;
		private final List<Call> currentWaitingCalls;
		private final List<Integer> currentGoRequests;

		private Builder(ElevatorState state) {
			this.initialState = state;
			this.opened = state.isOpened();
			currentWaitingCalls = newArrayList(state.getWaitingCalls());
			currentGoRequests = newArrayList(state.getGoRequests());
		}

		public static Builder from(ElevatorState state) {
			return new Builder(state);
		}

		public Builder addWaitingCall(int atFloor, Direction to) {
			currentWaitingCalls.add(new Call(atFloor, to));
			return this;
		}

		public Builder removeWaitingCall(int atFloor, Direction to) {
			currentWaitingCalls.remove(new Call(atFloor, to));
			return this;
		}

		public Builder addGoRequest(int floorToGo) {
			currentGoRequests.add(floorToGo);
			return this;
		}

		public Builder removeGoRequest(int floorToGo) {
			currentGoRequests.remove(currentGoRequests.indexOf(floorToGo));
			return this;
		}

		public Builder incrementFloor() {
			incr += 1;
			return this;
		}

		public Builder decrementFloor() {
			incr -= 1;
			return this;
		}

		public Builder setClosed() {
			opened = false;
			return this;
		}

		public Builder setOpened() {
			opened = true;
			return this;
		}

		public ElevatorState get() {
			return new ElevatorState(initialState.getCurrentFloor() + incr, opened, currentWaitingCalls, currentGoRequests);
		}

	}

}
