/*
 * Dateiname      : DenyAnswerSpeachact.java
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
 * Sprechakt um Anfragen abzulehnen. Der Sprechakt kann keine Inhalte 
 * transportieren, kostet dafür aber auch nichts, wenn die für Antworten
 * extra Kosten erhoben werden.
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class DenyAnswerSpeachact extends AnswerSpeachact {

	/**
	 * @param parameters
	 */
	public DenyAnswerSpeachact(Hashtable parameters) {
		super(parameters);
	}

	/**
	 * Die Methode erlaubt kein Setzen eines Inhaltes.
	 */
	public final void setContent(Object content) 
		throws InvalidElementException {
		if(content != null) {
			throw new InvalidElementException();
		}
		m_content = null;
	}
}
