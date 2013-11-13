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
import utils.WaitingCallAndGoRemover;

public class BetterWaitingForTheBestCommandGenerator implements CommandGenerator {

	private final StateManager stateManager;
	private final WaitingCallAndGoRemover waitingCallAndGoRemover;

	public BetterWaitingForTheBestCommandGenerator(StateManager stateManager, WaitingCallAndGoRemover waitingCallAndGoRemover) {
		this.stateManager = stateManager;
		this.waitingCallAndGoRemover = waitingCallAndGoRemover;
	}

	@Override
	public Command nextCommand() {

		// Hack du jour
		if (stateManager.isCabinFull()) {
			throw new RuntimeException("Hey, the cabin is full, I'm the fucking hack of the day (13 november) ;-)");
		}

		if (isOpened()) {
			if (thereIsACallAtCurrentFloorMatchingCurrentDirection() && threeLastCommandAreNotNothing()) {
				return storeCommandInHistory(NOTHING);
			}
			if (thereIsACallAtCurrentFloorMatchingCurrentDirection() && threeLastCommandAreNothing()) {
				Logger.warn("HACK to unlock strange situation ! ...");
			}
			return close();
		}
		if (thereIsAGoAtCurrentFloor()) {
			return open();
		}
		if (thereIsACallAtCurrentFloorMatchingCurrentDirection() || thereIsACallAtCurrentFloorAndNoOtherCallsOrGoMatchingCurrentDirection()) {
			if (dontSkipExtraWaitingCalls()) {
				return open();
			}
		}
		if (isCurrentDirectionIs(UP)) {
			if (thereIsAGoUpstairs() || thereIsACallUpstairs()) {
				return up();
			}
			if (thereIsAGoDownstairs() || thereIsACallDownstairs()) {
				return down();
			}
		}
		if (isCurrentDirectionIs(DOWN)) {
			if (thereIsAGoDownstairs() || thereIsACallDownstairs()) {
				return down();
			}
			if (thereIsAGoUpstairs() || thereIsACallUpstairs()) {
				return up();
			}
		}
		if (isNotAtMiddleFloor()) {
			if (isAboveMiddleFloor()) {
				return down();
			}
			return up();
		}
		return storeCommandInHistory(NOTHING);
	}

	private Command down() {
		stateManager.decrementFloor();
		stateManager.setCurrentDirection(DOWN);
		return storeCommandInHistory(Command.DOWN);
	}

	private Command up() {
		stateManager.incrementFloor();
		stateManager.setCurrentDirection(UP);
		return storeCommandInHistory(Command.UP);
	}

	private Command open() {
		stateManager.setOpened();
		waitingCallAndGoRemover.removeAllCallsFromTheCurrentFloor();
		waitingCallAndGoRemover.removeAllGosFromTheCurrentFloor();
		return storeCommandInHistory(OPEN);
	}

	private Command close() {
		stateManager.setClosed();
		return storeCommandInHistory(CLOSE);
	}

	private boolean isAboveMiddleFloor() {
		return getCurrentFloor() > getMiddelFloor();
	}

	private boolean isNotAtMiddleFloor() {
		return getCurrentFloor() != getMiddelFloor();
	}

	private int getMiddelFloor() {
		return stateManager.getFloorBoundaries().getMiddelFloor();
	}

	private int getCurrentFloor() {
		return stateManager.getCurrentState().getCurrentFloor();
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

	private boolean thereIsACallAtCurrentFloorAndNoOtherCallsOrGoMatchingCurrentDirection() {
		boolean thereIsACallAtCurrentFloor = false;
		boolean thereIsACallAtAnotherFloorMatchingCurrentDirection = false;
		boolean thereIsAGoAtAnotherFloorMatchingCurrentDirection = false;
		for (Call call : stateManager.getCurrentState().getWaitingCalls()) {
			if (isSameFloorThantCurrentFloor(call)) {
				thereIsACallAtCurrentFloor = true;
			} else if (needSameDirectionThanCurrentDirection(call)) {
				thereIsACallAtAnotherFloorMatchingCurrentDirection = true;
			}
		}
		for (Integer goRequest : stateManager.getCurrentState().getGoRequests()) {
			if (isNotSameFloorThantCurrentFloor(goRequest) && needSameDirectionThanCurrentDirection(goRequest)) {
				thereIsAGoAtAnotherFloorMatchingCurrentDirection = true;
			}
		}
		return thereIsACallAtCurrentFloor && !thereIsACallAtAnotherFloorMatchingCurrentDirection && !thereIsAGoAtAnotherFloorMatchingCurrentDirection;
	}

	private boolean needSameDirectionThanCurrentDirection(Integer goRequest) {
		return evaluateDirectionFromCurrentFloorTo(goRequest) == stateManager.getCurrentState().getCurrentDirection();
	}

	private boolean needSameDirectionThanCurrentDirection(Call call) {
		return evaluateDirectionFromCurrentFloorTo(call.floor) == stateManager.getCurrentState().getCurrentDirection();
	}

	private Direction evaluateDirectionFromCurrentFloorTo(int floor) {
		return floor > stateManager.getCurrentState().getCurrentFloor() ? UP : DOWN;
	}

	private boolean isSameDirectionThanCurrentDirection(Call call) {
		return call.direction == stateManager.getCurrentState().getCurrentDirection();
	}

	private boolean isNotSameFloorThantCurrentFloor(Integer goRequest) {
		return goRequest != stateManager.getCurrentState().getCurrentFloor();
	}

	private boolean isSameFloorThantCurrentFloor(Call call) {
		return call.floor == stateManager.getCurrentState().getCurrentFloor();
	}

	private boolean thereIsAGoAtCurrentFloor() {
		return stateManager.getCurrentState().getGoRequests().contains(stateManager.getCurrentState().getCurrentFloor());
	}

	private boolean dontSkipExtraWaitingCalls() {
		return !stateManager.mustSkipExtraWaitingCalls();
	}

}
