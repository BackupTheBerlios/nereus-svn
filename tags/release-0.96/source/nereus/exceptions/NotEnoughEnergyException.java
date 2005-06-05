/*
 * Dateiname      : NotEnoughEnergyException.java
 * Erzeugt        : 16. Juni 2003
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
 * Die Exception wird ausgel�st, wenn ein Agent nicht �ber gen�gend Energie zum
 * Ausf�hren einer Aktion verf�gt.
 *
 * @author Daniel Friedrich
 */
public class NotEnoughEnergyException extends Exception {
    
    /**
     * Konstruktor.
     */
    public NotEnoughEnergyException() {
        super(
                "Der Agent verf�gt nicht �ber gen�gend Energie zum Ausf�hren "
                + "der Aktion.");
    }
    
    /**
     * Konstruktor.
     *
     * @param message
     */
    public NotEnoughEnergyException(String message) {
        super("Der Agent "
                + message
                + " verf�gt nicht �ber gen�gend Energie zum Ausf�hren der Aktion.");
    }
    
    /**
     * Konstruktor.
     *
     * @param message
     * @param cause
     */
    public NotEnoughEnergyException(String message, Throwable cause) {
        super("Der Agent "
                + message
                + " verf�gt nicht �ber gen�gend Energie zum Ausf�hren der Aktion.",
                cause);
    }
    
    /**
     * Konstruktor.
     *
     * @param cause
     */
    public NotEnoughEnergyException(Throwable cause) {
        super("Der Agent "
                + " verf�gt nicht �ber gen�gend Energie zum Ausf�hren der Aktion",
                cause);
    }
}
