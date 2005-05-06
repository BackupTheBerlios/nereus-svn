package scenario.island;

import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;
import weka.classifiers.bayes.NaiveBayes;

/**
 * Brokeragent der Strategie IBSR, der zum Lernen das Lernverfahren NaiveBayes 
 * einsetzt. 
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioIBSRBrokerNaiveBayesAgent 
	extends IBSRBrokerAgent 
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioIBSRBrokerNaiveBayesAgent() {
		super();
		m_classifier =  new NaiveBayes();	
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioIBSRBrokerNaiveBayesAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier =  new NaiveBayes();	
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioIBSRBrokerNaiveBayesAgent(Id id, String name) {
		super(id, name);
		m_classifier =  new NaiveBayes();	
		m_isALearningAgent = true;
	}

	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}	
}
