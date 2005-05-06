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
public abstract class AbstractAgentOptional 
	extends AbstractAgent {
	/**
	 * Gibt an ob der Agent beendet werden soll.
	 */
	protected boolean m_gameIsFinished = false;
	
	
	/**
	 * Gibt an, ob der Agent ein lernender Agent ist, d.h. eine eigene Hypothese
	 * bildet und für ihn damit eine Lernrate während dem Spiel berechnet wird.
	 */
	protected boolean m_isALearningAgent = true;
	
	/**
	 * Gibt zurück, ob die Runde gestartet werden kann.
	 */
	private boolean m_canStartRound = false;
	
	/**
	 * aktueller privater ActionKey des Agenten zur Authenifizierung beim 
	 * Szenario (Szenariohandler)
	 */
	private ActionKey m_actionKey; 

	/**
	 * Konstruktor
	 */
	public AbstractAgentOptional() {
		super();
		m_id = new Id();	
	}

	/**
	 * Konstruktor
	 * 
	 * @param String name - Name des Agenten.
	 */
	public AbstractAgentOptional(String name) {
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
	public AbstractAgentOptional(String name, AbstractScenarioHandler handler) {
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
	public AbstractAgentOptional(Id id,String name, AbstractScenarioHandler handler) {
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
	public AbstractAgentOptional(Id id, String name) {
		super();
		m_name = name;
		m_id = id;
	}
	
	/**
	 * Gibt an, ob der Agent ein ausführbarer Agent ist. 
	 * 
	 * Diese Methode muss von jedem Agenten überschrieben werden, ansonsten wird
	 * der betreffende Agent nicht ausgeführt.
	 * 
	 * @return boolean - false, da Superklasse
	 */
	public static boolean isRunableAgent() {
		return false;
	}

	/**
	 * Setzt die Id des Agenten.
	 * 
	 * @param id - Id des Agenten
	 */
	private void setId(Id id) {
		m_id = id;
	}
	
	/**
	 * Liefert den ActionKey des Agenten zurück.
	 * 
	 * Dient zur Synchroniserung des Zugriffs auf den Actionkey. 
	 * 
	 * @return ActionKey - aktueller ActionKey
	 */
	protected synchronized ActionKey getActionKey() {
		// Solange warten bis ein neuer ActionKey da ist.
		while(m_actionKey == null) {}
		// ActionKey auslesen
		ActionKey retval = m_actionKey;
		// ActionKey entfernen
		m_actionKey = null;
		// ActionKey zurückgeben
		return retval;
	}
	
	/**
	 * Über diese Methode setzt das Szenario einen neuen ActionKey.
	 * 
	 * @param key ActioKey - neuer Schlüssel zur Authentifizierung
	 */
	public void setActionKey(ActionKey key) {
		m_actionKey = key;
	}
	
	/**
	 * Beendet die Simulation des Agenten und gibt alle Resourcen frei.
	 */
	public final void finishSimulation() {
		System.out.println("Deaktviere Agent " + m_name);
		m_gameIsFinished = true;
		//this.destroy();
	}
	
	/**
	 * Gibt zurück ob die Runde gestartet werden kann.
	 * 
	 * @return boolean - True  Runde kann beginnen / False noch nicht beginnen
	 */
	protected final boolean canStartRound() {
		return m_canStartRound;
	}
	
	/**
	 * Teilt dem Agenten mit, dass er eine neue Runde starten kann.
	 * 
	 * @param value - gibt an ob gestartet werden soll oder nicht
	 */
	public void startNewRound(boolean value) {
		m_canStartRound = value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#destroy()
	 */
	public void destroy() {
		//this = null;
		this.m_actionKey = null;
		this.m_id = null;
		this.m_name = null;
		this.m_scenario = null;
	}
	
	/**
	 * Gibt zurück, ob der Agent selbst lernt.
	 * 
	 * @return boolean - True Agent ist ein Learning Agent
	 */
	public boolean isALearningAgent() {
		return m_isALearningAgent;
	}
}


