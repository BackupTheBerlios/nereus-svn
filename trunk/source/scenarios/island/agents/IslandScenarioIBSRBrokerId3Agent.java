package scenario.island;

import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;
import weka.classifiers.trees.Id3;

/**
 * Brokeragent der Strategie IBSR, der zum Lernen das Lernverfahren Id3 
 * einsetzt. 
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioIBSRBrokerId3Agent 
	extends IBSRBrokerAgent 
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioIBSRBrokerId3Agent() {
		super();
		m_classifier =  new Id3();
		m_isALearningAgent = true;	
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioIBSRBrokerId3Agent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier =  new Id3();	
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioIBSRBrokerId3Agent(Id id, String name) {
		super(id, name);
		m_classifier =  new Id3();	
		m_isALearningAgent = true;
	}

	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}	
}
