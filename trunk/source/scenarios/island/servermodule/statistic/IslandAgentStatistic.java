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

import utils.Id;

/**
 * Statistikkomponente für einen Agenten des Inselszenarios.
 * 
 * @author Daniel Friedrich
 */
public class IslandAgentStatistic {	
	
	/**
	 * Einstellungen des Spiels
	 */
	private Hashtable m_parameter = new Hashtable();

	/**
	 * Tabelle mit den Standard-Parameter, die ausgewertet werden sollen
	 */
	private Vector m_statisticParameter = new Vector();
	
	/**
	 * Tabelle mit Datensätzen zur Berechnung der statistischen Werte
	 */
	private Hashtable m_statisticValues = new Hashtable();
	
	/**
	 * Tabelle mit den Resultaten der statistischen Untersuchung
	 */
	private Hashtable m_statisticResult = new Hashtable();
	
	/**
	 * Tabelle mit den zusätzlich auswertenden statistischen Parametern
	 */
	private Vector m_additionalStatisticParameters = new Vector();
	
	/**
	 * Tabelle mit den Prefixen der zusätzlichen Parameter
	 */
	private Hashtable m_additionalStatisticParameterPrefixes = new Hashtable();
	
	/**
	 * Id des Agenten über den die Statistik geführt wird.
	 */
	private Id m_agentId;
	
	/**
	 * aktuelle Runde
	 */
	private int m_actualRound = 0;
	
	/**
	 * Kosten pro Runde
	 */
	private double m_costsPerRound = 0.0;
	
	/**
	 * Synchronisationsschalter
	 */
	private boolean m_actualSyncWait = false;
	
	/**
	 * Anzahl der besuchten Plätze
	 */
	private int m_visitedPlaces = 0;
	
	/**
	 * Anzahl der gefundenen Zellen.
	 */
	private double m_cellsFounded = 0.0;
	
	/**
	 * Anzahl, wie oft Energiezellen von den Agenten übersehen wurden.
	 */
	private double m_cellsMissed = 0.0;
	
	/**
	 * Anzahl der wirklich gefundenen Zellen
	 */
	private double m_reallyCellsMissed = 0.0;


	/**
	 * Konstruktor.
	 * 
	 * @param id - Id des Agenten, für den die Statistik erzeugt wird.
	 * @param parameter - Parameter des Spiels.
	 * @param sParameter
	 */
	public IslandAgentStatistic(
		Id id, 	
		Hashtable parameter, 
		Vector sParameter) {
		super();
		m_agentId = id;
		m_parameter = parameter;
		
		m_statisticParameter = sParameter;
		Enumeration enum = m_statisticParameter.elements();
		while(enum.hasMoreElements()) {
			Object key = enum.nextElement();
			m_statisticValues.put(key, new Vector());
		}
	}
	
