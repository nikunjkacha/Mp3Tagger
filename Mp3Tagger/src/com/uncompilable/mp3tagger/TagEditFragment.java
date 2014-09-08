package com.uncompilable.mp3tagger;

import java.io.File;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import com.uncompilable.mp3tagger.model.Genre;
import com.uncompilable.mp3tagger.model.Id3Frame;
import com.uncompilable.mp3tagger.model.TagCloud;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class TagEditFragment extends Fragment {
	private SimpleFileListAdapter mAdapter;
	private MainActivity mMain;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.tagedit_fragment, container, false);

		NumberPicker npTrackNumber = (NumberPicker) root.findViewById(R.id.npTracknumber);
		npTrackNumber.setMinValue(0);
		npTrackNumber.setMaxValue(512);

		Spinner spGenres = (Spinner) root.findViewById(R.id.spGenre);
		spGenres.setAdapter(new ArrayAdapter<Genre>(mMain, R.layout.simple_textview_item, Genre.values()));
		
		refresh();
		ListView lvSelection = (ListView) root.findViewById(R.id.lvFiles);
		lvSelection.setAdapter(mAdapter);

		return root;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.mMain = (MainActivity) activity;
		this.mAdapter = new SimpleFileListAdapter(mMain, new File[0]);
		
		mMain.getSelectionController().getSelection().addObserver(new Observer() {

			@Override
			public void update(Observable source, Object data) {
				populateWidgets();
			}
			
		});
	}

	public void refresh() {
		if (mAdapter != null) {
			mAdapter.setDisplayedFiles(mMain.
					getSelectionController().
					getSelection().
					getFileSet().
					toArray(new File[((MainActivity)this.getActivity()).getSelectionController().getSelection().getFileSet().size()]));
		}
		if (getView() != null) populateWidgets();
	}
	
	private void populateWidgets() {
		EditText etTitle 	= (EditText) getView().findViewById(R.id.etTitle   );
		EditText etArtist  	= (EditText) getView().findViewById(R.id.etArtist  );
		EditText etAlbum    = (EditText) getView().findViewById(R.id.etAlbum   );
		EditText etComposer = (EditText) getView().findViewById(R.id.etComposer);
		
		NumberPicker npTracknumber = (NumberPicker) getView().findViewById(R.id.npTracknumber);
		Spinner spGenre = (Spinner) getView().findViewById(R.id.spGenre);
		
		TagCloud tags = mMain.getSelectionController().getSelection().getTagCloud();
		
		Collection<String> currentFrame;
		
		currentFrame = tags.getValues(Id3Frame.TITLE	  ); fillWidget(currentFrame, etTitle	);
		currentFrame = tags.getValues(Id3Frame.ARTIST	  ); fillWidget(currentFrame, etArtist	);
		currentFrame = tags.getValues(Id3Frame.ALBUM_TITLE); fillWidget(currentFrame, etAlbum	);
		currentFrame = tags.getValues(Id3Frame.COMPOSER	  ); fillWidget(currentFrame, etComposer);
		
		if (mMain.getSelectionController().getSelection().getFileSet().size() == 1) {
			npTracknumber.setEnabled(true);
			int track = 0;
			if (tags.getValues(Id3Frame.TITLE) != null) {
				String strTrack = (String)tags.getValues(Id3Frame.TRACK_NUMBER).toArray()[0];
				track = Integer.parseInt(strTrack.split("/")[0]);
			}
			npTracknumber.setValue(track);
 		} else {
 			npTracknumber.setEnabled(false);
 		}
		
		
	}
	
	private void fillWidget(Collection<String> values, EditText widget) {
		if (values.size() == 0) widget.setText(R.string.noValues);
		else if (values.size() > 1) widget.setText(R.string.multipleValues);
		else widget.setText((String)values.toArray()[0]);
	}
}
