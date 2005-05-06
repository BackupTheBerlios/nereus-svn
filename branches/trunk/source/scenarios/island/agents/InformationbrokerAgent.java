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
import java.util.Hashtable;
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
import speachacts.UnRegisterSpeachact;
import utils.Id;
import visualisation.IVisualisation;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Der Agent implementiert den Informationbroker-Agenten der Strategie 
 * Informationbroker aus der Diplomarbeit.
 * 
 * @author Daniel Friedrich
 */
public class InformationbrokerAgent
	extends IslandScenarioAgent
	implements IIslandScenarioAgent,
				IMessagingAgent {
						
	
	/**
	 * Menge der eigenen Beispiele
	 */
	protected Instances m_ownExamples;
		
	/**
	 * Registrierte Agenten.
	 */
	protected Vector m_registeredAgents = new Vector();	
		
	/**
	 * Speicher für die neuen Trainingsbeispiele für die Agenten
	 */	
	protected Hashtable m_mailboxes = new Hashtable();
		
	/**
	 * Liste aller Agenten die sich in dieser Runde schon mit ihrer Anfrage 
	 * gemeldet haben.
	 */
	protected int m_numOfAgentWhoAsked = 0;
	
	/**
	 * Gibt an, ob in der letzten Runde ein Beispiel erstellt wurde.
	 */
	private boolean m_createLastRoundAExample = false;
	
	/**
	 * Gibt an, ob die Message zum Beenden der Kommunikation beendet ist.
	 */
	protected boolean m_stopCommunicationMessageSended = false;
	
	/**
	 * Anzahl der Registrierten Agenten
	 */
	private int m_numOfRegisteredAgents = 0;
	
	/**
	 * Gibt an, ob die antwort aller Agenten geloggt wird. 
	 */
	private boolean m_haveLoggedThatAllAgentsAnswered = false;
	
	/**
	 * Konstruktor.
	 */
	public InformationbrokerAgent() {
		super();
		m_isALearningAgent = true;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public InformationbrokerAgent(
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
	public InformationbrokerAgent(Id id, String name) {
		super(id, name);
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
			// Bestimme Kommschranke, wenn in zweiten Runde
			if(m_roundCounter == 2) {
				if(m_registeredAgents.size() > 0) {
					m_communication = m_registeredAgents.size() * 8;
				}
			}
			// Liste der Registrierten Agenten bereinigen 
			this.cleanUpRegisteredAgentList();
			
			if(m_createLastRoundAExample) {
				Enumeration enum = m_mailboxes.keys();
				while(enum.hasMoreElements()) {
					Object key = enum.nextElement();
					Vector box = (Vector)m_mailboxes.get(key);
					box.addElement(m_examples.lastInstance());
					m_mailboxes.put(key,box);
				}
			}
			
			// Kommunizieren oder nicht ?
			if(this.shouldICommunicate()) {
				if(m_roundCounter == 1) {
					this.sendNotificationMessage();
				}else {
					this.sendStopCommunicationMessage();
				}
			}
			// Attribute wahrnehmen
			Instance attributes = 
				this.getScenarioHandler().getAttributesFromPlace(
				this.getActionKey(),m_id);
			// Neue Beispiele erhalten	
			if(m_newExamples) {
				// Hypothese bilden
				this.buildHypothesis(m_examples);
			}			
			// Ist Platz schon ausgebeutet			
			if(!this.getScenarioHandler().isPlaceExplored(this.getActionKey(),m_id)) {
				if(this.shouldIExploreThePlace(attributes)) {
					// graben und auswerten
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
					m_createLastRoundAExample = true;	
			 	}else {
			 		m_createLastRoundAExample = false;
			 	}
			} // if Platz ausgebeutet
			// Bewegen oder auch nicht, wenn zuwenig Energie da ist.
			this.move();	
			m_haveLoggedThatAllAgentsAnswered = false;
			// Liste der Agent die angefragt haben, wieder auf Null setzen.
			m_numOfAgentWhoAsked = 0;								
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}

	}
	
	/**
	 * Gibt zurück, ob der Agent kommunizieren soll.
	 * 
	 * Dies ist immer dann der Fall, wenn eine der beiden folgenden Bedingungen 
	 * erfüllt ist:
	 * - der Agent befindet sich in der ersten Runde
	 * - die Anzahl der Trainingsbeispiele ist größer als die 
	 * 	 Kommunikationschwelle (m_communication) und der Agent hat die Stop-
	 * 	 Communication Nachricht an alle Agenten noch nicht versendet.
	 * 
	 * @return boolean
	 */
	private boolean shouldICommunicate() {		
		if((m_roundCounter == 1) 
			|| ((m_examples != null) 
				&& (m_examples.numInstances() > m_communication)
				&& !m_stopCommunicationMessageSended)) {
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see scenario.communication.IMessagingAgent#receiveMessage(speachacts.Speachact)
	 */
	public Speachact receiveMessage(Speachact act) {
		try {
			Speachact answer = null;
			if(act instanceof UnRegisterSpeachact) {
				// Agent will sich abmelden, Agent aus Liste austragen
				m_registeredAgents.remove(act.getSender());
				m_numOfRegisteredAgents--;
				// Mailbox löschen
				m_mailboxes.remove(act.getSender().toString());
				answer = (this.getScenarioHandler().createSpeachAct(
					DenyAnswerSpeachact.class));
				answer.setContent(null);
				answer.setReceiver(act.getSender());
				answer.setSender(m_id);	
				return answer;
			}else if(act instanceof AskForExamplesSpeachact) {
				// Antwort vorbereiten
				 answer = this.getScenarioHandler().createSpeachAct(
					AnswerExamplesSpeachact.class);
				answer.setSender(m_id);
				answer.setReceiver(act.getSender());
				// übergebenes Beispiel abspeichern in allen Behältern.
				Object content = act.getContent();
				if((content != null) && (content instanceof Instance)) {
					Instance newInstance = (Instance)content;
					// loggen
					try {
						this.getScenarioHandler().log(
							m_id,
							IVisualisation.CommMsg,
							"Der Informationbroker ("
							+ m_id.toString() 
							+ ") hat vom Agenten ("
							+ act.getSender().toString()
							+ ") das folgende Beispiel erhalten:  "
							+ newInstance.toString());
					}catch(Exception e) {
						e.printStackTrace(System.out);	
					} 						
					// der eigenen Erfahrung hinzufügen
					m_examples.add(newInstance);
					for(int i=0; i < m_registeredAgents.size();i++) {
						// der Reihe nach durch gehen
						Id agentId = (Id)m_registeredAgents.get(i);
						// nicht an sich selbst verteilen
						if(!agentId.equals(act.getSender())) {
							 // Post einwerfen
							 Vector tmpExperience = (Vector)m_mailboxes.get(
							 	agentId.toString());
							 tmpExperience.add(newInstance);
							 m_mailboxes.put(agentId.toString(),tmpExperience);
						}
					}
				}
				// Agent in List der Agenten eintragen die angefragt haben
				m_numOfAgentWhoAsked++;
				// warten bis alle Agenten angefragt haben.
				while((m_numOfAgentWhoAsked < m_numOfRegisteredAgents)
					&& (!m_stopCommunicationMessageSended)) {}
				if(!m_haveLoggedThatAllAgentsAnswered) {
					try {
						this.getScenarioHandler().log(
							m_id,
							IVisualisation.CommMsg,
							"Der Informationbroker ("
							+ m_id.toString() 
							+ ") hat von allen registrierten Agenten "
							+ "Anfragen erhalten");
						m_haveLoggedThatAllAgentsAnswered = true;	
					}catch(Exception e) {
						e.printStackTrace(System.out);	
					}		
				} 						
				// Mailbox zurücksenden
				Vector examples = new Vector();
				Vector mailbox = 
					(Vector)m_mailboxes.get(act.getSender().toString());
				for(int i=0; i < mailbox.size();i++) {
					examples.add(mailbox.get(i));
				}
				mailbox.clear();
				m_mailboxes.put(act.getSender().toString(), mailbox);
				answer.setContent(examples);
				((AnswerExamplesSpeachact)answer).answersMoreThanOneExample();
				return answer;				
			}else {
				answer = (this.getScenarioHandler().createSpeachAct(
					DenyAnswerSpeachact.class));
				answer.setContent(null);
				answer.setReceiver(act.getSender());
				answer.setSender(m_id);
				return answer;
			}
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}
	
	/**
	 * Sendet eine Notification-Nachricht an alle Agenten.
	 */
	private void sendNotificationMessage() {
		try {
			// Hole Liste aller erreichbaren Agenten
			Vector reachableAgents = 
				this.getScenarioHandler().getForCommunicationReachableAgents(
					this.getActionKey(), 
					m_id);
			StringBuffer agentIds = new StringBuffer("");		
			/*
			 * Sende an jede die Notification Nachricht und werte dessen Antwort 
			 * aus
			 */
			for(int i=0; i < reachableAgents.size(); i++) {
				Id agentId = (Id)reachableAgents.get(i);
				// loggen
				agentIds.append(agentId.toString() + " "); 	
				Speachact act = this.getScenarioHandler().createSpeachAct(
					NotificationSpeachact.class);
				act.setReceiver(agentId);
				act.setSender(m_id);
				Speachact answer = 
					this.getScenarioHandler().sendMessage(this.getActionKey(), act);
				if(answer instanceof RegisterSpeachact) {
					// Agent in Registrierung eintragen.
					m_registeredAgents.addElement(agentId);
					m_numOfRegisteredAgents++;
					// Mailbox für den Agenten anlegen.
					m_mailboxes.put(agentId.toString(),new Vector());	
				}					
			}
			try {
				this.getScenarioHandler().log(
					m_id,
					IVisualisation.CommMsg,
					"Der Informationbroker hat die Notifaction-Nachricht "
					+ "erfolgreich an die folgenden Agenten gesendet: " 
					+ agentIds.toString());
			}catch(Exception e) {
				e.printStackTrace(System.out);	
			} 					
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Sendet eine StopCommunication Nachricht an alle Registrierten Agenten.
	 */
	private void sendStopCommunicationMessage() {
		try {
			StringBuffer agentIds = new StringBuffer("");
			/*
			 * Laufe über die Liste aller registrierten Agenten und schicke 
			 * diesen die Nachricht
			 */ 
			m_stopCommunicationMessageSended = true; 
			for(int i=0; i < m_registeredAgents.size(); i++) {
				Id agentId = (Id)m_registeredAgents.get(i); 	
				// loggen
				agentIds.append(agentId.toString() + " "); 					
				Speachact act = this.getScenarioHandler().createSpeachAct(
					StopCommunicationSpeachact.class);
				act.setReceiver(agentId);
				act.setSender(m_id);
				Speachact answer = 
					this.getScenarioHandler().sendMessage(
					this.getActionKey(), 
					act);
				// Antwort ist egal.								
			}
			
			try {
				this.getScenarioHandler().log(
					m_id,
					IVisualisation.CommMsg,
					"Der Informationbroker hat die StopCommunication-Nachricht "
					+ " erfolgreich an die folgenden Agenten gesendet: " 
					+ agentIds.toString());
			}catch(Exception e) {
				e.printStackTrace(System.out);	
			} 				
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}	
	
	/**
	 * Löscht die Agenten aus der Liste der registrierten, die aus dem Spiel sind.
	 */
	private void cleanUpRegisteredAgentList() {
		try {
			/*
			 * Alle Agenten aus der Liste der registrierten Agenten rauswerfen, die
			 * vom Szenario aus dem spiel genommen wurden und sich nicht abgemeldet
			 * haben.
			 */
			Vector reachableAgents =  
				this.getScenarioHandler().getForCommunicationReachableAgents(
					this.getActionKey(),m_id);
			if(!(m_registeredAgents.size() == reachableAgents.size())) {
				for(int i=0; i < m_registeredAgents.size(); i++) {
					Id agentId = (Id)m_registeredAgents.get(i);
					if(!reachableAgents.contains(agentId)) {
						m_registeredAgents.remove(agentId);		 
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getPhase()
	 */
	public int getPhase() {
		if(m_classification-1 < m_numExamples) {
			if(m_stopCommunicationMessageSended) {
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
		return "Informationbroker";
	}
}
