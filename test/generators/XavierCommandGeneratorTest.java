package generators;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static utils.Command.NOTHING;
import static utils.Command.OPEN;
import static utils.Direction.UP;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import utils.Call;
import utils.Direction;
import utils.ElevatorState;
import utils.StateManager;

@RunWith(MockitoJUnitRunner.class)
public class XavierCommandGeneratorTest {

	private final StateManager stateManager = Mockito.mock(StateManager.class);

	private final XavierCommandGenerator xavierCommandGenerator = new XavierCommandGenerator(stateManager);

	@Test
	public void command_is_nothing_without_state_command() {
		when(stateManager.getCurrentState()).thenReturn(new ElevatorState());

		assertThat(xavierCommandGenerator.nextCommand()).isEqualTo(NOTHING);
	}

	@Test
	public void command_is_open_if_call_at_same_floor_0() {
		final ElevatorState state = new ElevatorState(0, call(0, UP));
		when(stateManager.getCurrentState()).thenReturn(state);

		assertThat(xavierCommandGenerator.nextCommand()).isEqualTo(OPEN);
	}

	@Test
	public void command_is_open_if_call_at_same_floor_1() {
		final ElevatorState state = new ElevatorState(1, call(2, UP), call(1, UP));
		when(stateManager.getCurrentState()).thenReturn(state);

		assertThat(xavierCommandGenerator.nextCommand()).isEqualTo(OPEN);
	}

	private Call call(final int floor, final Direction direction) {
		return new Call(floor, direction);
	}
}
