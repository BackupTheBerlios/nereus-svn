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
package speachacts;

import java.util.Hashtable;

import weka.classifiers.Classifier;

import exceptions.InvalidElementException;

/**
 * Die Klasse repräsentiert einen Sprechakt zum Versenden einer Hypothesen.
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class SendHypothesisSpeachact extends SendSpeachact {

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public SendHypothesisSpeachact(Hashtable parameters) {
		super(parameters);
	}

	/**
	 * Die Methode setzt den Inhalt des Sprechaktes. 
	 * 
	 * Der Inhalt dieses Sprechaktes kann nur aus einer einzelnen Hypothese 
	 * oder aus null bestehen. Wird versucht etwas anderes als Inhalt 
	 * zu übertragen, dann wird eine InvalidElementException geworfen. 
	 */
	public void setContent(Object content) throws InvalidElementException {
		if((content == null) || (content instanceof Classifier)) {
			m_content = content;
		}else {
			throw new InvalidElementException(
				"SendHypothesisSpeachact - fehlerhaftes Inhaltselement: " 
				+ content.toString());
		}
	}
}
