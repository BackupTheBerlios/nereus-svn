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

package bienenstockVisualisierung.visualisierungsUmgebung;
import java.util.HashSet;
import java.util.Hashtable;
import bienenstockVisualisierung.Koordinate;

/**
 * repräsentiert das allgemeine Feld.
 *
 *  @author Philip Funck
 */
public class VisFeld {

    /**
     * Position des Feldes auf der Spielkarte.
     */
    private Koordinate position;

    /**
     * ist eine Liste de Angrenzenden Felder.
     * @associates VisFeld
     */
    private Hashtable nachbarfelder;

    /**
     * Sichtweite der Biene am Boden des Feldes.
     */
    private int sichtweiteAmBoden;

    /**
     * Sichtweite der Biene in der Luft des Feldes.
     */
    private int sichtweiteInDerLuft;

    /**
     * ist eien Liste der auf dem Feld Wartenden oder zuhörenden Bienen.
     * @associates VisBiene
     */
    private HashSet wartendeBienen;

    /**
     * ist eine Liste der über dem Feld fliegenden Bienen.
     * @associates VisBiene
     */
    private HashSet fliegendeBienen;

    /**
     * ist eine Liste der auf dem Feld tanzenden Bienen.
     * @associates VisBiene
     */
    private HashSet tanzendeBienen;

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
    public VisFeld(Koordinate feldPosition,
            int sichtBoden,
            int sichtLuft,
            HashSet wBienen,
             HashSet fBienen,
            HashSet tBienen) {
        sichtweiteAmBoden = sichtBoden;
        sichtweiteInDerLuft = sichtLuft;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;
        position = feldPosition;
    }

    /**
     * gibt die Position des Feldes auf der Spielkarte zurück.
     *
     * @return Position des Feldes
     */
    Koordinate gibPosition() {
        return position;
    }

    /**
     * gibt die direkt angrenzenden Nachbarfelder des Feldes zurück.
     *
     * @return Nachbarfelder
     */
    Hashtable gibNachbarfelder() {
        return (Hashtable) nachbarfelder.clone();
    }

    /**
     * gibt die Sichtweite der Biene am Boden des Feldes zurück.
     *
     * @return Sichtweite am Boden
     */
    int gibSichtweiteAmBoden() {
        return sichtweiteAmBoden;
    }

    /**
     * gibt die Sichtweite der Biene in der Luft des Feldes zurück.
     *
     * @return Sichtweite in der Luft
     */
    int gibSichtweiteInDerLuft() {
        return sichtweiteInDerLuft;
    }

    /**
     * gibt eine Liste der dort watenden Bienen zurück.
     * 
     * @return wartende Bienen
     */
    HashSet gibWartendeBienen() {
        return (HashSet) wartendeBienen.clone();
    }

    /**
     * gibt eine Liste der dort fliegenden Bienen zurück.
     * 
     * @return fliegende Bienen
     */
    HashSet gibFliegendeBienen() {
        return (HashSet) fliegendeBienen.clone();
    }

    /**
     * gibt eine Liste der dort tanzenden Bienen zurück.
     * 
     * @return tanzende Bienen
     */
    HashSet gibTanzendeBienen() {
        return (HashSet) tanzendeBienen.clone();
    }

    /**
     * setzt dem Feld seine direkt angrenzenden Nachbarfelder.
     *
     * @param nachbarn die Nachbarfelder
     */
    void setzeNachbarfelder(Hashtable nachbarn) {
        nachbarfelder = nachbarn;
    }
}
