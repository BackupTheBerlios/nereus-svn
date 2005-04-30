/*
 * Created on 05.09.2003
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
 * Die Klasse repräsentiert den Informationbroker für die Agenten der Strategie 
 * Informationbroker. Als Lernverfahren wird ein Summenbasierter QMC-Classifier 
 * eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioInfobrokerQMCAgent 
	extends InformationbrokerAgent
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioInfobrokerQMCAgent() {
		super();
		//m_classifier =  new DontCareExpandQMClassifier();
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioInfobrokerQMCAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		//m_classifier =  new DontCareExpandQMClassifier();
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor. 
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioInfobrokerQMCAgent(Id id, String name) {
		super(id, name);
		//m_classifier =  new DontCareExpandQMClassifier();
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
