/*
 * Dateiname      : ServerInfoObject.java
 * Erzeugt        : 22. Mai 2003
 * Letzte Änderung: 11. Mai 2005
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
package nereus.simulator;

import java.io.File;
import java.util.LinkedList;
import java.util.*;

import nereus.utils.GameXMLConfigHandler;
import nereus.utils.GameConf;
import nereus.utils.ScenarioXMLInputReader;

/**
 * Dient zum Abfragen von wichtigen Konfigurationsinformationen, wie z.B. dem
 * Basispfad des Servers, dem Pfad an dem die Szenarios abgelegt sind. Welcher
 * Pathseparator im laufenden System gültig usw. Das Object kann überall auf dem
 * Server abgefragt werden. Es existiert ausserdem immer nur genau ein Object.
 * (siehe Singelton-Pattern)
 *
 * @author Daniel Friedrich
 */
public class ServerInfoObject {
    
    /**
     * Singelton-Instanz
     */
    public static ServerInfoObject m_instance = null;
    
    /**
     *	Pfadseparator des laufenende Systems
     */
    private String m_pathSeparator = null;
    
    /**
     * Basispfad des Servers
     */
    private String m_serverBasePath = null;
    
    /**
     * Hostname des Servers
     */
    private String m_serverName = null;
    
    /**
     * Pfad an dem die Szenarien abgelegt sind.
     */
    private String m_scenarioPath = null;
    
    /**
     * Pfad an dem die Umwelten der Szenarien abgespeichert sind.
     */
    private String m_enviromentsPath = null;
    
    /**
     * Liste in der Konfigurationen der Spiele gespeichert sind.
     */
    private LinkedList gameConfList;
    
    /**
     * Konfigurationsdateiname
     */
    private String CONFIGDATEINAME="serverconfig.xml";
    
    protected static String cofigFileURI="";
    
    private LinkedList scenarioNames=null;
    
    /**
     * Konstruktor.
     *
     * Als private, damit niemand irrtümlich mehrere Instanzen anlegt. Im
     * Konstruktor werden die ganzen Konf-Parameter bestimmt.
     *
     */
    private ServerInfoObject(String pathName) {
        super();
        // Basispfad bestimmen.
        //File dFile = new File("");
        //String pathName = dFile.getAbsolutePath();
        m_serverBasePath = new String(pathName);
        System.out.println("Serverbasispfad: "+ m_serverBasePath);
        // Pfadseparator bestimmen
        String seperator = null;
        if(pathName.indexOf("/") > -1) {
            m_pathSeparator = "/";
        }else {
            m_pathSeparator = "\\";
        }
        System.out.println("Pfad-Separator: " + m_pathSeparator);
        // Scenario- und Umweltpfad bestimmen
        if(m_serverBasePath.endsWith(m_pathSeparator)) {
            // Scenariopfad festlegen.
            m_scenarioPath = m_serverBasePath + "scenarios" + m_pathSeparator;
            // Umweltpfad festlegen
            m_enviromentsPath =
                    m_serverBasePath
                    + "enviroments"
                    + m_pathSeparator;
            
        }else {
            // Scenariopfad festlegen.
            m_scenarioPath =
                    m_serverBasePath
                    + m_pathSeparator
                    + "scenarios"
                    + m_pathSeparator;
            // Umweltpfad festlegen
            m_enviromentsPath =
                    m_serverBasePath
                    + m_pathSeparator
                    + "enviroments"
                    + m_pathSeparator;
        }
        System.out.println("Szenariopfad: "+ m_scenarioPath);
        //System.out.println("Umweltpfad: "+ m_enviromentsPath);
        
        this.gameConfList=readGameConfList(this.cofigFileURI);
    }
    
    
    /**
     * Game-Config-Daten aus einer XML-KonfiguratiosDatei ein.
     */
    private LinkedList readGameConfList(String configFileURI){
        String gameConfigXMLpath;
        if (m_serverBasePath.endsWith(m_pathSeparator))
            gameConfigXMLpath=m_serverBasePath+CONFIGDATEINAME;
        else gameConfigXMLpath=m_serverBasePath+ m_pathSeparator + CONFIGDATEINAME;
        if (configFileURI!="") gameConfigXMLpath=configFileURI;
        GameXMLConfigHandler handler=new GameXMLConfigHandler();
        ScenarioXMLInputReader sceanrioXMLInputReader=new ScenarioXMLInputReader(gameConfigXMLpath ,handler);
        return handler.getGameConfList();
    }
    
    
    
