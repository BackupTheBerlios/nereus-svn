/*
 * Created on 17.06.2003
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

import java.util.Hashtable;

import utils.Id;
import visualisation.IVisualisation;
import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;
import exceptions.InvalidGameException;

/**
 * Das Interface bieten den Zugriff auf den auf der Server-Seite arbeitenden
 * InformationHandler an.
 * 
 * @author Daniel Friedrich
 */
public interface IInformationHandler {
	/**
	 * Registriert eine Visualisierungskomponente beim Spiel.
	 * 
	 * @param Id agentId - Id des Agenten zu dem die V-Komponente gehört.
	 * @param IVisualisation component - Die V-Komponente. 
	 */
	public void registerVisualisation(
		Id agentId,
		Id gameId,
		IVisualisation component)
		throws InvalidAgentException, 
			   InvalidGameException, 
			   InvalidElementException;

	/**
	 * Aktualisiert den Visualisierungs-Parameter parameter beim Client.
	 * 
	 * @param String parameter - Name des Attributes das aktualisiert wird.
	 * @param Id agentId - Id des Agenten dessen Visualisierungen akt. werden
	 * @param double value - Inhalt der beim Client ausgegeben werden soll.
	 * @throws AgentExecutionException
	 */
	public void updateVisParameter(
		int parameter,
		Id agentId,
		Id gameId,
		double value)
		throws InvalidAgentException, 
			   InvalidGameException, 
			   InvalidElementException;

	/**
	 * Registriert einen Agenten zur Kommunikation
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Id agentId - Id des Agenten der die Info benötigt.
	 * 
	 * @throws InvalidActionKeyException
	 */
	public void registerAgentForCommunication(Id agentId, Id gameId);

	/**
	 * Loggt eine Information vom Agenten.
	 * 
	 * @param Id agentId - Id des Agenten der etwas loggen will.
	 * @param String logInfo - Information die geloggt werden soll.
	 * @throws AgentExecutionException
	 */
	public void log(
		Id agentId, 
		Id gameId, 
		int type, 
		String logInfo)
		throws InvalidAgentException, 
			   InvalidGameException, 
			   InvalidElementException;

	/**
	 * Loggt eine Information von einem Spiel
	 * 
	 * @param Id gameId - Id des Spiels das etwas loggen will.
	 * @param String logInfo - Information die geloggt werden soll.
	 * @throws AgentExecutionException
	 */
	public void log(
		Id gameId, 
		int type, 
		String logInfo)
		throws InvalidAgentException, 
			   InvalidGameException, 
			   InvalidElementException;

	/**
	 * Gibt das Log für den Agenten agentId zurück.
	 * 
	 * @param Id agentId - Id des Agenten für den das Log angefordert wird.
	 * @return String - das angeforderte Log
	 * @throws InvalidAgentException
	 */
	public String getLog(Id agentId)
		throws InvalidAgentException, 
			   InvalidElementException;
	;

	/**
	 * Gibt das Log aller Agenten aus.
	 * 
	 * @return String - das angeforderte Log
	 * @throws InvalidAgentException
	 */
	public String getCompleteLog(Id gameId) throws InvalidGameException;

	/**
	 * Setzt die Visualisierungskomponenten aller übergebenen Agenten zurück.
	 * @param agents
	 * @throws InvalidAgentException
	 */
	public void resetVisualisations(Hashtable agents)
		throws InvalidAgentException;		
}
