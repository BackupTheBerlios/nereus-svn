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

package scenario.bienenstock.visualisierungsUmgebung;

import scenario.bienenstock.Koordinate;
import java.util.HashSet;

/**
 * repräsentiert eine Blume.
 *
 * @author Philip Funck
 */
public class VisBlume extends VisFeld {

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
              tBienen);

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
