package com.uncompilable.mp3tagger;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


public class AlbumGridAdapter extends ArrayAdapter<Uri> {
	Uri[] mImages;

	public AlbumGridAdapter(Context context, int resource, Uri[] objects) {
		super(context, resource, objects);
		mImages = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView result = new ImageView(this.getContext());
		result.setImageURI(mImages[position]);
		return result;
	}
	
}
