/*
 * Created on 05.08.2003
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
 * Superklasse für alle Sprechakte die Verwaltungstätigkeiten beschreiben. 
 * Beispiele hierfür sind das Registrieren und Abmelden bei Komponenten.
 * 
 * Abstrakte Hierarchie-Sprechakte dürfen nicht als Klasse in der Liste der
 * erlaubte Sprechakte eines Szenarios aufgenommen werden.
 * 
 * @author Daniel Friedrich
 */
public abstract class FacilitationSpeachact extends Speachact {

	/**
	 * Flag, da die Nachricht sowohl eine einfache Nachricht sein kann, wie 
	 * auch eine Antwort auf eine andere Facilitation Nachrich, z.B bei einem
	 * Register.
	 */
	private boolean m_isAnswer = false;

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public FacilitationSpeachact(Hashtable parameters) {
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
		return m_isAnswer;
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#calculateCosts()
	 */
	public final double calculateCosts() {
		return this.getFacilitationCosts();
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#calculateAnswerCosts()
	 */
	public double calculateAnswerCosts() {
		if(this.haveExtraAnswerCosts()) {
			return this.calculateCosts();  
		}
		return 0.0;
	}

	/**
	 * Eine Facilitation-Nachricht erlaubt kein Setzen eines Inhaltes. 
	 */
	public void setContent(Object content) throws InvalidElementException {
		if(content != null) {
			throw new InvalidElementException();
		}
		m_content = null;
	}
	
	/**
	 * Setzt ob die Nachricht eine Antwort darstellt.
	 */
	public void setIsAnswer() {
		m_isAnswer = true;
	}
}
