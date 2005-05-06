/*
 * Erzeugt        : 5. Oktober 2004
 * Letzte Änderung: 18. Januar 2005 durch Philip Funck
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
import simulator.AbstractScenarioHandler;
import utils.ActionKey;
import utils.Id;
import exceptions.InvalidAgentException;
import exceptions.InvalidActionKeyException;
import exceptions.InvalidElementException;

/**
 * des Szenarios für den Agenten.
 * 
 * Sie stellt der Biene die Aktionen, die sie ausführen kann zur Verfügung. 
 */
public class BienenstockSzenarioHandler extends AbstractScenarioHandler {
    Scenario spielmeister;

    public BienenstockSzenarioHandler(Scenario meister) {
        spielmeister = meister;
    }

    public EinfacheKarte infoAusschnittHolen(long aktCode) {
        return spielmeister.infoAusschnittHolen(aktCode);
    }

    public long aktionStarten(long aktCode) {
        return spielmeister.startenLassen(aktCode);
    }

    public long aktionFliegen(long aktCode,
                              Koordinate ziel) {
        return spielmeister.fliegenLassen(aktCode, ziel);
    }

    public long aktionLanden(long aktCode) {
        return spielmeister.landenLassen(aktCode);
    }

    public long aktionWarten(long aktCode) {
        return spielmeister.wartenLassen(aktCode);
    }

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

    public long aktionZuschauen(long aktCode,
                                int tanzendeBieneID) {
        return spielmeister.zuschauenLassen(aktCode,
                                            tanzendeBieneID);
    }

    public long aktionHonigTanken(long aktCode, int menge) {
        return spielmeister.honigTankenLassen(aktCode, menge);
    }

    public long aktionNektarAbliefern(long aktCode) {
        return spielmeister.nektarAbliefernLassen(aktCode);
    }

    public long aktionNektarAbbauen(long aktCode, int menge) {
        return spielmeister.nektarAbbauenLassen(aktCode, menge);
    }

    public Object getParameter(
	    ActionKey key, 
	    Id agentId, 
	    String name) 
	    throws InvalidAgentException,
	        InvalidActionKeyException,
	        InvalidElementException {
        return null;
    }
}
