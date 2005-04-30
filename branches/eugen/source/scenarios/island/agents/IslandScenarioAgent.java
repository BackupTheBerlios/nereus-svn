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

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;

import qmc.DontCareExpandHalfQMCClassifier;
import qmc.DontCareExpandQMClassifier;
import simulator.AbstractAgent;
import simulator.AbstractScenarioHandler;
import speachacts.Speachact;
import utils.Id;
import visualisation.IVisualisation;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.Id3;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Die Klasse repräsentiert den Root-Agenten des Scenarios. Die meisten Agenten 
 * die im Szenario eingesetzt werden, erben von diesem Agenten. Er bringt
 * eine Reihe wichtiger Funktionen mit, die jeder Agent benötigen kann. 
 * 
 * @author Daniel Friedrich
 */
public abstract class IslandScenarioAgent 
	extends AbstractAgent 
	implements IIslandScenarioAgent {

	/**
	 * Rundenzähler
	 */
	protected int m_roundCounter = 0;

	/**
	 * Ernergiekosten für das Bewegen.
	 */
	protected double m_movingCosts = 0.0;

	/**
	 * Energiekosten zum Graben an einem Platz. 
	 */
	protected double m_exploringCosts = 0.0;
	
	/**
	 * Energiekosten zum Graben an einem Platz. 
	 */
	protected double m_communicationCosts = 0.0;

	/**
	 * Liste der zuletzt besuchten Plätze.
	 */
	protected LinkedList m_lastVisitedPlaces = new LinkedList();
	
	/**
	 * Menge aller gesammelten Trainingsbeispiele
	 */
	protected Instances m_examples = null;

	/**
	 * Wurden neue Beispiele gefunden ?
	 */
	protected boolean m_newExamples = false;			

	/**
	 * Der verwendete Lernalgorithmus des Agenten.
	 */
	protected Classifier m_classifier;	
	
	/**
	 * Anzahl der Beispiele
	 */
	protected int m_numExamples = 0;

	/*
	 * Gecasteter ScenarioHandler
	 */
	//protected IIslandScenarioHandler m_scenarioHandler;
	
	/**
	 * Aktuelle Energy des Agenten
	 */
	protected double m_actualEnergy = 0.0;
	
	/**
	 * Speichert die Id des aktuellen Platzes des Agenten.
	 */
	protected Id m_actualPlace;
	
	/**
	 * Id des zuletzt besuchten Platzes (nicht des aktuellen).
	 */
	protected Id m_lastPlace = null;

	/**
	 * Anzahl der Beispiele ab denen der Agent lernt.
	 */	
	protected int m_classification = 15;
	
	/**
	 * Anzahl der Beispiele bis zu denen der Agent lernt.
	 */
	protected int m_communication = 40;

	protected boolean m_canDistributeHypothesisis = false;

	/**
	 * Konstruktor.
	 */
	public IslandScenarioAgent() {
		super();
		m_name = m_id.toString();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 */
	public IslandScenarioAgent(String name,
		AbstractScenarioHandler handler) {
		super(name, handler);		
	}

	/**
	 * Konstruktor.
	 */
	public IslandScenarioAgent(Id id, String name) {
		super(id,name);
	}

	/**
	 * Zugriffsmethode für den Szenariohandler.
	 * 
	 * @return IIslandScenarioHandler - Der SzenarioHandler des Szenarios
	 */
	protected IIslandScenarioHandler getScenarioHandler() {
		return (IIslandScenarioHandler)m_scenario;
	}

	/**
	 * Fügt ein Beispiel den Beispielmengen hinzu.
	 * 
	 * @param Instance example - Beispiel
	 * @param boolean - Klassifikation des Beispiels
	 * @param Vector - Beispielmengen
	 */
	protected void addExample(
		Instance example, 
		Boolean classValue,  
		Vector experience) {
		// Beispiel vervollständigen	
		if(classValue != null){
			if(classValue.booleanValue()) {
				// positives Beispiel
				example.setClassValue(1.0);
			}else {
				// negatives Beispiel
				example.setClassValue(0.0);
			}			
		}
		// In die entsprechenden Beispielmenge packen
		for(int i=0; i < experience.size();i++) {
			Instances set = (Instances)experience.get(i);
			
			set.add(new Instance(example));
		}
		m_numExamples++;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public final void run() {
		super.run();
		// Solange die Runden durchführen bis das Spiel beendet ist.
		int count = 0;
		while((!m_gameIsFinished) ) {
			if(this.canStartRound()) {
				count++;
				// Kosten für die einzelnen Aktionen abfragen.
				this.getCosts();
				this.simulate();
			}
		}
	}		

	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getHypothesis()
	 */
	public Classifier getHypothesis() {
	
		if((m_classifier != null) && (m_canDistributeHypothesisis)) {
			try {
				return (Classifier.makeCopies(m_classifier,1))[0];
			}catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioAgent#getPhase()
	 */
	public abstract int getPhase();
	
	/**
	 * Ermittelt die Parameterwerte für die Kosten.
	 */
	private void getCosts() {
		try {
			Object obj = this.getScenarioHandler().getParameter(
				this.getActionKey(),
				m_id,
				IIslandScenarioHandler.ENERGYFORDIGGING);
			if(obj != null) {
				m_exploringCosts = ((Double)obj).doubleValue();	
			}
			obj = this.getScenarioHandler().getParameter(
				this.getActionKey(),
				m_id,
				IIslandScenarioHandler.ENERGYFORMOVING);
			if(obj != null) {
				m_movingCosts = ((Double)obj).doubleValue();	
			}
			obj = this.getScenarioHandler().getParameter(
				this.getActionKey(),
				m_id,
				IIslandScenarioHandler.ENERGYFORCOMMUNICATION);
			if(obj != null) {
				m_communicationCosts = ((Double)obj).doubleValue();	
			}
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}	
	
	/**
	 * Erstellt eine Liste der Zielplätze des Agenten für die Bewegung.
	 */
	protected LinkedList createTargetList() {
		if(this.shouldIMove()) {
			try {
				// mögliche Ziele holen
				Vector ways = 
					this.getScenarioHandler().getPossibleWaysFromPlace(
						this.getActionKey(),
						m_id);
				LinkedList targetList = new LinkedList();
				LinkedList unknownPlaces = new LinkedList();
				
				for(int i=0; i < ways.size(); i++) {
					Id placeId = (Id)ways.get(i);
					ListIterator iterator = m_lastVisitedPlaces.listIterator();
					int count = 0;
					boolean hasVisited = false;
					while(iterator.hasNext()) {
						if(placeId.equals((Id)iterator.next())) {
							hasVisited = true;
						}
					}
					if(!hasVisited) {
						//unknownPlaces.add(placeId);
						targetList.add(placeId); 
					}
				}
				// Diese Plätze schon mal aus der Liste der möglichen Wege filtern
				ways.removeAll(targetList);
				//System.out.println("Nach filtern noch " + ways.size() + " mögliche Zielplätze");
				
				
				// letzten Platz entfernen
 
				if(m_lastPlace != null) {
					ways.remove(m_lastPlace);
				}
				/*
				 * Die restlichen Plätze nach zufälliger Auswahl hinten an die
				 * Liste anfügen,
				 */ 
				Random randomizer = new Random(2003);
				while(ways.size() > 0) {
					int index = Math.abs(randomizer.nextInt(ways.size()));
					targetList.addLast(ways.get(index));
					ways.remove(index);
				}
				return targetList;			
			}catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}	
		return new LinkedList(); 
	}

	/**
	 * Gibt zurück ob der Agent sich in dieser Runde bewegen soll oder nicht.
	 * 
	 * Der Agent bewegt sich immer dann nicht, wenn er zuwenig Energie hat,
	 * um an den nächsten Platz zukommen und dort zu graben. Da dies nur eine 
	 * Verschwendung von wertvoller Energie ist. 
	 * 
	 * @return boolean
	 */
	protected boolean shouldIMove() {
		try {
			if(this.getScenarioHandler().getMyEnergyValue(this.getActionKey(), m_id) 
				> (m_movingCosts + m_exploringCosts)) {
				return true;
			}
			// loggen
			try {
				this.getScenarioHandler().log(
					m_id,
					IVisualisation.StateMsg,
					"Der Agent "
					+ m_id.toString() 
					+ " besitzt nicht genügend Energie zum Bewegen und "
					+ "bleibt deshalb an seinem aktuellen Platz: "
					+ m_actualPlace.toString());
			}catch(Exception e) {
				e.printStackTrace(System.out);	
			}				
			System.out.println("Nicht genug Energie, nicht bewegen.");
			return false;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
	}
	
	/**
	 * Gibt zurück ob an dem Platz gegraben werden soll.
	 * 
	 * @param Instance instance - Attribute des aktuellen Platzes
	 * @return boolean
	 * @throws Exception
	 */
	protected boolean shouldIExploreThePlace(Instance instance) throws Exception {
		// Nur Graben wenn dafür Energie da ist
		if(this.getScenarioHandler().getMyEnergyValue(this.getActionKey(), m_id) 
			< (m_exploringCosts)) {
			// loggen
			try {
				this.getScenarioHandler().log(
					m_id,
					IVisualisation.StateMsg,
					"Der Agent "
					+ m_id.toString() 
					+ " besitzt nicht genügend Energie um am Platz "
					+ m_actualPlace.toString()
					+" zu graben. ");
				System.out.println("Nicht graben zuwenig Energie");	
			}catch(Exception e) {
				e.printStackTrace(System.out);	
			}					
			return false;			
		}
		
		if(this.shouldIClassify()) {
			if(m_classifier == null) {
				return false;
			}
			// Nur Graben wenn Klassifikation des Platzes das ergibt.
			double result = m_classifier.classifyInstance(instance);
			if(result < 0.1) {
				return false;
			}
			return true;
		}else {
			// Immer Graben, da noch in der ersten Runde
			return true;
		}
	}	
	
	/**
	 * Bewegt den Agenten, inklusive Auswahl der Ziele, feststellen der
	 * Bewegung usw.
	 */
	protected void move() {
		// Zielliste erstellen
		/*
		 * Die Liste muss umbedingt erst erstellt werden. Der Aufruf der
		 * Methode createTargetList() im Aufruf der Methode moveTo führt 
		 * dazu das sich in m_actionKey noch der alte Key befindet, dieser
		 * wird beim Erzeugen der Liste erneuert. dadurch kommt es zu einem 
		 * CheckIdentity-Fehler.
		 */
		LinkedList targetList = this.createTargetList();
		try {
			StringBuffer buffer = new StringBuffer();
			for(int i=0; i < targetList.size();i++) {
				buffer.append(i + ": " + targetList.get(i).toString() + " ");
			}
			this.getScenarioHandler().log(m_id, IVisualisation.StateMsg, 
				"Zieliste des Agenten " 
				+ m_id.toString() 
				+ ":" 
				+ buffer.toString());	
		}catch(Exception e) {
			e.printStackTrace(System.out);
		} 
		// Getrennte Try-Catches damit fehlerhaftes Log nicht move verhindert.
		try {
			// Runde beenden
			m_actualPlace = 
				this.getScenarioHandler().moveTo(this.getActionKey(),m_id,targetList);
			/*
			 * Feststellen ob sich bewegt wurde und wenn ja dann Liste der 
			 * zuletzt besuchten Plätze aktualisieren.
			 */
			if((m_lastPlace == null) || (!m_lastPlace.equals(m_actualPlace))) {
				// Agent hat sich bewegt.
				m_lastVisitedPlaces.addFirst(m_lastPlace);
				m_lastPlace = m_actualPlace;  
			}
			if(m_lastVisitedPlaces.contains(m_actualPlace)) {
				System.out.println("Bereits besuchter Platz.");
			}
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Gräbt an dem Platz und wertet das Ergebnis aus.
	 * 
	 * Wird eine Energiezelle gefunden, dann wird true zurück gegeben, ansonsten
	 * false.
	 *  
	 * @return
	 */
	protected boolean exploreThePlace() {
		try {
			boolean action = 
				this.getScenarioHandler().explorePlace(this.getActionKey(),m_id);
			if(action) {
					boolean cellFound = 
						this.getScenarioHandler().energyCellFounded(
								this.getActionKey(),
								m_id);	
					// Erfolg loggen	
					if(cellFound) {
						this.getScenarioHandler().log(
							m_id,
							IVisualisation.StateMsg,
							"Agent " 
							+ m_id.toString() 
							+ " findet eine Energiezelle.");
						System.out.println("Energiezelle gefunden.");							
					}
					m_newExamples = true;
					return cellFound;
			}			
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		return false;
	}
	
	/**
	 * Gibt zurück, ob der übergebene Spreachakt versendet werden soll.
	 * 
	 * Die Methode beachtet berechnet dazu die voraussichtlichen Kosten und 
	 * vergleicht diese + den eventuell übergebenen Aufschlag mit dem aktuellen
	 * Energieniveau. Ist genügend Energie vorhanden darf gesendet werden.
	 * 
	 * @param Speachact answer - Sprechakt
	 * @param double agio - Aufpreis
	 * @return
	 */
	protected boolean couldIRenderTheAnswer(
		Speachact answer, 
		double agio, 
		double faktor) {
		try {
			// eigene Energie holen
			double myActualEnergie = 
				this.getScenarioHandler().getMyEnergyValue(this.getActionKey(),m_id);
			// Kosten für Antwort berechnen
			double answerCosts = answer.calculateAnswerCosts();	
			if(faktor > 0.1) {	
				answerCosts = answerCosts * faktor;
			}

			if((answerCosts + agio) < (myActualEnergie + 0.1)) {
				return true; 	
			}		
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		// loggen
		try {
			this.getScenarioHandler().log(
				m_id,
				IVisualisation.StateMsg,
				"Der Agent "
				+ m_id.toString() 
				+ " besitzt nicht genügend Energie, um auf die Anfrage zu antworten.");
		}catch(Exception e) {
			e.printStackTrace(System.out);	
		}	
		return false;
	}
	
	/**
	 * Gibt zurück, ob der Agent genügend Energie für die Antwort einer Nachricht vorhanden ist.
	 * 
	 * @param question
	 * @param agio
	 * @param faktor
	 * @return
	 */
	protected boolean couldIRenderTheQuestion(
		Speachact question, 
		double agio, 
		double faktor) {
		try {
			// eigene Energie holen
			double myActualEnergie = 
				this.getScenarioHandler().getMyEnergyValue(this.getActionKey(),m_id);
			// Kosten für Antwort berechnen
			double costs = question.calculateCosts();	
			if(faktor > 0.1) {	
				costs = costs * faktor;
			}

			if((costs + agio) < (myActualEnergie + 0.1)) {
				return true; 	
			}		
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		// loggen
		try {
			this.getScenarioHandler().log(
				m_id,
				IVisualisation.StateMsg,
				"Der Agent "
				+ m_id.toString() 
				+ " besitzt nicht genügend Energie, um auf die Anfrage zu antworten.");
		}catch(Exception e) {
			e.printStackTrace(System.out);	
		}	
		return false;
	}	
	
	/**
	 * Gibt zurück, ob der Agent den Platz klassifizieren soll.
	 * 
	 * Dies ist immer dann der Fall, wenn die folgende Bedingunge erfüllt ist: 
	 * - der Agent befindet sich nicht in der ersten Runde
	 * - die Anzahl der Trainingsbeispiele ist höher als die 
	 * 	 Klassifikationsschwelle (m_classification)
	 * 
	 * @param Instances examples 
	 * @return boolean
	 */
	protected boolean shouldIClassify() {
		if((m_communication == 0) || (m_classification == 0)) {
			// Theoretisch ein Fehler, muss aber nicht sein deshalb nur Warnung
			System.out.println(
				"m_classification: " 
				+ m_classification 
				+ " und m_communication: " 
				+ m_communication);
		}
		if((m_examples != null) 
			&& (m_examples.numInstances() > m_classification)){
			return true;
		}
		return false;
	}	
	
	/**
	 * Bildet eine Hypothese.
	 * 
	 * @param examples
	 */
	protected void buildHypothesis(Instances examples) {
		try {
			if(m_classifier instanceof Id3) {
				((Id3)m_classifier).buildClassifier(examples); 
			}else if(m_classifier instanceof NaiveBayes) {
				((NaiveBayes)m_classifier).buildClassifier(examples); 
			}else if(m_classifier instanceof DontCareExpandHalfQMCClassifier) {
				((DontCareExpandHalfQMCClassifier)m_classifier).buildClassifier(examples); 
			}else if(m_classifier instanceof DontCareExpandQMClassifier) {
				((DontCareExpandQMClassifier)m_classifier).buildClassifier(examples); 
			}else {
				m_classifier.buildClassifier(examples);		
			}
			m_canDistributeHypothesisis = true;
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}

	}	
	
	/* (non-Javadoc)
	 * @see simulator.AbstractAgent#isRunableAgent()
	 */
	public static boolean isRunableAgent() {
		return false;
	}
}
