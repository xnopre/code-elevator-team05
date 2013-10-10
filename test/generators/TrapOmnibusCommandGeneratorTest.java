package generators;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static utils.Command.CLOSE;
import static utils.Command.DOWN;
import static utils.Command.OPEN;
import static utils.Command.UP;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import utils.StateManager;

@RunWith(MockitoJUnitRunner.class)
public class TrapOmnibusCommandGeneratorTest {

	@Mock
	private StateManager stateManager;

	@InjectMocks
	private TrapOmnibusCommandGenerator omnibusCommandGenerator;

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
		verify(stateManager).incrementFloor();

	}

	@Test
	public void ensure_fourth_command_is_to_go_open() {
		assertUpCycle();
		verify(stateManager).incrementFloor();
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
	}

	@Test
	public void go_to_second_floor() {
		assertUpCycle();
		assertUpCycle();
		verify(stateManager, Mockito.times(2)).incrementFloor();
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
	}

	@Test
	public void ensure_that_floor_has_been_incremented() {
		assertUpCycle();
		verify(stateManager).incrementFloor();
	}

	@Test
	public void ensure_that_floor_has_been_decremented() {
		givenAnElevatorAtLastFloorDoorClosed();

		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(DOWN);
		verify(stateManager).decrementFloor();
	}

	private void givenAnElevatorAtLastFloorDoorClosed() {
		Mockito.doReturn(true).when(stateManager).atLastFloor();
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(CLOSE);
	}

	@Test
	public void ensure_that_next_command_after_down_is_open() {
		givenAnElevatorAtLastFloorDoorClosed();

		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(DOWN);
		verify(stateManager).decrementFloor();
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
	}

	private void assertUpCycle() {
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(OPEN);
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(CLOSE);
		assertThat(omnibusCommandGenerator.nextCommand()).isEqualTo(UP);
	}

}
