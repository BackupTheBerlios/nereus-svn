/*
 * Created on 13.05.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package simulator;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import scenario.communication.IMessagingAgent;
import simulator.statistic.IStatisticScenario;
import simulator.statistic.StatisticManagement;
import utils.Id;
import visualisation.IVisualisation;
import exceptions.InvalidElementException;
/**
 * Die Klasse repräsentiert ein Spiel das im Simulator ausgeführt werden kann. 
 * Ein Spiel managed den Ablauf der Spiels, es enthält das Szenario und eine 
 * Reihe von Agenten. 
 * 
 * @author Daniel Friedrich
 */
public class Game extends Thread {

	/**
	 *  Name des Spiels
	 */
	private String m_gameName; 
    
    /**
     *  Id des Spiels
     */
	private Id m_id;
    
    /**
     * Visualisierungshandler
     */
    private IInformationHandler m_informationHandler;
	
	/**
	 * Tabelle mit allen Agenten die beim Spiel registriert sind.
	 */
	private Hashtable m_agents = new Hashtable();
	
	/** 
	 * Das Scenario mit dem gespielt werden soll.
	 */
	private AbstractScenario m_scenario;
	
	/** 
	 * Tabelle aller Parametereinstellungen des Spiels.
	 */
	private Hashtable m_params;
	
	/**
	 * Maximalanzahl an Agenten für das Spiel.
	 */
	private int m_maxNumberOfAgents = 0;
	
	/**
	 * Parameter für das Scenario
	 */
	private Hashtable m_scenarioParams;
	
	/**
	 * Ist das Spiel rundenbasiert ?
	 */
	private boolean m_roundbased = true;
	
	/**
	 * ScenarioHandler zur Kommunikation der Agenten mit dem Scenario
	 */
	private AbstractScenarioHandler m_scenarioHandler;
	
	/**
	 * Flag ob das Spiel schon ausgeführt wurde.
	 */
	private boolean m_wasStarted = false;
	
	/**
	 * Gibt an ob die Simulation des Spiels schon beendet ist.
	 */
	private boolean m_simulationIsFinished = false;
	
	/**
	 * Klassen der Agenten im Spiel.
	 */
	private Hashtable m_agentClasses = new Hashtable();
	
	/**
	 * Statistikkomponente des Spiels.
	 */
	private StatisticManagement m_statisticManagement;

