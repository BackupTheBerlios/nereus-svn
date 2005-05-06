/*
 * Created on 04.08.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut f�r Intelligente Systeme, Universit�t Stuttgart (2003)
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
	 * Konstante f�r den Parameter zur Erfassung der Kommunikationskosten.
	 */
	public static final String COMMCOSTS = "Kommunikationskosten";
	/**
	 * Konstante f�r den Parameter zur Erfassung der Anzahl der �bermittelten Nachrichten.
	 */
	public static final String NUMOFMESSAGES = "Anzahl an Nachrichten";
	
	/**
	 * Gibt den ermittelten Wert des Parameter parameter zur�ck.
	 * 
	 * @param parameter - Name des Parameters.
	 * @return Object - Wert des Parameters.
	 */
	public Object getParameter(String parameter);
		
	/**
	 * Gibt den Zusatz (z.B. % oder Runden) des Parameters parameter zur�ck.
	 * 
	 * @param parameter - Name des Parameters
	 * @return String - Zusatz
	 */
	public String getParameterPrefix(String parameter);	
	
	/**
	 * F�gt die �bergebene Information der statistischen Erfassung hinzu.
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
	 * Gibt die erfassten Parameternamen zur�ck.
	 * 
	 * @return Vector mit den Parameternamen
	 */
	public Vector getParameters();
	
	/**
	 * Gibt einen String mit den ermittelten Statistik-Daten im CSV-Format zur�ck.
	 * 
	 * @return String - Statistik im CSV-Format
	 */
	public String getCSVDataset();
	
	/**
	 * Sorgt daf�r, dass die Statistik erstellt wird.
	 */
	public void createStatistic();
	
	/**
	 * Gibt die Anzahl der Agenten zur�ck, f�r die die Statistik erfasst wird.
	 * 
	 * @return int - Anzahl an Agenten.
	 */
	public int getNumOfAgents();
}
