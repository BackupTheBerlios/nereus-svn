/*
 * Erzeugt        : 6. Oktober 2004
 * Letzte Änderung: 14. Februar 2005 durch Samuel Walz
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

import scenario.bienenstock.einfacheUmgebung.EinfacheKarte;
/**
 * ist das Interface für den Handler des Szenarios.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
public interface IBienenstockSzenarioHandler {
    
    /**
     * gibt dem Agenten den Teil der Spielkarte zurück, den er
     * wahrnehmen kann.
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der sichtbare Ausschnitt der Spielkarte
     *                      für den anfragenden Agenten
     */
    EinfacheKarte infoAusschnittHolen(long aktCode);

    /**
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode
     */
    long aktionStarten(long aktCode);

    /**
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @param ziel          Das gewünschte Flugziel
     * @return              Der neue Aktionscode
     */
    long aktionFliegen(long aktCode,
                              Koordinate ziel);
    /**
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode
     */
    long aktionLanden(long aktCode);

    /**
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode
     */
    long aktionWarten(long aktCode);

    /**
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @param zielX         Die mitzuteilende X-Koordinate
     * @param zielY         Die Mitzuteilende Y-Koordinate
     * @param richtung      Gibt an, ob die Richtung mitzuteilen ist
     * @param entfernung    Gibt an, ob die Entfernung mitzuteilen ist
     * @return              Der neue Aktionscode
     */
    long aktionTanzen(long aktCode,
                             int zielX,
                             int zielY,
                             boolean richtung, 
                             boolean entfernung);

    /**
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @param tanzendeBieneID   Die ID der Biene, der der Agent zuschauen möchte
     * @return              Der neue Aktionscode
     */
    long aktionZuschauen(long aktCode,
                                int tanzendeBieneID);

    /**
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @param menge         Die gewünschte Honigmenge
     * @return              Der neue Aktionscode
     */
    long aktionHonigTanken(long aktCode,
                                  int menge);

    /**
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode
     */
    long aktionNektarAbliefern(long aktCode);

    /**
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @param menge         Die gewünschte Nektarmenge
     * @return              Der neue Aktionscode
     */
    long aktionNektarAbbauen(long aktCode,
                                    int menge);
}
