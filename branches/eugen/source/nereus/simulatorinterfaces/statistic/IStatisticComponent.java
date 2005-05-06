/*
 * Dateiname      : IStatisticComponent.java
 * Erzeugt        : 4. August 2003
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
package nereus.simulatorinterfaces.statistic;

import java.util.Vector;

import nereus.utils.Id;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidElementException;

/**
 * Die Schnittstelle definiert die Methoden, die eine szenariospezifische 
 * Statistikkomponente erfassen muss.
 * 
 * @author Daniel Friedrich
 */
public interface IStatisticComponent {
	
	/**
	 * Konstante für den Parameter zur Erfassung der Kommunikationskosten.
	 */
	public static final String COMMCOSTS = "Kommunikationskosten";
	/**
	 * Konstante für den Parameter zur Erfassung der Anzahl der übermittelten Nachrichten.
	 */
	public static final String NUMOFMESSAGES = "Anzahl an Nachrichten";
	
	/**
	 * Gibt den ermittelten Wert des Parameter parameter zurück.
	 * 
	 * @param parameter - Name des Parameters.
	 * @return Object - Wert des Parameters.
	 */
	public Object getParameter(String parameter);
		
	/**
	 * Gibt den Zusatz (z.B. % oder Runden) des Parameters parameter zurück.
	 * 
	 * @param parameter - Name des Parameters
	 * @return String - Zusatz
	 */
	public String getParameterPrefix(String parameter);	
	
	/**
	 * Fügt die übergebene Information der statistischen Erfassung hinzu.
	 * 
	 * @param agentId
	 * @param parameter
	 * @param value
	 * @throws InvalidAgentException
	 * @throws InvalidElementException
	 */
	public void addInformation(Id agentId, String parameter, Object value)
		throws InvalidAgentException,
			   InvalidElementException;
	
	/**
	 * Gibt die erfassten Parameternamen zurück.
	 * 
	 * @return Vector mit den Parameternamen
	 */
	public Vector getParameters();
	
	/**
	 * Gibt einen String mit den ermittelten Statistik-Daten im CSV-Format zurück.
	 * 
	 * @return String - Statistik im CSV-Format
	 */
	public String getCSVDataset();
	
	/**
	 * Sorgt dafür, dass die Statistik erstellt wird.
	 */
	public void createStatistic();
	
	/**
	 * Gibt die Anzahl der Agenten zurück, für die die Statistik erfasst wird.
	 * 
	 * @return int - Anzahl an Agenten.
	 */
	public int getNumOfAgents();
}
