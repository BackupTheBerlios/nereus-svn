/*
 * Erzeugt        : 20. Januar 2005
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

package scenario.bienenstock.statistik;

import scenario.bienenstock.Koordinate;
import utils.Id;

/**
 * speichert die Werte der Biene zu jeder Runde.
 *
 * Z�hlt bei jedem Eintragen von Werten der Biene intern die Rundennummer hoch.
 * @author Philip Funck
 * @author Samuel Walz
 */
public class StatistikBiene {

    /**
     * ID der zugeh�rigen Biene.
     */
    private Id bienenID;

    /**
     * Menge des geladenen Honigs.
     */
    private int honig;

    /**
     * Menge des geladenen Nektars.
     */
    private int nektar;

    /**
     * Nummer der aktuellen Runde.
     */
    private int runde;

    /**
     * Zustand der zugeh�rigen Biene.
     */
    private String zustand;

    /**
     * Der Konstruktor.
     *
     * @param id        ID der Biene
     * @param sHonig    geladener Honig der Biene
     * @param sNektar   geladener Nektar der Biene
     * @param sZustand  Zustand der Biene
     */
    public StatistikBiene(Id id, int sHonig, int sNektar, String sZustand) {
        honig = sHonig;
        nektar = sNektar;
        bienenID = id;
        zustand = sZustand;
    }

    /**
     * Konstruktor.
     *
     * @param id  Die ID der Biene
     */
    public StatistikBiene(Id id) {
        bienenID = id;
        honig = 0;
        nektar = 0;
        zustand = "";
    }

    /**
     * gibt die Menge des geladenen Honigs zur�ck.
     *
     * @return   eine positive ganze Zahl
     */
    public int gibHonig() {
        return honig;
    }

    /**
     * gibt die Menge des geladenen Nektars zur�ck.
     *
     * @return    eine positive ganze Zahl
     */
    public int gibNektar() {
        return nektar;
    }

    /**
     * gibt die Position der Biene auf der Karte zur�ck.
     *
     * @return  die x-und die y- Koordinate der Position
     */
    public Koordinate gibPosition() {
        return null;
    }

    /**
     * gibt den Zustand der Biene zur�ck.
     *
     * @return  Der Zustand der Biene als Wort
     */
    public String gibZustand() {
        return zustand;
    }

    /**
     * gibt die ID der zugeh�rigen Biene zur�ck.
     *
     * @return   eine positive ganze Zahl
     */
    public Id gibID() {
        return bienenID;
    }

    /**
     * enth�lt die Werte der zugeh�rigen Biene.
     *
     * @author Philip Funck
     */
    public class WerteBiene {
        /**
         * menge an geladenen Honig
         */
        private int geladenerHonig;
        /**
         * Menge an geladene Nektar
         */
        private int geladenerNektar;
        /**
         * Position der Biene
         */
        private int position;
        /**
         * Status der Biene
         */
        private int status;
    }
}
