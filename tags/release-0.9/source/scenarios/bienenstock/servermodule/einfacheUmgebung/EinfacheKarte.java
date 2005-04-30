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
import java.util.Hashtable;

/**
 * eine vereinfachte Darstellung der Spielkarte für den Agenten.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
public class EinfacheKarte {
    
    /**
     * vereinfachte Darstellung des Agenten.
     */
    private EinfacheBiene selbst;

    /**
     * der für den Agenten sichtbare Ausschnitt der Spielkarte.
     * 
     * @associates EinfachesFeld
     */
    private Hashtable spielfeldAusschnitt;
    
    /**
     * Konstruktor.
     * 
     * @param selber    die vereinfachte Darstellung des Agenten
     * @param felder    die für den Agenten aktuell sichtbaren Felder der
     *                  Spielkarte
     */
    public EinfacheKarte(EinfacheBiene selber, Hashtable felder) {
        selbst = selber;
        spielfeldAusschnitt = felder;
    }

    /**
     * Gibt die vereinfachte Darstellung des Agenten zurück.
     * 
     * @return EinfacheBiene
     */
    public EinfacheBiene gibSelbst() {
        return selbst;
    }

    /**
     * gibt eine Liste aller für den Aganten sichtbaren Felder zurück.
     * 
     * @return Hashtable
     */
    public Hashtable gibFelder() {
        return spielfeldAusschnitt;
    }

}
