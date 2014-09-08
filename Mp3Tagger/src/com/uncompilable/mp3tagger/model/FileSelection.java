package com.uncompilable.mp3tagger.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
	private Map<File, List<File>> mFileSelection;
	
	/**
	 * Creates a new, empty Selection
	 */
	public FileSelection() {
		mTagCloud = new TagCloud();
		mFileSelection = new TreeMap<File, List<File>>();
	}
	
	/**
	 * Adds a file to the selection and associates the given tag with it
	 * @param file - The file to add
	 * @param tag - The tag to associate with the file
	 * @throws InvalidFileException - If the file is not a valid mp3-file
	 */
	public void addFile(File file, AbstractMP3Tag tag) throws InvalidFileException {
		if (mFileSelection.containsKey(file)) { //Mustn't add multiple copies of a file
			Log.w(Constants.MAIN_TAG, "Tried to add file " + file.getName() + " that was already selected.");
			return;
		}
		
		try {
			mTagCloud.addEntry(file, tag);
		} catch (NoTagAssociatedWithFileException e) {
			Log.w(Constants.MAIN_TAG, "WARNING: Tried to add file " + file.getName() + " without a valid ID3Tag.");
			addFile(file, new ID3v1());
		}
		
		List<File> parents = new ArrayList<File>();
		File parent = file.getParentFile();
		while (parent != null) {
			parents.add(parent);
			parent = parent.getParentFile();
		}
		mFileSelection.put(file, parents);
	}
	
	/**
	 * Removes a file and its associated Tag from this selection
	 * @param file - The file to be removed
	 */
	public void removeFile(File file) {
		if (mFileSelection.containsKey(file)) {
			mFileSelection.remove(file);
			mTagCloud.removeEntry(file);
		}
	}
	
	/**
	 * If the parameter is a file, returns whether the file is contained in the selection.
	 * If the parameter is a directory, returns whether this selection contains a file that is in the directory
	 * @param file - The file to look for
	 * @return 
	 */
	public boolean contains(File file) {
		if (file.isFile()) {
			return mFileSelection.containsKey(file);
		} else {
			for (List<File> query : mFileSelection.values()) {
				if (query.contains(file)) return true;
			}
			return false;
		}
	}
	
	/**
	 * Returns the files in this selection in a set.
	 * @return - The Files
	 */
	public Set<File> getSelection() {
		return mFileSelection.keySet();
	}
	
	/**
	 * Returns the TagCloud for this selection.
	 * @return - The TagCloud
	 */
	public TagCloud getTagCloud() {
		return mTagCloud;
	}
}
