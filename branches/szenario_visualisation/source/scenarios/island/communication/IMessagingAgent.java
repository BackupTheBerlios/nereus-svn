/*
 * Created on 20.06.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package scenario.communication;

import speachacts.Speachact;

/**
 * Das Interface beschreibt die Methode, die ein Agent implementieren muss, 
 * damit er an der Kommunikation über den MessagingServer teilnehmen kann.
 * 
 * @author Daniel Friedrich
 */
public interface IMessagingAgent {
	
	/**
	 * Die Methode muss implementiert sein, damit ein Agent Nachrichten senden
	 * und empfangen kann.
	 * 
	 * @param act - Sprechakt der empfangen wird.
	 * @return Speachact - Antwort auf den empfangenen Sprechakt
	 */
	public Speachact receiveMessage(Speachact act);
}
