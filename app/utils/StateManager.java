package utils;

import java.util.Arrays;

public class StateManager {

	public static final ElevatorState INITIAL_STATE = new ElevatorState();

	private ElevatorState currentState = INITIAL_STATE;

	private final FloorBoundaries floorBoundaries;

	private final SizeLimitedArrayList lastCommands = new SizeLimitedArrayList(3);

	public StateManager(FloorBoundaries floorBoundaries) {
		this.floorBoundaries = floorBoundaries;
	}

	public void reset() {
		currentState = INITIAL_STATE;
	}

	public void storeWaitingCall(int atFloor, Direction to) {
		currentState = ElevatorState.Builder.from(currentState).addWaitingCall(atFloor, to).get();
	}

	public void removeWaitingCall(int atFloor, Direction to) {
		currentState = ElevatorState.Builder.from(currentState).removeWaitingCall(atFloor, to).get();
	}

	public void storeGoRequest(int floorToGo) {
		currentState = ElevatorState.Builder.from(currentState).addGoRequest(floorToGo).get();
	}

	public void removeGoRequest(int floorToGo) {
		currentState = ElevatorState.Builder.from(currentState).removeGoRequest(floorToGo).get();
	}

	public ElevatorState getCurrentState() {
		return currentState;
	}

	public void incrementFloor() {
		if (atLastFloor())
			throw new UnreachableFloorException();
		currentState = ElevatorState.Builder.from(currentState).incrementFloor().get();
	}

	public void decrementFloor() {
		if (atFirstFloor())
			throw new UnreachableFloorException();
		currentState = ElevatorState.Builder.from(currentState).decrementFloor().get();
	}

	public boolean atFirstFloor() {
		return floorBoundaries.atFirstFloor(currentState.getCurrentFloor());
	}

	public boolean atLastFloor() {
		return floorBoundaries.atLastFloor(currentState.getCurrentFloor());
	}

	public void setOpened() {
		currentState = ElevatorState.Builder.from(currentState).setOpened().get();
	}

	public void setClosed() {
		currentState = ElevatorState.Builder.from(currentState).setClosed().get();
	}

	public void storeCommandInHistory(Command command) {
		lastCommands.add(command);
	}

	public boolean areThreeLastCommandEqualTo(Command command) {
		Command[] commands = new Command[] { command, command, command };
		return Arrays.equals(lastCommands.toArray(), commands);
	}
}
