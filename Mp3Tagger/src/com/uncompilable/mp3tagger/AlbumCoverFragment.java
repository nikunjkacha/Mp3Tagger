package com.uncompilable.mp3tagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AlbumCoverFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.albumcover_fragment, container, false);

		final MainActivity main = (MainActivity)this.getActivity();
		new Thread() {
			@Override
			public void run() {
				main.getSelectionController().getAlbumCoverController().getAlbumImages("Blood%20Money");
			}
		}.start();
		
		root.findViewById(R.id.gvCoverGrid);
		//AlbumGridAdapter adapter = new AlbumGridAdapter()

		return root;
	}
}
