package utils;

import static utils.Direction.DOWN;
import static utils.Direction.UP;

import java.util.Collection;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class WaitingCallRemover {

	private final StateManager statemanager;

	public WaitingCallRemover(StateManager statemanager) {
		this.statemanager = statemanager;
	}

	public void removeOneCallFromCurrentFloorToGoAtFloor(final int floorToGo) {

		final Collection<Call> matchedCalls = Collections2.filter(statemanager.getCurrentState().getWaitingCalls(), new Predicate<Call>() {

			@Override
			public boolean apply(@Nullable Call call) {
				return callWasMadeFromCurrentFloor(call) && callMatchesDirectionWithFloorToGo(call);
			}

			private boolean callMatchesDirectionWithFloorToGo(Call call) {
				return call.direction == calculateDirectionForFloorToGo();
			}

			private boolean callWasMadeFromCurrentFloor(Call call) {
				return call.floor == statemanager.getCurrentState().getCurrentFloor();
			}

			private Direction calculateDirectionForFloorToGo() {
				return floorToGo < statemanager.getCurrentState().getCurrentFloor() ? DOWN : UP;
			}
		});

		final Call call = matchedCalls.iterator().next();
		statemanager.removeWaitingCall(call.floor, call.direction);
	}
}
