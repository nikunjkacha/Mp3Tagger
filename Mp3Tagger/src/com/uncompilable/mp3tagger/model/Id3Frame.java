package com.uncompilable.mp3tagger.model;

/**
 * Enum containing all the supported id3 Frames
 * @author dennis
 *
 */
public enum Id3Frame {
	TITLE(1),
	ARTIST(2),
	ALBUM_TITLE(4),
	COMPOSER(8),
	TRACK_NUMBER(16),
	GENRE(32);
	
	private int selectionValue;
	
	Id3Frame(int selectionValue) {
		this.selectionValue = selectionValue;
	}
	
	public int getSelectionValue() {
		return this.selectionValue;
	}
}