    /**
     * Liefert eine Liste der verfügbaren Szenarien
     * @return Liste der verfügbaren Szenarien
     */
    public LinkedList getScenarioNames(){
        if (this.scenarioNames==null) {
            Iterator gameConfIter=this.gameConfList.iterator();
            HashSet scenarioSet=new HashSet();
            while (gameConfIter.hasNext()){
                GameConf myGameConf=(GameConf)gameConfIter.next();
                String scenarioName=myGameConf.getScenarioName();
                scenarioSet.add(scenarioName);
            }
            LinkedList scenarioListe=new LinkedList();
            Iterator hashSetIter=scenarioSet.iterator();
            while (hashSetIter.hasNext()){
                String scenarioName=new String((String) hashSetIter.next());
                scenarioListe.add(scenarioName);
            }
            this.scenarioNames=scenarioListe;
        }
        return this.scenarioNames;
        
    }
    
    
    
    /**
     * Liefert eine Liste aus einer XML-Datei eingelesenen Game-Config-Daten
     * (scenario, karte, parameter).
     */
    public LinkedList getGameConfList(){
        return this.gameConfList;
    }
    
    
    
    /**
     * Liefert die Configuratinsdaten fuer ein bestimmters Szenario
     *
     * @param scenarioName Name des Szenario, fuer den die Daten
     * die Configurationsdaten geliefert werden sollen.
     * @return Configurationsdaten fuer den vorgegebenen scenarioNamen.
     */
    
    public GameConf getGameConf(String scenarioName){
        Iterator gameConfIter=this.gameConfList.iterator();
        GameConf retValue;
        while (gameConfIter.hasNext()){
            GameConf actualGameConf=(GameConf) gameConfIter.next();
            if(scenarioName.equals(actualGameConf.getScenarioName())){
                retValue=actualGameConf;
                return retValue;
            }
        }
        return null;
    }
    
    /**
     * Zugriffsmethode auf das ServerInfoObject.
     *
     * @return ServerInfoObject
     */
    public static ServerInfoObject getInstance(String path) {
        // wenn noch keine Instanz existiert, dann eine erstellen
        if(m_instance == null) {
            m_instance = new ServerInfoObject(path);
        }
        return m_instance;
    }
    
    /**
     * Liefert den Pfad an dem die Scenarios abgelegt sind.
     *
     * @return String - Pfad an dem die Scenarios abgelegt sind
     */
    public String getScenarioPath() {
        return m_scenarioPath;
    }
    
    /**
     * Liefert den Basispfad des Servers.
     *
     * @return String - Basispfad des Servers.
     */
    public String getServerBasePath() {
        return m_serverBasePath;
    }
    
    /**
     * Liefert den PathSeparator des Systems auf dem der Server läuft.
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
     * Liefert den Namen des Servers zurück, ist dieser nicht gesetzt dann localhost.
     *
     * @return String - Name des Servers
     */
    public String getServerName() {
        if(m_serverName == null) {
            return "localhost";
        }
        return m_serverName;
    }
    
    /**
     * Liefert den Pfad an dem die Umwelten der Szenarien gespeichert sind.
     *
     * @return String - Pfad  an dem die Umwelten der Szenarien gespeichert sind.
     */
    public String getEnviromentsPath() {
        return m_enviromentsPath;
    }
}
