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
import weka.classifiers.rules.Prism;

/**
 * Die Klasse repräsentiert einen Consulting Agent der Strategie Kompetente 
 * Berater. Als Lernverfahren wird der Prism-Algorithmus eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioConsultingPrismAgent 
	extends ConsulterAgent
	implements IMessagingAgent,
			   IIslandScenarioAgent {	

	/**
	 * Konstruktor.
	 */
	public IslandScenarioConsultingPrismAgent() {
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
	public IslandScenarioConsultingPrismAgent(
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
	public IslandScenarioConsultingPrismAgent(Id id, String name) {
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
