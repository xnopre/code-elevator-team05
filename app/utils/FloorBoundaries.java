package utils;

import com.google.common.collect.Range;

public class FloorBoundaries {

	private Range<Integer> floorRange;

	public FloorBoundaries(int firstFloor, int lastFloor) {
		ensureAtLeastTwoFloors(firstFloor, lastFloor);
		setRange(firstFloor, lastFloor);
	}

	public void setRange(int firstFloor, int lastFloor) {
		this.floorRange = Range.closed(firstFloor, lastFloor);
	}

	public boolean atFirstFloor(int floor) {
		verifyIsInBoundariesOrDie(floor);
		return atFirst(floor);
	}

	public boolean atLastFloor(int floor) {
		verifyIsInBoundariesOrDie(floor);
		return atLast(floor);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((floorRange == null) ? 0 : floorRange.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FloorBoundaries other = (FloorBoundaries) obj;
		if (floorRange == null) {
			if (other.floorRange != null) {
				return false;
			}
		} else if (!floorRange.equals(other.floorRange)) {
			return false;
		}
		return true;
	}

	// privates ------------------------------

	private boolean atFirst(int floor) {
		return floorRange.lowerEndpoint() == floor;
	}

	private boolean atLast(int floor) {
		return floorRange.upperEndpoint() == floor;
	}

	private void verifyIsInBoundariesOrDie(int floor) {
		if (!floorRange.contains(floor)) {
			throw new IllegalArgumentException();
		}
	}

	private void ensureAtLeastTwoFloors(int firstFloor, int lastFloor) {
		if (firstFloor == lastFloor)
			throw new IllegalArgumentException("At least 2 floors are required");
	}
}
