/*
 * Dateiname      : VisPlatz.java
 * Erzeugt        : 6. Oktober 2004
 * Letzte Änderung: 08. Juni 2005 durch Samuel Walz
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (samuel@gmx.info)
 *
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

package scenarios.bienenstock.visualisierungsUmgebung;

import scenarios.bienenstock.agenteninfo.Koordinate;

import java.util.HashSet;
import java.io.Serializable;

/**
 * repräsentiert den Platz in der vereinfachten Darstellung
 * der Spielkarte für die Visualisierung.
 *
 * @author Philip Funck
 */
public class VisPlatz extends VisFeld implements Serializable {
    
    /**
     * Konstruktor.
     *
     * @param feldPosition  Position des Feldes
     * @param sichtBoden    Sichtweite am Boden
     * @param sichtLuft     Sichtweite in der Luft
     * @param wBienen       Kapazität wartende Bienen
     * @param fBienen       Kapazität fliegende Bienen
     * @param tBienen       Kapazität tanzende Bienen
     */
    public VisPlatz(Koordinate feldPosition,
            int sichtBoden,
            int sichtLuft,
            HashSet wBienen,
            HashSet fBienen,
            HashSet tBienen
            ) {
        super(feldPosition,
                sichtBoden,
                sichtLuft,
                wBienen,
                fBienen,
                tBienen);
        /*position = feldPosition;
        sichtweiteAmBoden = sichtBoden;
        sichtweiteInDerLuft = sichtLuft;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;*/
    }
    
}
