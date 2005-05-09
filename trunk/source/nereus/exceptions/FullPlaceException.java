/*
 * Dateiname      : FullPlaceException.java
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
 * Die Exception wird geworfen, wenn ein Platz schon von einem Agenten belegt 
 * ist und deshalb dort kein weiterer Agent gespeichert werden kann.
 * 
 * @author Daniel Friedrich
 */
public class FullPlaceException extends FullEnviromentException {
	
	private String m_message;
	/**
	 * Konstruktor.
	 */
	public FullPlaceException() {
		super();
		m_message = "Der Platz ist besetzt der Agent kann nicht mehr" 
			+ " hinzugefügt werden.";
	}

	/**
	 * Konstruktor.
	 * @param message
	 */
	public FullPlaceException(String message) {
		super();
		m_message = "Der Platz " 
			+ message 
			+ " ist besetzt der Agent kann nicht mehr" 
			+ " hinzugefügt werden.";
	}

	/**
	 * Konstruktor.
	 * @param message
	 * @param cause
	 */
	public FullPlaceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor.
	 * @param cause
	 */
	public FullPlaceException(Throwable cause) {
		super(cause);
		m_message = "Der Platz ist besetzt der Agent kann nicht mehr" 
			+ " hinzugefügt werden.";
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		if(m_message != null) {
			return m_message;
		}
		return super.getMessage();
	}

}
