package com.uncompilable.mp3tagger.model;

/**
 * Enum containing all the supported id3 Frames
 * @author dennis
 *
 */
public enum Id3Frame {
	TITLE(1),
	ARTIST(2),
	ALBUM_TITLE(3),
	COMPOSER(4),
	TRACK_NUMBER(5),
	GENRE(6),
	YEAR(7),
	ALBUM_ARTIST(8);

	private int selectionValue;

	Id3Frame(final int selectionValue) {
		this.selectionValue = (int)Math.pow(2, selectionValue);
	}

	public int getSelectionValue() {
		return this.selectionValue;
	}
}
