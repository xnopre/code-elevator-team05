package utils;

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
}
