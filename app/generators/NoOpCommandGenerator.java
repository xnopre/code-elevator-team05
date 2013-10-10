package generators;

import utils.Command;

public class NoOpCommandGenerator implements CommandGenerator {

	@Override
	public Command nextCommand() {
		return Command.NOTHING;
	}

}
