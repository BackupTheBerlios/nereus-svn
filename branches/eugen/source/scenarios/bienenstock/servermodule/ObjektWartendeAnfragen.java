/*
 * Erzeugt        : 16. Oktober 2004
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
package scenario.bienenstock;

/**
 * dient als Warteschlange f�r die Anfragen der Agenten.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
class ObjektWartendeAnfragen {

    /**
     * Anzahl der wartenden Agentenanfragen.
     */
    private int anzahl = 0;

    /**
     * Konstruktor.
     */
    public ObjektWartendeAnfragen() {
    }

    /**
     * gibt die Anzahl der wartenden Anfragen zur�ck.
     * 
     * @return int
     */
    int gibAnzahl() {
        return anzahl;
    }

    /**
     * erh�ht die Anzahl der wartenden Anfragen um 1.
     */
    void inkrementiere() {
        anzahl = anzahl + 1;
    }
    
    /**
     * setzt die Anzahl der wartenden Anfragen auf 0 zur�ck.
     */
    void setzeNull() {
        anzahl = 0;
    }

}