	/**
	 * Berechne die Statistik des Agenten.
	 */
	public void createStatistic() {
		// Synchronisierung anschalten
		m_actualSyncWait = true;
			
		Enumeration enum = m_statisticValues.keys();
		while(enum.hasMoreElements()) {
			double result = 0.0;
			Object key = enum.nextElement();
			if(key.equals(IslandStatisticComponent.MOVES)) {
				double moves = 0.0;
				Vector values = (Vector)m_statisticValues.get(
					IslandStatisticComponent.MOVES);
				// Anzahl der besuchten Plätze bestimmen
				moves = (double)values.size();	
				/*
				 * Anzahl der durchgeführten Bewegungen im Verhältnis zu den
				 * maximal durchführbaren Bewegungen, d.h. 
				 * 1 Bewegung * Rundenanzahl
				 */
				double maxPossibleMoves = (double)m_actualRound;   
				if(m_parameter.containsKey("RoundLimit")) {
					int roundLimit = ((Integer)m_parameter.get("RoundLimit")).intValue();
					maxPossibleMoves = (double)roundLimit;  	
				} 
				double movingRate = moves / maxPossibleMoves * 100.0;
				m_statisticResult.put(
					IslandStatisticComponent.MOVINGRATE,
					new Double(movingRate));
				m_additionalStatisticParameterPrefixes.put(
					IslandStatisticComponent.MOVINGRATE,
					" %");
				m_additionalStatisticParameters.addElement(
					IslandStatisticComponent.MOVINGRATE);	
				result = moves;							
			}
			if(key.equals(IslandStatisticComponent.LEARNINGRATE)) {
				Vector values = (Vector)m_statisticValues.get(
					IslandStatisticComponent.LEARNINGRATE);
				double learningRateAmount = 0.0;	
				for(int i=0; i < values.size(); i++) {
					learningRateAmount = learningRateAmount 
						+ ((Double)values.get(i)).doubleValue();
				}
				// Prozente aus der Summe erstellen
				result = (learningRateAmount / ((double)values.size()));						
			}else if(key.equals(IslandStatisticComponent.MISSEDCELLS)) {
				//Summe berechnen
				Vector values = (Vector)m_statisticValues.get(key);
				for(int i=0; i < values.size(); i++) {
					result = result + ((Double)values.get(i)).doubleValue();
				}
				m_cellsMissed = result;
			}else if(key.equals(IslandStatisticComponent.REALLYMISSEDCELLS)) {
				//Summe berechnen
				Vector values = (Vector)m_statisticValues.get(key);
				for(int i=0; i < values.size(); i++) {
					result = result + ((Double)values.get(i)).doubleValue();
				}
				m_reallyCellsMissed = result;
			}else if(key.equals(IslandStatisticComponent.FOUNDEDCELLS)) {
				//Summe berechnen
				Vector values = (Vector)m_statisticValues.get(key);
				for(int i=0; i < values.size(); i++) {
					result = result + ((Double)values.get(i)).doubleValue();
				}	
				m_cellsFounded = result;			
			}else if((key.equals(IslandStatisticComponent.COMMCOSTS)) 
				|| (key.equals(IslandStatisticComponent.OTHERCOSTS))
				|| (key.equals(IslandStatisticComponent.NUMOFMESSAGES))
				|| (key.equals(IslandStatisticComponent.EXPLORATIONS))
				|| (key.equals(IslandStatisticComponent.CLASSIFICATIONLINE))
				|| (key.equals(IslandStatisticComponent.COMMUNICATIONLINE))) {
				// Alles reine Summen
				Vector values = (Vector)m_statisticValues.get(key);
				for(int i=0; i < values.size(); i++) {
					result = result + ((Double)values.get(i)).doubleValue();
				}				
			}else {
				if(((String)key).startsWith("Le")) {
					Vector values = (Vector)m_statisticValues.get(key);
					if(values.size() > 0) {
						result = ((Double)values.get(0)).doubleValue();					
					}
				}
				
			}	
			/*
			 * Nur abspeichern, wenn wirklich eine Zahl bei der Berechnung 
			 * heraus kommt, bei der Lernrate können auch NaNs auftreten.
			 */	
			if(!Double.isNaN(result)) {
				m_statisticResult.put(key, new Double(result));					
			}			
		}	
		/*
		 * Berechnen der Trefferquote. Die Trefferquote beschreibt den Anteil
		 * der Erfolgreichen Grabungen (d.h. die Grabungen, bei denen eine 
		 * Energiezelle gefunden wurde).
		 */	
		double explorations = 
			((Double)m_statisticResult.get(
				IslandStatisticComponent.EXPLORATIONS)).doubleValue(); 
		 
		double hitrate = m_cellsFounded / explorations * 100.0;
		m_statisticResult.put(
			IslandStatisticComponent.HITRATE, new Double(hitrate)); 
		m_additionalStatisticParameterPrefixes.put(
			IslandStatisticComponent.HITRATE,
			" %");
		m_additionalStatisticParameters.addElement(
			IslandStatisticComponent.HITRATE);		
		// Synchronisierung abschalten
		m_actualSyncWait = false;	
	}
	
