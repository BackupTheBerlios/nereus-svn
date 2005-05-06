/*
 * Erzeugt        : 13. Dezember 2004
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

package scenario.bienenstock.umgebung;

/**
 * dient zur Verwaltung ausstehender Aktionen der Agenten.
 *
 * @author Philip Funck
 */
public class AusstehendeAktion {
    /**
     * ob die Aktion ausstehend ist
     */
    private boolean ausstehend;
    /**
     * id der zugehörigen Biene
     */
    private int id;

    /**
     * Konstruktor.
     *
     * @param ident Id der zugehörigen Biene
     */
    public AusstehendeAktion(int ident) {
        id = ident;
        ausstehend = true;
    }

    /**
     * gibt die ID der zugehörigen Biene zurück.
     *
     * @return int
     */
    public int gibID() {
        return id;
    }

    /**
     * gibt zurück, ob die Aktion noch aussteht.
     *
     * @return boolean
     */
    public boolean gibAusstehend() {
        return ausstehend;
    }

    /**
     * setzt es auf wahr, das die aktionnoch austeht.
     */
    public void setzeFalse() {
        ausstehend = false;
    }

    /**
     * setzt es auf falsch, das die aktion noch aussteht.
     *
     */
    public void setzeTrue() {
        ausstehend = true;
    }
}
