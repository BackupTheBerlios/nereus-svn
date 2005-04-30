/*
 * Created on 03.09.2003
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
import weka.classifiers.trees.Id3;

/**
 * Die Klasse repräsentiert den Informationbrokeragenten der Strategie 
 * Informationbroker Als Lernverfahren wird der Id3-Algorithmus eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioInfobrokerId3Agent 
	extends InformationbrokerAgent
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioInfobrokerId3Agent() {
		super();
		/*
		 * Erzeuge einen Classifier für den Entscheidungsbaumalgorithmus ID3
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
	public IslandScenarioInfobrokerId3Agent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		/*
		 * Erzeuge einen Classifier für den Entscheidungsbaumalgorithmus ID3
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
	public IslandScenarioInfobrokerId3Agent(Id id, String name) {
		super(id, name);
		/*
		 * Erzeuge einen Classifier für den Entscheidungsbaumalgorithmus ID3
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
