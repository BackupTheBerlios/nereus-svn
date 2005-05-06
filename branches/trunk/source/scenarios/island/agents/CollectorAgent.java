/*
 * Created on 25.09.2003
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

import java.util.Vector;

import scenario.communication.IMessagingAgent;
import scenario.island.IslandScenarioAgent;
import simulator.AbstractScenarioHandler;
import speachacts.AnswerHypothesisSpeachact;
import speachacts.AskForHypothesisSpeachact;
import speachacts.CommitSpeachact;
import speachacts.DenyAnswerSpeachact;
import speachacts.NotificationSpeachact;
import speachacts.RegisterSpeachact;
import speachacts.Speachact;
import speachacts.StopCommunicationSpeachact;
import speachacts.UnRegisterSpeachact;
import utils.Id;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Die Klasse repräsentiert die Sammelagenten der Strategie 
 * Zentrales Lernen aus der Diplomarbeit dar. 
 * 
 * @author Daniel Friedrich
 */
public class CollectorAgent
	extends IslandScenarioAgent
	implements IMessagingAgent, IIslandScenarioAgent {

	/**
	 * Gibt an ob die Kommunikation mit dem Learning Agent bereits gestoppt 
	 * wurde.
	 */
	private boolean m_communicationIsStopped = false;

	/**
	 * Id des Learning Agent, damit mit diesem kommuniziert werden kann. 
	 */
	private Id m_learningAgent = null;
	
	/**
	 * Enthält das nächste Beispiel, dass kommuniziert werden soll.
	 */
	private Instance m_exampleToCommunicate = null;

	/**
	 * Konstruktor.
	 */
	public CollectorAgent() {
		super();
		m_isALearningAgent = false;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public CollectorAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = false;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public CollectorAgent(Id id, String name) {
		super(id, name);
		m_isALearningAgent = false;
	}

	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return true;
	}

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getPhase()
	 */
	public int getPhase() {
		if (m_classifier != null) {
			// Hypothese vorhanden, deshalb 2 oder 3
			if (m_communicationIsStopped) {
				// Kommunikation eingestellt, deshalb Phase 3
				return 3;
			}
			return 2;
		}
		return 1;
	}

	/* (non-Javadoc)
	 * @see scenario.communication.IMessagingAgent#receiveMessage(speachacts.Speachact)
	 */
	public Speachact receiveMessage(Speachact act) {
		Speachact answer = null;
		try {
			// Ist das eine Notification-Nachricht?
			if (act instanceof NotificationSpeachact) {
				if (act.getSender() != null) {
					m_learningAgent = act.getSender();
				}
				// registrierung vorbereiten
				answer =
					this.getScenarioHandler().createSpeachAct(RegisterSpeachact.class);

			} else if (act instanceof StopCommunicationSpeachact) {
				answer =
					this.getScenarioHandler().createSpeachAct(CommitSpeachact.class);
			} else {
				answer =
					this.getScenarioHandler().createSpeachAct(
						DenyAnswerSpeachact.class);
			}
			if (answer != null) {
				answer.setSender(m_id);
				answer.setReceiver(act.getSender());
				answer.setContent(null);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		System.out.println(
			"Kosten der Antwort (Collector Agent): "
				+ Double.toString(answer.calculateAnswerCosts()));
		return answer;
	}

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#simulate()
	 */
	public void simulate() {
		try {
			// Runde hochzählen
			m_roundCounter++;

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
				this.communicate();
			}

			if(!isPlaceExplored){
				if(this.shouldIExploreThePlace(attributes)) {
					boolean cellFound = this.exploreThePlace();
					// Neues Wissen verarbeiten
					Vector tmpVector = new Vector();
					if (m_roundCounter == 1) {
						m_examples = new Instances(attributes.dataset(), 0);
					}
					if (cellFound) {
						attributes.setClassValue(1.0);
					} else {
						attributes.setClassValue(0.0);
					}
					m_exampleToCommunicate = attributes;
					m_newExamples = true;
				}
			}
			// Zielliste erstellen und bewegen
			this.move();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}
	
	/* (non-Javadoc)
	 * @see scenario.island.IslandScenarioAgent#shouldIClassify()
	 */
	protected boolean shouldIClassify() {
		if((m_communication == 0) || (m_classification == 0)) {
			/*
			 * Ist eigentlich nicht korrekt der Wert, deshalb sichheitshalber 
			 * eine Ausgabe ins out.
			 */
			System.out.println(
				"m_classification: " 
				+ m_classification 
				+ " und m_communication: " 
				+ m_communication);
		}
		if(m_classifier != null) {
			return true;
		}
		return false;
	}	

	/**
	 * Ermittelt ob der Agent kommunizieren soll. 
	 * 
	 * Das ist immer dann der Fall, wenn der Agent nicht in der ersten Runde 
	 * ist, über genügend Energie verfügt und ausserdem die Kommunikation noch
	 * nicht vom Learning Agent beendet wurde.
	 * 
	 * @return
	 */
	private boolean shouldICommunicate() {
		/*
		 * keine Kommunikation, wenn diese gestopped ist, auch nicht in der 
		 * ersten Runde, oder wenn die Energie dann nicht mehr zum 
		 * unregistrieren langt und wenn der Learning Agent bekannt ist,
		 * ansonsten immer kommunizieren.
		 */
		boolean haveIEnoughEnergy = false;
		try {
			double energy =
				this.getScenarioHandler().getMyEnergyValue(this.getActionKey(), m_id);
			Speachact act =
				this.getScenarioHandler().createSpeachAct(
					AskForHypothesisSpeachact.class);
			if(m_exampleToCommunicate != null) {
				m_examples.add(m_exampleToCommunicate);
				m_numExamples++;
				act.setContent(m_exampleToCommunicate);
			}
			if (energy > act.calculateCosts()) {
				haveIEnoughEnergy = true;
			}				 
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		if (m_communicationIsStopped) {
			// Log ins out zum Erkennen des regulären Ablaufs
			System.out.println("Kommunikation wurde gestoppt");
		}
		
		/*
		 * Eigentliche Entscheidung, ob kommuniziert wird oder nicht. 
		 */
		if ((m_communicationIsStopped)
			|| (m_roundCounter == 1)
			|| (!haveIEnoughEnergy)
			|| (m_learningAgent == null)
			|| (m_examples == null)) {
			return false;
		}
		// kommuniziere
		return true;
	}

	/**
	 * Der Agent kommuniziert mit dem Learning Agent.
	 * 
	 * Der Agent fragt nach einer neuen Hypothese beim Learning Agent nach und
	 * übermittelt falls vorhanden, dass in m_exampleToCommunicate enthaltene
	 * gesammelte Beispiel, dabei gleich mit. 
	 */
	private void communicate() {
		try {
			// Sprechakt erstellen
			Speachact act =
				this.getScenarioHandler().createSpeachAct(
					AskForHypothesisSpeachact.class);
			// Ist neues Beispiel vorhanden ?		
			if (m_newExamples) {
				act.setContent(m_exampleToCommunicate);
				m_newExamples = false;		
			} else {
				act.setContent(null);
			}
			// Anschrift erfassen
			act.setSender(m_id);
			act.setReceiver(m_learningAgent);
			System.out.println(
				"Kosten der Anfrage (Collector Agent): "
					+ Double.toString(act.calculateCosts()));
			// versenden und auf Antwort warten
			Speachact answer =
				this.getScenarioHandler().sendMessage(this.getActionKey(), act);
			// Antwort auswerten
			if (answer instanceof AnswerHypothesisSpeachact) {
				if ((answer.getContent() != null)
					&& (answer.getContent() instanceof Classifier)) {
					// Hypothese abspeichern
					m_numExamples = m_classification + 5;
					m_classifier = (Classifier) answer.getContent();
				}
			} else {
				// Learning Agent antwortet nicht mehr, daher 
				m_communicationIsStopped = true;
				// abmelden
				Speachact send =
					this.getScenarioHandler().createSpeachAct(
						UnRegisterSpeachact.class);
				send.setContent(null);
				send.setSender(m_id);
				send.setReceiver(m_learningAgent);
				Speachact commit =
					this.getScenarioHandler().sendMessage(
						this.getActionKey(), 
						send);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getAgentTypString()
	 */
	public String getStrategyString() {
		return "Zentrales Lernen";
	}

}
