package generators;

import static utils.Command.CLOSE;
import static utils.Command.NOTHING;
import static utils.Command.OPEN_DOWN;
import static utils.Command.OPEN_UP;
import static utils.Direction.DOWN;
import static utils.Direction.UP;

import java.util.Collection;

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
	public Command nextCommand(int cabin) {

		// // Hack du jour (13 & 14/11/2013)
		// if (stateManager.isCabinFull()) {
		// throw new
		// RuntimeException("Hey, the cabin is full, I'm the fucking hack of the day (13 & 14 november) ;-)");
		// }

		if (isOpened(cabin)) {
			if (thereIsACallAtCurrentFloorMatchingCurrentDirection(cabin) && isNotCabinFull(cabin)) {
				return NOTHING;
			}
			return close(cabin);
		}
		if (thereIsAGoAtCurrentFloor(cabin)) {
			return open(cabin, getCurrentDirection(cabin));
		}
		// if (dontSkipExtraWaitingCalls(cabin)) {
		if (isNotCabinFull(cabin)) {
			if (thereIsACallAtCurrentFloorMatchingCurrentDirection(cabin)) {
				return open(cabin, getCurrentDirection(cabin));
			}
			if (thereIsACallAtCurrentFloorAndNoOtherCallsOrGoMatchingCurrentDirection(cabin)) {
				return open(cabin, invertCurrentDirection(cabin));
			}
		}
		// }
		if (isCurrentDirectionIs(cabin, UP)) {
			if (thereIsAGoUpstairs(cabin) || thereIsACallUpstairs(cabin)) {
				return up(cabin);
			}
			if (thereIsAGoDownstairs(cabin) || thereIsACallDownstairs(cabin)) {
				return down(cabin);
			}
		}
		if (isCurrentDirectionIs(cabin, DOWN)) {
			if (thereIsAGoDownstairs(cabin) || thereIsACallDownstairs(cabin)) {
				return down(cabin);
			}
			if (thereIsAGoUpstairs(cabin) || thereIsACallUpstairs(cabin)) {
				return up(cabin);
			}
		}
		if (isNotAtMiddleFloor(cabin)) {
			if (isAboveMiddleFloor(cabin)) {
				return down(cabin);
			}
			return up(cabin);
		}
		return NOTHING;
	}

	private boolean isNotCabinFull(int cabin) {
		return !stateManager.isCabinFull(cabin);
	}

	private Command down(int cabin) {
		stateManager.decrementFloor(cabin);
		stateManager.setCurrentDirection(cabin, DOWN);
		return Command.DOWN;
	}

	private Command up(int cabin) {
		stateManager.incrementFloor(cabin);
		stateManager.setCurrentDirection(cabin, UP);
		return Command.UP;
	}

	private Command open(int cabin, Direction direction) {
		stateManager.setOpened(cabin);
		waitingCallAndGoRemover.removeAllCallsFromTheCurrentFloor(direction);
		waitingCallAndGoRemover.removeAllGosFromTheCurrentFloor(cabin);
		return (direction == UP ? OPEN_UP : OPEN_DOWN);
	}

	private Command close(int cabin) {
		stateManager.setClosed(cabin);
		return CLOSE;
	}

	private boolean isAboveMiddleFloor(int cabin) {
		return getCurrentFloor(cabin) > getMiddelFloor();
	}

	private boolean isNotAtMiddleFloor(int cabin) {
		return getCurrentFloor(cabin) != getMiddelFloor();
	}

	private int getMiddelFloor() {
		return stateManager.getFloorBoundaries().getMiddelFloor();
	}

	private int getCurrentFloor(int cabin) {
		return stateManager.getCurrentState().getCurrentFloor(cabin);
	}

	private boolean isCurrentDirectionIs(int cabin, Direction direction) {
		return getCurrentDirection(cabin) == direction;
	}

	private Direction getCurrentDirection(int cabin) {
		return stateManager.getCurrentState().getCurrentDirection(cabin);
	}

	private Direction invertCurrentDirection(int cabin) {
		return getCurrentDirection(cabin) == DOWN ? UP : DOWN;
	}

	private boolean thereIsAGoUpstairs(int cabin) {
		final int currentFloor = stateManager.getCurrentState().getCurrentFloor(cabin);
		for (Integer goRequest : stateManager.getCurrentState().getGoRequests(cabin)) {
			if (goRequest > currentFloor) {
				return true;
			}
		}
		return false;
	}

	private boolean thereIsACallUpstairs(int cabin) {
		final int currentFloor = stateManager.getCurrentState().getCurrentFloor(cabin);
		for (Call call : stateManager.getCurrentState().getWaitingCalls()) {
			if (call.floor > currentFloor) {
				return true;
			}
		}
		return false;
	}

	private boolean thereIsAGoDownstairs(int cabin) {
		final int currentFloor = stateManager.getCurrentState().getCurrentFloor(cabin);
		for (Integer goRequest : stateManager.getCurrentState().getGoRequests(cabin)) {
			if (goRequest < currentFloor) {
				return true;
			}
		}
		return false;
	}

	private boolean thereIsACallDownstairs(int cabin) {
		final int currentFloor = stateManager.getCurrentState().getCurrentFloor(cabin);
		for (Call call : stateManager.getCurrentState().getWaitingCalls()) {
			if (call.floor < currentFloor) {
				return true;
			}
		}
		return false;
	}

	private boolean isOpened(int cabin) {
		return stateManager.getCurrentState().isOpened(cabin);
	}

	private boolean thereIsACallAtCurrentFloorMatchingCurrentDirection(int cabin) {
		final Collection<Call> waitingCalls = stateManager.getCurrentState().getWaitingCalls();
		for (Call call : waitingCalls) {
			if (isSameFloorThantCurrentFloor(cabin, call) && isSameDirectionThanCurrentDirection(cabin, call)) {
				return true;
			}
		}
		return false;
	}

	private boolean thereIsACallAtCurrentFloorAndNoOtherCallsOrGoMatchingCurrentDirection(int cabin) {
		boolean thereIsACallAtCurrentFloor = false;
		boolean thereIsACallAtAnotherFloorMatchingCurrentDirection = false;
		boolean thereIsAGoAtAnotherFloorMatchingCurrentDirection = false;
		for (Call call : stateManager.getCurrentState().getWaitingCalls()) {
			if (isSameFloorThantCurrentFloor(cabin, call)) {
				thereIsACallAtCurrentFloor = true;
			} else if (needSameDirectionThanCurrentDirection(cabin, call)) {
				thereIsACallAtAnotherFloorMatchingCurrentDirection = true;
			}
		}
		for (Integer goRequest : stateManager.getCurrentState().getGoRequests(cabin)) {
			if (isNotSameFloorThantCurrentFloor(cabin, goRequest) && needSameDirectionThanCurrentDirection(cabin, goRequest)) {
				thereIsAGoAtAnotherFloorMatchingCurrentDirection = true;
			}
		}
		return thereIsACallAtCurrentFloor && !thereIsACallAtAnotherFloorMatchingCurrentDirection && !thereIsAGoAtAnotherFloorMatchingCurrentDirection;
	}

	private boolean needSameDirectionThanCurrentDirection(int cabin, Integer goRequest) {
		return evaluateDirectionFromCurrentFloorTo(cabin, goRequest) == getCurrentDirection(cabin);
	}

	private boolean needSameDirectionThanCurrentDirection(int cabin, Call call) {
		return evaluateDirectionFromCurrentFloorTo(cabin, call.floor) == getCurrentDirection(cabin);
	}

	private Direction evaluateDirectionFromCurrentFloorTo(int cabin, int floor) {
		return floor > stateManager.getCurrentState().getCurrentFloor(cabin) ? UP : DOWN;
	}

	private boolean isSameDirectionThanCurrentDirection(int cabin, Call call) {
		return call.direction == getCurrentDirection(cabin);
	}

	private boolean isNotSameFloorThantCurrentFloor(int cabin, Integer goRequest) {
		return goRequest != stateManager.getCurrentState().getCurrentFloor(cabin);
	}

	private boolean isSameFloorThantCurrentFloor(int cabin, Call call) {
		return call.floor == stateManager.getCurrentState().getCurrentFloor(cabin);
	}

	private boolean thereIsAGoAtCurrentFloor(int cabin) {
		return stateManager.getCurrentState().getGoRequests(cabin).contains(stateManager.getCurrentState().getCurrentFloor(cabin));
	}

}
