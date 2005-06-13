/*
 * Dateiname      : Coordinator.java
 * Erzeugt        : 13. Mai 2003
 * Letzte Änderung: 13 Juni. 2005 durch Samuel Walz
 * Autoren        : Daniel Friedrich
 *                  Eugen Volk
 *
 *
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut für Intelligente Systeme
 * der Universität Stuttgart unter Betreuung von Dietmar Lippold
 * (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package nereus.simulator;

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
import java.util.Iterator;

import nereus.utils.DataTransferObject;
import nereus.utils.Id;
import nereus.utils.ParameterDescription;
import nereus.utils.GameConf;
import nereus.visualisation.IVisualisation;
import nereus.exceptions.InvalidElementException;
import nereus.exceptions.InvalidGameException;
import nereus.simulatorinterfaces.AbstractAgent;
import nereus.simulatorinterfaces.AbstractScenario;
import nereus.simulatorinterfaces.ICoordinator;
import nereus.simulatorinterfaces.IInformationHandler;
import nereus.simulator.visualisation.VisualisationServer;

/**
 * Die Klasse dient dazu Agenten, Spiele und Visualisierungskomponenten
 * beim Server anzumelden. Ausserdem kann die Klasse Agenten und ihre Klassen
 * zum Server übertragen. Sie vereint die beiden Klassen
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
     * Informationhandler, der dass Sender der während den Spielen anfallenden
     * Informationen an die Clients managed.
     */
    private InformationHandler m_informationHandler;
    /**
     * Tabelle mit allen aktuell angemeldeten Spielen
     */
    private Hashtable m_games = new Hashtable();
    
    /**
     * Spielezähler
     */
    private int m_gameCounter = 0;
    
    /**
     *  Pfad in dem die Scenarios gespeichert sind. Muß im Konstruktor
     * gesetzt werden.
     */
    private String m_scenarioPath;
    
    /**
     * Basis Pfad des Simulationsservers.
     */
    private String m_path;
    
    
    /**
     * Hashtable zum mappen der Spielnamen auf die dazugehörige Id.
     */
    private Hashtable m_gameNameId = new Hashtable();
    
    /**
     * Name des Hosts auf dem der Simulator läuft.
     */
    private String m_hostname;
    
    /**
     * ServerInfoObject
     */
    private ServerInfoObject m_sInfoObject = null;
    
    /**
     * Die Server-Vis-Komponente
     */
    private VisualisationServer m_visualisationServer = null;
    
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
        
        m_visualisationServer = new VisualisationServer();
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
        this.startInformationHandler();
        m_sInfoObject = ServerInfoObject.getInstance(pathName);
        m_path = m_sInfoObject.getScenarioPath();
        m_scenarioPath = m_sInfoObject.getScenarioPath();
        
        m_visualisationServer = new VisualisationServer();
    }
    
    /**
     * Konstruktor.
     *
     * @param port
     * @param csf
     * @param ssf
     * @throws <{java.rmi.RemoteException}>
     */
  /*  public Coordinator(
            int port,
            RMIClientSocketFactory csf,
            RMIServerSocketFactory ssf)
            throws RemoteException {
        super(port, csf, ssf);
        // starte den InformationHandler
        this.startInformationHandler();
    } */
    
        /* (non-Javadoc)
         * @see jAgentSimulator.server.ICoordinator#registerGame(java.util.Hashtable)
         */
    public Id registerGame(DataTransferObject dto, GameConf gameConf)
    throws RemoteException, InvalidElementException {
        System.out.println("Coordinator: Spiel soll registriert werden...");
        if (dto.containsKey("ScenarioName")) {
            String tmpScenarioString = (String) dto.get("ScenarioName");
                        /*
                         * Erstellen des wirklichen ScenarioNamens, d.h. den übergebenen
                         * Verzeichnisnamen + . + Scenario
                         */
            // Beginne mit dem Package(Verzeichnisnamen)
            StringBuffer scenarioName = new StringBuffer(tmpScenarioString);
            scenarioName.append("."); //scenarioName.append("scenariomanagement.");
            // Jetzt fehlt nur der Scenario-Zusatz
            scenarioName.append("Scenario");
            // Erzeuge die Id für das Spiel
            Id gameId = newGameId((String) dto.get("GameName"));
            AbstractScenario scenario = null;
            System.out.println("Coordinator: versuche " + scenarioName
                    + " zu registrieren");
            try {
                Class scenarioClass = Class.forName("scenarios." + scenarioName);
                scenario = (AbstractScenario)scenarioClass.newInstance();
                scenario.initialize(gameId,
                        m_informationHandler,
                        new Hashtable(dto),
                        gameConf);
                scenario.setVisServer(m_visualisationServer);
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
            // Id des Spiels zurückgeben
            return newGame.getId();
        } else {
            throw new InvalidElementException("Parameter ScenarioName");
        }
    }
    
    /**
     * Liefert eine neue Id, deren Bezeichnung aus dem übergebenen Namen,
     * einem Bindestrich und einer ganzen Zahl besteht. Die gelieferte Id
     * ist unter allen Spielen eindeutig.
     *
     * @param gameName  Der Name des Spiels.
     *
     * @return  Eine neue eindeutige Spiel-Id.
     */
    private Id newGameId(String gameName) {
        String neueBezeichnung;
        int zahl;
        
        zahl = 0;
        do {
            zahl++;
            neueBezeichnung = gameName + "-" + zahl;
        } while (m_games.containsKey(neueBezeichnung));
        
        return new Id(neueBezeichnung);
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
        
        // Das Spiel holen, aber vorher checken ob es überhaupt existiert.
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
        
        // Das Spiel holen, aber vorher checken ob es überhaupt existiert.
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
        Enumeration enumer = m_games.elements();
        Vector retval = new Vector();
        while (enumer.hasMoreElements()) {
            Game tmpGame = (Game) enumer.nextElement();
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
            // löse den Namen name zu einer Id auf.
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
            if (tmpGame.getAgents().size()>0){
                
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
            }else throw new InvalidGameException("No Agents registred");
        }else {
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
                         * füttern. Ansonsten wird kein neuer Thread erstellt und der alte
                         * kann nicht mehr verwendet werden.
                         */
            String tmpGameName=tmpGame.getGameName(); // "Spiel" + m_gameCounter;
            if (!tmpGame.isSimulationFinished()) try{
                tmpGame.join();
            }catch (InterruptedException ex){
                System.out.println("tmpGame");
            }
            m_gameCounter++;
            Game newGame =
                    new Game(tmpGameName,
                    m_informationHandler,
                    tmpGame);
            // ersetze im HT das alte durch das neue Spiel
            m_games.remove(id.toString());
            m_games.put(id.toString(), newGame);
            try{
                tmpGame.stopAgentThreads();
                tmpGame.stop();
            }catch(Exception ex){}
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
     * Liefert den Parameterwert des Parameters name eines Spiels zurück.
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
     * Liefert einen Liste der Scenarionamen zurück.
     *
     * @return LinkedList - Liste der Szenarionamen
     */
    public LinkedList getScenarioNames() throws RemoteException {
        LinkedList retval = new LinkedList();
        try {
            retval=this.m_sInfoObject.getScenarioNames();
            if ((retval!=null) && (retval.size() > 0)) {
                return retval;
            }
            return null;
        } catch (Exception ioE) {
            System.out.println("Fehler: " + ioE.toString());
            throw new RemoteException("Fehler: ", ioE);
        }
    }
    
    
   /* public LinkedList getScenarioNames() throws RemoteException {
        LinkedList retval = new LinkedList();
        try {
            File pathName = new File(m_scenarioPath);
            System.out.println("Szenariopfad: " + pathName);
            File[] files = pathName.listFiles();
    
            for (int i = 0; i < files.length; i++) {
                // Das .class Ende abschneiden, bevor die Daten zurück gegeben werden.
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
    } */
    
    
    /* (non-Javadoc)
     * @see simulator.ICoordinator#getScenarioParameter(java.lang.String)
     */
    public LinkedList getScenarioParameter(String scenarioName, GameConf gameConf)
    throws RemoteException {
        LinkedList retval = null;
        String className = "Scenario";
        System.out.println("Classname: " + className);
        try {
            System.out.println("Try: " + "scenarios."
                    + scenarioName
                    + "."
                    + className);
            Class scenarioClass =
                    Class.forName(
                    "scenarios."
                    + scenarioName //+ ".scenariomanagement"
                    + "."
                    + className);
            System.out.println("Coordinator: Erstelle eine neue SzenarioInstanz");
            AbstractScenario scenario =
                    (AbstractScenario) scenarioClass.newInstance();
            System.out.println("Coordinator: Hole Parameter.");
            return scenario.getScenarioParameter(gameConf);
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
            tmpGame.stopAgentThreads();
            // lösche Spiel aus Server
            tmpGame.close();
            m_games.remove(gameId.toString());
            // Stoppe Game-Thread
            try{
                tmpGame.stop();
            }catch (Exception exc){}
            tmpGame = null;
            
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
            Enumeration enumer = game.getAgents().elements();
            while (enumer.hasMoreElements()) {
                AbstractAgent tmpAgent = (AbstractAgent) enumer.nextElement();
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
        Enumeration enumer = m_games.elements();
        while (enumer.hasMoreElements()) {
            Game tmpGame = (Game) enumer.nextElement();
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
    
    
    /**
     * Liefert zu einem Scenario verfügbare Game-Konfigurationen
     * @param scenarioName Name des Szenario
     * @return verfügbare Game-Konfigurationen zu einem Scenario
     */
    public LinkedList getGameConfTags(String scenarioName) throws RemoteException {
        LinkedList liste=this.m_sInfoObject.getGameConfTags(scenarioName);
        if (liste.size()>=1) return liste;
        else throw new NoSuchElementException();
    }
    
    /**
     * Liefert die Configuratinsdaten zu einem bestimmte Konfig-Eintag.
     *
     * @param tagName Name des Eintrags, fuer den
     * die Configurationsdaten geliefert werden sollen.
     * @return Configurationsdaten fuer den vorgegebenen Eintagsnamen.
     */
    
    public GameConf getGameConfToTag(String tagName) throws RemoteException {
        GameConf gameConf=this.m_sInfoObject.getGameConfToTag(tagName);
        if (gameConf!=null) return gameConf;
        else throw new NoSuchElementException();
    }
    
}
