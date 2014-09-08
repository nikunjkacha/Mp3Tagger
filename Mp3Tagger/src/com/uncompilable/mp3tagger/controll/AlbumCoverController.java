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


	public String[] getAlbumImages(String... searchTerms) {
		URL url;
		URLConnection connection;
		
		String searchTerm = searchTerms[0].replace(" ", "%20");
		for (int i=1; i<searchTerms.length; i++) {
			searchTerm.concat("%20" + searchTerms[0]);
		}
		String urlString = API_URL + searchTerm;
		
		try {
			Log.d(Constants.MAIN_TAG, "Trying to connect to URL " + urlString);
			url = new URL(urlString);
			connection = url.openConnection();
		} catch (IOException e) {
			Log.e(Constants.MAIN_TAG, "ERROR: Could not connect to URL " + urlString, e);
			return new String[0];
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

			String[] result = new String[resultArray.length()];
			for (int i=0; i < resultArray.length(); i++) {
				result[i] = resultArray.getJSONObject(i).getString("url");
				Log.d(Constants.MAIN_TAG, result[i]);
			}
			return result;

		} catch (IOException | JSONException e) {
			Log.e(Constants.MAIN_TAG, "Error trying to read JSON file.", e);
			try { reader.close(); } catch (IOException e1) {}
			return new String[0];
		}
	}
}
