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

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Vector;

import utils.DataTransferObject;
import utils.Id;
import visualisation.IVisualisation;
import exceptions.InvalidElementException;
import exceptions.InvalidGameException;

/**
 * Das Interface definiert alle vom Server angebotenen Dienste, hierzu wird die
 * Java-RMI-Technologie verwendet. Das Interface wird von der Klasse Coordinator
 * implementiert.
 *
 * Die Klasse Coordinator dient als Verwaltungseinheit und Schnittstelle zur
 * Kommunikation mit dem Client (hauptsaechlich mit der Klasse SpielAnmeldung).
 * Sie ist die einzigste Klasse die serverseitig RMI-Methoden anbietet. Die
 * angebotenen RMI-Methoden k�nnen vom Client aufgerufen werden, um Spiele
 * (Klasse Game), Agenten (Interface IAgent) und Visualisierungskomponenten
 * (Interface IVisualisation) anzumelden.
 * 
 * @author Daniel Friedrich
 */
public interface ICoordinator extends Remote {
	/**
	     * Die Methode erzeugt ein Game(Spiel), d.h. die Id und die Klasse.
	     * Zur Erzeugung des Spiels werden die Parameter, die in der Hashtable
	     * params gespeichert sind, ben�tig. Der Client bekommt f�r den Zugriff
	     * auf das neu erstellte Spiel, die Id des Spiels zur�ck geliefert.
	     *
	     * @param  Hashtable params  - Parameterwerte des Games
	     * @return Id - id des Games
	     */
	public Id registerGame(DataTransferObject dto)
		throws RemoteException, InvalidElementException;

	/**
	 * Liefert den Namen des Spiels mit der Id id zur�ck.
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
	 * Liefert true zur�ck, wenn das Spiel mit der Id id registriert und offen ist.
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
	 * Liefert einen Vector mit allen offene Spiele zur�ck.
	 *
	 * Ein Spiel ist solange offen, wie es registriert ist und weniger als
	 * die maximale Anzahl an Agenten angemeldet sind.
	 * 
	 * @throws RemoteException
	 */
	public Vector listAllOpenedGames() throws RemoteException;

	/**
	 * Registriert eine Visualisierungskomponente f�r einen bestimmten Agenten.
	 *
	 * Registriert eine Visualisierungskomponente f�r den Agenten mit der Id
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
	 * Registriert eine Visualisierungskomponente f�r einen bestimmten Agenten.
	 *
	 * Registriert eine Visualisierungskomponente f�r den Agenten mit dem Namen
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
	 * Liefert den Parameterwert des Parameters name eines Spiels zur�ck.
	 *
	 * @param Id gameId
	 * @param String name
	 * @return Object - Parameterwert
	 * @throws RemoteException
	 */
	public Object getParameter(Id gameId, String name)
		throws RemoteException, InvalidGameException;

	/**
	 * Liefert einen Vector voller Strings mit den Scenarionamen zur�ck.
	 *
	 * @return Vector - Menge aller Szenarios
	 * @throws RemoteException
	 */
	public LinkedList getScenarioNames() throws RemoteException;

	/**
	 * Liefert eine Liste mit den Szenarioparametern zur�ck.
	 * 
	 * @param scenarioName
	 * @return LinkedList Liste der Szenarioparameter
	 * @throws RemoteException
	 */
	public LinkedList getScenarioParameter(String scenarioName)
		throws RemoteException;

	/**
	 * Liefert eine Liste mit den Spielparametern zur�ck.
	 * 
	 * @return LinkedList - List der Spielparameter
	 * @throws RemoteException
	 */
	public LinkedList getGameParameter() throws RemoteException;

	/**
	 * Liefert einen Vektor mit den Ids der Agenten des Spiels (gameId) zur�ck.
	 * 
	 * @param Id gameId - Id des Spiels
	 * @return Vector - Menge der Agenten des Spiels
	 * @throws RemoteException
	 * @throws InvalidElementException
	 */
	public Vector getAgentsForGame(Id gameId)
		throws RemoteException, InvalidElementException;

	/**
	 * Liefert ein File mit den Ergebnissen der Simulation zur�ck.
	 * 
	 * @param gameId
	 * @return String - Simulationsergenisse
	 * @throws RemoteException
	 * @throws InvalidElementException
	 */
	public String getSimulationResult(Id gameId)
		throws RemoteException, InvalidElementException;

	/**
	 * Liefert einen Vector mit den Namen der Spiele die ge�ffnet werden k�nnen.
	 * 
	 * @return Vector - Menge aller verf�gbaren Spiel 
	 * @throws RemoteException
	 */
	public Vector getAvailableGames() throws RemoteException;

	/**
	 * Liefert die Parameter eines Spiels zur�ck.
	 * @param gameId
	 * @return DataTransferObject - Transferobjekt mit allen Parametern
	 * @throws RemoteException
	 * @throws InvalidElementException
	 */
	public DataTransferObject getParameters(Id gameId)
		throws RemoteException, InvalidElementException;

	/**
	 * Liefert die Id f�r einen Spielnamen zur�ck.
	 * 
	 * @param gameName
	 * @return Id - Id des Spiels
	 * @throws RemoteException
	 * @throws InvalidElementException
	 */
	public Id getGameId(String gameName)
		throws RemoteException, InvalidElementException;
}
