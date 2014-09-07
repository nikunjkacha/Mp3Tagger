package com.uncompilable.mp3tagger.controll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uncompilable.mp3tagger.utility.Constants;

import android.media.Image;
import android.net.Uri;
import android.util.Log;

public class AlbumCoverController {
	private SelectionController mSelectionController;

	private static final String API_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";

	public AlbumCoverController(SelectionController selectionController) {
		super();
		this.mSelectionController = selectionController;
	}


	public Uri[] getAlbumImages(String title) {
		URL url;
		URLConnection connection;


		try {
			url = new URL(API_URL + title);
			connection = url.openConnection();
		} catch (IOException e) {
			Log.e(Constants.MAIN_TAG, "ERROR: Could not connect to URL " + API_URL + title, e);
			return new Uri[0];
		}

		String line;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			JSONObject json = new JSONObject(builder.toString());

			JSONArray resultArray = json.getJSONObject("responseData").getJSONArray("results");

			Uri[] result = new Uri[resultArray.length()];
			for (int i=0; i < resultArray.length(); i++) {
				Uri.Builder uriBuilder = new Uri.Builder();
				uriBuilder.path(resultArray.getJSONObject(i).getString("url"));
				result[i] = uriBuilder.build();
				Log.d(Constants.MAIN_TAG, result[i].getPath());
			}

		} catch (IOException | JSONException e) {
			Log.e(Constants.MAIN_TAG, "Error trying to read JSON file.", e);
			try { reader.close(); } catch (IOException e1) {}
			return new Uri[0];
		}



		return null;
	}
}
