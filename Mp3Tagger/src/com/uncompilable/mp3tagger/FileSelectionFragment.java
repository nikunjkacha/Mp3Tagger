package com.uncompilable.mp3tagger;

import java.io.File;

import com.uncompilable.mp3tagger.utility.Constants;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * First Fragment, per default shown on Application startup.
 * Displays a list of files and lets the user select and navigate them.
 * @author dennis
 *
 */
public class FileSelectionFragment extends Fragment {
	private ListView lvFiles;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.selection_fragment, container, false);
		
		String rootPath = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(Constants.PREF_KEY_ROOTDIR, "/");
		lvFiles = (ListView) root.findViewById(R.id.lvFiles);
		lvFiles.setAdapter(new FileListAdapter(this.getActivity(), new File(rootPath).listFiles()));
		
		return root;
	}
}
