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
 * Die Exception dient zum Melden, dass ein Element nicht existiert oder falsch 
 * verwendet wurde.
 * 
 * @author Daniel Friedrich
 */
public class InvalidElementException extends InvalidException {

	/**
	 * Konstruktor.
	 */
	public InvalidElementException() {
		super(
			"Das verwendete Element exisitiert nicht oder wurde falsch "
			+ "verwendet.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidElementException(String message) {
		super(
			"Das verwendete Element " 
			+ message 
			+ " exisitiert nicht oder wurde falsch verwendet.");
	}

	/**
	 * Konstruktor.
	 *  
	 * @param message
	 * @param cause
	 */
	public InvalidElementException(
		String message,
		Throwable cause) {
		super(
			"Das verwendete Element " 
			+ message + 
			" existiert nicht oder wurde falsch verwendet.", cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidElementException(Throwable cause) {
		super(
			"Das verwendete Element existiert nicht oder wurde falsch "
			+ "verwendet.",
			cause);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "Das verwendete Element exisitiert nicht oder wurde falsch "
			+ "verwendet.";
	}
}
