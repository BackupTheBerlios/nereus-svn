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
 * Superklasse in der Hierarchie der Invalid..Exception. 
 * 
 * @author Daniel Friedrich
 */
public class InvalidException extends Exception {

	/**
	 * Konstruktor.
	 */
	public InvalidException() {
		super();
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param messageText
	 */
	public InvalidException(String messageText) {
		super(messageText);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param messageText
	 * @param cause
	 */
	public InvalidException(String messageText, Throwable cause) {
		super(messageText, cause);
	}

}
