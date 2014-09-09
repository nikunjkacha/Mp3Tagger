package com.uncompilable.mp3tagger;

import java.io.File;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import com.mpatric.mp3agic.ID3v1Genres;
import com.uncompilable.mp3tagger.model.Id3Frame;
import com.uncompilable.mp3tagger.model.TagCloud;
import com.uncompilable.mp3tagger.utility.Constants;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Spinner;

public class TagEditFragment extends Fragment {
	private SimpleFileListAdapter mAdapter;
	private MainActivity mMain;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.tagedit_fragment, container, false);

		EditText etTitle 	= (EditText) root.findViewById(R.id.etTitle   );
		EditText etArtist  	= (EditText) root.findViewById(R.id.etArtist  );
		EditText etAlbum    = (EditText) root.findViewById(R.id.etAlbum   );
		EditText etComposer = (EditText) root.findViewById(R.id.etComposer);

		CheckBox cbTitle 	= (CheckBox) root.findViewById(R.id.cbTitle   );
		CheckBox cbArtist  	= (CheckBox) root.findViewById(R.id.cbArtist  );
		CheckBox cbAlbum    = (CheckBox) root.findViewById(R.id.cbAlbum   );
		CheckBox cbComposer = (CheckBox) root.findViewById(R.id.cbComposer);

		//Set Listeners for all items
		ItemListener itemListener;
		TagCloud cloud = mMain.getSelectionController().getSelection().getTagCloud();
		CheckboxListener checkListener = new CheckboxListener(cloud, Id3Frame.TITLE);
		
		itemListener = new ItemListener(cloud, Id3Frame.TITLE); 
		etTitle.addTextChangedListener(itemListener);
		cbTitle.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.ARTIST);   
		etArtist.addTextChangedListener(itemListener);
		cbArtist.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.ALBUM_TITLE);    
		etAlbum.addTextChangedListener(itemListener);
		cbAlbum.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.COMPOSER); 
		etComposer.addTextChangedListener(itemListener);
		cbComposer.setOnClickListener(checkListener);

		NumberPicker npTrackNumber = (NumberPicker) root.findViewById(R.id.npTracknumber);
		npTrackNumber.setMinValue(0);
		npTrackNumber.setMaxValue(512);
		
		final CheckBox cbTrack = (CheckBox) root.findViewById(R.id.cbTracknumber);
		final TagCloud tags = mMain.getSelectionController().getSelection().getTagCloud();
		
		cbTrack.setOnClickListener(checkListener);
		npTrackNumber.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				cbTrack.setChecked(true);
				tags.setFrameSelected(Id3Frame.TRACK_NUMBER);
			}
			
		});

		Spinner spGenres = (Spinner) root.findViewById(R.id.spGenre);
		spGenres.setAdapter(new ArrayAdapter<String>(mMain, R.layout.simple_textview_item, ID3v1Genres.GENRES));
		final CheckBox cbGenres = (CheckBox) root.findViewById(R.id.cbGenre);
		
		cbGenres.setOnClickListener(checkListener);

		spGenres.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				tags.setFrameSelected(Id3Frame.GENRE);
				cbGenres.setChecked(true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				tags.setFrameUnselected(Id3Frame.GENRE);
				cbGenres.setChecked(false);
			}

		});

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

		//Fill the EditTexts with the tag value
		currentFrame = tags.getValues(Id3Frame.TITLE	  ); fillWidget(currentFrame, etTitle	);
		currentFrame = tags.getValues(Id3Frame.ARTIST	  ); fillWidget(currentFrame, etArtist	);
		currentFrame = tags.getValues(Id3Frame.ALBUM_TITLE); fillWidget(currentFrame, etAlbum	);
		currentFrame = tags.getValues(Id3Frame.COMPOSER	  ); fillWidget(currentFrame, etComposer);

		updateCheckboxes();
		
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

		currentFrame = tags.getValues(Id3Frame.GENRE);
		int index = 0;
		if (tags.getValues(Id3Frame.GENRE).size() > 0) 
			index = ID3v1Genres.matchGenreDescription((String)tags.getValues(Id3Frame.GENRE).toArray()[0]);
		if (index < spGenre.getAdapter().getCount()) spGenre.setSelection(index < 0? 0 : index);

	}

	private void fillWidget(Collection<String> values, EditText widget) {
		int size = values.size();
		if (size == 0) widget.setText(R.string.noValues);
		else if (size > 1) widget.setText(R.string.multipleValues);
		else widget.setText((String)values.toArray()[0]); 
	}
	
	private void updateCheckboxes() {
		CheckBox cbTitle 	= (CheckBox) getView().findViewById(R.id.cbTitle   	  );
		CheckBox cbArtist  	= (CheckBox) getView().findViewById(R.id.cbArtist  	  );
		CheckBox cbAlbum    = (CheckBox) getView().findViewById(R.id.cbAlbum   	  );
		CheckBox cbComposer = (CheckBox) getView().findViewById(R.id.cbComposer	  );
		CheckBox cbTrack    = (CheckBox) getView().findViewById(R.id.cbTracknumber);
		CheckBox cbGenre 	= (CheckBox) getView().findViewById(R.id.cbGenre	  );
		
		//Check the checkboxes, if the frame is currently selected by the TagMap
		TagCloud tags = mMain.getSelectionController().getSelection().getTagCloud();
		
		cbTitle	  .setChecked(tags.isSelected(Id3Frame.TITLE	   ));
		cbArtist  .setChecked(tags.isSelected(Id3Frame.ARTIST	   ));
		cbAlbum   .setChecked(tags.isSelected(Id3Frame.ALBUM_TITLE ));
		cbComposer.setChecked(tags.isSelected(Id3Frame.COMPOSER	   ));
		cbTrack   .setChecked(tags.isSelected(Id3Frame.TRACK_NUMBER));
		cbGenre   .setChecked(tags.isSelected(Id3Frame.GENRE       ));
	}

	private class CheckboxListener implements OnClickListener {
		private final TagCloud tagCloud;
		private final Id3Frame frame;
		
		protected CheckboxListener(TagCloud cloud, Id3Frame frame) {
			this.tagCloud = cloud;
			this.frame = frame;
		}

		@Override
		public void onClick(View source) {
			if (source instanceof CheckBox) {
				CheckBox cbSource = (CheckBox) source;
				if (cbSource.isChecked()) {
					tagCloud.setFrameSelected(frame);
				} else {
					tagCloud.setFrameUnselected(frame);
				}
			} else {
				Log.w(Constants.MAIN_TAG, "Tried to invoke onClick() of ItemListener with " + source.getClass().toString());
			}

		}
	}
	
	private class ItemListener implements TextWatcher {
		private final TagCloud tagCloud;
		private final Id3Frame frame;

		public ItemListener(TagCloud cloud, Id3Frame frame) {
			this.tagCloud = cloud;
			this.frame = frame;
		}

		@Override
		public void afterTextChanged(Editable source) {
			String newString = source.toString();
			if (newString.length() > 0) { 
				this.tagCloud.setFrameSelected(frame);
			} else {
				this.tagCloud.setFrameUnselected(frame);
			}
			if (newString.equals(getResources().getString(R.string.noValues)) ||
					newString.equals(getResources().getString(R.string.multipleValues))) {
				this.tagCloud.setFrameUnselected(frame);
			}
			updateCheckboxes();
		}

		@Override
		public void beforeTextChanged(CharSequence text, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence text, int start, int count, int after) {

		}

	}
}
