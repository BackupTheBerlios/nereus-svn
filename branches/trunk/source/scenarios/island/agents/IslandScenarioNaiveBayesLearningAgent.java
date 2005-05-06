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
import weka.classifiers.bayes.NaiveBayes;

/**
 * Learning Agent der Strategie Zentrales Lernen, der zum Lernen das 
 * Lernverfahren Naive-Bayes einsetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioNaiveBayesLearningAgent
	extends LearningAgent
	implements IIslandScenarioAgent, IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioNaiveBayesLearningAgent() {
		super();
		m_classifier = new NaiveBayes();
		m_isALearningAgent = true;
	}

	/**
	 * @param name
	 * @param handler
	 */
	public IslandScenarioNaiveBayesLearningAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier = new NaiveBayes();
		m_isALearningAgent = true;
	}

	/**
	 * @param id
	 * @param name
	 */
	public IslandScenarioNaiveBayesLearningAgent(Id id, String name) {
		super(id, name);
		m_classifier = new NaiveBayes();
		m_isALearningAgent = true;
	}
	
	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}	
}
