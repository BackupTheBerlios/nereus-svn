/*
 * Created on 04.08.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package simulator.statistic;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Die Schnittstelle definiert die Methoden, die ein Szenario implementieren
 * muss, wenn es für es eine Statistik erstellt werden soll.
 * 
 * @author Daniel Friedrich
 */
public interface IStatisticScenario {
	
	/**
	 * Erstellt eine neue szenariospezifische Statistikkomponente.
	 * 
	 * Das Szenario muss eine Statistikkomponente erstellen, die der 
	 * Schnittstelle IStatisticComponent genügt. Die Komponente ist dazu da,
	 * während des Ablaufs des Spiels eigenständig die statistischen Daten zu
	 * erfassen und auszuwerten. 
	 * 
	 * @param agents - Menge der Agenten
	 * @return IStatisticComponent - Statistikkomponente des Szenarios.
	 */
	public IStatisticComponent createNewStatisticComponent(Vector agents);
	
	/**
	 * Gibt eine Hashtable zurück in der die Parameter aufgelistet sind, die statistisch erfasst werden.
	 * 
	 * @return Hashtable - Hashtable mit den zu erfassenden Parametern
	 */
	public Hashtable getEnviromentStatisticParameters();
}
