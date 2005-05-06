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
import weka.classifiers.trees.Id3;

/**
 * @author Daniel Friedrich
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FgmlId3FEAgent 
	extends FastExperienceAgent
	implements IMessagingAgent, 
			   IFgmlScenarioAgent {

	/**
	 * Konstruktor.
	 */
	public FgmlId3FEAgent() {
		super();
		m_isALearningAgent = true;
		m_classifier = new Id3();		
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 */
	public FgmlId3FEAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = true;
		m_classifier = new Id3();
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public FgmlId3FEAgent(Id id, String name) {
		super(id,name);
		m_isALearningAgent = true;
		m_classifier = new Id3();
	}

	public static boolean isRunableAgent() {
		return true;
	}	
}

