package com.uncompilable.mp3tagger;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


public class AlbumGridAdapter extends ArrayAdapter<String> {
	private String[] mImages;

	public AlbumGridAdapter(Context context, String[] objects) {
		super(context, R.layout.covergrid_item, objects);
		mImages = objects;
	}
	
	public AlbumGridAdapter(Context context) {
		super(context, R.layout.covergrid_item);
		mImages = new String[0];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView ivCover = new ImageView(this.getContext());
		ImageLoader.getInstance().displayImage(mImages[position], ivCover);
		return ivCover;
	}
	
	public void setItems(String[] items) {
		this.mImages = items;
	}
	
	@Override
	public int getCount() {
		return mImages == null? 0 : mImages.length;
	}
	
}
