/*
 * Dateiname      : IScenarioHandler.java
 * Erzeugt        : 18. Juli 2003
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
import nereus.utils.ActionKey;
import nereus.utils.Id;
import nereus.exceptions.InvalidActionKeyException;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidElementException;

/**
 * Die Schnittstelle definiert alle Methoden, die ein Szenariohandler 
 * implementieren muss.
 * 
 * @author Daniel Friedrich
 */
public interface IScenarioHandler {

	/**
	 * Liefert einen Wert für einen angefragten Parameter zurück.
	 * 
	 * @param key - Identitätsschlüssel des Agenten
	 * @param agentId - Id des Agenten
	 * @param name - Name des Parameters
	 * @return Object - Wert des Parameters
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 * @throws InvalidElementException
	 */
	public Object getParameter(
		ActionKey key, 
		Id agentId, 
		String name) 
		throws InvalidAgentException,
			InvalidActionKeyException,
			InvalidElementException;	

	/**
	 * Erzeugt den gewünschten Sprechakt.
	 * 
	 * @param speachact - Klasse des gewünschten Sprechakts
	 * @return Speachact - gewünschter Sprechakt.
	 * @throws InvalidActionKeyException
	 */
	public Speachact createSpeachAct(Class speachact) 
		throws InvalidActionKeyException,
			   InvalidElementException;										
}
