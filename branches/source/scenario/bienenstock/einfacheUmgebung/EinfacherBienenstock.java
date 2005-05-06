/*
 * Dateiname      : EinfacherBienenstock.java
 * Erzeugt        : 6. Oktober 2004
 * Letzte Änderung: 14. Februar 2005 durch Philip Funck
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


package scenario.bienenstock.einfacheUmgebung;
import java.util.HashSet;
import scenario.bienenstock.agenteninfo.Koordinate;

/**
 * vereinfachte Darstellung eines Bienenstocks.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
public class EinfacherBienenstock extends EinfachesFeld {
    
    /**
     * Die ID des Bienenvolkes, dem der Stock gehört. 
     */
    private int volksID;
    
    /**
     * Konstruktor.
     * 
     * @param pos       die Position des Bienenstocks auf der Karte
     * @param wBienen   am Stock wartende Bienen
     * @param fBienen   am Stock fliegende Bienen
     * @param tBienen   am Stock tanzende Bienen
     * @param vID       ID des zugehörigen Bienenvolkes
     */
    public EinfacherBienenstock(Koordinate pos,
                                HashSet wBienen,
                                HashSet fBienen,
                                HashSet tBienen,
                                int vID) {
        super(pos,
              wBienen,
              fBienen,
              tBienen);
        /*position = pos;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;*/
        volksID = vID;
    }

    /**
     * gibt die ID des zugehörigen Bienenvolkes zurück.
     * 
     * @return int
     */
    public int gibVolksID() {
        return volksID;
    }

    
}
