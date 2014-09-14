package com.uncompilable.mp3tagger;

import java.util.Observable;
import java.util.Observer;

import com.uncompilable.mp3tagger.utility.ObservableArray;

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


public class AlbumGridAdapter extends ArrayAdapter<Bitmap> {
	private ObservableArray<Bitmap> mImages;
	
	private int mSelected;

	protected final int NONE_SELECTED = -1; 

	public AlbumGridAdapter(Context context, Bitmap[] objects) {
		this(context, new ObservableArray<Bitmap>(objects));
	}

	public AlbumGridAdapter(Context context, ObservableArray<Bitmap> objects) {
		super(context, R.layout.covergrid_item, objects.returnArray());
		mSelected = NONE_SELECTED;
		mImages = objects;
	
		registerObservableArray(mImages);
	}
	
	public AlbumGridAdapter(Context context) {
		this(context, new Bitmap[0]);
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
		
		ivCover.setImageBitmap(mImages.get(position));

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

	public void setItems(Bitmap[] items) {
		if (this.mImages != null) {
			this.mImages.deleteObservers();
		}
		this.mImages = new ObservableArray<Bitmap>(items);
		this.registerObservableArray(mImages);
	}
	
	public void setItems(ObservableArray<Bitmap> items) {
		this.mImages.deleteObservers();
		this.mImages = items;
		this.registerObservableArray(mImages);
	}

	@Override
	public int getCount() {
		return mImages == null? 0 : mImages.length();
	}

	public void selected(int pos) {
		if (pos == mSelected) {
			mSelected = NONE_SELECTED;
		} else if (mImages.get(pos) != null){
			this.mSelected = pos;
		}
	}

	public Bitmap getSelected() {
		if (mSelected > getCount() - 1 || mSelected < 0) 
			return null;
		return mImages.get(mSelected);
	}
	
	private void registerObservableArray(ObservableArray<Bitmap> array) {
		array.addObserver(new Observer() {

			@Override
			public void update(Observable observable, Object data) {
				notifyDataSetChanged();
			}
			
		});
	}

}
