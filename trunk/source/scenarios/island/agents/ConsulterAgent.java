/*
 * Created on 29.09.2003
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

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

import scenario.communication.IMessagingAgent;
import scenario.island.IslandScenarioAgent;
import simulator.AbstractScenarioHandler;
import speachacts.AnswerDecisionSpeachact;
import speachacts.AskAnotherTimeSpeachact;
import speachacts.AskForDecisionSpeachact;
import speachacts.DenyAnswerSpeachact;
import speachacts.Speachact;
import utils.Id;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Die Klasse ist die Superklasse aller ConsulterAgent-Agentenklassen. Damit der 
 * Agent auch funktioniert muss ihm in einer Subklasse noch ein Classifier 
 * verpasst werden. 
 * 
 * @author Daniel Friedrich
 */
public class ConsulterAgent
	extends IslandScenarioAgent
	implements IIslandScenarioAgent, 
			   IMessagingAgent {

	/**
	 * Gibt an, ob die Beratung bereits gestoppt wurde.
	 */
	private boolean m_consultingStopped = false;
	
	/**
	 * Gibt an, ob die Berratung bereits begonnen hat.
	 */
	private boolean m_haveStartedConsulting = false;
	
	/**
	 * Gibt an, ob bereits Hypothesen erstellt werden.
	 */
	private boolean m_haveStartedHypothesisbuilding = false; 
	
	/**
	 * Bestimmt wie lange ein Agent wartet, bis erst nach dem Erreichen 
	 * der Klassifikationsschwelle dass erste Mal nach einer Beratung fragt.
	 * Die Spanne existiert damit die anderen Agenten bis dahin auch die 
	 * Klassifikationsschwelle erreichen können.
	 */
	private int m_startConsultingBarrier = 3;
	
	/**
	 * Gibt an, in welcher Runde die nächste Beraterwahl ist.
	 */	
	private int m_nextConsulterElection = 0;
	
	/**
	 * GIbt an, in welchem Rundenabstand die Beraterwahlen durchgeführt werden.
	 */
	private int m_distanceBetweenElections = 2;
	
	/**
	 * Liste mit allen aktuellen Consultern.
	 */
	private LinkedList m_consulter = new LinkedList();
	
	/**
	 * Anzahl der positiven Entscheidungen in der Runde.
	 */
	private int m_positiveDecisions = 0;
	
	/**
	 * Hashtable in der die korrekten Entscheidungen der Agenten erfasst werden.
	 */
	private Hashtable m_decisions = new Hashtable();
	
	/**
	 * Hashtable mit den Entscheidungen der Berater für diese Runde.
	 */
	private Hashtable m_roundDecisions = new Hashtable();
	
	/**
	 * Anzahl der eigenen korrekten Entscheidungen.
	 */
	private int m_ownCorrectDecisions = 0;
	
	/**
	 * Eigene Entscheidung in der Runde.
	 */
	private boolean m_ownDecision = false;
	
	/**
	 * Gibt an, ob das erste Mal Berater gewählt werden.
	 */
	private boolean m_inFirstEvaluationRound = false;

	/**
	 * Konstruktor.
	 */
	public ConsulterAgent() {
		super();
		m_isALearningAgent = true;
		m_classification = 15;
		m_startConsultingBarrier = 4;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 * @param handler
	 */
	public ConsulterAgent(String name, AbstractScenarioHandler handler) {
		super(name, handler);
		m_isALearningAgent = true;
		m_classification = 15;
		m_startConsultingBarrier = 4;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id
	 * @param name
	 */
	public ConsulterAgent(Id id, String name) {
		super(id, name);
		m_isALearningAgent = true;
		m_classification = 15;
		m_startConsultingBarrier = 4;
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
		if(m_numExamples > m_classification) {
			if(m_consultingStopped) {
				return 3;	
			}
			return 2;
		}
		return 1;
	}

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#simulate()
	 */
	public void simulate() {
		try {
			// rundenzähler hochsetzen
			m_roundCounter++;
			// Alte Entscheidungen zurücksetzen
			m_roundDecisions = new Hashtable();
			
			// nicht mehr erreichbare Consulter aussortieren
			if(m_roundCounter != 1) {
				this.cleanUpTheConsultingGroup();
			}
			
			// Müssen neue Berater gewählt werden ?
			if(this.shouldIElectMyConsulter()) {
				if(!m_haveStartedConsulting) {
					// Starte Consulting
					m_haveStartedConsulting = true;
					m_consulter = this.getStartingConsultingGroup();
				}else {
					// Ansonsten wähle die Berater normal
					m_consulter = this.electConsulter();
				}
			}
			
			// Attribute wahrnehmen
			Instance attributes = 
				this.getScenarioHandler().getAttributesFromPlace(
					this.getActionKey(),
					m_id);
					
			// wurde an dem Platz schon gegraben ?		
			if(!this.getScenarioHandler().isPlaceExplored(this.getActionKey(),m_id)) {
				
				//  Soll der Agent die Berater um Rat fragen?
				if(this.shouldICommunicate()) {
					this.communicate(attributes);					
				}
				
				// Soll gegraben werden?
				if(this.shouldIExploreThePlace(attributes)) {
					// Graben
					boolean cellFound = this.exploreThePlace();

					// in der ersten Runde dann Erfahrungsspeicher initialisieren
					if(m_roundCounter == 1) {
						if(cellFound) {
							// positives Beispiel
							attributes.setClassValue(1.0);
						}else {
							// negative Beispiel
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
						
					// Hypothese bilden wenn notwendig
					if(this.shouldIBuildHypothesis()) {
						this.buildHypothesis(m_examples);
						m_haveStartedHypothesisbuilding = true;
					}			
					

					if((m_haveStartedConsulting) && (!m_consultingStopped)) {
						// Bewerte die eigene Entscheidung
						if(cellFound == m_ownDecision) {
							m_ownCorrectDecisions++;
						}						

						// Bewerte die Entscheidungen der Consulter
						this.estimateConsultants(cellFound);	
						
						// Kontrolliere ob das Consulting noch benötigt wird	
						this.checkIfINeedTheConsulting();	
					}								
				}else {
					System.out.println("Agent " + m_id.toString() + " Nicht graben.");
				}
			}else {
				if(m_numExamples > m_classification) {
					if(m_classifier.classifyInstance(attributes) > 0.1) {
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
				// Hypothese bilden wenn notwendig
				if(this.shouldIBuildHypothesis()) {
					this.buildHypothesis(m_examples);
					m_haveStartedHypothesisbuilding = true;
				}				
			}
			// Bewegen
			this.move();
			
			// aufräumen
			m_positiveDecisions = 0;
			//m_roundDecisions.clear();			
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}


	}

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getStrategyString()
	 */
	public String getStrategyString() {
		return "Kompetente Berater";
	}
	
	/**
	 * Wählt die neuen Berater aus den bisherigen Beratern aus.
	 * 
	 * @return LinkedList - Liste der neuen Berater
	 */
	private LinkedList electConsulter() {
		m_inFirstEvaluationRound = false;
		// bestimme Anzahl der Consulter
		int numOfNeedConsulter = 
			(new Double((double)m_consulter.size() / 2.0)).intValue();
		if((m_consulter.size() % 2) > 0) {
			// Addiere einen Agenten drauf, es wird aufgerundet	
			numOfNeedConsulter++;
		}
		// muss mindestens 2 sein
		if(numOfNeedConsulter < 2) {
			numOfNeedConsulter = 2;
		}
		LinkedList electedConsulter = new LinkedList();
		LinkedList removeableConsulter = new LinkedList();
		// Wähle die besten Berater aus.
		int numOfElectedConsulter = 0;
		while(numOfElectedConsulter < numOfNeedConsulter) {
			int highestCount = 0;
			Id electedAgent = null;
			ListIterator iterator = m_consulter.listIterator();
			while(iterator.hasNext()) {
				Id consulter = (Id)iterator.next();
				if(!m_decisions.containsKey(consulter.toString())) {
					removeableConsulter.addLast(consulter);
				}
				int correctDecisions = 
					((Integer)m_decisions.get(consulter.toString())).intValue();
				if(correctDecisions > highestCount) {
					electedAgent = consulter;
					highestCount = correctDecisions;	
				}
			}
			if(electedAgent == null) {
				// wähle die ersten  beiden aus
				if(m_consulter.size() > 1) {
					electedConsulter.add(m_consulter.get(0));
					electedConsulter.add(m_consulter.get(0));	
					numOfElectedConsulter = 2;
					break;
				}else {
					// stoppe das ganze, nicht genügend Agenten da
					m_consultingStopped = true;
				}	
			}else {
				electedConsulter.addLast(electedAgent);
				numOfElectedConsulter++;
			}
			// Löschen der fehlerhaften
			m_consulter.removeAll(removeableConsulter);
		}
		// Bestimme nächste Auswahlrunde
		m_nextConsulterElection = m_roundCounter + m_distanceBetweenElections;
		 	
		return electedConsulter;
	}
	
	/**
	 * Gibt zurück, ob der Agent kommunizieren soll oder nicht.
	 * 
	 * @return boolean
	 */
	private boolean shouldICommunicate() {
		if((m_haveStartedConsulting) && (!m_consultingStopped)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gibt die Startgruppe der Consulter zurück.
	 * 
	 * @return LinkedList - Startgruppe von Consultern
	 */
	private LinkedList getStartingConsultingGroup() {
		LinkedList retval = new LinkedList();
		try {
			Vector reachable = 
				this.getScenarioHandler().getForCommunicationReachableAgents(
					this.getActionKey(),
					m_id);	
			for(int i=0; i < reachable.size();i++) {
				Id consulter = (Id)reachable.get(i);
				retval.addLast(consulter);
				m_decisions.put(consulter.toString(), new Integer(0));		
			}
			m_nextConsulterElection = 
				m_roundCounter + m_distanceBetweenElections;
			m_inFirstEvaluationRound = true;
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		return retval;
	}
	
	/**
	 * Bereinigt die Liste der Consulter, von den nicht mehr existenten Agenten.
	 */
	private void cleanUpTheConsultingGroup() {
		Vector reachableAgents = null;
		try {	
			reachableAgents = 
				this.getScenarioHandler().getForCommunicationReachableAgents(
					this.getActionKey(),
					m_id);
		}catch(Exception e) {
			e.printStackTrace(System.out);
			reachableAgents = new Vector();
		}
		ListIterator iterator = m_consulter.listIterator();
		LinkedList consulterToRemove = new LinkedList();
		while(iterator.hasNext()) {
			Id consulter = (Id)iterator.next();
			if(!reachableAgents.contains(consulter)) {				
				consulterToRemove.addLast(consulter);			
			}
		}
		// alle nicht mehr erreichbaren Consulter aus der beraterliste löschen
		m_consulter.removeAll(consulterToRemove);
		// jetzt weniger wie zwei Consulter, dann beende Phase 2
		if((m_haveStartedConsulting) && (m_consulter.size() < 2)) {
			m_consultingStopped = true;
		}
	}
	
	/**
	 * Gibt zurück ob die Consulter in der Runde gewählt werden müssen.
	 * 
	 * @return boolean 
	 */
	private boolean shouldIElectMyConsulter() {
		if(m_consultingStopped) {
			return false;
		}
		if(m_haveStartedConsulting) {
			// kontrolliere ob aktuelle Runde eine Wahlrunde ist
			if(m_roundCounter == m_nextConsulterElection) {
				return true;
			}
		}else {
			if(m_numExamples > m_classification) {
				// Sicherheitshalber erst mal Hypothese bilden
				try {
					this.buildHypothesis(m_examples);
					//m_classifier.buildClassifier(m_examples);	
					m_haveStartedHypothesisbuilding = true;				
				}catch(Exception e) {
					e.printStackTrace(System.out);
				}
				// kontrolliere ob bereits ein Termin für die erste Wahl exists.
				if(m_nextConsulterElection == 0) {
					// Lege einen Fest
					m_nextConsulterElection = 
						m_roundCounter + m_startConsultingBarrier;
				}else {
					if(m_nextConsulterElection == m_roundCounter)  {
						return true;
					}
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see scenario.communication.IMessagingAgent#receiveMessage(speachacts.Speachact)
	 */
	public Speachact receiveMessage(Speachact act) {
		Speachact answer = null;
		try {
			if(act instanceof AskForDecisionSpeachact) {
				if((m_haveStartedHypothesisbuilding)
					&& (act.getContent() != null) 
					&& (act.getContent() instanceof Instance)) {
					// bereite Antwort vor
					answer = 
						this.getScenarioHandler().createSpeachAct(
							AnswerDecisionSpeachact.class);
					// Lese Beispiel aus		
					Instance instance = (Instance)act.getContent();
					// klassifiziere das Beispiel
					double cellExpected = 
						m_classifier.classifyInstance(instance);	
					// Entscheide
					if(cellExpected > 0.1) {
						answer.setContent(Boolean.TRUE);	
					}else {
						answer.setContent(Boolean.FALSE);
					}	
					// Entscheide ob genügen Energie zum Senden vorhanden ist
					if(!this.couldIRenderTheAnswer(answer, 0.0, 0.0)) {	
						answer = null;
					}
					
				}else {
					answer = 
						this.getScenarioHandler().createSpeachAct(
							AskAnotherTimeSpeachact.class);
					answer.setContent(null);		
				}
			}
			if(answer == null) {
				answer = 
					this.getScenarioHandler().createSpeachAct(
					DenyAnswerSpeachact.class);
				answer.setContent(null);	
			}
			answer.setReceiver(act.getSender());
			answer.setSender(m_id);
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		return answer;
	}
	
	/**
	 * Der Agent fragt bei seinem Beratern nach Klassifikationen für seinen aktuellen Platz nach.
	 * 
	 * @param instance
	 */
	private void communicate(Instance instance) {
		try {
			LinkedList consulterToRemove = new LinkedList();
			LinkedList consulterWithWaitingRequest = new LinkedList();
			ListIterator iterator = m_consulter.listIterator();
			while(iterator.hasNext()) {
				Id consulter = (Id)iterator.next();
				Speachact question = 
					this.getScenarioHandler().createSpeachAct(
						AskForDecisionSpeachact.class);
				question.setContent(instance);
				question.setSender(m_id);
				question.setReceiver(consulter);
				if(this.couldIRenderTheAnswer(question,0.0,0.0)) {
					Speachact answer = 
						this.getScenarioHandler().sendMessage(
							this.getActionKey(),
							question);
					if(answer instanceof AnswerDecisionSpeachact) {
						if((answer.getContent() != null)
							&& (answer.getContent() instanceof Boolean)) {
							boolean decision = 
								((Boolean)answer.getContent()).booleanValue();
							if(decision) {
								m_positiveDecisions++;				
							}
							// Ergebnis abspeichern
							m_roundDecisions.put(
								consulter.toString(),
								(Boolean)answer.getContent());
						}
					}else if(answer instanceof AskAnotherTimeSpeachact) {
						// vermerken, das der agent noch nicht antworten kann
						consulterWithWaitingRequest.addLast(answer.getSender());
					}else {
						consulterToRemove.addLast(answer.getSender());
					}
				}else {
					break;		
				}				
			}
						
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see scenario.island.IslandScenarioAgent#shouldIExploreThePlace(weka.core.Instance)
	 */
	protected boolean shouldIExploreThePlace(Instance instance)
		throws Exception {
		if(!m_haveStartedConsulting) {
			// Immer graben, da in Phase 1
			return true;
		}
		double result = m_classifier.classifyInstance(instance);
		if(m_consultingStopped) {
			// 3. Phase, nur graben, wenn die Klassifikation das ergab. 
			if(result > 0.1) {
				return true;
			}
			return false;
		}else {
			if(result > 0.1) {
				m_positiveDecisions++;
				m_ownDecision = true;	
			}			
			int negativeDecisions = 
				(new Double((double)m_consulter.size() / 2.0)).intValue();
			if((m_positiveDecisions > negativeDecisions) 
				|| ((m_positiveDecisions == negativeDecisions)
					&& (result > 0.1))) {
				return true;
			}
			m_ownDecision = false;			
			return false;
		}
	}
	
	/**
	 * Bewertet die Berater.
	 * 
	 * @param result
	 */
	private void estimateConsultants(boolean result) {
		ListIterator iterator = m_consulter.listIterator();
		while(iterator.hasNext()) {
			Id consulter = (Id)iterator.next();
			if(m_roundDecisions.containsKey(consulter.toString())) {
				boolean decision = 
					((Boolean)m_roundDecisions.get(
						consulter.toString())).booleanValue();
						
				if(decision == result) {
					int corrects = 
						((Integer)m_decisions.get(consulter.toString())).intValue();
					corrects++;
					m_decisions.put(consulter.toString(),new Integer(corrects));	
				}											
			}
		}
	}
	
	/**
	 * Die Methode kontrolliert, ob der Agent noch seine Berater benötigt.
	 * 
	 * Dies ist nicht mehr der Fall, wenn er 
	 * a) Bessere Ergebnisse erzielt als alle Berater
	 * b) Er bessere Ergebnisse, als einer seiner Berater erzielt und er nur 
	 *	  noch über zwei Berater verfügt.
	 */
	private void checkIfINeedTheConsulting() {
		// In der ersten Phase noch nicht kontrollieren
		if(m_inFirstEvaluationRound) {
			int countBetter = 0;
			ListIterator iterator = m_consulter.listIterator();
			while(iterator.hasNext()) {
				Id consulter = (Id)iterator.next();
				int correctDecisions = 
					((Integer)m_decisions.get(consulter.toString())).intValue();
				if(m_ownCorrectDecisions > correctDecisions-3) {
					countBetter++;	
				}
			}
			// Bei zwei Beratern, reicht besser sein als einer.
			if(m_consulter.size() == 2) {
				if(countBetter > 1) {
					m_consultingStopped = true;
				}
			}
			// Ansonsten besser als alle
			if(countBetter == m_consulter.size()) {
				m_consultingStopped = true;			
			}
		}
	}
	
	/**
	 * Gibt zurück ob eine Hypothese gebildet werden soll.
	 * @return
	 */
	private boolean shouldIBuildHypothesis() {
		if( (m_examples != null)
			&& (m_examples.numInstances() > 1)) {
			return true;
		}
		return false;
	}
	
	
}
