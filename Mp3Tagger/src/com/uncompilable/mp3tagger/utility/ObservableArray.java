package com.uncompilable.mp3tagger.utility;

import java.util.Observable;

/**
 * Simple class that holds an Array and makes it Observable
 * @author dennis
 *
 * @param <T>
 */
public class ObservableArray<T> extends Observable {
	private T[] mArray;
	
	public ObservableArray(T[] array) {
		this.mArray = array;
	}
	
	public void set(int pos, T item) {
		this.mArray[pos] = item;
		
		setChanged();
		notifyObservers();
	}
	
	public T get(int pos) {
		return this.mArray[pos];
	}
	
	public int length() {
		return this.mArray.length;
	}
	
	public T[] returnArray() {
		return this.mArray;
	}
}
