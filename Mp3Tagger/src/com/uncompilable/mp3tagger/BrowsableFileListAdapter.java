package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uncompilable.mp3tagger.utility.Constants;

public class BrowsableFileListAdapter extends AbstractFileListAdapter {
	private File mRootfile;

	public BrowsableFileListAdapter(final MainActivity main, final File root) {
		super(main, getFileList(root));
		this.mRootfile = root;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final View result = super.getView(position, convertView, parent);

		final ImageView ivIcon = (ImageView) result.findViewById(R.id.ivIcon);

		if (this.mFiles[position].isDirectory()) {
			ivIcon.setImageResource(R.drawable.ic_directory);
		} else if (this.mFiles[position].equals(AbstractFileListAdapter.sPlaying)) {
			ivIcon.setImageResource(R.drawable.ic_mp3file_playing);
		} else {
			ivIcon.setImageResource(R.drawable.ic_mp3file);
		}

		result.setOnClickListener(new AbstractFileListAdapter.ItemClickListener(this.mFiles[position], this) {
			@Override
			public void onClick(final View source) {
				super.onClick(source);
				if (BrowsableFileListAdapter.this.mFiles[position].isDirectory()) {
					try {
						BrowsableFileListAdapter.this.setRoot(BrowsableFileListAdapter.this.mFiles[position].getCanonicalFile());
					} catch (final IOException e) {
						Log.w(Constants.MAIN_TAG, "ERROR: could not create canonical file from " + BrowsableFileListAdapter.this.mFiles[position].getAbsolutePath(), e);
						BrowsableFileListAdapter.this.setRoot(BrowsableFileListAdapter.this.mFiles[position]);
					}
					BrowsableFileListAdapter.this.setDisplayedFiles(getFileList(BrowsableFileListAdapter.this.mRootfile));
				}
				BrowsableFileListAdapter.this.notifyDataSetChanged();
			}
		});

		return result;
	}

	public void setRoot(final File newRoot) {
		try {
			this.mRootfile = newRoot.getCanonicalFile();
		} catch (final IOException e) {
			this.mRootfile = newRoot.getAbsoluteFile();
		}
	}

	private static File[] getFileList(final File dir) {
		final File rootDir = Environment.getRootDirectory();
		final List<File> subDir = new ArrayList<File>(Arrays.asList(dir.listFiles(MP3_DIR_FILTER)));
		if (!rootDir.equals(dir) &&
				dir.getParentFile() != null) {
			subDir.add(0, new File(dir.getAbsolutePath() + "/.."));
		}
		return subDir.toArray(new File[subDir.size()]);
	}

	public final static FileFilter MP3_DIR_FILTER = new FileFilter() {
		@Override
		public boolean accept(final File file) {
			return file.isDirectory() || file.getName().endsWith(".mp3");
		}
	};
}
