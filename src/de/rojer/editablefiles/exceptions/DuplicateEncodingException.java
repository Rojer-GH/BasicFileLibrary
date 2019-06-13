package de.rojer.editablefiles.exceptions;

import java.util.HashMap;

/**
 * This exception occurs, if an encoding list contains a minimum of two of the
 * same characters
 * 
 * @author Rojer
 * @version 17.3.2019
 */

public class DuplicateEncodingException extends Exception {

	private static final long serialVersionUID = 1L;

	protected String message;

	public DuplicateEncodingException(HashMap<Character, Character> encodingList) {
		message = "Duplicate Encoding found!\n";
		for (int i = 0; i < encodingList.size(); i++) {
			message += "\'" + (char) i + "\' -> \'" + encodingList.get((char) i) + "\'\n";
		}
	}

	@Override
	public String getMessage() {
		return message;
	}

}