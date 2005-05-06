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
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import speachacts.Speachact;
import utils.Id;
import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;
import exceptions.InvalidGameException;
import exceptions.NotEnoughEnergyException;

/**
 * Das AbstractScenario muss vom Scenarioentwickler überschrieben werden. Es 
 * dient dazu das konkrete Szenario mit Informationen zu versorgen und zu 
 * starten, ohne dass das Spiel die genaue Scenario-Klasse kennen muss. 
 * 
 * @author Daniel Friedrich
 */
public abstract class AbstractScenario implements Serializable {
	
	/**
	 * Informationhandler, managed den Informationsfluss an die Viskomponenten.
	 */
    protected IInformationHandler m_visHandler;
    
    /**
     *  Spielparameter
     */
    protected Hashtable m_parameter;

    /**
     * Agenten des Scenarios
     */
    protected Hashtable m_agents = new Hashtable();
    
    /**
     *  Hashtable in der die aktuelle Energie jedes registrierten Agenten
     *  gespeichert ist.
     */
    protected Hashtable m_agentsEnergy = new Hashtable();
    
    /**
     * Id des Spiels zu dem das Szenario gehört.
     */
	protected Id m_gameId;
	
	/**
	 * Szenariohandler des Szenarios.
	 */
	protected AbstractScenarioHandler m_scenarioHandler = null;
	
	/**
	 * Liste der für das Szenario erlaubten Sprechakte.
	 */
	protected LinkedList m_allowedSpeachacts = new LinkedList();
	
	/**
	 * Konstruktor
	 * 
	 * @param InformationHandler vishandler - InformationHandler 
	 * @param Hashtable parameter - Spielparameter
	 */
	public AbstractScenario(
		Id gameId,
		IInformationHandler visHandler,
        Hashtable parameter){
    	super();
    	m_gameId = gameId;
    	m_visHandler = visHandler;
    	m_parameter = parameter;
    }
    
    /**
	 * Konstruktor.
	 */
	public AbstractScenario(){
    	super();
    }
    
    /**
     * Setzt den Informationhandler beim Szenario.
     * 
	 * @param visHandler
	 */
	public void setVishandler(IInformationHandler visHandler) {
    	m_visHandler = visHandler;
    }
    
    /**
     * Setzt die Spielparameter beim Szenario.
     * 
	 * @param params
	 */
	public void setParameters(Hashtable params) {
    	m_parameter = params;
    }
    
    /**
	 * Simuliert das komplette Spiel. 
	 * 
	 * Ob das Spiel rundenbasiert oder ohne Runden stattfindet, bleibt dem 
	 * Implementierer des konkreten Scenarios überlassen.
	 */
	public abstract void simulateGame() 
		throws InvalidAgentException,
				NotEnoughEnergyException,
				InvalidElementException;
    

    /**
     * Erzeugt einen neuen ScenarioHandler.
     * 
     * Das konkrete Scenario muss hier seinen ScenarioHandler erzeugen. Der 
     * ScenarioHandler wird aus Sicherheitsgründen implementiert. Er soll den 
     * Agenten einen indirekten Zugriff auf das Scenario erlauben, ohne das die 
     * Agenten das Scenario jemals in die Hand bekommen. 
     * 
	 * @return AbstractScenarioHandler
	 */
	public abstract AbstractScenarioHandler createNewScenarioHandler();
    
    /**
     * Liefert den angefragten Parameter zurück.
     * 
	 * @param String name - Name des Parameters, der abgefragt wird.
	 * @return Object - Parameterwert
	 * @throws AgentExecutionException
	 */
	public Object getParameter(Id agentId, String name) 
		throws InvalidElementException{
    	if(m_parameter.containsKey(name)) {
    		return m_parameter.get(name);
    	}else {
    		throw new InvalidElementException(agentId.toString());
    	}
    }
    
