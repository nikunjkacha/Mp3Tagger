package com.uncompilable.mp3tagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

public class TagEditFragment extends Fragment {

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.tagedit_fragment, container, false);
		
		NumberPicker npTrackNumber = (NumberPicker) root.findViewById(R.id.npTracknumber);
		npTrackNumber.setMinValue(0);
		npTrackNumber.setMaxValue(512);
		
		return root;
	}
}
