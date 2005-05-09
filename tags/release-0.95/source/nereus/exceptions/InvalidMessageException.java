/*
 * Dateiname      : InvalidMessageException.java
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
 * Wird ausgelöst, wenn entweder die angegebene Nachricht nicht existent ist,
 * oder aus irgendeinem Szenariospezifischen Grund an dieser Stelle verwendet
 * werden darf. Welcher der beiden Fälle eingetreten ist, wird aus
 * Sicherheitsgründen nicht bekanntgegeben.
 * 
 * @author Daniel Friedrich
 */
public class InvalidMessageException extends Exception {

	/**
	 * Konstruktor.
	 */
	public InvalidMessageException() {
		super("Nachricht mit der Id " 
		+ " existiert oder darf an dieser Stelle nicht verwendet werden.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidMessageException(String message) {
		super("Nachricht mit der Id " 
			+ message 
			+ " existiert oder darf an dieser Stelle nicht verwendet werden.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 * @param cause
	 */
	public InvalidMessageException(String message, Throwable cause) {
		super("Nachricht mit der Id " 
			+ message 
			+ " existiert oder darf an dieser Stelle nicht verwendet werden.",
		cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidMessageException(Throwable cause) {
		super("Nachricht mit der Id "
			+ " existiert oder darf an dieser Stelle nicht verwendet werden",
			cause);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "Nachricht mit der Id " 
			+ " existiert oder darf an dieser Stelle nicht verwendet werden.";
	}	
}
