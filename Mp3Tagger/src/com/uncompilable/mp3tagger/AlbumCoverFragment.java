package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.uncompilable.mp3tagger.controll.SelectionController;
import com.uncompilable.mp3tagger.model.FileSelection;
import com.uncompilable.mp3tagger.utility.Constants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

public class AlbumCoverFragment extends Fragment {
	private AsyncTask<String, String, String> mFetchTask;
	private AlbumGridAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.albumcover_fragment, container, false);

		mAdapter = new AlbumGridAdapter(getActivity());

		final FileSelection fileSelection = MainActivity.sSelectionController.getSelection();
		fileSelection.addObserver(new Observer() {

			@Override
			public void update(Observable source, Object data) {
				if (mFetchTask != null) 
					mFetchTask.cancel(true);

				mFetchTask = new FetchTask();
				mFetchTask.execute("");
			}

		});
		mFetchTask = new FetchTask();
		mFetchTask.execute("");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
		ImageLoader.getInstance().init(config);

		GridView gvCoverGrid = (GridView) root.findViewById(R.id.gvCoverGrid);
		gvCoverGrid.setAdapter(mAdapter);

		Button btnSaveCover = (Button) root.findViewById(R.id.btnSaveCover);
		btnSaveCover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View source) {
				final Bitmap cover = mAdapter.getSelected();
				if (cover != null) {
					final Collection<File> parentDirs = new HashSet<File>();
					for (File file : MainActivity.sSelectionController.getSelection().getFileSet()) {
						parentDirs.add(file.getParentFile());
					}

					if (parentDirs.size() > 1) {
						AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
						dialogBuilder.setMessage(R.string.coverAlert);
						dialogBuilder.setTitle(R.string.coverAlert_title);
						dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								saveCover(cover, parentDirs);
							}
						});
						
						dialogBuilder.setNegativeButton(R.string.cancel, null);
						
						AlertDialog dialog = dialogBuilder.create();
						dialog.show();
					} else {
						saveCover(cover, parentDirs);
					}

				}


			}

		});

		return root;
	}

	
	private void saveCover(final Bitmap cover, final Collection<File> parentDirs) {
		for (File dir : parentDirs) {
			String path = dir.getAbsolutePath().concat("/cover.jpg");
			OutputStream out = null;

			for (String oldFileName : SelectionController.COVER_NAMES) {
				String oldFilePath = dir.getAbsolutePath().concat("/".concat(oldFileName));
				File oldFile = new File(oldFilePath);
				if (oldFile.exists()) {
					oldFile.delete();
				}
			}

			try {
				out = new FileOutputStream(path);
				cover.compress(CompressFormat.JPEG, 90, out);
				MainActivity.sSelectionController.getSelection().getTagCloud().setCoverFile(new File(path));
			} catch (IOException e) {
				Log.e(Constants.MAIN_TAG, "Could not write cover file", e);
			} finally {
				try {
					if (out != null)
						out.close();
				} catch (IOException e) {
					//Do nothing
				}
			}
		}
	}

	private class FetchTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... mes) {
			mAdapter.setItems(MainActivity.sSelectionController.getAlbumCoverController().getAlbumImages());
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			mAdapter.notifyDataSetChanged();
		}


	}
}
