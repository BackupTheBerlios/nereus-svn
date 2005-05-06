/*
 * Created on 04.08.2003
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
 * Die Exception wird geworfen, wenn die Umwelt eines Szenarios bereits voll
 * besetzt ist und ein Agent nicht mehr hinzugef�gt werden kann.
 * 
 * @author Daniel Friedrich
 */
public class FullEnviromentException extends Exception {
	
	/**
	 * Konstruktor. 
	 */
	public FullEnviromentException() {
		super("EnviromentIsFullException: Die Umwelt des Szenarios ist voll " 
			+ " der Agent kann nicht mehr hinzugef�gt werden.");
	}

	/**
	 * @param message
	 */
	public FullEnviromentException(String message) {
		super("EnviromentIsFullException: Die Umwelt des Szenarios ist voll " 
			+ " der Agent "
			+ message 
			+ " kann nicht mehr hinzugef�gt werden.");
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FullEnviromentException(String message, Throwable cause) {
		super("EnviromentIsFullException: Die Umwelt des Szenarios ist voll " 
			+ " der Agent "
			+ message 
			+ " kann nicht mehr hinzugef�gt werden.",
			cause);
	}

	/**
	 * @param cause
	 */
	public FullEnviromentException(Throwable cause) {
		super("EnviromentIsFullException: Die Umwelt des Szenarios ist voll " 
			+ " der Agent kann nicht mehr hinzugef�gt werden.",
			cause);
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "EnviromentIsFullException: Die Umwelt des Szenarios ist voll " 
			+ " der Agent kann nicht mehr hinzugef�gt werden.";
	}
}
