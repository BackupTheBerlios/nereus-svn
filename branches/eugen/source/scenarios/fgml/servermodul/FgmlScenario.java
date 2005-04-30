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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import qmc.DontCareExpandHalfQMCClassifier;
import qmc.DontCareExpandQMClassifier;
import qmc.Term;
import scenario.communication.IMessagingAgent;
import scenario.communication.MessagingServer;
import scenario.fgml.statistic.FgmlStatisticComponent;
import scenario.islandenviroments.IslandEnviroment;
import scenario.islandenviroments.Place;
import simulator.AbstractAgent;
import simulator.AbstractScenario;
import simulator.AbstractScenarioHandler;
import simulator.IInformationHandler;
import simulator.ServerInfoObject;
import simulator.statistic.IStatisticComponent;
import simulator.statistic.IStatisticScenario;
import speachacts.Speachact;
import utils.ActionKey;
import utils.Id;
import utils.ParameterDescription;
import visualisation.IVisualisation;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.Id3;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.ClassFilter;
import exceptions.FullEnviromentException;
import exceptions.InvalidActionKeyException;
import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;
import exceptions.InvalidGameException;
import exceptions.InvalidMessageException;
import exceptions.InvalidSpeachactException;
import exceptions.NotEnoughEnergyException;

/**
 * @author Daniel Friedrich
 *
 * Die Klasse repräsentiert das Contest-Szenario für den Multiagent Learning 
 * Contest auf der FGML 03 in Karlsruhe. Die Klasse ist funktionell eine 1:1 
 * Kopie der Klasse IslandScenario. Hier wurde explizit auf die Verwendung 
 * der Vererbung zur Vermeidung doppelten Codes verzichtet, um durch die 
 * Anpassungen für die FGML-Version nicht die Lauffähigkeit des Original-Servers
 * zu gefährden.
 */
