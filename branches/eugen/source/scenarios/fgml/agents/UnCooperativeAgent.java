package scenario.fgml;

import java.util.Vector;

import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import speachacts.Speachact;
import utils.Id;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Daniel Friedrich
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UnCooperativeAgent 
	extends FgmlScenarioAgent
	implements IFgmlScenarioAgent,
				IMessagingAgent {
	/* (non-Javadoc)
	 * @see scenario.communication.IMessagingAgent#receiveMessage(speachacts.Speachact)
	 */
	public Speachact receiveMessage(Speachact act) {
		return null;
	}

	/**
	 * Konstruktor.
	 */
	public UnCooperativeAgent() {
		super();
		m_isALearningAgent = false;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public UnCooperativeAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = false;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public UnCooperativeAgent(Id id, String name) {
		super(id, name);
		m_isALearningAgent = false;
	}
	
	public static boolean isRunableAgent() {
		return false;
	}	

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getPhase()
	 */
	public int getPhase() {
		if(m_classification-1 < m_numExamples) {
			if(m_communication-1 < m_numExamples) {
				// Bereits Kommunikationsschwelle überschritten
				return 3;
			}
			// Klassifikationsschwelle erreicht.
			return 2;
		}
		// Ansonsten erste Phase
		return 1;
	}
	
	public boolean shouldIBuildHypothesis() {
		if(m_numExamples > m_classification) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see scenario.fgml.IFgmlScenarioAgent#simulate()
	 */
	public void simulate() {
		// Runde hochzählen
		m_roundCounter++;	

		try {
			// Attribute wahrnehmen
			Instance attributes = 
				m_scenarioHandler.getAttributesFromPlace(
					this.getActionKey(),
					m_id);
			// Neue Beispiele erhalten	
			if(this.shouldIBuildHypothesis()) {
				// Hypothese bilden
				this.buildHypothesis(m_examples);
			}
			// Wurde an dem Platz schon gegraben ?
			if(!m_scenarioHandler.isPlaceExplored(this.getActionKey(),m_id)) {
				// Platz noch nicht ausgebeutet
				if(this.shouldIExploreThePlace(attributes)) {
					// Graben und auswerten
					boolean cellFound = this.exploreThePlace();
					// Erst noch Erfahrungspeicher initialisieren
					if(m_roundCounter == 1) {
						if(cellFound) {
							attributes.setClassValue(1.0);
						}else {
							attributes.setClassValue(0.0);
						}
						m_examples = new Instances(attributes.dataset(),0);				
					}					
					// abspeichern der gewonnenen Erfahrung.
					Vector tmpVector = new Vector();
					tmpVector.addElement(m_examples);
					this.addExample(
						attributes,
						new Boolean(cellFound),
						tmpVector);										
				}
			}else {
				System.out.println("Platz " + m_actualPlace.toString() + " schon ausgebeutet");
			}
			// Bewegen wenn möglich
			this.move();
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}	

	}

	/* (non-Javadoc)
	 * @see scenario.fgml.IFgmlScenarioAgent#getStrategyString()
	 */
	public String getStrategyString() {
		return "Uncooperative Agentteam";
	}

}
