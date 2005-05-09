/*
 * Dateiname      : IInformationHandler.java
 * Erzeugt        : 17. Juni 2003
 * Letzte Änderung: 
 * Autoren        : Daniel Friedrich
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

import java.util.Hashtable;

import nereus.utils.Id;
import nereus.visualisation.IVisualisation;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidElementException;
import nereus.exceptions.InvalidGameException;

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
     * Aktualisiert die Umgebung der/des Agenten beim Client.
     * @since 13 juli 2004 
     */
    void updateVisEnvironment(  Id gameId, Object environment);

    /**
     * Registriert eine Umgebung zur Kommunikation 
     * @since 13 juli 2004
     */
    void registerEnvironmentForCommunication(Id gameId);

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
