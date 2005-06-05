/*
 * Dateiname      : IBienenstockSzenarioHandler.java
 * Erzeugt        : 6. Oktober 2004
 * Letzte Änderung: 23. Mai 2005 durch Eugen Volk
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
package scenarios.bienenstock.interfaces;

import nereus.exceptions.InvalidActionKeyException;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidElementException;
import scenarios.bienenstock.einfacheUmgebung.EinfacheKarte;
import nereus.utils.ActionKey;
import nereus.utils.Id;
import scenarios.bienenstock.agenteninfo.Koordinate;

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
    
    public Object getParameter(ActionKey key,
            Id agentId,
            String name) throws InvalidAgentException,
            InvalidActionKeyException,
            InvalidElementException;
    
    /**
     * liefert zu einem Szenario-Parameter-Namen den zugehörigen
     * Wert als String zurück.
     *
     * @throws InvalidElementException  parameterName ist in der Parameter-Liste
     * nicht vorhanden.
     * @param parameterName der Name des Parameters
     */
    public String getScenarioParameter(String parameterName)
    throws InvalidElementException;
    
}
