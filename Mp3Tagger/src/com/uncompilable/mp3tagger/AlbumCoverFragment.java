package com.uncompilable.mp3tagger;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class AlbumCoverFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.albumcover_fragment, container, false);

		final MainActivity main = (MainActivity)this.getActivity();

		final AlbumGridAdapter adapter = new AlbumGridAdapter(main);
		
		new AsyncTask<AlbumGridAdapter, String, String>() {

			@Override
			protected String doInBackground(AlbumGridAdapter... adapter) {
				adapter[0].setItems(main.getSelectionController().getAlbumCoverController().getAlbumImages("Blood Money Tom Waits"));
				
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				adapter.notifyDataSetChanged();
			}
			
		}.execute(adapter);
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(main).build();
		ImageLoader.getInstance().init(config);
		
		GridView gvCoverGrid = (GridView) root.findViewById(R.id.gvCoverGrid);
		gvCoverGrid.setAdapter(adapter);
		
		return root;
	}
}
