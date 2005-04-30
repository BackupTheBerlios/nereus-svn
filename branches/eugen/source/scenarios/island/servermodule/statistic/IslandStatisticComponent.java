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
package scenario.island.statistic;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;

import simulator.statistic.IStatisticComponent;
import utils.Id;
/**
 * Berechnet die Statistik eines Spiels im Insel-Szenario. Als statistische
 * Werte werden dazu erfasst:
 * - Lernrate
 * - Restkosten
 * - Kommunikationskosten
 * - gef. E-Zellen
 * - nicht gef. E-Zellen
 * - Anzahl des übersehens einer E-Zelle
 * - Anzahl der Bewegungen
 * - Anzahl der Grabungen
 * - Begin der Phase I
 * - Begin der Phase II
 * - Anzahl der besuchten Plätze
 * 
 * Aus diesen Daten heraus werden berechnet:
 * - Rating 
 * - Bewegungsrate
 * - Trefferqoute
 * 
 * Es werden immer die durchschnittlichen Werte für ein Team und alle Messungen
 * ermittelt. 
 * 
 * @author Daniel Friedrich
 */
public class IslandStatisticComponent implements IStatisticComponent {
	
	// Festgelegten Statistikparameter
	/**
	 * Konstante zum Melden von ermittelten Lernratenwerten
	 */
	public static final String LEARNINGRATE = "Lernrate";	
	/**
	 * Konstante zum Melden von ermittelten Werten der Restkosten
	 */
	public static final String OTHERCOSTS = "Restkosten";
	/**
	 * Konstante zum Melden von ermittelten Werten der gef. E-Zellen
	 */
	public static final String FOUNDEDCELLS = "gef. E-Zellen";
	/**
	 * Konstante zum Melden von ermittelte Werten für die Anzahl der 
	 * durchgeführten Bewegungen.
	 */
	public static final String MOVES = " Bewegungen";
	/**
	 * Konstante zum Melden von ermittelten werten für die Anzahl der
	 * besuchten Plätze
	 */
	public static final String REALLYVISITEDPLACES =  "wirklich besuchte Plätze";
	/**
	 * Konstante zum Durchreichen der ermittelten Werten für die Bewegungsrate
	 */
	public static final String MOVINGRATE = "Bewegungsrate";
	/**
	 * Konstante zum Durchreichen der ermittelten Werten für die Trefferqoute.
	 * Die Trefferqoute beschreibt, wie oft ein Agent beim Graben eine 
	 * Energiezelle gefunden hat.
	 */
	public static final String HITRATE = "Trefferqoute";
	/**
	 * Konstante zum Durchreichen der ermittelten Werten für das Rating.
	 * Rating = Lernrate + relativer Energiegewinn - relativer Energieverbrauch
	 */
	public static final String RATING = "Rating";	
	/**
	 * Konstante zum Melden der ermittelten Werten für die übersehenen E-Zellen
	 */
	public static final String MISSEDCELLS = "MissedCells";
	/**
	 * Konstante zum Melden der ermittelten Werten für die verpassten E-Zellen
	 */
	public static final String REALLYMISSEDCELLS = "ReallyMissedCells";
	/**
	 * Konstante zum Melden der ermittelten Werten für die Anzahl der Grabungen
	 */
	public static final String EXPLORATIONS = "Grabungen";
	/**
	 * Konstante zum Melden der ermittelten Werten für das Erreichen der
	 * Klassifikationsschwelle
	 */
	public static final String CLASSIFICATIONLINE = "Klassifikationsschwelle";
	/**
	 * Konstante zum Melden der ermittelten Werten für das Erreichen der
	 * Kommunikationsschwelle
	 */
	public static final String COMMUNICATIONLINE = "Kommunikationsschwelle";
	
	/**
	 * Menge der Statistikparameter
	 */
	private Vector m_statisticParameter = new Vector();
	
	/** 
	 * Hashtable mit den erfassten Werten für die Statistikparameter.
	 */
	private Hashtable m_statisticValues = new Hashtable();
	
	/**
	 * Hashtable mit den berechneten Statistikparameter
	 */
	private Hashtable m_statisticResult = new Hashtable();

	/**
	 * Hashtable mit den Statistikkomponenten der Agenten des Spiels.
	 */
	private Hashtable m_agentStatistics = new Hashtable();
	
	/**
	 * Hashtable der Spielparameter.
	 */
	private Hashtable m_parameter = new Hashtable();
	
	/**
	 * Hashtable mit den Zusätzen der Statistikparametern.
	 */
	private Hashtable m_statisticParameterPrefix = new Hashtable();
	
	/**
	 * Max. Anzahl der berechneten Runden.
	 */
	private int m_roundLimit = 0;
	
	/**
	 * Energie in einer Energiezelle.
	 */
	private double m_cellEnergy = 0.0;
	
