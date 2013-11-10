package utils;

import org.apache.commons.lang.builder.ToStringBuilder;

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

	public boolean isAtFirstFloor(int floor) {
		verifyIsInBoundariesOrDie(floor);
		return _isAtFirstFloor(floor);
	}

	public boolean isAtLastFloor(int floor) {
		verifyIsInBoundariesOrDie(floor);
		return _isAtLastFloor(floor);
	}

	public int getMiddelFloor() {
		return floorRange.lowerEndpoint() + (floorRange.upperEndpoint() - floorRange.lowerEndpoint()) / 2;
	}

	public int calculateFloorsNumber() {
		return floorRange.upperEndpoint() - floorRange.lowerEndpoint() + 1;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	// privates ------------------------------

	private boolean _isAtFirstFloor(int floor) {
		return floorRange.lowerEndpoint() == floor;
	}

	private boolean _isAtLastFloor(int floor) {
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
