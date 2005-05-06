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

import qmc.DontCareExpandHalfQMCClassifier;
import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;

/**
 * Agent der Strategie Unkooperative Agenten, der zum Lernen das 
 * QMC-Lernverfahren einsetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioQMCLearningAgent
	extends LearningAgent
	implements IIslandScenarioAgent, IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioQMCLearningAgent() {
		super();
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioQMCLearningAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioQMCLearningAgent(Id id, String name) {
		super(id, name);
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}	
}
