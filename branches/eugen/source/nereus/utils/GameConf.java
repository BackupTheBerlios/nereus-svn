/*
 * Dateiname      : GameConf.java
 * Erzeugt        : 11. Mai 2005
 * Letzte Änderung: 11. Mai 2005 durch Eugen Volk
 * Autoren        : Eugen Volk
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Eugen Volk am Institut für Intelligente Systeme
 * der Universität Stuttgart unter Betreuung von Dietmar Lippold
 * (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package nereus.utils;

import java.io.Serializable;

/**
 * Eine DatenStruktur zur Verwaltung der Eingelesenen Eintrags aus
 * der KonfigurationsDatei fuer den Simulator.
 * @author Eugen Volk
 */
public class GameConf implements Serializable {
    
    /** Name des Eintrags, unt dem es in der Konfiguration auftauchen soll */
    private String tagName;
    
    /** Name des Szenarios */
    private String scenarioName;
    
    /** Dateiname der Kare, die sich unter scenario/<scenario-Name>/karten/xxx.gml>
     * befindet. */
    private String kartenDateiName;
    
    /** Dateiname der Konfigurationsdatei, in der Parameter des Scenarios gespeichert sind,
     * zu finden in scenario/<scenario-Name>/parameters/xxxxx.xml */
    private String parameterDateiName;
    
    /** Creates a new instance of GameConf */
    public GameConf(String tagName, String scenarioName, String kartenDateiName,
            String parameterDateiName) {
        this.tagName=new String(tagName);
        this.scenarioName=new String(scenarioName);
        this.kartenDateiName=new String(kartenDateiName);
        this.parameterDateiName=new String(parameterDateiName);
    }
    
    
    
    /** liefert den Namen des Eintrags
     * @return Namen des Eintrags
     */
    public String getTagName(){
        return this.tagName;
    }
    
    /**
     * liefert den Scenario-Namen
     * @return Scenario-Namen
     */
    public String getScenarioName(){
        return this.scenarioName;
    }
    
    /**
     * liefet den DateiNamen der Parameter-Konfigurationsdatei
     * @return DateiNamen der Parameter-Konfigurationsdatei
     */
    public String getParameterDateiName(){
        return this.parameterDateiName;
    }
    
    /**
     * liefert den DateiNamen der verwendeten Scenario-Karte
     * @return DateiNamen der verwendeten Scenario-Karte
     */
    public String getKartenDateiName(){
        return this.kartenDateiName;
    }
    
}
