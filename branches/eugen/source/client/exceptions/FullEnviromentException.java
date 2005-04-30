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
 * Die Exception wird geworfen, wenn die Umwelt eines Szenarios bereits voll
 * besetzt ist und ein Agent nicht mehr hinzugefügt werden kann.
 * 
 * @author Daniel Friedrich
 */
public class FullEnviromentException extends Exception {
	
	/**
	 * Konstruktor. 
	 */
	public FullEnviromentException() {
		super("EnviromentIsFullException: Die Umwelt des Szenarios ist voll " 
			+ " der Agent kann nicht mehr hinzugefügt werden.");
	}

	/**
	 * @param message
	 */
	public FullEnviromentException(String message) {
		super("EnviromentIsFullException: Die Umwelt des Szenarios ist voll " 
			+ " der Agent "
			+ message 
			+ " kann nicht mehr hinzugefügt werden.");
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FullEnviromentException(String message, Throwable cause) {
		super("EnviromentIsFullException: Die Umwelt des Szenarios ist voll " 
			+ " der Agent "
			+ message 
			+ " kann nicht mehr hinzugefügt werden.",
			cause);
	}

	/**
	 * @param cause
	 */
	public FullEnviromentException(Throwable cause) {
		super("EnviromentIsFullException: Die Umwelt des Szenarios ist voll " 
			+ " der Agent kann nicht mehr hinzugefügt werden.",
			cause);
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "EnviromentIsFullException: Die Umwelt des Szenarios ist voll " 
			+ " der Agent kann nicht mehr hinzugefügt werden.";
	}
}
