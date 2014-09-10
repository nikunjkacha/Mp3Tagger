package com.uncompilable.mp3tagger;

import java.util.Observable;
import java.util.Observer;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.uncompilable.mp3tagger.model.FileSelection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class AlbumCoverFragment extends Fragment {
	private MainActivity mMain;
	private AsyncTask<String, String, String> mFetchTask;
	private AlbumGridAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.albumcover_fragment, container, false);

		mMain = (MainActivity)this.getActivity();

		mAdapter = new AlbumGridAdapter(mMain);
		
		final FileSelection fileSelection = mMain.getSelectionController().getSelection();
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
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mMain).build();
		ImageLoader.getInstance().init(config);
		
		GridView gvCoverGrid = (GridView) root.findViewById(R.id.gvCoverGrid);
		gvCoverGrid.setAdapter(mAdapter);
		
		return root;
	}
	
	
	private class FetchTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... mes) {
			mAdapter.setItems(mMain.getSelectionController().getAlbumCoverController().getAlbumImages());
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			mAdapter.notifyDataSetChanged();
		}
		
		
	}
}
