/*
 * Dateiname      : EinfacheBlume.java
 * Erzeugt        : 6. Oktober 2004
 * Letzte �nderung: 14. Februar 2005 durch Philip Funck
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

package scenario.bienenstock.einfacheUmgebung;

import java.util.HashSet;
import scenario.bienenstock.agenteninfo.Koordinate;

/**
 * dient zur vereinfachten Repr�sentation der Blume in der einfachen Umgebung.
 * 
 * @author Philip Funck
 */
public class EinfacheBlume extends EinfachesFeld {

    /**
     * kennzeichnet die Art der Blume.
     */
    private int merkmal;

    /**
     * Liste aller Bienen, die gerade an dieser Blume Nektar abbauen.
     * 
     * @associates java.lang.Integer
     */
    private HashSet abbauendeBienen;

    /**
     * Konstruktor.
     * 
     * @param pos       Position der Blume auf der Karte
     * @param wBienen   an dieser Blume wartende Bienen
     * @param fBienen   an dieser Blume fliegende Bienen
     * @param tBienen   an dieser Blume tanzende Bienen
     * @param merk      Merkmal der Blume
     * @param aBienen   an dieser Blume abbauende Bienen
     */
    public EinfacheBlume(Koordinate pos,
                         HashSet wBienen,
                         HashSet fBienen,
                         HashSet tBienen,
                         int merk,
                         HashSet aBienen) {
        super(pos,
              wBienen,
              fBienen,
              tBienen);
        /*position = pos;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;*/
        merkmal = merk;
        abbauendeBienen = aBienen;
    }

    /**
     * gibt das Merkmal der Blume zur�ck.
     * 
     * @return int
     */
    public int gibMerkmal() {
        return merkmal;
    }

    /**
     * gibt eine Liste der IDs aller abbauenden Bienen zur�ck.
     * 
     * @associates java.lang.Integer
     * @return     Eine Liste aller abbauenden Bienen
     */
    public HashSet gibIDsAbbauendeBienen() {
        return (HashSet) abbauendeBienen.clone();
    }


}
