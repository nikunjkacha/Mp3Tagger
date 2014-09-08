package com.uncompilable.mp3tagger;

import java.io.File;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * This adapter takes a path to a directory and adapts its contents
 * for display in a selection list.
 * @author dennis
 *
 */
public class SimpleFileListAdapter extends AbstractFileListAdapter {
	public static final int NONE_PLAYING = -1;

	public SimpleFileListAdapter(MainActivity main, File[] files) {
		super(main, files);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View result = super.getView(position, convertView, parent);

		final ImageView ivIcon = (ImageView) result.findViewById(R.id.ivIcon);

		if (mFiles[position].equals(AbstractFileListAdapter.sPlaying)) {
			ivIcon.setImageResource(R.drawable.ic_playing);
		} else {
			ivIcon.setImageDrawable(null);
		}

		return result;
	}
}
