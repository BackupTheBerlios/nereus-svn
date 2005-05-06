/*
 * Dateiname      : AskAnotherTimeSpeachact.java
 * Erzeugt        : 29. Juli 2003
 * Letzte Änderung: 
 * Autoren        : Daniel Friedrich
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
package nereus.communication.speachacts;

import java.util.Hashtable;

import nereus.exceptions.InvalidElementException;


/**
 * Sprechakt soll anfragendem Agenten mitteilen, dass der Agent die Anfrage gerne
 * beantwortetn würde, ihm aber im Moment die Sachkenntnis dazu fehlt.
 * 
 * @author Daniel Friedrich
 */
public final class AskAnotherTimeSpeachact extends AnswerSpeachact {

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public AskAnotherTimeSpeachact(Hashtable parameters) {
		super(parameters);
	}

	/* (non-Javadoc)
	 * @see speachacts.Speachact#setContent(java.lang.Object)
	 */
	public void setContent(Object content) throws InvalidElementException {
		if(content != null) {
			throw new InvalidElementException();	
		}
	}
}
