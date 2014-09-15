package com.uncompilable.mp3tagger.utility;

import java.util.Observable;

/**
 * Simple class that holds an Array and makes it Observable
 * @author dennis
 *
 * @param <T>
 */
public class ObservableArray<T> extends Observable {
	private final T[] mArray;

	public ObservableArray(final T[] array) {
		super();
		this.mArray = array.clone();
	}

	public void set(final int pos, final T item) {
		this.mArray[pos] = item;

		this.setChanged();
		this.notifyObservers();
	}

	public T get(final int pos) {
		return this.mArray[pos];
	}

	public int length() {
		return this.mArray.length;
	}

	public T[] returnArray() {
		return this.mArray.clone();
	}
}
