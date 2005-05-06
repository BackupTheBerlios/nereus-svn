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

/**
 * Sprechakt zur Anfrage nach einer Hypothese.
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * m�glich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingef�hrt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class AskForHypothesisSpeachact extends AskSpeachact {

	/**
	 * Konstante zum Durchreichen des Hypothesenfaktors. 
	 */
	public final String HYPOTHESISFACTOR = "HypothesisFactor";

	/**
	 * Berechnungsfaktor f�r Hypothesen, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten f�r eine Nachricht.
	 */
	private final double m_hypothesisFactor = 0.5;

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public AskForHypothesisSpeachact(Hashtable parameters) {
		super(parameters);
	}
}
