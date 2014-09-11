package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import com.uncompilable.mp3tagger.utility.Constants;

import android.content.Context;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public abstract class AbstractFileListAdapter extends ArrayAdapter<File> {
	protected static MediaPlayer sPlayer = new MediaPlayer();
	protected static File sPlaying = null;

	protected final MainActivity mMain;

	protected File[] mFiles;

	public AbstractFileListAdapter(MainActivity main, File[] files) {
		super(main, R.layout.list_item, files);

		setDisplayedFiles(files);

		this.mMain = main;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View result = convertView;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			result = inflater.inflate(R.layout.list_item, parent, false);
		}

		final TextView  tvName      = (TextView ) result.findViewById(R.id.tvFilename);
		final CheckBox  cbSelected  = (CheckBox ) result.findViewById(R.id.cbSelected);

		ItemClickListener listener = new ItemClickListener(mFiles[position], this, PreferenceManager.getDefaultSharedPreferences(mMain).getBoolean(Constants.PREF_KEY_PLAYABLE, true));
		result.setOnClickListener(listener);
		tvName.setText(mFiles[position].getName());

		if (mFiles[position].isDirectory() && "..".equals(mFiles[position].getName())) {
			cbSelected.setVisibility(View.INVISIBLE);;
		} else {
			cbSelected.setVisibility(View.VISIBLE);
			cbSelected.setChecked(MainActivity.sSelectionController.getSelection().contains(mFiles[position]));
			cbSelected.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View source) {
					if (cbSelected.isChecked()) {
						Collection<File> files = MainActivity.sSelectionController.scanDirectory(mFiles[position]);
						
						
						
						new SelectionTask(mMain).execute(files.toArray(new File[files.size()]));
					} else {
						MainActivity.sSelectionController.removeFromSelection(mFiles[position]);
					}
				}
			});
		}
		return result;
	}


	public void setDisplayedFiles(final File[] files) {
		mFiles = files.clone();

		//Sort entries: directory before files, compare same types by name
		Arrays.sort(mFiles, new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				if (file1.isDirectory() && file2.isFile()) return -1;
				if (file1.isFile() && file2.isDirectory()) return 1;
				return file1.getName().compareTo(file2.getName());
			}
		});
		notifyDataSetChanged();
	}

	public final File[] getDisplayedFiles() {
		return mFiles;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public int getCount() {
		return mFiles.length;
	}


	protected class ItemClickListener implements OnClickListener {
		private File linkedFile;
		private boolean playable;
		private AbstractFileListAdapter adapter;

		protected ItemClickListener(File linkedFile, AbstractFileListAdapter adapter) {
			this(linkedFile, adapter, true);
		}

		protected ItemClickListener(File linkedFile, AbstractFileListAdapter adapter, boolean playable) {
			super();
			this.linkedFile = linkedFile;
			this.playable = playable;
			this.adapter = adapter;
		}


		@Override
		public void onClick(View source) {
			if (linkedFile.isFile() && linkedFile.getName().endsWith(".mp3") && playable) {
				MediaPlayer player = AbstractFileListAdapter.sPlayer;
				try {
					if (player.isPlaying()) {
						player.stop();
						player.reset();
					}
					if (!linkedFile.equals(AbstractFileListAdapter.sPlaying)) {
						player.setDataSource(new FileInputStream(linkedFile).getFD());
						player.prepare();
						player.start();
						AbstractFileListAdapter.sPlaying = linkedFile;
					} else {
						AbstractFileListAdapter.sPlaying = null;
					}
					if (adapter != null) adapter.notifyDataSetChanged();
				} catch (IllegalArgumentException | IllegalStateException | IOException e) {
					Log.w(Constants.MAIN_TAG, "Could not play file " + linkedFile.getName() + ".", e);
				}
			}
		}
	}
}
