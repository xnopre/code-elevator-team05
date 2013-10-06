package utils;

import static utils.Command.NOTHING;
import static utils.Command.OPEN;

import java.util.Collection;

public class XavierCommandGenerator implements CommandGenerator {

	private final StateManager stateManager;

	public XavierCommandGenerator(StateManager stateManager) {
		this.stateManager = stateManager;
	}

	@Override
	public Command nextCommand() {
		if (isThereACallAtCurrentFloor(stateManager.getCurrentState().getCurrentFloor())) {
			return OPEN;
		}
		return NOTHING;
	}

	private boolean isThereACallAtCurrentFloor(int currentFloor) {
		final Collection<Call> waitingCalls = stateManager.getCurrentState().getWaitingCalls();
		for (Call call : waitingCalls) {
			if (call.floor == currentFloor) {
				return true;
			}
		}
		return false;
	}

}
