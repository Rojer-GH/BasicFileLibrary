package de.rojer.editablefiles;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * This class can read and load all characters from a text file
 * 
 * @author Rojer
 * @version 7.04.2019
 */

public class StringFile extends EditableFile {

	// Attributes

	/**
	 * The text of the file
	 */
	protected String source = "";

	// Constructors

	/**
	 * Call this method to find a file (when not found, create) and copy all its
	 * text into a usable variable
	 * 
	 * @param path     the path to the folder
	 * @param fileName the name of the file
	 */
	public StringFile(String path, String fileName) {
		super(path, fileName);
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			while (it.hasNext()) {
				String line = it.next();
				source += line + "\n";
			}
			source += "\0";
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Call this method to find a file (when not found, create) and copy all its
	 * text into a usable variable
	 * 
	 * @param completePath the complete path to the file (with extension)
	 */
	public StringFile(String completePath) {
		super(completePath);
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			while (it.hasNext()) {
				String line = it.next();
				source += line + "\n";
			}
			source += "\0";
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// "Destructors"

	public void destroy(StringFile file) {
		this.file.delete();
		this.encoding = null;
		this.path = null;
		this.fileName = null;
		this.completePath = null;
		this.source = null;
		file = null;
	}

	// EditableFile-inherited methods

	/**
	 * This method will ALWAYS return true
	 */
	@Override
	public boolean getBoolean(String path) {
		return true;
	}

	/**
	 * This method will ALWAYS return 0.0
	 */
	@Override
	public double getDouble(String path) {
		return 0.0;
	}

	/**
	 * This method will ALWAYS return 0.0F
	 */
	@Override
	public float getFloat(String path) {
		return 0.0F;
	}

	/**
	 * This method will ALWAYS return 0
	 */
	@Override
	public int getInt(String path) {
		return 0;
	}

	/**
	 * This method will ALWAYS return null
	 */
	@Override
	public String getString(String path) {
		return null;
	}

	/**
	 * This method won't do anything
	 */
	@Override
	public void writeBoolean(String path, boolean value) {

	}

	/**
	 * This method won't do anything
	 */
	@Override
	public void writeDouble(String path, double value) {

	}

	/**
	 * This method won't do anything
	 */
	@Override
	public void writeFloat(String path, float value) {

	}

	/**
	 * This method won't do anything
	 */
	@Override
	public void writeInt(String path, int value) {

	}

	/**
	 * This method won't do anything
	 */
	@Override
	public void writeString(String path, String value) {

	}

	// Getters

	/**
	 * @return the text
	 */
	public String getSource() {
		return source;
	}

}
