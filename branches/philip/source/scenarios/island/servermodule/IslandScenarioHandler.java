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
package scenario.island;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import simulator.AbstractScenario;
import simulator.AbstractScenarioHandler;
import simulator.IInformationHandler;
import speachacts.Speachact;
import utils.ActionKey;
import utils.Id;
import weka.core.Instance;
import exceptions.InvalidActionKeyException;
import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;
import exceptions.InvalidMessageException;
import exceptions.InvalidSpeachactException;
import exceptions.NotEnoughEnergyException;

/**
 * Der IslandScenarioHandler sorgt für den sicheren Zugriff auf das Szenario. 
 * Dafür bietet der ScenarioHandler alle Methoden an, die das Insel-Szenario 
 * den Agenten anbietet. Diese Methodenmenge ist in der Schnittstelle 
 * IIslandScenarioHandler definiert.
 * 
 * @author Daniel Friedrich
 */
public class IslandScenarioHandler 
	extends AbstractScenarioHandler 
	implements IIslandScenarioHandler {	
	
	/**
	 * Konstruktor.
	 * 
	 * @param scenario - Insel-Szenario
	 * @param infoHandler - Informationshandler 
	 * @param gameId - Id des Spiels.
	 * @param parameters - Parameter des Spiels
	 */
	public IslandScenarioHandler(
		AbstractScenario scenario,
		IInformationHandler infoHandler,
		Id gameId,
		Hashtable parameters) {
		super();
		m_scenario = scenario;	
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param scenario - Insel-Szenario
	 */
	public IslandScenarioHandler(
		AbstractScenario scenario) {
		m_scenario = scenario;
	}

	
	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioHandler#getAttributesFromPlace(utils.ActionKey, utils.Id)
	 */
	public Instance getAttributesFromPlace(ActionKey key, Id agentId) 
		throws InvalidAgentException,
				InvalidActionKeyException  {
		
		// Identität überprüfen
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch -> Fehler werfen.
			throw new InvalidActionKeyException();
		}			
		return ((IslandScenario)
			m_scenario).getAttributesForPlace(agentId);		
	}

	/* (non-Javadoc)
	 * @see scenario.islandParallel.IIslandScenarioHandler#isPlaceExplored(utils.ActionKey, utils.Id)
	 */
	public boolean isPlaceExplored(ActionKey key, Id agentId) 
		throws InvalidAgentException,
			InvalidActionKeyException  {		
		
		// Identität überprüfen
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch -> Fehler werfen.
			throw new InvalidActionKeyException();
		}		
		return ((IslandScenario)m_scenario).isPlaceExplored(agentId);	
	}
	

	/* (non-Javadoc)
	 * @see simulator.IScenarioHandler#getParameter(utils.ActionKey, utils.Id, java.lang.String)
	 */
	public Object getParameter(ActionKey key, Id agentId, String name ) 
		throws InvalidAgentException,
				InvalidActionKeyException,
				InvalidElementException {
		
		// Identität überprüfen
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch -> Fehler werfen.
			throw new InvalidActionKeyException();
		}				
		return ((IslandScenario)m_scenario).getParameter(agentId,name);			
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IIslandScenarioHandler#getMyEnergyValue(utils.ActionKey, utils.Id)
	 */
	public double getMyEnergyValue(ActionKey key, Id agentId) 
		throws InvalidAgentException,
				InvalidActionKeyException {
		
		// Identität überprüfen		
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch -> Fehler werfen.
			throw new InvalidActionKeyException();
		}				
		return ((IslandScenario)m_scenario).getMyEnergyValue(agentId); 	
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IIslandScenarioHandler#moveTo(utils.ActionKey, utils.Id, java.util.Vector)
	 */
	public Id moveTo(ActionKey key, Id agentId, LinkedList places) 
		throws InvalidAgentException, 
				InvalidActionKeyException,
				InvalidElementException {
		
		// Identität überprüfen		
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch -> Fehler werfen.
			throw new InvalidActionKeyException();
		}			
		return ((IslandScenario)m_scenario).moveTo(agentId,places);
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IIslandScenarioHandler#explorePlace(utils.ActionKey, utils.Id)
	 */
	public boolean explorePlace(ActionKey key, Id agentId) 
		throws InvalidAgentException, 
				InvalidActionKeyException,
				InvalidElementException {
		
		// Identität überprüfen			
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch -> Fehler werfen.
			throw new InvalidActionKeyException();
		}				
		return ((IslandScenario)m_scenario).explorePlace(agentId);
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IIslandScenarioHandler#getPossibleWaysFromPlace(utils.ActionKey, utils.Id)
	 */
	public Vector getPossibleWaysFromPlace(ActionKey key, Id agentId)
		throws InvalidAgentException, 
			   InvalidActionKeyException {
			   	
		// Identität überprüfen					
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch -> Fehler werfen.
			throw new InvalidActionKeyException();
		}	
		/*
		 * Vector kopieren, da bei einer normalen Weitergabe, später die Plätze
		 * aus dem Vector gelöscht werden könnten.
		 */					
		Vector retval = new Vector();
		Vector input = ((IslandScenario)
			m_scenario).getPossibleTargetPlaceForMoving(agentId);	
		Enumeration enum = input.elements();
		while(enum.hasMoreElements()) {
			retval.addElement(enum.nextElement());											
		}
		return retval;
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IIslandScenarioHandler#sendMessage(utils.ActionKey, speachacts.Speachact)
	 */
	public Speachact sendMessage(
		ActionKey key, 
		Speachact act) 
		throws InvalidAgentException, 
				InvalidActionKeyException,
				InvalidMessageException,
				InvalidSpeachactException,
				NotEnoughEnergyException {
		
		// Identität überprüfen					
		if(!((IslandScenario)m_scenario).checkIdentity(
			key, act.getSender())) {
			// Identität falsch -> Fehler werfen.
			throw new InvalidActionKeyException();
		}
		//return m_messagingServer.sendMessage(act);
		return ((IslandScenario)m_scenario).sendMessage(key, act);
	}
		
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IIslandScenarioHandler#log(utils.Id, java.lang.String)
	 */
	public void log(Id agentId, int type, String content)
		throws InvalidAgentException,
				InvalidElementException {		
		// Loggen					
		m_scenario.log(agentId, type, content);
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IIslandScenarioHandler#getPlaceId(utils.ActionKey, utils.Id)
	 */
	public Id getPlaceId(ActionKey key,Id agentId)
		throws InvalidActionKeyException,
				InvalidAgentException {
					
		// Identität überprüfen
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch -> Fehler werfen.
			throw new InvalidActionKeyException();
		}
		return ((IslandScenario)m_scenario).getPlaceId(agentId);				
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IIslandScenarioHandler#getForCommunicationReachableAgents(utils.ActionKey, utils.Id)
	 */
	public Vector getForCommunicationReachableAgents(ActionKey key, Id agentId) 
		throws InvalidActionKeyException,
				InvalidAgentException,
				InvalidElementException {
					
		// Identität überprüfen			
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch -> Fehler werfen.
			throw new InvalidActionKeyException();
		}
		return ((IslandScenario)
			m_scenario).getForCommunicationReachableAgents(agentId);				
	}
					
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IIslandScenarioHandler#energyCellFounded(utils.ActionKey, utils.Id)
	 */
	public boolean energyCellFounded(ActionKey key, Id agentId) 
		throws InvalidActionKeyException {
		
		// Identität überprüfen	
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}
		return ((IslandScenario)m_scenario).energyCellFounded(agentId);
	}
	
	/* (non-Javadoc)
	 * @see scenario.island.IIslandScenarioHandler#analysePlace(utils.ActionKey, utils.Id)
	 */
	public boolean analysePlace(ActionKey key, Id agentId) 
		throws InvalidActionKeyException,
			   InvalidAgentException,
			   InvalidElementException {
		
		// Identität überprüfen
		if(!((IslandScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}
		return ((IslandScenario)m_scenario).analysePlace(agentId);			
	}		
}
