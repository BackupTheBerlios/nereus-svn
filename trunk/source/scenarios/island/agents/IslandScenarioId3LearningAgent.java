/*
 * Created on 26.09.2003
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
import weka.classifiers.trees.Id3;

/**
 * Learning Agent der Strategie Zentrales Lernen, der zum Lernen das 
 * Lernverfahren QMC einsetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioId3LearningAgent 
	extends LearningAgent 
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioId3LearningAgent() {
		super();
		m_classifier = new Id3();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioId3LearningAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier = new Id3();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioId3LearningAgent(Id id, String name) {
		super(id, name);
		m_classifier = new Id3();
		m_isALearningAgent = true;
	}

	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}	
}
