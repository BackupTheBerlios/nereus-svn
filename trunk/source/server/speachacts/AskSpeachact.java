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
 * Die Klasse repräsentiert die Superklasse aller Frage-Sprechakte.
 *
 * Abstrakte Hierarchie-Sprechakte dürfen nicht in die Liste der erlaubten 
 * Sprechakte beim Szenario aufgenommen werde. Ansonsten wird das Sicherheits-
 * konzept ausgehebelt.
 * 
 * @author Daniel Friedrich
 */
public abstract class AskSpeachact extends Speachact {

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public AskSpeachact(Hashtable parameters) {
		super(parameters);
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#answerRequired()
	 */
	public boolean answerRequired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#isAnswer()
	 */
	public boolean isAnswer() {		
		return false;
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#calculateCosts()
	 */
	public final double calculateCosts() {
		return 
			this.getFactor() * this.getContentSize() * this.getCommCosts(); 
	}
	
	/* (non-Javadoc)
	 * @see speachacts.Speachact#calculateAnswerCosts()
	 */
	public final double calculateAnswerCosts() {
		return 0.0;
	}

	/**
	 * Setzt den Inhalt der Nachricht.
	 * 
	 * In einer AskNachricht sind alle Objekte als Inhalt erlaut. Bitte beachten 
	 * Sie aber dass alle Objekte ausser String, Instance, Instances, Boolean,
	 * Id, null und Vectoren mit diesen Objekten die Größe 100 zugerechnet
	 * bekommen, was ihren Transport entsprechend teuer macht. 
	 */
	public final void setContent(Object content) 
		throws InvalidElementException {
		m_content = content;
	}
}
