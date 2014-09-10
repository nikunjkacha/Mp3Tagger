package com.uncompilable.mp3tagger.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import com.mpatric.mp3agic.ID3v1Genres;
import com.mpatric.mp3agic.ID3v2;
import com.uncompilable.mp3tagger.error.InvalidFileException;
import com.uncompilable.mp3tagger.error.NoTagAssociatedWithFileException;

/**
 * Container class for handling id3 tags of a set of files.
 * @author dennis
 *
 */
public class TagCloud {
	private Map<File, ID3v2> mFileTagMap;
	private final Map<Id3Frame, Multiset<String>> mFrameValueMap;
	
	private int mSelectedFields;

	/**
	 * Creates a new TagCloud with an empty file- and tagMap
	 */
	protected TagCloud() {
		mFileTagMap = new HashMap<File, ID3v2>();
		mFrameValueMap = new EnumMap<Id3Frame, Multiset<String>>(Id3Frame.class);

		for (Id3Frame frame : Id3Frame.values()) {
			mFrameValueMap.put(frame, TreeMultiset.<String>create());
		}
		
		mSelectedFields = 0;
	}

	/**
	 * Sets the value of the Id3Frame given in the FrameValuePair to the associated Value.
	 * Careful: Deletes all values that were previously associated with this frame! 
	 * @param value - The FrameValuePair containing the new Value.
	 */
	public void setValue(FrameValuePair value) {
		Multiset<String> newValues = TreeMultiset.<String>create();
		newValues.add(value.getValue());
		mFrameValueMap.put(value.getFrame(), newValues);
	}
	
	/**
	 * Returns the Collection of Values associated with an ID3Tag.
	 * @param frame - The frame whose values are to be returned
	 * @return - The associated values. If there are no associated values: returns an empty Collection.
	 */
	public Collection<String> getValues(Id3Frame frame) {
		Collection<String> values = mFrameValueMap.get(frame).elementSet();
		return values == null? new ArrayList<String>() : values;
	}
	
	/**
	 * Sets an Id3Frame as selected. 
	 * WriteLocalChanges() will now write this frame.
	 * @param frame - The Frame to select
	 */
	public void setFrameSelected(Id3Frame frame) {
		mSelectedFields |= frame.getSelectionValue();
	}
	
	/**
	 * Sets an Id3Frame as unselected.
	 * WriteLocalChanges() will now not write this frame.
	 * @param frame - The Frame to unselect
	 */
	public void setFrameUnselected(Id3Frame frame) {
		mSelectedFields &= (0xFFFF - frame.getSelectionValue());
	}
	
	/**
	 * Returns true if the specified frame is currently selected
	 * @param frame
	 * @return
	 */
	public boolean isSelected(Id3Frame frame) {
		return (mSelectedFields & frame.getSelectionValue()) != 0;
	}

	/**
	 * Adds an Entry to the TagCloud.
	 * Associates the given file with the tag in the fileTagMap.
	 * Reads out all id3Frames from the tag-Object and puts them into the frameValueMap.
	 * Overwrites the current File Association if the file is already in the fileTagMap.
	 * @param file - The file of the Entry
	 * @param tag - The Id3Tag associated with the file
	 * @throws InvalidFileException - If the file is null
	 * @throws NoTagAssociatedWithFileException - If the tag is not valid
	 */
	protected void addEntry(File file, ID3v2 tag) throws InvalidFileException, NoTagAssociatedWithFileException {
		if (file == null) throw new InvalidFileException();
		if (mFileTagMap.containsKey(file)) removeEntry(file);
		mFileTagMap.put(file, tag);
		
		//Adjust the frameValueMap
		if (validValue(tag.getTitle   		  ())) mFrameValueMap.get(Id3Frame.TITLE       ).add(tag.getTitle    	    ());
		if (validValue(tag.getArtist  		  ())) mFrameValueMap.get(Id3Frame.ARTIST      ).add(tag.getArtist  		()); 
		if (validValue(tag.getAlbum   		  ())) mFrameValueMap.get(Id3Frame.ALBUM_TITLE ).add(tag.getAlbum   		());
		if (validValue(tag.getComposer		  ())) mFrameValueMap.get(Id3Frame.COMPOSER    ).add(tag.getComposer		());
		if (validValue(tag.getGenreDescription())) mFrameValueMap.get(Id3Frame.GENRE       ).add(tag.getGenreDescription());
		if (validValue(tag.getTrack			  ())) mFrameValueMap.get(Id3Frame.TRACK_NUMBER).add(tag.getTrack			());
	}

