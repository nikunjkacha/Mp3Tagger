package com.uncompilable.mp3tagger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.uncompilable.mp3tagger.error.InvalidFileException;
import com.uncompilable.mp3tagger.error.NoTagAssociatedWithFileException;
import com.uncompilable.mp3tagger.utility.Constants;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class SelectionTask extends AsyncTask<File, Integer, String> {
	private ProgressDialog mDialog;
	private MainActivity mMain;
	private Collection<File> mAdded;
	private boolean mError;
	
	public SelectionTask(MainActivity main) {
		super();
		this.mMain = main;
		this.mDialog = new ProgressDialog(main);
		mAdded = new ArrayList<File>();
	}

	@Override
	protected void onPreExecute() {
		this.mMain.setUpdateSelectionHeader(false);
		
		this.mError = false;
		
		this.mDialog.setProgress(0);
		this.mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		this.mDialog.setTitle(mMain.getResources().getString(R.string.diaAddFiles));
		
		this.mDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mMain.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancel(true);
			}
		});
		
		this.mDialog.setCanceledOnTouchOutside(false);
		this.mDialog.show();
		
	}
	
	@Override
	protected String doInBackground(File... files) {
		this.mDialog.setMax(files.length);
		int progress = 0;
		for (File file : files) {
			try {
				MainActivity.sSelectionController.addToSelection(file);
				mAdded.add(file);
				progress++;
			} catch (UnsupportedTagException | InvalidDataException
					| InvalidFileException | IOException | NoTagAssociatedWithFileException e) {
				Log.e(Constants.MAIN_TAG, "ERROR: could not add file " + file + " to Selection!", e);
				this.mError = true;
			}
			
			publishProgress(progress);
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
		this.mMain.setUpdateSelectionHeader(true);
		
		if (this.mError) {
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mMain);
			dialogBuilder.setMessage(R.string.selectionError);
			dialogBuilder.setTitle(R.string.error_title);

			dialogBuilder.setPositiveButton(R.string.confirm, null);

			AlertDialog dialog = dialogBuilder.create();
			dialog.show();
		}
	}
	
	@Override
	protected void onCancelled() {
		for (File toRemove : mAdded) { //Remove all added files on cancel
			MainActivity.sSelectionController.removeFromSelection(toRemove);
		}
		
		this.mMain.setUpdateSelectionHeader(true);

	}
}
