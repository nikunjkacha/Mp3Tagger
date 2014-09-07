package com.uncompilable.mp3tagger.model;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import org.farng.mp3.AbstractMP3Tag;
import org.farng.mp3.id3.ID3v1;

import android.util.Log;

import com.uncompilable.mp3tagger.error.InvalidFileException;
import com.uncompilable.mp3tagger.error.NoTagAssociatedWithFileException;
import com.uncompilable.mp3tagger.utility.Constants;

/**
 * Model class for handling file selections.
 * @author dennis
 *
 */
public class FileSelection {
	private TagCloud mTagCloud;
	private Set<File> mFileSelection;
	
	/**
	 * Creates a new, empty Selection
	 */
	public FileSelection() {
		mTagCloud = new TagCloud();
		mFileSelection = new TreeSet<File>();
	}
	
	/**
	 * Adds a file to the selection and associates the given tag with it
	 * @param file - The file to add
	 * @param tag - The tag to associate with the file
	 * @throws InvalidFileException - If the file is not a valid mp3-file
	 */
	public void addFile(File file, AbstractMP3Tag tag) throws InvalidFileException {
		if (mFileSelection.contains(file)) { //Mustn't add multiple copies of a file
			Log.w(Constants.MAIN_TAG, "Tried to add file " + file.getName() + " that was already selected.");
			return;
		}
		
		try {
			mTagCloud.addEntry(file, tag);
		} catch (NoTagAssociatedWithFileException e) {
			Log.w(Constants.MAIN_TAG, "WARNING: Tried to add file " + file.getName() + " without a valid ID3Tag.");
			addFile(file, new ID3v1());
		}
		mFileSelection.add(file);
	}
	
	/**
	 * Removes a file and its associated Tag from this selection
	 * @param file - The file to be removed
	 */
	public void removeFile(File file) {
		if (mFileSelection.contains(file)) {
			mFileSelection.remove(file);
			mTagCloud.removeEntry(file);
		}
	}
	
	/**
	 * Returns the files in this selection in a set.
	 * @return - The Files
	 */
	public Set<File> getSelection() {
		return mFileSelection;
	}
	
	/**
	 * Returns the TagCloud for this selection.
	 * @return - The TagCloud
	 */
	public TagCloud getTagCloud() {
		return mTagCloud;
	}
}
