/*
 * Created on 23.11.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package exceptions;

/**
 * Die Exception dient zum Melden, dass ein Sprechakt nicht verwendet werden 
 * darf.
 * 
 * @author Daniel Friedrich
 */
public class InvalidSpeachactException extends InvalidException {
	/**
	 * Konstruktor.
	 */
	public InvalidSpeachactException() {
		super("Der verwendete Sprechakt ist nicht zur Verwendung erlaubt.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidSpeachactException(String message) {
		super(
			"Der verwendete Sprechakt " 
			+ message 
			+ " ist nicht zur Verwendung erlaubt.");
	}

	/**
	 * Konstruktor.
	 *  
	 * @param message
	 * @param cause
	 */
	public InvalidSpeachactException(
		String message,
		Throwable cause) {
		super(
			"Der verwendete Sprechakt " 
			+ message + 
			" ist nicht zur Verwendung erlaubt.", cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidSpeachactException(Throwable cause) {
		super(
			"Der verwendete Sprechakt ist nicht zur Verwendung erlaubt.",
			cause);
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "Der verwendete Sprechakt ist nicht zur Verwendung erlaubt.";
	}
}

