package scenario.island;

import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;
import weka.classifiers.rules.Prism;

/**
 * Brokeragent der Strategie IBSR, der zum Lernen das Lernverfahren Prism 
 * einsetzt. 
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioIBSRBrokerPrismAgent 
	extends IBSRBrokerAgent 
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioIBSRBrokerPrismAgent() {
		super();
		m_classifier =  new Prism();	
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioIBSRBrokerPrismAgent(
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
	public IslandScenarioIBSRBrokerPrismAgent(Id id, String name) {
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
