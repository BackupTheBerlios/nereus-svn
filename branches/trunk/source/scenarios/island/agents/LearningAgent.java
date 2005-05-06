/*
 * Created on 24.09.2003
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

import java.util.LinkedList;
import java.util.Vector;

import scenario.communication.IMessagingAgent;
import simulator.AbstractScenarioHandler;
import speachacts.AnswerHypothesisSpeachact;
import speachacts.AskForHypothesisSpeachact;
import speachacts.DenyAnswerSpeachact;
import speachacts.NotificationSpeachact;
import speachacts.RegisterSpeachact;
import speachacts.Speachact;
import speachacts.StopCommunicationSpeachact;
import speachacts.UnRegisterSpeachact;
import utils.Id;
import visualisation.IVisualisation;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Superklasse für den Learning Agent der Strategie Zentrales Lernen aus der
 * Diplomarbeit. Der Lerning Agent ist der einzige lernende Agent in dem 
 * Agententeam, er wird von den Collector Agents (Sammelagenten) mit Beispielen
 * versorgt und liefert ihnen dafür die Hypothese. Die Subklassen müssen den 
 * Agenten noch mit einem beliebigen Lernverfahren versehen.
 * 
 * @author Daniel Friedrich
 */
public class LearningAgent
	extends IslandScenarioAgent
	implements IIslandScenarioAgent, IMessagingAgent {

	/**
	 * Gibt an on in der Runde schon eine Hypothese erstellt wurde.
	 */
	private boolean m_hypothesisWasBuilded = false;

	/**
	 * Gibt an ob bereits die Kommunikation eingestellt wurde.
	 */
	private boolean m_stopCommunicationMsgWasSend = false;

	/**
	 * Anzahl der registrierten Agenten.
	 */
	private int m_numOfRegisteredAgents = 0;

	/**
	 * Anzahl der Agenten die für eine Hypothese angefragt haben.
	 */
	private int m_numOfAgentsWhoAskedForHypothesis = 0;

	/**
	 * Die Registrierten Agenten.
	 */
	private Vector m_registeredAgents = new Vector();

	/**
	 * Konstruktor
	 */
	public LearningAgent() {
		super();		
		m_isALearningAgent = true;
		m_classification = 15;
		m_communication = 40;		
	}

	/**
	 * Konstruktor
	 * 
	 * @param name
	 * @param handler
	 */
	public LearningAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = true;
		m_classification = 15;
		m_communication = 40;
	}

	/**
	 * Konstruktor
	 * 
	 * @param id
	 * @param name
	 */
	public LearningAgent(Id id, String name) {
		super(id, name);
		m_isALearningAgent = true;
		m_classification = 15;
		m_communication = 40;		
	}

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

			/*
			 * Die Menge der registrierten Agenten von den ausgeschiedenen 
			 * bereinigen.
			 */
			this.cleanUpRegisteredAgentList();

			// Attribute wahrnehmen
			Instance attributes =
				this.getScenarioHandler().getAttributesFromPlace(
					this.getActionKey(),
					m_id);
			// Feststellen ob der Platz bereist ausgebeutet ist 
			boolean isPlaceExplored =
				this.getScenarioHandler().isPlaceExplored(
					this.getActionKey(), 
					m_id);

			// Soll kommuniziert werden?
			if (this.shouldICommunicate()) {
				// ja				
				if (m_roundCounter == 1) {
					// Notifcation-Message senden
					this.sendNotificationMessage();
				} else {
					// Stop Communication Message senden
					this.sendStopCommunicationMessage();
				}
			}
			m_newExamples = false;

			/*
			 * solange warten bis sich alle registrierenten Agenten gemeldet
			 * ausser, die Agenten befinden sich in der ersten Phase, dann 
			 * kann auch gleich geantwortet werden. 
			 */
			while ((m_numOfAgentsWhoAskedForHypothesis
				< m_numOfRegisteredAgents)
				&& (m_roundCounter != 1)
				&& (!m_stopCommunicationMsgWasSend)) {
			}

			// Erstelle eine Hypothese
			if (this.shouldIBuildHypothesis()) {
				this.buildHypothesis(m_examples);
				m_hypothesisWasBuilded = true;
			}

			// Soll gegraben werden ?
			if ((!isPlaceExplored)
				&& (this.shouldIExploreThePlace(attributes))) {
				// ja -> graben
				boolean cellFound = this.exploreThePlace();
				// Neues Wissen verarbeiten
				if (m_examples == null) {
					if (cellFound) {
						attributes.setClassValue(1.0);
					} else {
						attributes.setClassValue(0.0);
					}
					m_examples = new Instances(attributes.dataset(), 0);
				}
				Vector tmpVector = new Vector();
				tmpVector.addElement(m_examples);
				this.addExample(attributes, new Boolean(cellFound), tmpVector);
				m_newExamples = true;
			}
			// Zielliste erstellen und bewegen
			this.move();
			// Anzahl der anfragenden Agenten einer Runde wieder zurücksetzen
			m_numOfAgentsWhoAskedForHypothesis = 0;
			m_hypothesisWasBuilded = false;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/* (non-Javadoc)
	 * @see scenario.communication.IMessagingAgent#receiveMessage(speachacts.Speachact)
	 */
	public Speachact receiveMessage(Speachact act) {
		Speachact answer = null;
		try {
			if (act instanceof UnRegisterSpeachact) {
				// Endregistrierung vornehmen
				if (act.getSender() != null) {
					m_registeredAgents.remove(act.getSender());
					m_numOfRegisteredAgents--;
				}
				// loggen
				try {
					this.getScenarioHandler().log(
						m_id,
						IVisualisation.CommMsg,
						"Der Agent ("
							+ act.getSender().toString()
							+ " meldet sich beim Learning Agent ab.");
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}
				answer =
					this.getScenarioHandler().createSpeachAct(
						DenyAnswerSpeachact.class);
				answer.setSender(m_id);
				answer.setReceiver(act.getSender());
				answer.setContent(null);
			} else if (
				(act instanceof AskForHypothesisSpeachact)
					&& (this
						.couldIRenderTheAnswer(
							act,
							m_movingCosts,
							(double) m_numOfRegisteredAgents))
					&& (!m_stopCommunicationMsgWasSend)) {
				if ((act.getContent() != null)
					&& (act.getContent() instanceof Instance)) {
					// Beispiel enthalten, dieses wegspeichern
					Instance tmpInstance = (Instance) act.getContent();
					if (m_examples == null) {
						m_examples = new Instances(tmpInstance.dataset(), 0);
					}
					m_examples.add(tmpInstance);
					m_numExamples++;
				}
				// Agent als anfragenden Agenten melden
				m_numOfAgentsWhoAskedForHypothesis++;

				// Darauf warten das eine Hypothese erstellt wurde.	
				while ((m_numExamples > m_classification)
					&& (!m_hypothesisWasBuilded)) {
				}

				// Antwort versenden
				answer =
					this.getScenarioHandler().createSpeachAct(
						AnswerHypothesisSpeachact.class);
				answer.setReceiver(act.getSender());
				answer.setSender(m_id);
				if (m_hypothesisWasBuilded) {
					answer.setContent(
						(Classifier.makeCopies(m_classifier, 1))[0]);
				} else {
					answer.setContent(null);
				}
			} else {
				if ((!this
					.couldIRenderTheAnswer(
						act,
						0.0,
						(double) m_numOfRegisteredAgents))
					&& (act instanceof AskForHypothesisSpeachact)) {
					m_stopCommunicationMsgWasSend = true;
					Speachact pact =
						this.getScenarioHandler().createSpeachAct(
							AnswerHypothesisSpeachact.class);
					double energy =
						pact.calculateAnswerCosts()
							* (double) m_numOfRegisteredAgents;
					double actualEnergy =
						this.getScenarioHandler().getMyEnergyValue(
							this.getActionKey(),
							m_id);
				}
				answer =
					this.getScenarioHandler().createSpeachAct(
						DenyAnswerSpeachact.class);
				answer.setSender(m_id);
				answer.setReceiver(act.getSender());
				answer.setContent(null);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		// Antwort versenden
		return answer;
	}

	/**
	 * Gibt an, ob der Agent kommunizieren soll.
	 * 
	 * @return
	 */
	private boolean shouldICommunicate() {
		// In der ersten Runde die Notification-Message senden.
		if (m_roundCounter == 1) {
			return true;
		}
		/*
		 * Wenn genug Beispiele vorhanden sind die StopCommunication Nachricht
		 * senden.
		 */
		if ((m_numExamples > m_communication)
			&& (!m_stopCommunicationMsgWasSend)) {
			return true;
		}
		return false;
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
			// Sich selbst entfernen
			reachableAgents.remove(m_id);

			/*
			 * Sende an jeden die Notification Nachricht und werte dessen Antwort 
			 * aus
			 */
			for (int i = 0; i < reachableAgents.size(); i++) {
				Id agentId = (Id) reachableAgents.get(i);
				Speachact act =
					this.getScenarioHandler().createSpeachAct(
						NotificationSpeachact.class);
				act.setReceiver(agentId);
				act.setSender(m_id);
				Speachact answer =
					this.getScenarioHandler().sendMessage(
						this.getActionKey(), 
						act);
				if (answer instanceof RegisterSpeachact) {
					// Agent in Registrierung eintragen.
					m_numOfRegisteredAgents++;
					m_registeredAgents.add(act.getSender());
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/**
	 * Sendet eine StopCommunication Nachricht an alle Registrierten Agenten.
	 */
	private void sendStopCommunicationMessage() {
		try {
			/*
			 * Laufe über die Liste aller registrierten Agenten und schicke 
			 * diesen die Nachricht
			 */
			for (int i = 0; i < m_registeredAgents.size(); i++) {
				Id agentId = (Id) m_registeredAgents.get(i);
				Speachact act =
					this.getScenarioHandler().createSpeachAct(
						StopCommunicationSpeachact.class);
				act.setReceiver(agentId);
				act.setSender(m_id);
				Speachact answer =
					this.getScenarioHandler().sendMessage(
						this.getActionKey(), 
						act);
				// Antwort ist egal .				
			}
			// Ausführung vermerken
			m_stopCommunicationMsgWasSend = true;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getPhase()
	 */
	public int getPhase() {
		if (m_classification - 1 < m_numExamples) {
			if (m_stopCommunicationMsgWasSend) {
				return 3;
			}
			return 2;
		}
		return 1;
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
			LinkedList notReachAbleAgents = new LinkedList();

			Vector reachableAgents =
				this.getScenarioHandler().getForCommunicationReachableAgents(
					this.getActionKey(),
					m_id);
			if (!(m_numOfRegisteredAgents == reachableAgents.size())) {
				for (int i = 0; i < m_numOfRegisteredAgents; i++) {
					Id agentId = (Id) m_registeredAgents.get(i);
					if (!reachableAgents.contains(agentId)) {
						notReachAbleAgents.addLast(agentId);
					}
				}
			}
			// Liste bereinigen
			reachableAgents.removeAll(notReachAbleAgents);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/**
	 * Gibt an, ob die Hypothese gebildet werden soll.
	 * 
	 * @return
	 */
	private boolean shouldIBuildHypothesis() {
		if ((m_numExamples > 1) && (!m_hypothesisWasBuilded)) {
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getAgentTypString()
	 */
	public String getStrategyString() {
		return "Zentrales Lernen";
	}
}
