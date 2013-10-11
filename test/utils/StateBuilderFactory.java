package utils;

public class StateBuilderFactory {

	private final StateManager mockStateManager;

	public StateBuilderFactory(StateManager mockStateManager) {
		super();
		this.mockStateManager = mockStateManager;
	}

	public StateBuilderForTest givenAnElevatorClosedAtFloor(int floor) {
		return new StateBuilderForTest(mockStateManager, floor, false);
	}

	public StateBuilderForTest givenAnElevatorOpenedAtFloor(int floor) {
		return new StateBuilderForTest(mockStateManager, floor, true);
	}

	public static Call call(final int floor, final Direction direction) {
		return new Call(floor, direction);
	}

}
