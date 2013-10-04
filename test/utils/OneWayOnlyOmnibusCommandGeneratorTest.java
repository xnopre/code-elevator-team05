package utils;

import static org.fest.assertions.Assertions.assertThat;
import static utils.Command.CLOSE;
import static utils.Command.OPEN;
import static utils.Command.UP;

import org.junit.Test;

public class OneWayOnlyOmnibusCommandGeneratorTest {

	private final OneWayOnlyOmnibusCommandGenerator omnibusCommandGenerator = new OneWayOnlyOmnibusCommandGenerator();

	@Test
	public void ensure_first_command_is_open_the_door() {
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
	}

	@Test
	public void ensure_second_command_is_close_the_door() {
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(CLOSE);
	}

	@Test
	public void ensure_third_command_is_to_go_up() {
		assertUpCycle();
	}

	@Test
	public void ensure_fourth_command_is_to_go_open() {
		assertUpCycle();
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
	}

	@Test
	public void go_to_second_floor() {
		assertUpCycle();
		assertUpCycle();
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
	}

	private void assertUpCycle() {
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(CLOSE);
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(UP);
	}
}
