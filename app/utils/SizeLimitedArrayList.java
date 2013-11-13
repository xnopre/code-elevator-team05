package utils;

import static java.util.Arrays.asList;

import java.util.ArrayList;

public class SizeLimitedArrayList<E> {

	private final ArrayList<E> list = new ArrayList<E>();

	private final int maxSize;

	public SizeLimitedArrayList(int maxSize) {
		this.maxSize = maxSize;
	}

	public void add(E object) {
		list.add(object);
		while (list.size() > maxSize) {
			list.remove(0);
		}
	}

	public int size() {
		return list.size();
	}

	public E[] toArray() {
		return (E[]) list.toArray();
	}

	public void setContent(SizeLimitedArrayList<E> lastCommands) {
		list.clear();
		list.addAll(asList(lastCommands.toArray()));
	}

	@Override
	public String toString() {
		return "SizeLimitedArrayList[maxSize=" + maxSize + "," + list.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + maxSize;
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
		SizeLimitedArrayList other = (SizeLimitedArrayList) obj;
		if (list == null) {
			if (other.list != null) {
				return false;
			}
		} else if (!list.equals(other.list)) {
			return false;
		}
		if (maxSize != other.maxSize) {
			return false;
		}
		return true;
	}

}
