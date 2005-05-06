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
import weka.classifiers.bayes.NaiveBayes;

/**
 * @author Daniel
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FgmlNaiveBayesFEAgent 
	extends FastExperienceAgent
	implements IMessagingAgent, 
			   IFgmlScenarioAgent {

	/**
	 * Konstruktor.
	 */
	public FgmlNaiveBayesFEAgent() {
		super();
		m_isALearningAgent = true;
		m_classifier = new NaiveBayes();		
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 */
	public FgmlNaiveBayesFEAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = true;
		m_classifier = new NaiveBayes();
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public FgmlNaiveBayesFEAgent(Id id, String name) {
		super(id,name);
		m_isALearningAgent = true;
		m_classifier = new NaiveBayes();
	}

	public static boolean isRunableAgent() {
		return true;
	}	
}
