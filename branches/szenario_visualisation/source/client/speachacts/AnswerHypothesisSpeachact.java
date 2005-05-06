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
import java.util.Vector;

import weka.classifiers.Classifier;
import exceptions.InvalidElementException;

/**
 * Sprechakt zur Antwort einer Hypothese auf eine Anfrage nach einer Hypothese.
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class AnswerHypothesisSpeachact extends AnswerSpeachact {

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public AnswerHypothesisSpeachact(Hashtable parameters) {
		super(parameters);
	}

	/**
	 * Setzt den Inhalt der Nachricht.
	 * 
	 * Für diesen Sprechakt gibt es die Beschränkung auf Classifier, Strings,
	 * und Vector Objekte. Alle anderen lösen eine InvalidElementException aus.
	 * 
	 * @param Object content
	 */
	public void setContent(Object content) 
		throws InvalidElementException	{
		if((content == null) 
			|| (content instanceof Vector)
			|| (content instanceof Classifier)) {
			m_content = content;	
		}else {
			throw new InvalidElementException();
		}
	}
}