public class FgmlScenario
	extends AbstractScenario
	implements IStatisticScenario {

	/**
	 * Die wahrnehmbaren Attribute der Plätze der Insel.
	 */
	protected Instances m_islandAttributes = null;

	/**
	 * Statistik-Komponente
	 */
	protected FgmlStatisticComponent m_statisticComponent;
	/**
	 * Umgebung in der das Szenario spielt.
	 */
	/**
	 * Die Umwelt, d.h. in diesem Fall die Insel.
	 */
	private IslandEnviroment m_enviroment;
	/**
	 * Vector aller aktiven Agenten
	 */

	/**
	 * Alle aktiven Agenten.
	 */
	private Vector m_activeAgents = new Vector();

	/**
	 * In dieser Runde erfolgreiche Agenten bei der Suche nach Energiezellen.
	 */
	private Hashtable m_successfulAgents = new Hashtable();
	/** 
	 * Pfad der Datei, die den Graphen für die Insel enthält.
	 */
	private String m_graphFile = 
		"islandscenario/isle300.arff";
	
	/**
	 * Pfad der Datei, die die Attributwerte für die Plätze der Insel enthält.
	 */
	private String m_attributeFile =
		"islandscenario/isle300Attributes.arff";

	/**
	 * Gibt an, ob mit der Berechnung des neuen Platzes für den Agenten begonnen
	 * werden kann.
	 */
	private boolean m_canGetCalculations = false;

	/**
	 * Gibt an, ob eine neue Runde begonnen werden kann.
	 */
	private boolean m_canStartNextRound = false;

	/**
	 * Hashtable in der die aktuellen Phasen der Agenten gespeichert werden.
	 */
	private Hashtable m_agentPhases = new Hashtable();
	
	/**
	 * Die Lernraten der Agenten.
	 */
	private Hashtable m_learningRates = new Hashtable();

	/**
	 * Aktuelle ActionKeys der Agenten
	 */
	private Hashtable m_actionKeys = new Hashtable();

	/**
	 * Counter der mitteilt, wieviel Agenten ihr Resultat noch zu erhalten haben
	 */
	private int m_returnCount = 0;

	/**
	 * Rundenzähler
	 */
	private int m_roundCount = 1;

	/**
	 * Rundenbasierte Kommunikation ?
	 */
	private boolean m_roundBasedCommunication = true;
	/**
	 * Messagebasierte Kostenberechnung?
	 */
	private boolean m_messageBasesCosts = false;

	/**
	 * Kosten für das Bewegen
	 */
	private double m_energyForMoving = 0.0;

	/**
	 * Kosten für das Graben
	 */
	private double m_energyForExploring = 0.0;

	/**
	 * Vector mit allen Platzwahlen
	 */
	private Hashtable m_placeChoices = new Hashtable();

	/**
	 * Hashtable mit den berechneten Plätzen der Agenten für die Bewegung.
	 */
	private Hashtable m_caculatedPlaces = new Hashtable();

	/**
	 * 
	 */
	private int m_calcCounter = 0;

	/**
	 * Gibt an ob das Spiel beendet ist.
	 */
	private boolean m_isGameFinished = false;

	/**
	 * szenariohandler.
	 */
	private FgmlScenarioHandler m_scenarioHandler;

	/**
	 * Komponente zum Versenden von Nachrichten
	 */
	private MessagingServer m_messageServer;

	/**
	 * Runde in der die Lernrate das nächste Mal bestimmt werden.
	 */
	private int m_learningRateNextCalc = 0;

	/**
	 * Intervall in dem die Lernrate neu berechnet wird.
	 */
	private int m_learningRateCalcIntervall = 10;

	/**
	 * Anzahl der maximal auszuführenden Runden.
	 */
	private int m_maxRounds = 0;

	/**
	 * Anzahl der aktiven Agenten im Spiel.
	 */
	private int m_numOfActiveAgents = 0;

	/**
	 * Anzahl der Agenten im Spiel.
	 */
	private int m_numOfAgents = 0;

	/**
	 * Anzahl der Agenten, die dass Spiel beendet haben.
	 */
	private int m_numOfFinishedAgents = 0;
	
	/**
	 * Menge der besuchten Plätze.
	 */
	private Hashtable m_exploredPlaces = new Hashtable();
	
	/**
	 * ServerInfoObject für den Zugriff auf die Konfigurationsdaten.
	 */
	private ServerInfoObject m_sInfoObject; 	

	/**
	 * Konstruktor
	 * 
	 * @param visHandler
	 * @param parameter
	 * @throws InvalidAttributeOrPlaceNumberException
	 */
	public FgmlScenario(
		Id gameId,
		IInformationHandler visHandler,
		Hashtable parameter)
		throws InvalidElementException {
		super(gameId, visHandler, parameter);
		
		
		m_enviroment = new IslandEnviroment();
		// sInfoObject holen und Pfade bestimmen.		
		m_sInfoObject = ServerInfoObject.getInstance();
		m_graphFile = 
			m_sInfoObject.getEnviromentsPath() 
			+ "islandscenario/isle300.arff";
		m_attributeFile = 
			m_sInfoObject.getEnviromentsPath() 
			+  "islandscenario/isle300Attributes.arff";			
		
		System.out.println("Graphfile: " + m_graphFile);
		System.out.println("Attributefile: " + m_attributeFile);	
		Instances instances =
			m_enviroment.createEnviroment(m_graphFile, m_attributeFile);

		this.initializeLerningRateExamples(instances);

		m_energyForExploring = 6.0;
		m_energyForMoving = 3.0;
		m_maxRounds = 30;
		m_parameter.put("RoundLimit",new Integer(m_maxRounds));
		m_parameter.put("EnergyForDigging",new Double(m_energyForExploring));
		m_parameter.put("EnergyForMoving", new Double(m_energyForMoving));
		m_parameter.put("EnergyForCommunication",new Double(1.0));
		m_parameter.put("EnergyCell", new Double(70.0));
		m_parameter.put("StartEnergy", new Double(150.0));
		m_parameter.put("numExps", new Integer(1));
		m_parameter.put("RoundBased",Boolean.TRUE);
		
	}

	private void initializeLerningRateExamples(Instances instances) {
		m_islandAttributes =
			new Instances(instances.firstInstance().dataset(), 0);
		m_islandAttributes.setClassIndex(instances.numAttributes() - 1);

		Enumeration enum = instances.enumerateInstances();
		while (enum.hasMoreElements()) {
			Instance place = (Instance) enum.nextElement();
			boolean duplicate = false;
			Term placeTerm = new Term(place);
			for (int i = 0; i < m_islandAttributes.numInstances(); i++) {
				if (placeTerm
					.equals(new Term(m_islandAttributes.instance(i)))) {
					duplicate = true;
				}
			}
			if (!duplicate) {
				m_islandAttributes.add(place);
			}
		}
		System.out.println(
			""
				+ (m_islandAttributes.numInstances() + 1)
				+ " Beispiele vorhanden.");
	}

	/**
	 * 
	 */
	public FgmlScenario() throws InvalidElementException {
		super();
		m_enviroment = new IslandEnviroment();
		// sInfoObject holen und Pfade bestimmen.		
		m_sInfoObject = ServerInfoObject.getInstance();
		m_graphFile = 
			m_sInfoObject.getEnviromentsPath() 
			+ "islandscenario/isle300.arff";
		m_attributeFile = 
			m_sInfoObject.getEnviromentsPath() 
			+  "islandscenario/isle300Attributes.arff";			
		Instances instances =
			m_enviroment.createEnviroment(m_graphFile, m_attributeFile);
		this.initializeLerningRateExamples(instances);
	}

	/* (non-Javadoc)
	 * @see simulator.AbstractScenario#simulateGame()
	 */ /*
			public void simulateGame(IStatisticComponent component)
				throws
					InvalidAgentException,
					NotEnoughEnergyException,
					InvalidElementException {*/
	public void simulateGame()
		throws
			InvalidAgentException,
			NotEnoughEnergyException,
			InvalidElementException {
		try {
			m_visHandler.log(
				m_gameId,
				IVisualisation.StateMsg,
				"Starte mit der Simulation des Spiels");
		} catch (InvalidGameException ige) {
			throw new InvalidElementException("Spiel " + m_gameId.toString());
		}

		// Neuen MessageServer erstellen
		if (m_messageServer != null) {
			m_messageServer = null;
		}
		m_messageServer =
			new MessagingServer(
				this,
				m_visHandler,
				m_gameId,
				m_parameter,
				m_statisticComponent);

		m_learningRateNextCalc = m_learningRateCalcIntervall;
		
		// Lernraten beseitigen 
		m_learningRates.clear();
		
		Vector ids = new Vector();
		/*
		 * Alle Agenten der Reihe nach abarbeiten, dabei für jeden Agenten einen
		 * neuen ActioKey erzeugen und in die Liste abspeichern. Den Agenten 
		 * der Umwelt hinzufügen, aktivieren und der Liste der aktiven Agenten
		 * hinzufügen.
		 */
		Enumeration agents = m_agents.elements();
		while (agents.hasMoreElements()) {

			AbstractAgent agent = (AbstractAgent) agents.nextElement();
			// neuen ActionKey generieren
			ActionKey key = new ActionKey();
			// diesen in Liste abspeichern
			m_actionKeys.put(agent.getId().toString(), key);
			// Key beim Agenten setzen 			
			agent.setActionKey(key);

			m_agentPhases.put(agent.getId().toString(), new Integer(1));
			// den Agenten der Umwelt hinzufügen
			try {
				m_enviroment.addAgentToEnviroment(agent.getId());
			} catch (FullEnviromentException fee) {
				throw new InvalidAgentException(agent.getId().toString(), fee);
			}
			// Agent aktivieren
			agent.start();
			// Agent den aktiven Agenten zufügen
			m_activeAgents.add(agent);
			m_numOfActiveAgents++;
			// Loggen
			try {
				m_visHandler.log(
					agent.getId(),
					m_gameId,
					IVisualisation.StateMsg,
					"Aktiviere Agent " + agent.getAgentName());
			} catch (InvalidGameException ige) {
				throw new InvalidElementException(
					"Spiel " + m_gameId.toString());
			}
			/*
			 * Alle Lernraten mit 0.0 initialisieren
			 */
			m_learningRates.put(agent.getId().toString(), new Double(0.0));
			
			/*
			 * Registriere Agent beim MessageServer wenn er das Interface
			 * IMessageAgent implmentiert.
			 */
			if (agent instanceof IMessagingAgent) {
				m_messageServer.registerAgent(agent);
			}
			ids.addElement(agent.getId());

		}

		// Rundenzahlen bestimmen
		m_roundCount = 1;

		// Ab hier beginnt die Abarbeitung der Runden.
		//while((m_maxRounds+1 > m_roundCount) && (m_activeAgents.size() > 0)) {
		while ((m_maxRounds + 1 > m_roundCount) && (m_numOfActiveAgents > 0)) {
			System.out.println("Runde: " + m_roundCount);
			// Logge den Beginn einer neuen Runde
			try {
				m_visHandler.log(
					m_gameId,
					IVisualisation.StateMsg,
					"Simuliere die " + m_roundCount + ". Runde des Spiels\n");
			} catch (InvalidGameException ige) {
				throw new InvalidElementException(
					"Spiel " + m_gameId.toString());
			}

			/*
			 * Alle aktiven Agenten die Runde starten lassen.
			 */
			Enumeration activeAgents = m_activeAgents.elements();
			while (activeAgents.hasMoreElements()) {
				AbstractAgent activeAgent =
					(AbstractAgent) activeAgents.nextElement();
				// Agent aktivieren
				activeAgent.startNewRound(true);
				// nächste Runde für die Statistik des Agenten einläuten
				m_statisticComponent.nextRound(activeAgent.getId());

			}

			// Jetzt arbeiten die Agenten

			/*
			 * Darauf warten das alle Agenten ihre MoveTo-Aktion ausführen 
			 * wollen.
			 */
			//while(!(m_finishedAgents.size() == m_activeAgents.size())) {}
			while (!(m_numOfFinishedAgents == m_numOfActiveAgents)) {
			}

			/* 
			 * Setze das Flag zum Beginnen der nächsten Runde bei allen Agenten 
			 * zurück.
			 */
			Enumeration enum = m_activeAgents.elements();
			while (enum.hasMoreElements()) {
				((AbstractAgent) enum.nextElement()).startNewRound(false);
			}

			/*
			 * Starte die Berechnung der Plätze, wenn die Berechnung der Plätze
			 * fertig ist, dann gibt die Methode die Ergebnisse für die Agenten
			 * durch Freischalten des Flags m_canGetCalculation
			 */
			this.startCalculation();

			/*
			 * Warten bis alle Agenten ihre neuen Plätzen übermittelt bekommen 
			 * haben.
			 */
			while (m_returnCount > 0) {
			}

			// nehme das Flag zum Holen der Berechungsergebnisse zurück.
			this.resetCanGetCalculationFlag();

			/*
			 * Logge das Ende der Runde und erledige noch ein paar 
			 * Aufraumaufgaben
			 */
			try {
				m_visHandler.log(
					m_gameId,
					IVisualisation.StateMsg,
					"Beende die Simulation der "
						+ m_roundCount
						+ ". Runde des Spiels\n");
			} catch (InvalidGameException ige) {
				throw new InvalidElementException(
					"Spiel " + m_gameId.toString());
			}
			m_successfulAgents.clear();

			m_numOfFinishedAgents = 0;

			if (m_learningRateNextCalc == m_roundCount) {
				m_learningRateNextCalc =
					m_learningRateNextCalc + m_learningRateCalcIntervall;
				System.out.println(
					"Lerne wieder in Runde: " + m_learningRateNextCalc);
			}			

			// Zähle Rundenzähler eins hoch
			m_roundCount++;
		}
		Enumeration toDeactivedAgents = m_agents.elements();
		while (toDeactivedAgents.hasMoreElements()) {
			AbstractAgent agent =
				(AbstractAgent) toDeactivedAgents.nextElement();
			agent.finishSimulation();
		}
		m_activeAgents.clear();
		//m_unActiveAgents.clear();
		try {
			m_visHandler.log(
				m_gameId,
				IVisualisation.StateMsg,
				"Beende die Simulation des Spiels.\n");
		} catch (InvalidGameException ige) {
			throw new InvalidElementException("Spiel " + m_gameId.toString());
		}

	}

	/* (non-Javadoc)
	 * @see simulator.AbstractScenario#createNewScenarioHandler()
	 */
	public AbstractScenarioHandler createNewScenarioHandler() {

		FgmlScenarioHandler handler = new FgmlScenarioHandler(this);
		if (m_scenarioHandler == null) {
			m_scenarioHandler = handler;
		}
		return handler;

	}

	/**
	 * Gibt die Attributewerte des Platzes zurück, nur in Discovery-Phase.
	 * @param agentId
	 * @return Instance - Attributwerte als Instance aus dem Weka-System
	 * @throws InvalidAgentException
	 * @throws UncorrectPhaseException
	 */
	public Instance getAttributesForPlace(Id agentId)
		throws InvalidAgentException, InvalidActionKeyException {

		Instance unfilteredInstance =
			m_enviroment.getPlaceForAgent(agentId).getInstance(agentId);
		try {
			ClassFilter classFilter = new ClassFilter();
			if (classFilter.input(unfilteredInstance)) {
				/*System.out.println("Ungefilterte Instanz: "
					+ unfilteredInstance.toString());
				System.out.println("Gefilterte Instanz: "
					+ classFilter.output().toString());*/
				return classFilter.output();
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Gibt zurück, ob am Platz schon gegraben wurde, nur in discovery-Phase.
	 *  
	 * @param agentId
	 * @return boolean - True Platz ist ausgebeutet / False Platz ist nicht ausgebeutet
	 * @throws InvalidAgentException
	 * @throws UncorrectPhaseException
	 */
	public boolean isPlaceExplored(Id agentId)
		throws InvalidAgentException, InvalidActionKeyException {
		if (m_enviroment.getPlaceForAgent(agentId).isExplored(agentId)) {
			System.out.println(
				"Platz "
					+ m_enviroment.getPlaceForAgent(agentId).getId().toString()
					+ " des Agenten "
					+ agentId.toString()
					+ " ist schon ausgebeutet.");
		}
		return m_enviroment.getPlaceForAgent(agentId).isExplored(agentId);
	}

	/**
	 * Liefert die möglichen Zielplätze, die vom Platz aus zu erreichen sind.
	 * 
	 * @param agentId
	 * @return Vector - Menge der möglichen Zielplätze
	 * @throws InvalidAgentException
	 * @throws UncorrectPhaseException
	 */
	public Vector getPossibleTargetPlaceForMoving(Id agentId)
		throws InvalidAgentException {

		return m_enviroment.getPlaceForAgent(agentId).getReachablePlaces();
	}

	/**
	 * Liefert einen Spielparameter zurück.
	 * 
	 * @param String name - Name des Parameters
	 * @param agentId
	 * @return Object - Wert des Parameters
	 * @throws InvalidElementException
	 */
	public Object getParameter(String name) throws InvalidElementException {

		if (m_parameter.containsKey(name)) {
			return m_parameter.get(name);
		} else {
			throw new InvalidElementException(name);
		}
	}

	/**
	 * Liefert die aktuelle Energie des Agenten zurück.
	 * 
	 * @param agentId
	 * @return double - Aktuelle Energie des Agenten.
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 */
	public double getMyEnergyValue(Id agentId)
		throws InvalidAgentException, InvalidActionKeyException {
		try {
			m_visHandler.log(
				agentId,
				m_gameId,
				IVisualisation.StateMsg,
				"Das aktuelle Energieniveau des Agenten "
					+ agentId.toString()
					+ " beträgt "
					+ Double.toString(this.getEnergyFromAgent(agentId)));
		} catch (Exception e) {
		}
		return this.getEnergyFromAgent(agentId);
	}

	/**
	 * Bewegt einen Agenten zum Ende der Runde an seinen neuen Platz.
	 * 
	 * @param key
	 * @param agentId
	 * @param places
	 * @return Id - Id des Platzes an den sich der Agent bewegt.
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 */
	public Id moveTo(Id agentId, LinkedList places)
		throws InvalidAgentException, InvalidElementException {
		Id retval = null;
		// Id des bisherigen Platz für die statistische Auswertung mitloggen
		Id oldPlace = m_enviroment.getPlaceForAgent(agentId).getId();
		try {
			/*
			 * überprüfe ob sich der Agent die Bewegung überhaupt leisten kann
			 * oder ob er aus dem Spiel entfernt werden muss. Wenn der Agent
			 * über zuwenig Energie verfügt, dann deaktiviere den Agenten,
			 * entferne ihn aus der Umwelt und füge ihn abschliessend in die 
			 * Liste der inaktiven Agenten hinzu.
			 */
			if ((places.size() > 0)
				&& (!this
					.checkEnergy(
						agentId,
						((Double) m_parameter.get("EnergyForMoving"))
							.doubleValue()))) {
				//	Agent removen
				m_enviroment.removeAgentFromEnviroment(agentId);
				AbstractAgent eAgent =
					(AbstractAgent) m_agents.get(agentId.toString());
				m_activeAgents.remove(eAgent);
				//m_unActiveAgents.addElement(eAgent);		
				// Agent stoppen
				eAgent.finishSimulation();
				return null;
			} else {
				// Speichere Platzwahl
				if ((places != null) && (places.size() > 0)) {
					m_placeChoices.put(agentId.toString(), places);
				}
				m_numOfFinishedAgents++;
				//m_finishedAgents.addElement(m_agents.get(agentId.toString()));
				/*
				 * Erhöhe den Zähler der registriert, wieviel Agenten eine 
				 * Rückantwort erwarten.
				 */
				m_returnCount++;

				/*
				 * Warten bis die Berechung der Plätze für alle Agenten durch-
				 * geführt wurde.
				 */
				//System.out.println("Energie :" + Double.toString(this.getEnergyFromAgent(agentId))); 
				while (!this.canGetCalculations()) {
				}

				/*
				 * Hole die Id des berechneten neuen Platzes des Agenten aus der 
				 * Liste der berechneten neuen Plätze, wenn die Id != null ist,
				 * dann berechne dem Agenten die Kosten für die Bewegung und
				 * bewege den Agenten. Danach logge das Ergebnis des moveTo.
				 */
				retval = this.getCalculatedPlace(agentId);
				if (retval != null) {
					m_enviroment.moveAgentToPlace(agentId, retval);
					// Energie abziehen
					this.reduceEnergyFromAgent(
						agentId,
						((Double) m_parameter.get("EnergyForMoving"))
							.doubleValue());
				} else {
					this.log(
						agentId,
						IVisualisation.StateMsg,
						"Der Agent "
							+ agentId.toString()
							+ " verbleibt an seinem Platz,"
							+ m_enviroment
								.getPlaceForAgent(agentId)
								.getId()
								.toString()
							+ " da er keinen "
							+ " Bewegungswunsch geäussert hat.");
					System.out.println(
						"Agent "
							+ agentId.toString()
							+ " verbleibt an seinem Platz");
					retval =
						(Id) m_enviroment.getPlaceForAgent(agentId).getId();
				}
			}
		} catch (InvalidElementException nse) {
			throw new InvalidElementException(
				"Zielplatz des Agenten " + agentId.toString());
		}
		double learningRate = 0.0;
		// Kontrollieren ob der Agent die Phase gewechselt hat, das speichern
		boolean phaseChange = this.checkPhaseChange(agentId, m_roundCount);

		if ((m_roundCount == m_learningRateNextCalc)
			|| (m_roundCount == m_maxRounds)
			|| (phaseChange)) {
			learningRate = this.calculateLerningRateForAgent(agentId);

			/*
			 * Lernrate zwischenspeichern, damit sie in den Zwischenrunden immer 
			 * an den Client gesendet werden kann.
			 */
			m_learningRates.put(agentId.toString(), new Double(learningRate));

			try {
				// Melde die Veränderung an die Vis-Komponente
				m_visHandler.updateVisParameter(
					IVisualisation.LearningRate,
					agentId,
					m_gameId,
					learningRate);
			} catch (InvalidElementException nse) {
				throw new InvalidElementException(
					"Visualisierung des Agenten " + agentId.toString());
			} catch (InvalidGameException ige) {
				throw new InvalidElementException(
					"Spiel " + m_gameId.toString());
			}
		}
		
		try {
			// Melde die Veränderung an die Vis-Komponente
			m_visHandler.updateVisParameter(
				IVisualisation.EnergyValue,
				agentId,
				m_gameId,
				this.getEnergyFromAgent(agentId));
			// Melde die letze erfasste Lernrate an die Vis-Komponente
			m_visHandler.updateVisParameter(
				IVisualisation.LearningRate,
				agentId,
				m_gameId,
				((Double)m_learningRates.get(agentId.toString())).doubleValue());				
		} catch (InvalidElementException nse) {
			throw new InvalidElementException(
				"Visualisierung des Agenten " + agentId.toString());
		} catch (InvalidGameException ige) {
			throw new InvalidElementException(
				"Spiel " + m_gameId.toString());
		}		
		/*
		 * Reduziere den Zähler der die Agenten die noch eine Antwort bekommen
		 * müssen um eins.
		 */
		m_returnCount--;

		// Hier noch statistische Auswertung
		try {
			if (!oldPlace.equals(retval)) {
				// Die neue Platzwahl an Statistik senden 
				m_statisticComponent.addInformation(
					agentId,
					FgmlStatisticComponent.VISITEDPLACES,
					new Integer(1));
				// Bewegungskosten als Restkosten loggen
				m_statisticComponent.addInformation(
					agentId,
					FgmlStatisticComponent.COSTS,
					new Double(m_energyForMoving));
			}
			//System.out.println("Bewegungskosten: " + m_energyForMoving);	
			// Wenn eine Lernrate berechnet wurde dann diese an die Stat senden
			if (learningRate > 0.1) {
				// Die Lernrate an Statistik senden	
				m_statisticComponent.addInformation(
					agentId,
					FgmlStatisticComponent.LEARNINGRATE,
					new Double(learningRate));
			}

			// Kontrolliere ob eine Energiezelle übersehen wurde.
			Place place = m_enviroment.getPlace(oldPlace);
			if ((place.containsEnergyCell()) && (!place.isExplored())) {
				m_statisticComponent.addInformation(
					agentId,
					FgmlStatisticComponent.MISSEDCELLS,
					new Double(1.0));
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return retval;
	}

	/**
	 * Check ob ein Agent, wirklich der ist für den er sich ausgibt.
	 * 
	 * Die Identität des Agenten ist dann gesichert, wenn der übergebene 
	 * Schlüssel mit dem beim Szenario gespeicherten Schlüssel für den Agenten
	 * übereinstimmt.  
	 * 
	 * @param key Actionkey - Authentitifzierungsschlüssel des Agenten
	 * @param agentId - Id des Agenten
	 * @return boolean - True Identität gesichert / False Identität falsch
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 */
	public synchronized boolean checkIdentity(ActionKey key, Id agentId) {
		if (m_actionKeys.containsKey(agentId.toString())) {
			if (key.equals(m_actionKeys.get(agentId.toString()))) {
				this.setNewActionKey(agentId);
				return true;
			}
		}
		return false;
	}

	/**
	 * Berechnet den neuen Platz eines Agenten in der nächsten Runde
	 * 
	 * @param agentId
	 * @param places
	 * @return Id - Id liefert die Id des berechneten Platz.
	 */
	private Id getCalculatedPlace(Id agentId) {
		Id retval = null;
		try {
			if (m_caculatedPlaces.containsKey(agentId.toString())) {
				retval = (Id) m_caculatedPlaces.get(agentId.toString());

				m_visHandler.log(
					agentId,
					m_gameId,
					IVisualisation.StateMsg,
					"Der Agent "
						+ agentId.toString()
						+ " kann sich an Platz "
						+ retval.toString()
						+ " bewegen.");
			} else {
				m_visHandler.log(
					agentId,
					m_gameId,
					IVisualisation.StateMsg,
					"Der Agent "
						+ agentId.toString()
						+ " bleibt an seinem Platz.");
			}
		} catch (Exception e) {
			System.out.println("Fehler: Problem beim Logging");
			e.printStackTrace(System.out);
		}
		return retval;
	}

	/**
	 * Erzeugt einen neuen ActionKey und speichert diesen beim Agenten.
	 * 
	 * @param agentId
	 */
	public synchronized void setNewActionKey(Id agentId) {
		// neuer ActionKey für den Agenten
		IFgmlScenarioAgent iipsAgent =
			((IFgmlScenarioAgent) m_agents.get(agentId.toString()));
		ActionKey newKey = new ActionKey();
		//m_actionKeys.remove(agentId.toString());
		m_actionKeys.put(agentId.toString(), newKey);
		iipsAgent.setActionKey(newKey);
	}

	/**
	 * Fährt für Agent Grabung durch und informatiert ihn dann über das Ergebnis.
	 * 
	 * @param key
	 * @param agentId
	 * @return boolean - True Energiezelle gefunden / False keine Zelle gefunden
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 */
	public boolean explorePlace(Id agentId)
		throws InvalidAgentException, InvalidElementException {
		boolean cellFound = false;
		Place tmpPlace = m_enviroment.getPlaceForAgent(agentId);
		if (this
			.checkEnergy(
				agentId,
				((Double) m_parameter.get("EnergyForDigging")).doubleValue()));
		if (tmpPlace.explore(agentId)) {
			/*
			 * dem Agenten die Energie-
			 * Differenz gutschreiben.
			 */
			this.addEnergyToAgent(
				agentId,
				((Double) m_parameter.get("EnergyCell")).doubleValue()
					- ((Double) m_parameter.get("EnergyForDigging"))
						.doubleValue());
			// vermerke das Agent erfolgreich war in dieser Runde.
			m_successfulAgents.put(agentId.toString(), agentId);
			cellFound = true;
		} else {
			/*
			 * Energie abziehen
			 */
			this.reduceEnergyFromAgent(
				agentId,
				((Double) m_parameter.get("EnergyForDigging")).doubleValue());
		}
		// Statistische Auswertung
		try {
			// Erst Kosten melden
			m_statisticComponent.addInformation(
				agentId,
				FgmlStatisticComponent.COSTS,
				new Double(m_energyForExploring));
			// Grabungsversuche melden	
			m_statisticComponent.addInformation(
				agentId,
				FgmlStatisticComponent.EXPLORATIONS,
				new Integer(1));
			if (cellFound) {
				// Jetzt melden ob eine Energy gefunden wurde 
				m_statisticComponent.addInformation(
					agentId,
					FgmlStatisticComponent.FOUNDEDCELLS,
					new Double(1.0));
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return true;
	}

	/**
	 * Gibt an ob mit der nächsten Runde begonnen werden kann.
	 * 
	 * @return boolean
	 */
	private boolean canStartNextRound() {
		return m_canStartNextRound;
	}

	/**
	 * Gibt an ob mit der Berechnung der Plätze usw. angefangen werden kann.
	 * 
	 * @return boolean
	 */
	private boolean canGetCalculations() {
		return m_canGetCalculations;
	}

	private void startCalculation() {
		m_caculatedPlaces = new Hashtable();
		m_calcCounter++;
		Hashtable preSelection = new Hashtable();
		while (m_placeChoices.size() != 0) {
			Hashtable roundSelection = new Hashtable();
			// Wünsche für diese Runde holen
			Enumeration keys = m_placeChoices.keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				LinkedList places = (LinkedList) m_placeChoices.get(key);
				// Wunsch auslesen
				Id selection = (Id) places.get(0);
				/*
				 * Erst kontrollieren ob schon besetzt oder ob der Wunsch schon 
				 * von einem anderen Agenten diese Runde geäussert wurde.
				 */
				try {
					if (m_enviroment.getPlace(selection).isEmpty()) {
						if (roundSelection.containsKey(selection)) {
							// beide Verwerfen.
							roundSelection.remove(selection);
						} else {
							// Wunsch eintragen
							roundSelection.put(selection, key);
						}
					}
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}
				places.remove(0);
				// Wenn kein weiterer Wunsch mehr enthalten ist, dann weg
				if (places.size() == 0) {
					m_placeChoices.remove(key);
				}
			}
			/*
			 * Rundenwünsche in die preselection eintragen, existiert der Wunsch 
			 * dort schon, dann den aktuellen Wunsch verwerfen, ansonsten die 
			 * weiteren Wünsche des Agenten aus der Wunschliste entfernen und
			 * den aktuellen Wunsch in die preselection eintragen
			 */
			Enumeration entries = roundSelection.keys();
			while (entries.hasMoreElements()) {
				Id entry = (Id) entries.nextElement();
				if (!preSelection.containsKey(entry)) {
					Object obj = roundSelection.get(entry);
					preSelection.put(entry, obj);
					m_placeChoices.remove(obj);
				}
			}
		}
		// Wünsche in ordnungsgemäße Hashtable eintragen.
		Enumeration entries = preSelection.keys();
		while (entries.hasMoreElements()) {
			Id entry = (Id) entries.nextElement();
			m_caculatedPlaces.put(preSelection.get(entry), entry);
		}
		// Gebe die berechneten Agenten frei. 
		m_canGetCalculations = true;
	}

	/**
	 * Hebt die Wirkung des Starte die Berechnungen Flags auf.
	 */
	private void resetCanGetCalculationFlag() {
		m_canGetCalculations = false;
	}

	/**
	 * Hebt die Wirkung des Starte nächste Runde Flags auf.
	 */
	private void resetCanStartNextRoundFlag() {
		m_canStartNextRound = false;
	}

	/**
	 * Berechnet die Lernrate des Agenten mit der übergebenen Id.
	 * 
	 * @param Id agentId - Id des Agenten dessen Lernrate berechnet werden soll
	 * @return double - Lernrate
	 */
	private double calculateLerningRateForAgent(Id agentId)
		throws InvalidAgentException {
		double rate = 0.0;
		//System.out.println("#### Beginne Calculation der Lernrate ##########");
		if (m_agents.containsKey(agentId.toString())) {
			IFgmlScenarioAgent agent =
				(IFgmlScenarioAgent) m_agents.get(agentId.toString());
			//System.out.println("#### Agent ist bekannt ##########");	
			if (((AbstractAgent) agent).isALearningAgent()) {
				//System.out.println("#### Agent ist ein Learning Agent ##########");
				Classifier hypothesis = agent.getHypothesis();
				if (hypothesis != null) {
					try {
						int correctClassifiedInstances = 0;
						int numPlaces = 0;
						//Enumeration places = m_enviroment.getPlaces().elements();
						Enumeration places =
							m_islandAttributes.enumerateInstances();
						while (places.hasMoreElements()) {
							//Place place = (Place)places.nextElement();
							Instance placeInstance =
								(Instance) places.nextElement();
							//double classValue = place.getInstance().classValue();
							double classValue = placeInstance.classValue();
							// Filtere Klasse heraus.
							ClassFilter filter = new ClassFilter();
							// Kopie dafür erstellen
							boolean test = filter.input(placeInstance);
							Instance filteredInstance;
							if (test)
								filteredInstance = filter.output();
							else
								return 0.0;
							// Klassifiziere Beispiel
							//System.out.println("Klassifiziere");
							double result = 0.0;
							if (hypothesis instanceof DontCareExpandHalfQMCClassifier) {
								result = ((DontCareExpandHalfQMCClassifier) hypothesis)
									.classifyInstance(filteredInstance);
							} else if (hypothesis instanceof DontCareExpandQMClassifier) {
								result = ((DontCareExpandQMClassifier) hypothesis)
									.classifyInstance(filteredInstance);
							} else if (hypothesis instanceof Id3) {
								result = ((Id3) hypothesis)
									.classifyInstance(filteredInstance);
							} else if (hypothesis instanceof NaiveBayes) {
								result = ((NaiveBayes) hypothesis)
									.classifyInstance(filteredInstance);
							} else {
								result = 
									hypothesis.classifyInstance(filteredInstance);
							}

							double difference = result - classValue;
								//hypothesis.classifyInstance(filteredInstance)
								// - classValue;
							if ((difference > -0.1) && (difference < 0.1)) {
								correctClassifiedInstances++;
							}else {
								System.out.println("Fehler: " + filteredInstance.toString() + "->" + Double.toString(classValue));
							}
							numPlaces++;
							//System.out.print(".");
						}
						System.out.println("klassifierziert: " + numPlaces);
						rate =
							(((double) correctClassifiedInstances)
								/ ((double) numPlaces)
								* 100);
						//System.out.println("#### Lernrate berechnet ##########");		
					} catch (Exception e) {
						try {
							//System.out.println("#### Fehler beim berechnen der Lernrate ##########");
							this.log(
								agentId,
								IVisualisation.StateMsg,
								"Fehler beim Berechnen der Lernrate beim Agenten "
									+ agentId.toString());

							e.printStackTrace(System.out);
						} catch (InvalidElementException nse) {
							System.out.println(
								"#### Logging klappt nicht. ##########");
						}
					}
				} /*else {
										System.out.println("#### HYPOTHESE == NULL ##########");
									}*/
			} /*else {
								System.out.println("#### Der Agent ist kein lernender Agent ##########");										
							}*/
		} else {
			throw new InvalidAgentException(agentId.toString());
		}

		return rate;
	}

	/**
	 * Gibt die Id des Platzes zurück, an dem der Agent sich befindet.
	 * 
	 * @param Id agentId - Id des Agenten der die Information benötigt.
	 * 
	 * @return Id
	 * 
	 * @throws InvalidActionKeyException
	 * @throws InvalidAgentException
	 */
	public Id getPlaceId(Id agentId)
		throws InvalidActionKeyException, InvalidAgentException {
		Place place = m_enviroment.getPlaceForAgent(agentId);
		return place.getId();
	}

	/**
	 * Liefert alle Agenten zurück mit denen der Agent kommunizieren kann.
	 * 
	 * @param Id agentId - Id des Agenten der kommunizieren möchte.
	 * 
	 * @return Vector - Vector mit den Agenten zur Kommunikation
	 * 
	 * @throws InvalidActionKeyException
	 * @throws InvalidAgentException
	 */
	public Vector getForCommunicationReachableAgents(Id agentId)
		throws
			InvalidActionKeyException,
			InvalidAgentException,
			InvalidElementException {
		Object tmp = m_parameter.get("CommunicationDistance");
		if (!(tmp == null) || !(tmp instanceof Integer)) {
			/*
			 * Es gibt keine maximale Kommunikationsdistanz und damit werden
			 * die Ids aller aktiven Agenten zurückgegeben.
			 */
			Vector retval = new Vector();
			//for(int i=0; i < m_activeAgents.size();i++) {
			for (int i = 0; i < m_numOfActiveAgents; i++) {
				retval.addElement(
					((AbstractAgent) m_activeAgents.get(i)).getId());
			}
			return retval;
		}
		int distance =
			((Integer) m_parameter.get("CommunicationDistance")).intValue();
		return m_enviroment.getNearestAgents(
			m_enviroment.getPlaceForAgent(agentId).getId(),
			distance);
	}

	/**
	 * Gibt an ob der Agent in dieser Runde eine Energiezelle gefunden hat.
	 * 
	 * @param Id agentId - Id des Agenten
	 * @return boolean
	 */
	public boolean energyCellFounded(Id agentId) {
		if (m_successfulAgents.containsKey(agentId.toString())) {
			/*
			try {
				m_statisticComponent.addInformation(
					agentId, 
					IslandStatisticComponent.FOUNDEDCELLS,
					new Integer(1));
			}catch(Exception e) {
				e.printStackTrace(System.out);
			}*/
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see simulator.AbstractScenario#reset()
	 */
	public void reset() {
		// Alte Identitäten wegwerfen
		m_actionKeys.clear();
		// Aktive, unaktive sowie finishedAgents und deren Energiewerte resten
		//m_unActiveAgents.clear();
		m_activeAgents.clear();
		//m_finishedAgents.clear();
		m_numOfFinishedAgents = 0;
		m_numOfActiveAgents = 0;
		m_agentsEnergy.clear();
		Enumeration agents = m_agents.elements();
		// Flags wieder zurücksetzen.
		m_canGetCalculations = false;
		m_canStartNextRound = false;
		m_enviroment = new IslandEnviroment();
		try {
			m_enviroment.createEnviroment(m_graphFile, m_attributeFile);
		} catch (Exception e) {
			System.out.println("Fehler:");
			e.printStackTrace(System.out);
		}
		// Visualisierungen reseten
		try {
			m_visHandler.resetVisualisations(m_agents);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		m_agents.clear();
	}

	/**
	 * Sendet eine Nachricht an einen Agenten und liefert auch gleich die Antwort.
	 * 
	 * @param key - Identitätskey des Senders
	 * @param act - Id des Senders
	 * @return Spechact - Antwort des Empfängers der Nachricht.
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 * @throws InvalidMessageException
	 * @throws InvalidSpeachactException
	 * @throws NotEnoughEnergyException
	 */
	public Speachact sendMessage(ActionKey key, Speachact act)
		throws
			InvalidAgentException,
			InvalidActionKeyException,
			InvalidMessageException,
			InvalidSpeachactException,
			NotEnoughEnergyException {
		return m_messageServer.sendMessage(act);
	}

	/* (non-Javadoc)
	 * @see simulator.statistic.IStatisticScenario#createNewStatisticComponent(java.util.Vector)
	 */
	public IStatisticComponent createNewStatisticComponent(Vector agentIds) {
		m_statisticComponent =
			new FgmlStatisticComponent(agentIds, m_parameter);
		return m_statisticComponent;
	}

	/* (non-Javadoc)
	 * @see simulator.statistic.IStatisticScenario#getEnviromentStatisticParameters()
	 */
	public Hashtable getEnviromentStatisticParameters() {
		Hashtable retval = new Hashtable();
		String agentenname = "";
		if (m_agents.size() > 0) {
			agentenname =
				((IFgmlScenarioAgent) m_agents.elements().nextElement())
					.getStrategyString();
		}
		retval.put("Strategie: ", agentenname);
		retval.put("Szenario", m_parameter.get("ScenarioName"));
		retval.put("Plätze", new Integer(m_enviroment.numPlaces()));
		retval.put(
			"E-Zellen",
			new Integer(m_enviroment.numPlacesWithEnergyCells()));
		return retval;
	}

	/**
	 * Überprüft ob sich die aktuelle Phase eines Agenten in der Runde geändert hat.
	 * 
	 * @param agentId - Id des Agenten, dessen akt. Phase überprüft wird. 
	 * @param round - Runde für die Überprüfung.
	 * @return boolean - True Phase hat sich geändert / False nicht geändert.
	 */
	private boolean checkPhaseChange(Id agentId, int round) {
		// Agent holen
		IFgmlScenarioAgent agent =
			(IFgmlScenarioAgent) m_agents.get(agentId.toString());
		// letzte gespeicherte Phase holen
		int lastSavedPhase =
			((Integer) m_agentPhases.get(agentId.toString())).intValue();
		// aktuelle Phase holen
		int actualPhase = agent.getPhase();
		if (lastSavedPhase != actualPhase) {
			try {
				m_agentPhases.put(agentId.toString(), new Integer(actualPhase));
				if (actualPhase == 2) {
					m_statisticComponent.addInformation(
						agentId,
						FgmlStatisticComponent.STARTPHASEII,
						new Integer(round));
				} else {
					m_statisticComponent.addInformation(
						agentId,
						FgmlStatisticComponent.STARTPHASEIII,
						new Integer(round));
				}
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
			//System.out.println("Phasenwechsel");
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see simulator.AbstractScenario#getScenarioParameter()
	 */
	public LinkedList getScenarioParameter() {
		LinkedList parameterList = new LinkedList();
		parameterList.add(
			new ParameterDescription(
				"ScenarioName",
				ParameterDescription.StringType,
				new String("Fgml"),
				false));		
		parameterList.add(
			new ParameterDescription(
				"Klassifikationsschwelle",
				ParameterDescription.IntegerType,
				new Integer(8)));	
		parameterList.add(
			new ParameterDescription(
				"Kommunikationsschwelle",
				ParameterDescription.IntegerType,
				new Integer(25)));
		parameterList.add(
			new ParameterDescription(
				"RoundLimit",
				ParameterDescription.IntegerType,
				new Integer(30),
				false));
		parameterList.add(
			new ParameterDescription(
				"EnergyForDigging",
				ParameterDescription.DoubleType,
				new Double(6.0),
				false));
		parameterList.add(
			new ParameterDescription(
				"EnergyForMoving",
				ParameterDescription.DoubleType,
				new Double(3.0),
				false));
		parameterList.add(
			new ParameterDescription(
				"EnergyForCommunication",
				ParameterDescription.DoubleType,
				new Double(1.0),
				false));
		parameterList.add(
			new ParameterDescription(
				"EnergyCell",
				ParameterDescription.DoubleType,
				new Double(70.0),
				false));
		parameterList.add(
			new ParameterDescription(
				"StartEnergy",
				ParameterDescription.DoubleType,
				new Double(150.0),
				false));
		parameterList.add(
			new ParameterDescription(
				"numExps",
				ParameterDescription.IntegerType,
				new Integer(1),
				false));					
		parameterList.add(
			new ParameterDescription(
				"RoundBased",
				ParameterDescription.BooleanType,
				Boolean.TRUE,
				false));													
		return parameterList;
	}
	
	/* (non-Javadoc)
	 * @see simulator.AbstractScenario#initializeListOfAllowedSpeachactClasses()
	 */
	public void initializeListOfAllowedSpeachactClasses() {
		/*
		 * Im Insel-Szenario können alle bisher erlaubten Sprechakte eingesetzt 
		 * werden.
		 */
		m_allowedSpeachacts.add(
			Speachact.getListOfAllUseableSpeachactClasses());
	}
}