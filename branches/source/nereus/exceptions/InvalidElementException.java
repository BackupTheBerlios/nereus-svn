/*
 * Dateiname      : InvaliElementException.java
 * Erzeugt        : 19. Juni 2003
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
 * Die Exception dient zum Melden, dass ein Element nicht existiert oder falsch 
 * verwendet wurde.
 * 
 * @author Daniel Friedrich
 */
public class InvalidElementException extends InvalidException {

	/**
	 * Konstruktor.
	 */
	public InvalidElementException() {
		super(
			"Das verwendete Element exisitiert nicht oder wurde falsch "
			+ "verwendet.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidElementException(String message) {
		super(
			"Das verwendete Element " 
			+ message 
			+ " exisitiert nicht oder wurde falsch verwendet.");
	}

	/**
	 * Konstruktor.
	 *  
	 * @param message
	 * @param cause
	 */
	public InvalidElementException(
		String message,
		Throwable cause) {
		super(
			"Das verwendete Element " 
			+ message + 
			" existiert nicht oder wurde falsch verwendet.", cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidElementException(Throwable cause) {
		super(
			"Das verwendete Element existiert nicht oder wurde falsch "
			+ "verwendet.",
			cause);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return "Das verwendete Element exisitiert nicht oder wurde falsch "
			+ "verwendet.";
	}
}
