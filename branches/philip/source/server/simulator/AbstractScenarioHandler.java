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

import speachacts.Speachact;
import exceptions.InvalidActionKeyException;
import exceptions.InvalidElementException;

/**
 * Der ScenarioHandler wird aus Sicherheitsgründen implementiert. Er soll den 
 * Agenten einen indirekten Zugriff auf das Scenario erlauben, ohne das die 
 * Agenten das Scenario jemals in die Hand bekommen.
 * 
 * @author Daniel Friedrich
 */
public abstract class AbstractScenarioHandler implements IScenarioHandler {
    /**
     * Szenario, dass der Agent schützt
     */
	protected AbstractScenario m_scenario;
	 
	/**
	 * Erzeugt den gewünschten Sprechakt.
	 * 
	 * @param speachactClass - Klasse des gewünschten Sprechaktes
	 * @return Speachact - erstellt Sprechakt.
	 * @throws InvalidActionKeyException
	 */
	public Speachact createSpeachAct(Class speachactClass) 
		throws InvalidActionKeyException,
				InvalidElementException {
		return m_scenario.createSpeachAct(speachactClass);			 
	} 	
}


