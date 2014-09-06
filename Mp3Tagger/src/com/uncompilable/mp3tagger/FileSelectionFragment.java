package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

import com.uncompilable.mp3tagger.utility.Constants;

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

	public FileSelectionFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mCurrentPath = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(Constants.PREF_KEY_ROOTDIR, "/");
		View root = inflater.inflate(R.layout.selection_fragment, container, false);

		final FileListAdapter listAdapter = new FileListAdapter(this.getActivity(), new File(mCurrentPath).listFiles());
		populateAdapter(listAdapter);
		
		mLvFiles = (ListView) root.findViewById(R.id.lvFiles);
		mLvFiles.setAdapter(listAdapter);
		mLvFiles.setClickable(true);

		mLvFiles.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
				Log.d(Constants.MAIN_TAG, "Clicked on view " + parent + ", Item at position " + position + " with rowID " + id);

				File newRootFile = listAdapter.getDisplayedFiles()[position];
				if (newRootFile.isDirectory()) {
					mCurrentPath = newRootFile.getAbsolutePath();
					populateAdapter(listAdapter);
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