	/**
	 * Startenergie
	 */
	private double m_startEnergy = 0.0;
	
	/**
	 * Anzahl der wirklich besuchten Plätze.
	 */
	private int m_reallyVisitedPlaces = 0;
	
	/**
	 * Konstruktor.
	 * 
	 * @param agents
	 * @param parameter
	 */
	public IslandStatisticComponent(Vector agents, Hashtable parameter) {
		super();
		m_parameter = parameter;
		
		/*
		 * Statistikparameter erfassen
		 */
		m_statisticParameter.addElement(LEARNINGRATE);
		m_statisticParameter.addElement(COMMCOSTS);
		m_statisticParameter.addElement(OTHERCOSTS);
		m_statisticParameter.addElement(FOUNDEDCELLS);
		m_statisticParameter.addElement(MOVES);
		m_statisticParameter.addElement(REALLYVISITEDPLACES);
		m_statisticParameter.addElement(NUMOFMESSAGES);
		m_statisticParameter.addElement(RATING);
		m_statisticParameter.addElement(MISSEDCELLS);
		m_statisticParameter.addElement(REALLYMISSEDCELLS);
		m_statisticParameter.addElement(EXPLORATIONS);
		m_statisticParameter.addElement(CLASSIFICATIONLINE);
		m_statisticParameter.addElement(COMMUNICATIONLINE);
		// dazu passenden Wertebehälter erzeugen
		m_statisticValues.put(LEARNINGRATE, new Vector());
		m_statisticValues.put(COMMCOSTS, new Vector());
		m_statisticValues.put(OTHERCOSTS, new Vector());
		m_statisticValues.put(FOUNDEDCELLS, new Vector());
		m_statisticValues.put(MOVES, new Vector());
		m_statisticValues.put(REALLYVISITEDPLACES, new Vector());
		m_statisticValues.put(NUMOFMESSAGES, new Vector());
		m_statisticValues.put(RATING, new Vector());
		m_statisticValues.put(MISSEDCELLS, new Vector());
		m_statisticValues.put(REALLYMISSEDCELLS, new Vector());
		m_statisticValues.put(EXPLORATIONS, new Vector());
		m_statisticValues.put(CLASSIFICATIONLINE, new Vector());
		m_statisticValues.put(COMMUNICATIONLINE, new Vector());
		// dazu passende Prefixe erzeugen
		m_statisticParameterPrefix.put(LEARNINGRATE, " %");
		m_statisticParameterPrefix.put(COMMCOSTS, " E-Einheiten");
		m_statisticParameterPrefix.put(OTHERCOSTS, " E-Einheiten");
		m_statisticParameterPrefix.put(FOUNDEDCELLS, " E-Zellen");
		m_statisticParameterPrefix.put(MOVES, " Bewegungen");
		m_statisticParameterPrefix.put(REALLYVISITEDPLACES, " Plätze");
		m_statisticParameterPrefix.put(MOVINGRATE, " %");
		m_statisticParameterPrefix.put(NUMOFMESSAGES, " Nachrichten");
		m_statisticParameterPrefix.put(RATING, " Punkte");
		m_statisticParameterPrefix.put(MISSEDCELLS, " E-Zellen");
		m_statisticParameterPrefix.put(REALLYMISSEDCELLS, " E-Zellen");
		m_statisticParameterPrefix.put(EXPLORATIONS, " Versuche");
		m_statisticParameterPrefix.put(CLASSIFICATIONLINE, " Runde");
		m_statisticParameterPrefix.put(COMMUNICATIONLINE, " Runde");
		
		/*
		 * Agentenstatistiken anlegen 
		 */
		for(int i=0; i< agents.size(); i++) {
			Id agentId = (Id)agents.get(i);
			if(!m_agentStatistics.containsKey(agentId.toString())) {
				m_agentStatistics.put(agentId.toString(),
					new IslandAgentStatistic(
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
			IslandAgentStatistic agentStat = 
				(IslandAgentStatistic)m_agentStatistics.get(statKeys.nextElement());
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
				|| (key.equals(MOVINGRATE))
				|| (key.equals(HITRATE)
				|| (key.equals(CLASSIFICATIONLINE))
				|| (key.equals(COMMUNICATIONLINE)))){
				result = (result / ((double)values.size()));
			}
			// Ansonsten Summenwerte
			m_statisticResult.put(key, new Double(result));
		}
		// Eintragen des berechneten Wertes.
		m_statisticResult.put(
			REALLYVISITEDPLACES, 
			new Double((double)m_reallyVisitedPlaces));
		// Berechne das Rating.
		this.calculateRating();
	}

	/* (non-Javadoc)
	 * @see simulator.statistic.IStatisticComponent#addInformation(java.lang.String, java.lang.Object)
	 */
	public void addInformation(Id agentId, String infoName, Object value)
		throws InvalidAgentException,
				InvalidElementException {		
		if(m_agentStatistics.containsKey(agentId.toString())) {
			((IslandAgentStatistic)m_agentStatistics.get(
				agentId.toString())).addInformation(
					infoName,
					value);	
		}else {
			throw new InvalidAgentException(agentId.toString());
		}
	}
	
	/**
	 * Erfasse einen Statistischen Wert für das Team.
	 * 
	 * @param infoName
	 * @param value
	 * @throws InvalidElementException
	 */
	public void addTeamInformation(String infoName, Object value) 
		throws InvalidElementException {	
				
		if(infoName.equals(REALLYVISITEDPLACES)) {
			if(value instanceof Integer) {
				int newValue = ((Integer)value).intValue();
				m_reallyVisitedPlaces = m_reallyVisitedPlaces + newValue;	
			}else {
				throw new InvalidElementException();	
			}			
		}	
	}
	
	/* (non-Javadoc)
	 * @see simulator.statistic.IStatisticComponent#getParameters()
	 */
	public Vector getParameters() {
		return m_statisticParameter;
	}
	
	/* (non-Javadoc)
	 * @see simulator.statistic.IStatisticComponent#getCSVDataset()
	 */
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
	
	/**
	 * Berechne das Rating des Agententeams.
	 */
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
		 * Die erreichbare Energie berechnet sich aus den gefundenen 
		 * Energiezellen und den verpaßten Energiezellen.
		 */		
		// Erst einmal die benötigen Werte für die Berechnung bestimmen:
		double foundedCells = 
			((Double)m_statisticResult.get(FOUNDEDCELLS)).doubleValue();
		double missedCells = 
			((Double)m_statisticResult.get(REALLYMISSEDCELLS)).doubleValue();
		double cells = foundedCells + missedCells; 
		double commCosts =
			((Double)m_statisticResult.get(COMMCOSTS)).doubleValue(); 
		double restCosts = 
			((Double)m_statisticResult.get(OTHERCOSTS)).doubleValue(); 
		
		/*
		 * Berechnung des Explorationsrating, d.h. welchen Anteil an der maximal
		 * erreichbaren Energie die Agenten gefunden haben.
		 */ 
		double reachableEnergy = cells * m_cellEnergy;
		double reachedEnergy = foundedCells * m_cellEnergy;
		System.out.println(
			"Ratingberechnung: erreichbare Energie: " 
			+ Double.toString(reachableEnergy));
		System.out.println(
			"Ratingberechnung: gefundene Zellen: " 
			+ Double.toString(foundedCells));
			
		double explorationRating = (reachedEnergy / reachableEnergy) * 100.0;	
		
		/*
		 * Berechnung des Verbrauchsrating, d.h. welchen Anteil an der maximal
		 * verbrauchbaren Energie die Agenten verbraucht haben. 
		 */
		double maxUsableEnergy = 
			((m_startEnergy * m_agentStatistics.size()) + reachableEnergy);
		
		double consumRating = (commCosts + restCosts) / maxUsableEnergy * 100.0;
	
		// Berechnung des kompletten Ratings.
		rating = lernrating + explorationRating - consumRating;
		
		/*
		 * Melden der berechneten Werte.
		 */
		System.out.print("Rating = "); 
		System.out.print(Double.toString(lernrating) + " (Lernrating) + ");
		System.out.print(
			Double.toString(explorationRating)  
			+ " (Explorationrating) - ");
		System.out.print(
			Double.toString(consumRating)
			+ " (Verbrauchsrating)\n");
		System.out.println(
			"Rating = " 
			+ Double.toString(rating) 
			+ " Ratingpunkte");	

		// Eintragen des Wertes in die Tabelle der statistischen Resultate
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
	
	/* (non-Javadoc)
	 * @see simulator.statistic.IStatisticComponent#getParameterPrefix(java.lang.String)
	 */
	public String getParameterPrefix(String parameter) {
		if(m_statisticParameterPrefix.containsKey(parameter)) {
			return (String)m_statisticParameterPrefix.get(parameter);
		}else {
			if(parameter.startsWith(IslandStatisticComponent.LEARNINGRATE)){
				return " %";
			}
		}
		return "";		
	}
	
	/**
	 * Verarbeite die Werte der nächsten Runde.
	 * 
	 * @param agentId
	 * @throws InvalidAgentException
	 */
	public void nextRound(Id agentId) throws InvalidAgentException {
		if(m_agentStatistics.containsKey(agentId.toString())) {
			((IslandAgentStatistic)m_agentStatistics.get(agentId.toString())).nextRound();
		}else {
			throw new InvalidAgentException(agentId.toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see simulator.statistic.IStatisticComponent#getNumOfAgents()
	 */
	public int getNumOfAgents() {
		return m_agentStatistics.size();
	}

}
