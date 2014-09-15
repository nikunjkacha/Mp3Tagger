package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.uncompilable.mp3tagger.utility.Constants;

public class SaveTask extends AsyncTask<File, Integer, String> {
	private final ProgressDialog mDialog;
	private final MainActivity mMain;

	public SaveTask(final MainActivity main) {
		super();
		this.mMain = main;
		this.mDialog = new ProgressDialog(main);
	}

	@Override
	protected void onPreExecute() {
		this.mDialog.setProgress(0);
		this.mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		this.mDialog.setTitle(this.mMain.getResources().getString(R.string.diaSaveFiles));

		this.mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, this.mMain.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				SaveTask.this.cancel(true);
			}
		});

		this.mDialog.setCanceledOnTouchOutside(false);
		this.mDialog.show();

	}

	@Override
	protected String doInBackground(final File... files) {
		this.mDialog.setMax(files.length);
		int progress = 0;

		try {
			for (final File file : files) {
				MainActivity.sSelectionController.getIOController().writeTags(file);

				this.publishProgress(progress++);
			}
		} catch (UnsupportedTagException | InvalidDataException
				| NotSupportedException | IOException e) {
			Log.e(Constants.MAIN_TAG, "ERROR: Could not write tags to file", e);
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(final Integer... progress) {
		this.mDialog.setProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(final String result) {
		this.mDialog.hide();
	}
}
