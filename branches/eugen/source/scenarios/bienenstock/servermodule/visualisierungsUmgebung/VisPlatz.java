/*
 * Erzeugt        : 6. Oktober 2004
 * Letzte �nderung: 26. Januar 2005 durch Philip Funck
 *
 * Teil des Softwarepraktikums mit dem Titel
 *
 *   "Design und Implementierung eines Szenarios f�r einen
 *    Multiagenten-Simulator"
 *
 * bei Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de)
 *
 * Autoren  : Philip Funck (mango.3@gmx.de)
 *            Samuel Walz (felix-kinkowski@gmx.net)
 *
 * copyright: Institut f�r Intelligente Systeme, Universit�t Stuttgart (2004)
 *            http://www.iis.uni-stuttgart.de
 */

package scenario.bienenstock.visualisierungsUmgebung;

import scenario.bienenstock.Koordinate;
import java.util.HashSet;

/**
 * repr�sentiert den Platz in der vereinfachten Darstellung
 * der Spielkarte f�r die Visualisierung.
 *
 * @author Philip Funck
 */
public class VisPlatz extends VisFeld {

    /**
     * Konstruktor.
     *
     * @param feldPosition  Position des Feldes
     * @param sichtBoden    Sichtweite am Boden
     * @param sichtLuft     Sichtweite in der Luft
     * @param wBienen       Kapazit�t wartende Bienen
     * @param fBienen       Kapazit�t fliegende Bienen
     * @param tBienen       Kapazit�t tanzende Bienen
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
