/*
 * Created on 13.05.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package simulator;

import java.io.Serializable;

import utils.ActionKey;
import utils.Id;


/**
 * Abstrakte Superklasse, von der alle Agenten über eine beliebig tiefe 
 * Veerbungshierarchie erben müssen.
 * 
 * @author Daniel Friedrich
 */
public abstract class AbstractAgent 
	extends Thread 
	implements Serializable {

    /**
     * Szenariohandler. 
     */
    protected AbstractScenarioHandler m_scenario;
    
    /**
     * Name des Agenten.
     */
	protected String m_name;	
	
	/**
	 * Id des Agenten
	 */
	protected Id m_id;	

	/**
	 * Konstruktor
	 */
	public AbstractAgent() {
		super();
		m_id = new Id();	
	}

	/**
	 * Konstruktor
	 * 
	 * @param String name - Name des Agenten.
	 */
	public AbstractAgent(String name) {
		super();
		m_name = name;
		m_id = new Id();	
		
	}	
	
	/**
	 * Konstruktor
	 * 
	 * @param name - Name des Agenten
	 * @param handler - Szenariohandler
	 */
	public AbstractAgent(String name, AbstractScenarioHandler handler) {
		super();
		m_name = name;
		m_id = new Id();	
		m_scenario = handler;
	
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param id - Id des Agenten
	 * @param name - Name des Agenten
	 * @param handler - Szenariohandler
	 */
	public AbstractAgent(Id id,String name, AbstractScenarioHandler handler) {
		super();
		m_name = name;
		m_id = id;	
		m_scenario = handler;
	
	}

	/**
	 * Konstruktor.
	 * 
	 * @param id - Id des Agenten
	 * @param name - Name des Agenten
	 */
	public AbstractAgent(Id id, String name) {
		super();
		m_name = name;
		m_id = id;
	}	

	/**
	 * Liefert den Namen des Agenten.
	 * 
	 * @return String - Name des Agenten
	 */
	public String getAgentName() {
		return m_name;
	}

	/**
	 * Liefert die Id des Agenten.
	 * 
	 * @return Id - Id des Agenten
	 */
	public Id getId() {
		return m_id;
	}

	/**
	 * Setzt den Namen des Agenten.
	 * 
	 * @param name - Name des Agenten
	 */
	public void setAgentName(String name) {
		m_name = name;
	}

	/**
	 * Setzt das Szenario in dem der Agent operiert.
	 * 
	 * @param scenario - Szenario indem der Agent agiert.
	 */
	public void setScenario(AbstractScenarioHandler scenario) {
		m_scenario = scenario;
	}	
}


