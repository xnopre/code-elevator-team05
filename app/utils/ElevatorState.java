package utils;

public class ElevatorState {

	public int currentFloor;

	public int currentUsers;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElevatorState other = (ElevatorState) obj;
		if (currentFloor != other.currentFloor)
			return false;
		if (currentUsers != other.currentUsers)
			return false;
		return true;
	}

}
