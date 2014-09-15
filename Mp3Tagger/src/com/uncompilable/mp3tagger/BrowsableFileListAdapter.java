package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.uncompilable.mp3tagger.utility.Constants;

import android.util.Log;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BrowsableFileListAdapter extends AbstractFileListAdapter {
	private File mRootfile;

	public BrowsableFileListAdapter(MainActivity main, File root) {
		super(main, getFileList(root));
		mRootfile = root;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View result = super.getView(position, convertView, parent);

		final ImageView ivIcon = (ImageView) result.findViewById(R.id.ivIcon);

		if (mFiles[position].isDirectory()) {
			ivIcon.setImageResource(R.drawable.ic_directory);
		} else if (mFiles[position].equals(AbstractFileListAdapter.sPlaying)) {
			ivIcon.setImageResource(R.drawable.ic_mp3file_playing);
		} else {
			ivIcon.setImageResource(R.drawable.ic_mp3file);
		}

		result.setOnClickListener(new AbstractFileListAdapter.ItemClickListener(mFiles[position], this) {
			@Override
			public void onClick(View source) {
				super.onClick(source);
				if (mFiles[position].isDirectory()) {
					try {
						setRoot(mFiles[position].getCanonicalFile());
					} catch (IOException e) {
						Log.w(Constants.MAIN_TAG, "ERROR: could not create canonical file from " + mFiles[position].getAbsolutePath(), e);
						setRoot(mFiles[position]);
					}
					setDisplayedFiles(getFileList(mRootfile));
				}
				notifyDataSetChanged();
			}
		});

		return result;
	}

	public void setRoot(File newRoot) {
		try {
			this.mRootfile = newRoot.getCanonicalFile();
		} catch (IOException e) {
			this.mRootfile = newRoot.getAbsoluteFile();
		}
	}

	private static File[] getFileList(File dir) {
		File rootDir = Environment.getRootDirectory();
		List<File> subDir = new ArrayList<File>(Arrays.asList(dir.listFiles(MP3_DIR_FILTER)));
		if (!rootDir.equals(dir) &&
				dir.getParentFile() != null) {
			subDir.add(0, new File(dir.getAbsolutePath() + "/.."));
		}
		return subDir.toArray(new File[subDir.size()]);
	}

	public final static FileFilter MP3_DIR_FILTER = new FileFilter() {
		@Override
		public boolean accept(File file) {
			return file.isDirectory() || file.getName().endsWith(".mp3");
		}
	};
}
