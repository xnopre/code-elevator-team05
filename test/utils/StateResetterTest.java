package utils;

import junit.framework.Assert;

import org.junit.Test;

public class StateResetterTest {

	private final ElevatorState elevatorState = new ElevatorState();
	private final StateResetter stateResetter = new StateResetter(elevatorState);

	@Test
	public void reset_must_reset_all_data_in_state() {
		elevatorState.currentFloor = 3;
		elevatorState.currentUsers = 1;

		stateResetter.reset();

		Assert.assertEquals(0, elevatorState.currentFloor);
		Assert.assertEquals(0, elevatorState.currentUsers);
		ElevatorState initialElevatorState = new ElevatorState();
		initialElevatorState.currentFloor = 0;
		initialElevatorState.currentUsers = 0;
		Assert.assertEquals(initialElevatorState, elevatorState);
	}
}
