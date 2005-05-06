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
import scenario.bienenstock.Koordinate;
import java.util.HashSet;
import java.util.HashMap;

/**
 * vereinfachte Darstellung des Feldes.
 * 
 * @author Philip Funck
 */
public class EinfachesFeld {
    
    /**
     * die Position des Feldes auf der Karte.
     */
    private Koordinate position;

    /**
     * die direkt angrenzenden Nachbarfelder des Feldes.
     * 
     * @associates scenario.bienenstock.einfacheUmgebung.EinfachesFeld 
     */
    private HashMap nachbarfelder;

    /**
     * Liste aller auf dem Feld wartenden Bienen.
     * 
     * @associates java.lang.Integer
     */
    private HashSet wartendeBienen;

    /**
     * Liste aller über dem Feld fliegenden Bienen.
     * 
     * @associates java.lang.Integer
     */
    private HashSet fliegendeBienen;

    /**
     * Liste aller auf dem Feld tanzenden Bienen.
     * 
     * @associates java.lang.Integer
     */
    private HashSet tanzendeBienen;

    /**
     * Konstrukotor.
     * 
     * @param pos       Position des Feldes auf der Karte
     * @param wBienen   am Feld wartende Bienen
     * @param fBienen   über dem Feld fliegende Bienen
     * @param tBienen   am Feld tanzende Bienen
     */
    public EinfachesFeld(Koordinate pos,
                         HashSet wBienen,
                         HashSet fBienen,
                         HashSet tBienen) {
        position = pos;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;
    }

    /**
     * setzt die angrenzenden Nachbarfelder des Feldes.
     * 
     * @param nachbarn  Liste aller angrenzenden Nachbarfelder
     */
    public void setzeNachbarfelder(HashMap nachbarn) {
        nachbarfelder = nachbarn;
    }

    /**
     * gibt die Position des Feldes auf der Karte zurück.
     * 
     * @return Koordinate
     */
    public Koordinate gibPosition() {
        return position;
    }

    /**
     * gibt eine Liste der direkt an das Feld angrenzenden Nachbarfelder zurück.
     * 
     * @return HashMap
     */
    public HashMap gibNachbarfelder () {
        return nachbarfelder;
    }

    /**
     * gibt eine Liste der IDs aller auf dem Feld wartenden Bienen zurück.
     * 
     * @associates java.lang.Integer
     * @return HashSet
     */
    public HashSet gibIDsWartendeBienen() {
        return (HashSet) wartendeBienen.clone();
    }

    /**
     * gibt eine Liste der IDs aller über dem Feld fliegenden Bienen zurück.
     * 
     * @associates java.lang.Integer
     * @return HashSet
     */
    public HashSet gibIDsFliegendeBienen() {
        return (HashSet) fliegendeBienen.clone();
    }

    /**
     * gibt eine Liste der IDs aller auf dem Feld tanzenden Bienen zurück.
     * 
     * @associates java.lang.Integer
     * @return HashSet
     */
    public HashSet gibIDsTanzendeBienen() {
        return (HashSet) tanzendeBienen.clone();
    }
}
