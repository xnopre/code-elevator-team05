package controllers;

import play.Logger;
import play.mvc.Controller;
import utils.CallStorer;
import utils.Direction;
import utils.ElevatorCommandGenerator;
import utils.StateResetter;

public class Application extends Controller {

	private static final StateResetter stateResetter = new StateResetter();
	private static final CallStorer callStorer = new CallStorer();
	private static final ElevatorCommandGenerator elevatorCommandGenerator = new ElevatorCommandGenerator();

	public static void reset(String cause) {
		Logger.info("Request received 'reset' with cause : " + cause);
		stateResetter.reset();
		ok();
	}

	public static void call(int atFloor, Direction to) {
		Logger.info("Request received 'call' atFloor %d to %s", atFloor, to);
		callStorer.storeCall(atFloor, to);
		ok();
	}

	public static void nextCommand() {
		Logger.info("Request received 'nextCommand'");
		renderText(elevatorCommandGenerator.nextCommand());
	}
}