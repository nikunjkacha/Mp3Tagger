package com.uncompilable.mp3tagger.controll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.uncompilable.mp3tagger.model.Id3Frame;
import com.uncompilable.mp3tagger.model.TagCloud;
import com.uncompilable.mp3tagger.utility.Constants;

public class AlbumCoverController {
	private final SelectionController mSelectionCtrl;

	private static final String API_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";

	public AlbumCoverController(final SelectionController selectionCntrl) {
		super();
		this.mSelectionCtrl = selectionCntrl;

	}

	/**
	 * Returns an Array of Strings with Image-URL's for the album in the Collection.
	 * Returns null, of more than one Album Title is in the selection.
	 * @return - The Image URL's or null.
	 * @throws IOException
	 * @throws JSONException
	 */
	public String[] getAlbumImages() throws JSONException, IOException {
		final TagCloud tags = this.mSelectionCtrl.getSelection().getTagCloud();
		final String[] albums  = tags.getValues(Id3Frame.ALBUM_TITLE).toArray(new String[tags.getValues(Id3Frame.ALBUM_TITLE).size()]);
		final String[] artists = tags.getValues(Id3Frame.ARTIST).toArray(new String[tags.getValues(Id3Frame.ARTIST).size()]);

		// Only search for Images, if there is only one Album Title
		if (albums.length == Constants.ONE_ITEM) {

			String searchTerm = albums[0].replace(" ", "%20");

			// Add artist to search string, if there is only one
			if (artists.length == Constants.ONE_ITEM) {
				searchTerm = searchTerm.concat("%20".concat(artists[0].replace(" ", "%20")));
			}

			return this.connect(searchTerm);

		} else
			return null;
	}

	private String[] connect(final String searchTerm) throws JSONException, IOException {
		URL url;
		URLConnection connection;
		final String urlString = API_URL + searchTerm;

		Log.d(Constants.MAIN_TAG, "Trying to connect to URL " + urlString);
		url = new URL(urlString);
		connection = url.openConnection();

		String line;
		final StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;

		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while((line = reader.readLine()) != null) {
			builder.append(line);
		}
		final JSONObject json = new JSONObject(builder.toString());

		final JSONArray resultArray = json.getJSONObject("responseData").getJSONArray("results");

		final String[] result = new String[resultArray.length()];
		for (int i=0; i < resultArray.length(); i++) {
			result[i] = resultArray.getJSONObject(i).getString("url");
			Log.d(Constants.MAIN_TAG, result[i]);
		}
		return result;
	}
}
