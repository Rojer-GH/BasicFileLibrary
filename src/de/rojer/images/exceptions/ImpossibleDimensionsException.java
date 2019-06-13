/**
 * 
 */
package de.rojer.images.exceptions;

/**
 * This exception occurs, if certain dimensions for an image are wanted that are
 * not possible
 * 
 * @author Rojer
 * @version 17.3.2019
 */
public class ImpossibleDimensionsException extends Exception {

	private static final long serialVersionUID = 1L;

	protected String message;

	public ImpossibleDimensionsException(String wanted, String possible) {
		message = "The wanted value of " + wanted + " is not possible in this context (possible: " + possible + ").";
	}

	@Override
	public String getMessage() {
		return message;
	}

}
