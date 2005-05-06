/*
 * Created on 29.07.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut f�r Intelligente Systeme, Universit�t Stuttgart (2003)
 */
package speachacts;

import java.util.Hashtable;

import exceptions.InvalidElementException;

/**
 * Sprechakt um Anfragen abzulehnen. Der Sprechakt kann keine Inhalte 
 * transportieren, kostet daf�r aber auch nichts, wenn die f�r Antworten
 * extra Kosten erhoben werden.
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * m�glich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingef�hrt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class DenyAnswerSpeachact extends AnswerSpeachact {

	/**
	 * @param parameters
	 */
	public DenyAnswerSpeachact(Hashtable parameters) {
		super(parameters);
	}

	/**
	 * Die Methode erlaubt kein Setzen eines Inhaltes.
	 */
	public final void setContent(Object content) 
		throws InvalidElementException {
		if(content != null) {
			throw new InvalidElementException();
		}
		m_content = null;
	}
}
