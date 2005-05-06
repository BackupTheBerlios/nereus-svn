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
 * Die Klasse repräsentiert einen Sprechakt zum Versenden von 
 * Trainingsbeispielen
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class SendExamplesSpeachact extends SendSpeachact {

	/**
	 * Gibt an ob mehr als ein Beispiel transportiert werden.
	 */
	private boolean m_moreThanOneExample = false;

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public SendExamplesSpeachact(Hashtable parameters) {
		super(parameters);
	}
	
	/**
	 * Setzt das Flag, dass mehr als ein Beispiel übertragen wird.
	 */
	public void answersMoreThanOneExample(){
		m_moreThanOneExample = true;
	}
	
	/**
	 * Gibt zurück, ob mehr als ein Beispiel übertragen wird.
	 * 
	 * @return boolean - True (Mehrere Beispiele) / False (ein Beispiel)
	 */
	public boolean moreThanOneExample() {
		return m_moreThanOneExample;
	}	

	/**
	 * Die Methode setzt den Inhalt des Sprechaktes. 
	 * 
	 * Der Inhalt dieses Sprechaktes kann nur aus einer einzelnen Hypothese oder
	 * null bestehen. Wird versucht etwas anderes als Inhalt zu übertragen, 
	 * dann wird eine InvalidElementException geworfen. 
	 */
	public void setContent(Object content) throws InvalidElementException {
		if((content == null) 
			|| (content instanceof Instance)
			|| (content instanceof Vector)
			|| (content instanceof Instances)) {
			m_content = content;
		}else {
			throw new InvalidElementException(
				"SendHypothesisSpeachact - fehlerhaftes Inhaltselement: " 
				+ content.toString());
		}
	}
}
