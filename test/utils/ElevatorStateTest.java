package utils;

import static org.fest.assertions.Assertions.assertThat;
import static utils.Direction.DOWN;
import static utils.Direction.UP;

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
	public void mustGoAtMiddleFloor_must_be_alternated_between_cabins() {
		final ElevatorState elevatorState = new ElevatorState(4);
		assertThat(elevatorState.mustGoAtMiddleFloor(0)).isTrue();
		assertThat(elevatorState.mustGoAtMiddleFloor(1)).isFalse();
		assertThat(elevatorState.mustGoAtMiddleFloor(2)).isTrue();
		assertThat(elevatorState.mustGoAtMiddleFloor(3)).isFalse();
	}

	@Test
	public void default_direction_must_be_alternated_between_cabins() {
		final ElevatorState elevatorState = new ElevatorState(4);
		assertThat(elevatorState.getCurrentDirection(0)).isEqualTo(UP);
		assertThat(elevatorState.getCurrentDirection(1)).isEqualTo(DOWN);
		assertThat(elevatorState.getCurrentDirection(2)).isEqualTo(UP);
		assertThat(elevatorState.getCurrentDirection(3)).isEqualTo(DOWN);
	}
}
