/**
 * Created on 04.10.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package scenario.fgml.statistic;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import scenario.fgml.statistic.FgmlAgentStatistic;
import simulator.statistic.IStatisticComponent;
import utils.Id;
import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;

/**
 * @author Daniel Friedrich
 *
 * Statistikkomponente für ein Spiel, das während des Multiagent-Contest auf der 
 * FGML 2003 in Karlsruhe ausgeführt wurde.
 */
public class FgmlStatisticComponent implements IStatisticComponent {

	public static final String LEARNINGRATE = "Lernrate";
	
	public static final String COSTS = "Restkosten";
	public static final String FOUNDEDCELLS = "gef. E-Zellen";
	public static final String VISITEDPLACES = "besuchte Plätze";
	public static final String MOVINGRATE = "Bewegungsrate";
	public static final String HITRATE = "Trefferqoute";
	
	public static final String RATING = "Rating";	
	public static final String MISSEDCELLS = "MissedCells";
	public static final String ROUNDCOSTAMOUNT = "RoundCostAmount";
	public static final String EXPLORATIONS = "Grabungen";
	
	public static final String STARTPHASEII = "Begin Phase II";
	public static final String STARTPHASEIII = "Begin Phase III";
	
	private Vector m_statisticParameter = new Vector();
	
	private Hashtable m_statisticValues = new Hashtable();
	
	private Hashtable m_statisticResult = new Hashtable();

	private Hashtable m_agentStatistics = new Hashtable();
	
	private Hashtable m_parameter = new Hashtable();
	
	private Hashtable m_statisticParameterPrefix = new Hashtable();
	
	private int m_roundLimit = 0;
	
	private double m_cellEnergy = 0.0;
	
	private double m_startEnergy = 0.0;
	
	
	/**
	 * Konstruktor.
	 */
	public FgmlStatisticComponent(Vector agents, Hashtable parameter) {
		super();
		m_parameter = parameter;
		
		/*
		 * Statistikparameter erfassen
		 */
		m_statisticParameter.addElement(LEARNINGRATE);
		m_statisticParameter.addElement(COMMCOSTS);
		m_statisticParameter.addElement(COSTS);
		m_statisticParameter.addElement(FOUNDEDCELLS);
		m_statisticParameter.addElement(VISITEDPLACES);
		//m_statisticParameter.addElement(MOVINGRATE);
		m_statisticParameter.addElement(NUMOFMESSAGES);
		m_statisticParameter.addElement(RATING);
		m_statisticParameter.addElement(ROUNDCOSTAMOUNT);	
		m_statisticParameter.addElement(MISSEDCELLS);
		m_statisticParameter.addElement(EXPLORATIONS);
		m_statisticParameter.addElement(STARTPHASEII);
		m_statisticParameter.addElement(STARTPHASEIII);
		// dazu passenden Wertebehälter erzeugen
		m_statisticValues.put(LEARNINGRATE, new Vector());
		m_statisticValues.put(COMMCOSTS, new Vector());
		m_statisticValues.put(COSTS, new Vector());
		m_statisticValues.put(FOUNDEDCELLS, new Vector());
		m_statisticValues.put(VISITEDPLACES, new Vector());
		//m_statisticValues.put(MOVINGRATE, new Vector());
		m_statisticValues.put(NUMOFMESSAGES, new Vector());
		m_statisticValues.put(RATING, new Vector());
		m_statisticValues.put(ROUNDCOSTAMOUNT, new Vector());
		m_statisticValues.put(MISSEDCELLS, new Vector());
		m_statisticValues.put(EXPLORATIONS, new Vector());
		m_statisticValues.put(STARTPHASEII, new Vector());
		m_statisticValues.put(STARTPHASEIII, new Vector());
		// dazu passende Prefixe erzeugen
		m_statisticParameterPrefix.put(LEARNINGRATE, " %");
		m_statisticParameterPrefix.put(COMMCOSTS, " E-Einheiten");
		m_statisticParameterPrefix.put(COSTS, " E-Einheiten");
		m_statisticParameterPrefix.put(FOUNDEDCELLS, " E-Zellen");
		m_statisticParameterPrefix.put(VISITEDPLACES, " Plätze");
		m_statisticParameterPrefix.put(MOVINGRATE, " %");
		m_statisticParameterPrefix.put(NUMOFMESSAGES, " Nachrichten");
		m_statisticParameterPrefix.put(RATING, " Punkte");
		m_statisticParameterPrefix.put(ROUNDCOSTAMOUNT, " E-Einheiten");
		m_statisticParameterPrefix.put(MISSEDCELLS, " E-Zellen");
		m_statisticParameterPrefix.put(EXPLORATIONS, " Versuche");
		m_statisticParameterPrefix.put(STARTPHASEII, " Runde");
		m_statisticParameterPrefix.put(STARTPHASEIII, " Runde");
		

		/*
		 * Agentenstatistiken anlegen 
		 */
		for(int i=0; i< agents.size(); i++) {
			Id agentId = (Id)agents.get(i);
			if(!m_agentStatistics.containsKey(agentId.toString())) {
				m_agentStatistics.put(agentId.toString(),
					new FgmlAgentStatistic(
						agentId,
						m_parameter,
						this.getParameters()));
			}
		}
		
		if(m_parameter.containsKey("RoundLimit")) {
			m_roundLimit = ((Integer)m_parameter.get("RoundLimit")).intValue();
		}	
		if(m_parameter.containsKey("EnergyCell")) {
			m_cellEnergy = ((Double)m_parameter.get("EnergyCell")).doubleValue();
		}
		
			
	}
	

