/*
 * Dateiname      : VisBlume.java
 * Erzeugt        : 6. Oktober 2004
 * Letzte Änderung: 15. Juni 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (samuel@gmx.info)
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


package scenarios.bienenstock.visualisierungsUmgebung;

import java.util.HashSet;
import java.io.Serializable;

import scenarios.bienenstock.agenteninfo.Koordinate;

/**
 * repräsentiert eine Blume.
 *
 * @author Philip Funck
 */
public class VisBlume extends VisFeld implements Serializable {

    /**
     * ist das äußere Merkmal der Blume.
     */
    private int merkmal;

    /**
     * ist die vorhandene Nektarmenge.
     */
    private int vorhandenerNektar;

    /**
     * ist das Maximum, das von einer Biene pro Runde bezogen werden kann.
     */
    private int maximumNektarProRunde;

    /**
     * ist ene Liste aller zur Zeit Nektar abbauenden Bienen.
     * @associates VisBiene
     */
    private HashSet abbauendeBienen;

    /**
     * Konstruktor.
     *
     * @param feldPosition  Position
     * @param sichtBoden    Sichtweite am Boden
     * @param sichtLuft     Sichtweite in der Luft
     * @param wBienen       wartende Bienen
     * @param fBienen       fliegende Bienen
     * @param tBienen       tanzende Bienen
     * @param blumenMerkmal Merkmal der Blume
     * @param vorhNektar    vorhandener Nektar
     * @param maxNektarProRunde Maximalmenge die abgegeben wird
     * @param aBienen   abbauende Bienen
     */
    public VisBlume(
            Koordinate feldPosition,
            int sichtBoden,
            int sichtLuft,
            HashSet wBienen,
            HashSet fBienen,
            HashSet tBienen,
            HashSet sBienen,
            int blumenMerkmal,
            int vorhNektar,
            int maxNektarProRunde,
            HashSet aBienen
            ) {
        super(feldPosition,
                sichtBoden,
                sichtLuft,
                wBienen,
                fBienen,
                tBienen, 
                sBienen);
        
        /*position = feldPosition;
        sichtweiteAmBoden = sichtBoden;
        sichtweiteInDerLuft = sichtLuft;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;*/
        
        merkmal = blumenMerkmal;
        vorhandenerNektar = vorhNektar;
        maximumNektarProRunde = maxNektarProRunde;
        abbauendeBienen = aBienen;
    }

    /**
     * gibt das Merkmal der Blume zurück.
     *
     * @return Merkmal der Blume
     */
    public int gibMerkmal() {
        return merkmal;
    }

    /**
     * gibt zurück, wieviel Nektar vorhanden ist.
     *
     * @return vorhandener Nektar
     */
    public int gibVorhandenerNektar() {
        return vorhandenerNektar;
    }

    /**
     * gibt zurück, wieviel Nektar pro Runde und Biene maximal
     * abgegeben werden kann.
     *
     * @return Maximalabgabemenge Nektar
     */
    public int gibMaxNektarProRunde() {
        return maximumNektarProRunde;
    }

    /**
     * gibt eine Liste aller an dieser Blume abbauenden Bienen zurück.
     *
     * @return abbauende Bienen
     */
    public HashSet gibAbbauendeBienen() {
        return (HashSet) abbauendeBienen.clone();
    }
}

