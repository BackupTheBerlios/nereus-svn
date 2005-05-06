/*
 * Erzeugt        : 6. Oktober 2004
 * Letzte �nderung: 14. Februar 2005 durch Philip Funck
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

package scenario.bienenstock.einfacheUmgebung;

import java.util.HashSet;
import scenario.bienenstock.Koordinate;

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
