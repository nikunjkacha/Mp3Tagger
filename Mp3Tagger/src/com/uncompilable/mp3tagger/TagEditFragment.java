package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import com.mpatric.mp3agic.ID3v1Genres;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.uncompilable.mp3tagger.model.FrameValuePair;
import com.uncompilable.mp3tagger.model.Id3Frame;
import com.uncompilable.mp3tagger.model.TagCloud;
import com.uncompilable.mp3tagger.utility.Constants;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Spinner;

public class TagEditFragment extends Fragment {
	private SimpleFileListAdapter mAdapter;
	private MainActivity mMain;

	private EditText mEtTitle;
	private EditText mEtArtist;
	private EditText mEtAlbum;
	private EditText mEtComposer;

	private NumberPicker mNpTrack;
	private Spinner mSpGenre;

	private CheckBox mCbTitle;
	private CheckBox mCbArtist;
	private CheckBox mCbAlbum;
	private CheckBox mCbComposer;
	private CheckBox mCbTrack;
	private CheckBox mCbGenre;

	private ImageButton mBtnCover;

	private int mCoverWidth, mCoverHeight;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.tagedit_fragment, container, false);

		mEtTitle 	= (EditText) root.findViewById(R.id.etTitle   );
		mEtArtist  	= (EditText) root.findViewById(R.id.etArtist  );
		mEtAlbum    = (EditText) root.findViewById(R.id.etAlbum   );
		mEtComposer = (EditText) root.findViewById(R.id.etComposer);

		mCbTitle 	= (CheckBox) root.findViewById(R.id.cbTitle   );
		mCbArtist  	= (CheckBox) root.findViewById(R.id.cbArtist  );
		mCbAlbum    = (CheckBox) root.findViewById(R.id.cbAlbum   );
		mCbComposer = (CheckBox) root.findViewById(R.id.cbComposer);
		mCbTrack    = (CheckBox) root.findViewById(R.id.cbTracknumber);
		mCbGenre    = (CheckBox) root.findViewById(R.id.cbGenre);

		//Set Listeners for all items
		ItemListener itemListener;
		TagCloud cloud = mMain.getSelectionController().getSelection().getTagCloud();
		CheckboxListener checkListener = new CheckboxListener(cloud, Id3Frame.TITLE);

		itemListener = new ItemListener(cloud, Id3Frame.TITLE); 
		mEtTitle.addTextChangedListener(itemListener);
		mCbTitle.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.ARTIST);   
		mEtArtist.addTextChangedListener(itemListener);
		mCbArtist.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.ALBUM_TITLE);    
		mEtAlbum.addTextChangedListener(itemListener);
		mCbAlbum.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.COMPOSER); 
		mEtComposer.addTextChangedListener(itemListener);
		mCbComposer.setOnClickListener(checkListener);

		mNpTrack= (NumberPicker) root.findViewById(R.id.npTracknumber);
		mNpTrack.setMinValue(0);
		mNpTrack.setMaxValue(512);

		final TagCloud tags = mMain.getSelectionController().getSelection().getTagCloud();

		mCbTrack.setOnClickListener(checkListener);
		mNpTrack.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				tags.setFrameSelected(Id3Frame.TRACK_NUMBER);
				updateCheckboxes();
			}

		});

		mSpGenre = (Spinner) root.findViewById(R.id.spGenre);
		mSpGenre.setAdapter(new ArrayAdapter<String>(mMain, R.layout.simple_textview_item, ID3v1Genres.GENRES));

		mCbGenre.setOnClickListener(checkListener);

		mSpGenre.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				tags.setFrameSelected(Id3Frame.GENRE);
				updateCheckboxes();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				tags.setFrameUnselected(Id3Frame.GENRE);
				updateCheckboxes();
			}

		});


		refresh();
		ListView lvSelection = (ListView) root.findViewById(R.id.lvFiles);
		lvSelection.setAdapter(mAdapter);

		mBtnCover = (ImageButton) root.findViewById(R.id.btnEditCover);

		mBtnCover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View source) {
				mMain.switchTab(MainActivity.TAB_COVER);
			}

		});

		Button btnSave = (Button) root.findViewById(R.id.btnSaveTags);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View source) {
				saveTags();
			}

		});

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
				refresh();
			}

		});
		
		mMain.getSelectionController().getSelection().getTagCloud().addObserver(new Observer() {
			
			@Override
			public void update(Observable source, Object data) {
				populateWidgets();
			}
		});
	}

	private void refresh() {
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


		if (mMain.getSelectionController().getSelection().getFileSet().size() == 1) {
			npTracknumber.setEnabled(true);
			int track = 0;
			if (tags.getValues(Id3Frame.TRACK_NUMBER) != null && tags.getValues(Id3Frame.TRACK_NUMBER).size() > 0) {
				String strTrack = (String)tags.getValues(Id3Frame.TRACK_NUMBER).toArray()[0];
				track = Integer.parseInt(strTrack.split("/")[0]);
			}
			npTracknumber.setValue(track);
		} else {
			npTracknumber.setEnabled(false);
		}

		currentFrame = tags.getValues(Id3Frame.GENRE);
		int index = 0;
		if (tags.getValues(Id3Frame.GENRE).size() > 0)  {
			index = ID3v1Genres.matchGenreDescription((String)tags.getValues(Id3Frame.GENRE).toArray()[0]);
		}
		if (tags.getValues(Id3Frame.GENRE).size() > 1) {
			tags.setFrameUnselected(Id3Frame.GENRE);
		}
		if (index < spGenre.getAdapter().getCount()) spGenre.setSelection(index < 0? 0 : index);

		updateCheckboxes();
		if (mCoverWidth <= 0) {
			mCoverWidth = mBtnCover.getWidth(); 
		}

		if (mCoverHeight <= 0) {
			mCoverHeight = mBtnCover.getHeight();
		}

		File coverFile = mMain.getSelectionController().getSelection().getTagCloud().getCoverFile();
		if (coverFile != null) {
			Bitmap coverBitmap = BitmapFactory.decodeFile(coverFile.getAbsolutePath());
			if (coverBitmap != null) { 
				mBtnCover.setImageBitmap(Bitmap.createScaledBitmap(coverBitmap, mCoverWidth, mCoverHeight, false));
			} else {
				mBtnCover.setImageResource(R.drawable.ic_mp3file);
			}
		} else {
			mBtnCover.setImageResource(R.drawable.ic_mp3file);
		}
	}

	private void fillWidget(Collection<String> values, EditText widget) {
		int size = values.size();
		if (size == 0) widget.setText(R.string.noValues);
		else if (size > 1) widget.setText(R.string.multipleValues);
		else widget.setText((String)values.toArray()[0]); 
	}

	private void updateCheckboxes() {
		//Check the checkboxes, if the frame is currently selected by the TagMap
		TagCloud tags = mMain.getSelectionController().getSelection().getTagCloud();

		mCbTitle   .setChecked(tags.isSelected(Id3Frame.TITLE	   ));
		mCbArtist  .setChecked(tags.isSelected(Id3Frame.ARTIST	   	));
		mCbAlbum   .setChecked(tags.isSelected(Id3Frame.ALBUM_TITLE ));
		mCbComposer.setChecked(tags.isSelected(Id3Frame.COMPOSER	));
		mCbTrack   .setChecked(tags.isSelected(Id3Frame.TRACK_NUMBER));
		mCbGenre   .setChecked(tags.isSelected(Id3Frame.GENRE       ));
	}

	private void saveTags() {
		TagCloud tags = mMain.getSelectionController().getSelection().getTagCloud();

		tags.setValue(new FrameValuePair(Id3Frame.TITLE, mEtTitle.getText().toString()));
		tags.setValue(new FrameValuePair(Id3Frame.ARTIST, mEtArtist.getText().toString()));
		tags.setValue(new FrameValuePair(Id3Frame.ALBUM_TITLE, mEtAlbum.getText().toString()));
		tags.setValue(new FrameValuePair(Id3Frame.COMPOSER, mEtComposer.getText().toString()));

		tags.setValue(new FrameValuePair(Id3Frame.TRACK_NUMBER, mNpTrack.getValue() + ""));
		tags.setValue(new FrameValuePair(Id3Frame.GENRE, (String) mSpGenre.getSelectedItem()));

		try {
			mMain.getSelectionController().getIOController().writeTags();
		} catch (UnsupportedTagException | InvalidDataException
				| NotSupportedException | IOException e) {
			Log.e(Constants.MAIN_TAG, "ERROR: Could not write tags to file", e);
		}
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
