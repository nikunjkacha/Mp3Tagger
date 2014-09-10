package com.uncompilable.mp3tagger;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.GridView;


public class AlbumGridAdapter extends ArrayAdapter<String> {
	private String[] mImageURIs;
	private Bitmap[] mImages;
	
	private int mSelected;

	protected final int NONE_SELECTED = -1; 

	public AlbumGridAdapter(Context context, String[] objects) {
		super(context, R.layout.covergrid_item, objects);
		mImageURIs = objects;
		mSelected = NONE_SELECTED;
		mImages = new Bitmap[objects == null? 0 : objects.length];
	}

	public AlbumGridAdapter(Context context) {
		this(context, new String[0]);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView ivCover = (ImageView) convertView;
		if (convertView == null) {
			ivCover = new ImageView(this.getContext());
			ivCover.setScaleType(ScaleType.CENTER_CROP);
			ivCover.setPadding(10, 10, 10, 10);
			ivCover.setLayoutParams(new GridView.LayoutParams(290,290));
		}
		ImageLoader.getInstance().displayImage(mImageURIs[position], ivCover, new ImageLoadingListener() {


			@Override
			public void onLoadingComplete(String uri, View view, Bitmap bitmap) {
				mImages[position] = bitmap;
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {}
		});

		ivCover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View source) {
				selected(position);
				notifyDataSetChanged();
			}

		});

		if (position == mSelected) {
			ivCover.setBackgroundColor(Color.GREEN);
		} else {
			ivCover.setBackgroundColor(Color.WHITE);
		}
		return ivCover;
	}

	public void setItems(String[] items) {
		this.mImageURIs = items;
		this.mImages = new Bitmap[items == null? 0 : items.length];
	}

	@Override
	public int getCount() {
		return mImageURIs == null? 0 : mImageURIs.length;
	}

	public void selected(int pos) {
		if (pos == mSelected) {
			mSelected = NONE_SELECTED;
		} else if (mImages[pos] != null){
			this.mSelected = pos;
		}
	}

	public Bitmap getSelected() {
		if (mSelected > getCount() - 1 || mSelected < 0) 
			return null;
		return mImages[mSelected];
	}

}
