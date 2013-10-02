package utils;

import java.util.Collection;

public class OnlyOneCallPerFloorException extends RuntimeException {

	public OnlyOneCallPerFloorException(int floor,
			Collection<ElevatorCall> waitingCalls) {
		super("A call is already registered at floor " + floor + " : "
				+ waitingCalls);
	}

}
