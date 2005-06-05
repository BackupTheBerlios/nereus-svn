/*
 * Dateiname      : AgentsList.java
 * Erzeugt        : 10. Mai 2005
 * Letzte Änderung: 10. Mai 2005 durch Eugen Volk
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
import java.util.*;

/**
 * Eine Datenstruktur für die Auswahl der verfügbaren Agenten zu
 * einem bestimmten Szenario.
 * @author  Eugen Volk
 */
public class AgentsList {
    
    
    /**
     * Erzeugt eine Datenstruktur in der
     * Szenario-Name und eine Liste der verfügbaren Agenten gespeichert sind.
     *
     * @param name Name des Scenario
     */
    public AgentsList(String scenarioName) {
        this.scenarioName=scenarioName;
        this.agentsList=new LinkedList();
    }
    /** Szenario-Name */
    private String scenarioName;
    
    /**  Eine Liste der Verfügbaren Agenten */
    private LinkedList agentsList;
    
    /**
     * Fügt den Namen eines Agenten der Liste hinzu.
     * @param agentName Name des Agenten
     */
    public void addAgentName(String agentName){
        agentsList.add(new String(agentName));
    }
    
    /**
     * Liefert eine Liste der vefügbaren Agenten.
     *
     * @return eine Verketete Liste von AgentenNamen.
     */
    public LinkedList getAgents(){
        return this.agentsList;
        
    }
    
    /**
     * liefert den ScenarioNamen
     * @return ScenarioNamen
     */
    public String getScenarioName(){
        return this.scenarioName;
    }
}
