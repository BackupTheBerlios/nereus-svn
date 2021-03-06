/*
 * Dateiname      : InvalidException.java
 * Erzeugt        : 7. September 2003
 * Letzte �nderung:
 * Autoren        : Daniel Friedrich
 *
 *
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut f�r Intelligente Systeme
 * der Universit�t Stuttgart unter Betreuung von Dietmar Lippold
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
 * Superklasse in der Hierarchie der Invalid..Exception.
 *
 * @author Daniel Friedrich
 */
public class InvalidException extends Exception {
    
    /**
     * Konstruktor.
     */
    public InvalidException() {
        super();
    }
    
    /**
     * Konstruktor.
     *
     * @param messageText
     */
    public InvalidException(String messageText) {
        super(messageText);
    }
    
    /**
     * Konstruktor.
     *
     * @param messageText
     * @param cause
     */
    public InvalidException(String messageText, Throwable cause) {
        super(messageText, cause);
    }
    
}
