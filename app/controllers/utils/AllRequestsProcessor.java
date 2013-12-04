package controllers.utils;

import generators.CommandGenerator;
import play.Logger;
import utils.Command;
import utils.Direction;
import utils.StateManager;
import utils.WaitingCallAndGoRemover;

public class AllRequestsProcessor {

	private final WaitingCallAndGoRemover waitingCallAndGoRemover;
	private final CommandGenerator elevatorCommandGenerator;
	private final StateManager stateManager;

	public AllRequestsProcessor(StateManager stateManager, WaitingCallAndGoRemover waitingCallAndGoRemover, CommandGenerator elevatorCommandGenerator) {
		this.stateManager = stateManager;
		this.waitingCallAndGoRemover = waitingCallAndGoRemover;
		this.elevatorCommandGenerator = elevatorCommandGenerator;
	}

	public void reset(int lowerFloor, int higherFloor, int cabinSize, int cabinCount) {
		stateManager.reset(lowerFloor, higherFloor, cabinSize, cabinCount);
	}

	public void call(int atFloor, Direction to) {
		stateManager.storeWaitingCall(atFloor, to);
	}

	public String nextCommands() {
		final long time0 = System.currentTimeMillis();
		String nextCommands = nextCommands_timed();
		final long duration = System.currentTimeMillis() - time0;
		Logger.info("NextCommand '" + encodeCr(nextCommands) + "' calculated in " + duration + " ms. New current state = " + stateManager.getCurrentState());
		return nextCommands;
	}

	public void go(int cabin, int floorToGo) {
		Logger.info("Request received go(" + floorToGo + ")");
		// try {
		// waitingCallAndGoRemover.removeOneCallFromCurrentFloorToGoAtFloor(floorToGo);
		// } catch (ElevatorException e) {
		// Logger.warn("Error processing 'go' : " + e.getMessage());
		// }
		stateManager.storeGoRequest(cabin, floorToGo);
		Logger.info("    After go(" + floorToGo + ") : " + stateManager.getCurrentState());
	}

	public void userHasEntered(int cabin) {
		Logger.info("Request received 'userHasEntered'");
	}

	public void userHasExited(int cabin) {
		Logger.info("Request received 'userHasExited'");
		waitingCallAndGoRemover.removeGoRequest(cabin, stateManager.getCurrentState().getCurrentFloor(cabin));
		Logger.info("    After 'userHasExited' : " + stateManager.getCurrentState());
	}

	private String nextCommands_timed() {
		int cabinCount = stateManager.getCabinCount();
		String nextCommands = "";
		for (int cabin = 0; cabin < cabinCount; cabin++) {
			Command nextCommand = elevatorCommandGenerator.nextCommand(cabin);
			if (cabin > 0) {
				nextCommands += "\n";
			}
			nextCommands += nextCommand;
		}
		return nextCommands;
	}

	private String encodeCr(String cmds) {
		return cmds.replaceAll("\n", "<CR>");
	}
}
