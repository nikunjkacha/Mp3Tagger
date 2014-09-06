package com.uncompilable.mp3tagger;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This adapter takes a path to a directory and adapts its contents
 * for display in a selection list.
 * @author dennis
 *
 */
public class FileListAdapter extends ArrayAdapter<File> {
	private File[] mFiles;
	private Context mContext;
	
	public FileListAdapter(Context context, File[] files) {
		super(context, R.layout.list_item, files);
		
		mContext = context;
		setDisplayedFiles(files);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = convertView;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			result = inflater.inflate(R.layout.list_item, parent, false);
		}
		
		ImageView ivIcon      = (ImageView) result.findViewById(R.id.ivIcon    );
		TextView  tvName      = (TextView ) result.findViewById(R.id.tvFilename);
		CheckBox  cbSelected  = (CheckBox ) result.findViewById(R.id.cbSelected);
		
		ivIcon.setImageResource(mFiles[position].isFile()? R.drawable.ic_mp3file : R.drawable.ic_directory);
		tvName.setText(mFiles[position].getName());
		cbSelected.setChecked(false);;
		
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
}
