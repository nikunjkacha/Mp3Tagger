package com.uncompilable.mp3tagger.controll;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.SharedPreferences;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.uncompilable.mp3tagger.error.InvalidFileException;
import com.uncompilable.mp3tagger.error.NoTagAssociatedWithFileException;
import com.uncompilable.mp3tagger.model.FileSelection;
import com.uncompilable.mp3tagger.model.Id3Frame;
import com.uncompilable.mp3tagger.utility.Constants;

public class SelectionController {
	private final IOController mIOController;
	private final FileSelection mSelection;
	private final AlbumCoverController mAlbumController;

	private final FileFilter mp3Filter;
	private final FileFilter mp3OrDirectoryFilter;


	private final SharedPreferences mPreferences;

	public static final String[] COVER_NAMES = new String[] {"AlbumArt.jpg", "cover.jpg"};

	public SelectionController(final SharedPreferences prefs) {
		super();

		this.mIOController = new IOController(this);
		this.mAlbumController = new AlbumCoverController(this);

		this.mSelection = new FileSelection();

		this.mPreferences = prefs;


		this.mp3Filter = new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return file != null && file.isFile() && file.getName().endsWith(".mp3");
			}
		};

		this.mp3OrDirectoryFilter = new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return file != null && (file.isDirectory() || SelectionController.this.mp3Filter.accept(file));
			}
		};
	}

	public void addToSelection(final File file) throws InvalidFileException, IOException, NoTagAssociatedWithFileException, UnsupportedTagException, InvalidDataException {
		for (final File toAdd : this.scanDirectory(file)) {
			this.mSelection.addFile(toAdd, this.mIOController.readFile(toAdd));
		}

		this.scanForCovers();
	}

	public void removeFromSelection(final File file) {
		for (final File toRemove : this.scanDirectory(file)) {
			this.mSelection.removeFile(toRemove);
		}

		this.scanForCovers();
	}

	private void scanForCovers() {
		if (this.mSelection.getTagCloud().getValues(Id3Frame.ALBUM_TITLE).size() != Constants.ONE_ITEM) {
			this.mSelection.getTagCloud().setCoverFile(null);
		}

		final Set<File> directories = new HashSet<File>();
		for (final File file : this.mSelection.getFileSet()) {
			directories.add(file.getParentFile());
		}

		for (final File dir : directories) {
			if (!dir.isDirectory()) {
				break;
			}

			for (final File file : dir.listFiles()) {
				for (final String name : COVER_NAMES) {
					if (name.equalsIgnoreCase(file.getName())) {
						this.mSelection.getTagCloud().setCoverFile(file);
						return;
					}
				}
			}
		}

		this.mSelection.getTagCloud().setCoverFile(null);
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
		return this.mSelection;
	}


	public List<File> scanDirectory(final File file) {
		return this.scanDirectory(file, Integer.parseInt(this.mPreferences.getString(Constants.PREF_KEY_REC, "5")));
	}

	private List<File> scanDirectory(final File file, final int n) {
		final List<File> result = new ArrayList<File>();
		if (file.isFile() && this.mp3Filter.accept(file)) {
			result.add(file);
		} else if (n > 0 && file.isDirectory()) {
			result.addAll(Arrays.asList(file.listFiles(this.mp3OrDirectoryFilter)));
			final List<File> toAdd = new ArrayList<File>();
			final List<File> toRemove = new ArrayList<File>();

			for (final File subItem : result) {
				if (subItem.isDirectory()) {
					toRemove.add(subItem);
					toAdd.addAll(this.scanDirectory(subItem, n-1));
				}
			}

			result.removeAll(toRemove);
			result.addAll(toAdd);
		}
		return result;
	}
}
