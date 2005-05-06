/*
 * Created on 03.09.2003
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

import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;
import weka.classifiers.rules.Prism;

/**
 * Die Klasse repräsentiert den Informationbrokeragenten der Strategie 
 * Informationbroker Als Lernverfahren wird der Prism-Algorithmus eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioInfobrokerPrismAgent
	extends InformationbrokerAgent
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 * 
	 * 
	 */
	public IslandScenarioInfobrokerPrismAgent() {
		super();
		/*
		 * Erzeuge einen Classifier für den Entscheidungsbaumalgorithmus ID3
		 * Algorithmus aus dem WEKA-System
		 */
		m_classifier =  new Prism();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioInfobrokerPrismAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier =  new Prism();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioInfobrokerPrismAgent(Id id, String name) {
		super(id, name);
		m_classifier =  new Prism();
	m_isALearningAgent = true;
	}
	
	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}	
}
