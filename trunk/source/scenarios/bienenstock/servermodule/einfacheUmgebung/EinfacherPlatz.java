/*
 * Erzeugt        : 6. Oktober 2004
 * Letzte Änderung: 26. Januar 2005 durch Philip Funck
 *
 * Teil des Softwarepraktikums mit dem Titel
 *
 *   "Design und Implementierung eines Szenarios für einen
 *    Multiagenten-Simulator"
 *
 * bei Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de)
 *
 * Autoren  : Philip Funck (mango.3@gmx.de)
 *            Samuel Walz (felix-kinkowski@gmx.net)
 *
 * copyright: Institut für Intelligente Systeme, Universität Stuttgart (2004)
 *            http://www.iis.uni-stuttgart.de
 */

package scenario.bienenstock.einfacheUmgebung;

import java.util.HashSet;
import scenario.bienenstock.Koordinate;

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
     */
    public EinfacherPlatz(Koordinate pos,
                         HashSet wBienen,
                         HashSet fBienen,
                         HashSet tBienen) {
        super(pos,
              wBienen,
              fBienen,
              tBienen);
        /*position = pos;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;*/
    }
}