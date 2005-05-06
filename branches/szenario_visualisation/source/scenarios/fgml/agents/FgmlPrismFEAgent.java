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
import weka.classifiers.rules.Prism;

/**
 * @author Daniel
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FgmlPrismFEAgent 
	extends FastExperienceAgent
	implements IMessagingAgent, 
			   IFgmlScenarioAgent {

	/**
	 * Konstruktor.
	 */
	public FgmlPrismFEAgent() {
		super();
		m_isALearningAgent = true;
		m_classifier = new Prism();		
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 */
	public FgmlPrismFEAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = true;
		m_classifier = new Prism();
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public FgmlPrismFEAgent(Id id, String name) {
		super(id,name);
		m_isALearningAgent = true;
		m_classifier = new Prism();
	}

	public static boolean isRunableAgent() {
		return true;
	}	
}
