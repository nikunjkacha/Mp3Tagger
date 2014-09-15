package com.uncompilable.mp3tagger;

import java.io.File;
import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
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

import com.mpatric.mp3agic.ID3v1Genres;
import com.uncompilable.mp3tagger.model.FrameValuePair;
import com.uncompilable.mp3tagger.model.Id3Frame;
import com.uncompilable.mp3tagger.model.TagCloud;
import com.uncompilable.mp3tagger.utility.Constants;

public class TagEditFragment extends Fragment {
	private SimpleFileListAdapter mAdapter;
	private MainActivity mMain;

	private EditText mEtTitle;
	private EditText mEtArtist;
	private EditText mEtAlbum;
	private EditText mEtComposer;
	private EditText mEtYear;
	private EditText mEtAlbumArtist;

	private NumberPicker mNpTrack;
	private Spinner mSpGenre;

	private CheckBox mCbTitle;
	private CheckBox mCbArtist;
	private CheckBox mCbAlbum;
	private CheckBox mCbComposer;
	private CheckBox mCbTrack;
	private CheckBox mCbGenre;
	private CheckBox mCbYear;
	private CheckBox mCbAlbumArtist;

	private ImageButton mBtnCover;
	private Button mBtnSave;

	private int mCoverWidth, mCoverHeight;
	
	public TagEditFragment() {
		super();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.tagedit_fragment, container, false);

		this.bindViews(root);

		this.mNpTrack.setMinValue(0);
		this.mNpTrack.setMaxValue(this.getResources().getInteger(R.integer.maxInput));

		this.mSpGenre.setAdapter(new ArrayAdapter<String>(this.mMain, R.layout.simple_textview_item, ID3v1Genres.GENRES));

		this.refresh();
		this.addListeners();

		final ListView lvSelection = (ListView) root.findViewById(R.id.lvFiles);
		lvSelection.setAdapter(this.mAdapter);

