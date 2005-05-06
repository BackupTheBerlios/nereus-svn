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
package scenario.fgml;

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
 * @author Daniel Friedrich
 *
 * Szenariohandler für das FGML 2003 Szenario.
 */
public class FgmlScenarioHandler 
	extends AbstractScenarioHandler 
	implements IFgmlScenarioHandler {	
	
	/**
	 * Konstruktor.
	 * 
	 * @param scenario
	 * @param infoHandler
	 * @param gameId
	 * @param parameters
	 */
	public FgmlScenarioHandler(
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
	 * @param scenario
	 */
	public FgmlScenarioHandler(
		AbstractScenario scenario) {
		m_scenario = scenario;
	}

	/**
	 * Liefert die Instanz mit den Attributwerten des Platzes zurück
	 * 
	 * @param agentId
	 * @return Instance - Attribute des Platzes als Weka-Instanz
	 * @throws InvalidAgentException
	 */		
	public Instance getAttributesFromPlace(ActionKey key, Id agentId) 
		throws InvalidAgentException,
				InvalidActionKeyException  {
		if(!((FgmlScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}			
		return ((FgmlScenario)
			m_scenario).getAttributesForPlace(agentId);		
	}

	/* (non-Javadoc)
	 * @see scenario.islandParallel.IFgmlScenarioHandler#isPlaceExplored(utils.ActionKey, utils.Id)
	 */
	public boolean isPlaceExplored(ActionKey key, Id agentId) 
		throws InvalidAgentException,
			InvalidActionKeyException  {
				
		if(!((FgmlScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}		
		return ((FgmlScenario)m_scenario).isPlaceExplored(agentId);	
	}
	
	/* (non-Javadoc)
	 * @see simulator.IScenarioHandler#getParameter(utils.ActionKey, java.lang.String, utils.Id)
	 */
	public Object getParameter(ActionKey key, Id agentId, String name ) 
		throws InvalidAgentException,
				InvalidActionKeyException,
				InvalidElementException {
		if(!((FgmlScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}				
		return ((FgmlScenario)m_scenario).getParameter(agentId,name);			
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IFgmlScenarioHandler#getMyEnergyValue(utils.ActionKey, utils.Id)
	 */
	public double getMyEnergyValue(ActionKey key, Id agentId) 
		throws InvalidAgentException,
				InvalidActionKeyException {
		if(!((FgmlScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}				
		return ((FgmlScenario)m_scenario).getMyEnergyValue(agentId); 	
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IFgmlScenarioHandler#moveTo(utils.ActionKey, utils.Id, java.util.Vector)
	 */
	public Id moveTo(ActionKey key, Id agentId, LinkedList places) 
		throws InvalidAgentException, 
				InvalidActionKeyException,
				InvalidElementException {
		if(!((FgmlScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}			
		return ((FgmlScenario)m_scenario).moveTo(agentId,places);
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IFgmlScenarioHandler#explorePlace(utils.ActionKey, utils.Id)
	 */
	public boolean explorePlace(ActionKey key, Id agentId) 
		throws InvalidAgentException, 
				InvalidActionKeyException,
				InvalidElementException {
		if(!((FgmlScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}				
		return ((FgmlScenario)m_scenario).explorePlace(agentId);
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IFgmlScenarioHandler#getPossibleWaysFromPlace(utils.ActionKey, utils.Id)
	 */
	public Vector getPossibleWaysFromPlace(ActionKey key, Id agentId)
				throws InvalidAgentException, 
						InvalidActionKeyException {
		if(!((FgmlScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}	
		/*
		 * Vector kopieren, da bei einer normalen Weitergabe, später die Plätze
		 * aus dem Vector gelöscht werden.
		 */					
		Vector retval = new Vector();
		Vector input = ((FgmlScenario)
			m_scenario).getPossibleTargetPlaceForMoving(agentId);	
		Enumeration enum = input.elements();
		while(enum.hasMoreElements()) {
			retval.addElement(enum.nextElement());											
		}
		return retval;
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IFgmlScenarioHandler#sendMessage(utils.ActionKey, speachacts.Speachact)
	 */
	public Speachact sendMessage(
		ActionKey key, 
		Speachact act) 
		throws InvalidAgentException, 
				InvalidActionKeyException,
				InvalidMessageException,
				InvalidSpeachactException,
				NotEnoughEnergyException {
					
		if(!((FgmlScenario)m_scenario).checkIdentity(
			key, act.getSender())) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}
		//return m_messagingServer.sendMessage(act);
		return ((FgmlScenario)m_scenario).sendMessage(key, act);
	}
		
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IFgmlScenarioHandler#log(utils.Id, java.lang.String)
	 */
	public void log(Id agentId, int type, String content)
		throws InvalidAgentException,
				InvalidElementException {				
		m_scenario.log(agentId, type, content);
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IFgmlScenarioHandler#getPlaceId(utils.ActionKey, utils.Id)
	 */
	public Id getPlaceId(ActionKey key,Id agentId)
		throws InvalidActionKeyException,
				InvalidAgentException {
	
		if(!((FgmlScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}
		return ((FgmlScenario)m_scenario).getPlaceId(agentId);				
	}
	
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IFgmlScenarioHandler#getForCommunicationReachableAgents(utils.ActionKey, utils.Id)
	 */
	public Vector getForCommunicationReachableAgents(ActionKey key, Id agentId) 
		throws InvalidActionKeyException,
				InvalidAgentException,
				InvalidElementException {
		if(!((FgmlScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}
		return ((FgmlScenario)
			m_scenario).getForCommunicationReachableAgents(agentId);				
	}
					
	/* (non-Javadoc)
	 * @see scenario.islandParallel.IFgmlScenarioHandler#energyCellFounded(utils.ActionKey, utils.Id)
	 */
	public boolean energyCellFounded(ActionKey key, Id agentId) 
		throws InvalidActionKeyException {
		if(!((FgmlScenario)m_scenario).checkIdentity(key,agentId)) {
			// Identität falsch
			throw new InvalidActionKeyException();
		}
		return ((FgmlScenario)m_scenario).energyCellFounded(agentId);
	}
}

