package com.uncompilable.mp3tagger.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.TreeMap;

import android.util.Log;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.uncompilable.mp3tagger.error.InvalidFileException;
import com.uncompilable.mp3tagger.error.NoTagAssociatedWithFileException;
import com.uncompilable.mp3tagger.utility.Constants;


/**
 * Model class for handling file selections.
 * @author dennis
 *
 */
public class FileSelection extends Observable {
	private final TagCloud mTagCloud;
	private final Map<File, List<File>> mFileSelection;

	/**
	 * Creates a new, empty Selection
	 */
	public FileSelection() {
		super();
		this.mTagCloud = new TagCloud();
		this.mFileSelection = new TreeMap<File, List<File>>();
	}

	/**
	 * Adds a file to the selection and associates the given tag with it
	 * @param file - The file to add
	 * @param tag - The tag to associate with the file
	 * @throws InvalidFileException - If the file is not a valid mp3-file
	 */
	public void addFile(final File file, final ID3v2 tag) throws InvalidFileException {
		if (this.mFileSelection.containsKey(file)) { //Mustn't add multiple copies of a file
			Log.w(Constants.MAIN_TAG, "Tried to add file " + file.getName() + " that was already selected.");
			return;
		}

		try {
			this.mTagCloud.addEntry(file, tag);
		} catch (final NoTagAssociatedWithFileException e) {
			Log.w(Constants.MAIN_TAG, "WARNING: Tried to add file " + file.getName() + " without a valid ID3Tag.");
			this.addFile(file, new ID3v24Tag());
		}

		final List<File> parents = new ArrayList<File>();
		File parent = file.getParentFile();
		while (parent != null) {
			parents.add(parent);
			parent = parent.getParentFile();
		}
		this.mFileSelection.put(file, parents);

		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Removes a file and its associated Tag from this selection
	 * @param file - The file to be removed
	 */
	public void removeFile(final File file) {
		if (this.mFileSelection.containsKey(file)) {
			this.mFileSelection.remove(file);
			this.mTagCloud.removeEntry(file);

			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * If the parameter is a file, returns whether the file is contained in the selection.
	 * If the parameter is a directory, returns whether this selection contains a file that is in the directory
	 * @param file - The file to look for
	 * @return
	 */
	public boolean contains(final File file) {
		if (file.isFile())
			return this.mFileSelection.containsKey(file);
		else {
			for (final List<File> query : this.mFileSelection.values()) {
				if (query.contains(file)) return true;
			}
			return false;
		}
	}

	/**
	 * Returns the files in this selection in a set.
	 * @return - The Files
	 */
	public Set<File> getFileSet() {
		return this.mFileSelection.keySet();
	}

	/**
	 * Returns the TagCloud for this selection.
	 * @return - The TagCloud
	 */
	public TagCloud getTagCloud() {
		return this.mTagCloud;
	}
}
