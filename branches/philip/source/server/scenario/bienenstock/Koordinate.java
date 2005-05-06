/*
 * Erzeugt        : 21. Oktober 2004
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
 * ist der Datentyp mit dem eine Position auf der Spielkarte angegeben wird.
 * 
 * @author Philip Funck
 * @author Samuel Walz 
 */
public class Koordinate {
    /**
     * die X-Koordinate.
     */
    private int xPosition;
    
    /**
     * die Y-Koordinate.
     */
    private int yPosition;

    /**
     * Der Konstruktor.
     * 
     * @param x     Die X-Koordinate
     * @param y     Die Y-Koordinate
     */
    public Koordinate(int x, int y) {
        xPosition = x;
        yPosition = y;
    }

    /**
     * gibt die X-Koordinate zur�ck.
     * 
     * @return int
     */
    public int gibXPosition() {
        return xPosition;
    }

    /**
     * gibt die Y-Koordinate zur�ck.
     * 
     * @return int
     */
    public int gibYPosition() {
        return yPosition;
    }

    /**
     * vergleicht ein anderes Objekt auf Gleichheit mit sich selbst.
     * 
     * Der Test verl�uft erfolgreich bei gleichem Objekttyp und
     * gleichen Koordinaten.
     * 
     * @param obj    das Objekt mi dem verglichen werden soll.
     * @return          true bei Gleichheit
     */
    public boolean equals(Object obj) {
        if (obj instanceof Koordinate) {
            return (xPosition == ((Koordinate) obj).gibXPosition())
                && (yPosition == ((Koordinate) obj).gibYPosition());
        } else {
            return false;
        }
    }
    
    /**
     * generiert einen Hashcode f�r das Objekt.
     * 
     * @return   X-Koordinate * 1000 + Y-Koordinate
     */
    public int hashCode() {
        return ((xPosition * 1000) + yPosition);
    }
}
