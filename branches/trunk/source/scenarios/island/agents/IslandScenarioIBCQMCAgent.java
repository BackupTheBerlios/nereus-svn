/*
 * Created on 05.09.2003
 *
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package scenario.island;

//import qmcD.DontCareQMCClassifier;
import qmc.DontCareExpandHalfQMCClassifier;
import scenario.communication.IMessagingAgent;
//import qmc.DontCareExpandQMClassifier;
import simulator.AbstractScenarioHandler;
import utils.Id;

/**
 * Die Klasse repräsentiert einen Sammelagenten der Strategie Informationbroker
 * Als Lernverfahren wird ein Summenbasierter QMC-Classifier eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioIBCQMCAgent 
	extends IBCollectorAgent
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioIBCQMCAgent() {
		super();
		//m_classifier = new DontCareExpandQMClassifier();
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioIBCQMCAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		//m_classifier = new DontCareExpandQMClassifier();
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioIBCQMCAgent(Id id, String name) {
		super(id, name);
		//m_classifier = new DontCareExpandQMClassifier();
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
