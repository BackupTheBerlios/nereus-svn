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
 * Wird ausgelöst, wenn entweder die angegebene Nachricht nicht existent ist,
 * oder aus irgendeinem Szenariospezifischen Grund an dieser Stelle verwendet
 * werden darf. Welcher der beiden Fälle eingetreten ist, wird aus
 * Sicherheitsgründen nicht bekanntgegeben.
 * 
 * @author Daniel Friedrich
 */
public class InvalidMessageException extends Exception {

	/**
	 * Konstruktor.
	 */
	public InvalidMessageException() {
		super("Nachricht mit der Id " 
		+ " existiert oder darf an dieser Stelle nicht verwendet werden.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidMessageException(String message) {
		super("Nachricht mit der Id " 
			+ message 
			+ " existiert oder darf an dieser Stelle nicht verwendet werden.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 * @param cause
	 */
	public InvalidMessageException(String message, Throwable cause) {
		super("Nachricht mit der Id " 
			+ message 
			+ " existiert oder darf an dieser Stelle nicht verwendet werden.",
		cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidMessageException(Throwable cause) {
		super("Nachricht mit der Id "
			+ " existiert oder darf an dieser Stelle nicht verwendet werden",
			cause);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "Nachricht mit der Id " 
			+ " existiert oder darf an dieser Stelle nicht verwendet werden.";
	}	
}
