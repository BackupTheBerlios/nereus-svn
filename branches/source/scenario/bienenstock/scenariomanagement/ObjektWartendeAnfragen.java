/*
 * Dateiname      : ObjektWartendeAnfragen.java
 * Erzeugt        : 16. Oktober 2004
 * Letzte �nderung: 26. Januar 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *                  
 *                  
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut f�r
 * Intelligente Systeme der Universit�t Stuttgart unter Betreuung von
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

package scenario.bienenstock.scenariomanagement;

/**
 * dient als Warteschlange f�r die Anfragen der Agenten.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
public class ObjektWartendeAnfragen {

    /**
     * Anzahl der wartenden Agentenanfragen.
     */
    private int anzahl = 0;

    /**
     * Konstruktor.
     */
    public ObjektWartendeAnfragen() {
    }

    /**
     * gibt die Anzahl der wartenden Anfragen zur�ck.
     * 
     * @return int
     */
    public int gibAnzahl() {
        return anzahl;
    }

    /**
     * erh�ht die Anzahl der wartenden Anfragen um 1.
     */
    public void inkrementiere() {
        anzahl = anzahl + 1;
    }
    
    /**
     * setzt die Anzahl der wartenden Anfragen auf 0 zur�ck.
     */
    public void setzeNull() {
        anzahl = 0;
    }

}


