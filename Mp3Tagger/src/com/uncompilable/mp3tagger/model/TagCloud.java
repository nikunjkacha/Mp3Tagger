package com.uncompilable.mp3tagger.model;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.farng.mp3.AbstractMP3Tag;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import com.uncompilable.mp3tagger.error.InvalidFileException;
import com.uncompilable.mp3tagger.error.NoTagAssociatedWithFileException;

/**
 * Container class for handling id3 tags of a set of files.
 * @author dennis
 *
 */
class TagCloud {
	private Map<File, AbstractMP3Tag> mFileTagMap;
	private Map<Id3Frame, Multiset<String>> mFrameValueMap;
	
	private int mSelectedFields;

	/**
	 * Creates a new TagCloud with an empty file- and tagMap
	 */
	public TagCloud() {
		mFileTagMap = new HashMap<File, AbstractMP3Tag>();
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
		
		//Mark changed frame as selected
		mSelectedFields |=value.getFrame().getSelectionValue();
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
	public void addEntry(File file, AbstractMP3Tag tag) throws InvalidFileException, NoTagAssociatedWithFileException {
		if (file == null) throw new InvalidFileException();
		if (mFileTagMap.containsKey(file)) removeEntry(file);
		mFileTagMap.put(file, tag);

		//Adjust the frameValueMap
		if (tag.getSongTitle      	 () != null) mFrameValueMap.get(Id3Frame.TITLE       ).add(tag.getSongTitle     	());
		if (tag.getLeadArtist    	 () != null) mFrameValueMap.get(Id3Frame.ARTIST      ).add(tag.getLeadArtist    	());
		if (tag.getAlbumTitle    	 () != null) mFrameValueMap.get(Id3Frame.ALBUM_TITLE ).add(tag.getAlbumTitle    	());
		if (tag.getAuthorComposer	 () != null) mFrameValueMap.get(Id3Frame.COMPOSER    ).add(tag.getAuthorComposer	());
		if (tag.getSongGenre     	 () != null) mFrameValueMap.get(Id3Frame.GENRE       ).add(tag.getSongGenre     	());
		if (tag.getTrackNumberOnAlbum() != null) mFrameValueMap.get(Id3Frame.TRACK_NUMBER).add(tag.getTrackNumberOnAlbum());
	}

	/**
	 * Removes a file entry from this TagCloud and adjusts the frameValueMap accordingly.
	 * @param file - The file to be removed
	 * @throws InvalidFileException - If the file is null
	 */
	public void removeEntry(File file) {
		AbstractMP3Tag tag = mFileTagMap.remove(file);
		if (tag == null) return;
		
		//Adjust the frameValueMap
		if (tag.getSongTitle      	 () != null) mFrameValueMap.get(Id3Frame.TITLE       ).remove(tag.getSongTitle     	());
		if (tag.getLeadArtist    	 () != null) mFrameValueMap.get(Id3Frame.ARTIST      ).remove(tag.getLeadArtist    	());
		if (tag.getAlbumTitle    	 () != null) mFrameValueMap.get(Id3Frame.ALBUM_TITLE ).remove(tag.getAlbumTitle    	());
		if (tag.getAuthorComposer	 () != null) mFrameValueMap.get(Id3Frame.COMPOSER    ).remove(tag.getAuthorComposer	());
		if (tag.getSongGenre     	 () != null) mFrameValueMap.get(Id3Frame.GENRE       ).remove(tag.getSongGenre     	());
		if (tag.getTrackNumberOnAlbum() != null) mFrameValueMap.get(Id3Frame.TRACK_NUMBER).remove(tag.getTrackNumberOnAlbum());
	}
	
	/**
	 * Writes every value that has been set via the setValue-Method into the tags of the fileTagMap.
	 */
	public void writeLocalChanges() {
		for (AbstractMP3Tag tag : mFileTagMap.values()) {
			if ((mSelectedFields & Id3Frame.TITLE.getSelectionValue()) != 0) {
				tag.setSongTitle((String) mFrameValueMap.get(Id3Frame.TITLE).toArray()[0]);
			}
			if ((mSelectedFields & Id3Frame.ARTIST.getSelectionValue()) != 0) {
				tag.setLeadArtist((String) mFrameValueMap.get(Id3Frame.ARTIST).toArray()[0]);
			}
			if ((mSelectedFields & Id3Frame.ALBUM_TITLE.getSelectionValue()) != 0) {
				tag.setAlbumTitle((String) mFrameValueMap.get(Id3Frame.ALBUM_TITLE).toArray()[0]);
			}
			if ((mSelectedFields & Id3Frame.COMPOSER.getSelectionValue()) != 0) {
				tag.setAuthorComposer((String) mFrameValueMap.get(Id3Frame.COMPOSER).toArray()[0]);
			}
			if ((mSelectedFields & Id3Frame.GENRE.getSelectionValue()) != 0) {
				tag.setSongGenre((String) mFrameValueMap.get(Id3Frame.GENRE).toArray()[0]);
			}
			if ((mSelectedFields & Id3Frame.TRACK_NUMBER.getSelectionValue()) != 0) {
				tag.setTrackNumberOnAlbum((String) mFrameValueMap.get(Id3Frame.TRACK_NUMBER).toArray()[0]);
			}
		}
	}
}
