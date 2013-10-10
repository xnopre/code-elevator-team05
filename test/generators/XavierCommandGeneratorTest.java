package generators;

import static org.fest.assertions.Assertions.assertThat;
import static utils.Command.NOTHING;
import static utils.Command.OPEN;
import generators.XavierCommandGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import utils.Direction;
import utils.FloorBoundaries;
import utils.StateManager;

@RunWith(MockitoJUnitRunner.class)
public class XavierCommandGeneratorTest {

	private final FloorBoundaries floorBoundaries = new FloorBoundaries(0, 3);

	private final StateManager stateManager = new StateManager(floorBoundaries);

	private final XavierCommandGenerator xavierCommandGenerator = new XavierCommandGenerator(stateManager);

	@Test
	public void command_is_nothing_without_state_command() {
		assertThat(xavierCommandGenerator.nextCommand()).isEqualTo(NOTHING);
	}

	@Test
	public void command_is_open_if_call_at_same_floor_0() {
		stateManager.storeCall(0, Direction.UP);
		assertThat(xavierCommandGenerator.nextCommand()).isEqualTo(OPEN);
	}

	@Test
	public void command_is_open_if_call_at_same_floor_1() {
		stateManager.incrementFloor();
		stateManager.storeCall(2, Direction.UP);
		stateManager.storeCall(1, Direction.UP);
		assertThat(xavierCommandGenerator.nextCommand()).isEqualTo(OPEN);
	}
}
