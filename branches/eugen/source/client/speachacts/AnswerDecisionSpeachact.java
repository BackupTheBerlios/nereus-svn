/*
 * Created on 05.09.2003
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
import java.util.Vector;

import exceptions.InvalidElementException;

/**
 * Spreachakt zum Antworten von Entscheidungen.
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * m�glich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingef�hrt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class AnswerDecisionSpeachact extends AnswerSpeachact {		

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public AnswerDecisionSpeachact(Hashtable parameters) {
		super(parameters);
	}

	/**
	 * Setzt den Inhalt der Nachricht.
	 * 
	 * F�r diesen Sprechakt gibt es die Beschr�nkung, dass einzelne Booleans
	 * oder Vectoren mit Booleans als Inhalt erlaubt sind.
	 * 
	 * @param Object content
	 */
	public void setContent(Object content) 
		throws InvalidElementException	{
		if((content == null) 
			|| (content instanceof Vector)
			|| (content instanceof Boolean)) {
			m_content = content;	
		}else {
			throw new InvalidElementException();
		}
	}
}
