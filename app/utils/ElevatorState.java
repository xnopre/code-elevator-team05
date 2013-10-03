package utils;

import java.util.ArrayList;
import java.util.Collection;

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

	public ElevatorState addWaitingCall(int atFloor, Direction to) {
		Collection<Call> waitingCalls = new ArrayList<Call>(
				this.waitingCalls);
		waitingCalls.add(new Call(atFloor, to));
		return new ElevatorState(waitingCalls, currentFloor);
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

}
