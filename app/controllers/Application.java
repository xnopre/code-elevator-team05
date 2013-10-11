package controllers;

import generators.BetterWaitingForTheBestCommandGenerator;
import generators.CommandGenerator;
import play.Logger;
import play.mvc.Controller;
import utils.Command;
import utils.Direction;
import utils.ElevatorState;
import utils.FloorBoundaries;
import utils.StateManager;
import utils.WaitingCallRemover;

public class Application extends Controller {

	private static final StateManager stateManager = new StateManager(new FloorBoundaries(0, 5));
	private static final CommandGenerator elevatorCommandGenerator = new BetterWaitingForTheBestCommandGenerator(stateManager);
	private static final WaitingCallRemover waitingCallRemover = new WaitingCallRemover(stateManager);

	public static void index() {
		render();
	}

	public static void reset(String cause) {
		Logger.info("Request received 'reset' with cause : " + cause);
		stateManager.reset();
		ok();
	}

	public static void call(int atFloor, Direction to) {
		Logger.info("Request received 'call' atFloor %d to %s", atFloor, to);
		stateManager.storeWaitingCall(atFloor, to);
		ok();
	}

	public static void nextCommand() {
		Logger.info("Request received 'nextCommand'");
		Command nextCommand = elevatorCommandGenerator.nextCommand();
		Logger.info(nextCommand + ", " + stateManager.getCurrentState());
		renderText(nextCommand);
	}

	public static void go(int floorToGo) {
		Logger.info("Request received go(" + floorToGo + ")");
		waitingCallRemover.removeOneCallFromCurrentFloorToGoAtFloor(floorToGo);
		stateManager.storeGoRequest(floorToGo);
		Logger.info("    After go(" + floorToGo + ") : " + stateManager.getCurrentState());
		ok();
	}

	public static void userHasEntered() {
		Logger.info("Request received 'userHasEntered'");
		ok();
	}

	public static void userHasExited() {
		Logger.info("Request received 'userHasExited'");
		stateManager.removeGoRequest(stateManager.getCurrentState().getCurrentFloor());
		Logger.info("    After 'userHasExited' : " + stateManager.getCurrentState());
		ok();
	}

	public static void state() {
		final ElevatorState currentState = stateManager.getCurrentState();
		render(currentState);
	}
}
