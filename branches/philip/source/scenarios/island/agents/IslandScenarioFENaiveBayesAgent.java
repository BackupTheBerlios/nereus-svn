/*
 * Created on 02.09.2003
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
 * Die Klasse repräsentiert einen Agenten der Strategie Schnelles Erfahrungs-
 * wachstum. Als Lernverfahren wird ein Nayes Bayes-Algorithmus eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioFENaiveBayesAgent 
	extends FastExperienceAgent 
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioFENaiveBayesAgent() {
		super();
		m_name = m_id.toString();
		/*
		 * Erzeuge einen Classifier den Klassifikationsregelalgorithmus
		 * Naive Bayes aus dem WEKA-System
		 */		
		m_classifier = new NaiveBayes();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioFENaiveBayesAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		/*
		 * Erzeuge einen Classifier den Klassifikationsregelalgorithmus
		 * Naive Bayes aus dem WEKA-System
		 */		
		m_classifier = new NaiveBayes();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioFENaiveBayesAgent(Id id, String name) {
		super(id, name);
		/*
		 * Erzeuge einen Classifier den Klassifikationsregelalgorithmus
		 * Naive Bayes aus dem WEKA-System
		 */		
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
