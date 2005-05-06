/*
 * Erzeugt        : 18. Oktober 2004
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
 * enth�lt alle Konstanten des Szenarios.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Konstanten {

    /**
     * Die Konstante f�r den Zustand "Fliegend".
     */
    public static final int FLIEGEND = 0;
    
    /**
     * Die Konstante f�r den Zustand "Wartend".
     */
    public static final int WARTEND = 1;
    
    /**
     * Die Konstante f�r den Zustand "Tanzend".
     */
    public static final int TANZEND = 2;
    
    /**
     * Die Konstante f�r den Zustand "Zuschauend".
     */
    public static final int ZUSCHAUEND = 3;
    
    /**
     * Die Konstante f�r den Zustand "Abbauend".
     */
    public static final int ABBAUEND = 4;

    /**
     * Die Konstante f�r die Startphase.
     */
    public static final int STARTPHASE = 0;
    
    /**
     * Die Konstante f�r die Wartephase.
     */
    public static final int WARTEPHASE = 1;
    
    /**
     * Die Konstante f�r die Bearbeitungsphase.
     */
    public static final int BEARBEITUNGSPHASE = 2;
    
    /**
     * Die Konstante f�r die Endphase.
     */
    public static final int ENDPHASE = 3;

    /**
     * Der Konstruktor.
     */
    public Konstanten() {
    }
}
