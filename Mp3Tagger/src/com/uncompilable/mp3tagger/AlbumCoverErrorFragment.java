package com.uncompilable.mp3tagger;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class AlbumCoverErrorFragment extends Fragment {
	
	public AlbumCoverErrorFragment() {
		super();
	}
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.albumcover_error_fragment, container, false);

		final ImageButton btnRefresh = (ImageButton) root.findViewById(R.id.btnRefresh);

		btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View source) {
				final AlbumCoverActivity parentActivity = (AlbumCoverActivity) AlbumCoverErrorFragment.this.getActivity();
				parentActivity.runFetchTask();
			}

		});

		return root;
	}

}
