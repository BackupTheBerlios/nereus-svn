/*
 * Created on 04.10.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package scenario.fgml;

import java.util.Vector;

import scenario.communication.IMessagingAgent;
import scenario.fgml.FgmlScenarioAgent;
import scenario.fgml.IFgmlScenarioAgent;
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
 * Der FgmlCollectorAgent ist eine spezielle Version des CollectorAgent der 
 * Zentrales Lernen Strategie aus der Diplomarbeit für die FGML 03 in Karlsruhe.
 * 
 * @author Daniel Friedrich
 */
public class CollectorAgent 
	extends FgmlScenarioAgent
	implements IMessagingAgent, 
				IFgmlScenarioAgent {
		
	private boolean m_communicationIsStopped = false;	
	
	private Id m_learningAgent = null;

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
	
	public static boolean isRunableAgent() {
		return true;
	}

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getPhase()
	 */
	public int getPhase() {
		if(m_classifier != null) {
			// Hypothese vorhanden, deshalb 2 oder 3
			if(m_communicationIsStopped) {
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
			if(act instanceof NotificationSpeachact) {
				if(act.getSender() != null) {
					m_learningAgent = act.getSender();
				}
				// registrierung vorbereiten
				answer = 
					m_scenarioHandler.createSpeachAct(RegisterSpeachact.class);

			}else if(act instanceof StopCommunicationSpeachact) {
				answer = 
					m_scenarioHandler.createSpeachAct(CommitSpeachact.class);			
			}else {
				answer = 
					m_scenarioHandler.createSpeachAct(DenyAnswerSpeachact.class);			
			}
			if(answer != null) {
				answer.setSender(m_id);
				answer.setReceiver(act.getSender());	
				answer.setContent(null);
			}			
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		System.out.println("Kosten der Antwort (Collector Agent): " + Double.toString(answer.calculateAnswerCosts()));
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
				m_scenarioHandler.getAttributesFromPlace(
					this.getActionKey(),
					m_id);
			// Feststellen ob der Platz bereist ausgebeutet ist 
			boolean isPlaceExplored = 
				m_scenarioHandler.isPlaceExplored(this.getActionKey(),m_id);
			
			// Soll kommuniziert werden?
			if(this.shouldICommunicate()) {
				// ja				
				this.communicate();
			}
			m_newExamples = false;
					
			// Soll gegraben werden ?
			if ((!isPlaceExplored) && (this.shouldIExploreThePlace(attributes))) {
				// ja -> graben
				boolean cellFound = this.exploreThePlace();
				// Neues Wissen verarbeiten
				Vector tmpVector = new Vector();
				if(m_roundCounter == 1) {
					if(cellFound) {
						attributes.setClassValue(1.0);
					}else {
						attributes.setClassValue(0.0);
					}
					m_examples = new Instances(attributes.dataset(),0);					
				}
				tmpVector.add(m_examples);

				this.addExample(attributes,new Boolean(cellFound),tmpVector);
				m_newExamples = true;
			}	
			// Zielliste erstellen und bewegen
			this.move();	
		}catch(Exception e) {
			e.printStackTrace(System.out);		
		}

	}
	
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
				m_scenarioHandler.getMyEnergyValue(this.getActionKey(), m_id);
			 Speachact act = 
				m_scenarioHandler.createSpeachAct(
					AskForHypothesisSpeachact.class);
			 if((m_examples != null) && (m_numExamples > 0)) {
				act.setContent(m_examples.firstInstance());
				if(energy > act.calculateCosts()) {
					haveIEnoughEnergy = true;				
				}
			 }else {
				// erste Runde, deshalb auf jeden Fall.
				haveIEnoughEnergy = true;
			 }
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
		if(m_communicationIsStopped) {
			System.out.println("Kommunikation wurde gestoppt");
		}
		 
		if((m_communicationIsStopped) 
			|| (m_roundCounter == 1)
			|| (!haveIEnoughEnergy)
			|| (m_learningAgent == null)
			|| (m_examples == null)) {
			return false;
		}
		// kommuniziere
		return true;
	}
	
	private void communicate() {
		try {
			if(m_roundCounter == 8) {
				System.out.println("Stop");
			}
			//System.out.print("Versuche Kommunikation -> ");
			Speachact act =
				m_scenarioHandler.createSpeachAct(
					AskForHypothesisSpeachact.class);
			if(m_newExamples) {
				act.setContent(m_examples.lastInstance());
				//System.out.print("Beispiel gesendet.\n");		
			}else {
				act.setContent(null);
				//System.out.println("Kein Beispiel vorhanden.\n");
			}
			act.setSender(m_id);
			act.setReceiver(m_learningAgent);
			System.out.println("Kosten der Anfrage (Collector Agent): " + Double.toString(act.calculateCosts()));
			// versenden und auf Antwort warten
			Speachact answer = 
				m_scenarioHandler.sendMessage(this.getActionKey(),act);
			// Antwort auswerten
			if(answer instanceof AnswerHypothesisSpeachact) {
				if((answer.getContent() != null) 
					&& (answer.getContent() instanceof Classifier)) {
					// Hypothese abspeichern
					m_classifier = (Classifier)answer.getContent();		
				}
			}else {
				// Learning Agent antwortet nicht mehr, daher 
				m_communicationIsStopped = true;
				// abmelden
				Speachact send = m_scenarioHandler.createSpeachAct(UnRegisterSpeachact.class);
				send.setContent(null);
				send.setSender(m_id);
				send.setReceiver(m_learningAgent);
				Speachact commit = m_scenarioHandler.sendMessage(this.getActionKey(),send);
			}
		}catch(Exception e) {
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

