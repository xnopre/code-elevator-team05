package utils;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class ElevatorStateTest {

	// @Test
	// public void test_toString() {
	// Collection<Call> waitingCalls = Arrays.asList(new Call(1, UP), new
	// Call(9, DOWN));
	// Collection<Integer> goRequests = Arrays.asList(7, 8);
	// final ElevatorState elevatorState = new ElevatorState(0, true, UP,
	// waitingCalls, goRequests, 2);
	// final String elevatorStateStr = elevatorState.toString();
	// Assertions.assertThat(elevatorStateStr).startsWith("utils.ElevatorState@");
	// Assertions
	// .assertThat(elevatorStateStr)
	// .endsWith(
	// "[currentFloor=0,waitingCalls=[ElevatorCall[floor=1, direction=UP], ElevatorCall[floor=9, direction=DOWN]],goRequests=[7, 8],opened=true,currentDirection=UP]");
	// }

	@Test
	public void mustGoAtMiddleFloor_must_be_set_only_on_first_cabin() {
		final ElevatorState elevatorState = new ElevatorState(3);
		assertThat(elevatorState.mustGoAtMiddleFloor(0)).isTrue();
		assertThat(elevatorState.mustGoAtMiddleFloor(1)).isFalse();
		assertThat(elevatorState.mustGoAtMiddleFloor(2)).isFalse();
	}
}
