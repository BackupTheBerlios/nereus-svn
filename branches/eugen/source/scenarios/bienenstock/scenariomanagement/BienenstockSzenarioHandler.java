/*
 * Dateiname      : BienenstockSzenarioHandler.java
 * Erzeugt        : 5. Oktober 2004
 * Letzte Änderung: 25. Januar 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *                  
 *                  
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut für
 * Intelligente Systeme der Universität Stuttgart unter Betreuung von
 * Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package scenarios.bienenstock.scenariomanagement;

import scenarios.bienenstock.einfacheUmgebung.EinfacheKarte;
import nereus.simulatorinterfaces.AbstractScenarioHandler;
import nereus.utils.ActionKey;
import nereus.utils.Id;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidActionKeyException;
import nereus.exceptions.InvalidElementException;
import scenarios.bienenstock.interfaces.IBienenstockSzenarioHandler;
import scenarios.bienenstock.agenteninfo.Koordinate;
import scenarios.bienenstock.Scenario;




/**
 * ist der Handler des Szenarios für den Agenten.
 * 
 * Sie stellt der Biene die Aktionen, die sie ausführen kann zur Verfügung.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
public class BienenstockSzenarioHandler extends AbstractScenarioHandler 
                                        implements IBienenstockSzenarioHandler{
    /**
     * Das zugehörige Szenario
     */
    private Scenario spielmeister;

    /**
     * setzt das Szenario für den Handler.
     * 
     * @param meister    das zugehörige Szenario
     */
    public BienenstockSzenarioHandler(Scenario meister) {
        spielmeister = meister;
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode            Aktionscode des Agenten
     * @return                   Der für den Agenten sichtbare Ausschnitt
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
     * @return                   neuer Aktionscode für den Agenten
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
     * @return              neuer Aktionscode für den Agenten
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
     * @return                  neuer Aktionscode für den Agenten
     */
    public long aktionLanden(long aktCode) {
        return spielmeister.landenLassen(aktCode);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode           Aktionscode des Agenten
     * @return                  neuer Aktionscode für den Agenten
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
     * @return                  neuer Aktionscode für den Agenten
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
     * @param tanzendeBieneID   Die ID der Biene, der der Agent zuschauen möchte
     * @return                  neuer Aktionscode für den Agenten
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
     * @param menge                 Gewünschte Honigmenge
     * @return long                  neuer Aktionscode für den Agenten
     */
    public long aktionHonigTanken(long aktCode, int menge) {
        return spielmeister.honigTankenLassen(aktCode, menge);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode               Aktionscode des Agenten
     * @return long                  neuer Aktionscode für den Agenten
     */
    public long aktionNektarAbliefern(long aktCode) {
        return spielmeister.nektarAbliefernLassen(aktCode);
    }

    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     * 
     * @param aktCode                   Aktionscode des Agenten
     * @param menge                  Gewünschte Nektarmenge
     * @return long                  neuer Aktionscode für den Agenten
     */
    public long aktionNektarAbbauen(long aktCode, int menge) {
        return spielmeister.nektarAbbauenLassen(aktCode, menge);
    }

    /**
     * Soll dem Agenten die Parameter des Szenarios übergeben.
     * 
     * noch nicht in Betrieb.
     * 
     * @throws InvalidAgentException        ungültiger Agent
     * @throws InvalidActionKeyException    ungültiger Aktionscode
     * @throws InvalidElementException      ungültiges Objekt              
     * @param key           Aktionscode des Agenten
     * @param agentId       ID des Agenten
     * @param name          Name des Agenten
     * @return  gibt null zurück
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
