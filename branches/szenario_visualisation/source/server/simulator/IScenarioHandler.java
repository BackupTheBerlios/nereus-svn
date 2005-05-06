/*
 * Created on 18.07.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut f�r Intelligente Systeme, Universit�t Stuttgart (2003)
 */
package simulator;

import speachacts.Speachact;
import utils.ActionKey;
import utils.Id;
import exceptions.InvalidActionKeyException;
import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;

/**
 * Die Schnittstelle definiert alle Methoden, die ein Szenariohandler 
 * implementieren muss.
 * 
 * @author Daniel Friedrich
 */
public interface IScenarioHandler {

	/**
	 * Liefert einen Wert f�r einen angefragten Parameter zur�ck.
	 * 
	 * @param key - Identit�tsschl�ssel des Agenten
	 * @param agentId - Id des Agenten
	 * @param name - Name des Parameters
	 * @return Object - Wert des Parameters
	 * @throws InvalidAgentException
	 * @throws InvalidActionKeyException
	 * @throws InvalidElementException
	 */
	public Object getParameter(
		ActionKey key, 
		Id agentId, 
		String name) 
		throws InvalidAgentException,
			InvalidActionKeyException,
			InvalidElementException;	

	/**
	 * Erzeugt den gew�nschten Sprechakt.
	 * 
	 * @param speachact - Klasse des gew�nschten Sprechakts
	 * @return Speachact - gew�nschter Sprechakt.
	 * @throws InvalidActionKeyException
	 */
	public Speachact createSpeachAct(Class speachact) 
		throws InvalidActionKeyException,
			   InvalidElementException;										
}
