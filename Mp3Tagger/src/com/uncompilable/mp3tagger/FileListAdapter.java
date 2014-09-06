package com.uncompilable.mp3tagger;

import java.io.File;

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
		mFiles = files;
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
		
		ivIcon.setImageResource(R.drawable.ic_directory);
		tvName.setText(mFiles[position].getName());
		cbSelected.setSelected(false);
		
		return result;
	}

}
