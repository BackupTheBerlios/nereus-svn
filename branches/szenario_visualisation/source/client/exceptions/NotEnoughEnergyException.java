/*
 * Created on 16.06.2003
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
 * Die Exception wird ausgelöst, wenn ein Agent nicht über genügend Energie zum 
 * Ausführen einer Aktion verfügt.  
 * 
 * @author Daniel Friedrich
 */
public class NotEnoughEnergyException extends Exception {

	/**
	 * Konstruktor. 
	 */
	public NotEnoughEnergyException() {
		super(
			"Der Agent verfügt nicht über genügend Energie zum Ausführen "
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
			+ " verfügt nicht über genügend Energie zum Ausführen der Aktion.");
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
			+ " verfügt nicht über genügend Energie zum Ausführen der Aktion.",
		cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public NotEnoughEnergyException(Throwable cause) {
		super("Der Agent "
			+ " verfügt nicht über genügend Energie zum Ausführen der Aktion",
			cause);
	}
}
