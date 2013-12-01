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
		Command nextCommand1 = elevatorCommandGenerator.nextCommand(0);
		final long duration = System.currentTimeMillis() - time0;
		Logger.info("NextCommand " + nextCommand1 + " calculated in " + duration + " ms. New current state = " + stateManager.getCurrentState());
		Command nextCommand = nextCommand1;
		return nextCommand + "\n" + Command.NOTHING;
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
		// waitingCallAndGoRemover.removeGoRequest(stateManager.getCurrentState().getCurrentFloor());
		Logger.info("    After 'userHasExited' : " + stateManager.getCurrentState());
	}

}
