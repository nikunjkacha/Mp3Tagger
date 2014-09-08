package com.uncompilable.mp3tagger;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.NumberPicker;

public class TagEditFragment extends Fragment {
	private FileListAdapter mAdapter;
	private MainActivity mMain;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.tagedit_fragment, container, false);

		NumberPicker npTrackNumber = (NumberPicker) root.findViewById(R.id.npTracknumber);
		npTrackNumber.setMinValue(0);
		npTrackNumber.setMaxValue(512);

		refresh();
		ListView lvSelection = (ListView) root.findViewById(R.id.lvFiles);
		lvSelection.setAdapter(mAdapter);

		return root;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.mMain = (MainActivity) activity;
		this.mAdapter = new FileListAdapter(mMain, new File[0]);
	}

	public void refresh() {
		if (mAdapter != null) {
			mAdapter.setDisplayedFiles(mMain.
					getSelectionController().
					getSelection().
					getSelection().
					toArray(new File[((MainActivity)this.getActivity()).getSelectionController().getSelection().getSelection().size()]));
		}
	}
}
