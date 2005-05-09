/*
 * Dateiname      : InvalidAgentException.java
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
 * Die Exception wird ueberall dort ausgeloest, wo ein Verweis auf den aktuellen 
 * Agenten uebergeben werden soll. Und zwar dann, wenn der angegebene 
 * Objektverweis mit keinem beim Server registrierten Agenten uebereinstimmt.
 * 
 * @author Daniel Friedrich
 */
public class InvalidAgentException extends Exception {

	/**
	 * Konstruktor.
	 */
	public InvalidAgentException() {
		super("Agent mit der Id "
			+ " existiert nicht oder die erwartete Id stimmt nicht mit der "
			+ "erwarteten Id überein.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidAgentException(String message) {
		super("Agent mit der Id " 
			+ message 
			+ " existiert nicht oder die erwartete Id stimmt nicht mit der "
			+ "erwarteten Id überein.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 * @param cause
	 */
	public InvalidAgentException(String message, Throwable cause) {
		super("Agent mit der Id " 
			+ message 
			+ " existiert nicht oder die erwartete Id stimmt nicht mit der "
			+ "erwarteten Id überein.",
		cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidAgentException(Throwable cause) {
		super("Agent mit der Id "
			+ " existiert nicht oder die erwartete Id stimmt nicht mit der "
			+ "erwarteten Id überein.",
			cause);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "Agent mit der Id "
			+ " existiert nicht oder die erwartete Id stimmt nicht mit der "
			+ "erwarteten Id überein.";
	}	
}
