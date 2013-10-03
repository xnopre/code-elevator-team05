package utils;

import java.util.Collection;

public class OnlyOneCallPerFloorException extends RuntimeException {

	public OnlyOneCallPerFloorException(int floor,
			Collection<Call> waitingCalls) {
		super("A call is already registered at floor " + floor + " : "
				+ waitingCalls);
	}

}
