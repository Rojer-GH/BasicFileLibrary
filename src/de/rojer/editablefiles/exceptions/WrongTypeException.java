package de.rojer.editablefiles.exceptions;

/**
 * This exception occurs, if the program tries to read a data as the wrong type
 * 
 * @author Rojer
 * @version 17.3.2019
 */

public class WrongTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	protected String message;

	public WrongTypeException(String type, String wanted) {
		message = "Tried to convert from " + type + " to " + wanted + "!";
	}

	@Override
	public String getMessage() {
		return message;
	}

}