/*
 * Created on 13.05.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut f�r Intelligente Systeme, Universit�t Stuttgart (2003)
 */
package simulator;

import java.io.File;
import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.rmi.server.RMIClassLoader;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Vector;

import utils.DataTransferObject;
import utils.Id;
import utils.ParameterDescription;
import visualisation.IVisualisation;
import exceptions.InvalidElementException;
import exceptions.InvalidGameException;

/**
 * Die Klasse dient dazu Agenten, Spiele und Visualisierungskomponenten
 * beim Server anzumelden. Ausserdem kann die Klasse Agenten und ihre Klassen 
 * zum Server �bertragen. Sie vereint die beiden Klassen 
 * VisualisierungsAnmeldung, AgentAnmeldung und SpielAnmeldung.
 * 
 * @author Daniel Friedrich
 */
public class Coordinator extends UnicastRemoteObject implements ICoordinator {
	
	/**
	 * Pfad an dem die Umwelten der Szenarien abgespeichert sind.
	 */
	public static final String ENVIROMENTPATH = "EnviromentsPath";
	
	/**
	 * Informationhandler, der dass Sender der w�hrend den Spielen anfallenden 
	 * Informationen an die Clients managed.
	 */
	private InformationHandler m_informationHandler;
	/**
	 * Tabelle mit allen aktuell angemeldeten Spielen
	 */
	private Hashtable m_games = new Hashtable();
	
	/**
	 * Spielez�hler
	 */
	private int m_gameCounter = 0;
	
	/**
	 *  Pfad in dem die Scenarios gespeichert sind.
	 */
	private String m_scenarioPath =
		"/home/friedrdl/diplomarbeit/src/serverSrc/scenario/";
	/**
	 * Basis Pfad des Simulationsservers.
	 */
	private String m_path;
		
	/**
	 * PackageName des Scenarios
	 */
	private String m_packageName = "scenario";
	
	/**
	 * Hashtable zum mappen der Spielnamen auf die dazugeh�rige Id.
	 */
	private Hashtable m_gameNameId = new Hashtable();

	/**
	 * Name des Hosts auf dem der Simulator l�uft.
	 */
	private String m_hostname;
	
	/**
	 * ServerInfoObject
	 */
	private ServerInfoObject m_sInfoObject = null;

