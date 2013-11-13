package utils;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static utils.Direction.DOWN;
import static utils.Direction.UP;

import org.junit.Test;

public class CallTest {

	@Test
	public void test_toString() {
		assertEquals("ElevatorCall[floor=1, direction=DOWN]", new Call(1, Direction.DOWN).toString());
	}

	@Test
	public void test_equals() {
		final Call call = new Call(1, DOWN);
		assertThat(call).isEqualTo(call);
		assertThat(new Call(1, DOWN)).isEqualTo(new Call(1, DOWN));
		assertThat(new Call(1, DOWN)).isNotEqualTo(new Call(2, DOWN));
		assertThat(new Call(1, DOWN)).isNotEqualTo(new Call(1, UP));
	}

}
