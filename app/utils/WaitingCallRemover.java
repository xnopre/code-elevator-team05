package utils;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class WaitingCallRemover {

	private final StateManager statemanager;

	public WaitingCallRemover(StateManager statemanager) {
		this.statemanager = statemanager;
	}

	public void removeAllCallsFromTheCurrentFloor() {

		final Collection<Call> matchedCalls = Collections2.filter(statemanager.getCurrentState().getWaitingCalls(), new Predicate<Call>() {

			@Override
			public boolean apply(@Nullable Call call) {
				return callWasMadeFromCurrentFloor(call);
			}

			private boolean callWasMadeFromCurrentFloor(Call call) {
				return call.floor == statemanager.getCurrentState().getCurrentFloor();
			}
		});

		final Iterator<Call> iterator = matchedCalls.iterator();
		if (iterator.hasNext()) {
			while (iterator.hasNext()) {
				final Call call = iterator.next();
				statemanager.removeWaitingCall(call.floor, call.direction);
			}
		} else {
			throw new ElevatorException("No call found for current floor");
		}
	}
}
