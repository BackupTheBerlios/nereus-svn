/*
 * Created on 29.09.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package speachacts;

import java.util.Hashtable;

import exceptions.InvalidElementException;

/**
 * Sprechakt soll anfragendem Agenten mitteilen, dass der Agent die Anfrage gerne
 * beantwortetn würde, ihm aber im Moment die Sachkenntnis dazu fehlt.
 * 
 * @author Daniel Friedrich
 */
public final class AskAnotherTimeSpeachact extends AnswerSpeachact {

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public AskAnotherTimeSpeachact(Hashtable parameters) {
		super(parameters);
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#setContent(java.lang.Object)
	 */
	public void setContent(Object content) throws InvalidElementException {
		if(content != null) {
			throw new InvalidElementException();	
		}
	}
}
