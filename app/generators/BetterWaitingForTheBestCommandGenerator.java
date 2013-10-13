package generators;

import static utils.Command.CLOSE;
import static utils.Command.NOTHING;
import static utils.Command.OPEN;

import java.util.Collection;

import play.Logger;
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
			if (thereIsACallAtCurrentFloor() && !stateManager.areThreeLastCommandEqualTo(NOTHING)) {
				return storeCommandInHistory(NOTHING);
			}
			if (thereIsACallAtCurrentFloor() && stateManager.areThreeLastCommandEqualTo(NOTHING)) {
				Logger.warn("HASK to unlock strange situation ! ...");
			}
			stateManager.setClosed();
			return storeCommandInHistory(CLOSE);
		}
		if (thereIsACallAtCurrentFloor() || thereIsAGoAtCurrentFloor()) {
			stateManager.setOpened();
			return storeCommandInHistory(OPEN);
		}
		if (thereIsAGoUpstairs() || thereIsACallUpstairs()) {
			stateManager.incrementFloor();
			return storeCommandInHistory(Command.UP);
		}
		if (thereIsAGoDownstairs() || thereIsACallDownstairs()) {
			stateManager.decrementFloor();
			return storeCommandInHistory(Command.DOWN);
		}
		return storeCommandInHistory(NOTHING);
	}

	private Command storeCommandInHistory(Command command) {
		stateManager.storeCommandInHistory(command);
		return command;
	}

	private boolean thereIsAGoUpstairs() {
		final int currentFloor = stateManager.getCurrentState().getCurrentFloor();
		for (Integer goRequest : stateManager.getCurrentState().getGoRequests()) {
			if (goRequest > currentFloor) {
				return true;
			}
		}
		return false;
	}

	private boolean thereIsACallUpstairs() {
		final int currentFloor = stateManager.getCurrentState().getCurrentFloor();
		for (Call call : stateManager.getCurrentState().getWaitingCalls()) {
			if (call.floor > currentFloor) {
				return true;
			}
		}
		return false;
	}

	private boolean thereIsAGoDownstairs() {
		final int currentFloor = stateManager.getCurrentState().getCurrentFloor();
		for (Integer goRequest : stateManager.getCurrentState().getGoRequests()) {
			if (goRequest < currentFloor) {
				return true;
			}
		}
		return false;
	}

	private boolean thereIsACallDownstairs() {
		final int currentFloor = stateManager.getCurrentState().getCurrentFloor();
		for (Call call : stateManager.getCurrentState().getWaitingCalls()) {
			if (call.floor < currentFloor) {
				return true;
			}
		}
		return false;
	}

	private boolean isOpened() {
		return stateManager.getCurrentState().isOpened();
	}

	private boolean thereIsACallAtCurrentFloor() {
		final Collection<Call> waitingCalls = stateManager.getCurrentState().getWaitingCalls();
		for (Call call : waitingCalls) {
			if (call.floor == stateManager.getCurrentState().getCurrentFloor()) {
				return true;
			}
		}
		return false;
	}

	private boolean thereIsAGoAtCurrentFloor() {
		return stateManager.getCurrentState().getGoRequests().contains(stateManager.getCurrentState().getCurrentFloor());
	}

}
