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

import simulator.AbstractScenarioHandler;
import utils.Id;
import weka.classifiers.trees.Id3;

/**
 * Agent der Strategie Unkooperative Agenten, der zum Lernen das Lernverfahren
 * Lernverfahren Id3 einsetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioId3UnCooperativeAgent extends UnCooperativeAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioId3UnCooperativeAgent() {
		super();
		m_classifier =  new Id3();
		m_isALearningAgent = true;
		m_communication = 40;
		m_classification = 15;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioId3UnCooperativeAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier =  new Id3();
		m_isALearningAgent = true;
		m_communication = 40;
		m_classification = 15;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioId3UnCooperativeAgent(Id id, String name) {
		super(id, name);
		m_classifier =  new Id3();
		m_isALearningAgent = true;
		m_communication = 40;
		m_classification = 15;
	}
	public static boolean isRunableAgent() {
		return true;
	}
}
