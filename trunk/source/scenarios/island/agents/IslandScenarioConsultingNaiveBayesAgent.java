/*
 * Created on 07.09.2003
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
 * Die Klasse repräsentiert einen Consulting Agent der Strategie Kompetente 
 * Berater. Als Lernverfahren wird der Naive-Bayes-Algorithmus eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioConsultingNaiveBayesAgent
	extends ConsulterAgent
	implements IMessagingAgent,
				IIslandScenarioAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioConsultingNaiveBayesAgent() {
		super();
		/*
		 * Erzeuge einen Classifier für den Lernalgorithm NaiveBayes aus dem 
		 * WEKA-System
		 */
		m_classifier =  new NaiveBayes();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioConsultingNaiveBayesAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		/*
		 * Erzeuge einen Classifier für den Lernalgorithm NaiveBayes aus dem 
		 * WEKA-System
		 */
		m_classifier =  new NaiveBayes();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioConsultingNaiveBayesAgent(Id id, String name) {
		super(id, name);
		/*
		 * Erzeuge einen Classifier für den Lernalgorithm NaiveBayes aus dem 
		 * WEKA-System
		 */
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
