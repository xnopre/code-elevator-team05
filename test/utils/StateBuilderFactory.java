package utils;

public class StateBuilderFactory {

	private final StateManager mockStateManager;

	public StateBuilderFactory(StateManager mockStateManager) {
		super();
		this.mockStateManager = mockStateManager;
	}

	public StateBuilder givenAnElevatorClosedAtFloor(int floor) {
		return new StateBuilder(mockStateManager, floor, false);
	}

	public StateBuilder givenAnElevatorOpenedAtFloor(int floor) {
		return new StateBuilder(mockStateManager, floor, true);
	}

	public static Call call(final int floor, final Direction direction) {
		return new Call(floor, direction);
	}

}
