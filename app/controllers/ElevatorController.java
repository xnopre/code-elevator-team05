package controllers;

import generators.BetterWaitingForTheBestCommandGenerator;
import generators.CommandGenerator;
import play.Logger;
import play.mvc.Controller;
import utils.Direction;
import utils.ElevatorState;
import utils.FloorBoundaries;
import utils.StateManager;
import utils.WaitingCallAndGoRemover;
import controllers.utils.AllRequestsProcessor;

public class ElevatorController extends Controller {

	private static final Object monitor = new Object();

	private static final StateManager stateManager = new StateManager(new FloorBoundaries(0, 19));

	private static final WaitingCallAndGoRemover waitingCallAndGoRemover = new WaitingCallAndGoRemover(stateManager);

	private static final CommandGenerator elevatorCommandGenerator = new BetterWaitingForTheBestCommandGenerator(stateManager, waitingCallAndGoRemover);

	private static final AllRequestsProcessor allRequestsProcessor = new AllRequestsProcessor(stateManager, waitingCallAndGoRemover, elevatorCommandGenerator);

	public static void index() {
		synchronized (monitor) {
			render();
		}
	}

	public static void reset(int lowerFloor, int higherFloor, int cabinSize, int cabinCount, String cause) {
		synchronized (monitor) {
			Logger.info("Request received 'reset' : lowerFloor=" + lowerFloor + ", higherFloor=" + higherFloor + ", cabinSize=" + cabinSize + ", cabinCount="
					+ cabinCount + ", cause=" + cause);
			allRequestsProcessor.reset(lowerFloor, higherFloor, cabinSize, cabinCount);
			ok();
		}
	}

	public static void call(int atFloor, Direction to) {
		synchronized (monitor) {
			Logger.info("Request received 'call' atFloor %d to %s", atFloor, to);
			allRequestsProcessor.call(atFloor, to);
			ok();
		}
	}

	public static void nextCommands() {
		synchronized (monitor) {
			final String nextCommands = allRequestsProcessor.nextCommands();
			Logger.info("NextCommands : " + encodeCr(nextCommands));
			renderText(nextCommands);
		}
	}

	public static void go(int cabin, int floorToGo) {
		synchronized (monitor) {
			Logger.info("Request received go(" + floorToGo + ")");
			allRequestsProcessor.go(cabin, floorToGo);
			ok();
		}
	}

	public static void userHasEntered(int cabin) {
		synchronized (monitor) {
			Logger.info("Request received 'userHasEntered'");
			allRequestsProcessor.userHasEntered(cabin);
			ok();
		}
	}

	public static void userHasExited(int cabin) {
		synchronized (monitor) {
			Logger.info("Request received 'userHasExited'");
			allRequestsProcessor.userHasEntered(cabin);
			ok();
		}
	}

	public static void state() {
		synchronized (monitor) {
			final ElevatorState currentState = stateManager.getCurrentState();
			final FloorBoundaries currentFloorBoundaries = stateManager.getFloorBoundaries();
			final boolean mustSkipExtraWaitingCalls = stateManager.mustSkipExtraWaitingCalls();
			render(currentState, currentFloorBoundaries, mustSkipExtraWaitingCalls);
		}
	}

	private static String encodeCr(String cmd) {
		return cmd.replaceAll("\n", "<CR>");
	}

}
