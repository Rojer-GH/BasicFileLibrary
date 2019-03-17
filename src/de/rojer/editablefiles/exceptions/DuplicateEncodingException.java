package de.rojer.editablefiles.exceptions;

import java.util.HashMap;

public class DuplicateEncodingException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public DuplicateEncodingException(HashMap<Character, Character> encodingList) {
		message = "Duplicate Encoding found!\n";
		for(int i = 0; i < encodingList.size(); i++) {
			message += "\'" + (char)i + "\' -> \'" + encodingList.get((char)i) + "\'\n";
		}
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}