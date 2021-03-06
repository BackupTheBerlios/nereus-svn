/*
 * Dateiname      : AbstractScenarioOptional.java
 * Erzeugt        : 13. Mai 2003
 * Letzte �nderung: 
 * Autoren        : Daniel Friedrich
 *                  
 *                  
 *                  
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut f�r Intelligente Systeme
 * der Universit�t Stuttgart unter Betreuung von Dietmar Lippold
 * (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package nereus.simulatorinterfaces;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import nereus.communication.speachacts.Speachact;
import nereus.utils.Id;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidElementException;
import nereus.exceptions.InvalidGameException;
import nereus.exceptions.NotEnoughEnergyException;



/**
 * Das AbstractScenario muss vom Scenarioentwickler �berschrieben werden. Es 
 * dient dazu das konkrete Szenario mit Informationen zu versorgen und zu 
 * starten, ohne dass das Spiel die genaue Scenario-Klasse kennen muss. 
 * 
 * @author Daniel Friedrich
 */
public abstract class AbstractScenarioOptional extends AbstractScenario {	
	/**
	 * Liste der f�r das Szenario erlaubten Sprechakte.
	 */
	protected LinkedList m_allowedSpeachacts = new LinkedList();
	
	/**
	 * Konstruktor
	 * 
	 * @param InformationHandler vishandler - InformationHandler 
	 * @param Hashtable parameter - Spielparameter
	 */
	public AbstractScenarioOptional(
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
	public AbstractScenarioOptional(){
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
     * Liefert den angefragten Parameter zur�ck.
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
     * L�scht einen Agenten aus dem Scenario.
     * 
	 * @param Id agentId - Id des zu l�schenden Agenten.
	 * @throws AgentExecutionException
	 */
	public void removeAgent(Id agentId) throws InvalidAgentException {
    	if(m_agents.containsKey(agentId.toString())) {
    		// L�schen aus der Agentenliste
    		m_agents.remove(agentId.toString());
    		// L�schen aus der Energieliste
    		m_agentsEnergy.remove(agentId.toString());
    	}else {
    		throw new InvalidAgentException(agentId.toString());
    	}
    }
    
    /**
     * F�gt einem Agenten neue Energie hinzu.
     * 
	 * @param Id agentId - Id des Agenten dem die Energie hinzugef�gt werden soll.
	 * @param int energy - Energie die hinzugef�gt werden soll
	 * @throws AgentExecutionException
	 */
	public synchronized void addEnergyToAgent(
    	Id agentId, 
    	double energy) throws InvalidAgentException {
    	// check ob Agent �berhaupt angemeldet ist.	
		if(m_agentsEnergy.containsKey(agentId.toString())) {
			// Hinzuf�gen der Energie zur Energie des Agenten
			double tmpEnergy = 
				((Double)m_agentsEnergy.get(agentId.toString())).doubleValue();
			// vorsichtshalber alten Eintrag l�schen
			//m_agentsEnergy.remove(agentId.toString());
			// neuen Energieeintrag wieder hinzuf�gen.
			m_agentsEnergy.put(
				agentId.toString(), new Double(tmpEnergy + energy));			
		}else {
			throw new InvalidAgentException(agentId.toString());
		}	
    }
    
    /**
     * Gibt die Energie des Agenten mit der Id agentId zur�ck.
     * 
	 * @param Id agentId - Id des Agenten.
	 * @return int - Energie des Agenten 
	 * @throws AgentExecutionException
	 */
	public double getEnergyFromAgent(Id agentId) 
		throws InvalidAgentException {
		// check ob Agent �berhaupt angemeldet ist.	
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
		// check ob Agent �berhaupt angemeldet ist.
		if(m_agentsEnergy.containsKey(agentId.toString())) {
			// Holen des alten Energiestands
			double tmpEnergy = 
				((Double)m_agentsEnergy.get(agentId.toString())).doubleValue();
			// vorsichtshalber alten Eintrag l�schen
			//m_agentsEnergy.remove(agentId.toString());
			// neuen reduzierten Energieeintrag wieder hinzuf�gen.
			m_agentsEnergy.put(
				agentId.toString(), new Double(tmpEnergy - energy));				
		}else {
			throw new InvalidAgentException(agentId.toString());
		}		
    }
    
    /**
     * Liefert boolean zur�ck, ob der Agent �ber gen�gend Energie verf�gt.
     * 
	 * @param agentId - Id des kontrollierten Agenten
	 * @param energy -- Mindestmass an Energie das vorhanden sein muss
	 * @return boolean - True aussreichend Energie vorhanden, False nicht
	 * @throws exceptions.InvalidAgentException
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
	 * @param Id gameId - ID des Spiels zu dem der Agent geh�rt.
	 * @param String content - Nachricht.
	 * @throws exceptions.InvalidAgentException
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
	 * Gibt die f�r das Szenario erlaubten Sprechaktklassen zur�ck.
	 *   
	 * @param classes
	 * @throws exceptions.InvalidElementException
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



