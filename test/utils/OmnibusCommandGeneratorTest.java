package utils;

import static org.fest.assertions.Assertions.assertThat;
import static utils.Command.CLOSE;
import static utils.Command.DOWN;
import static utils.Command.OPEN;
import static utils.Command.UP;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OmnibusCommandGeneratorTest {

	@Mock
	private StateManager stateManager;

	@InjectMocks
	private OmnibusCommandGenerator omnibusCommandGenerator;

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

	@Test
	@Ignore("à Cédric!")
	public void ensure_elevator_goes_to_the_last_floor_and_go_down() {
		assertUpCycle();
		assertUpCycle();
		assertDownCycle();
	}

	private void assertUpCycle() {
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(CLOSE);
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(UP);
	}

	private void assertDownCycle() {
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(CLOSE);
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(DOWN);
	}

}
