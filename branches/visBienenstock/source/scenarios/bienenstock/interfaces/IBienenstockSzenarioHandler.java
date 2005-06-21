/*
 * Dateiname      : IBienenstockSzenarioHandler.java
 * Erzeugt        : 6. Oktober 2004
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
package scenarios.bienenstock.interfaces;

import nereus.exceptions.InvalidActionKeyException;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidElementException;
import scenarios.bienenstock.einfacheUmgebung.EinfacheKarte;
import nereus.utils.ActionKey;
import nereus.utils.Id;
import nereus.utils.BooleanWrapper;
import scenarios.bienenstock.agenteninfo.Koordinate;

/**
 * ist das Interface f�r den Handler des Szenarios.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public interface IBienenstockSzenarioHandler {
    
    /**
     * gibt dem Agenten den Teil der Spielkarte zur�ck, den er
     * wahrnehmen kann.
     *
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der sichtbare Ausschnitt der Spielkarte
     *                      f�r den anfragenden Agenten
     */
    EinfacheKarte infoAusschnittHolen(long aktCode);
    
    /**
     * l�sst die Biene von Boden starten.
     * @param aktCode       Der Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      fals die beabsichtigte Aktion ausgef�hrt wurde.
     * @return              Der neue Aktionscode
     */
    long aktionStarten(long aktCode, BooleanWrapper erfolg);
    
    /**
     * l�sst die Biene zum benachbarten Feld fliegen.
     * @param aktCode       Der Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      falls die beabsichtigte Aktion ausgef�hrt wurde.
     * @param ziel          Das gew�nschte Flugziel
     * @return              Der neue Aktionscode
     */
    long aktionFliegen(long aktCode, BooleanWrapper erfolg,
            Koordinate ziel);
    /**
     * l�sst die Biene landen.
     * @param aktCode       Der Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      falls die beabsichtigte Aktion ausgef�hrt wurde.
     * @return              Der neue Aktionscode
     */
    long aktionLanden(long aktCode, BooleanWrapper erfolg);
    
    /**
     * l�sst die Biene warten.
     * @param aktCode       Der Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      falls die beabsichtigte Aktion ausgef�hrt wurde.
     * @return              Der neue Aktionscode
     */
    long aktionWarten(long aktCode, BooleanWrapper erfolg);
    
    /**
     * l�sst die Biene tanzen, Dadurch teilt die Biene Information den
     * anderen Bienen mit.
     * @param aktCode       Der Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      falls die beabsichtigte Aktion ausgef�hrt wurde.
     * @param zielX         Die mitzuteilende X-Koordinate
     * @param zielY         Die Mitzuteilende Y-Koordinate
     * @param richtung      Gibt an, ob die Richtung mitzuteilen ist
     * @param entfernung    Gibt an, ob die Entfernung mitzuteilen ist
     * @return              Der neue Aktionscode
     */
    long aktionTanzen(long aktCode,
            BooleanWrapper erfolg,
            int zielX,
            int zielY,
            boolean richtung,
            boolean entfernung);
    
    /**
     * l�sst die Biene tanzen, Dadurch teilt die Biene Information den
     * anderen Bienen mit.
     * @param aktCode       Der Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      falls die beabsichtigte Aktion ausgef�hrt wurde.
     * @param zielX         Die mitzuteilende X-Koordinate
     * @param zielY         Die Mitzuteilende Y-Koordinate
     * @param richtung      Gibt an, ob die Richtung mitzuteilen ist
     * @param entfernung    Gibt an, ob die Entfernung mitzuteilen ist
     * @param nutzen        Nutzen der Blume
     * @return              Der neue Aktionscode
     */
    long aktionTanzen(long aktCode,
            BooleanWrapper erfolg,
            int zielX,
            int zielY,
            boolean richtung,
            boolean entfernung,
            double nutzen);
    
    /**
     * l�sst die Biene beim einer anderen Biene beim Tanzen zuschauen.
     * @param aktCode       Der Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      falls die beabsichtigte Aktion ausgef�hrt wurde.
     * @param tanzendeBieneID   Die ID der Biene, der der Agent zuschauen m�chte
     * @return              Der neue Aktionscode
     */
    long aktionZuschauen(long aktCode, BooleanWrapper erfolg,
            int tanzendeBieneID);
    
    /**
     * l�sst die Biene Honig tanken.
     * @param aktCode       Der Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      falls die beabsichtigte Aktion ausgef�hrt wurde.
     * @param menge         Die gew�nschte Honigmenge
     * @return              Der neue Aktionscode
     */
    long aktionHonigTanken(long aktCode, BooleanWrapper erfolg,
            int menge);
    
    /**
     * l�sst die Biene ihren gesammelten Nektar abliefern.
     * @param aktCode       Der Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      falls die beabsichtigte Aktion ausgef�hrt wurde.
     * @return              Der neue Aktionscode
     */
    long aktionNektarAbliefern(long aktCode, BooleanWrapper erfolg);
    
    /**
     * l�sst die Biene von der Blume Nektar abbauen.
     * @param aktCode       Der Aktionscode des Agenten
     * @param erfolg        Platzhalter, der nach der Ausf�hrung true enth�lt,
     *                      falls die beabsichtigte Aktion ausgef�hrt wurde.
     * @param menge         Die gew�nschte Nektarmenge
     * @return              Der neue Aktionscode
     */
    long aktionNektarAbbauen(long aktCode, BooleanWrapper erfolg,
            int menge);
    
    /**
     * Soll dem Agenten die Parameter des Szenarios �bergeben.
     *
     *
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
            String name) throws InvalidAgentException,
            InvalidActionKeyException,
            InvalidElementException;
    
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
    throws InvalidElementException;
    
}
