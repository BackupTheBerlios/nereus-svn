/*
 * Dateiname      : ClientInfoObject.java
 * Erzeugt        : 5. August 2003
 * Letzte �nderung: 
 * Autoren        : Daniel Friedrich
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
package client;

import java.io.File;

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
	 * Konstruktor.
	 *
	 * Als private, damit niemand irrt�mlich mehrere Instanzen anlegt. Im 
	 * Konstruktor werden die ganzen Konf-Parameter bestimmt. 
	 * 
	 */
	private ClientInfoObject(String pathName) {
		super();
		// Basispfad bestimmen.
		File dFile = new File("");
		//String pathName = dFile.getAbsolutePath();
		m_clientBasePath = new String(pathName);
		System.out.println("Clientbasispfad: "+ m_clientBasePath);
		// Pfadseparator bestimmen
		String seperator = null;
		if(pathName.indexOf("/") > -1) {
			m_pathSeparator = "/";
		}else {
			m_pathSeparator = "\\";
		}
		System.out.println("Pfad-Separator: " + m_pathSeparator);
		// Scenario- und Umweltpfad bestimmen
		if(m_clientBasePath.endsWith(m_pathSeparator)) {
			// Agentenklassenpfad festlegen.
			m_agentClassesPath = m_clientBasePath + "scenario" + m_pathSeparator;							
		}else {
			// Scenariopfad festlegen.
			m_agentClassesPath =
				m_clientBasePath 
				+ m_pathSeparator
				+ "scenario" 
				+ m_pathSeparator;
		}
		System.out.println("Szenariopfad: "+ m_agentClassesPath);
	}
	
	/**
	 * Zugriffsmethode auf das ServerInfoObject.
	 * 
	 * @return ServerInfoObject
	 */
	public static ClientInfoObject getInstance(String path) {
		// wenn noch keine Instanz existiert, dann eine erstellen
		if(m_instance == null) {
			m_instance = new ClientInfoObject(path);
		}
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
