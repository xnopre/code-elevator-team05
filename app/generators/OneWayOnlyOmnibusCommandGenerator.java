package generators;

import static utils.Command.CLOSE;
import static utils.Command.OPEN;
import static utils.Command.UP;
import utils.Command;

public class OneWayOnlyOmnibusCommandGenerator implements CommandGenerator {

	private Command previousCommand = Command.NOTHING;

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
			return OPEN;
		default:
			throw new UnsupportedOperationException();
		}
	}
}
