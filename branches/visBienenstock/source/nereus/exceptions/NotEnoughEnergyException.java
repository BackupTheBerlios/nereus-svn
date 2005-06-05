/*
 * Dateiname      : NotEnoughEnergyException.java
 * Erzeugt        : 16. Juni 2003
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
 * Die Exception wird ausgelöst, wenn ein Agent nicht über genügend Energie zum
 * Ausführen einer Aktion verfügt.
 *
 * @author Daniel Friedrich
 */
public class NotEnoughEnergyException extends Exception {
    
    /**
     * Konstruktor.
     */
    public NotEnoughEnergyException() {
        super(
                "Der Agent verfügt nicht über genügend Energie zum Ausführen "
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
                + " verfügt nicht über genügend Energie zum Ausführen der Aktion.");
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
                + " verfügt nicht über genügend Energie zum Ausführen der Aktion.",
                cause);
    }
    
    /**
     * Konstruktor.
     *
     * @param cause
     */
    public NotEnoughEnergyException(Throwable cause) {
        super("Der Agent "
                + " verfügt nicht über genügend Energie zum Ausführen der Aktion",
                cause);
    }
}
