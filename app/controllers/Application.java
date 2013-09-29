package controllers;

import play.Logger;
import play.mvc.Controller;
import utils.Direction;

public class Application extends Controller {

	public static void reset(String cause) {
		Logger.info("Request received 'reset' with cause : " + cause);
		ok();
	}

	public static void nextCommand() {
		Logger.info("Request received 'nextCommand'");
		renderText("NOTHING");
	}

	public static void call(int atFloor, Direction to) {
		Logger.info("Request received 'call' atFloor %d to %s", atFloor, to);
		ok();
	}

}