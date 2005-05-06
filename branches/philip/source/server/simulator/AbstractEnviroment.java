/*
 * Created on 04.08.2003
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

import utils.Id;
import exceptions.FullEnviromentException;
import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;

/**
 * Die Schnittstellte definiert alle Methoden, die ein Umwelt anbieten muss,
 * damit sie im Simulator einsetzbar ist.
 * 
 * @author Daniel Friedrich
 */
public abstract class AbstractEnviroment {

	/**
	 * Konstruktor.
	 */
	public AbstractEnviroment() {
		super();
	}
	
	/**
	 * Erstellt die Umwelt.
	 * 
	 * @param graphFile
	 * @param attributeFile
	 * @throws InvalidElementException
	 */
	public abstract void createEnviroment(
		String graphFile, 
		String attributeFile) 
			throws InvalidElementException;
			
	/**
	 * Fügt der Umwelt den Agenten mit der Id agentId hinzu.
	 * 
	 * @param agentId
	 * @throws InvalidAgentException
	 * @throws FullEnviromentException
	 */
	public abstract void addAgentToEnviroment(Id agentId) 
			throws InvalidAgentException,
					FullEnviromentException;		

	/**
	 * Entfernt den Agenten mit der Id agentId aus der Umwelt.
	 * 
	 * @param agentId Id - Id des zu entfernenden Agenten.
	 * @throws InvalidAgentException
	 */
	public abstract void removeAgentFromEnviroment(Id agentId) 
			throws InvalidAgentException;

}
