/*
 * Created on 06.09.2003
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
import weka.classifiers.trees.Id3;

/**
 * Die Klasse repr�sentiert einen Agenten der Strategie Selbstregulierende 
 * Kommunikation.Als Lernverfahren wird der Id3-Algorithmus eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioSRCommId3Agent
	extends SelfregulatingCommunicationAgent 
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioSRCommId3Agent() {
		super();
		/*
		 * Erzeuge einen Classifier f�r den Entscheidungsbaumalgorithmus ID3
		 * Algorithmus aus dem WEKA-System
		 */
		m_classifier =  new Id3();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioSRCommId3Agent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		/*
		 * Erzeuge einen Classifier f�r den Entscheidungsbaumalgorithmus ID3
		 * Algorithmus aus dem WEKA-System
		 */
		m_classifier =  new Id3();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioSRCommId3Agent(Id id, String name) {
		super(id, name);
		/*
		 * Erzeuge einen Classifier f�r den Entscheidungsbaumalgorithmus ID3
		 * Algorithmus aus dem WEKA-System
		 */
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
