/*
 * Created on 17.09.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package simulator.statistic;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import utils.Id;

/**
 * Komponente zum Erstellen von Statistiken für ein Spiel mit einem beliebigen
 * Spiel.
 * 
 * @author Daniel Friedrich
 */
public class StatisticManagement {
	
	/**
	 * Id des Spiels für das die Statistik ist. 
	 */
	private Id m_gameId;
	
	/**
	 * Name des Spiels für das die Statistik ist.
	 */	
	private String m_gameName;
	
	/**
	 * Liste mit Parametern die ausgewertet werden sollen
	 */
	private Vector m_parameters = new Vector();
	
	/**
	 * Umweltparameter die ausgegeben werden sollen
	 */
	private Hashtable m_envParameters = new Hashtable();

	/**
	 * Statistiken der Spiele
	 */
	private Vector m_statistics = new Vector();
	
	/**
	 * Statistic
	 */
	private String m_statisticText = null;
	
	/**
	 * CSV-Ausgabe der Statistik
	 */
	private String m_csvStatisticText = null;
	
	/**
	 * Liste aller Agenten
	 */
	private Hashtable m_agents = new Hashtable();
	
	/**
	 * Hashtable mit den Prefixen der 
	 */
	private Hashtable m_parameterPrefixes = new Hashtable();
	

	/**
	 * Konstruktor.
	 */
	public StatisticManagement(
		Id gameId,
		String gameName,
		Hashtable envParameters) {
		super();
		m_gameId = gameId;
		m_gameName = gameName;
		m_envParameters = envParameters;
	}

	public void nextExperiment(IStatisticComponent statistic) {
		m_statistics.addElement(statistic);
	}
	
	/**
	 * Liefert eine Trennlinie fär die Ausgabe.
	 * @return String
	 */
	private String getLine() {
		StringBuffer retval = new StringBuffer(
			"-------------------------------------------------");
		retval.append("-------------------------------\n");
		return retval.toString();
	}	
	
	public void createStatistic() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getHead());
		buffer.append(this.calculateStatistic());
		m_statisticText = buffer.toString();
	}
	
	public String getGameStatistic() {
		return m_statisticText;
	}
	
	public String getAgentStatistics() {
		StringBuffer buffer = new StringBuffer();
		Enumeration keys = m_agents.keys();
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement(); 
			Object id = m_agents.get(key);
			for(int i=0; i < m_statistics.size(); i++) {
				//@TODO
			}			 
		}
		return buffer.toString();
	}
	
	public String getHead() {
		IStatisticComponent component = 
			(IStatisticComponent)m_statistics.get(0);
		m_envParameters.put(
			"Agentenanzahl", 
			new Integer(component.getNumOfAgents()));
		StringBuffer buffer = new StringBuffer();
		buffer.append(getLine());	
		buffer.append("Statistische Auswertung\n"); 	
		buffer.append(getLine());	
		buffer.append("Spiel: " + m_gameName + "\n");	
		Date today = new Date();	
		buffer.append("Datum: " + today.toString() + "\n");
		buffer.append("Experimente: " + m_statistics.size() + "\n");	
		buffer.append(getLine());	
		buffer.append("Umwelt: " + "\n");
		Enumeration keys = m_envParameters.keys();
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
			buffer.append(
				key.toString() 
				+  ": " 
				+ m_envParameters.get(key).toString() 
				+ "\n");
		}		
		buffer.append(getLine());
		return buffer.toString();			
	}
	
	
	
	public String calculateStatistic() {
		StringBuffer retval = new StringBuffer();
		StringBuffer csvBuffer = new StringBuffer();
		
		// Statistiken berechnen lassen
		for(int i=0; i < m_statistics.size();i++) {
			IStatisticComponent stat = 
				(IStatisticComponent)m_statistics.get(i);
			try {	
				stat.createStatistic();
			}catch(Exception e) {
				e.printStackTrace(System.out);	
			}
		}
		
		/*
		 * Neueste Statistischen Parameter abfragen und die Berechnung 
		 * vorbereiten.
		 */
		IStatisticComponent statComponent = 
			(IStatisticComponent)m_statistics.get(0); 
		m_parameters = statComponent.getParameters();
		Enumeration elements = m_parameters.elements();
		while(elements.hasMoreElements()) {
			// Parametername holen
			String sKey = (String)elements.nextElement();
			// Prefix dafür holen
			m_parameterPrefixes.put(
				sKey,statComponent.getParameterPrefix(sKey));
			// Im CSV-Buffer eine Spalte erstellen
			csvBuffer.append(sKey + ";"); 	
		}
		// CSV-Header abschliessen
		csvBuffer.append("\n");
		
		// Werte berechnen
		Enumeration params = m_parameters.elements();
		while(params.hasMoreElements()) {
			String param = (String)params.nextElement();
			double amount = 0.0;
			int counter = 0;
			for(int i=0; i < m_statistics.size(); i++) {
				IStatisticComponent stat = 
					(IStatisticComponent)m_statistics.get(i);
				try {
					// Wert für Parameter abfragen
					Object sValue = stat.getParameter(param);
					// Testen ob der überhaupt erfasst wurde
					if(sValue != null) {
						amount = amount + ((Double)sValue).doubleValue();
						// mitzählen, wie oft erfasst.
						counter++; 
					}
				}catch(Exception e) {
					e.printStackTrace(System.out);
				}
			}
			// Die Summe noch durch die Anzahl der erfassten Daten teilen
			double result = amount / ((double)counter);
			// Daten wegspeichern
			retval.append(
				param 
				+ ": " 
				+ Double.toString(result) 
				+ statComponent.getParameterPrefix(param)
				+ "\n");
		}
		
		// Footer nach unten
		retval.append(getLine());
		
		// csv erstellen
		for(int index=0; index < m_statistics.size();index++) {
			IStatisticComponent sComponent =
				(IStatisticComponent)m_statistics.get(index);
			csvBuffer.append(sComponent.getCSVDataset());
		}
		m_csvStatisticText = csvBuffer.toString();
		this.saveCSVStatistic();
		return retval.toString();
	}
	
	public void saveCSVStatistic() {
		try {
			File statFile = new File("statistic" + m_gameId + ".csv");
			FileWriter logFileWriter = new FileWriter(statFile);
			logFileWriter.write(m_csvStatisticText);
			logFileWriter.close();
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
