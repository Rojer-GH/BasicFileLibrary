package de.rojer.editablefiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.rojer.editablefiles.exceptions.DuplicateEncodingException;
import de.rojer.editablefiles.exceptions.WrongTypeException;

/**
 * This class can be used to read from and write to text files. It uses standard ASCII encoding (total of 127 characters).
 * You can write/read: Integers, Booleans, Floats, Doubles and normal Strings.
 * You can change the encoding of every file to prevent "normal" people from reading your files.
 * 
 * 	@author Rojer
 *  @version 17.3.2019 15:04 UTC+1 
 * 
 */

public class EditableFile {
	
	/**
	 * The amount of total available characters
	 */
	public static final int AMOUNT_OF_POSSIBLE_CHARACTERS = 127;
	
	//The text file
	protected File file;
	
	//The path to the file
	protected String path;
	//The name of the file (with extension)
	protected String fileName;
	
	//The list of encoded characters
	protected HashMap<Character, Character> encoding;
		
	//List of types
	protected static final String TYPE_INT = "INT", 
								  TYPE_CHAR = "CHAR", 
								  TYPE_BOOLEAN = "BOOLEAN", 
								  TYPE_FLOAT_DOUBLE = "FLOAT/DOUBLE", 
								  TYPE_STRING = "STRING";
	
	/**
	 * Create a file
	 * @param path the path to the file
	 * @param fileName the name of the file (with extension!)
	 */
	public EditableFile(String path, String fileName) {
		this.path = path;
		this.fileName = fileName;
		this.encoding = new HashMap<Character, Character>();
		for(int i = 0; i < AMOUNT_OF_POSSIBLE_CHARACTERS; i++) {
			addEncodingChar((char)i, (char)i);
		}
		loadFile();
	}
	
	/**
	 * Create a file with an offset encoding
	 * @param path the path to the file
	 * @param fileName the name of the file (with encoding!)
	 * @param offsetEncoding the amount of characters to move (positive: to the right, negative: to the left)
	 */
	public EditableFile(String path, String fileName, int offsetEncoding) {
		this.path = path;
		this.fileName = fileName;
		this.encoding = new HashMap<Character, Character>();
		for(int i = 0; i < AMOUNT_OF_POSSIBLE_CHARACTERS; i++) {
			addEncodingChar((char)i, (char)i);
		}
		moveEncodingList(offsetEncoding);
		loadFile();
	}
	
	/**
	 * Create a file with a specific encoding list
	 * @param path the path to the file
	 * @param fileName the name of the file (with extension!)
	 * @param encoding the custom list of encoded characters (only the characters that should be encoded differently)
	 */
	public EditableFile(String path, String fileName, HashMap<Character, Character> encoding) {
		this.path = path;
		this.fileName = fileName;
		this.encoding = new HashMap<Character, Character>();
		for(int i = 0; i < AMOUNT_OF_POSSIBLE_CHARACTERS; i++) {
			addEncodingChar((char)i, (char)i);
		}
		Iterator<Character> it = encoding.keySet().iterator();
		while(it.hasNext()) {
			char key = it.next();
			if(encoding.get(key) != key) {
				addEncodingChar((char)key, (char)encoding.get(key));
			}
		}
		loadFile();
	}
	
	/**
	 * Create a file with a specific encoding list
	 * @param path the path to the file
	 * @param fileName the name of the file (with extension!)
	 * @param encoding the custom list of encoded characters (only the characters that should be encoded differently)
	 * @param offsetEncoding the amount of characters to move (positive: to the right, negative: to the left)
	 */
	public EditableFile(String path, String fileName, HashMap<Character, Character> encoding, int offsetEncoding) {
		this.path = path;
		this.fileName = fileName;
		this.encoding = new HashMap<Character, Character>();
		for(int i = 0; i < AMOUNT_OF_POSSIBLE_CHARACTERS; i++) {
			addEncodingChar((char)i, (char)i);
		}
		Iterator<Character> it = encoding.keySet().iterator();
		while(it.hasNext()) {
			char key = it.next();
			if(encoding.get(key) != key) {
				addEncodingChar((char)key, (char)encoding.get(key));
			}
		}
		moveEncodingList(offsetEncoding);
		loadFile();
	}
	
