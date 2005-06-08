/*
 * Dateiname      : AbstractScenario.java
 * Erzeugt        : 13. Mai 2003
 * Letzte Änderung: 08. Juni 2005 durch Samuel Walz
 * Autoren        : Daniel Friedrich
 *                  Eugen Volk
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
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import nereus.communication.speachacts.Speachact;
import nereus.utils.Id;
import nereus.utils.GameConf;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidElementException;
import nereus.exceptions.InvalidGameException;
import nereus.exceptions.NotEnoughEnergyException;
import nereus.simulatorinterfaces.AbstractAgent;
import nereus.simulatorinterfaces.AbstractScenarioHandler;
import nereus.simulatorinterfaces.IVisualisationServerIntern;


/**
 * Das AbstractScenario muss vom Scenarioentwickler überschrieben werden. Es
 * dient dazu das konkrete Szenario mit Informationen zu versorgen und zu
 * starten, ohne dass das Spiel die genaue Scenario-Klasse kennen muss.
 *
 * @author Daniel Friedrich
 */
public abstract class AbstractScenario implements Serializable {
    
    /**
     * Informationhandler, managed den Informationsfluss an die Viskomponenten.
     */
    protected IInformationHandler m_visHandler;
    
    /**
     *  Spielparameter
     */
    protected Hashtable m_parameter;
    
    /**
     * Agenten des Scenarios
     */
    protected Hashtable m_agents = new Hashtable();
    
    /**
     *  Hashtable in der die aktuelle Energie jedes registrierten Agenten
     *  gespeichert ist.
     */
    protected Hashtable m_agentsEnergy = new Hashtable();
    
    /**
     * Id des Spiels zu dem das Szenario gehört.
     */
    protected Id m_gameId;
    
    /**
     * Szenariohandler des Szenarios.
     */
    protected AbstractScenarioHandler m_scenarioHandler = null;
    
    /**
     * Die Server-Vis-Komponente
     */
    protected IVisualisationServerIntern m_visualisationServer = null;
    
    /**
     * Konstruktor
     *
     * @param vishandler - InformationHandler
     * @param parameter - Spielparameter
     * @param visServer - Server-Vis-Komponente
     */
    public AbstractScenario(
            Id gameId,
            IInformationHandler visHandler,
            Hashtable parameter,
            IVisualisationServerIntern visServer){
        super();
        m_gameId = gameId;
        m_visHandler = visHandler;
        m_parameter = parameter;
        m_visualisationServer = visServer;
    }
    
    /**
     * Konstruktor.
     */
    public AbstractScenario(){
        super();
    }
    
    
    /**
     * Initialisiert die Werte m_gameId, visHandler und parameter.
     * Dient als ersatz des Parametrisierten Konstruktors.
     *
     * @parm gameId Id des Spiels
     * @param InformationHandler vishandler - InformationHandler
     * @param Hashtable parameter - Spielparameter
     * @param gameConf zu verwendende Konfugurationdatei mit Karen-Namen und ScenarioKonfidatei-Namen.
     * @param visServer zu verwendende Server-Vis-Komponente
     */
    public void initialize(
            Id gameId,
            IInformationHandler visHandler,
            Hashtable parameter,
            GameConf gameConf,
            IVisualisationServerIntern visServer){
        m_gameId = gameId;
        m_visHandler = visHandler;
        m_parameter = parameter;
        m_visualisationServer = visServer;
    }
    
    
    /**
     * Simuliert das komplette Spiel.
     *
     * Ob das Spiel rundenbasiert oder ohne Runden stattfindet, bleibt dem
     * Implementierer des konkreten Scenarios überlassen.
     */
    public abstract void simulateGame()
    throws InvalidAgentException,
            NotEnoughEnergyException,
            InvalidElementException;
    
    
    /**
     * Erzeugt einen neuen ScenarioHandler.
     *
     * Das konkrete Scenario muss hier seinen ScenarioHandler erzeugen. Der
     * ScenarioHandler wird aus Sicherheitsgründen implementiert. Er soll den
     * Agenten einen indirekten Zugriff auf das Scenario erlauben, ohne das die
     * Agenten das Scenario jemals in die Hand bekommen.
     *
     * @return AbstractScenarioHandler
     */
    public abstract AbstractScenarioHandler createNewScenarioHandler();
    
    /**
     * Fügt dem Scenario einen neuen Agenten hinzu.
     *
     * Dabei bekommt ein Agent auch gleich seinen Szenariohandler gesetzt.
     *
     * @param AbstractAgent agent - Agent der Hinzugefügt werden soll
     * @param int energy - Startenergie des Agenten
     */
    public void addAgent(AbstractAgent agent, double energy) throws InvalidAgentException {
        if(m_scenarioHandler == null) {
            this.createNewScenarioHandler();
        }
        
        if(!m_agents.containsKey(agent.getId().toString())) {
            // Setzen des Scenariohandlers
            agent.setScenario(m_scenarioHandler);
            // Hinzufügen zur Agentenliste
            m_agents.put(agent.getId().toString(), agent);
            // Agenten in die Energieliste aufnehmen
            m_agentsEnergy.put(agent.getId().toString(), new Double(energy));
        }else {
            throw new InvalidAgentException(agent.getId().toString());
        }
    }
    
    /**
     * Erzeugt den gewünschten Sprechakt.
     *
     * @param speachact
     * @return Speachact - erzeugter Speachact.
     * @throws InvalidActionKeyException
     */
    public Speachact createSpeachAct(Class speachact)
    throws InvalidElementException {
        try {
            Constructor cons =
                    speachact.getConstructor(new Class[]{Hashtable.class});
                    Object object = cons.newInstance(
                            new Object[]{this.copyHashtable(m_parameter)});
                            return (Speachact)object;
        }catch(Exception e) {
            throw new InvalidElementException();
        }
    }
    
    /**
     * Deep Copy einer Hashtable.
     *
     * @param table
     * @return Hashtable - Kopie
     */
    public Hashtable copyHashtable(Hashtable table) {
        Hashtable retval = new Hashtable();
        Enumeration keys = table.keys();
        while(keys.hasMoreElements()) {
            Object key = keys.nextElement();
            retval.put(key, this.copyEntry(table.get(key)));
        }
        return retval;
    }
    
    /**
     * Copy eines Parametereintrags.
     * @param element
     * @return Object - Kopierter Eintrag
     */
    public Object copyEntry(Object element) {
        
        if(element instanceof Boolean) {
            return new Boolean(((Boolean)element).booleanValue());
        }
        if(element instanceof String) {
            return new String((String)element);
        }
        if(element instanceof Double) {
            return new Double(((Double)element).doubleValue());
        }
        if(element instanceof Integer) {
            return new Integer(((Integer)element).intValue());
        }
        if(element instanceof Long) {
            return new Long(((Long)element).longValue());
        }
        return null;
    }
    
    /**
     * Sorgt für einen Reset aller Parameterwerte, wenn ein Spiel erneut simuliert
     * werden soll.
     */
    public abstract void reset();
    
    /**
     * Gibt eine Liste mit den Parametern, die dass Szenario benötigt zurück.
     *
     * @return LinkedList - Liste der Parameter
     */
    public abstract LinkedList getScenarioParameter();
    
     /**
     * Gibt eine Liste mit den Parametern, die dass Szenario benötigt zurück.
      * @param gameConf Game-KonfigDatei
     *
     * @return LinkedList - Liste der Parameter
     */
    public abstract LinkedList getScenarioParameter(GameConf gameConf);
    
    
   
}