    /**
     * Fügt dem Scenario einen neuen Agenten hinzu.
     * 
     * Dabei bekommt ein Agent auch gleich seinen Szenariohandler gesetzt.
     * 
	 * @param AbstractAgent agent - Agent der Hinzugefügt werden soll
	 * @param int energy - Startenergie des Agenten
	 */    
	public void addAgent(
		AbstractAgent agent,
		double energy) throws InvalidAgentException {
		if(m_scenarioHandler == null) {
			this.createNewScenarioHandler();
		}	
			
    	if(!m_agents.containsKey(agent.getId().toString())) {
    		// Setzen des Scenariohandlers
    		agent.setScenario(m_scenarioHandler);
    		// Hinzufügen zur Agentenliste
    		m_agents.put(agent.getId().toString(), agent);
    		// Agenten in die Energieliste aufnehmen
    		m_agentsEnergy.put(agent.getId().toString(), new Double(energy)); 
    	}else {
    		throw new InvalidAgentException(agent.getId().toString());
    	}
    }    
    
    /**
     * Löscht einen Agenten aus dem Scenario.
     * 
	 * @param Id agentId - Id des zu löschenden Agenten.
	 * @throws AgentExecutionException
	 */
	public void removeAgent(Id agentId) throws InvalidAgentException {
    	if(m_agents.containsKey(agentId.toString())) {
    		// Löschen aus der Agentenliste
    		m_agents.remove(agentId.toString());
    		// Löschen aus der Energieliste
    		m_agentsEnergy.remove(agentId.toString());
    	}else {
    		throw new InvalidAgentException(agentId.toString());
    	}
    }
    
    /**
     * Fügt einem Agenten neue Energie hinzu.
     * 
	 * @param Id agentId - Id des Agenten dem die Energie hinzugefügt werden soll.
	 * @param int energy - Energie die hinzugefügt werden soll
	 * @throws AgentExecutionException
	 */
	public synchronized void addEnergyToAgent(
    	Id agentId, 
    	double energy) throws InvalidAgentException {
    	// check ob Agent überhaupt angemeldet ist.	
		if(m_agentsEnergy.containsKey(agentId.toString())) {
			// Hinzufügen der Energie zur Energie des Agenten
			double tmpEnergy = 
				((Double)m_agentsEnergy.get(agentId.toString())).doubleValue();
			// vorsichtshalber alten Eintrag löschen
			//m_agentsEnergy.remove(agentId.toString());
			// neuen Energieeintrag wieder hinzufügen.
			m_agentsEnergy.put(
				agentId.toString(), new Double(tmpEnergy + energy));			
		}else {
			throw new InvalidAgentException(agentId.toString());
		}	
    }
    
    /**
     * Gibt die Energie des Agenten mit der Id agentId zurück.
     * 
	 * @param Id agentId - Id des Agenten.
	 * @return int - Energie des Agenten 
	 * @throws AgentExecutionException
	 */
	public double getEnergyFromAgent(Id agentId) 
		throws InvalidAgentException {
		// check ob Agent überhaupt angemeldet ist.	
		if(m_agentsEnergy.containsKey(agentId.toString())) {
			return ((Double)m_agentsEnergy.get(agentId.toString())).doubleValue();	
		}else {
			throw new InvalidAgentException();
		}
    }
    
    /**
     * Reduziert die Energie des Agenten mit der Id agentId.
     * 
	 * @param Id agentId - Id des Agenten, dessen Energie reduziert werden soll
	 * @param int energy - Wert um den Energie reduziert werden soll
	 * @throws AgentExecutionException
	 */
	public void reduceEnergyFromAgent(
    	Id agentId,
    	double energy ) throws InvalidAgentException {
		// check ob Agent überhaupt angemeldet ist.
		if(m_agentsEnergy.containsKey(agentId.toString())) {
			// Holen des alten Energiestands
			double tmpEnergy = 
				((Double)m_agentsEnergy.get(agentId.toString())).doubleValue();
			// vorsichtshalber alten Eintrag löschen
			//m_agentsEnergy.remove(agentId.toString());
			// neuen reduzierten Energieeintrag wieder hinzufügen.
			m_agentsEnergy.put(
				agentId.toString(), new Double(tmpEnergy - energy));				
		}else {
			throw new InvalidAgentException(agentId.toString());
		}		
    }
    
