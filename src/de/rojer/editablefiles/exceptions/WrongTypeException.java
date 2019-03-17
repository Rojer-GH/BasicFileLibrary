package de.rojer.editablefiles.exceptions;

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