package utils;

import static utils.Direction.DOWN;
import static utils.Direction.UP;

import java.util.Arrays;
import java.util.Collection;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class ElevatorStateTest {

	@Test
	public void test_toString() {
		Collection<Call> waitingCalls = Arrays.asList(new Call(1, UP), new Call(9, DOWN));
		Collection<Integer> goRequests = Arrays.asList(7, 8);
		final ElevatorState elevatorState = new ElevatorState(0, true, UP, waitingCalls, goRequests);
		final String elevatorStateStr = elevatorState.toString();
		Assertions.assertThat(elevatorStateStr).startsWith("utils.ElevatorState@");
		Assertions
				.assertThat(elevatorStateStr)
				.endsWith(
						"[currentFloor=0,waitingCalls=[ElevatorCall[floor=1, direction=UP], ElevatorCall[floor=9, direction=DOWN]],goRequests=[7, 8],opened=true,currentDirection=UP]");
	}

}
