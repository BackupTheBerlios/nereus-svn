/*
 * Created on 05.10.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package scenario.fgml;

import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;
import weka.classifiers.trees.j48.J48;

/**
 * @author Daniel
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FgmlC45LearningAgent 
	extends LearningAgent
	implements IMessagingAgent, 
			   IFgmlScenarioAgent {

	/**
	 * Konstruktor.
	 */
	public FgmlC45LearningAgent() {
		super();
		m_isALearningAgent = true;
		m_classifier = new J48();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 */
	public FgmlC45LearningAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = true;
		m_classifier = new J48();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public FgmlC45LearningAgent(Id id, String name) {
		super(id, name);
		m_isALearningAgent = true;
		m_classifier = new J48();
	}

	public static boolean isRunableAgent() {
		return true;
	}
}
