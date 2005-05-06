/*
 * Erzeugt        : 6. Oktober 2004
 * Letzte Änderung: 14. Februar 2005 durch Philip Funck
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
