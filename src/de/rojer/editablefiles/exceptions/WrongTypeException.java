package de.rojer.editablefiles.exceptions;

/**
 * This exception occurs, if the program tries to read a data as the wrong type
 * 
 * @author Rojer
 * @version 17.3.2019 15:04 UTC+1 
 */

public class WrongTypeException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public WrongTypeException(String type, String wanted) {
		message = "Tried to convert from " + type + " to " + wanted + "!";
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}