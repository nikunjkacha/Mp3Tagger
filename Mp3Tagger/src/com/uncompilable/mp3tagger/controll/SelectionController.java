package com.uncompilable.mp3tagger.controll;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.uncompilable.mp3tagger.error.InvalidFileException;
import com.uncompilable.mp3tagger.error.NoTagAssociatedWithFileException;
import com.uncompilable.mp3tagger.model.FileSelection;
import com.uncompilable.mp3tagger.model.Id3Frame;
import com.uncompilable.mp3tagger.utility.Constants;

import android.content.SharedPreferences;

public class SelectionController {
	private final IOController mIOController;
	private final FileSelection mSelection;
	private final AlbumCoverController mAlbumController;

	final FileFilter mp3Filter;
	final FileFilter mp3OrDirectoryFilter;


	private final SharedPreferences mPreferences;

	public static final String[] COVER_NAMES = new String[] {"AlbumArt.jpg", "cover.jpg"};

	public SelectionController(SharedPreferences prefs) {
		super();

		mIOController = new IOController(this);
		mAlbumController = new AlbumCoverController(this);

		mSelection = new FileSelection();

		this.mPreferences = prefs;


		mp3Filter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file != null && file.isFile() && file.getName().endsWith(".mp3");
			}
		};

		mp3OrDirectoryFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file != null && (file.isDirectory() || mp3Filter.accept(file));
			}
		};
	}

	public void addToSelection(File file) throws InvalidFileException, IOException, NoTagAssociatedWithFileException, UnsupportedTagException, InvalidDataException {
		for (File toAdd : scanDirectory(file)) {
			mSelection.addFile(toAdd, mIOController.readFile(toAdd));
		}
		
		scanForCovers();
	}

	public void removeFromSelection(File file) {
		for (File toRemove : scanDirectory(file)) {
			mSelection.removeFile(toRemove);
		}
		
		scanForCovers();
	}

	private void scanForCovers() {
		if (mSelection.getTagCloud().getValues(Id3Frame.ALBUM_TITLE).size() != 1) {
			mSelection.getTagCloud().setCoverFile(null);
		}
		
		Set<File> directories = new HashSet<File>();
		for (File file : mSelection.getFileSet()) {
			directories.add(file.getParentFile());
		}

		for (File dir : directories) {
			if (!dir.isDirectory()) break;

			for (File file : dir.listFiles()) {
				for (String name : COVER_NAMES) {
					if (name.equalsIgnoreCase(file.getName())) {
						mSelection.getTagCloud().setCoverFile(file);
						return;
					}
				}
			}
		}
		
		mSelection.getTagCloud().setCoverFile(null);
	}

	public SharedPreferences getPrefs() {
		return this.mPreferences;
	}

	public IOController getIOController() {
		return this.mIOController;
	}

	public AlbumCoverController getAlbumCoverController() {
		return this.mAlbumController;
	}

	public FileSelection getSelection() {
		return mSelection;
	}


	public List<File> scanDirectory(File file) {
		return scanDirectory(file, Integer.parseInt(mPreferences.getString(Constants.PREF_KEY_RECURSION, "5")));
	}

	private List<File> scanDirectory(File file, int n) {
		List<File> result = new ArrayList<File>();
		if (file.isFile() && mp3Filter.accept(file)) {
			result.add(file);
		} else if (n > 0 && file.isDirectory()) {
			result.addAll(Arrays.asList(file.listFiles(mp3OrDirectoryFilter)));
			List<File> toAdd = new ArrayList<File>();
			List<File> toRemove = new ArrayList<File>();

			for (File subItem : result) {
				if (subItem.isDirectory()) {
					toRemove.add(subItem);
					toAdd.addAll(scanDirectory(subItem, n-1));
				}
			}

			result.removeAll(toRemove);
			result.addAll(toAdd);
		}
		return result;
	}
}
