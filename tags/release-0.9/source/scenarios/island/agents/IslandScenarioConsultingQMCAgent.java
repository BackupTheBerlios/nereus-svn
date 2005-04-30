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

import qmc.DontCareExpandHalfQMCClassifier;
import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;

/**
 * Die Klasse repräsentiert einen Consulting Agent der Strategie Kompetente 
 * Berater. Als Lernverfahren wird der QMC-Algorithmus eingesetzt.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioConsultingQMCAgent
	extends ConsulterAgent 
	implements IMessagingAgent {

	/**
	 * Konstruktor.
	 */
	public IslandScenarioConsultingQMCAgent() {
		super();
		/*
		 * Erzeuge einen Classifier für den Lernalgorithm QMC nach dem 
		 * Minimierungsverfahren von Quine-McCluskey aus der Logik. Der 
		 * Algorithmus wurde im Rahmen der Diplomarbeit entwickelt.
		 */
		m_classifier = new DontCareExpandHalfQMCClassifier();		
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IslandScenarioConsultingQMCAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		/*
		 * Erzeuge einen Classifier für den Lernalgorithm QMC nach dem 
		 * Minimierungsverfahren von Quine-McCluskey aus der Logik. Der 
		 * Algorithmus wurde im Rahmen der Diplomarbeit entwickelt.
		 */
		 m_classifier = new DontCareExpandHalfQMCClassifier();
		 m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IslandScenarioConsultingQMCAgent(Id id, String name) {
		super(id, name);
		/*
		 * Erzeuge einen Classifier für den Lernalgorithm QMC nach dem 
		 * Minimierungsverfahren von Quine-McCluskey aus der Logik. Der 
		 * Algorithmus wurde im Rahmen der Diplomarbeit entwickelt.
		 */
		m_classifier = new DontCareExpandHalfQMCClassifier();		
		m_isALearningAgent = true;
	}

	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}	
}
