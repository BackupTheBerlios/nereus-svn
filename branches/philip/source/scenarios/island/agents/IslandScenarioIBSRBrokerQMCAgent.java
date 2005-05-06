package scenario.island;

import qmc.DontCareExpandHalfQMCClassifier;
import simulator.AbstractScenarioHandler;
import utils.Id;

/**
 * Brokeragent der Strategie IBSR, der zum Lernen das QMC-Lernverfahren 
 * einsetzt. 
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioIBSRBrokerQMCAgent extends IBSRBrokerAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioIBSRBrokerQMCAgent() {
		super();
		m_classifier =  new DontCareExpandHalfQMCClassifier();	
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioIBSRBrokerQMCAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier =  new DontCareExpandHalfQMCClassifier();	
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioIBSRBrokerQMCAgent(Id id, String name) {
		super(id, name);
		m_classifier =  new DontCareExpandHalfQMCClassifier();	
		m_isALearningAgent = true;
	}
	
	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}	
}
