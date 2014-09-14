package com.uncompilable.mp3tagger;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class AlbumCoverErrorFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.albumcover_error_fragment, container, false);
		
		ImageButton btnRefresh = (ImageButton) root.findViewById(R.id.btnRefresh);
		
		btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View source) {
				AlbumCoverActivity parentActivity = (AlbumCoverActivity) getActivity();
				parentActivity.runFetchTask();
			}
			
		});
		
		return root;
	}

}