	//Loads in the file for use
	private void loadFile() {
		File folder = new File(path);
		file = new File(path.concat("/" + fileName));
		if(!file.exists()) {
			folder.mkdirs();
			try {
				file.createNewFile();
			}catch(IOException e) {
				System.out.println("Failed to create and load file!");
			}
		}
		try {
			testForDuplicates();
		} catch (DuplicateEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes this file
	 * @param instance : the instance of this object
	 */
	public void deleteFile(EditableFile instance) {
		file.delete();
		instance = null;
	}
	
	/**
	 * Gets an integer with a certain path
	 * @param path the path to the integer value
	 * @return the integer value (only integer values!)
	 * @throws WrongTypeException when the value at that path is not an integer
	 */
	public int getTrueInt(String path) throws WrongTypeException{
		int result = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			boolean found = false;
			while(it.hasNext()) {
				String text = it.next();
				String oldPath = encode(decode(text).split(":")[0] + ": ");
				if(path.compareTo(oldPath) <= -1) {
					break;
				}else if(path.equals(oldPath)) {
					found = true;
					String number = decode(text.substring(oldPath.length()));
					if(!isInt(number)) {
						br.close();
						throw new WrongTypeException(getType(number), TYPE_INT);
					}
					result = Integer.parseInt(number);
				}
			}
			if(!found) {
				System.out.println("Couldn't find that integer!");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Gets an integer with a certain path
	 * @param path the path to the integer value
	 * @return the integer value
	 * @throws WrongTypeException when the value at that path is not an integer, float or double
	 */
	public int getInt(String path) throws WrongTypeException{
		float result = 0.0F;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			boolean found = false;
			while(it.hasNext()) {
				String text = it.next();
				String oldPath = encode(decode(text).split(":")[0] + ": ");
				if(path.compareTo(oldPath) <= -1) {
					break;
				}else if(path.equals(oldPath)) {
					found = true;
					String number = decode(text.substring(oldPath.length()));
					if(!isFloat(number)) {
						br.close();
						throw new WrongTypeException(getType(number), TYPE_INT);
					}
					result = Float.parseFloat(number);
				}
			}
			if(!found) {
				System.out.println("Couldn't find that integer!");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (int)result;
	}
	
	/**
	 * Gets a character with a certain path
	 * @param path the path to the character
	 * @return the character value
	 * @throws WrongTypeException when the value at that path is not an character
	 */
	public char getChar(String path) throws WrongTypeException{
		char result = '\0';
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			boolean found = false;
			while(it.hasNext()) {
				String text = it.next();
				String oldPath = encode(decode(text).split(":")[0] + ": ");
				if(path.compareTo(oldPath) <= -1) {
					break;
				}else if(path.equals(oldPath)) {
					found = true;
					String number = decode(text.substring(oldPath.length()));
					if(!isChar(number)) {
						br.close();
						throw new WrongTypeException(getType(number), TYPE_CHAR);
					}
					result = number.charAt(0);
				}
			}
			if(!found) {
				System.out.println("Couldn't find that char!");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Gets a boolean with a certain path
	 * @param path the path to the boolean value
	 * @return the boolean value
	 * @throws WrongTypeException when the value at that path is not a boolean
	 */
	public boolean getBoolean(String path) throws WrongTypeException{
		boolean result = false;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			boolean found = false;
			while(it.hasNext()) {
				String text = it.next();
				String oldPath = encode(decode(text).split(":")[0] + ": ");
				if(path.compareTo(oldPath) <= -1) {
					break;
				}else if(path.equals(oldPath)) {
					found = true;
					String number = decode(text.substring(oldPath.length()));
					if(!isBoolean(number)) {
						br.close();
						throw new WrongTypeException(getType(number), TYPE_BOOLEAN);
					}
					result = Boolean.parseBoolean(number);
				}
			}
			if(!found) {
				System.out.println("Couldn't find that boolean!");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Gets a float with a certain path
	 * @param path the path to the float value
	 * @return the float value
	 * @throws WrongTypeException when the value at that path is not an integer, double or float
	 */
	public float getFloat(String path) throws WrongTypeException{
		float result = 0.0F;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			boolean found = false;
			while(it.hasNext()) {
				String text = it.next();
				String oldPath = encode(decode(text).split(":")[0] + ": ");
				if(path.compareTo(oldPath) <= -1) {
					break;
				}else if(path.equals(oldPath)) {
					found = true;
					String number = decode(text.substring(oldPath.length()));
					if(!isFloat(number)) {
						br.close();
						throw new WrongTypeException(getType(number), TYPE_FLOAT_DOUBLE);
					}
					result = Float.parseFloat(number);
				}
			}
			if(!found) {
				System.out.println("Couldn't find that float!");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Gets a double with a certain path
	 * @param path the path to the double value
	 * @return the double value
	 * @throws WrongTypeException when the value at that path is not an int, double or float
	 */
	public double getDouble(String path) throws WrongTypeException {
		double result = 0.0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			boolean found = false;
			while(it.hasNext()) {
				String text = it.next();
				String oldPath = encode(decode(text).split(":")[0] + ": ");
				if(path.compareTo(oldPath) <= -1) {
					break;
				}else if(path.equals(oldPath)) {
					found = true;
					String number = decode(text.substring(oldPath.length()));
					if(!isFloat(number)) {
						br.close();
						throw new WrongTypeException(getType(number), TYPE_FLOAT_DOUBLE);
					}
					result = Double.parseDouble(number);
				}
			}
			if(!found) {
				System.out.println("Couldn't find that double!");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Gets a string with a certain path
	 * @param path the path to the string
	 * @return the string
	 */
	public String getString(String path) {
		String result = "";
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			boolean found = false;
			while(it.hasNext()) {
				String text = it.next();
				String oldPath = encode(decode(text).split(":")[0] + ": ");
				if(path.compareTo(oldPath) <= -1) {
					break;
				}else if(path.equals(oldPath)) {
					found = true;
					result = decode(text.substring(oldPath.length()));
				}
			}
			if(!found) {
				System.out.println("Couldnt find that string!");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
		
	/**
	 * Writes an integer to the file
	 * @param path the path for the value
	 * @param value the integer value
	 */
	public void writeInt(String path, int value) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			String valueString = encode(value + "");
			ArrayList<String> lines = new ArrayList<String>();
			boolean isWritten = false;
			while(it.hasNext()) {
				String oldText = it.next();
				String oldPath = encode(decode(oldText).split(":")[0] + ": ");
				if(path.equals(oldPath) && !isWritten) {
					lines.add(path + valueString);
					isWritten = true;
				}else if(path.compareTo(oldPath) <= -1 && !isWritten) {
					lines.add(path + valueString);
					lines.add(oldText);
					isWritten = true;
				}else{
					lines.add(oldText);
				}
				
			}
			if(!isWritten) {
				lines.add(path + valueString);
			}
			br.close();
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			for(String s : lines) {
				pw.println(s);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a character to the file
	 * @param path the path for the value
	 * @param value the character
	 */
	public void writeChar(String path, char value) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			/*
			 * br.lines().sorted(new Comparator<String>() {
				@Override
				public int compare(String str1, String str2) {
					int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
					return (res != 0) ? res : str1.compareTo(str2);
				}
			});
			 */
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			String valueString = encode(value + "");
			ArrayList<String> lines = new ArrayList<String>();
			boolean isWritten = false;
			while(it.hasNext()) {
				String oldText = it.next();
				String oldPath = encode(decode(oldText).split(":")[0] + ": ");
				
				if(path.equals(oldPath) && !isWritten) {
					lines.add(path + valueString);
					isWritten = true;
				}else if(path.compareTo(oldPath) <= -1 && !isWritten) {
					lines.add(path + valueString);
					lines.add(oldText);
					isWritten = true;
				}else{
					lines.add(oldText);
				}
				
			}
			if(!isWritten) {
				lines.add(path + valueString);
			}
			br.close();
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			for(String s : lines) {
				pw.println(s);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a boolean to the file
	 * @param path the path for the value
	 * @param value the boolean value
	 */
	public void writeBoolean(String path, boolean value) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			/*
			 * br.lines().sorted(new Comparator<String>() {
				@Override
				public int compare(String str1, String str2) {
					int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
					return (res != 0) ? res : str1.compareTo(str2);
				}
			});
			 */
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			String valueString = encode(value + "");
			ArrayList<String> lines = new ArrayList<String>();
			boolean isWritten = false;
			while(it.hasNext()) {
				String oldText = it.next();
				String oldPath = encode(decode(oldText).split(":")[0] + ": ");
				
				if(path.equals(oldPath) && !isWritten) {
					lines.add(path + valueString);
					isWritten = true;
				}else if(path.compareTo(oldPath) <= -1 && !isWritten) {
					lines.add(path + valueString);
					lines.add(oldText);
					isWritten = true;
				}else{
					lines.add(oldText);
				}
				
			}
			if(!isWritten) {
				lines.add(path + valueString);
			}
			br.close();
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			for(String s : lines) {
				pw.println(s);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a float to the file
	 * @param path the path for the value
	 * @param value the float value
	 */
	public void writeFloat(String path, float value) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			/*
			 * br.lines().sorted(new Comparator<String>() {
				@Override
				public int compare(String str1, String str2) {
					int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
					return (res != 0) ? res : str1.compareTo(str2);
				}
			});
			 */
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			String valueString = encode(value + "");
			ArrayList<String> lines = new ArrayList<String>();
			boolean isWritten = false;
			while(it.hasNext()) {
				String oldText = it.next();
				String oldPath = encode(decode(oldText).split(":")[0] + ": ");
				
				if(path.equals(oldPath) && !isWritten) {
					lines.add(path + valueString);
					isWritten = true;
				}else if(path.compareTo(oldPath) <= -1 && !isWritten) {
					lines.add(path + valueString);
					lines.add(oldText);
					isWritten = true;
				}else{
					lines.add(oldText);
				}
				
			}
			if(!isWritten) {
				lines.add(path + valueString);
			}
			br.close();
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			for(String s : lines) {
				pw.println(s);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a double to the file
	 * @param path the path for the value
	 * @param value the double value
	 */
	public void writeDouble(String path, double value) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			/*
			 * br.lines().sorted(new Comparator<String>() {
				@Override
				public int compare(String str1, String str2) {
					int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
					return (res != 0) ? res : str1.compareTo(str2);
				}
			});
			 */
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			String valueString = encode(value + "");
			ArrayList<String> lines = new ArrayList<String>();
			boolean isWritten = false;
			while(it.hasNext()) {
				String oldText = it.next();
				String oldPath = encode(decode(oldText).split(":")[0] + ": ");
				
				if(path.equals(oldPath) && !isWritten) {
					lines.add(path + valueString);
					isWritten = true;
				}else if(path.compareTo(oldPath) <= -1 && !isWritten) {
					lines.add(path + valueString);
					lines.add(oldText);
					isWritten = true;
				}else{
					lines.add(oldText);
				}
				
			}
			if(!isWritten) {
				lines.add(path + valueString);
			}
			br.close();
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			for(String s : lines) {
				pw.println(s);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a string to the file
	 * @param path the path for the value
	 * @param value the string
	 */
	public void writeString(String path, String value) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			/*
			 * br.lines().sorted(new Comparator<String>() {
				@Override
				public int compare(String str1, String str2) {
					int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
					return (res != 0) ? res : str1.compareTo(str2);
				}
			});
			 */
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			String valueString = encode(value + "");
			ArrayList<String> lines = new ArrayList<String>();
			boolean isWritten = false;
			while(it.hasNext()) {
				String oldText = it.next();
				String oldPath = encode(decode(oldText).split(":")[0] + ": ");
				
				if(path.equals(oldPath) && !isWritten) {
					lines.add(path + valueString);
					isWritten = true;
				}else if(path.compareTo(oldPath) <= -1 && !isWritten) {
					lines.add(path + valueString);
					lines.add(oldText);
					isWritten = true;
				}else{
					lines.add(oldText);
				}
				
			}
			if(!isWritten) {
				lines.add(path + valueString);
			}
			br.close();
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			for(String s : lines) {
				pw.println(s);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete a line in the file
	 * @param path the path to the line
	 */
	public void deleteLine(String path) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			path = encode(path + ": ");
			ArrayList<String> lines = new ArrayList<String>();
			while(it.hasNext()) {
				String oldText = it.next();
				String oldPath = encode(decode(oldText).split(":")[0] + ": ");
				
				if(oldPath.compareTo(path) != 0) {
					lines.add(oldText);
				}
			}
			br.lines().skip(br.lines().count());
			br.close();
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			for(String s : lines) {
				pw.println(s);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Clears the whole content of the file
	 */
	public void clearFile() {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			for(int i = 0; i < br.lines().count(); i++) {
				pw.print("");
			}
			pw.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param shouldDecode indicates, whether the information should be decoded according to this files encodingList
	 * @return a string containing all information of the file
	 */
	public String returnContentsAsString(boolean shouldDecode) {
		String information = "";
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Iterator<String> it = br.lines().iterator();
			while(it.hasNext()) {
				String text = it.next();
				information += text + "\n";
			}
			information += "\0";
			br.lines().skip(br.lines().count());
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return information;
	}
	
	/**
	 * Adds a new encoding for a character
	 * @param target the character
	 * @param encoding the encoding
	 */
	private void addEncodingChar(char target, char encoding) {
		this.encoding.put(target, encoding);
	}
	
	/**
	 * Shifts the encoding list in a certain direction
	 * @param units the amount of characters to move (positive: to the right, negative: to the left)
	 */
	private void moveEncodingList(int units) {
		int offset = (units % AMOUNT_OF_POSSIBLE_CHARACTERS) * (int)Math.signum(units);
		HashMap<Character, Character> newMap = new HashMap<Character, Character>();
		for(int i = 0; i < AMOUNT_OF_POSSIBLE_CHARACTERS; i++) {
			newMap.put((char)i, encoding.get((char)i));
		}
		for(int i = 0; i < AMOUNT_OF_POSSIBLE_CHARACTERS; i++) {
			int newIndex = i - offset;
			if(newIndex >= AMOUNT_OF_POSSIBLE_CHARACTERS) {
				newIndex -= AMOUNT_OF_POSSIBLE_CHARACTERS;
			}else if(newIndex < 0) {
				newIndex += AMOUNT_OF_POSSIBLE_CHARACTERS;
			}
			encoding.put((char)i, newMap.get((char)newIndex));
		}
	}
	
	//Encodes a string according to the encoding list
	private String encode(String string) {
		char[] result = new char[string.length()];
		for(int i = 0; i < result.length; i++) {
			result[i] = encoding.get(string.charAt(i));
		}
		return new String(result);
	}
	
	//Decodes a string according to the encoding list
	private String decode(String string) {
		char[] result = new char[string.length()];
		for(int i = 0; i < result.length; i++) {
			Iterator<Character> it = encoding.keySet().iterator();
			boolean hasDecode = false;
			char decodeKey = 0;
			while(it.hasNext()) {
				char key = it.next();
				if(encoding.get(key) == string.charAt(i)) {
					hasDecode = true;
					decodeKey = key;
					break;
				}
			}
			if(hasDecode) {
				result[i] = decodeKey;
			}else{
				result[i] = string.charAt(i);
			}
		}
		return new String(result);
	}
	
	//Checks if a given string can be parsed into an int
	private boolean isInt(String test) {
		try {
			Integer.parseInt(test);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	//Checks if a given string can be parsed into a float/double
	private boolean isFloat(String test) {
		try {
			Float.parseFloat(test);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	//Checks if a given string can be parsed into a boolean
	private boolean isBoolean(String test) {
		if(test.equalsIgnoreCase("true") || test.equalsIgnoreCase("false")) {
			return true;
		}
		return false;
	}
	
	private boolean isChar(String test) {
		if(test.length() <= 1) {
			return true;
		}
		return false;
	}
	
	//Returns the type of a given string
	private String getType(String test) {
		String type = TYPE_STRING;
		if(isBoolean(test)) {		
			type = TYPE_BOOLEAN;
		}else if(isInt(test)) {
			type = TYPE_INT;
		}else if(isFloat(test)) {
			type = TYPE_FLOAT_DOUBLE;
		}else if(isChar(test)) {
			type = TYPE_CHAR;
		}
		return type;
	}
	
	//Checks if an encoding character has duplicates
	private boolean hasDuplicate(char target) {
		Iterator<Character> keys = encoding.keySet().iterator();
		int count = 0;
		while(keys.hasNext()) {
			if(encoding.get(keys.next()) == target) {
				count++;
			}
		}
		return count > 1;
	}
	
	//Searches the whole list for duplicates
	private void testForDuplicates() throws DuplicateEncodingException{
		for(int i = 0; i < AMOUNT_OF_POSSIBLE_CHARACTERS; i++) {
			if(hasDuplicate((char)i)) {
				throw new DuplicateEncodingException(encoding);
			}
		}
	}

}