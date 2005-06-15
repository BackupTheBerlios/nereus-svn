/*
 * Dateiname      : ClientInfoObject.java
 * Erzeugt        : 5. August 2003
 * Letzte �nderung: 15. Juni durch Eugen Volk
 * Autoren        : Daniel Friedrich
 *                  Eugen Volk
 *
 *
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut f�r Intelligente Systeme
 * der Universit�t Stuttgart unter Betreuung von Dietmar Lippold
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
package nereus.registrationgui;

import java.io.File;
import java.util.*;

import nereus.utils.ScenarioXMLInputReader;
import nereus.utils.AgentsXMLConfigHandler;
import nereus.utils.AgentsList;

/**
 * Dient zum Abfragen von wichtigen Konfigurationsinformationen, wie z.B. dem
 * Basispfad des Client, dem Pfad an dem die Agentenklassen abgelegt sind.
 * Welcher Pathseparator im laufenden System g�ltig usw. Das Object kann �berall
 * von den Clientklassen abgefragt werden. Es existiert jeweils immer nur eine
 * Instanz pro Client. (siehe Singelton-Pattern)
 *
 * @author Daniel Friedrich
 */
public class ClientInfoObject {
    
    /**
     * Singelton-Instanz
     */
    public static ClientInfoObject m_instance = null;
    
    protected String clientConfigFileURI="";
    
    /**
     *	Pfadseparator des laufenende Systems
     */
    private String m_pathSeparator = null;
    
    /**
     * Basispfad des Client
     */
    private String m_clientBasePath = null;
    
    /**
     * Hostname des Servers
     */
    private String m_serverName = null;
    
    /**
     * Pfad an dem die Agentenklassen abgelegt sind.
     */
    private String m_agentClassesPath = null;
    
    /**
     * Eine Liste aus AgentsList-Strukturen.
     */
    private LinkedList agentsScenarioList;
    
    /**
     *
     */
    private HashMap visClassNames=null;
    
 
    
    /**
     * Konstruktor.
     *
     * Als private, damit niemand irrt�mlich mehrere Instanzen anlegt. Im
     * Konstruktor werden die ganzen Konf-Parameter bestimmt.
     *
     */
    private ClientInfoObject(String pathName, String clientConfigFileURI) {
        super();
        
        m_clientBasePath = new String(pathName);
        System.out.println("Clientbasispfad: "+ m_clientBasePath);
        this.clientConfigFileURI=new String(clientConfigFileURI);
        
        String seperator = null;
        if(pathName.indexOf("/") > -1) {
            m_pathSeparator = "/";
        }else {
            m_pathSeparator = "\\";
        }
        if (!m_clientBasePath.endsWith(m_pathSeparator)){
            m_clientBasePath=new String(m_clientBasePath + m_pathSeparator);
        }
        
         
        this.agentsScenarioList=readScenarioAgentsList(this.clientConfigFileURI);
        this.visClassNames=readVisClassNames(this.clientConfigFileURI);
    }
    
    
    /**
     * Liest aus einer XML-Konfig-Datei verf�gbaren Agenten aus.
     * @param configFileURI URI der Konfigurationsdatei
     * @return eine Liste die aus einer AgentsList-Struktur besteht.
     */
    private LinkedList readScenarioAgentsList(String configFileURI){
        String clientConfigXMLpath;
        clientConfigXMLpath=configFileURI;
        
        AgentsXMLConfigHandler handler=new AgentsXMLConfigHandler();
        ScenarioXMLInputReader sceanrioXMLInputReader=new ScenarioXMLInputReader(clientConfigXMLpath,handler);
        this.agentsScenarioList=handler.getScenarioAgentsList();
        return handler.getScenarioAgentsList();
    }
    
    /**
     * Liefert zu einem SzenarioNamen verf�gbare Agenten.
     * @return Liste zu einem Szenario verf�gbaren Agenten.
     */
    public LinkedList getAgentsList(String scenarioName){
        Iterator agentsScenListIter=this.agentsScenarioList.iterator();
        LinkedList retVal=null;
        while(agentsScenListIter.hasNext()){
            AgentsList actual;
            actual=(AgentsList)agentsScenListIter.next();
            if (scenarioName.equals(actual.getScenarioName())){
                retVal=actual.getAgents();
                return retVal;
            }
        }
        return retVal;
    }
    
    /**
     * Liest aus einer XML-Konfig-Datei verf�gbaren VisalisierungsKlassenNamen aus.
     * @param configFileURI URI der Konfigurationsdatei
     */
    private HashMap readVisClassNames(String configFileURI){
        String clientConfigXMLpath;
        clientConfigXMLpath=configFileURI;
       
        AgentsXMLConfigHandler handler=new AgentsXMLConfigHandler();
        ScenarioXMLInputReader sceanrioXMLInputReader=new ScenarioXMLInputReader(clientConfigXMLpath,handler);
        this.visClassNames=handler.getVisualisationClassNames();
        return this.visClassNames;
    }
    
    /**
     * liefert zu einem scenarioNamen zugeh�rigen Namen der Klasse f�r
     * die Steuerung der Visualisierung des Szenarios.
     * @param scenarioName Name des Szenario
     * @return KlassenName des VisualisierungssteuerungsKlasse.
     */
    public String getVisualisationClassName(String scenarioName){
        String retValue;
        if (this.visClassNames.containsKey(scenarioName)){
            retValue=(String)this.visClassNames.get(scenarioName);
            return retValue;
        }else return null;
    }
    
    
    
    /**
     * Zugriffsmethode auf das ServerInfoObject.
     *
     * @param basePath BasisPfad
     * @param clientConfigFileURI
     * @param agentsPath Agentenpfad
     *
     * @return ServerInfoObject
     */
    public static ClientInfoObject getInstance(String basePath, String clientConfigFileURI) {
        // wenn noch keine Instanz existiert, dann eine erstellen
        if(m_instance == null) {
            m_instance = new ClientInfoObject(basePath, clientConfigFileURI);
        }
        return m_instance;
    }
    
     /**
     * Zugriffsmethode auf das ServerInfoObject.
     *
     * @return ServerInfoObject
     */
    public static ClientInfoObject getInstance(){
        return m_instance;
    }
    
    
    /**
     * Liefert den Pfad an dem die Agentenklassen abgelegt sind.
     *
     * @return String - Pfad an dem die Agentenklassen abgelegt sind
     */
    public String getAgentClassesPath() {
        return m_agentClassesPath;
    }
    
    /**
     * Liefert den Basispfad des Clients.
     *
     * @return String - Basispfad des Clients.
     */
    public String getClientBasePath() {
        return m_clientBasePath;
    }
    
    /**
     * Liefert den PathSeparator des Systems auf dem der Server l�uft.
     *
     * @return String - PathSeparator.
     */
    public String getPathSeparator() {
        return m_pathSeparator;
    }
    
    /**
     * Setzt den Namen des Servers.
     *
     * @param serverName - Name des Servers
     */
    public void setServerName(String serverName) {
        m_serverName = serverName;
    }
    
    /**
     * Liefert den Namen des Servers zur�ck, ist dieser nicht gesetzt dann localhost.
     *
     * @return String - Name des Servers
     */
    public String getServerName() {
        if(m_serverName == null) {
            return "localhost";
        }
        return m_serverName;
    }
}