	/**
	 * Konstruktor.
	 * 
	 * @param id - Id des Spiels
	 * @param gameParameters - Spielparameter
	 * @param scenario - Szenario 
	 * @param iHandler - Informationhandler
	 * @throws InvalidElementException
	 */
	public Game(Id id, 
				Hashtable gameParameters, 
				AbstractScenario scenario,
				IInformationHandler iHandler) 
		throws InvalidElementException {
		super((String)gameParameters.get("GameName"));
		m_informationHandler = iHandler;
		m_id = id;
		m_gameName = (String)gameParameters.get("GameName");		
		// Parameter wegspeichern
		m_params = gameParameters;
		// Das Hashtable für eine spätere Füllung initialisieren.
		m_scenarioParams = new Hashtable();
		m_scenario = scenario; 
		// checken ob das Spiel rundenbasiert ist.		
		if(gameParameters.containsKey("RoundBased")) {
			Object tmpObject = gameParameters.get("RoundBased");
			if((tmpObject != null) && (!((Boolean)tmpObject).booleanValue())) {
				m_roundbased = false;
			}
		}
		// ScenarioHandler erstellen
		m_scenarioHandler = m_scenario.createNewScenarioHandler();
		m_maxNumberOfAgents = ((Integer)m_params.get("MaxAgents")).intValue();			
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param gameParameters -Spielparameter
	 * @param scenario - Szenario
	 * @param scenarioParameters - Szenarioparameter
	 * @throws AgentExecutionException
	 */
	public Game( 
		Hashtable gameParameters, 
		AbstractScenario scenario,
		Hashtable scenarioParameters) throws InvalidElementException {			
		super((String)gameParameters.get("GameName"));
		m_id = new Id();
		m_gameName = (String)gameParameters.get("GameName");
		// Parameter wegspeichern
		m_params = gameParameters;
		// Scenario-Parameter zwischenspeichern.
		m_scenarioParams = scenarioParameters;
		// scenario speichern.
		m_scenario = scenario;
		// checken ob das Spiel rundenbasiert ist.		
		if(gameParameters.containsKey("RoundBased")) {
			Object tmpObject = gameParameters.get("RoundBased");
			if((tmpObject != null) && (!((Boolean)tmpObject).booleanValue())) {
				m_roundbased = false;
			}
	    }
		//	ScenarioHandler erstellen
		m_scenarioHandler = m_scenario.createNewScenarioHandler();
		m_maxNumberOfAgents = ((Integer)m_params.get("MaxAgents")).intValue();		
	}	

	/**
	 * Konstruktor.
	 * 
	 * @param name - Name des Spiels
	 * @param iHandler - Informationhandler
	 * @param oldGame - altes Spiel das ersetzt werden soll durch neues.
	 */
	public Game(String name, IInformationHandler iHandler,Game oldGame) {
		super(name);
		m_id = oldGame.getId();
		m_agents = new Hashtable();
		m_gameName = oldGame.getGameName();
		// Szenario kopieren und dann reseten.
		m_scenario = oldGame.getScenario();
		m_scenario.reset();
		m_scenarioHandler = m_scenario.createNewScenarioHandler();
		m_scenarioParams = oldGame.getScenarioParameters();
		m_params = oldGame.getGameParameters();
		m_roundbased = oldGame.isRoundBased();		
		m_maxNumberOfAgents = ((Integer)m_params.get("MaxAgents")).intValue();
		m_agentClasses = oldGame.getAgentClasses();
		m_informationHandler = iHandler;
		
		
		m_scenario.reset(); 
		// Agenten neu erzeugen.
		Hashtable tmpAgents = (Hashtable)oldGame.getAgents().clone();
		Enumeration enum = tmpAgents.elements();
		while(enum.hasMoreElements()) {
			AbstractAgent tmpAgent = 
				(AbstractAgent)enum.nextElement();
			/*
			 * Einen neuen Agenten mit der gleichen Id wie der bisherige 
			 * erzeugen und diesen dann beim Spiel als Ersatz für den 
			 * bisherigen Agenten abspeichern.
			 */
			try {
				Class[] cParams = new Class[2];
				cParams[0] = Id.class;
				cParams[1] = String.class;
				Constructor cons = ((Class)m_agentClasses.get(
					tmpAgent.getId().toString())).getConstructor(cParams);
				Object[] params = new Object[2];
				params[0] = tmpAgent.getId();
				params[1] = tmpAgent.getAgentName();
	
				AbstractAgent newAgent = 
					(AbstractAgent)cons.newInstance(params);						
				m_agents.put(newAgent.getId().toString(), newAgent);
				newAgent.setScenario(m_scenarioHandler);
				// registrieren beim Szenario
				m_scenario.addAgent(newAgent, 
					((Double)this.getGameParameter(
						"StartEnergy")).doubleValue());		
			}catch(Exception e) {
				System.out.println("Fehler beim Reaktivieren der Agenten.");
			}					
		}
	}

	/**
	 * Liefert alle registrierten Agenten des Spiels.
	 * 
	 * @return Hashtable - die registrierten Agenten.
	 */
	public Hashtable getAgents() {
		return m_agents;
	}

	/**
	 * Liefert alle Parametereinstellungen des Spiels als Hashtable zurück.
	 * 
	 * @return Hashtable - die Spielparameter
	 */
	public Hashtable getGameParameters() {
		return m_params;
	}
	
	/**
	 * Liefert alle Parametereinstellungen des Scenarios als Hashtable zurück.
	 * 
	 * @return Hashtable - die Szenarioparameter
	 */
	public Hashtable getScenarioParameters() {
		return m_scenarioParams;
	}
	
	/**
	 * Liefert die Anzahl der aktuell angemeldeten Agenten zurück.
	 * 
	 * @return int - aktuelle Anzahl der Agenten
	 */
	public int getCurrentAgentCount() {
		return m_agents.size();
	}

	/**
	 * Liefert das Szenario des Spiels.
	 * 
	 * @return AbstractSzenario - Szenario des Spiels
	 */
	public AbstractScenario getScenario() {
		return m_scenario;
	}

	/**
	 * Registriert einen Agenten beim Spiel.
	 * 
	 * @param AbstractAgent - zu registrierender Agent
	 */
	public void registerAgent(AbstractAgent agent)
		throws InvalidElementException{
		if(this.isOpen()) {
			try {				
				// neuen ScenarioHandler erzeugen, wenn noch keiner existiert.
				if(m_scenarioHandler == null) {
					m_scenarioHandler = m_scenario.createNewScenarioHandler();
				}
				// Dem Agent sein Scenario zuweisen
                System.out.println("Game: Weise dem Agenten sein Szenario zu");
				agent.setScenario(m_scenarioHandler);
				/*
				 * Agenten für die Kommunikation registrieren, wenn sie das
				 * IMessagingService-Interface implementieren.
				 */
                System.out.println("Game: Ist der Agent ein MessagingAgent?");
				if(agent instanceof IMessagingAgent) {
					m_informationHandler.registerAgentForCommunication(
						agent.getId(),
						m_id);
				}
				// registrieren
                System.out.println("Game: trage Agent in m_agents ein");
				m_agents.put(agent.getId().toString(),agent);
				// Klasse des Agenten zum späteren reaktivieren speichern
				m_agentClasses.put(agent.getId().toString(),agent.getClass());
				// Ans Scenario übergeben				
                System.out.println("Game: Fuege Agent dem Szenario hinzu");
				m_scenario.addAgent(
					agent,
					((Double)this.getGameParameter("StartEnergy")).doubleValue());								
			}catch(Exception e) {
				throw new InvalidElementException();
			}
		}else {
			// Meldung schmeissen, das Spiel schon voll besetzt ist.
			throw new InvalidElementException();				
		}				
	}

	/**
	 * Speichert einen Parameter.
	 * 
	 * @param String name - Name des Parameters
	 * @param Object value - Parameterwert
	 */
	public void setParameter(String name, Object value) {
		m_params.put(name, value);
	}

	/**
	 * Speichert das Scenario.
	 * 
	 * @param AbstractScenarion scenario - Szenario des Spiels
	 */
	public void setScenario(AbstractScenario scenario) {
		m_scenario = scenario;
	}
	
	/**
	 * Liefert die Id des Spiels.
	 * 
	 * @return Id - Id des Spiels
	 */
	public Id getId() {
		return m_id;
	}

	/**
	 * Gibt zurück, ob das Spiel noch offen ist.
	 * 
	 * Ein Spiel ist dann noch offen, wenn es noch freie Plätze für Agenten
	 * gibt, d.h. wenn noch Agenten angemeldet werden können.
	 * 
	 * @return boolean - True Spiel offen, False Spiel ist bereits ausgebucht.
	 */
	public boolean isOpen() {
		if( m_agents.size() < m_maxNumberOfAgents ) {
			return true;
		}
		return false;
	}
	
	/**
	 * Speichert die maximal zulässige Anzahl an Agenten für das Spiel.
	 *  
	 * @param int number - Anzahl der max. für das Spiel zugelassenen Agenten
	 */
	public void setMaxNumberOfAgents(int number) {
		m_params.put("MaxAgents",new Integer(number));
	}
	
	/**
	 * Liefert die maximal zulässige Anzahl an Agenten für das Spiel.
	 * 
	 * @return int - Anzahl der max. für das Spiel zugelassenen Agenten
	 */
	public int getMaxNumberOfAgents() {
		return ((Integer)m_params.get("MaxAgents")).intValue();
	}
	
	/**
	 * Liefert den Namen des Spiels.
	 * 
	 * @return String - Name des Spiels
	 */
	public String getGameName() {
		return m_gameName;
	}
	
	/**
	 * Liefert den Spielparameter mit dem Namen name zurück oder Exception.
	 * 
	 * @param String name - Name des gesuchten Parameters.
	 * @return Object - Parameterwert
	 * @throws InvalidElementException
	 */
	public Object getGameParameter(String name) 
		throws InvalidElementException {
		if(m_params.containsKey(name)) {
			return m_params.get(name);	
		}else {
			throw new InvalidElementException();
		}		
	}
	
	/**
	 * Liefert zurück, ob das Spiel rundenbasiert ist oder nicht.
	 * 
	 * Der Normalfall ist ein rundenbasiertes Spiel.
	 * 
	 * @return boolean - True Spiel ist rundenbasiert, ansonsten False
	 */
	public boolean isRoundBased() {
		return m_roundbased;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() { 			
		//super.run();
		try {
			int numExps = ((Integer)this.getGameParameter("numExps")).intValue();
			for(int i = 0; i < numExps; i++) {
				m_informationHandler.log(
					m_id,
					IVisualisation.StateMsg,	
					"Beginne die Simulation des " + (i+1) + ". Experiments.");
				System.out.println("Spielnummer: " + i + " von " +numExps);
					
				
				Vector agents = new Vector();
				Vector agentIds = new Vector();
				Enumeration enum = m_agents.elements();
				while(enum.hasMoreElements()) {
					AbstractAgent agent = (AbstractAgent)enum.nextElement();
					agentIds.addElement(agent.getId());
					if(i==0) {
						agents.addElement(agent);
					}
				}
				if(i == 0) {
					// Statistikkomponente initialsieren.
					m_statisticManagement = new StatisticManagement(
						m_id,
						m_gameName,
						((IStatisticScenario)
							m_scenario).getEnviromentStatisticParameters());
				}
				m_statisticManagement.nextExperiment(
					((IStatisticScenario)
						m_scenario).createNewStatisticComponent(agentIds));
				
	
				/*
				 * Das Szenario muss reseted werden, da die Agenten im Szenario
				 * sonst mit einer IllegalThreadStateException abrauchen. Dann
				 * müssen die Agenten neu erzeugt werdenn und dem Szenario 
				 * neu übergeben werden.
				 */	
				if(m_wasStarted) { 
					m_scenario.reset(); 
					// Agenten neu erzeugen.
					Hashtable tmpAgents = (Hashtable)m_agents.clone();
					m_agents.clear();
					Enumeration tmpEnum = tmpAgents.elements();
					((IInformationHandler)m_informationHandler).resetVisualisations(m_agents);
					while(tmpEnum.hasMoreElements()) {
						AbstractAgent tmpAgent = 
							(AbstractAgent)tmpEnum.nextElement();
						/*
						 * Einen neuen Agenten mit der gleichen Id wie der bisherige 
						 * erzeugen und diesen dann beim Spiel als Ersatz für den 
						 * bisherigen Agenten abspeichern.
						 */
						try {
							Class[] cParams = new Class[2];
							cParams[0] = Id.class;
							cParams[1] = String.class;
							Constructor cons = ((Class)m_agentClasses.get(
								tmpAgent.getId().toString())).getConstructor(cParams);
							Object[] params = new Object[2];
							params[0] = tmpAgent.getId();
							params[1] = tmpAgent.getAgentName();

							AbstractAgent newAgent = 
								(AbstractAgent)cons.newInstance(params);						
							m_agents.put(newAgent.getId().toString(), newAgent);
							newAgent.setScenario(m_scenarioHandler);
							// registrieren beim Szenario
							m_scenario.addAgent(newAgent, 
								((Double)this.getGameParameter(
									"StartEnergy")).doubleValue());		
						}catch(Exception e) {
							System.out.println("Fehler beim Reaktivieren der Agenten.");
						}					
					}
				}
				// Flag setzen
				m_wasStarted = true; 
				
				m_scenario.simulateGame();
				m_informationHandler.log(
					m_id,	
					IVisualisation.StateMsg,
					"Beende die Simulation des " + (i+1) + ". Experiments.");
				
								
			}	
			m_simulationIsFinished = true;			
					
		}catch(Exception e) {
			System.out.println("Fehler beim Ablauf des Spiels");
			e.printStackTrace(System.out);
			m_simulationIsFinished = true;
		}
		if((m_scenario instanceof IStatisticScenario)
			&& (m_params.containsKey("StatisticPath"))) {
			this.saveStatisticTo((String)m_params.get("StatisticPath"));	
		}
	}
	
	/**
	 * Speichert die Statistikdatei am angegebenen Pfad.
	 * 
	 * @param path - Pfad an dem die Statistik gespeichert werden soll.
	 */
	public void saveStatisticTo(String path) {
		try {
			m_statisticManagement.createStatistic();
			
			File statFile = new File(path + "statistic" + m_id + ".stat");
			FileWriter logFileWriter = new FileWriter(statFile);
			logFileWriter.write(m_statisticManagement.getGameStatistic());
			logFileWriter.close();
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}	
	
	/**
	 * Gibt die Statistik zurück.
	 * 
	 * @return String - Inhalt der Statistik.
	 */
	public String getStatistic() {
		if(m_scenario instanceof IStatisticScenario) {
			StringBuffer buffer = new StringBuffer();
			try {
				m_statisticManagement.createStatistic();
				buffer.append(m_statisticManagement.getGameStatistic());		
				return buffer.toString();
			}catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		return "Das Szenario ist kein Statistic-Szenario und "
				+ "deshalb ist keine Statistik vorhanden.";
	}
	
	/**
	 * Gibt zurück ob das Spiel schon gestartet wurde.
	 * 
	 * @return boolean - True Spiel wurde bereits einmal gestartet
	 */
	public boolean wasStarted() {
		return m_wasStarted;
	}
	
	/**
	 * Gibt zurück, ob die Simulation beendet ist.
	 * 
	 * @return boolean True die Simulation ist beendet, ansonsten False
	 */
	public boolean isSimulationFinished() {
		return m_simulationIsFinished;
	}
	
	
	/**
	 * Schliesst das Spiel und gibt alle Ressourcen wieder frei.
	 */
	public void close() {
		m_agents = null;
		m_params = null;
		m_scenario = null;
		m_scenarioHandler = null;
		m_informationHandler = null;
	}
	
	/**
	 * Liefert eine Hashtable mit allen Klassen der Agenten zurück.
	 * 
	 * @return Hashtable - Klassen der Agenten
	 */
	public Hashtable getAgentClasses() {
		return m_agentClasses;
	}
 
}
