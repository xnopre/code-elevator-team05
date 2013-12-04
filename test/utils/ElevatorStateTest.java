package utils;

import static org.fest.assertions.Assertions.assertThat;
import static utils.Direction.DOWN;
import static utils.Direction.UP;

import org.junit.Test;

public class ElevatorStateTest {

	@Test
	public void mustGoAtMiddleFloor_must_be_alternated_between_cabins() {
		final ElevatorState elevatorState = new ElevatorState(8, -5, 48);
		assertThat(elevatorState.getRestingFloor(0)).isEqualTo(-2);
		assertThat(elevatorState.getRestingFloor(1)).isEqualTo(5);
		assertThat(elevatorState.getRestingFloor(2)).isEqualTo(12);
		assertThat(elevatorState.getRestingFloor(3)).isEqualTo(18);
		assertThat(elevatorState.getRestingFloor(4)).isEqualTo(25);
		assertThat(elevatorState.getRestingFloor(5)).isEqualTo(31);
		assertThat(elevatorState.getRestingFloor(6)).isEqualTo(38);
		assertThat(elevatorState.getRestingFloor(7)).isEqualTo(45);
	}

	@Test
	public void default_direction_must_be_alternated_between_cabins() {
		final ElevatorState elevatorState = new ElevatorState(4, -5, 48);
		assertThat(elevatorState.getCurrentDirection(0)).isEqualTo(UP);
		assertThat(elevatorState.getCurrentDirection(1)).isEqualTo(DOWN);
		assertThat(elevatorState.getCurrentDirection(2)).isEqualTo(UP);
		assertThat(elevatorState.getCurrentDirection(3)).isEqualTo(DOWN);
	}
}
