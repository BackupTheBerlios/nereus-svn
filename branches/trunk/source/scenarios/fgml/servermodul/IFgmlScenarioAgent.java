/*
 * Created on 04.10.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package scenario.fgml;

import simulator.AbstractScenarioHandler;
import utils.ActionKey;
import weka.classifiers.Classifier;

/**
 * @author Daniel Friedrich
 *
 * Das Interface beschreibt die Methoden die ein Agent implementieren muss, 
 * damit er an einem Spiel im Contest-Szenario für die FGML 03 in Karlsruhe 
 * teilnehmen kann.
 */
public interface IFgmlScenarioAgent {

	/**
	 * Setzt beim Agenten einen neuen ActionKey
	 */
	public void setActionKey(ActionKey actionKey);

	/**
	 * Setzt den Scenariohandler
	 */
	public void setScenarioHandler(AbstractScenarioHandler scenario);
	
	/**
	 * In dieser Methode muss der Agenten den eigentlichen Ablauf der Simulation
	 * einbauen. Die Methode ist als abstract definiert und wird jede Runde
	 * einmal, wenn die Runde begonnen werden kann aufgerufen wird. Deshalb 
	 * muss die Subklasse die Methode implementieren.
	 */
	public abstract void simulate();
	
	/**
	 * Gibt die Hypothese des Agenten zurück.
	 */
	public Classifier getHypothesis();
	
	/**
	 * Gibt die aktuelle Phase in der sich der Agent befindet zurück.
	 * 
	 * @return int - Nummer der Phase
	 */
	public int getPhase(); 
	
	/**
	 * Gibt den Namen der Strategie nach der der Agent handelt zurück.
	 *  
	 * @return String - Name der eingesetzten Strategie.
	 */
	public String getStrategyString();
}
