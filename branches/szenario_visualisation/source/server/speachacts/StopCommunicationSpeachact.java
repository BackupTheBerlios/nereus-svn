/*
 * Created on 03.09.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package speachacts;

import java.util.Hashtable;

/**
 * Der Sprechakt dient dazu Agenten mitzuteilen das sie ihrer Kommunikation 
 * einstellen sollen.
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Fiedrich
 */
public final class StopCommunicationSpeachact extends FacilitationSpeachact {

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public StopCommunicationSpeachact(Hashtable parameters) {
		super(parameters);
	}

}
