package com.uncompilable.mp3tagger;

import java.io.IOException;

import org.json.JSONException;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.uncompilable.mp3tagger.utility.ObservableArray;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

public class CoverFetchTask extends AsyncTask<String, String, String> {
	private AlbumCoverActivity mActivity;

	private static final String SUCCESS = "success";
	private static final String FAILURE = "failure";

	protected CoverFetchTask(AlbumCoverActivity activity) {
		this.mActivity = activity;
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mActivity).build();
		ImageLoader.getInstance().init(config);
	}

	@Override 
	protected void onPreExecute() {
	}

	@Override
	protected String doInBackground(String... mes) {
		try {
			String[] urls = MainActivity.sSelectionController.getAlbumCoverController().getAlbumImages();
			final ObservableArray<Bitmap> images = new ObservableArray<Bitmap>(new Bitmap[urls.length]);
			mActivity.getAdapter().setItems(images);

			for (int i=0; i<urls.length; i++) {
				
				final int pos = i;
				ImageLoader.getInstance().loadImage(urls[i], new ImageLoadingListener() {
					@Override
					public void onLoadingComplete(String uri, View view, final Bitmap bitmap) {
						mActivity.runOnUiThread(new Runnable() {
							public void run() {
								images.set(pos, bitmap);
							}
						});
							
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {}

					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}

					@Override
					public void onLoadingStarted(String arg0, View arg1) {}
				});

			}
			return SUCCESS;
		} catch (IOException | JSONException e) {
			return FAILURE;
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {

	}

	@Override
	protected void onPostExecute(String result) {
		if (SUCCESS.equalsIgnoreCase(result)) {
			mActivity.setVisibleFragment(AlbumCoverActivity.COVER_FRAGMENT);
			mActivity.getAdapter().notifyDataSetChanged();
		} else if (FAILURE.equalsIgnoreCase(result)) {
			mActivity.setVisibleFragment(AlbumCoverActivity.ERROR_FRAGMENT);
		}
	}
}