	/**
	 * Removes a file entry from this TagCloud and adjusts the frameValueMap accordingly.
	 * @param file - The file to be removed
	 * @throws InvalidFileException - If the file is null
	 */
	protected void removeEntry(File file) {
		ID3v2 tag = mFileTagMap.remove(file);
		if (tag == null) return;
		
		//Adjust the frameValueMap
		if (validValue(tag.getTitle   		  ())) mFrameValueMap.get(Id3Frame.TITLE       ).remove(tag.getTitle    	    ());
		if (validValue(tag.getArtist  		  ())) mFrameValueMap.get(Id3Frame.ARTIST      ).remove(tag.getArtist  		()); 
		if (validValue(tag.getAlbum   		  ())) mFrameValueMap.get(Id3Frame.ALBUM_TITLE ).remove(tag.getAlbum   		());
		if (validValue(tag.getComposer		  ())) mFrameValueMap.get(Id3Frame.COMPOSER    ).remove(tag.getComposer		());
		if (validValue(tag.getGenreDescription())) mFrameValueMap.get(Id3Frame.GENRE       ).remove(tag.getGenreDescription());
		if (validValue(tag.getTrack			  ())) mFrameValueMap.get(Id3Frame.TRACK_NUMBER).remove(tag.getTrack			());
	}
	
	/**
	 * Writes every value that has been set via the setValue-Method into the tags of the fileTagMap.
	 */
	public void writeLocalChanges() {
		for (ID3v2 tag : mFileTagMap.values()) {
			if ((mSelectedFields & Id3Frame.TITLE.getSelectionValue()) != 0) {
				tag.setTitle((String) mFrameValueMap.get(Id3Frame.TITLE).toArray()[0]);
			}
			if ((mSelectedFields & Id3Frame.ARTIST.getSelectionValue()) != 0) {
				tag.setArtist((String) mFrameValueMap.get(Id3Frame.ARTIST).toArray()[0]);
			}
			if ((mSelectedFields & Id3Frame.ALBUM_TITLE.getSelectionValue()) != 0) {
				tag.setAlbum((String) mFrameValueMap.get(Id3Frame.ALBUM_TITLE).toArray()[0]);
			}
			if ((mSelectedFields & Id3Frame.COMPOSER.getSelectionValue()) != 0) {
				tag.setComposer((String) mFrameValueMap.get(Id3Frame.COMPOSER).toArray()[0]);
			}
			if ((mSelectedFields & Id3Frame.GENRE.getSelectionValue()) != 0) {
				tag.setGenreDescription((String) mFrameValueMap.get(Id3Frame.GENRE).toArray()[0]);
				tag.setGenre(ID3v1Genres.matchGenreDescription((String)mFrameValueMap.get(Id3Frame.GENRE).toArray()[0]));
			}
			if ((mSelectedFields & Id3Frame.TRACK_NUMBER.getSelectionValue()) != 0) {
				tag.setTrack((String) mFrameValueMap.get(Id3Frame.TRACK_NUMBER).toArray()[0]);
			}
		}
	}
	
	/**
	 * Returns the fileTagMap of the TagCloud.
	 * @return - The FileTagMap
	 */
	public Map<File, ID3v2> getTagMap() {
		return this.mFileTagMap;
	}
	
	private boolean validValue(String value) {
		return value != null && !value.isEmpty();
	}
}
