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
package simulator;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Vector;

import scenario.fgml.FgmlScenario;
import simulator.ContestInformationHandler;
import simulator.Game;
import speachacts.Speachact;
import utils.Id;

/**
 * Der ContestSimulator ist eine spezielle Version des AgentteamSimulationServer
 * für den MultiagentLearningContest bei der FGML 03 in Karlsruhe.  
 * 
 * @author Daniel Friedrich
 */
public class ContestSimulator {
	
	/**
	 * Name des Scenarios.
	 */
	private String m_scenarioName = "FGML Multiagent-Learning Contest";

	/**
	 * Name der ausgewählten Strategie.
	 */
	private String m_strategyName;
	
	/**
	 * Ausgewähltes Lernverfahren.
	 */
	private String m_learningMethodName;

	/**
	 * Anzahl an Agenten die Teil des Teams sind.
	 */
	private int m_numberOfAgents = 0;

	/**
	 * Basispath des Spiels.
	 */
	private String m_basePath = "";

	/**
	 * Spiel
	 */
	private Game m_game;

	/**
	 * Agenten
	 */
	private Vector m_agents = new Vector();

	/**
	 * Parameter für das Spiel
	 */
	private Hashtable m_parameter = new Hashtable();

	/**
	 * Szenario des Spiels
	 */
	private AbstractScenario m_scenario;

	/**
	 * Scenariohandler
	 */
	private AbstractScenarioHandler m_scenarioHandler;

	/**
	 * InformationHandler
	 */
	private IInformationHandler m_infoHandler;
	
	/**
	 * Klassifikationsschwelle.
	 */
	private int m_classificationBarrier = 0;
	
	/**
	 * Kommunikationsschwelle.
	 */
	private int m_communicationBarrier =0;

	/**
	 * Konstruktor.
	 */
	public ContestSimulator(
		String strategyName,
		String learningMethodName,
		int numOfAgents,
		int classificationBarrier,
		int communicationBarrier) {
		super();
		m_strategyName = strategyName;
		m_learningMethodName = learningMethodName;
		m_numberOfAgents = numOfAgents;
		m_classificationBarrier = classificationBarrier;
		m_communicationBarrier = communicationBarrier;
	}

	/**
	 * Methode zum Ausführen des Simulators.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 5) {
			ContestSimulator simulator =
				new ContestSimulator(
					args[0],
					args[1],
					Integer.parseInt(args[2]),
					Integer.parseInt(args[3]),
					Integer.parseInt(args[4]));

			simulator.buildEnviroment();
			simulator.simulate();
		} else {
			System.out.println(
				"Fehlerhafter Programmaufruf!, verwenden Sie bitte:");
			System.out.println(
				"FGMLContestSimulator "
					+ "<Strategiename>"
					+ "<Lernverfahrenname>"
					+ "<Agentenanzahl>"
					+ "<Klassifikationschwelle>"
					+ "<Kommunikationschwelle>");
		}
	}

	/**
	 * Erstelle die Umwelt.
	 */
	private void buildEnviroment() {
		try {
			System.out.println(
				"Beginne die Umwelt der Simulation zu erstellen.");
			// Parameter erstellen
			this.buildParameterSet();
			System.out.println("Status: ParameterSet erstellt.");
			/*
			 * Szenario erzeugen
			 */
			m_infoHandler = new ContestInformationHandler(true);
			Id id = new Id();
			
			m_scenario = new FgmlScenario(id, m_infoHandler, m_parameter);

			// Scenariohandler erstellen
			m_scenarioHandler = m_scenario.createNewScenarioHandler();
			System.out.println("Status: Szenario erzeugt.");
			/*
			 * Spiel erzeugen
			 */
			m_game = new Game(id, m_parameter, m_scenario, m_infoHandler);
			System.out.println("Status: Spiel erzeugt.");

			this.buildAgents();

			System.out.println(
				"Status; Agenten erstellt und dem Spiel hinzugefügt.");
			System.out.println(
				"Status: Simulationsumgebung vollständig erstellt");

		} catch (Exception e) {
			System.out.println(
				"Fehler beim Erstellen der Simulationsumgebung.");
			e.printStackTrace(System.out);
		}
	}

