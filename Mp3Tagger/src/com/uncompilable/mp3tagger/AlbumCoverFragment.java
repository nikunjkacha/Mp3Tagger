package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.uncompilable.mp3tagger.controll.SelectionController;
import com.uncompilable.mp3tagger.utility.Constants;

public class AlbumCoverFragment extends Fragment {
	private final AlbumGridAdapter mAdapter;

	protected AlbumCoverFragment(final AlbumGridAdapter adapter) {
		super();
		this.mAdapter = adapter;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.albumcover_fragment, container, false);

		final GridView gvCoverGrid = (GridView) root.findViewById(R.id.gvCoverGrid);
		gvCoverGrid.setAdapter(this.mAdapter);

		final Button btnSaveCover = (Button) root.findViewById(R.id.btnSaveCover);
		btnSaveCover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View source) {
				final Bitmap cover = AlbumCoverFragment.this.mAdapter.getSelected();
				if (cover != null) {
					final Collection<File> parentDirs = new HashSet<File>();
					for (final File file : MainActivity.sSelectionController.getSelection().getFileSet()) {
						parentDirs.add(file.getParentFile());
					}

					if (parentDirs.size() > Constants.ONE_ITEM) {
						final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AlbumCoverFragment.this.getActivity());
						dialogBuilder.setMessage(R.string.coverAlert);
						dialogBuilder.setTitle(R.string.coverAlert_title);
						dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog, final int which) {
								AlbumCoverFragment.this.saveCover(cover, parentDirs);

							}
						});

						dialogBuilder.setNegativeButton(R.string.cancel, null);

						final AlertDialog dialog = dialogBuilder.create();
						dialog.show();
					} else {
						AlbumCoverFragment.this.saveCover(cover, parentDirs);
					}

				}

				AlbumCoverFragment.this.getActivity().finish();
			}

		});

		return root;
	}


	private void saveCover(final Bitmap cover, final Collection<File> parentDirs) {
		for (final File dir : parentDirs) {
			final String path = dir.getAbsolutePath().concat("/cover.jpg");
			OutputStream out = null;

			for (final String oldFileName : SelectionController.COVER_NAMES) {
				final String oldFilePath = dir.getAbsolutePath().concat("/".concat(oldFileName));
				final File oldFile = new File(oldFilePath);
				if (oldFile.exists()) {
					oldFile.delete();
				}
			}

			try {
				out = new FileOutputStream(path);
				cover.compress(CompressFormat.JPEG, 90, out);
				MainActivity.sSelectionController.getSelection().getTagCloud().setCoverFile(new File(path));
			} catch (final IOException e) {
				Log.e(Constants.MAIN_TAG, "Could not write cover file", e);
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (final IOException e) {
					//Do nothing
				}
			}
		}
	}
}
