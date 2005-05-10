/*
 * Dateiname      : Koordinate.java
 * Erzeugt        : 21. Oktober 2004
 * Letzte Änderung: 26. Januar 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *                  
 *                  
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut für
 * Intelligente Systeme der Universität Stuttgart unter Betreuung von
 * Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package scenarios.bienenstock.agenteninfo;

/**
 * ist der Datentyp mit dem eine Position auf der Spielkarte angegeben wird.
 * 
 * @author Philip Funck
 * @author Samuel Walz 
 */
public class Koordinate {
    /**
     * die X-Koordinate.
     */
    private int xPosition;
    
    /**
     * die Y-Koordinate.
     */
    private int yPosition;

    /**
     * Der Konstruktor.
     * 
     * @param x     Die X-Koordinate
     * @param y     Die Y-Koordinate
     */
    public Koordinate(int x, int y) {
        xPosition = x;
        yPosition = y;
    }

    /**
     * gibt die X-Koordinate zurück.
     * 
     * @return int
     */
    public int gibXPosition() {
        return xPosition;
    }

    /**
     * gibt die Y-Koordinate zurück.
     * 
     * @return int
     */
    public int gibYPosition() {
        return yPosition;
    }

    /**
     * vergleicht ein anderes Objekt auf Gleichheit mit sich selbst.
     * 
     * Der Test verläuft erfolgreich bei gleichem Objekttyp und
     * gleichen Koordinaten.
     * 
     * @param obj    das Objekt mi dem verglichen werden soll.
     * @return          true bei Gleichheit
     */
    public boolean equals(Object obj) {
        if (obj instanceof Koordinate) {
            return (xPosition == ((Koordinate) obj).gibXPosition())
                && (yPosition == ((Koordinate) obj).gibYPosition());
        } else {
            return false;
        }
    }
    
    /**
     * generiert einen Hashcode für das Objekt.
     * 
     * @return   X-Koordinate * 1000 + Y-Koordinate
     */
    public int hashCode() {
        return ((xPosition * 1000) + yPosition);
    }
}
