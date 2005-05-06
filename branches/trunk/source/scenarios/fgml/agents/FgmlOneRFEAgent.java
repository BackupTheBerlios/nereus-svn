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
import weka.classifiers.rules.OneR;

/**
 * @author Daniel Friedrich
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FgmlOneRFEAgent 
	extends FastExperienceAgent
	implements IMessagingAgent, 
			   IFgmlScenarioAgent {

	/**
	 * Konstruktor.
	 */
	public FgmlOneRFEAgent() {
		super();
		m_isALearningAgent = true;
		m_classifier = new OneR();		
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 */
	public FgmlOneRFEAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = true;
		m_classifier = new OneR();
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public FgmlOneRFEAgent(Id id, String name) {
		super(id,name);
		m_isALearningAgent = true;
		m_classifier = new OneR();
	}

	public static boolean isRunableAgent() {
		return true;
	}	
}
