/*
 * Erzeugt        : 19. Mai 2004
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

import scenario.bienenstock.Koordinate;

/**
 * repräsentiert einen Platz.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Platz extends Feld {

    /**
     * Konstruktor.
     *
     * @param eigenID   die ID
     * @param sichtBoden    Sichtweite am Boden
     * @param sichtLuft     Sichtweite in der Luft
     * @param maxWartendeBienen maximum wartende Bienen
     * @param maxFliegendeBienen    maximum fliegende Bienen
     * @param maxTanzendeBienen     maximum tanzende Bienen
     * @param feldPosition  Koordinate des PLatzes
     */
    public Platz (
        int eigenID,
        int sichtBoden,
        int sichtLuft,
        int maxWartendeBienen,
        int maxFliegendeBienen,
        int maxTanzendeBienen,
        Koordinate feldPosition) {
        super(eigenID,
              sichtBoden,
              sichtLuft,
              maxWartendeBienen,
              maxFliegendeBienen,
              maxTanzendeBienen,
              feldPosition);
    }

}