	/* (non-Javadoc)
	 * @see simulator.statistic.IStatisticComponent#createStatistic()
	 */
	public void createStatistic() {
		/*
		 * Erst einmal alle Agentenstatistiken berechnen lassen, dann die 
		 * Additional-Parameter der Agenten abfragen, diese den eigenen 
		 * Statistik-Parametern hinzufügen, dann über alle Statistik-Parameter
		 * laufen und die Werte der Agenten abfragen und in die Values-Liste
		 * eintragen.
		 */
		Enumeration statKeys = m_agentStatistics.keys();
		while(statKeys.hasMoreElements()) {
			FgmlAgentStatistic agentStat = 
				(FgmlAgentStatistic)m_agentStatistics.get(statKeys.nextElement());
			// Agentenstatistiken berechnen lassen	
			agentStat.createStatistic();
			/*
			 * Über alle Statistikparameter laufen und diese mit den Werten aus
			 * der aktuellen Agentenstatistik füllen
			 */ 
			for(int index=0; index < m_statisticParameter.size(); index++) {
				String key = (String)m_statisticParameter.get(index);
				// Parameterwert abfragen
				Object object = agentStat.getParameter(key);
				// wenn object == null, dann nicht vorhanden in der Agentstat.
				if(object != null) {
					Vector tmp = (Vector)m_statisticValues.get(key);
					tmp.addElement(object);
					m_statisticValues.put(key,tmp);							
				}
			}

			// Zusätzliche Statistikparameter abfragen und notfalls hinzufügen
			Vector sparams = agentStat.getStatisticParameters();
			Enumeration pkeys = sparams.elements();
			while(pkeys.hasMoreElements()) {
				String pkey = (String)pkeys.nextElement();
				if(!m_statisticParameter.contains(pkey)) {
					m_statisticParameter.addElement(pkey);	
					// neuen Statistikparameter-Wertebehälter erzeugen 
					m_statisticValues.put(pkey, new Vector()); 
					// Parameterwert abfragen
					Object object = agentStat.getParameter(pkey);
					// wenn object == null, dann nicht vorhanden in der Agentstat.
					if(object != null) {
						Vector tmp = (Vector)m_statisticValues.get(pkey);
						tmp.addElement(object);
						m_statisticValues.put(pkey,tmp);								
					}					
				}
			}

		}
		
		for(int index =0; index < m_statisticParameter.size(); index++) {
			String key = (String)m_statisticParameter.get(index);
			Vector values = (Vector)m_statisticValues.get(key);
			double result = 0.0;
			try {
				for(int i=0; i < values.size(); i++) {
					result = result + ((Double)values.get(i)).doubleValue();
				}
			}catch(Exception e) {
				e.printStackTrace(System.out);
			}
			if((key.startsWith(LEARNINGRATE)) 
				|| (key.equals(ROUNDCOSTAMOUNT)) 
				|| (key.equals(MOVINGRATE))
				|| (key.equals(HITRATE)
				|| (key.equals(STARTPHASEII))
				|| (key.equals(STARTPHASEIII)))){
				result = (result / ((double)values.size()));
			}
			// Ansonsten Summenwerte
			m_statisticResult.put(key, new Double(result));
		}
		
		this.calculateRating();
	}

