package com.uncompilable.mp3tagger.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains an album Title and a list of URLs that point to 
 * internet image resources. Every URL should point to a possible album
 * cover for the given Album
 * @author dennis
 *
 */
public class AlbumEntry {
	private List<String> mUrls; 
	private String mTitle;
	
	/**
	 * Constructs a new AlbumEntry with the given title and an empty URL-List
	 * @param title - The title of the album
	 */
	public AlbumEntry(String title) {
		super();
		this.setTitle(title);
		this.mUrls = new ArrayList<String>();
	}

	/**
	 * Getter for the albumtitle
	 * @return - The title String of the entry
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Sets the title of the entry.
	 * Careful: resets the associated URL-List!
	 * @param title - The new title
	 */
	public void setTitle(String title) {
		this.mTitle = title;
		if (!mTitle.equals(title)) {
			mUrls = new ArrayList<String>();
		}
	}

	/**
	 * Returns all the URL-Strings associated with this entry in a list.
	 * @return - The List of URL-Strings
	 */
	public List<String> getUrls() {
		return mUrls;
	}

	/**
	 * Associates a new URL-List with this entry
	 * @param urls - The new List of URL-Strings
	 */
	public void setUrls(List<String> urls) {
		this.mUrls = urls;
	}
	
	/**
	 * Adds an URL to this entry
	 * @param url - The URL-String to add.
	 */
	public void addUrl(String url) {
		mUrls.add(url);
	}
	
	/**
	 * Returns true, iff this entry has at least one associated URL
	 * @return - getUrls() != null && getUrls().size() > 0
	 */
	public boolean hasUrls() {
		return mUrls != null && mUrls.size() > 0;
	}
	
}
