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
 * Die Exception wird ueberall dort ausgeloest, wo ein Verweis auf den aktuellen 
 * Agenten uebergeben werden soll. Und zwar dann, wenn der angegebene 
 * Objektverweis mit keinem beim Server registrierten Agenten uebereinstimmt.
 * 
 * @author Daniel Friedrich
 */
public class InvalidAgentException extends Exception {

	/**
	 * Konstruktor.
	 */
	public InvalidAgentException() {
		super("Agent mit der Id "
			+ " existiert nicht oder die erwartete Id stimmt nicht mit der "
			+ "erwarteten Id überein.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidAgentException(String message) {
		super("Agent mit der Id " 
			+ message 
			+ " existiert nicht oder die erwartete Id stimmt nicht mit der "
			+ "erwarteten Id überein.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 * @param cause
	 */
	public InvalidAgentException(String message, Throwable cause) {
		super("Agent mit der Id " 
			+ message 
			+ " existiert nicht oder die erwartete Id stimmt nicht mit der "
			+ "erwarteten Id überein.",		cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidAgentException(Throwable cause) {
		super("Agent mit der Id "
			+ " existiert nicht oder die erwartete Id stimmt nicht mit der "
			+ "erwarteten Id überein.",
			cause);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "Agent mit der Id "
			+ " existiert nicht oder die erwartete Id stimmt nicht mit der "
			+ "erwarteten Id überein.";
	}	
}
