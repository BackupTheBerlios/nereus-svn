/*
 * Dateiname      : InvaliClassifierException.java
 * Erzeugt        : 7. September 2003
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
 * Die Exception wird geworfen, wenn ein verwendeter Classifier nicht der 
 * erwarteten Form entspricht. Z.B. wenn ein Id3 übergeben wurde, aber ein 
 * NaiveBayes benötigt wird.
 * 
 * @author Daniel Friedrich
 */
public class InvalidClassifierException extends Exception {

	/**
	 * Konstruktor.
	 */
	public InvalidClassifierException() {
		super(
			"Der verwendete Classifier entspricht nicht der erwarteten Form.");
	}

	/**
	 * Konstruktor.
	 * 
	 * @param message
	 */
	public InvalidClassifierException(String message) {
		super(
			"Der verwendete Classifier "
				+ message
				+ " entspricht nicht der erwarteten Form.");
	}

	/**
	 * Konstruktor.
	 *  
	 * @param message
	 * @param cause
	 */
	public InvalidClassifierException(String message, Throwable cause) {
		super(
			"Der verwendete Classifier "
				+ message
				+ " entspricht nicht der erwarteten Form.",
			cause);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param cause
	 */
	public InvalidClassifierException(Throwable cause) {
		super(
			"Der verwendete Classifier entspricht nicht der erwartenden Form.",
			cause);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return 
			"Der verwendete Classifier entspricht nicht der erwarteten Form.";
	}
}
