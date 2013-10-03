package controllers;

import play.Logger;
import play.mvc.Controller;
import utils.CommandGenerator;
import utils.Direction;
import utils.NoOpCommandGenerator;
import utils.ElevatorState;
import utils.FloorBoundaries;
import utils.StateManager;

public class Application extends Controller {

	private static final StateManager stateManager = new StateManager(
			new FloorBoundaries(0, 5));
	private static final CommandGenerator elevatorCommandGenerator = new NoOpCommandGenerator();

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
		renderText(elevatorCommandGenerator.nextCommand());
	}

	public static void state() {
		final ElevatorState currentState = stateManager.getCurrentState();
		render(currentState);
	}
}
