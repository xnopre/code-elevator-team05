package utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.Direction.UP;

import java.util.Arrays;

public class StateBuilderForTest {

	private final int floor;

	private final boolean opened;

	private final StateManager mockStateManager;

	private Direction direction = UP;

	private Call[] calls;

	private Integer[] floorsToGo;

	private final SizeLimitedArrayList<Command> lastCommands = new SizeLimitedArrayList<Command>(3);

	private final FloorBoundaries mockFloorBoundaries;

	private int middleFloor;

	private boolean cabinFull;

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

	public StateBuilderForTest andCabinFull() {
		cabinFull = true;
		return this;
	}

	public StateBuilderForTest andMiddleFloorIs(int middleFloor) {
		this.middleFloor = middleFloor;
		return this;
	}

	public void build() {
		createStateAndDoReturnItByStateManager(mockStateManager, floor, opened, direction, calls, floorsToGo, lastCommands);
		when(mockStateManager.getFloorBoundaries()).thenReturn(mockFloorBoundaries);
		when(mockStateManager.isCabinFull(0)).thenReturn(cabinFull);
		when(mockFloorBoundaries.getMiddelFloor()).thenReturn(middleFloor);
	}

	public static void createStateAndDoReturnItByStateManager(StateManager mockStateManager, int currentFloor, boolean opened, Direction direction,
			Call[] calls, Integer[] floorsToGo, SizeLimitedArrayList lastCommands) {
		final ElevatorState elevatorState = new ElevatorState(1);
		elevatorState.setCurrentFloor(0, currentFloor);
		if (calls != null) {
			for (Call call : calls) {
				elevatorState.addWaitingCall(call.floor, call.direction);
			}
		}
		if (opened) {
			elevatorState.setOpened(0);
		} else {
			elevatorState.setClosed(0);
		}
		elevatorState.setCurrentDirection(0, direction);
		if (floorsToGo != null) {
			elevatorState.getGoRequests(0).addAll(Arrays.asList(floorsToGo));
		}
		when(mockStateManager.getCurrentState()).thenReturn(elevatorState);

	}

}