/*
 * Created on 03.11.2003
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

import qmc.DontCareExpandHalfQMCClassifier;
import simulator.AbstractScenarioHandler;
import utils.Id;

/**
 * Informationbroker-Agent der Strategie IBSR, der zum Lernen das 
 * Lernverfahren QMC einsetzt. 
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioIBSRCollectorQMCAgent extends IBSRCollectorAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioIBSRCollectorQMCAgent() {
		super();
		m_classifier =  new DontCareExpandHalfQMCClassifier();	
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioIBSRCollectorQMCAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier =  new DontCareExpandHalfQMCClassifier();	
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioIBSRCollectorQMCAgent(Id id, String name) {
		super(id, name);
		m_classifier =  new DontCareExpandHalfQMCClassifier();	
		m_isALearningAgent = true;
	}
	
	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}	
}
