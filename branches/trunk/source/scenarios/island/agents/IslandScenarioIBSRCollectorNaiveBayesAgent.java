package scenario.island;

import simulator.AbstractScenarioHandler;
import utils.Id;
import weka.classifiers.bayes.NaiveBayes;

/**
 * Collectoragent der Strategie IBSR, der zum Lernen das Lernverfahren 
 * NaiveBayes einsetzt. 
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioIBSRCollectorNaiveBayesAgent
	extends IBSRCollectorAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioIBSRCollectorNaiveBayesAgent() {
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
	public IslandScenarioIBSRCollectorNaiveBayesAgent(
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
	public IslandScenarioIBSRCollectorNaiveBayesAgent(Id id, String name) {
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
