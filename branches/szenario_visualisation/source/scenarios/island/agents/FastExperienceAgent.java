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
import java.util.Vector;

import scenario.communication.IMessagingAgent;
import scenario.island.IslandScenarioAgent;
import simulator.AbstractScenarioHandler;
import speachacts.AnswerExamplesSpeachact;
import speachacts.AskForExamplesSpeachact;
import speachacts.AskSpeachact;
import speachacts.DenyAnswerSpeachact;
import speachacts.Speachact;
import utils.Id;
import visualisation.IVisualisation;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Der Agent implementiert die Strategie Schnelles Erfahrungswachstum, aus
 * der Diplomarbeit.
 * 
 * @author Daniel Friedrich
 */
public abstract class FastExperienceAgent
	extends IslandScenarioAgent
	implements IIslandScenarioAgent,
				IMessagingAgent {
	
	/**
	 * Menge der eigenen Beispiele 
	 */
	protected Instances m_ownExamples;

	/**
	 * Menge aller Beispiele
	 */
	protected Instances m_allExamples;	
	
	/**
	 * Konstruktor.
	 */
	public FastExperienceAgent() {
		super();
		m_isALearningAgent = true;
	}	
	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public FastExperienceAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public FastExperienceAgent(Id id, String name) {
		super(id, name);
		m_classification = 15;
		m_communication = 40;
		m_isALearningAgent = true;
	}	
	
	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return false;
	}

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#simulate()
	 */
	public void simulate() {
		// Runde hochzählen
		m_roundCounter++;
		
		if(this.shouldICommunicate()) {
			this.communicate();
		}

		// Bestimme Kommschranke, wenn in zweiten Runde
		if(m_roundCounter == 2) {
			try {
			m_communication =
				this.getScenarioHandler().getForCommunicationReachableAgents(
					this.getActionKey(),
					m_id).size()* 8;
			}catch(Exception e) {
				System.out.println("Fehler");
			}
		}		
		
		try {
			// Attribute wahrnehmen
			Instance attributes = 
				this.getScenarioHandler().getAttributesFromPlace(
					this.getActionKey(),
					m_id);
			// Neue Beispiele erhalten	
			if(m_newExamples) {
				// Hypothese bilden
				this.buildHypothesis(m_allExamples);
			}
			// Wurde an dem Platz schon gegraben ?
			if(!this.getScenarioHandler().isPlaceExplored(this.getActionKey(),m_id)) {
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
						m_allExamples = new Instances(attributes.dataset(),0);
						m_ownExamples = 
							new Instances(attributes.dataset(),0);						
					}					
					// abspeichern der gewonnenen Erfahrung.
					Vector tmpVector = new Vector();
					tmpVector.addElement(m_ownExamples);
					tmpVector.addElement(m_allExamples);
					this.addExample(
						attributes,
						new Boolean(cellFound),
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
	 * @see scenario.island.IslandScenarioAgent#shouldIClassify()
	 */
	protected boolean shouldIClassify() {
		if((m_communication == 0) || (m_classification == 0)) {
			// Eigentlich inkorrekt, deshalb warnung ausgeben.
			System.out.println(
				"m_classification: "
				 + m_classification 
				 + " und m_communication: " 
				 + m_communication);
		}
		if((m_allExamples != null) 
			&& (m_allExamples.numInstances() > m_classification)){
			return true;
		}
		return false;
	}	
	
	/**
	 * Der Agent fragt bei den anderen Agenten nach neuen Beispielen nach.
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
				// log
				this.getScenarioHandler().log(
					m_id,
					IVisualisation.StateMsg,
					"Der Agent "
					+ m_id
					+ " fragt den Agenten "
					+ agentId
					+ " nach Beispielen.");
				Speachact sendact = 
					this.getScenarioHandler().createSpeachAct(
						AskForExamplesSpeachact.class);	
				sendact.setSender(m_id);
				sendact.setReceiver(agentId);
				sendact.setContent(null);	
				Speachact act = this.getScenarioHandler().sendMessage(
					this.getActionKey(),
					sendact);
				if(act instanceof AnswerExamplesSpeachact) {
					if(act.getContent() != null) {
						/*
						 * Hole das übermittelte Objekt heraus, teste ob es 
						 * ungleich null ist und speichere die Instance dann 
						 * ab. (Es wird jeweils nur eine Instance und es werden
						 * nur Instancen versendet in der Strategie)
						 */
						Object obj = act.getContent();
						if((obj != null ) && (obj instanceof Instance)) {
							m_allExamples.add((Instance)obj);
							m_numExamples++;
						}
					}
				}
					
								
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
	private boolean shouldICommunicate() {
		if((m_roundCounter > 1) 
			&& (m_allExamples != null) 
			&& (m_allExamples.numInstances() < m_communication) ){
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
			 if(act instanceof AskSpeachact) {
				 if(act instanceof AskForExamplesSpeachact) {
					 answer = this.getScenarioHandler().createSpeachAct(
						 AnswerExamplesSpeachact.class);
					 if(m_ownExamples == null) {
						 answer.setContent(null);
						// loggen
						try {
							this.getScenarioHandler().log(
								m_id,
								IVisualisation.CommMsg,
								"Der Agent "
								+ m_id.toString() 
								+ " hat vom Agenten ("
								+ act.getReceiver().toString()
								+") eine Anfrage nach einem Beispiel erhalten. "
								+ " Er konnte allerdings keines zurücksenden.");
						}catch(Exception e) {
							e.printStackTrace(System.out);	
						}						 
					 }else {
					 	// Es werden nur einzelne Beispiele versendet.
					 	answer.setContent(new Instance(m_ownExamples.lastInstance()));
						// loggen
						try {
							this.getScenarioHandler().log(
								m_id,
								IVisualisation.CommMsg,
								"Der Agent "
								+ m_id.toString() 
								+ " hat vom Agenten ("
								+ act.getReceiver().toString()
								+") eine Anfrage nach einem Beispiel erhalten. "
								+ " Folgendes Beispiel hat er an den Agenten "
								+ " zurückgesandt: "
								+ m_ownExamples.lastInstance().toString());
						}catch(Exception e) {
							e.printStackTrace(System.out);	
						}					 	
					 }	
					// Berechnen ob die Antwort wirklich gesendet werden soll.
					if(!this.couldIRenderTheAnswer(answer,0.0,0.0)) {
						answer = null;					 	
					}				
				 }
			 }
			 if(answer == null) {
				 answer = (this.getScenarioHandler().createSpeachAct(
				DenyAnswerSpeachact.class));
				answer.setContent(null);
				answer.setReceiver(act.getSender());
				answer.setSender(m_id);
			 }
			 answer.setSender(m_id);
			 answer.setReceiver(act.getSender());
		 }catch(Exception e) {
			 e.printStackTrace(System.out);
		 }
		 return answer;
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
	
	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getAgentTypString()
	 */
	public String getStrategyString() {
		return "Schneller Erfahrungsaustausch";
	}
	

}
