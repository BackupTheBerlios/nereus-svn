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

import scenario.communication.IMessagingAgent;
import scenario.island.FastExperienceAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;
import weka.classifiers.rules.Prism;

/**
 * Die Klasse repräsentiert einen Agenten der Strategie Schnelles Erfahrungs-
 * wachstum. Als Lernverfahren wird ein Nayes Bayes-Algorithmus eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioFEPrismAgent 
	extends FastExperienceAgent 
	implements IIslandScenarioAgent,
				IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioFEPrismAgent() {
		super();
		m_classifier = new Prism();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioFEPrismAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier = new Prism();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioFEPrismAgent(Id id, String name) {
		super(id, name);
		m_classifier = new Prism();
		m_isALearningAgent = true;
	}
	
	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}	
}
