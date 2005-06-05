/*
 * Dateiname      : ICoordinator.java
 * Erzeugt        : 13. Mai 2003
 * Letzte Änderung: Eugen Volk am 12.05.05
 * Autoren        : Daniel Friedrich
 *                  Eugen Volk
 *
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
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package nereus.simulatorinterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Vector;

import nereus.utils.DataTransferObject;
import nereus.utils.Id;
import nereus.utils.GameConf;
import nereus.visualisation.IVisualisation;
import nereus.exceptions.InvalidElementException;
import nereus.exceptions.InvalidGameException;

/**
 * Das Interface definiert alle vom Server angebotenen Dienste, hierzu wird die
 * Java-RMI-Technologie verwendet. Das Interface wird von der Klasse Coordinator
 * implementiert.
 *
 * Die Klasse Coordinator dient als Verwaltungseinheit und Schnittstelle zur
 * Kommunikation mit dem Client (hauptsaechlich mit der Klasse SpielAnmeldung).
 * Sie ist die einzigste Klasse die serverseitig RMI-Methoden anbietet. Die
 * angebotenen RMI-Methoden können vom Client aufgerufen werden, um Spiele
 * (Klasse Game), Agenten (Interface IAgent) und Visualisierungskomponenten
 * (Interface IVisualisation) anzumelden.
 *
 * @author Daniel Friedrich
 */
public interface ICoordinator extends Remote {
    /**
     * Die Methode erzeugt ein Game(Spiel), d.h. die Id und die Klasse.
     * Zur Erzeugung des Spiels werden die Parameter, die in der Hashtable
     * params gespeichert sind, benötig. Der Client bekommt für den Zugriff
     * auf das neu erstellte Spiel, die Id des Spiels zurück geliefert.
     *
     * @param  Hashtable params  - Parameterwerte des Games
     * @param gameConf KonfigurationStruktur mit Karten- und ScenarioKonfigDatei-Namen
     * @return Id - id des Games
     */
    public Id registerGame(DataTransferObject dto,GameConf gameConf)
    throws RemoteException, InvalidElementException;
    
    /**
     * Liefert den Namen des Spiels mit der Id id zurück.
     *
     * @param  String id -  Id des Spiels
     *
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public String getGameName(Id id)
    throws RemoteException, InvalidGameException;
    
    /**
     * Schliesst ein Spiel auf dem Server.
     *
     * @param Id gameId - Id des Spiels das geschlossen werden soll
     * @return boolean - true -> Spiel geschlossen, false -> Spiel noch offen.
     *
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public boolean removeGame(Id gameId)
    throws RemoteException, InvalidGameException;
    
    /**
     * Liefert true zurück, wenn das Spiel mit der Id id registriert und offen ist.
     *
     * @param  String - Id des Spiels
     * @return boolean - True Spiel angemeldet und offen, False sonst
     *
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public boolean isGameRegisteredAndOpen(Id id)
    throws RemoteException, InvalidGameException;
    
    /**
     * Registriert einen Agenten beim Spiel mit der Id gameId.
     *
     * @param Id gameId
     * @param String agentName
     * @param String agentClass
     * @param String serverName
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public void registerAgent(
            Id gameId,
            String agentName,
            String agentClass,
            String serverName)
            throws RemoteException, InvalidGameException;
    
    /**
     * Registriert einen Agenten und seine Visualisierungskomponente beim Spiel mit der Id gameId.
     *
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public void registerAgent(Id gameId,
            //AbstractAgent agent,
            String agentName,
            String agentClass,
            String serverName,
            IVisualisation visualisation)
            throws RemoteException, InvalidGameException;
    
    /**
     * Liefert einen Vector mit allen offene Spiele zurück.
     *
     * Ein Spiel ist solange offen, wie es registriert ist und weniger als
     * die maximale Anzahl an Agenten angemeldet sind.
     *
     * @throws RemoteException
     */
    public Vector listAllOpenedGames() throws RemoteException;
    
