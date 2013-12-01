package utils;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class WaitingCallAndGoRemover {

	private final StateManager statemanager;

	public WaitingCallAndGoRemover(StateManager statemanager) {
		this.statemanager = statemanager;
	}

	public void removeAllCallsFromTheCurrentFloor(final Direction direction) {

		final Collection<Call> matchedCalls = Collections2.filter(statemanager.getCurrentState().getWaitingCalls(), new Predicate<Call>() {

			@Override
			public boolean apply(@Nullable Call call) {
				return callWasMadeFromCurrentFloor(call);
			}

			private boolean callWasMadeFromCurrentFloor(Call call) {
				return isSameAsCurrentFloor(call) && isSameDirection(call);
			}

			private boolean isSameDirection(Call call) {
				return call.direction == direction;
			}

			private boolean isSameAsCurrentFloor(Call call) {
				int cabinCount = statemanager.getCurrentState().getCabinCount();
				for (int cabin = 0; cabin < cabinCount; cabin++) {
					if (call.floor == statemanager.getCurrentState().getCurrentFloor(cabin)) {
						return true;
					}
				}
				return false;
			}
		});

		final Iterator<Call> iterator = matchedCalls.iterator();
		while (iterator.hasNext()) {
			final Call call = iterator.next();
			statemanager.removeWaitingCall(call.floor, call.direction);
		}
	}

	public void removeAllGosFromTheCurrentFloor(final int cabin) {
		final Collection<Integer> matchedGos = Collections2.filter(statemanager.getCurrentState().getGoRequests(cabin), new Predicate<Integer>() {

			@Override
			public boolean apply(@Nullable Integer go) {
				return goWasMadeForCurrentFloor(go);
			}

			private boolean goWasMadeForCurrentFloor(Integer go) {
				return go == statemanager.getCurrentState().getCurrentFloor(cabin);
			}
		});

		final Iterator<Integer> iterator = matchedGos.iterator();
		while (iterator.hasNext()) {
			final Integer go = iterator.next();
			statemanager.removeGoRequest(cabin, go);
		}
	}
}