	/**
	 * Konstruktor.
	 * 
	 * @throws <{java.rmi.RemoteException}>
	 */
	public Coordinator(
		String hostname, 
		String pathName) throws RemoteException {
		super();
		// starte den InformationHandler
		this.startInformationHandler();
		m_sInfoObject = ServerInfoObject.getInstance(pathName);
		//m_sInfoObject = new ServerInfoObject(pathName);
		
		m_path = m_sInfoObject.getScenarioPath();
		m_scenarioPath = m_sInfoObject.getScenarioPath();;
		m_hostname = hostname;
		m_sInfoObject.setServerName(hostname);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param port
	 * @throws <{java.rmi.RemoteException}>
	 */
	public Coordinator(int port,
			String pathName) throws RemoteException {
		super(port);
		// starte den InformationHandler
		m_sInfoObject = ServerInfoObject.getInstance(pathName);
		
		m_path = m_sInfoObject.getScenarioPath();
		m_scenarioPath = m_sInfoObject.getScenarioPath();;
		this.startInformationHandler();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param port
	 * @param csf
	 * @param ssf
	 * @throws <{java.rmi.RemoteException}>
	 */
	public Coordinator(
		int port,
		RMIClientSocketFactory csf,
		RMIServerSocketFactory ssf)
		throws RemoteException {
		super(port, csf, ssf);
		// starte den InformationHandler
		this.startInformationHandler();
	}

	/* (non-Javadoc)
	 * @see jAgentSimulator.server.ICoordinator#registerGame(java.util.Hashtable)
	 */
	public Id registerGame(DataTransferObject dto)
		throws RemoteException, InvalidElementException {
        System.out.println("Coordinator: Spiel soll registriert werden...");
		if (dto.containsKey("ScenarioName")) {
			String tmpScenarioString = (String) dto.get("ScenarioName");
			/*
			 * Erstellen des wirklichen ScenarioNamens, d.h. den �bergebenen 
			 * Verzeichnisnamen + . + Scenario
			 */
			// Beginne mit dem Package(Verzeichnisnamen)
			StringBuffer scenarioName = new StringBuffer(tmpScenarioString);
			scenarioName.append(".");
			// Jetzt fehlt nur der Scenario-Zusatz		
			scenarioName.append("Scenario");
			// Erzeuge die Id f�r das Spiel
			Id gameId = new Id();
			AbstractScenario scenario = null;
            System.out.println("Coordinator: versuche " + scenarioName
                    + "zu registrieren");
			try {
				Class scenarioClass = Class.forName("scenario." + scenarioName);
				Constructor constructor =
					scenarioClass.getConstructor(
						new Class[] {
							Id.class,
							IInformationHandler.class,
							Hashtable.class });
				scenario =
					(AbstractScenario) constructor.newInstance(
						new Object[] {
							gameId,
							m_informationHandler,
							new Hashtable(dto)});
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}

			if (m_informationHandler == null) {
				this.startInformationHandler();
			}
			Game newGame =
				new Game(
					gameId,
					new Hashtable(dto),
					scenario,
					m_informationHandler);
			m_gameNameId.put(newGame.getGameName(), gameId);
			// Spiel registrieren
			m_games.put(newGame.getId().toString(), newGame);
			m_gameCounter++;
			// Id des Spiels zur�ckgeben
			return newGame.getId();
		} else {
			throw new InvalidElementException("Parameter ScenarioName");
		}
	}

	/* (non-Javadoc)
	 * @see jAgentSimulator.server.ICoordinator#getGameName(jAgentSimulator.utils.Id)
	 */
	public String getGameName(Id gameId)
		throws RemoteException, InvalidGameException {
		// check ob SPiel existiert.
		if (m_games.containsKey(gameId.toString())) {
			Game game = (Game) m_games.get(gameId.toString());
			return game.getGameName();
		} else {
			throw new InvalidGameException(gameId.toString());
		}
	}

	/* (non-Javadoc)
	 * @see jAgentSimulator.server.ICoordinator#isGameRegisteredAndOpen(jAgentSimulator.utils.Id)
	 */
	public boolean isGameRegisteredAndOpen(Id gameId)
		throws RemoteException, InvalidGameException {
		// check ob SPiel existiert.
		if (m_games.containsKey(gameId.toString())) {
			Game game = (Game) m_games.get(gameId.toString());
			return game.isOpen();
		} else {
			throw new InvalidGameException(gameId.toString());
		}
	}

	/* (non-Javadoc)
	 * @see jAgentSimulator.server.ICoordinator#registerAgent(jAgentSimulator.agents.AbstractAgent)
	 */
	public void registerAgent(Id gameId,
		String agentName, 
		String agentClass, 
		String serverName)
		throws RemoteException, 
			   InvalidGameException {

		// Das Spiel holen, aber vorher checken ob es �berhaupt existiert.
		if (m_games.containsKey(gameId.toString())) {
			// Agent beim Game registrieren.
			Game game = (Game) m_games.get(gameId.toString());
			try {
				/*
				 * Fehlerkorrekturen
				 */
				if (!serverName.endsWith("/")) {
					serverName = serverName + "/";
				}
				if (!serverName.startsWith("http://")) {
					serverName = "http://" + serverName;
				}
				if (serverName.indexOf(".") > -1) {
					serverName =
						serverName.substring(
							serverName.indexOf("."),
							serverName.length());
				}
				// Create the agent
				Class aClass = RMIClassLoader.loadClass(serverName, agentClass);
				AbstractAgent agent = (AbstractAgent) aClass.newInstance();
				agent.setAgentName(agentName);
				game.registerAgent(agent);
				// Agent beim Infohandler registrieren
				m_informationHandler.registerAgentInfo(gameId, agent);
			} catch (Exception e) {
				System.out.println("Fehler: " + e.getMessage());
				e.printStackTrace(System.out);
			}
		} else {
			throw new InvalidGameException(gameId.toString());
		}
	}

	/* (non-Javadoc)
	 * @see jAgentSimulator.server.ICoordinator#registerAgent(jAgentSimulator.agents.AbstractAgent, jAgentSimulator.client.visualisation.IVisualisation)
	 */
	public void registerAgent(Id gameId,
		String agentName,
		String agentClass,
		String serverName,
		IVisualisation visualisation)
		throws RemoteException, InvalidGameException {

		// Das Spiel holen, aber vorher checken ob es �berhaupt existiert.
		if (m_games.containsKey(gameId.toString())) {
			// Agent beim Game registrieren.
			Game game = (Game) m_games.get(gameId.toString());
			try {
				/*
				 * Fehlerkorrekturen
				 */
				if (!serverName.endsWith("/")) {
					serverName = serverName + "/";
				}
				if (!serverName.startsWith("http://")) {
					serverName = "http://" + serverName;
				}
				if (serverName.indexOf(".") > -1) {
					serverName =
						serverName.substring(
							serverName.indexOf("."),
							serverName.length());
				}
				// Create the agent
				Class aClass = RMIClassLoader.loadClass(serverName, agentClass);
				AbstractAgent agent = (AbstractAgent) aClass.newInstance();
				game.registerAgent(agent);
				//	Agent beim Infohandler registrieren
				m_informationHandler.registerAgentInfo(gameId, agent);
				// Visualisierungskomponente beim InfoHandler registrieren.
				m_informationHandler.registerVisualisation(
					agent.getId(),
					gameId,
					visualisation);
			} catch (Exception e) {
				throw new InvalidGameException(gameId.toString());
			}
		} else {
			throw new InvalidGameException(gameId.toString());
		}
	}

	/* (non-Javadoc)
	 * @see jAgentSimulator.server.ICoordinator#listAllOpenedGames()
	 */
	public Vector listAllOpenedGames() throws RemoteException {
		Enumeration enum = m_games.elements();
		Vector retval = new Vector();
		while (enum.hasMoreElements()) {
			Game tmpGame = (Game) enum.nextElement();
			if (tmpGame.isOpen()) {
				Object[] output = new Object[4];
				output[0] = tmpGame.getId().toString(); // Id
				output[1] = tmpGame.getGameName(); // Name
				output[2] =
					new Integer(tmpGame.getMaxNumberOfAgents()).toString();
				// max Agentanzahl
				output[3] =
					new Integer(tmpGame.getCurrentAgentCount()).toString();
				// akt. Agentenanzahl				
				retval.addElement(output);
			}
		}
		if (retval.size() > 0) {
			return retval;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see jAgentSimulator.server.ICoordinator#registerVisualisation(jAgentSimulator.utils.Id, jAgentSimulator.utils.Id, jAgentSimulator.client.visualisation.IVisualisation)
	 */
	public void registerVisualisation(
		Id agentId,
		Id gameId,
		IVisualisation visualisation)
		throws RemoteException {

		try {
			m_informationHandler.registerVisualisation(
				agentId,
				gameId,
				visualisation);
		} catch (Exception e) {
			throw new RemoteException("Inner Exception", e);
		}
	}

	/* (non-Javadoc)
	 * @see jAgentSimulator.server.ICoordinator#registerVisualisation(jAgentSimulator.utils.Id, java.lang.String, jAgentSimulator.client.visualisation.IVisualisation)
	 */
	public void registerVisualisation(
		Id gameId,
		String agentName,
		IVisualisation visualisation)
		throws RemoteException {
		try {
			// l�se den Namen name zu einer Id auf.
			Id agentId =
				m_informationHandler.resolveAgentName(gameId, agentName);
			// registriere die Visualisierungskomponente 
			m_informationHandler.registerVisualisation(
				agentId,
				gameId,
				visualisation);
		} catch (Exception e) {
			throw new RemoteException("Inner Exception", e);
		}
	}

	/* (non-Javadoc)
	 * @see jAgentSimulator.server.ICoordinator#startGame(jAgentSimulator.utils.Id)
	 */
	public void startGame(Id id) throws RemoteException, InvalidGameException {
		// kontrolliere, ob das Spiel registriert ist.
		if (m_games.containsKey(id.toString())) {
			/*System.out.println(
				"Versuche das Spiel mit der Id " 
				+ id.toString() 
				+" zu simulieren.");*/
			Game tmpGame = (Game) m_games.get(id.toString());
			if (tmpGame.wasStarted()) {
				// restart, da Thread schon verbraucht
				this.restartGame(id);
			} else {
				// normaler Start Thread wurde noch nicht verwendet.
				tmpGame.start();
			}
			try {
				Thread.sleep(400);
			} catch (Exception e) {
				throw new RemoteException("Inner Exception", e);
			}
		} else {
			// Spiel ist nicht registriert.
			throw new InvalidGameException(id.toString());
		}
	}

	/**
	 * Startet ein bereits gelaufenes Spiel neu.
	 * 
	 * @param Id id - Id des Spiels, das neu gestartet werden soll.  
	 * @throws RemoteException
	 */
	public void restartGame(Id id)
		throws RemoteException, InvalidGameException {
		// kontrolliere, ob das Spiel registriert ist.
		if (m_games.containsKey(id.toString())) {
			Game tmpGame = (Game) m_games.get(id.toString());
			/* neue Game-Instanz erzeugen und mit den Daten der alten Instanz
			 * f�ttern. Ansonsten wird kein neuer Thread erstellt und der alte
			 * kann nicht mehr verwendet werden.
			 */
			m_gameCounter++;
			Game newGame =
				new Game(
					"Spiel" + m_gameCounter,
					m_informationHandler,
					tmpGame);
			// ersetze im HT das alte durch das neue Spiel
			m_games.remove(id.toString());
			m_games.put(id.toString(), newGame);
			// starte dann Spiel neu.
			newGame.start();
		} else {
			// Spiel ist nicht registriert.
			throw new InvalidGameException(id.toString());
		}
	}

	/**
	 * Startet den InformationHandler
	 */
	private void startInformationHandler() {
		m_informationHandler = new InformationHandler();
	}

	/**
	 * Liefert den Parameterwert des Parameters name eines Spiels zur�ck.
	 * 
	 * @param Id gameId
	 * @param String name
	 * @return Object - Parameterwert
	 * @throws AgentExecutionException
	 */
	public Object getParameter(Id gameId, String name)
		throws RemoteException, InvalidGameException {
		// kontrolliere, ob das Spiel registriert ist.
		if (m_games.containsKey(gameId.toString())) {
			Game game = (Game) m_games.get(gameId.toString());
			try {
				System.out.println("Coordinator: GameName " + name + " = " + game.getGameParameter(name));
				return game.getGameParameter(name);
			} catch (Exception e) {
				throw new RemoteException("RemoteException", e);
			}
		} else {
			// Spiel ist nicht registriert.
			throw new InvalidGameException(gameId.toString());
		}
	}

	/**
	 * Liefert einen Liste der Scenarionamen zur�ck.
	 * 
	 * @return LinkedList - Liste der Szenarionamen
	 */
	public LinkedList getScenarioNames() throws RemoteException {
		LinkedList retval = new LinkedList();
		try {
			File pathName = new File(m_scenarioPath);
			System.out.println("Szenariopfad: " + pathName);
			File[] files = pathName.listFiles();

			for (int i = 0; i < files.length; i++) {
				/* 
				 * Das .class Ende abschneiden, bevor die Daten zur�ck gegeben 
				 * werden.
				 */
				if (files[i].isDirectory()) {
						retval.addLast(files[i].getName());
				}
			}
			if (retval.size() > 0) {
				return retval;
			}
			return null;
		} catch (Exception ioE) {
			System.out.println("Fehler: " + ioE.toString());
			throw new RemoteException("Fehler: ", ioE);
		}
	}

	/* (non-Javadoc)
	 * @see simulator.ICoordinator#getScenarioParameter(java.lang.String)
	 */
	public LinkedList getScenarioParameter(String scenarioName)
		throws RemoteException {
		LinkedList retval = null;
		String className = "Scenario";
		System.out.println("Classname: " + className);
		try {
			System.out.println("Try: " + "scenario."
			+ scenarioName
			+ "."
			+ className);
			Class scenarioClass =
				Class.forName(
					"scenario."
						+ scenarioName
						+ "."
						+ className);
            System.out.println("Coordinator: Erstelle eine neue SzenarioInstanz");
			AbstractScenario scenario =
				(AbstractScenario) scenarioClass.newInstance();
            System.out.println("Coordinator: Hole Parameter.");
			return scenario.getScenarioParameter();
		} catch (Exception e) {
			throw new RemoteException("Inner Exception", e);
		}
	}

	/* (non-Javadoc)
	 * @see simulator.ICoordinator#getGameParameter()
	 */
	public LinkedList getGameParameter() throws RemoteException {
		LinkedList list = new LinkedList();
		list.add(
			new ParameterDescription(
				"GameName",
				ParameterDescription.StringType));
		list.add(
			new ParameterDescription(
				"MaxAgents",
				ParameterDescription.IntegerType));		
		list.add(
			new ParameterDescription(
				"MultipleGameTabsAllowed",
				ParameterDescription.BooleanType));
		return list;		
	}
	
	/* (non-Javadoc)
	 * @see simulator.ICoordinator#removeGame(utils.Id)
	 */
	public boolean removeGame(Id gameId)
		throws RemoteException, InvalidGameException {
		if (m_games.containsKey(gameId.toString())) {
			// hole Spiel
			Game tmpGame = (Game) m_games.get(gameId.toString());
			// l�sche Spiel aus Server
			tmpGame.close();
			tmpGame = null;
			m_games.remove(gameId.toString());
			return true;
		} else {
			throw new InvalidGameException(gameId.toString());
		}
	}

	/* (non-Javadoc)
	 * @see simulator.ICoordinator#getAgentsForGame(utils.Id)
	 */
	public Vector getAgentsForGame(Id gameId) throws RemoteException {
		Vector retval = new Vector();

		if (m_games.containsKey(gameId.toString())) {
			Game game = (Game) m_games.get(gameId.toString());
			Enumeration enum = game.getAgents().elements();
			while (enum.hasMoreElements()) {
				AbstractAgent tmpAgent = (AbstractAgent) enum.nextElement();
				retval.addElement(tmpAgent.getName());
			}
		} else {
			throw new NoSuchElementException();
		}

		return retval;
	}

	/* (non-Javadoc)
	 * @see simulator.ICoordinator#getSimulationResult(utils.Id)
	 */
	public String getSimulationResult(Id gameId) throws RemoteException {
		try {
			if (m_games.containsKey(gameId.toString())) {
				return ((Game) m_games.get(gameId.toString())).getStatistic();
			} else {
				throw new RemoteException("");
			}
		} catch (Exception e) {
			throw new RemoteException("Fehler beim Abfragen des Logs.", e);
		}
	}

	/* (non-Javadoc)
	 * @see simulator.ICoordinator#getAvailableGames()
	 */
	public Vector getAvailableGames() throws RemoteException {
		Vector retval = new Vector();
		Enumeration enum = m_games.elements();
		while (enum.hasMoreElements()) {
			Game tmpGame = (Game) enum.nextElement();
			try {
				Object tmpObject =
					tmpGame.getGameParameter("MultipleGameTabsAllowed");
				if (tmpObject != null) {
					if (((Boolean) tmpObject).booleanValue()) {
						retval.addElement(tmpGame.getName());
					}
				}
			} catch (Exception e) {
				System.out.println("Fehler: " + e.getMessage());
				e.printStackTrace(System.out);
			}

		}
		return retval;
	}

	/* (non-Javadoc)
	 * @see simulator.ICoordinator#getParameters(utils.Id)
	 */
	public DataTransferObject getParameters(Id gameId) throws RemoteException {
		DataTransferObject dto = new DataTransferObject();

		if (m_games.containsKey(gameId.toString())) {
			Game game = (Game) m_games.get(gameId.toString());
			return new DataTransferObject(game.getGameParameters());
		} else {
			throw new NoSuchElementException();
		}
	}

	/* (non-Javadoc)
	 * @see simulator.ICoordinator#getGameId(java.lang.String)
	 */
	public Id getGameId(String gameName) throws RemoteException {
		if (m_gameNameId.containsKey(gameName)) {
			return (Id) m_gameNameId.get(gameName);
		} else {
			throw new NoSuchElementException();
		}
	}
}
