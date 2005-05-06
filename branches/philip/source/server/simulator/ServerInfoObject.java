package simulator;

import java.io.File;

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
	 * Konstruktor.
	 *
	 * Als private, damit niemand irrtümlich mehrere Instanzen anlegt. Im 
	 * Konstruktor werden die ganzen Konf-Parameter bestimmt. 
	 * 
	 */
	private ServerInfoObject(String pathName) {
		super();
		// Basispfad bestimmen.
		File dFile = new File("");
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
			m_scenarioPath = m_serverBasePath + "scenario" + m_pathSeparator;			
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
				+ "scenario" 
				+ m_pathSeparator;
			// Umweltpfad festlegen	
			m_enviromentsPath = 
				m_serverBasePath
				+ m_pathSeparator 
				+ "enviroments" 
				+ m_pathSeparator;	
		}
		System.out.println("Szenariopfad: "+ m_scenarioPath);
		System.out.println("Umweltpfad: "+ m_enviromentsPath);
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
