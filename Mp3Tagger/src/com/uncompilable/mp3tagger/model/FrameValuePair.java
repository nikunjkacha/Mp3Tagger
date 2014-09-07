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
	public FrameValuePair(Id3Frame frame, String value) {
		this.setFrame(frame);
		this.setValue(value);
	}

	/**
	 * Returns the frame of this pair.
	 * @return - The pair's Id3Frame
	 */
	public Id3Frame getFrame() {
		return mFrame;
	}

	/**
	 * Sets the frame of this pair.
	 * @param frame - The new Id3Frame.
	 */
	public void setFrame(Id3Frame frame) {
		this.mFrame = frame;
	}

	/**
	 * Returns the value of this pair.
	 * @return - The pair's value.
	 */
	public String getValue() {
		return mValue;
	}

	/**
	 * Sets the value of this pair.
	 * @param value - The new value.
	 */
	public void setValue(String value) {
		this.mValue = value;
	}

}
