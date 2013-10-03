package utils;

import static utils.Command.CLOSE;
import static utils.Command.DOWN;
import static utils.Command.OPEN;
import static utils.Command.UP;

public class OmnibusCommandGenerator implements CommandGenerator {

	private Command previousCommand = Command.NOTHING;

	private final StateManager stateManager;

	public OmnibusCommandGenerator(StateManager stateManager) {
		this.stateManager = stateManager;
	}

	@Override
	public Command nextCommand() {

		Command nextCommand = computeNextCommand();
		changeFloorIfNecessary(nextCommand);
		storePreviousCommand(nextCommand);
		return nextCommand;
	}

	private void storePreviousCommand(Command nextCommand) {
		previousCommand = nextCommand;
	}

	private void changeFloorIfNecessary(Command nextCommand) {
		if (nextCommand.equals(UP)) {
			stateManager.incrementFloor();
		}
		if (nextCommand.equals(DOWN)) {
			stateManager.decrementFloor();
		}

	}

	private Command computeNextCommand() {
		switch (previousCommand) {
		case NOTHING:
		case UP:
		case DOWN:
			return OPEN;
		case OPEN:
			return CLOSE;
		case CLOSE:
			if (atLastFloor())
				return DOWN;
			return UP;
		default:
			throw new UnsupportedOperationException();
		}
	}

	private boolean atLastFloor() {
		return stateManager.atLastFloor();
	}
}
