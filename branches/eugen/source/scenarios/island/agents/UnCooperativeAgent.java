package scenario.island;

import scenario.communication.IMessagingAgent;
import java.util.Vector;

import simulator.AbstractScenarioHandler;
import speachacts.Speachact;
import utils.Id;
import weka.core.Instance;
import weka.core.Instances;
/**
 * Superklasse für einen Agenten der Strategie Unkooperativen Agenten.
 * Die Subklassen müssen den Agenten noch mit einem beliebigen Lernverfahren 
 * versehen.
 * 
 * @author Daniel Friedrich
 */
public class UnCooperativeAgent 
	extends IslandScenarioAgent
	implements IIslandScenarioAgent,
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
	 * @param name - Name des Agenten
	 * @param handler - Szenariohandler
	 */
	public UnCooperativeAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = false;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id - Id des Agenten
	 * @param name - Name des Agenten
	 */
	public UnCooperativeAgent(Id id, String name) {
		super(id, name);
		m_isALearningAgent = false;
	}
	
	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
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
	
	/**
	 * Ermittelt ob der Agent eine Hypothese bilden soll.
	 * 
	 * Dies ist immer der Fall, wenn er mehr Beispiele als zum Erreichen der
	 * Klassifikationsschwelle gesammelt hat.
	 * 
	 * @return boolean - True Hypothese bilden, ansonsten False und nicht bilden
	 */
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
				this.getScenarioHandler().getAttributesFromPlace(
					this.getActionKey(),
					m_id);
			// Neue Beispiele erhalten	
			if(this.shouldIBuildHypothesis()) {
				// Hypothese bilden
				this.buildHypothesis(m_examples);
			}
			// Wurde an dem Platz schon gegraben ?
			if(!this.getScenarioHandler().isPlaceExplored(
				this.getActionKey(),
				m_id)) {
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
				// Platz analysieren, wenn er nicht bereits besucht wurde.
				 if(!m_lastVisitedPlaces.contains(m_actualPlace)) {
					 boolean wasCellFound = this.getScenarioHandler().analysePlace(
						 this.getActionKey(), 
						 this.getId());
					 // abspeichern der gewonnenen Erfahrung.
					 Vector tmpVector = new Vector();
					 tmpVector.addElement(m_examples);
					 this.addExample(
						 attributes,
						 new Boolean(wasCellFound),
						 tmpVector);																
				 }
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
