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
import weka.classifiers.rules.Prism;

/**
 * Agent der Strategie Unkooperative Agenten, der zum Lernen das Lernverfahren
 * Lernverfahren Prism einsetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioPrismUnCooperativeAgent extends UnCooperativeAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioPrismUnCooperativeAgent() {
		super();
		m_classifier =  new Prism();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioPrismUnCooperativeAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier =  new Prism();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioPrismUnCooperativeAgent(Id id, String name) {
		super(id, name);
		m_classifier =  new Prism();
		m_isALearningAgent = true;
	}
	
	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}
}