		return root;

	}

	private void bindViews(final View root) {
		this.mEtTitle 	   = (EditText) root.findViewById(R.id.etTitle   	);
		this.mEtArtist  	   = (EditText) root.findViewById(R.id.etArtist  	);
		this.mEtAlbum       = (EditText) root.findViewById(R.id.etAlbum   	);
		this.mEtComposer    = (EditText) root.findViewById(R.id.etComposer	);
		this.mEtYear		   = (EditText) root.findViewById(R.id.etYear    	);
		this.mEtAlbumArtist = (EditText) root.findViewById(R.id.etAlbumArtist);

		this.mCbTitle 	   = (CheckBox) root.findViewById(R.id.cbTitle   	 );
		this.mCbArtist  	   = (CheckBox) root.findViewById(R.id.cbArtist  	 );
		this.mCbAlbum       = (CheckBox) root.findViewById(R.id.cbAlbum   	 );
		this.mCbComposer    = (CheckBox) root.findViewById(R.id.cbComposer	 );
		this.mCbTrack       = (CheckBox) root.findViewById(R.id.cbTracknumber );
		this.mCbGenre       = (CheckBox) root.findViewById(R.id.cbGenre		 );
		this.mCbYear 	   = (CheckBox) root.findViewById(R.id.cbYear		 );
		this.mCbAlbumArtist = (CheckBox) root.findViewById(R.id.cbAlbumArtist );

		this.mNpTrack = (NumberPicker) root.findViewById(R.id.npTracknumber);
		this.mSpGenre = (Spinner) root.findViewById(R.id.spGenre);

		this.mBtnCover = (ImageButton) root.findViewById(R.id.btnEditCover);
		this.mBtnSave  = (Button) root.findViewById(R.id.btnSaveTags);
	}

	private void addListeners() {
		ItemListener itemListener;
		final TagCloud cloud = MainActivity.sSelectionController.getSelection().getTagCloud();
		final CheckboxListener checkListener = new CheckboxListener(cloud, Id3Frame.TITLE);

		itemListener = new ItemListener(cloud, Id3Frame.TITLE);
		this.mEtTitle.addTextChangedListener(itemListener);
		this.mCbTitle.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.ARTIST);
		this.mEtArtist.addTextChangedListener(itemListener);
		this.mCbArtist.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.ALBUM_TITLE);
		this.mEtAlbum.addTextChangedListener(itemListener);
		this.mCbAlbum.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.COMPOSER);
		this.mEtComposer.addTextChangedListener(itemListener);
		this.mCbComposer.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.YEAR);
		this.mEtYear.addTextChangedListener(itemListener);
		this.mCbComposer.setOnClickListener(checkListener);

		itemListener = new ItemListener(cloud, Id3Frame.ALBUM_ARTIST);
		this.mEtAlbumArtist.addTextChangedListener(itemListener);
		this.mCbComposer.setOnClickListener(checkListener);

		this.mCbTrack.setOnClickListener(checkListener);

		final TagCloud tags = MainActivity.sSelectionController.getSelection().getTagCloud();

		this.mNpTrack.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(final NumberPicker picker, final int oldVal, final int newVal) {
				tags.setFrameSelected(Id3Frame.TRACK_NUMBER);
				TagEditFragment.this.updateCheckboxes();
			}

		});
		this.mCbGenre.setOnClickListener(checkListener);

		this.mSpGenre.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view, final int pos, final long itemId) {
				tags.setFrameSelected(Id3Frame.GENRE);
				TagEditFragment.this.updateCheckboxes();
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
				tags.setFrameUnselected(Id3Frame.GENRE);
				TagEditFragment.this.updateCheckboxes();
			}
		});

		this.mBtnCover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View source) {
				final Intent coverIntent = new Intent(TagEditFragment.this.mMain, AlbumCoverActivity.class);
				TagEditFragment.this.mMain.startActivity(coverIntent);
			}

		});

		this.mBtnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View source) {
				TagEditFragment.this.saveTags();
			}

		});

	}

	@Override
	public void onResume() {
		super.onResume();
		this.populateWidgets();
		this.updateCheckboxes();
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);

		this.mMain = (MainActivity) activity;
		this.mAdapter = new SimpleFileListAdapter(this.mMain, new File[0]);
	}

	private void refresh() {
		if (this.mAdapter != null) {
			this.mAdapter.setDisplayedFiles(MainActivity.sSelectionController.
					getSelection().
					getFileSet().
					toArray(new File[MainActivity.sSelectionController.getSelection().getFileSet().size()]));
		}
		if (this.getView() != null) {
			this.populateWidgets();
			this.updateCheckboxes();
		}
	}

	private void populateWidgets() {
		populateEditTexts();
		populateNumberPicker();
		populateSpinner();
		populateCoverButton();
	}

	private void populateEditTexts() {
		final TagCloud tags = MainActivity.sSelectionController.getSelection().getTagCloud();
		Collection<String> currentFrame;

		//Fill the EditTexts with the tag value
		currentFrame = tags.getValues(Id3Frame.TITLE	   ); this.fillWidget(currentFrame, this.mEtTitle	 	 );
		currentFrame = tags.getValues(Id3Frame.ARTIST	   ); this.fillWidget(currentFrame, this.mEtArtist	 );
		currentFrame = tags.getValues(Id3Frame.ALBUM_TITLE ); this.fillWidget(currentFrame, this.mEtAlbum	 	 );
		currentFrame = tags.getValues(Id3Frame.COMPOSER	   ); this.fillWidget(currentFrame, this.mEtComposer	 );
		currentFrame = tags.getValues(Id3Frame.YEAR		   ); this.fillWidget(currentFrame, this.mEtYear		 );
		currentFrame = tags.getValues(Id3Frame.ALBUM_ARTIST); this.fillWidget(currentFrame, this.mEtAlbumArtist);
	}

	private void populateNumberPicker() {
		final TagCloud tags = MainActivity.sSelectionController.getSelection().getTagCloud();

		//Enable the numberpicker only if there is exactly one seleted file
		if (MainActivity.sSelectionController.getSelection().getFileSet().size() == Constants.ONE_ITEM) {
			mNpTrack.setEnabled(true);
			int track = 0;
			if (tags.getValues(Id3Frame.TRACK_NUMBER) != null && tags.getValues(Id3Frame.TRACK_NUMBER).size() > 0) {
				final String strTrack = (String)tags.getValues(Id3Frame.TRACK_NUMBER).toArray()[0];
				track = Integer.parseInt(strTrack.split("/")[0]);
			}
			mNpTrack.setValue(track);
		} else {
			mNpTrack.setEnabled(false);
		}
	}

	private void populateSpinner() {	
		final TagCloud tags = MainActivity.sSelectionController.getSelection().getTagCloud();
		final Collection<String> currentFrame = tags.getValues(Id3Frame.GENRE);
		
		int index = 0;
		if (!currentFrame.isEmpty())  {
			index = ID3v1Genres.matchGenreDescription((String)tags.getValues(Id3Frame.GENRE).toArray()[0]);
		}
		if (currentFrame.size() > Constants.ONE_ITEM) {
			tags.setFrameUnselected(Id3Frame.GENRE);
		}
		if (index < mSpGenre.getAdapter().getCount()) {
			mSpGenre.setSelection(index < 0? 0 : index);
		}
	}
	
	private void populateCoverButton() {
		if (this.mCoverWidth <= 0) {
			this.mCoverWidth = this.mBtnCover.getWidth();
		}

		if (this.mCoverHeight <= 0) {
			this.mCoverHeight = this.mBtnCover.getHeight();
		}

		final File coverFile = MainActivity.sSelectionController.getSelection().getTagCloud().getCoverFile();
		if (coverFile != null && MainActivity.sSelectionController.getSelection().getTagCloud().getValues(Id3Frame.ALBUM_TITLE).size() == 1) {
			final Bitmap coverBitmap = BitmapFactory.decodeFile(coverFile.getAbsolutePath());
			if (coverBitmap == null) {
				this.mBtnCover.setImageResource(R.drawable.ic_mp3file);
			} else {
				this.mBtnCover.setImageBitmap(Bitmap.createScaledBitmap(coverBitmap, this.mCoverWidth, this.mCoverHeight, false));
			}
		} else {
			this.mBtnCover.setImageResource(R.drawable.ic_mp3file);
		}

		this.mBtnCover.setEnabled(MainActivity.sSelectionController.getSelection().getTagCloud().getValues(Id3Frame.ALBUM_TITLE).size() == 1);
	}

	private void fillWidget(final Collection<String> values, final EditText widget) {
		final int size = values.size();
		if (size == 0) {
			widget.setText(R.string.noValues);
		} else if (size > Constants.ONE_ITEM) {
			widget.setText(R.string.multipleValues);
		} else {
			widget.setText((String)values.toArray()[0]);
		}
	}

	private void updateCheckboxes() {
		//Check the checkboxes, if the frame is currently selected by the TagMap
		final TagCloud tags = MainActivity.sSelectionController.getSelection().getTagCloud();

		this.mCbTitle   	  .setChecked(tags.isSelected(Id3Frame.TITLE	   ));
		this.mCbArtist  	  .setChecked(tags.isSelected(Id3Frame.ARTIST	   ));
		this.mCbAlbum   	  .setChecked(tags.isSelected(Id3Frame.ALBUM_TITLE ));
		this.mCbComposer	  .setChecked(tags.isSelected(Id3Frame.COMPOSER	   ));
		this.mCbTrack   	  .setChecked(tags.isSelected(Id3Frame.TRACK_NUMBER));
		this.mCbGenre   	  .setChecked(tags.isSelected(Id3Frame.GENRE       ));
		this.mCbYear		  .setChecked(tags.isSelected(Id3Frame.YEAR		   ));
		this.mCbAlbumArtist.setChecked(tags.isSelected(Id3Frame.ALBUM_ARTIST));
	}

	private void saveTags() {
		final TagCloud tags = MainActivity.sSelectionController.getSelection().getTagCloud();

		tags.setValue(new FrameValuePair(Id3Frame.TITLE,		this.mEtTitle.getText().toString()));
		tags.setValue(new FrameValuePair(Id3Frame.ARTIST, 		this.mEtArtist.getText().toString()));
		tags.setValue(new FrameValuePair(Id3Frame.ALBUM_TITLE, 	this.mEtAlbum.getText().toString()));
		tags.setValue(new FrameValuePair(Id3Frame.COMPOSER, 	this.mEtComposer.getText().toString()));
		tags.setValue(new FrameValuePair(Id3Frame.YEAR, 		this.mEtYear.getText().toString()));
		tags.setValue(new FrameValuePair(Id3Frame.ALBUM_ARTIST, this.mEtAlbumArtist.getText().toString()));

		tags.setValue(new FrameValuePair(Id3Frame.TRACK_NUMBER, Integer.toString(this.mNpTrack.getValue())));
		tags.setValue(new FrameValuePair(Id3Frame.GENRE, (String) this.mSpGenre.getSelectedItem()));

		MainActivity.sSelectionController.getSelection().getTagCloud().writeLocalChanges();

		final Collection<File> fileSet = MainActivity.sSelectionController.getSelection().getFileSet();
		new SaveTask(this.mMain).execute(fileSet.toArray(new File[fileSet.size()]));
	}

	@Override
	public void setUserVisibleHint(final boolean isVisible) {
		super.setUserVisibleHint(isVisible);
		if (isVisible) {

			this.refresh();
			for (final Id3Frame frame : Id3Frame.values()) {
				MainActivity.sSelectionController.getSelection().getTagCloud().setFrameUnselected(frame);
			}
			this.updateCheckboxes();
		}
	}

	private class CheckboxListener implements OnClickListener {
		private final TagCloud tagCloud;
		private final Id3Frame frame;

		protected CheckboxListener(final TagCloud cloud, final Id3Frame frame) {
			this.tagCloud = cloud;
			this.frame = frame;
		}

		@Override
		public void onClick(final View source) {
			if (source instanceof CheckBox) {
				final CheckBox cbSource = (CheckBox) source;
				if (cbSource.isChecked()) {
					this.tagCloud.setFrameSelected(this.frame);
				} else {
					this.tagCloud.setFrameUnselected(this.frame);
				}
			} else {
				Log.w(Constants.MAIN_TAG, "Tried to invoke onClick() of ItemListener with " + source.getClass().toString());
			}

		}
	}

	private class ItemListener implements TextWatcher {
		private final TagCloud tagCloud;
		private final Id3Frame frame;

		public ItemListener(final TagCloud cloud, final Id3Frame frame) {
			this.tagCloud = cloud;
			this.frame = frame;
		}

		@Override
		public void afterTextChanged(final Editable source) {
			final String newString = source.toString();
			if (newString.length() > 0) {
				this.tagCloud.setFrameSelected(this.frame);
			} else {
				this.tagCloud.setFrameUnselected(this.frame);
			}
			if (newString.equals(TagEditFragment.this.getResources().getString(R.string.noValues)) ||
					newString.equals(TagEditFragment.this.getResources().getString(R.string.multipleValues))) {
				this.tagCloud.setFrameUnselected(this.frame);
			}
			TagEditFragment.this.updateCheckboxes();
		}

		@Override
		public void beforeTextChanged(final CharSequence text, final int start, final int count, final int after) {
			//do nothing
		}

		@Override
		public void onTextChanged(final CharSequence text, final int start, final int count, final int after) {
			//do nothing
		}

	}
}
