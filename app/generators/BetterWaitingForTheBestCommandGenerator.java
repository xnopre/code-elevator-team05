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
			if (thereIsACallAtCurrentFloor()) {
				return NOTHING;
			}
			stateManager.setClosed();
			return CLOSE;
		}
		if (thereIsACallAtCurrentFloor() || thereIsAGoAtCurrentFloor()) {
			stateManager.setOpened();
			return OPEN;
		}
		if (thereIsAGoUpstairs() || thereIsACallUpstairs()) {
			stateManager.incrementFloor();
			return Command.UP;
		}
		if (thereIsAGoDownstairs() || thereIsACallDownstairs()) {
			stateManager.decrementFloor();
			return Command.DOWN;
		}
		return NOTHING;
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
