/*
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

import scenario.fgml.statistic.FgmlStatisticComponent;
import utils.Id;

/**
 * @author Daniel Friedrich
 *
 * Statistikkomponente für einen Agenten, der am Multiagent-Contest auf der 
 * FGML 2003 in Karlsruhe teilgenommen hat.
 */
public class FgmlAgentStatistic {

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
	
	private double m_cellsFounded = 0.0;
	
	private double m_cellsMissed = 0.0;

	/**
	 * Konstruktor.
	 */
	public FgmlAgentStatistic(
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
	
	public void createStatistic() {
		// Synchronisierung anschalten
		m_actualSyncWait = true;
			
		Enumeration enum = m_statisticValues.keys();
		while(enum.hasMoreElements()) {
			double result = 0.0;
			Object key = enum.nextElement();
			if(key.equals(FgmlStatisticComponent.VISITEDPLACES)) {
				Vector values = (Vector)m_statisticValues.get(
					FgmlStatisticComponent.VISITEDPLACES);
				// Anzahl der besuchten Plätze bestimmen
				result = (double)values.size();	
				// Bewegungsrate berechnen
				double movingRate = (result / ((double)m_actualRound)) * 100.0;
				/*System.out.println(
					"Bewegungsrate von Agent: " 
					+ m_agentId.toString() 
					+ " = " 
					+ Double.toString(movingRate) 
					+ "%");*/
				m_statisticResult.put(
					FgmlStatisticComponent.MOVINGRATE,
					new Double(movingRate));
				m_additionalStatisticParameterPrefixes.put(
					FgmlStatisticComponent.MOVINGRATE,
					" %");
				m_additionalStatisticParameters.addElement(
					FgmlStatisticComponent.MOVINGRATE);							
			}
			if(key.equals(FgmlStatisticComponent.LEARNINGRATE)) {
				Vector values = (Vector)m_statisticValues.get(
					FgmlStatisticComponent.LEARNINGRATE);
				double learningRateAmount = 0.0;	
				for(int i=0; i < values.size(); i++) {
					learningRateAmount = learningRateAmount 
						+ ((Double)values.get(i)).doubleValue();
				}
				// Prozente aus der Summe erstellen
				result = (learningRateAmount / ((double)values.size()));						
			}else if(key.equals(FgmlStatisticComponent.ROUNDCOSTAMOUNT)) {
				Vector values = (Vector)m_statisticValues.get(key);
				double amount = 0.0;
				for(int i=0; i < values.size(); i++) {
					amount = amount + ((Double)values.get(i)).doubleValue();
				}
				// Summe noch teilen, da durchschnittliche Kosten pro Runde
				result = amount / (double)values.size(); 
			}else if(key.equals(FgmlStatisticComponent.MISSEDCELLS)) {
				//Summe berechnen
				Vector values = (Vector)m_statisticValues.get(key);
				for(int i=0; i < values.size(); i++) {
					result = result + ((Double)values.get(i)).doubleValue();
				}
				m_cellsMissed = result;
			}else if(key.equals(FgmlStatisticComponent.FOUNDEDCELLS)) {
				//Summe berechnen
				Vector values = (Vector)m_statisticValues.get(key);
				for(int i=0; i < values.size(); i++) {
					result = result + ((Double)values.get(i)).doubleValue();
				}	
				m_cellsFounded = result;			
			}else if((key.equals(FgmlStatisticComponent.COMMCOSTS)) 
				|| (key.equals(FgmlStatisticComponent.COSTS))
				|| (key.equals(FgmlStatisticComponent.NUMOFMESSAGES))
				|| (key.equals(FgmlStatisticComponent.EXPLORATIONS))
				|| (key.equals(FgmlStatisticComponent.STARTPHASEII))
				|| (key.equals(FgmlStatisticComponent.STARTPHASEIII))) {
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
				
			}/*
			System.out.println(
				"Agent-Stat (" 
				+ m_agentId.toString() 
				+ ") - Parameter " 
				+ key.toString()
				+ " berechnet -> "
				+ Double.toString(result));*/	
			/*
			 * Nur abspeichern, wenn wirklich eine Zahl bei der Berechnung 
			 * heraus kommt, bei der Lernrate können auch NaNs auftreten.
			 */	
			if(!Double.isNaN(result)) {
				m_statisticResult.put(key, new Double(result));					
			}			
		}	
		// Trefferqoute berechnen		
		double hitrate = 
			(m_cellsFounded / (m_cellsFounded + m_cellsMissed)) * 100.0;
		m_statisticResult.put(
			FgmlStatisticComponent.HITRATE, new Double(hitrate)); 
		m_additionalStatisticParameterPrefixes.put(
			FgmlStatisticComponent.HITRATE,
			" %");
		m_additionalStatisticParameters.addElement(
			FgmlStatisticComponent.HITRATE);		
		// Synchronisierung abschalten
		m_actualSyncWait = false;	
	}
	
	public Double getParameter(String parameter) {
		// Wenn grade Synchronisiert wird, warten.
		while(m_actualSyncWait){}		
		if(m_statisticResult.containsKey(parameter)) {
			return (Double)m_statisticResult.get(parameter);
		}
		return null;
	}
	
	public void addInformation(String parameter, Object value) {
		// Wenn grade Synchronisiert wird, warten.
		while(m_actualSyncWait){}	
		if(m_statisticValues.containsKey(parameter)) {
			Vector values = (Vector)m_statisticValues.get(parameter);
			
			if(parameter.equals(FgmlStatisticComponent.LEARNINGRATE)) {
				double dvalue = ((Double)value).doubleValue();
				// Einmal einfach zu den Lernrateneinträgen hinzufügen
				values.addElement(new Double(dvalue));
				/*
				 * Ausserdem einen neuen Parameter Lernrate + Runde anlegen und 
				 * für den Parameter auch den obligatorischen Vektor mit den (in 
				 * diesem Fall nur einem, also "dem") Wert(en) erstellen.
				 */
				String newParameter = FgmlStatisticComponent.LEARNINGRATE 
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
			}else if((parameter.equals(FgmlStatisticComponent.COSTS))
				|| (parameter.equals(FgmlStatisticComponent.COMMCOSTS))) {
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
	
	public Vector getStatisticParameters() {
		// Wenn grade Synchronisiert wird, warten.
		while(m_actualSyncWait){}
		return m_additionalStatisticParameters;
	}	
	
	public void nextRound() {
		// Synchronisation anschalten 
		m_actualSyncWait = true;
		/*
		 * Erst mal die Kosten pro Runde abspeichern, diese wieder auf null 
		 * setzen und dann 
		 */		
		Vector tmp = (Vector)
			m_statisticValues.get(FgmlStatisticComponent.ROUNDCOSTAMOUNT);
		tmp.addElement(new Double(m_costsPerRound));
		m_costsPerRound = 0.0;
		
		m_actualRound++;
		// Synchronisation ausschalten
		m_actualSyncWait = false;
	}
	
	public String getParameterPrefix(String parameter) {
		if(m_additionalStatisticParameterPrefixes.containsKey(parameter)) {
			return (String)m_additionalStatisticParameterPrefixes.get(parameter);
		}
		return " ";
	}
}
