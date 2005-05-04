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
 * Der Sprechakt fragt nach einer Klassifizierung f�r ein Beispiel
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * m�glich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingef�hrt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class AskForDecisionSpeachact extends AskSpeachact {
	
	/**
	 * Berechnungsfaktor f�r Entscheidungen, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten f�r eine Nachricht.
	 */
	private final double m_decisionFactor = 0.25;
	
	/**
	 * Parametername zum Durchreichen des Entscheidungsfaktors. 
	 */
	public final String DECISIONSFACTOR = "DecisionsFactor";	

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public AskForDecisionSpeachact(Hashtable parameters) {
		super(parameters);
	}
}
