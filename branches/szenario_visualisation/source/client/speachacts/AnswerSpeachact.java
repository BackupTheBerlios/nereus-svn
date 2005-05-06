/*
 * Created on 29.07.2003
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
 * Abstrakte Superklasse für Antwortsprechakte.
 * 
 * @author Daniel Friedrich
 */
public abstract class AnswerSpeachact extends Speachact {

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public AnswerSpeachact(Hashtable parameters) {
		super(parameters);
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#answerRequired()
	 */
	public boolean answerRequired() {
		return false;
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#isAnswer()
	 */
	public boolean isAnswer() {
		return true;
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#calculateCosts()
	 */
	public double calculateCosts() {
		return 0.0;
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#calculateAnswerCosts()
	 */
	public final double calculateAnswerCosts() {
		return
			this.getFactor() * this.getContentSize() * this.getAnswerCosts(); 
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#setContent(java.lang.Object)
	 */
	public abstract void setContent(Object content)
		throws InvalidElementException;
}