    /**
     * Liefert boolean zurück, ob der Agent über genügend Energie verfügt.
     * 
	 * @param agentId - Id des kontrollierten Agenten
	 * @param energy -- Mindestmass an Energie das vorhanden sein muss
	 * @return boolean - True aussreichend Energie vorhanden, False nicht
	 * @throws InvalidAgentException
	 */
	public boolean checkEnergy(Id agentId, double energy) 
    	throws InvalidAgentException {
    	if(m_agentsEnergy.containsKey(agentId.toString())) {
    		double energyAmount = 
    			((Double)m_agentsEnergy.get(agentId.toString())).doubleValue();
    		if(energy+0.1 > energyAmount) {
    			return false; 	 	 
    		}else {
    			return true;
    		}    		
    	}else {
    		throw new InvalidAgentException();
    	}
    }
    
	/**
	 * Loggt die Nachricht des Agenten
	 * 
	 * @param Id agentId - Id des Agenten der eine Nachricht loggen will.
	 * @param Id gameId - ID des Spiels zu dem der Agent gehört.
	 * @param String content - Nachricht.
	 * @throws InvalidAgentException
	 */
	public void log(Id agentId, int type,String content)
			throws InvalidAgentException,
					InvalidElementException {
		try {				
			m_visHandler.log(
				agentId, 
				m_gameId,
				type,
				content);
		}catch(InvalidGameException ige) {
			throw new InvalidElementException("Spiel " + m_gameId.toString());
		}
	}
	
	/**
	 * Erzeugt den gewünschten Sprechakt.
	 * 
	 * @param speachact
	 * @return Speachact - erzeugter Speachact.
	 * @throws InvalidActionKeyException
	 */
	public Speachact createSpeachAct(Class speachact) 
		throws InvalidElementException {
		try {
			Constructor cons = 
				speachact.getConstructor(new Class[]{Hashtable.class});
			Object object = cons.newInstance(
				new Object[]{this.copyHashtable(m_parameter)});
			return (Speachact)object;
		}catch(Exception e) {
			throw new InvalidElementException();	
		}
	}
	
	/**
	 * Deep Copy einer Hashtable.
	 * 
	 * @param table
	 * @return Hashtable - Kopie
	 */
	public Hashtable copyHashtable(Hashtable table) {
		Hashtable retval = new Hashtable();
		Enumeration keys = table.keys();
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
			retval.put(key, this.copyEntry(table.get(key)));
		}
		return retval;
	}
	
	/**
	 * Copy eines Parametereintrags.
	 * @param element
	 * @return Object - Kopierter Eintrag
	 */
	public Object copyEntry(Object element) {
		
		if(element instanceof Boolean) {
			return new Boolean(((Boolean)element).booleanValue());
		}
		if(element instanceof String) {
			return new String((String)element);
		}
		if(element instanceof Double) {
			return new Double(((Double)element).doubleValue());
		}
		if(element instanceof Integer) {
			return new Integer(((Integer)element).intValue());
		}
		if(element instanceof Long) {
			return new Long(((Long)element).longValue());
		}
		return null;
	}
	
	/**
	 * Sorgt für einen Reset aller Parameterwerte, wenn ein Spiel erneut simuliert
	 * werden soll.
	 */
	public abstract void reset();

	/**
	 * Gibt eine Liste mit den Parametern, die dass Szenario benötigt zurück.
	 * 
	 * @return LinkedList - Liste der Parameter
	 */
	public abstract LinkedList getScenarioParameter();
	
	/**
	 * Gibt die für das Szenario erlaubten Sprechaktklassen zurück.
	 *   
	 * @param classes
	 * @throws InvalidElementException
	 */
	public LinkedList getListOfAllowedSpeachactClasses() {
		return m_allowedSpeachacts;
	}
	
	/**
	 * Initialisiert die Liste der erlaubten Sprechakte.
	 * 
	 * Jedes Szenario, dass den MessagingServer einsetzen will, muss die
	 * Liste initialisieren.
	 */
	public abstract void initializeListOfAllowedSpeachactClasses();
}



