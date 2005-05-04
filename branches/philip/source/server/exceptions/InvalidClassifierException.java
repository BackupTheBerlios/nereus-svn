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
 * Die Exception wird geworfen, wenn ein verwendeter Classifier nicht der 
 * erwarteten Form entspricht. Z.B. wenn ein Id3 übergeben wurde, aber ein 
 * NaiveBayes benötigt wird.
 * 
 * @author Daniel Friedrich
 */
public class InvalidClassifierException extends Exception {

	/**
	 * Konstruktor.
	 */
	public InvalidClassifierException() {
		super(
			"Der verwendete Classifier entspricht nicht der erwarteten Form.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidClassifierException(String message) {
		super(
			"Der verwendete Classifier "
				+ message
				+ " entspricht nicht der erwarteten Form.");
	}

	/**
	 * Konstruktor.
	 *  
	 * @param message
	 * @param cause
	 */
	public InvalidClassifierException(String message, Throwable cause) {
		super(
			"Der verwendete Classifier "
				+ message
				+ " entspricht nicht der erwarteten Form.",
			cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidClassifierException(Throwable cause) {
		super(
			"Der verwendete Classifier entspricht nicht der erwartenden Form.",
			cause);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return 
			"Der verwendete Classifier entspricht nicht der erwarteten Form.";
	}
}
