package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.uncompilable.mp3tagger.utility.Constants;

public abstract class AbstractFileListAdapter extends ArrayAdapter<File> {
	protected static MediaPlayer sPlayer = new MediaPlayer();
	protected static File sPlaying;

	protected final MainActivity mMain;

	protected File[] mFiles;

	public AbstractFileListAdapter(final MainActivity main, final File[] files) {
		super(main, R.layout.list_item, files);

		this.setDisplayedFiles(files);

		this.mMain = main;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View result = convertView;

		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			result = inflater.inflate(R.layout.list_item, parent, false);
		}

		final TextView  tvName      = (TextView ) result.findViewById(R.id.tvFilename);
		final CheckBox  cbSelected  = (CheckBox ) result.findViewById(R.id.cbSelected);

		final ItemClickListener listener = new ItemClickListener(this.mFiles[position], this);
		result.setOnClickListener(listener);
		tvName.setText(this.mFiles[position].getName());

		if (this.mFiles[position].isDirectory() && Constants.SUPER_DIR.equals(this.mFiles[position].getName())) {
			cbSelected.setVisibility(View.INVISIBLE);
		} else {
			cbSelected.setVisibility(View.VISIBLE);
			cbSelected.setChecked(MainActivity.sSelectionController.getSelection().contains(this.mFiles[position]));
			cbSelected.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View source) {
					if (cbSelected.isChecked()) {
						final Collection<File> files = MainActivity.sSelectionController.scanDirectory(AbstractFileListAdapter.this.mFiles[position]);
						new SelectionTask(AbstractFileListAdapter.this.mMain).execute(files.toArray(new File[files.size()]));
					} else {
						MainActivity.sSelectionController.removeFromSelection(AbstractFileListAdapter.this.mFiles[position]);
					}
				}
			});
		}
		return result;
	}


	public void setDisplayedFiles(final File[] files) {
		this.mFiles = files.clone();

		//Sort entries: directory before files, compare same types by name
		Arrays.sort(this.mFiles, new Comparator<File>() {
			@Override
			public int compare(final File file1, final File file2) {
				if (file1.isDirectory() && file2.isFile()) return -1;
				if (file1.isFile() && file2.isDirectory()) return 1;
				return file1.getName().compareTo(file2.getName());
			}
		});
		this.notifyDataSetChanged();
	}

	public final File[] getDisplayedFiles() {
		return this.mFiles.clone();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(final int position) {
		return true;
	}

	@Override
	public int getCount() {
		return this.mFiles.length;
	}


	protected class ItemClickListener implements OnClickListener {
		private final File linkedFile;
		private final AbstractFileListAdapter adapter;

		protected ItemClickListener(final File linkedFile, final AbstractFileListAdapter adapter) {
			super();
			this.linkedFile = linkedFile;
			this.adapter = adapter;
		}


		@Override
		public void onClick(final View source) {
			if (this.linkedFile.isFile() && this.linkedFile.getName().endsWith(".mp3") &&
					PreferenceManager.getDefaultSharedPreferences(AbstractFileListAdapter.this.mMain).getBoolean(Constants.PREF_KEY_PLAYABLE, true)) {
				final MediaPlayer player = AbstractFileListAdapter.sPlayer;
				try {
					if (player.isPlaying()) {
						player.stop();
						player.reset();
					}
					if (this.linkedFile.equals(AbstractFileListAdapter.sPlaying)) {
						AbstractFileListAdapter.sPlaying = null;
					} else {
						player.setDataSource(new FileInputStream(this.linkedFile).getFD());
						player.prepare();
						player.start();
						
						player.setOnCompletionListener(completionListener);
						AbstractFileListAdapter.sPlaying = this.linkedFile;
					}
					if (this.adapter != null) {
						this.adapter.notifyDataSetChanged();
					}
				} catch (IllegalArgumentException | IllegalStateException | IOException e) {
					Log.w(Constants.MAIN_TAG, "Could not play file " + this.linkedFile.getName() + ".", e);
				}
			}
		}
		
		
		private final OnCompletionListener completionListener = new OnCompletionListener() {
			
			@Override
			public void onCompletion(final MediaPlayer player) {
				sPlaying = null;
				sPlayer.reset();
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
			}
		};
	}
	
}
