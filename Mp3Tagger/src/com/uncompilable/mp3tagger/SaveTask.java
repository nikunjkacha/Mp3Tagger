package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.IOException;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.uncompilable.mp3tagger.utility.Constants;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class SaveTask extends AsyncTask<File, Integer, String> {
	private ProgressDialog mDialog;
	private MainActivity mMain;
	
	public SaveTask(MainActivity main) {
		super();
		this.mMain = main;
		this.mDialog = new ProgressDialog(main);
	}

	@Override
	protected void onPreExecute() {
		this.mDialog.setProgress(0);
		this.mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		this.mDialog.setTitle(mMain.getResources().getString(R.string.diaSaveFiles));
		
		this.mDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mMain.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancel(true);
			}
		});
		
		this.mDialog.show();
		
	}
	
	@Override
	protected String doInBackground(File... files) {
		mDialog.setMax(files.length);
		int progress = 0;
		
		try {
			for (File file : files) {
				mMain.getSelectionController().getIOController().writeTags(file);
				
				publishProgress(progress++);
			}
		} catch (UnsupportedTagException | InvalidDataException
				| NotSupportedException | IOException e) {
			Log.e(Constants.MAIN_TAG, "ERROR: Could not write tags to file", e);
		}
		
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		this.mDialog.setProgress(progress[0]);
	}
	
	@Override
	protected void onPostExecute(String result) {
		this.mDialog.hide();
	}
}
