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

/**
 * Der Sprechakt fragt nach einer Klassifizierung für ein Beispiel
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class AskForDecisionSpeachact extends AskSpeachact {
	
	/**
	 * Berechnungsfaktor für Entscheidungen, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten für eine Nachricht.
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
