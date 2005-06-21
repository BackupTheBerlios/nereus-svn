/*
 * Dateiname      : BienenstockSzenarioHandler.java
 * Erzeugt        : 5. Oktober 2004
 * Letzte �nderung: 8. Juni 2005 durch Eugen Volk
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *
 *
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut f�r
 * Intelligente Systeme der Universit�t Stuttgart unter Betreuung von
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
import nereus.utils.BooleanWrapper;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidActionKeyException;
import nereus.exceptions.InvalidElementException;
import scenarios.bienenstock.interfaces.IBienenstockSzenarioHandler;
import scenarios.bienenstock.agenteninfo.Koordinate;
import scenarios.bienenstock.Scenario;




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
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     *
     * @return                   neuer Aktionscode f�r den Agenten
     */
    public long aktionStarten(long aktCode, BooleanWrapper erfolg) {
        return spielmeister.startenLassen(aktCode, erfolg);
    }
    
    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     *
     * @param aktCode           Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     *
     * @param ziel              Das anzufliegende Feld
     * @return              neuer Aktionscode f�r den Agenten
     */
    public long aktionFliegen(long aktCode, BooleanWrapper erfolg,
            Koordinate ziel) {
        return spielmeister.fliegenLassen(aktCode, erfolg, ziel);
    }
    
    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     *
     * @param aktCode           Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     *
     * @return                  neuer Aktionscode f�r den Agenten
     */
    public long aktionLanden(long aktCode, BooleanWrapper erfolg) {
        return spielmeister.landenLassen(aktCode, erfolg);
    }
    
    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     *
     * @param aktCode           Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     *
     * @return                  neuer Aktionscode f�r den Agenten
     */
    public long aktionWarten(long aktCode, BooleanWrapper erfolg) {
        return spielmeister.wartenLassen(aktCode, erfolg);
    }
    
    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     *
     * @param aktCode          Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     * @param zielX                 X-Koordinate des Ziels
     * @param zielY                Y-Koordinate des Ziels
     * @param richtung          mitteilen der Richtung?
     * @param entfernung     mitteilen der Entfernung?
     * @return                  neuer Aktionscode f�r den Agenten
     */
    public long aktionTanzen(long aktCode, BooleanWrapper erfolg,
            int zielX,
            int zielY,
            boolean richtung,
            boolean entfernung) {
        return spielmeister.tanzenLassen(aktCode, erfolg,
                zielX,
                zielY,
                richtung,
                entfernung);
    }
    
    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     *
     * @param aktCode          Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     * @param zielX                 X-Koordinate des Ziels
     * @param zielY                Y-Koordinate des Ziels
     * @param richtung          mitteilen der Richtung?
     * @param entfernung     mitteilen der Entfernung?
     * @param nutzen         Nutzen der Blume
     * @return                  neuer Aktionscode f�r den Agenten
     */
    public long aktionTanzen(long aktCode, BooleanWrapper erfolg,
            int zielX,
            int zielY,
            boolean richtung,
            boolean entfernung,
            double nutzen) {
        return spielmeister.tanzenLassen(aktCode, erfolg,
                zielX,
                zielY,
                richtung,
                entfernung,
                nutzen);
    }
    
    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     *
     * @see Scenario.aktionZuschauen
     * @param aktCode           Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     * @param tanzendeBieneID   Die ID der Biene, der der Agent zuschauen m�chte
     * @return                  neuer Aktionscode f�r den Agenten
     */
    public long aktionZuschauen(long aktCode, BooleanWrapper erfolg,
            int tanzendeBieneID) {
        return spielmeister.zuschauenLassen(aktCode, erfolg,
                tanzendeBieneID);
    }
    
    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     *
     * @see Scenario.aktionHonigTanken
     * @param aktCode               Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     * @param menge                 Gew�nschte Honigmenge
     * @return long                  neuer Aktionscode f�r den Agenten
     */
    public long aktionHonigTanken(long aktCode, BooleanWrapper erfolg, int menge) {
        return spielmeister.honigTankenLassen(aktCode, erfolg, menge);
    }
    
    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     *
     * @param aktCode               Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     * @return long                  neuer Aktionscode f�r den Agenten
     */
    public long aktionNektarAbliefern(long aktCode, BooleanWrapper erfolg) {
        return spielmeister.nektarAbliefernLassen(aktCode, erfolg);
    }
    
    /**
     * reicht die Anfrage des Agenten an die gleichnamige Methode des
     * Szenarios weiter.
     *
     * @param aktCode                   Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     * @param menge                  Gew�nschte Nektarmenge
     * @return long                  neuer Aktionscode f�r den Agenten
     */
    public long aktionNektarAbbauen(long aktCode, BooleanWrapper erfolg, int menge) {
        return spielmeister.nektarAbbauenLassen(aktCode, erfolg, menge);
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
    
    /**
     * liefert zu einem Szenario-Parameter-Namen den zugeh�rigen
     * Wert als String zur�ck.
     *
     * @throws InvalidElementException  parameterName ist in der Parameter-Liste
     * nicht vorhanden.
     * @param parameterName der Name des Parameters
     * @return zu einem Szenario-Parameter-Namen den zugeh�rigen
     * Wert als String.
     */
    public String getScenarioParameter(String parameterName)
    throws InvalidElementException{
        String retValue;
        retValue=spielmeister.getScenarioParameter(parameterName);
        return retValue;
    }
    
    
    
    
}
