package com.uncompilable.mp3tagger.controll;

import java.io.File;
import java.io.IOException;

import android.media.Image;
import android.util.Log;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.uncompilable.mp3tagger.error.NoTagAssociatedWithFileException;
import com.uncompilable.mp3tagger.utility.Constants;


/**
 * Controller for Input / Output
 * @author dennis
 *
 */
public class IOController {
	final SelectionController mSelectionController;

	/**
	 * Initializes a new IOController
	 * @param selectionController - The associated SelectionController
	 */
	public IOController(SelectionController selectionController) {
		super();

		this.mSelectionController = selectionController;
	}

	/**
	 * Reads a file and returns the ID3Tag of this file
	 * @param file - The file to be read
	 * @return - The tag of this file
	 * @throws IOException
	 * @throws TagException
	 * @throws NoTagAssociatedWithFileException
	 * @throws InvalidDataException 
	 * @throws UnsupportedTagException 
	 * @throws CannotReadException 
	 */
	public ID3v2 readFile(File file) throws IOException, NoTagAssociatedWithFileException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3 = new Mp3File(file.getAbsolutePath(), false);
		if (mp3.hasId3v2Tag()) {
			return mp3.getId3v2Tag();
		} else if (mp3.hasId3v1Tag()) {
			ID3v1 fileTag = mp3.getId3v1Tag();
			ID3v2 resultTag = new ID3v24Tag();

			resultTag.setTitle(fileTag.getTitle());
			resultTag.setArtist(fileTag.getArtist());
			resultTag.setAlbum(fileTag.getAlbum());
			resultTag.setTrack(fileTag.getTrack());
			resultTag.setGenre(fileTag.getGenre());
			resultTag.setGenreDescription(fileTag.getGenreDescription());

			return resultTag;
		} else {
			return new ID3v24Tag();
		}
	}

	/**
	 * Writes changes in the TagCloud into the files
	 * @throws IOException
	 * @throws InvalidDataException 
	 * @throws UnsupportedTagException 
	 * @throws NotSupportedException 
	 * @throws TagException
	 */
	public void writeTags(File file) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		String path = file.getCanonicalPath();
		Log.d(Constants.MAIN_TAG, "Writing file " + path);
		
		long time = System.currentTimeMillis();
		Mp3File mp3 = new Mp3File(file.getAbsolutePath(), false);
		long deltaTime = System.currentTimeMillis() - time;
		Log.d(Constants.MAIN_TAG, "Opened original mp3-File (" + deltaTime + "ms)");
		
		ID3v2 tag = mSelectionController.getSelection().getTagCloud().getTagMap().get(file);
		mp3.setId3v2Tag(tag);
		if (mp3.hasId3v1Tag()) {
			mp3.removeId3v1Tag();
		}
		
		time = System.currentTimeMillis();
		mp3.save(path + "_temp.mp3");
		deltaTime = System.currentTimeMillis() - time;
		Log.d(Constants.MAIN_TAG,  "Wrote mp3-File (" + deltaTime + "ms)");
		
		time = System.currentTimeMillis();
		boolean deleted = file.delete();
		deltaTime = System.currentTimeMillis() - time;
		Log.d(Constants.MAIN_TAG, "Deleted original file (" + deltaTime + "ms)");
		
		if (deleted) {
			time = System.currentTimeMillis();
			new File(path + "_temp.mp3").renameTo(new File(path));
			deltaTime = System.currentTimeMillis() - time;
			Log.d(Constants.MAIN_TAG, "Renamed saved file(" + deltaTime + "ms)");
		} else {
			new File(path + "_temp.mp3").delete();
		}
	}

	public void writeAlbumCover(Image cover) {

	}
}
