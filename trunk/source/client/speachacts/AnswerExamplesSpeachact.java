/*
 * Created on 05.09.2003
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

import weka.core.Instance;
import weka.core.Instances;
import exceptions.InvalidElementException;

/**
 * Sprechakt zum Antworten von Beispielen.
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class AnswerExamplesSpeachact extends AnswerSpeachact {

	/**
	 * Berechnungsfaktor für Beispiele, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten für eine Nachricht.
	 */
	private final double m_examplesFactor = 1.0;	
	
	/**
	 * Parametername zum Durchreichen des Beispielfaktors. 
	 */
	public final String EXAMPLESFACTOR = "ExamplesFactor";	

	/**
	 * Flag, dass speichert, ob mehr als ein Beispiel geantwortet wird.
	 * True - Es werden mehrere Beispiele geantwortet.
	 * False - Es wird nur ein Beispiel geantwortet.
	 */
	private boolean m_moreThanOneExample = false;

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public AnswerExamplesSpeachact(Hashtable parameters) {
		super(parameters);
	}
	
	/**
	 * Setzt ein Flag, dass mehr als ein Beispiel geantwortet wird. 
	 */
	public void answersMoreThanOneExample(){
		m_moreThanOneExample = true;
	}
	
	/**
	 * Gibt an, ob mehr als ein Beispiel geantwortet werden soll.
	 * 
	 * @return boolean True (mehre Beispiele) / False (ein Beispiel) 
	 */
	public boolean moreThanOneExample() {
		return m_moreThanOneExample;
	}
	
	/**
	 * Setzt den Inhalt der Nachricht.
	 * 
	 * Für diesen Sprechakt gibt es die Beschränkung auf Instance, Vector und 
	 * Instances Objekte. Alle anderen lösen eine InvalidElementException aus.
	 * 
	 * @param Object content
	 */
	public void setContent(Object content) 
		throws InvalidElementException	{
		if((content == null) 
			|| (content instanceof Vector)
			|| (content instanceof Instance)
			|| (content instanceof Instances)) {
			m_content = content;	
		}else {
			throw new InvalidElementException();
		}
	}
}
