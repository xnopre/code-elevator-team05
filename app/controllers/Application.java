package controllers;

import play.Logger;
import play.mvc.Controller;
import utils.Command;
import utils.CommandGenerator;
import utils.Direction;
import utils.ElevatorState;
import utils.FloorBoundaries;
import utils.StateManager;
import utils.TrapOmnibusCommandGenerator;

public class Application extends Controller {

	private static final StateManager stateManager = new StateManager(new FloorBoundaries(0, 5));
	private static final CommandGenerator elevatorCommandGenerator = new TrapOmnibusCommandGenerator(stateManager);

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
		stateManager.storeCall(atFloor, to);
		ok();
	}

	public static void nextCommand() {
		Logger.info("Request received 'nextCommand'");
		Command nextCommand = elevatorCommandGenerator.nextCommand();
		Logger.info(nextCommand + ", " + stateManager.getCurrentState());
		renderText(nextCommand);
	}

	public static void go(int floorToGo) {
		ok();
	}

	public static void userHasEntered() {
		ok();
	}

	public static void userHasExited() {
		ok();
	}

	public static void state() {
		final ElevatorState currentState = stateManager.getCurrentState();
		render(currentState);
	}
}