    /**
     * Registriert eine Visualisierungskomponente für einen bestimmten Agenten.
     *
     * Registriert eine Visualisierungskomponente für den Agenten mit der Id
     * agentId beim Spiel mit der Id gameId.
     *
     * @param Id gameId - Id des Spiels
     * @param Id agentId - Id des Agenten
     * @param IVisualisierung visualisation - Visualisierungkomponente
     *
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public void registerVisualisation(
            Id agentId,
            Id gameId,
            IVisualisation visualisation)
            throws RemoteException, InvalidElementException;
    
    /**
     * Registriert eine Visualisierungskomponente für einen bestimmten Agenten.
     *
     * Registriert eine Visualisierungskomponente für den Agenten mit dem Namen
     * agentName beim Spiel mit der Id gameId.
     *
     * @param Id gameId - Id des Spiels
     * @param String agentName - Name des Agenten
     * @param IVisualisierung visualisation - Visualisierungkomponente
     *
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public void registerVisualisation(
            Id gameId,
            String agentName,
            IVisualisation visualisation)
            throws RemoteException, InvalidElementException;
    
    /**
     * Startet das Spiel.
     *
     * @param Id id - Id des Spiels
     *
     * @throws <{RemoteException}>
     * @throws InvalidElementException
     */
    public void startGame(Id id) throws RemoteException, InvalidGameException;
    
    /**
     * Startet ein bereits gelaufenes Spiel neu.
     *
     * @param Id id - Id des Spiels, das neu gestartet werden soll.
     * @throws <{RemoteException}>
     * @throws InvalidElementException
     */
    public void restartGame(Id id)
    throws RemoteException, InvalidGameException;
    
    /**
     * Liefert den Parameterwert des Parameters name eines Spiels zurück.
     *
     * @param Id gameId
     * @param String name
     * @return Object - Parameterwert
     * @throws RemoteException
     */
    public Object getParameter(Id gameId, String name)
    throws RemoteException, InvalidGameException;
    
    /**
     * Liefert einen Vector voller Strings mit den Scenarionamen zurück.
     *
     * @return Vector - Menge aller Szenarios
     * @throws RemoteException
     */
    public LinkedList getScenarioNames() throws RemoteException;
    
    /**
     * Liefert eine Liste mit den Szenarioparametern zurück.
     *
     * @param scenarioName
     * @return LinkedList Liste der Szenarioparameter
     * @throws RemoteException
     */
    public LinkedList getScenarioParameter(String scenarioName, GameConf gameConf)
    throws RemoteException;
    
    /**
     * Liefert eine Liste mit den Spielparametern zurück.
     *
     * @return LinkedList - List der Spielparameter
     * @throws RemoteException
     */
    public LinkedList getGameParameter() throws RemoteException;
    
    /**
     * Liefert einen Vektor mit den Ids der Agenten des Spiels (gameId) zurück.
     *
     * @param Id gameId - Id des Spiels
     * @return Vector - Menge der Agenten des Spiels
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public Vector getAgentsForGame(Id gameId)
    throws RemoteException, InvalidElementException;
    
    /**
     * Liefert ein File mit den Ergebnissen der Simulation zurück.
     *
     * @param gameId
     * @return String - Simulationsergenisse
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public String getSimulationResult(Id gameId)
    throws RemoteException, InvalidElementException;
    
    /**
     * Liefert einen Vector mit den Namen der Spiele die geöffnet werden können.
     *
     * @return Vector - Menge aller verfügbaren Spiel
     * @throws RemoteException
     */
    public Vector getAvailableGames() throws RemoteException;
    
    /**
     * Liefert die Parameter eines Spiels zurück.
     * @param gameId
     * @return DataTransferObject - Transferobjekt mit allen Parametern
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public DataTransferObject getParameters(Id gameId)
    throws RemoteException, InvalidElementException;
    
    /**
     * Liefert die Id für einen Spielnamen zurück.
     *
     * @param gameName
     * @return Id - Id des Spiels
     * @throws RemoteException
     * @throws InvalidElementException
     */
    public Id getGameId(String gameName)
    throws RemoteException, InvalidElementException;
    
    /**
     * Liefert zu einem Scenario verfügbare Game-Konfigurationen
     * @param scenarioName Name des Szenario
     * @return verfügbare Game-Konfigurationen zu einem Scenario
     */
    public LinkedList getGameConfTags(String scenarioName)
    throws RemoteException;
    
    /**
     * Liefert die Configuratinsdaten zu einem bestimmte Konfig-Eintag.
     *
     * @param tagName Name des Eintrags, fuer den
     * die Configurationsdaten gelesen werden sollen.
     *
     * @return Configurationsdaten fuer den vorgegebenen Eintagsnamen.
     */
    public GameConf getGameConfToTag(String tagName) 
    throws RemoteException;
            
}
