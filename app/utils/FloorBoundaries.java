package utils;

import com.google.common.collect.Range;

public class FloorBoundaries {

	private final Range<Integer> floorRange;

	public FloorBoundaries(int firstFloor, int lastFloor) {
		ensureAtLeastTwoFloors(firstFloor, lastFloor);
		this.floorRange = Range.closed(firstFloor, lastFloor);
	}

	private void ensureAtLeastTwoFloors(int firstFloor, int lastFloor) {
		if (firstFloor == lastFloor)
			throw new IllegalArgumentException("At least 2 floors are required");
	}

	public boolean atFirstFloor(int floor) {
		verifyIsInBoundariesOrDie(floor);

		return atFirst(floor);
	}

	private boolean atFirst(int floor) {
		return floorRange.lowerEndpoint() == floor;
	}

	public boolean atLastFloor(int floor) {
		verifyIsInBoundariesOrDie(floor);
		return atLast(floor);
	}

	private boolean atLast(int floor) {
		return floorRange.upperEndpoint() == floor;
	}

	private void verifyIsInBoundariesOrDie(int floor) {
		if (!floorRange.contains(floor)) {
			throw new IllegalArgumentException();
		}
	}
}
