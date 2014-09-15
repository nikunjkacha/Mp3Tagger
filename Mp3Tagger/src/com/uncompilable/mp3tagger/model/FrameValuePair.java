package com.uncompilable.mp3tagger.model;

/**
 * Container for a Id3Frame and an associated value, encoded as a String
 * @author dennis
 *
 */
public class FrameValuePair {
	private Id3Frame mFrame;
	private String mValue;

	/**
	 * Creates a new FrameValuePair.
	 * @param frame - The Frame of the pair
	 * @param value - The value associated with the frame
	 */
	public FrameValuePair(final Id3Frame frame, final String value) {
		this.setFrame(frame);
		this.setValue(value);
	}

	/**
	 * Returns the frame of this pair.
	 * @return - The pair's Id3Frame
	 */
	public Id3Frame getFrame() {
		return this.mFrame;
	}

	/**
	 * Sets the frame of this pair.
	 * @param frame - The new Id3Frame.
	 */
	public void setFrame(final Id3Frame frame) {
		this.mFrame = frame;
	}

	/**
	 * Returns the value of this pair.
	 * @return - The pair's value.
	 */
	public String getValue() {
		return this.mValue;
	}

	/**
	 * Sets the value of this pair.
	 * @param value - The new value.
	 */
	public void setValue(final String value) {
		this.mValue = value;
	}

}