	/* (non-Javadoc)
	 * @see simulator.statistic.IStatisticComponent#addInformation(java.lang.String, java.lang.Object)
	 */
	public void addInformation(Id agentId, String infoName, Object value)
		throws InvalidAgentException,
				InvalidElementException {		
		if(m_agentStatistics.containsKey(agentId.toString())) {
			((FgmlAgentStatistic)m_agentStatistics.get(
				agentId.toString())).addInformation(
					infoName,
					value);	
		}else {
			throw new InvalidAgentException(agentId.toString());
		}
	}
	
	public Vector getParameters() {
		return m_statisticParameter;
	}
	
	public String getCSVDataset() {
		StringBuffer csvLine = new StringBuffer("");
		for(int index=0; index < m_statisticParameter.size(); index++) {
			String key = (String)m_statisticParameter.get(index);
			csvLine.append(
				((Double)m_statisticResult.get(key)).toString() 
				+ ";");
			
		}
		csvLine.append("\n");
		return csvLine.toString();
	}
	
	private void calculateRating() {
		double rating = 0.0;
		double lernrating = 0.0;
		if(m_statisticResult.containsKey(LEARNINGRATE)) {
			lernrating = 
				((Double)m_statisticResult.get(LEARNINGRATE)).doubleValue();
		}
		// Berechnung
		int reachablePlaces = m_roundLimit * m_agentStatistics.size();
		
		/* 
		 * Nur Annäherung, da das bestimmen, wieviele wirklich nicht gefunden 
		 * wurden relativ schwer ermittelbar ist. Im Szenario werden aber immer 
		 * 50 % der Plätze mit Zellen versehen, deshalb * 0.5.
		 */		
		double reachableEnergy = 
			((double)reachablePlaces) * 0.5 * m_cellEnergy;
		System.out.println("Ratingberechnung: erreichbare Energie: " + Double.toString(reachableEnergy));
		double foundedCells = 
			((Double)m_statisticResult.get(FOUNDEDCELLS)).doubleValue();
		System.out.println("Ratingberechnung: gefundene Zellen: " + Double.toString(foundedCells));	
		double explorationRating = 
			((foundedCells * m_cellEnergy) / reachableEnergy) * 100.0;	
			
		
		double consumRating = 
			(((Double)m_statisticResult.get(COMMCOSTS)).doubleValue()
			+ ((Double)m_statisticResult.get(COSTS)).doubleValue())
			/ ((m_startEnergy * m_agentStatistics.size()) + reachableEnergy)
			* 100.0;
		System.out.print("Lernrating: " + Double.toString(lernrating) + " + ");
		System.out.println("Exploration Rating: " + Double.toString(explorationRating) + " - ");
		System.out.println("Consum Rating: " + Double.toString(consumRating));

		rating = lernrating + explorationRating - consumRating;
		System.out.println(
			"Stat -> Rating: " + Double.toString(rating));	
		m_statisticResult.put(RATING, new Double(rating));
	}
	
	/* (non-Javadoc)
	 * @see simulator.statistic.IStatisticComponent#getParameter(java.lang.String)
	 */
	public Object getParameter(String parameter) {
		if(m_statisticResult.containsKey(parameter)) {
			return m_statisticResult.get(parameter);
		}
		return null;
	}
	
	public String getParameterPrefix(String parameter) {
		if(m_statisticParameterPrefix.containsKey(parameter)) {
			return (String)m_statisticParameterPrefix.get(parameter);
		}else {
			if(parameter.startsWith(FgmlStatisticComponent.LEARNINGRATE)){
				return " %";
			}
		}
		return "";		
	}
	
	public void nextRound(Id agentId) throws InvalidAgentException {
		if(m_agentStatistics.containsKey(agentId.toString())) {
			((FgmlAgentStatistic)m_agentStatistics.get(agentId.toString())).nextRound();
		}else {
			throw new InvalidAgentException(agentId.toString());
		}
	}
	
	public int getNumOfAgents() {
		return m_agentStatistics.size();
	}

}