	/**
	 * Gibt den Wert des gewünschten Statistikparameter zurück.
	 * 
	 * @param parameter - Parameter der Abgefragt wurde
	 * @return Double - Wert des Statistikparameter
	 */
	public Double getParameter(String parameter) {
		// Wenn grade Synchronisiert wird, warten.
		while(m_actualSyncWait){}		
		if(m_statisticResult.containsKey(parameter)) {
			return (Double)m_statisticResult.get(parameter);
		}
		return null;
	}
	
	/**
	 * Fügt der Statistik einen erfassten Wert zu.
	 * 
	 * @param parameter - Parameter für den ein Wert erfasst werden soll.
	 * @param value - der zu erfassende Wert.
	 */
	public void addInformation(String parameter, Object value) {
		// Wenn grade Synchronisiert wird, warten.
		while(m_actualSyncWait){}	
		if(m_statisticValues.containsKey(parameter)) {
			Vector values = (Vector)m_statisticValues.get(parameter);
			
			if(parameter.equals(IslandStatisticComponent.LEARNINGRATE)) {
				double dvalue = ((Double)value).doubleValue();
				// Einmal einfach zu den Lernrateneinträgen hinzufügen
				values.addElement(new Double(dvalue));
				/*
				 * Ausserdem einen neuen Parameter Lernrate + Runde anlegen und 
				 * für den Parameter auch den obligatorischen Vektor mit den (in 
				 * diesem Fall nur einem, also "dem") Wert(en) erstellen.
				 */
				String newParameter = IslandStatisticComponent.LEARNINGRATE 
					+ "_" 
					+ m_actualRound; 
				 
				m_additionalStatisticParameters.addElement(newParameter);
				m_additionalStatisticParameterPrefixes.put(newParameter," %");
				Vector tmp = null; 
				if(m_statisticValues.containsKey(newParameter)) {
					tmp = (Vector)m_statisticValues.get(newParameter);
					tmp.addElement(new Double(dvalue));
				}else {
					tmp = new Vector();
					tmp.addElement(value);
				}
				m_statisticValues.put(newParameter,tmp);
			}else if((parameter.equals(IslandStatisticComponent.OTHERCOSTS))
				|| (parameter.equals(IslandStatisticComponent.COMMCOSTS))) {
				/* 
				 * Kosten zur aktuellen Runde dazu zählen und ansonsten 
				 * das normale Vorgehen
				 */
				 m_costsPerRound = 
				 	m_costsPerRound + ((Double)value).doubleValue();
				 values.addElement(value);
			}else if(value instanceof Integer) {
				values.addElement(new Double(((Integer)value).doubleValue()));
			}else {
				values.addElement(value);
			}
			 
		}		
	}
	
	/**
	 * Liefert die Menge der berechneten Statistikparameter.
	 * 
	 * @return Vector.
	 */
	public Vector getStatisticParameters() {
		// Wenn grade Synchronisiert wird, warten.
		while(m_actualSyncWait){}
		return m_additionalStatisticParameters;
	}	
	
	/**
	 * Erfasse die nächste Runde im Spiel.
	 */
	public void nextRound() {
		// Synchronisation anschalten 
		m_actualSyncWait = true;
		
		m_actualRound++;
		// Synchronisation ausschalten
		m_actualSyncWait = false;
	}
	
	/**
	 * Gibt den Zusatz (z.B. %) des gewünschten Parameters zurück.
	 * 
	 * @param parameter - Parameter
	 * @return String - Prefix
	 */
	public String getParameterPrefix(String parameter) {
		if(m_additionalStatisticParameterPrefixes.containsKey(parameter)) {
			return (String)m_additionalStatisticParameterPrefixes.get(parameter);
		}
		return " ";
	}
}
