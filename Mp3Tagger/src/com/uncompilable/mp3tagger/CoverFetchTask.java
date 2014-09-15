package com.uncompilable.mp3tagger;

import java.io.IOException;

import org.json.JSONException;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.uncompilable.mp3tagger.utility.ObservableArray;

public class CoverFetchTask extends AsyncTask<String, String, String> {
	private final AlbumCoverActivity mActivity;

	private static final String SUCCESS = "success";
	private static final String FAILURE = "failure";

	protected CoverFetchTask(final AlbumCoverActivity activity) {
		super();
		
		this.mActivity = activity;

		final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.mActivity).build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	protected void onPreExecute() {
		//do nothing
	}

	@Override
	protected String doInBackground(final String... mes) {
		try {
			final String[] urls = MainActivity.sSelectionController.getAlbumCoverController().getAlbumImages();
			final ObservableArray<Bitmap> images = new ObservableArray<Bitmap>(new Bitmap[urls.length]);
			this.mActivity.getAdapter().setItems(images);

			for (int i=0; i<urls.length; i++) {

				final int pos = i;
				ImageLoader.getInstance().loadImage(urls[i], new ImageLoadingListener() {
					@Override
					public void onLoadingComplete(final String uri, final View view, final Bitmap bitmap) {
						CoverFetchTask.this.mActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								images.set(pos, bitmap);
							}
						});

					}

					@Override
					public void onLoadingCancelled(final String arg0, final View arg1) {
						//Do nothing
					}

					@Override
					public void onLoadingFailed(final String arg0, final View arg1, final FailReason arg2) {
						//Do nothing
					}

					@Override
					public void onLoadingStarted(final String arg0, final View arg1) {
						//Do nothing
					}
				});

			}
			return SUCCESS;
		} catch (IOException | JSONException e) {
			return FAILURE;
		}
	}

	@Override
	protected void onProgressUpdate(final String... values) {
		//do nothing
	}

	@Override
	protected void onPostExecute(final String result) {
		if (SUCCESS.equalsIgnoreCase(result)) {
			this.mActivity.setVisibleFragment(AlbumCoverActivity.COVER_FRAGMENT);
			this.mActivity.getAdapter().notifyDataSetChanged();
		} else if (FAILURE.equalsIgnoreCase(result)) {
			this.mActivity.setVisibleFragment(AlbumCoverActivity.ERROR_FRAGMENT);
		}
	}
}

