/*
 * Created on 04.08.2003
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

import java.util.Vector;

import utils.Id;
import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;

/**
 * Die Schnittstelle definiert die Methoden, die eine szenariospezifische 
 * Statistikkomponente erfassen muss.
 * 
 * @author Daniel Friedrich
 */
public interface IStatisticComponent {
	
	/**
	 * Konstante für den Parameter zur Erfassung der Kommunikationskosten.
	 */
	public static final String COMMCOSTS = "Kommunikationskosten";
	/**
	 * Konstante für den Parameter zur Erfassung der Anzahl der übermittelten Nachrichten.
	 */
	public static final String NUMOFMESSAGES = "Anzahl an Nachrichten";
	
	/**
	 * Gibt den ermittelten Wert des Parameter parameter zurück.
	 * 
	 * @param parameter - Name des Parameters.
	 * @return Object - Wert des Parameters.
	 */
	public Object getParameter(String parameter);
		
	/**
	 * Gibt den Zusatz (z.B. % oder Runden) des Parameters parameter zurück.
	 * 
	 * @param parameter - Name des Parameters
	 * @return String - Zusatz
	 */
	public String getParameterPrefix(String parameter);	
	
	/**
	 * Fügt die übergebene Information der statistischen Erfassung hinzu.
	 * 
	 * @param agentId
	 * @param parameter
	 * @param value
	 * @throws InvalidAgentException
	 * @throws InvalidElementException
	 */
	public void addInformation(Id agentId, String parameter, Object value)
		throws InvalidAgentException,
			   InvalidElementException;
	
	/**
	 * Gibt die erfassten Parameternamen zurück.
	 * 
	 * @return Vector mit den Parameternamen
	 */
	public Vector getParameters();
	
	/**
	 * Gibt einen String mit den ermittelten Statistik-Daten im CSV-Format zurück.
	 * 
	 * @return String - Statistik im CSV-Format
	 */
	public String getCSVDataset();
	
	/**
	 * Sorgt dafür, dass die Statistik erstellt wird.
	 */
	public void createStatistic();
	
	/**
	 * Gibt die Anzahl der Agenten zurück, für die die Statistik erfasst wird.
	 * 
	 * @return int - Anzahl an Agenten.
	 */
	public int getNumOfAgents();
}
