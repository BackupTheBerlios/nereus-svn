/*
 * Created on 07.09.2003
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

import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;
import weka.classifiers.rules.Prism;

/**
 * Die Klasse repr�sentiert einen Sammelagenten der Strategie Collectoragent
 * Als Lernverfahren wird der Naive Bayes-Algorithmus eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioIBCPrismAgent
	extends IBCollectorAgent
	implements IMessagingAgent {

	/**
	 * Konstruktor
	 */
	public IslandScenarioIBCPrismAgent() {
		super();
		/*
		 * Erzeuge einen Classifier f�r den Entscheidungsbaumalgorithmus ID3
		 * Algorithmus aus dem WEKA-System
		 */
		m_classifier = new Prism();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioIBCPrismAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		/*
		 * Erzeuge einen Classifier f�r den Entscheidungsbaumalgorithmus ID3
		 * Algorithmus aus dem WEKA-System
		 */
		m_classifier = new Prism();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 *  
	 * @param id
	 * @param name
	 */
	public IslandScenarioIBCPrismAgent(Id id, String name) {
		super(id, name);
		/*
		 * Erzeuge einen Classifier f�r den Entscheidungsbaumalgorithmus ID3
		 * Algorithmus aus dem WEKA-System
		 */
		m_classifier = new Prism();
		m_isALearningAgent = true;
	}

	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}
}
