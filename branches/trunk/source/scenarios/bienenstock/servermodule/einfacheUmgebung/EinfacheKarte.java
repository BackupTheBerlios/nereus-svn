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
import java.util.Hashtable;

/**
 * eine vereinfachte Darstellung der Spielkarte f�r den Agenten.
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
     * der f�r den Agenten sichtbare Ausschnitt der Spielkarte.
     * 
     * @associates EinfachesFeld
     */
    private Hashtable spielfeldAusschnitt;
    
    /**
     * Konstruktor.
     * 
     * @param selber    die vereinfachte Darstellung des Agenten
     * @param felder    die f�r den Agenten aktuell sichtbaren Felder der
     *                  Spielkarte
     */
    public EinfacheKarte(EinfacheBiene selber, Hashtable felder) {
        selbst = selber;
        spielfeldAusschnitt = felder;
    }

    /**
     * Gibt die vereinfachte Darstellung des Agenten zur�ck.
     * 
     * @return EinfacheBiene
     */
    public EinfacheBiene gibSelbst() {
        return selbst;
    }

    /**
     * gibt eine Liste aller f�r den Aganten sichtbaren Felder zur�ck.
     * 
     * @return Hashtable
     */
    public Hashtable gibFelder() {
        return spielfeldAusschnitt;
    }

}
