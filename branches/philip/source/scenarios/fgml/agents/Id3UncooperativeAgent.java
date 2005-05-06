package scenario.fgml;

import simulator.AbstractScenarioHandler;
import utils.Id;
import weka.classifiers.trees.Id3;

/**
 * @author Daniel
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Id3UncooperativeAgent 
	extends UnCooperativeAgent {

	/**
	 * Konstruktor.
	 * 
	 * 
	 */
	public Id3UncooperativeAgent() {
		super();
		m_classifier = new Id3();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public Id3UncooperativeAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_classifier = new Id3();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public Id3UncooperativeAgent(Id id, String name) {
		super(id, name);
		m_classifier = new Id3();
		m_isALearningAgent = true;
	}

}
