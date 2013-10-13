package utils;

import static com.google.common.collect.Lists.newArrayList;
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

	public StateBuilderForTest(StateManager mockStateManager, int floor, boolean opened) {
		this.mockStateManager = mockStateManager;
		this.floor = floor;
		this.opened = opened;
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

	public void build() {
		createStateAndDoReturnItByStateManager(mockStateManager, floor, opened, direction, calls, floorsToGo);
	}

	public static void createStateAndDoReturnItByStateManager(StateManager mockStateManager, int floor, boolean opened, Direction direction, Call[] calls,
			Integer[] floorsToGo) {
		final ElevatorState state = new ElevatorState(floor, opened, direction, calls != null ? newArrayList(calls) : new ArrayList<Call>(),
				floorsToGo != null ? newArrayList(floorsToGo) : new ArrayList<Integer>());
		when(mockStateManager.getCurrentState()).thenReturn(state);

	}

}