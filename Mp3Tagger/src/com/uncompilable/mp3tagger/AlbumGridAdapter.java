package com.uncompilable.mp3tagger;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.uncompilable.mp3tagger.utility.ObservableArray;


public class AlbumGridAdapter extends ArrayAdapter<Bitmap> {
	private ObservableArray<Bitmap> mImages;

	private int mSelected;

	protected final int NONE_SELECTED = -1;

	public AlbumGridAdapter(final Context context, final Bitmap[] objects) {
		this(context, new ObservableArray<Bitmap>(objects));
	}

	public AlbumGridAdapter(final Context context, final ObservableArray<Bitmap> objects) {
		super(context, R.layout.covergrid_item, objects.returnArray());
		this.mSelected = this.NONE_SELECTED;
		this.mImages = objects;

		this.registerObservableArray(this.mImages);
	}

	public AlbumGridAdapter(final Context context) {
		this(context, new Bitmap[0]);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		ImageView ivCover = (ImageView) convertView;
		if (convertView == null) {
			ivCover = new ImageView(this.getContext());
			ivCover.setScaleType(ScaleType.CENTER_CROP);
			ivCover.setPadding(10, 10, 10, 10);
			ivCover.setLayoutParams(new GridView.LayoutParams(290,290));
		}

		ivCover.setImageBitmap(this.mImages.get(position));

		ivCover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View source) {
				AlbumGridAdapter.this.selected(position);
				AlbumGridAdapter.this.notifyDataSetChanged();
			}

		});

		if (position == this.mSelected) {
			ivCover.setBackgroundColor(Color.GREEN);
		} else {
			ivCover.setBackgroundColor(Color.WHITE);
		}
		return ivCover;
	}

	public void setItems(final Bitmap[] items) {
		if (this.mImages != null) {
			this.mImages.deleteObservers();
		}
		this.mImages = new ObservableArray<Bitmap>(items.clone());
		this.registerObservableArray(this.mImages);
	}

	public void setItems(final ObservableArray<Bitmap> items) {
		this.mImages.deleteObservers();
		this.mImages = items;
		this.registerObservableArray(this.mImages);
	}

	@Override
	public int getCount() {
		return this.mImages == null? 0 : this.mImages.length();
	}

	public void selected(final int pos) {
		if (pos == this.mSelected) {
			this.mSelected = this.NONE_SELECTED;
		} else if (this.mImages.get(pos) != null){
			this.mSelected = pos;
		}
	}

	public Bitmap getSelected() {
		if (this.mSelected > this.getCount() - 1 || this.mSelected < 0)
			return null;
		return this.mImages.get(this.mSelected);
	}

	private void registerObservableArray(final ObservableArray<Bitmap> array) {
		array.addObserver(new Observer() {

			@Override
			public void update(final Observable observable, final Object data) {
				AlbumGridAdapter.this.notifyDataSetChanged();
			}

		});
	}

}
