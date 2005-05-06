/*
 * Dateiname      : AbstractScenarioHandler.java
 * Erzeugt        : 13. Mai 2003
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

import nereus.communication.speachacts.Speachact;
import nereus.exceptions.InvalidActionKeyException;
import nereus.exceptions.InvalidElementException;





/**
 * Der ScenarioHandler wird aus Sicherheitsgründen implementiert. Er soll den 
 * Agenten einen indirekten Zugriff auf das Scenario erlauben, ohne das die 
 * Agenten das Scenario jemals in die Hand bekommen.
 * 
 * @author Daniel Friedrich
 */
public abstract class AbstractScenarioHandler implements IScenarioHandler {
    /**
     * Szenario, dass der Agent schützt
     */
	protected AbstractScenario m_scenario;
	 
	/**
	 * Erzeugt den gewünschten Sprechakt.
	 * 
	 * @param speachactClass - Klasse des gewünschten Sprechaktes
	 * @return Speachact - erstellt Sprechakt.
	 * @throws InvalidActionKeyException
	 */
	public Speachact createSpeachAct(Class speachactClass) 
		throws InvalidActionKeyException,
				InvalidElementException {
		return m_scenario.createSpeachAct(speachactClass);			 
	} 	
}


