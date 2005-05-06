/*
 * Created on 02.09.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package scenario.island;

import utils.ActionKey;
import weka.classifiers.Classifier;

/**
 * Das Interface beschreibt die Methoden die ein Agent implementieren muss, 
 * damit er an einem Spiel im IslandParallelScenario teilnehmen kann.
 * 
 * @author Daniel Friedrich
 */
public interface IIslandScenarioAgent {
	
	/**
	 * Setzt beim Agenten einen neuen ActionKey
	 */
	public void setActionKey(ActionKey actionKey);
	
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
	 * @return int - Aktuelle Phase in der sich der Agent befindet. ( = 1,2,3)
	 */
	public int getPhase(); 
	
	/**
	 * Gibt den Namen der Strategie, nach der sich der Agent richt zurück.
	 * 
	 * @return String 
	 */
	public String getStrategyString();
}

