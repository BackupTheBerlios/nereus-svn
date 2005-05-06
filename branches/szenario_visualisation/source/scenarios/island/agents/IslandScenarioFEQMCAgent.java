/*
 * Created on 05.09.2003
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
import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;
/**
 * Die Klasse repräsentiert einen Agenten der Strategie Schnelles Erfahrungs-
 * wachstum.Als Lernverfahren wird ein Summenbasierter QMC-Classifier eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioFEQMCAgent 
	extends FastExperienceAgent 
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioFEQMCAgent() {
		super();
		//m_classifier = new DontCareExpandQMClassifier();
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioFEQMCAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		//m_classifier = new DontCareExpandQMClassifier();
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioFEQMCAgent(Id id, String name) {
		super(id, name);
		//m_classifier = new DontCareExpandQMClassifier();
		m_classifier = new DontCareExpandHalfQMCClassifier();
		m_isALearningAgent = true;
	}

	public static boolean isRunableAgent() {
		return true;
	}	
}
