/*
 * Dateiname      : EinfacherPlatz.java
 * Erzeugt        : 6. Oktober 2004
 * Letzte Änderung: 21. Juni 2005 durch Eugen Volk
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


package scenarios.bienenstock.einfacheUmgebung;

import java.util.HashSet;
import scenarios.bienenstock.agenteninfo.Koordinate;

/**
 * vereinfachte Darstellung des Platzes.
 *
 * @author Philip Funck
 */
public class EinfacherPlatz extends EinfachesFeld {
    
    /**
     * Konstruktor.
     *
     * @param pos       Position des Platzes auf der Karte
     * @param wBienen   am Platz wartende Bienen
     * @param fBienen   am Platz fliegende Bienen
     * @param tBienen   am Platz tanzende Bienen
     * @param sBienen   sonstige Bienen
     */
    public EinfacherPlatz(Koordinate pos,
            HashSet wBienen,
            HashSet fBienen,
            HashSet tBienen,
            HashSet sBienen) {
        super(pos,
                wBienen,
                fBienen,
                tBienen,
                sBienen);
        /*position = pos;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;*/
    }
}