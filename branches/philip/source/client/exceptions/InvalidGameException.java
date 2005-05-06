/*
 * Created on 04.08.2003
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
 * Die Exception dient zum Melden, dass ein Spiel nicht existiert.
 * 
 * @author Daniel Friedrich
 */
public class InvalidGameException extends Exception {

	/**
	 * Konstruktor.
	 */
	public InvalidGameException() {
		super("Das Spiel existiert nicht.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidGameException(String message) {
		super("Das Spiel " + message + " existiert nicht");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 * @param cause
	 */
	public InvalidGameException(String message, Throwable cause) {
		super("Das Spiel " + message + " existiert nicht", cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidGameException(Throwable cause) {
		super("Das Spiel existiert nicht",cause);
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "Das Spiel existiert nicht";
	}
}
