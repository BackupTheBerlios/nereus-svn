/*
 * Dateiname      : AbstractAgent.java
 * Erzeugt        : 13. Mai 2003
 * Letzte Änderung:
 * Autoren        : Daniel Friedrich
 *
 *
 *
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut für Intelligente Systeme
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
package nereus.simulatorinterfaces;

import java.io.Serializable;

import nereus.utils.ActionKey;
import nereus.utils.Id;
import nereus.simulatorinterfaces.AbstractScenarioHandler;
//import simulator.*;




/**
 * Abstrakte Superklasse, von der alle Agenten über eine beliebig tiefe
 * Veerbungshierarchie erben müssen.
 *
 * @author Daniel Friedrich
 */
public abstract class AbstractAgent
        extends Thread
        implements Serializable {
    
    /**
     * Szenariohandler.
     */
    protected AbstractScenarioHandler m_scenario;
    
    /**
     * Name des Agenten.
     */
    protected String m_name;
    
    /**
     * Id des Agenten
     */
    protected Id m_id;
    
    /**
     * Konstruktor
     */
    public AbstractAgent() {
        super();
        m_id = new Id();
    }
    
    /**
     * Konstruktor
     *
     * @param String name - Name des Agenten.
     */
    public AbstractAgent(String name) {
        super();
        m_name = name;
        m_id = new Id();
        
    }
    
    /**
     * Konstruktor
     *
     * @param name - Name des Agenten
     * @param handler - Szenariohandler
     */
    public AbstractAgent(String name, AbstractScenarioHandler handler) {
        super();
        m_name = name;
        m_id = new Id();
        m_scenario = handler;
        
    }
    
    /**
     * Konstruktor.
     *
     * @param id - Id des Agenten
     * @param name - Name des Agenten
     * @param handler - Szenariohandler
     */
    public AbstractAgent(Id id,String name, AbstractScenarioHandler handler) {
        super();
        m_name = name;
        m_id = id;
        m_scenario = handler;
        
    }
    
    /**
     * Konstruktor.
     *
     * @param id - Id des Agenten
     * @param name - Name des Agenten
     */
    public AbstractAgent(Id id, String name) {
        super();
        m_name = name;
        m_id = id;
    }
    
    /**
     * Liefert den Namen des Agenten.
     *
     * @return String - Name des Agenten
     */
    public String getAgentName() {
        return m_name;
    }
    
    /**
     * Liefert die Id des Agenten.
     *
     * @return Id - Id des Agenten
     */
    public Id getId() {
        return m_id;
    }
    
    /**
     * Setzt den Namen des Agenten.
     *
     * @param name - Name des Agenten
     */
    public void setAgentName(String name) {
        m_name = name;
    }
    
    /**
     * Setzt das Szenario in dem der Agent operiert.
     *
     * @param scenario - Szenario indem der Agent agiert.
     */
    public void setScenario(AbstractScenarioHandler scenario) {
        m_scenario = scenario;
    }
}


