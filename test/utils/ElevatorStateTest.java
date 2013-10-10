package utils;

import static utils.Direction.DOWN;
import static utils.Direction.UP;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class ElevatorStateTest {

	@Test
	public void ensure_builder_create_new_instance_with_next_floor() {
		ElevatorState basement = new ElevatorState();
		Assertions.assertThat(basement.getCurrentFloor()).isEqualTo(0);

		ElevatorState secondFloor = ElevatorState.Builder.from(basement).incrementFloor().incrementFloor().get();

		Assertions.assertThat(secondFloor).isNotSameAs(basement);
		Assertions.assertThat(secondFloor.getCurrentFloor()).isEqualTo(2);
	}

	@Test
	public void ensure_builder_create_new_instance_with_new_call() {
		ElevatorState basement = new ElevatorState();
		Assertions.assertThat(basement.getCurrentFloor()).isEqualTo(0);

		ElevatorState firstFloor = ElevatorState.Builder.from(basement).addWaitingCall(5, Direction.DOWN).addWaitingCall(2, Direction.UP).get();

		Assertions.assertThat(firstFloor).isNotSameAs(basement);

		Assertions.assertThat(firstFloor.getWaitingCalls()).containsOnly(new Call(5, DOWN), new Call(2, UP));
	}

	@Test
	public void ensure_builder_create_new_instance_even_when_no_modif() {
		ElevatorState basement = new ElevatorState();
		Assertions.assertThat(basement.getCurrentFloor()).isEqualTo(0);

		ElevatorState firstFloor = ElevatorState.Builder.from(basement).get();

		Assertions.assertThat(firstFloor).isNotSameAs(basement);
	}

	@Test
	public void ensure_builder_can_modify_both_fields_at_once() {
		ElevatorState basement = new ElevatorState();
		Assertions.assertThat(basement.getCurrentFloor()).isEqualTo(0);

		ElevatorState secondFloor = ElevatorState.Builder.from(basement).addWaitingCall(5, Direction.DOWN).incrementFloor().addWaitingCall(2, Direction.UP)
				.incrementFloor().get();

		Assertions.assertThat(secondFloor).isNotSameAs(basement);

		Assertions.assertThat(secondFloor.getWaitingCalls()).containsOnly(new Call(5, DOWN), new Call(2, UP));
		Assertions.assertThat(secondFloor.getCurrentFloor()).isEqualTo(2);

	}
}
