/*
 * Created on 02.09.2003
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
import speachacts.DenyAnswerSpeachact;
import speachacts.NotificationSpeachact;
import speachacts.RegisterSpeachact;
import speachacts.Speachact;
import speachacts.StopCommunicationSpeachact;
import utils.Id;
import visualisation.IVisualisation;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Der Agent implementiert einen der Sammelagenten der Informationbroker 
 * Strategie aus der Diplomarbeit.
 * 
 * @author Daniel Friedrich
 */
public abstract class IBCollectorAgent
	extends IslandScenarioAgent
	implements IIslandScenarioAgent,
				IMessagingAgent {

	/**
	 * Menge der eigenen Beispiele
	 */
	protected Instances m_ownExamples;	
	
	/**
	 * Id des Informationbrokers, zur Kommunikation mit ihm.
	 */	
	protected Id m_infobroker;	
	
	/**
	 * Gibt an ob der Agent beim Infobroker registriert ist.
	 */
	protected boolean m_registered = false;
	
	/**
	 * Gibt an ob der Informationbroker existiert.
	 */
	protected boolean m_infoBrokerExists = false;
	
	/**
	 * Gibt an, ob die Kommunikation bereits gestoppt wurde.
	 */
	protected boolean m_communicationIsStopped = false;

	/**
	 * Konstruktor.
	 */
	public IBCollectorAgent() {
		super();
		/*
		 * Kommunikationsschwelle auf null setzen, wenn der Informationbroker 
		 * später mitteilt das die Kommunikationschwelle erreicht wurde, 
		 * wird der Wert auf 1 gesetzt.
		 */ 
		//m_communication = 0;
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public IBCollectorAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		/*
		 * Kommunikationsschwelle auf null setzen, wenn der Informationbroker 
		 * später mitteilt das die Kommunikationschwelle erreicht wurde, 
		 * wird der Wert auf 1 gesetzt.
		 */ 
		//m_communication = 0;
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public IBCollectorAgent(Id id, String name) {
		super(id, name);
		/*
		 * Kommunikationsschwelle auf null setzen, wenn der Informationbroker 
		 * später mitteilt das die Kommunikationschwelle erreicht wurde, 
		 * wird der Wert auf 1 gesetzt.
		 */ 
		//m_communication = 0;
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
		try {
			// Runde hochzählen
			m_roundCounter++;
			if(this.haveIEnoughEnergyForThisRound()) {
				if(this.shouldICommunicate()) {
					this.communicate();
				}
				// Attribute wahrnehmen
					Instance attributes = 
					this.getScenarioHandler().getAttributesFromPlace(
						this.getActionKey(),m_id);
				// Neue Beispiele erhalten	
				if((m_newExamples) && (m_examples.numInstances() > 0)) {
					// Hypothese bilden
					this.buildHypothesis(m_examples);
					//m_classifier.buildClassifier(m_examples);
				}			
				// Ist Platz schon ausgebeutet			
				if(!this.getScenarioHandler().isPlaceExplored(
					this.getActionKey(),
					m_id)) {
					if(this.shouldIExploreThePlace(attributes)) {
						boolean cellFound = this.exploreThePlace();
						// Erst noch Erfahrungspeicher initialisieren
						if(m_roundCounter == 1) {
							if(cellFound) {
								attributes.setClassValue(1.0);
							}else {
								attributes.setClassValue(0.0);
							}
							m_examples = new Instances(attributes.dataset(),0);
							m_ownExamples = 
								new Instances(attributes.dataset(),0);						
						}
						Vector tmpVector = new Vector();
						tmpVector.addElement(m_examples);
						tmpVector.addElement(m_ownExamples);
						this.addExample(
							attributes,
							new Boolean(cellFound),
							tmpVector);
					} // soll gegraben werden
				} // if Platz ausgebeutet
			}else {
				// Beim Infobroker noch abmelden
				if(m_registered) {
					this.unregister();
				}
			}	
			// Bewegen wenn möglich.
			this.move();
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Meldet den Agenten beim Informationbroker ab.
	 */
	private void unregister() {
		try {
			Speachact speachact = 
				this.getScenarioHandler().createSpeachAct(
					NotificationSpeachact.class);
			speachact.setSender(m_id);
			speachact.setReceiver(m_infobroker);
			Speachact answer = 
				this.getScenarioHandler().sendMessage(
					this.getActionKey(), 
					speachact);	
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Managed die Kommunikation mit dem Informationbroker.
	 */
	private void communicate() {
		try {
			// Hole alle erreichbaren Agenten
			Vector reachableAgents = 
				this.getScenarioHandler().getForCommunicationReachableAgents(
					this.getActionKey(),
					this.getId());
			// Kontrolliere ob der Informationbroker noch existiert.
			if(reachableAgents.contains(m_infobroker)) {	
				Speachact sendact = 
					this.getScenarioHandler().createSpeachAct(
						AskForExamplesSpeachact.class);	
				sendact.setSender(m_id);
				sendact.setReceiver(m_infobroker);
				// mit Inhalt füllen
				sendact.setContent(null);	
				if(m_newExamples) {
				   sendact.setContent(m_examples.lastInstance());
				}
				// Versenden	
				Speachact answer = this.getScenarioHandler().sendMessage(
					this.getActionKey(),
					sendact);
				if(answer instanceof AnswerExamplesSpeachact) {
					StringBuffer contentString = new StringBuffer("");
					if(answer.getContent() != null) {
						// Hole Object heraus
						Object obj = answer.getContent();
						if((obj != null) && (obj instanceof Vector)) {
							Vector tmpVector = new Vector();
							tmpVector.addElement(m_examples);
							tmpVector.addElement(m_ownExamples);							
							Enumeration instances = ((Vector)obj).elements();
							while(instances.hasMoreElements()) {
								Instance tmpInstance = 
									(Instance)instances.nextElement();								
								this.addExample(
									tmpInstance,
									null,
									tmpVector);					
							}
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
		/*
		 * Nur Kommuniziern wenn dafür und für das Versenden der Abmelde-
		 * Nachricht genug Energie da ist
		 */
		try { 
			if((m_examples == null) || (m_examples.numInstances() == 0)) {
				return false;	
			}
			Speachact act = 
				this.getScenarioHandler().createSpeachAct(AskForExamplesSpeachact.class);
			act.setContent(m_examples.lastInstance());
			double costs = act.calculateCosts();	
			// Wenn zuwenig Energie vorhanden ist, dann beim Broker abmelden.
			if(this.getScenarioHandler().getMyEnergyValue(
				this.getActionKey(), 
				m_id) 
				< (costs + m_communicationCosts + m_exploringCosts)) {
				// Zuwenig Energie abmelden.
				this.unregister();
				return false;		
			}
		}catch(Exception e) {
			e.printStackTrace(System.out);		
		}
		if((m_roundCounter > 1) 
			&& (m_infoBrokerExists) 
			&& (!m_communicationIsStopped)){
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see scenario.communication.IMessagingAgent#receiveMessage(speachacts.Speachact)
	 */
	public Speachact receiveMessage(Speachact act) {
	 	try {
	 		/*
	 		 * Kontrolliere ob das eine Notification-Nachricht ist, wenn ja 
	 		 * registriere dich beim Infobroker. Ist es keine Notification 
	 		 * Nachricht teste, ob es eine StopCommunication-Nachricht ist 
	 		 * ansonsten sende gleich eine Deny-Nachricht. 
	 		 */
	 		if(act instanceof NotificationSpeachact) {
	 			// Registrieren
	 			Speachact answer = this.getScenarioHandler().createSpeachAct(
	 			 	RegisterSpeachact.class);
	 			// Informationbroker setzen	
				m_infobroker = act.getSender();
				m_infoBrokerExists = true;
	 			answer.setReceiver(m_infobroker);
	 			answer.setSender(m_id);
	 			 
				// loggen
				try {
					this.getScenarioHandler().log(
						m_id,
						IVisualisation.CommMsg,
						"Der Agent "
						+ m_id.toString() 
						+ " hat vom Informationbroker ("
						+ m_infobroker.toString()
						+ ") die Notification-Nachricht erhalten und registriert " 
						+ " sich nun bei diesem.");
				}catch(Exception e) {
					e.printStackTrace(System.out);	
				} 		 			 
	 			return answer;	
	 		}else if(act instanceof StopCommunicationSpeachact) {
	 			/*
	 			 * Setze das interne Flag, die Kommunikationschwelle ist 
	 			 * erreicht. Schicke dann eine Deny-Antwort.
	 			 */
				m_communicationIsStopped = true; 
				// loggen
				try {
					this.getScenarioHandler().log(
						m_id,
						IVisualisation.CommMsg,
						"Der Agent "
						+ m_id.toString() 
						+ " hat vom Informationbroker ("
						+ m_infobroker.toString()
						+") die StopCommunication-Nachricht erhalten.");
				}catch(Exception e) {
					e.printStackTrace(System.out);	
				}
	 		} 					
			Speachact answer = (this.getScenarioHandler().createSpeachAct(
				DenyAnswerSpeachact.class));
				answer.setContent(null);
				answer.setReceiver(act.getSender());
				answer.setSender(m_id);
			return answer;			
	 	}catch(Exception e) {
	 		e.printStackTrace(System.out);
	 	}
	 	return null;
	}
	
	/**
	 * Kontrolliert, ob der Agent genügend Energie für die Runde besitzt.
	 * 
	 * Damit ein Agent über genügend Energie für eine Runde verfügt muss er
	 * mindests Graben und Kommunizieren können.
	 *  
	 * @return
	 */
	protected boolean haveIEnoughEnergyForThisRound() {
		try {
			if(this.getScenarioHandler().getMyEnergyValue(
				this.getActionKey(), 
				m_id) 
				> (m_exploringCosts + m_communicationCosts)) {
				return true;
			}	
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}		
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getPhase()
	 */
	public int getPhase() {
		if(m_classification-1 < m_numExamples) {
			// Klassifikationschwelle erreicht.
			if(m_communicationIsStopped) {
				// Kommunikationschwelle erreicht
				return 3;
			}
			return 2;
		}
		return 1;
	}
	
	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getAgentTypString()
	 */
	public String getStrategyString() {
		return "Informationsbroker";
	}
}
