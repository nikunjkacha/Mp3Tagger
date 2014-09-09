package com.uncompilable.mp3tagger.controll;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import android.media.Image;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.uncompilable.mp3tagger.error.NoTagAssociatedWithFileException;


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
		Mp3File mp3 = new Mp3File(file.getAbsolutePath());
		return mp3.getId3v2Tag();
	}

	/**
	 * Writes changes in the TagCloud into the files
	 * @throws IOException
	 * @throws InvalidDataException 
	 * @throws UnsupportedTagException 
	 * @throws NotSupportedException 
	 * @throws TagException
	 */
	public void writeTags() throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		mSelectionController.getSelection().getTagCloud().writeLocalChanges();
		
		for (File file : mSelectionController.getSelection().getFileSet()) {
			String path = file.getCanonicalPath();
			Mp3File mp3 = new Mp3File(file.getAbsolutePath());
			ID3v2 tag = mSelectionController.getSelection().getTagCloud().getTagMap().get(file);
			mp3.setId3v2Tag(tag);
			mp3.save(path + "_temp.mp3");
			if (file.delete()) {
				new File(path + "_temp.mp3").renameTo(new File(path));
			} else {
				new File(path + "_temp.mp3").delete();
			}
		}
	}

	public void writeAlbumCover(Image cover) {

	}
}
