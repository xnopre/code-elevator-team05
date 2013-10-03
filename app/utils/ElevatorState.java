package utils;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.common.collect.Lists;

public class ElevatorState {

	private final int currentFloor;

	private final Collection<Call> waitingCalls;

	public ElevatorState(Collection<Call> waitingCalls, int currentFloor) {
		this.waitingCalls = waitingCalls;
		this.currentFloor = currentFloor;
	}

	public ElevatorState() {
		this(new ArrayList<Call>(), 0);
	}

	public Collection<Call> getWaitingCalls() {
		return waitingCalls;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((waitingCalls == null) ? 0 : waitingCalls.hashCode());
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

	static class Builder {

		private final ElevatorState initialState;
		private final Collection<Call> newCalls = new ArrayList<Call>();
		private int incr = 0;

		private Builder(ElevatorState state) {
			this.initialState = state;
		}

		public static Builder from(ElevatorState state) {
			return new Builder(state);
		}

		public Builder addWaitingCall(int atFloor, Direction to) {
			newCalls.add(new Call(atFloor, to));
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

		public ElevatorState get() {
			final Collection newWaitingCalls = Lists.newArrayList(initialState
					.getWaitingCalls());
			newWaitingCalls.addAll(newCalls);
			return new ElevatorState(newWaitingCalls,
					initialState.getCurrentFloor() + incr);
		}

	}
}
