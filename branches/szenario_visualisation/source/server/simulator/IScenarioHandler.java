/*
 * Created on 18.07.2003
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
	 * Liefert einen Wert für einen angefragten Parameter zurück.
	 * 
	 * @param key - Identitätsschlüssel des Agenten
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
	 * Erzeugt den gewünschten Sprechakt.
	 * 
	 * @param speachact - Klasse des gewünschten Sprechakts
	 * @return Speachact - gewünschter Sprechakt.
	 * @throws InvalidActionKeyException
	 */
	public Speachact createSpeachAct(Class speachact) 
		throws InvalidActionKeyException,
			   InvalidElementException;										
}
