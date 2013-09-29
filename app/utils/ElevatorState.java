package utils;

import java.util.ArrayList;
import java.util.Collection;

public class ElevatorState {

	private final Collection<ElevatorCall> waitingCalls;

	public ElevatorState(Collection<ElevatorCall> waitingCalls) {
		this.waitingCalls = waitingCalls;
	}

	public ElevatorState() {
		this(new ArrayList<ElevatorCall>());
	}

	public Collection<ElevatorCall> getWaitingCalls() {
		return waitingCalls;
	}

	public ElevatorState addWaitingCall(int atFloor, Direction to) {
		Collection<ElevatorCall> waitingCalls = new ArrayList<ElevatorCall>(this.waitingCalls);
		waitingCalls.add(new ElevatorCall(atFloor, to));
		return new ElevatorState(waitingCalls);
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

}
