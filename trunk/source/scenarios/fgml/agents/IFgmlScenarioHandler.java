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

import java.util.LinkedList;
import java.util.Vector;

import simulator.IScenarioHandler;
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
 * Das Interface beschreibt die Methoden die der ScenarioHandler für das 
 * Contest-Szenario für die FGML 03 in Karlsruhe implementieren muss.
 */
public interface IFgmlScenarioHandler extends IScenarioHandler{

	public static final String GAMENAME = "GameName";
	public static final String ENERGYFORDIGGING = "EnergyForDigging";
	public static final String ENERGYFORMOVING = "EnergyForMoving";
	public static final String ENERGYFORCOMMUNICATION = "EnergyForCommunication";
	public static final String ROUNDLIMIT = "RoundLimit";
	public static final String ENERGYFOREXISTING = "EnergyForExisting";
	

	public Instance getAttributesFromPlace(ActionKey key, Id agentId) 
		throws InvalidAgentException,
				InvalidActionKeyException;

	/**
	 * Gibt zurück ob der Platz bereits erforscht ist.
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Id agentId - Id des Agenten der die Info benötigt.
	 * 
	 * @return boolean
	 * 
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 */
	public boolean isPlaceExplored(ActionKey key, Id agentId) 
		throws InvalidAgentException,
			InvalidActionKeyException;		
	

	/**
	 * Liefert das aktuelle Energieniveau eines Agenten
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Id agentId - Id des Agenten der die Info benötigt.
	 * 
	 * @return double - Energieniveau
	 * 
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 */
	public double getMyEnergyValue(ActionKey key, Id agentId) 
		throws InvalidAgentException,
				InvalidActionKeyException;	

	/**
	 * Bewegt einen Agenten an einen der Zielplätze.
	 * 
	 * Die Methode dient zur Synchronisation der einzelnen Agenten.
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Id agentId - Id des Agenten der die Info benötigt.
	 * @param Vector places - Menge mit möglichen Zielorten
	 * 
	 * @return Id
	 * 
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 * @throws AgentExecutionException
	 */
	public Id moveTo(ActionKey key, Id agentId, LinkedList places) 
		throws InvalidAgentException, 
				InvalidActionKeyException,
				InvalidElementException;
	

	/**
	 * Gibt an ob ein Platz erforscht worden ist.
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Id agentId - Id des Agenten der die Info benötigt.
	 * 
	 * @return boolean
	 * 
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 * @throws InvalidAttributeOrPlaceNumberException
	 * @throws AgentExecutionException
	 */
	public boolean explorePlace(ActionKey key, Id agentId) 
		throws InvalidAgentException, 
				InvalidActionKeyException,
				InvalidElementException;
				
	/**
	 * Liefert die Menge aller abgehenden Wege vom Platz.
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Id agentId - Id des Agenten der die Info benötigt.
	 * 
	 * @return Vector
	 * 
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 */
	public Vector getPossibleWaysFromPlace(ActionKey key, Id agentId)
		throws InvalidAgentException, 
				InvalidActionKeyException;

	/**
	 * Versendet den Sprechakt. 
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Speechact act - Sprechakt
	 * 
	 * @return Speechact
	 * 
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 * @throws InvalidMessageException
	 * @throws NotEnoughEnergyException
	 * @throws AgentExecutionException
	 */
	public Speachact sendMessage(
		ActionKey key, 
		Speachact act) 
		throws InvalidAgentException, 
				InvalidActionKeyException,
				InvalidMessageException,
				InvalidSpeachactException,
				NotEnoughEnergyException;
				
	/**
	 * Loggt die Übergebene Nachricht für den Agenten.
	 * 
	 * @param Id agentId - Id des Agenten der die Nachricht loggen will.
	 * @param String content - Nachricht die geloggt werden soll.
	 * 
	 * @throws InvalidAgentException
	 * @throws InvalidElementException
	 */
	public void log(Id agentId, int type, String content)
		throws InvalidAgentException,
				InvalidElementException;		
				
				
	/**
	 * Liefert die Id des Platzes an dem sich der Agent gerade befindet.
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Id agentId - Id des Agenten der die Info benötigt.
	 * 
	 * @return Id
	 * 
	 * @throws InvalidActionKeyException
	 * @throws InvalidAgentException
	 */
	public Id getPlaceId(ActionKey key,Id agentId)
		throws InvalidActionKeyException,
				InvalidAgentException;				


	/**
	 * Liefert die Menge an erreichbaren Agenten.
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Id agentId - Id des Agenten der die Info benötigt.
	 * 
	 * @return Vector - Menge der zur Kommunikation erreichbaren Agenten  
	 * 
	 * @throws InvalidActionKeyException
	 * @throws InvalidAgentException
	 * @throws InvalidElementException
	 */
	public Vector getForCommunicationReachableAgents(
		ActionKey key, 
		Id agentId) 
		throws InvalidActionKeyException,
				InvalidAgentException,
				InvalidElementException;	
				
	/**
	 * Gibt zurück ob eine Energiezelle gefunden wurde.
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Id agentId - Id des Agenten der die Info benötigt.
	 * 
	 * @return boolean
	 * 
	 * @throws InvalidActionKeyException
	 */
	public boolean energyCellFounded(ActionKey key, Id agentId)
		throws InvalidActionKeyException;
			
}