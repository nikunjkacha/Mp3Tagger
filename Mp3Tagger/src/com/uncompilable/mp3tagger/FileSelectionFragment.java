package com.uncompilable.mp3tagger;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
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
	private BrowsableFileListAdapter mListAdapter;

	public FileSelectionFragment() {
		super();
		this.mListAdapter = null;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		File defaultRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

		if (!defaultRoot.exists()) {
			defaultRoot = new File("/");
		}
		final View root = inflater.inflate(R.layout.selection_fragment, container, false);

		this.mListAdapter = new BrowsableFileListAdapter((MainActivity)this.getActivity(), defaultRoot);

		final ListView lvFiles = (ListView) root.findViewById(R.id.lvFiles);
		lvFiles.setAdapter(this.mListAdapter);
		lvFiles.setClickable(true);

		return root;
	}
}
