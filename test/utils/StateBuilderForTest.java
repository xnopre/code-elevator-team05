package utils;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.Direction.UP;

import java.util.ArrayList;

public class StateBuilderForTest {

	private final int floor;

	private final boolean opened;

	private final StateManager mockStateManager;

	private Direction direction = UP;

	private Call[] calls;

	private Integer[] floorsToGo;

	private final SizeLimitedArrayList<Command> lastCommands = new SizeLimitedArrayList<Command>(3);

	private boolean isAtMiddleFloor;

	private final FloorBoundaries mockFloorBoundaries;

	public StateBuilderForTest(StateManager mockStateManager, int floor, boolean opened) {
		this.mockStateManager = mockStateManager;
		this.floor = floor;
		this.opened = opened;
		this.mockFloorBoundaries = mock(FloorBoundaries.class);
	}

	public StateBuilderForTest withDirection(Direction direction) {
		this.direction = direction;
		return this;
	}

	public StateBuilderForTest andNoWaitingCalls() {
		return andWaitingCalls();
	}

	public StateBuilderForTest andWaitingCalls(Call... calls) {
		this.calls = calls;
		return this;
	}

	public StateBuilderForTest andGoRequests(Integer... floorToGo) {
		this.floorsToGo = floorToGo;
		return this;
	}

	public StateBuilderForTest andIsNotAtMiddleFloor() {
		isAtMiddleFloor = false;
		return this;
	}

	public StateBuilderForTest andIsAtMiddleFloor() {
		isAtMiddleFloor = true;
		return this;
	}

	public void build() {
		createStateAndDoReturnItByStateManager(mockStateManager, floor, opened, direction, calls, floorsToGo, lastCommands);
		when(mockStateManager.getFloorBoundaries()).thenReturn(mockFloorBoundaries);
		when(mockFloorBoundaries.isAtMiddelFloor(anyInt())).thenReturn(isAtMiddleFloor);
	}

	public static void createStateAndDoReturnItByStateManager(StateManager mockStateManager, int floor, boolean opened, Direction direction, Call[] calls,
			Integer[] floorsToGo, SizeLimitedArrayList lastCommands) {
		final ElevatorState state = new ElevatorState(floor, opened, direction, calls != null ? newArrayList(calls) : new ArrayList<Call>(),
				floorsToGo != null ? newArrayList(floorsToGo) : new ArrayList<Integer>(), lastCommands);
		when(mockStateManager.getCurrentState()).thenReturn(state);

	}

}