	/**
	 * Baut das Parameterset auf.
	 */
	private void buildParameterSet() {
		m_parameter.put("GameName", "Testspiel");
		m_parameter.put("MaxAgents", new Integer(m_numberOfAgents));
		m_parameter.put("RoundLimit", new Integer(30));
		m_parameter.put("EnergyForDigging", new Double(6));
		m_parameter.put("EnergyForMoving", new Double(3));
		m_parameter.put(Speachact.SINGLECOMMCOSTS, new Double(1));
		m_parameter.put("EnergyForPlaceDiscovery", new Double(0));
		m_parameter.put(Speachact.ANSWERCOSTS, new Double(1));
		m_parameter.put("EnergyCell", new Double(70));
		m_parameter.put("StartEnergy", new Double(150));
		m_parameter.put("numExps", new Integer(1));
		m_parameter.put("ScenarioName", m_scenarioName);
		m_parameter.put("RoundBased", Boolean.TRUE);
		m_parameter.put("Graphfile", m_basePath + "isle300.arff");
		m_parameter.put(
			"Attributefile",
			m_basePath + "isle300Attributes.arff");
		m_parameter.put("StatisticPath", m_basePath);
		m_parameter.put("MultipleGameTabsAllowed", Boolean.TRUE);
		m_parameter.put(
			"Klassifikationsschwelle", 
			new Integer(m_classificationBarrier));
		m_parameter.put(
			"Kommunikationsschwelle", 
			new Integer(m_communicationBarrier));
	}

	private void buildAgents() {
		try {
			int startNumber = 0;
			String agentClass = null;

			if (m_strategyName.equals("ZentralesLernen")) {
				m_game.registerAgent(
					this.createAgent(
						"scenario.fgml.Fgml"
							+ m_learningMethodName
							+ "LearningAgent"));
				agentClass = "scenario.fgml.CollectorAgent";
				startNumber++;
			}else if (m_strategyName.equals("UncooperativeAgents")){
				agentClass =
					"scenario.fgml."
						+ m_learningMethodName
						+ "UncooperativeAgent";											
			}else {
				// Schnelles Erfahrungswachstum
				agentClass =
					"scenario.fgml.Fgml"
						+ m_learningMethodName
						+ "FEAgent";
			}
			// Erstelle die Agenten
			for (int i = startNumber; i < m_numberOfAgents; i++) {
				AbstractAgent agent = this.createAgent(agentClass);
				m_game.registerAgent(agent);
				m_agents.addElement(agent);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/**
	 * Startet die Simulation des oder der Spiele.
	 */
	public void simulate() {
		try {
			m_game.start();
			String write = m_infoHandler.getCompleteLog(m_game.getId());
			this.writeOut();
		} catch (Exception e) {
			System.out.println("Fehler während dem Ausführen des Spiels.");
			e.printStackTrace(System.out);
		}
	}

	/**
	 * Erzeuge die Agenten.
	 * 
	 * @param name
	 * @param aClass
	 * @return
	 */
	private AbstractAgent createAgent(String name, String aClass) {
		try {
			// Create the agentClass
			Class agentClass = Class.forName(aClass);
			Class[] parameterTypes =
				new Class[] { String.class, AbstractScenarioHandler.class };
			Constructor constructor = agentClass.getConstructor(parameterTypes);
			Object[] parameters = new Object[] { name, m_scenarioHandler };
			return (AbstractAgent) constructor.newInstance(parameters);
		} catch (Exception e) {
			System.out.println(
				"Fehler beim Erstellen eines Agenten der Klasse: " + aClass);
			e.printStackTrace(System.out);
		}
		return null;
	}

	/**
	 * Erzeuge einen Agenten.
	 * 
	 * @param agentClass
	 * @return
	 */
	private AbstractAgent createAgent(String agentClass) {
		try {
			// Create the agentClass
			Class aClass = Class.forName(agentClass);
			return (AbstractAgent) aClass.newInstance();
		} catch (Exception e) {
			System.out.println(
				"Fehler beim Erstellen eines Agenten der Klasse: "
					+ agentClass);
			e.printStackTrace(System.out);
		}
		return null;
	}

	/**
	 * Schreibe das Ergebnis aus.
	 */
	public void writeOut() {
		try {
			// Warten bis die Simulation beendet ist.
			System.out.println("Warte darauf dass das Spiel beendet ist");
			while (!m_game.isSimulationFinished()) {
			}
			//System.out.println("Beginne jetzt das Log-File rauszuschreiben");
			String log = m_infoHandler.getCompleteLog(m_game.getId());
			// dann in ein File schreiben.
			File logFile = new File(m_basePath + "testSimBasic.log");
			FileWriter logFileWriter = new FileWriter(logFile);
			logFileWriter.write(log);
			logFileWriter.close();
		} catch (Exception e) {
			System.out.println("Fehler beim Ausschreiben des Logs.");
			e.printStackTrace(System.out);
		}
	}
}
