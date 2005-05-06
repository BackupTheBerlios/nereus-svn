/*
 * Created on 16.06.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut f�r Intelligente Systeme, Universit�t Stuttgart (2003)
 */
package exceptions;

/**
 * Die Exception wird ausgel�st, wenn ein Agent nicht �ber gen�gend Energie zum 
 * Ausf�hren einer Aktion verf�gt.  
 * 
 * @author Daniel Friedrich
 */
public class NotEnoughEnergyException extends Exception {

	/**
	 * Konstruktor. 
	 */
	public NotEnoughEnergyException() {
		super(
			"Der Agent verf�gt nicht �ber gen�gend Energie zum Ausf�hren "
			+ "der Aktion.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public NotEnoughEnergyException(String message) {
		super("Der Agent " 
			+ message 
			+ " verf�gt nicht �ber gen�gend Energie zum Ausf�hren der Aktion.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 * @param cause
	 */
	public NotEnoughEnergyException(String message, Throwable cause) {
		super("Der Agent " 
			+ message 
			+ " verf�gt nicht �ber gen�gend Energie zum Ausf�hren der Aktion.",
		cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public NotEnoughEnergyException(Throwable cause) {
		super("Der Agent "
			+ " verf�gt nicht �ber gen�gend Energie zum Ausf�hren der Aktion",
			cause);
	}
}
