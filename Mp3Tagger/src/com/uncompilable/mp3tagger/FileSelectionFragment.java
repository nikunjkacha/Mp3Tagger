package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.uncompilable.mp3tagger.utility.Constants;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * First Fragment, per default shown on Application startup.
 * Displays a list of files and lets the user select and navigate them.
 * @author dennis
 *
 */
public class FileSelectionFragment extends Fragment {
	private ListView mLvFiles;
	private String mCurrentPath;
	private final MediaPlayer mPlayer;
	private File mPlaying;

	public FileSelectionFragment() {
		super();

		mPlayer = new MediaPlayer();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mCurrentPath = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(Constants.PREF_KEY_ROOTDIR, "/");
		View root = inflater.inflate(R.layout.selection_fragment, container, false);

		mPlaying = null;

		final FileListAdapter listAdapter = new FileListAdapter((MainActivity)this.getActivity(), new File(mCurrentPath).listFiles());
		populateAdapter(listAdapter);

		mLvFiles = (ListView) root.findViewById(R.id.lvFiles);
		mLvFiles.setAdapter(listAdapter);
		mLvFiles.setClickable(true);

		mLvFiles.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
				Log.d(Constants.MAIN_TAG, "Clicked on view " + parent + ", Item at position " + position + " with rowID " + id);

				File selectedFile = listAdapter.getDisplayedFiles()[position];
				if (selectedFile.isDirectory()) {
					mCurrentPath = selectedFile.getAbsolutePath();
					populateAdapter(listAdapter);
				} else if (selectedFile.isFile() && selectedFile.getName().endsWith(".mp3") &&
						PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(Constants.PREF_KEY_PLAYABLE, true)) {
					try {
						if (mPlaying != null) {
							mPlayer.stop();
							mPlayer.reset();
						}
						if (!selectedFile.equals(mPlaying)) {
							mPlayer.setDataSource(new FileInputStream(selectedFile).getFD());
							mPlayer.prepare();
							mPlayer.start();
							mPlaying = selectedFile;
							listAdapter.setPlayingIndex(position);
						} else {
							mPlaying = null;
							listAdapter.setPlayingIndex(FileListAdapter.NONE_PLAYING);
						}
						listAdapter.notifyDataSetChanged();
					} catch (IllegalArgumentException | IllegalStateException | IOException e) {
						Log.w(Constants.MAIN_TAG, "Could not play file " + selectedFile.getName() + ".", e);
					}
				}
			}


		});

		return root;
	}

	private void populateAdapter(FileListAdapter listAdapter) {
		java.util.List<File> subDir = new ArrayList<File>(Arrays.asList(new File(mCurrentPath).listFiles(new Mp3FileFilter())));
		subDir.add(0, new File(mCurrentPath + "/.."));

		listAdapter.setDisplayedFiles(subDir.toArray(new File[subDir.size()]));
	}

	private class Mp3FileFilter implements FileFilter {

		@Override
		public boolean accept(File file) {
			return file.isDirectory() || file.getName().endsWith(".mp3");
		}

	}
}
