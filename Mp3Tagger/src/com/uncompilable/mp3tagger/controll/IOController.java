package com.uncompilable.mp3tagger.controll;

import java.io.File;
import java.io.IOException;

import org.farng.mp3.AbstractMP3Tag;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagConstant;
import org.farng.mp3.TagException;
import org.farng.mp3.TagOptionSingleton;

import android.media.Image;

import com.uncompilable.mp3tagger.error.NoTagAssociatedWithFileException;
import com.uncompilable.mp3tagger.model.TagCloud;

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
		TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_OVERWRITE);
	}
	
	/**
	 * Reads a file and returns the ID3Tag of this file
	 * @param file - The file to be read
	 * @return - The tag of this file
	 * @throws IOException
	 * @throws TagException
	 * @throws NoTagAssociatedWithFileException
	 */
	public AbstractMP3Tag readFile(File file) throws IOException, TagException, NoTagAssociatedWithFileException {
		MP3File mp3 = new MP3File(file);
		
		if (mp3.hasID3v2Tag()) {
			return mp3.getID3v2Tag();
		} else if (mp3.hasID3v1Tag()) {
			return mp3.getID3v1Tag();
		} else {
			throw new NoTagAssociatedWithFileException();
		}
	}
	
	/**
	 * Writes changes in the TagCloud into the files
	 * @throws IOException
	 * @throws TagException
	 */
	public void writeTags() throws IOException, TagException {
		TagCloud tagCloud = mSelectionController.getSelection().getTagCloud();
		tagCloud.writeLocalChanges();
		
		for (File file : tagCloud.getTagMap().keySet()) {
			MP3File mp3File = new MP3File(file);
			mp3File.setID3v2Tag(tagCloud.getTagMap().get(file));
		}
	}
	
	public void writeAlbumCover(Image cover) {
		
	}
}
