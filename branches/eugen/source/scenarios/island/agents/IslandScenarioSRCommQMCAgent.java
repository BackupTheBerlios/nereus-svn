/*
 * Created on 06.09.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut f�r Intelligente Systeme, Universit�t Stuttgart (2003)
 */
package scenario.island;

import qmc.DontCareExpandHalfQMCClassifier;
import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;

/**
 * Die Klasse repr�sentiert einen Agenten der Strategie Selbstregulierende 
 * Kommunikation. Als Lernverfahren wird ein Summenbasierter QMC-Classifier 
 * eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioSRCommQMCAgent
	extends SelfregulatingCommunicationAgent 
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioSRCommQMCAgent() {
		super();
		/*
		 * Erzeuge einen Classifier f�r den Lernalgorithmus nach dem QMC-
		 * Minimierungsverfahren aus der Diplomarbeit.
		 */
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioSRCommQMCAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		/*
		 * Erzeuge einen Classifier f�r den Lernalgorithmus nach dem QMC-
		 * Minimierungsverfahren aus der Diplomarbeit.
		 */
		 m_classifier = new DontCareExpandHalfQMCClassifier();		 
		 m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioSRCommQMCAgent(Id id, String name) {
		super(id, name);
		/*
		 * Erzeuge einen Classifier f�r den Lernalgorithmus nach dem QMC-
		 * Minimierungsverfahren aus der Diplomarbeit.
		 */
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
