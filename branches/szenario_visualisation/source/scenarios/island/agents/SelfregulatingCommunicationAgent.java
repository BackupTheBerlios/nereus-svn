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

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import speachacts.CommitRecievingSpeachact;
import speachacts.DenyAnswerSpeachact;
import speachacts.SendExamplesSpeachact;
import speachacts.Speachact;
import utils.Id;
import visualisation.IVisualisation;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Der Agent implementiert die Strategie Selbstregulierende Kommunikation aus
 * der Diplomarbeit.
 * 
 * @author Daniel Friedrich
 */
public class SelfregulatingCommunicationAgent 
	extends IslandScenarioAgent 
	implements IIslandScenarioAgent,
				IMessagingAgent {

	/**
	 * Liste aller neuen Beispeile
	 */
	
	private LinkedList m_examplesGetLastRound = new LinkedList();

	/**
	 * Liste der Examples die in der letzten Runde empfangen wurde 
	 */
	private LinkedList m_examplesGetThisRound = new LinkedList();
	
	/**
	 * Gibt an, ob ein neues Beispiel gefunden wurde.
	 */
	private boolean m_foundNewExample = false;
	
	/**
	 * Anzahl der Beispiele für das letzte Beispiele
	 */
	private int m_numOfExamplesGetLastRound = 0;
	
	/**
	 * Anzahl der neuen Beispiele.
	 */
	private int m_numOfNewExamples = 0;
	
	/**
	 * Konstruktor.
	 */
	public SelfregulatingCommunicationAgent() {
		super();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public SelfregulatingCommunicationAgent(
		String name,
		AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public SelfregulatingCommunicationAgent(Id id, String name) {
		super(id, name);
		m_isALearningAgent = true;
	}

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#simulate()
	 */
	public void simulate() {
		try {
			// Runde hochzählen
			m_roundCounter++;
			
			// Neue Beispiele erhalten ? 
			if(m_examplesGetLastRound.size() > 0) {
				// Über Liste der neuen Beispiel laufen
				ListIterator iterator = m_examplesGetLastRound.listIterator();
				while(iterator.hasNext()) {
					Instance instance = (Instance)iterator.next();
					// Hinzufügen zu Erfahrung
					if(m_examples == null) {
						m_examples = new Instances(instance.dataset(),0);
					}
					if(instance != null) {	
						m_examples.add(instance);
					}					
				}
				// Liste wieder löschen
				m_examplesGetLastRound.clear();
				// Hypothese neu bilden
				this.buildHypothesis(m_examples);
			}
			
			// Attribute wahrnehmen
			Instance attributes = 
				this.getScenarioHandler().getAttributesFromPlace(
					this.getActionKey(),
					m_id);
			// Wurde an dem Platz schon gegraben ?
			if(!this.getScenarioHandler().isPlaceExplored(this.getActionKey(),m_id)) {
				// Soll gegraben werden ?
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
					Vector tmpVector = new Vector();
					tmpVector.addElement(m_examples);
					this.addExample(
						attributes,
						new Boolean(cellFound),
						tmpVector);	
					m_numOfNewExamples++;
					Classifier classifier = 
						(Classifier.makeCopies(m_classifier,1))[0];
					// Hypothese neu bilden
					this.buildHypothesis(m_examples);
					//m_classifier.buildClassifier(m_examples);
					// Soll kommuniziert werden ?
					if(this.shouldICommunicate(classifier)) {
						// kommunizieren
						this.communicate();
	 			    }	
				}			
			}	
			// Bewegen wenn möglich
			this.move();
			m_numOfExamplesGetLastRound = m_numOfNewExamples;
			m_numOfNewExamples = 0;
			m_examplesGetLastRound = new LinkedList(m_examplesGetThisRound);
			m_examplesGetThisRound = new LinkedList();		
		}catch(Exception e)  {
			e.printStackTrace(System.out);
		}
	}

	/**
	 * Agent überträgt, dass hypothesen 
	 */
	private void communicate() {
		try {
			// Hole alle erreichbaren Agenten
			Vector reachableAgents = 
				this.getScenarioHandler().getForCommunicationReachableAgents(
				this.getActionKey(),
					this.getId());
			// eigenes Energieniveau neu abfragen.	
			m_actualEnergy = this.getScenarioHandler().getMyEnergyValue(
				this.getActionKey(),
				this.getId());		
			Enumeration commPartner = reachableAgents.elements();
			while(commPartner.hasMoreElements()) {
				Id agentId = (Id)commPartner.nextElement();
				// loggen
				try {
					this.getScenarioHandler().log(
						m_id,
						IVisualisation.CommMsg,
						"Der Agent "
						+ m_id
						+ " sendet dem Agenten "
						+ agentId
						+ " das folgende Beispiel: " 
						+ m_examples.lastInstance().toString());
				}catch(Exception e) {
					e.printStackTrace(System.out);	
				} 				
				Speachact sendact = 
					this.getScenarioHandler().createSpeachAct(
						SendExamplesSpeachact.class);	
				sendact.setSender(m_id);
				sendact.setReceiver(agentId);
				sendact.setContent(m_examples.lastInstance());	
				Speachact act = this.getScenarioHandler().sendMessage(
					this.getActionKey(),
					sendact);
			}
		}catch(Exception e) {
			System.out.println("Fehler bei der Communication des Agenten "
				+ this.getId().toString());
			e.printStackTrace(System.out);	
		}
	}
	
	/**
	 * Gibt zurück, ob der Agent kommunizieren soll.
	 * 
	 * Dies ist immer dann der Fall, wenn die beiden folgenden Bedingungen 
	 * erfüllt sind:
	 * - der Agent befindet sich nicht in der ersten Runde
	 * - die Anzahl der Trainingsbeispiele ist niedriger als die 
	 * 	 Kommunikationschwelle (m_communication)
	 * 
	 * @return boolean
	 */
	private boolean shouldICommunicate(Classifier classifier) {
		if((m_roundCounter > 1) 
			&& (this.haveChanged(classifier))) {
			return true;
		}
		return false;
	}
	

	/* (non-Javadoc)
	 * @see scenario.communication.IMessagingAgent#receiveMessage(speachacts.Speachact)
	 */
	 public Speachact receiveMessage(Speachact act) {
		 Speachact answer = null;
		 try {	
			if(act instanceof SendExamplesSpeachact) {
				answer = this.getScenarioHandler().createSpeachAct(
					CommitRecievingSpeachact.class);	
				/*
				 * Info aus dem Sprechakt holen, in der Strategie werden nur
				 * einzelne Beispiele versendet, deshalb kann eine Prüfung auf
				 * mehrere Beispiele (Vector) entfallen.
				 */
				 if((act.getContent() != null) 
				 	&& (act.getContent() instanceof Instance)) {
				 	Instance instance = (Instance)act.getContent();
				 	// In Liste der neuen Elemente hinzufügen.
					m_examplesGetThisRound.addLast(instance);
					m_numOfNewExamples++;
				 	/*
				 	Vector tmpVector = new Vector();
				 	tmpVector.addElement(m_examples);	
				 	this.addExample(instance,null,tmpVector);*/
					// loggen
					try {
						this.getScenarioHandler().log(
							m_id,
							IVisualisation.CommMsg,
							"Der Agent ("
							+ m_id.toString() 
							+ ") hat vom Agenten ("
							+ act.getSender().toString()
							+ ") das folgende Beispiel erhalten:  "
							+ instance.toString());
					}catch(Exception e) {
						e.printStackTrace(System.out);	
					} 				 	
				 }
				// Berechnen ob die Antwort wirklich gesendet werden soll.
				if(!this.couldIRenderTheAnswer(answer,0.0,0.0)) {
					answer = null;					 	
				}					
			}
			if(answer == null) {
				answer = this.getScenarioHandler().createSpeachAct(
					DenyAnswerSpeachact.class);
			}
			answer.setSender(m_id);
			answer.setReceiver(act.getSender());		 				 
		 }catch(Exception e) {
			 System.out.println("Fehler beim Erstellen der Antwort");
		 }
		 return answer;
	 }

	private boolean haveChanged(Classifier classifier) {
		if((m_classifier == null) || (classifier == null))  {
			return false;
		}
		if(!m_classifier.toString().equals(classifier.toString())) {
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getPhase()
	 */
	public int getPhase() {
		if(m_classification < m_numExamples) {
			return 2;
		}
		return 1;
	}
	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getAgentTypString()
	 */
	public String getStrategyString() {
		return "Selbstregulierende Kommunikation";
	}
	
	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return false;
	}		
}

