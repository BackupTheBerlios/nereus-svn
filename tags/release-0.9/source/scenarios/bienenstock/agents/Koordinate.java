/*
 * Erzeugt        : 21. Oktober 2004
 * Letzte Änderung: 22. Oktober 2004 durch Philip Funck
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

package scenario.bienenstock;

/**
 * ist der Datentyp mit dem eine Position auf der Spielkarte angegeben wird.
 * @author Philip Funck
 * @author Samuel Walz
 * @version 1.0
 * @since 20 juli 2004 
 */
public class Koordinate {
    private int xPosition;
    private int yPosition;

    public Koordinate(int x, int y) {
        xPosition = x;
        yPosition = y;
    }

    public int gibXPosition() {
        return xPosition;
    }

    public int gibYPosition() {
        return yPosition;
    }

    public boolean equals(Object obj) {
		if(obj instanceof Koordinate) {
            return (xPosition == ((Koordinate)obj).gibXPosition())
                && (yPosition ==((Koordinate)obj).gibYPosition());
        }
        else {
	        return false;
        }
    }
    public int hashCode() {
		return (xPosition*1000 + yPosition);
    }
}
