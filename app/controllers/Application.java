package controllers;

import generators.BetterWaitingForTheBestCommandGenerator;
import generators.CommandGenerator;
import play.Logger;
import play.mvc.Controller;
import utils.Command;
import utils.Direction;
import utils.ElevatorException;
import utils.ElevatorState;
import utils.FloorBoundaries;
import utils.StateManager;
import utils.WaitingCallRemover;

public class Application extends Controller {

	private static final Object monitor = new Object();

	private static final StateManager stateManager = new StateManager(new FloorBoundaries(0, 5));
	private static final CommandGenerator elevatorCommandGenerator = new BetterWaitingForTheBestCommandGenerator(stateManager);
	private static final WaitingCallRemover waitingCallRemover = new WaitingCallRemover(stateManager);

	public static void index() {
		synchronized (monitor) {
			render();
		}
	}

	public static void reset(String cause) {
		synchronized (monitor) {
			Logger.info("Request received 'reset' with cause : " + cause);
			stateManager.reset();
			ok();
		}
	}

	public static void call(int atFloor, Direction to) {
		synchronized (monitor) {
			Logger.info("Request received 'call' atFloor %d to %s", atFloor, to);
			stateManager.storeWaitingCall(atFloor, to);
			ok();
		}
	}

	public static void nextCommand() {
		synchronized (monitor) {
			Logger.info("Request received 'nextCommand'");
			Command nextCommand = elevatorCommandGenerator.nextCommand();
			Logger.info(nextCommand + ", " + stateManager.getCurrentState());
			renderText(nextCommand);
		}
	}

	public static void go(int floorToGo) {
		synchronized (monitor) {
			Logger.info("Request received go(" + floorToGo + ")");
			try {
				waitingCallRemover.removeOneCallFromCurrentFloorToGoAtFloor(floorToGo);
			} catch (ElevatorException e) {
				Logger.warn("Error processing 'go' : " + e.getMessage());
			}
			stateManager.storeGoRequest(floorToGo);
			Logger.info("    After go(" + floorToGo + ") : " + stateManager.getCurrentState());
			ok();
		}
	}

	public static void userHasEntered() {
		synchronized (monitor) {
			Logger.info("Request received 'userHasEntered'");
			ok();
		}
	}

	public static void userHasExited() {
		synchronized (monitor) {
			Logger.info("Request received 'userHasExited'");
			stateManager.removeGoRequest(stateManager.getCurrentState().getCurrentFloor());
			Logger.info("    After 'userHasExited' : " + stateManager.getCurrentState());
			ok();
		}
	}

	public static void state() {
		synchronized (monitor) {
			final ElevatorState currentState = stateManager.getCurrentState();
			render(currentState);
		}
	}
}
