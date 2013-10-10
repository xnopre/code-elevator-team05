package generators;

import static utils.Command.CLOSE;
import static utils.Command.NOTHING;
import static utils.Command.OPEN;

import java.util.Collection;

import utils.Call;
import utils.Command;
import utils.StateManager;

public class BetterWaitingForTheBestCommandGenerator implements CommandGenerator {

	private final StateManager stateManager;

	public BetterWaitingForTheBestCommandGenerator(StateManager stateManager) {
		this.stateManager = stateManager;
	}

	@Override
	public Command nextCommand() {
		if (isOpened()) {
			if (isThereACallAtCurrentFloor()) {
				return NOTHING;
			}
			return CLOSE;
		}
		if (isThereACallAtCurrentFloor()) {
			stateManager.setOpened();
			return OPEN;
		}
		return NOTHING;
	}

	private boolean isOpened() {
		return stateManager.getCurrentState().isOpened();
	}

	private boolean isThereACallAtCurrentFloor() {
		final Collection<Call> waitingCalls = stateManager.getCurrentState().getWaitingCalls();
		for (Call call : waitingCalls) {
			if (call.floor == stateManager.getCurrentState().getCurrentFloor()) {
				return true;
			}
		}
		return false;
	}

}
