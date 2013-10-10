package utils;

import static utils.StateBuilderFactory.call;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.common.collect.Lists;

public class ElevatorState {

	private final int currentFloor;

	private final Collection<Call> waitingCalls;

	private final boolean opened;

	public ElevatorState() {
		this(0, false);
	}

	public ElevatorState(int floor, boolean opened, Call... calls) {
		currentFloor = floor;
		this.opened = opened;
		waitingCalls = Lists.newArrayList(calls);
	}

	public Collection<Call> getWaitingCalls() {
		return waitingCalls;
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
		private final Collection<Call> newCalls = new ArrayList<Call>();
		private int incr = 0;
		private boolean opened;
		private final List<Call> currentWaitingCalls;

		private Builder(ElevatorState state) {
			this.initialState = state;
			this.opened = state.isOpened();
			currentWaitingCalls = Lists.newArrayList(state.getWaitingCalls());
		}

		public static Builder from(ElevatorState state) {
			return new Builder(state);
		}

		public Builder addWaitingCall(int atFloor, Direction to) {
			currentWaitingCalls.add(new Call(atFloor, to));
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
			return new ElevatorState(initialState.getCurrentFloor() + incr, opened, currentWaitingCalls.toArray(new Call[0]));
		}

		public Builder removeWaitingCall(int atFloor, Direction to) {
			currentWaitingCalls.remove(call(atFloor, to));
			return this;
		}

	}

}
