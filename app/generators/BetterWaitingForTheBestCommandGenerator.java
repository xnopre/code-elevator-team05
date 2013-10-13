package generators;

import static utils.Command.CLOSE;
import static utils.Command.NOTHING;
import static utils.Command.OPEN;
import static utils.Direction.DOWN;
import static utils.Direction.UP;

import java.util.Collection;

import play.Logger;
import utils.Call;
import utils.Command;
import utils.Direction;
import utils.StateManager;

public class BetterWaitingForTheBestCommandGenerator implements CommandGenerator {

	private final StateManager stateManager;

	public BetterWaitingForTheBestCommandGenerator(StateManager stateManager) {
		this.stateManager = stateManager;
	}

	@Override
	public Command nextCommand() {
		if (isOpened()) {
			if (thereIsACallAtCurrentFloorMatchingCurrentDirection() && threeLastCommandAreNotNothing()) {
				return storeCommandInHistory(NOTHING);
			}
			if (thereIsACallAtCurrentFloorMatchingCurrentDirection() && threeLastCommandAreNothing()) {
				Logger.warn("HACK to unlock strange situation ! ...");
			}
			stateManager.setClosed();
			return storeCommandInHistory(CLOSE);
		}
		if (thereIsACallAtCurrentFloorMatchingCurrentDirection() || thereIsAGoAtCurrentFloor()) {
			stateManager.setOpened();
			return storeCommandInHistory(OPEN);
		}
		if (isCurrentDirectionIs(UP)) {
			if (thereIsAGoUpstairs() || thereIsACallUpstairs()) {
				stateManager.incrementFloor();
				return storeCommandInHistory(Command.UP);
			}
			if (thereIsAGoDownstairs() || thereIsACallDownstairs()) {
				stateManager.decrementFloor();
				stateManager.setCurrentDirection(DOWN);
				return storeCommandInHistory(Command.DOWN);
			}
		}
		if (isCurrentDirectionIs(DOWN)) {
			if (thereIsAGoDownstairs() || thereIsACallDownstairs()) {
				stateManager.decrementFloor();
				return storeCommandInHistory(Command.DOWN);
			}
			if (thereIsAGoUpstairs() || thereIsACallUpstairs()) {
				stateManager.incrementFloor();
				stateManager.setCurrentDirection(UP);
				return storeCommandInHistory(Command.UP);
			}
		}
		return storeCommandInHistory(NOTHING);
	}

	private boolean isCurrentDirectionIs(Direction direction) {
		return stateManager.getCurrentState().getCurrentDirection() == direction;
	}

	private boolean threeLastCommandAreNothing() {
		return stateManager.areThreeLastCommandEqualTo(NOTHING);
	}

	private boolean threeLastCommandAreNotNothing() {
		return !threeLastCommandAreNothing();
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

	private boolean thereIsACallAtCurrentFloorMatchingCurrentDirection() {
		final Collection<Call> waitingCalls = stateManager.getCurrentState().getWaitingCalls();
		for (Call call : waitingCalls) {
			if (isSameFloorThantCurrentFloor(call) && isSameDirectionThanCurrentDirection(call)) {
				return true;
			}
		}
		return false;
	}

	private boolean isSameDirectionThanCurrentDirection(Call call) {
		return call.direction == stateManager.getCurrentState().getCurrentDirection();
	}

	private boolean isSameFloorThantCurrentFloor(Call call) {
		return call.floor == stateManager.getCurrentState().getCurrentFloor();
	}

	private boolean thereIsAGoAtCurrentFloor() {
		return stateManager.getCurrentState().getGoRequests().contains(stateManager.getCurrentState().getCurrentFloor());
	}

}
