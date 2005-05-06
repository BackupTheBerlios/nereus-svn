/*
 * Dateiname      : InvalidAgentNumberException.java
 * Erzeugt        : 4. August 2003
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
package nereus.exceptions;

/**
 * Wird ausgeloest, wenn ein Client eine neue Gruppe anmelden will und dabei
 * eine Agentenanzahl angibt, die entweder kleiner 1 ist, oder den in der
 * Szenarienbeschreibung angegebenen Bedingungen wiederspricht.<P>
 * 
 * @author Daniel Friedrich
 */
public class InvalidAgentNumberException extends Exception {

	private int intGivenNumber;

    //Konstruktoren:
    public InvalidAgentNumberException(){};

    public InvalidAgentNumberException(String strMessage, int intGivenNumber)
    {
	//!!! Dietmar, kannst du folgendes loesen:

	// Irgendwie den Bisherige Konstruktor mit strMessage aufrufen

	this.intGivenNumber = intGivenNumber;
    }

    public String getLocalizedMessage()
    {
		// if //Abfragen, ob ein Messagetext uebergeben wurde
		 //   { /* Diese Beschreibung ausgeben */ }
		//else
    	//{ /* Standarrdbeschreibung ausgeben */ }
		return this.getMessage();
    }
}
