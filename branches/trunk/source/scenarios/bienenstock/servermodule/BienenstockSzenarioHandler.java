/*
 * Erzeugt        : 5. Oktober 2004
 * Letzte �nderung: 25. Januar 2005 durch Philip Funck
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

import scenario.bienenstock.einfacheUmgebung.EinfacheKarte;
import simulator.AbstractScenarioHandler;
import utils.ActionKey;
import utils.Id;
import exceptions.InvalidAgentException;
import exceptions.InvalidActionKeyException;
import exceptions.InvalidElementException;

/**
 * ist der Handler des Szenarios f�r den Agenten.
 * 
 * Sie stellt der Biene die Aktionen, die sie ausf�hren kann zur Verf�gung.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
public class BienenstockSzenarioHandler extends AbstractScenarioHandler 
                                        implements IBienenstockSzenarioHandler{
    /**
     * Das zugeh�rige Szenario
     */
    private Scenario spielmeister;

    /**
     * setzt das Szenario f�r den Handler.
     * 
     * @param meister    das zugeh�rige Szenario
     */
    public BienenstockSzenarioHandler(Scenario meister) {
        spielmeister = meister;
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode            Aktionscode des Agenten
     * @return                   Der f�r den Agenten sichtbare Ausschnitt
     *                           der Spielkarte
     */
    public EinfacheKarte infoAusschnittHolen(long aktCode) {
        return spielmeister.infoAusschnittHolen(aktCode);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode           Aktionscode des Agenten
     * @return                   neuer Aktionscode f�r den Agenten
     */
    public long aktionStarten(long aktCode) {
        return spielmeister.startenLassen(aktCode);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode           Aktionscode des Agenten
     * @param ziel              Das anzufliegende Feld
     * @return              neuer Aktionscode f�r den Agenten
     */
    public long aktionFliegen(long aktCode,
                              Koordinate ziel) {
        return spielmeister.fliegenLassen(aktCode, ziel);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode           Aktionscode des Agenten
     * @return                  neuer Aktionscode f�r den Agenten
     */
    public long aktionLanden(long aktCode) {
        return spielmeister.landenLassen(aktCode);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode           Aktionscode des Agenten
     * @return                  neuer Aktionscode f�r den Agenten
     */
    public long aktionWarten(long aktCode) {
        return spielmeister.wartenLassen(aktCode);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode          Aktionscode des Agenten
     * @param zielX                 X-Koordinate des Ziels
     * @param zielY                Y-Koordinate des Ziels
     * @param richtung          mitteilen der Richtung?
     * @param entfernung     mitteilen der Entfernung?
     * @return                  neuer Aktionscode f�r den Agenten
     */
    public long aktionTanzen(long aktCode,
                             int zielX,
                             int zielY,
                             boolean richtung,
                             boolean entfernung) {
        return spielmeister.tanzenLassen(aktCode,
                                         zielX,
                                         zielY,
                                         richtung,
                                         entfernung);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @see Scenario.aktionZuschauen
     * @param aktCode           Aktionscode des Agenten           
     * @param tanzendeBieneID   Die ID der Biene, der der Agent zuschauen m�chte
     * @return                  neuer Aktionscode f�r den Agenten
     */
    public long aktionZuschauen(long aktCode,
                                int tanzendeBieneID) {
        return spielmeister.zuschauenLassen(aktCode,
                                            tanzendeBieneID);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @see Scenario.aktionHonigTanken
     * @param aktCode               Aktionscode des Agenten
     * @param menge                 Gew�nschte Honigmenge
     * @return long                  neuer Aktionscode f�r den Agenten
     */
    public long aktionHonigTanken(long aktCode, int menge) {
        return spielmeister.honigTankenLassen(aktCode, menge);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode               Aktionscode des Agenten
     * @return long                  neuer Aktionscode f�r den Agenten
     */
    public long aktionNektarAbliefern(long aktCode) {
        return spielmeister.nektarAbliefernLassen(aktCode);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode                   Aktionscode des Agenten
     * @param menge                  Gew�nschte Nektarmenge
     * @return long                  neuer Aktionscode f�r den Agenten
     */
    public long aktionNektarAbbauen(long aktCode, int menge) {
        return spielmeister.nektarAbbauenLassen(aktCode, menge);
    }

    /**
     * Soll dem Agenten die Parameter des Szenarios �bergeben.
     * 
     * noch nicht in Betrieb.
     * 
     * @throws InvalidAgentException        ung�ltiger Agent
     * @throws InvalidActionKeyException    ung�ltiger Aktionscode
     * @throws InvalidElementException      ung�ltiges Objekt              
     * @param key           Aktionscode des Agenten
     * @param agentId       ID des Agenten
     * @param name          Name des Agenten
     * @return  gibt null zur�ck
     */
    public Object getParameter(ActionKey key,
                                Id agentId, 
                                String name) 
                        throws InvalidAgentException,
                                InvalidActionKeyException,
                                InvalidElementException {
        return null;
    }
}
