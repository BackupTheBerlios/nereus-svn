/*
 * Created on 07.09.2003
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
 * Die Exception dient zum Melden, dass ein ActionKey nicht mit dem Key für 
 * einen Agenten übereinstimmt.
 * 
 * @author Daniel Friedrich
 */
public class InvalidActionKeyException extends Exception {

	/**
	 * Konstruktor.
	 */
	public InvalidActionKeyException() {
		super(
			"Der verwendete ActionKey stimmt nicht mit dem ActionKey "
		 	+ "des Agenten überein.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidActionKeyException(String message) {
		super(
			"Der verwendete ActionKey "
			+ message
			+ "stimmt nicht mit dem ActionKey des Agenten überein.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 * @param cause
	 */
	public InvalidActionKeyException(String message, Throwable cause) {
		super(
			"Der verwendete ActionKey stimmt nicht mit dem ActionKey "
			+ message
			+ "des Agenten überein.",
			cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidActionKeyException(Throwable cause) {
		super(
			"Der verwendete ActionKey stimmt nicht mit dem ActionKey "
			+ "des Agenten überein.", cause);
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "Der verwendete ActionKey stimmt nicht mit dem ActionKey "
			+ "des Agenten überein.";
	}
}
