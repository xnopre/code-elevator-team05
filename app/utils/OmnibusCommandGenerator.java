package utils;

import static utils.Command.CLOSE;
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
		previousCommand = computeNextCommand();
		return previousCommand;
	}

	private Command computeNextCommand() {
		switch (previousCommand) {
		case NOTHING:
			return OPEN;
		case OPEN:
			return CLOSE;
		case CLOSE:
			return UP;
		case UP:
			stateManager.incrementFloor();
			return OPEN;
		default:
			throw new UnsupportedOperationException();
		}
	}
}
