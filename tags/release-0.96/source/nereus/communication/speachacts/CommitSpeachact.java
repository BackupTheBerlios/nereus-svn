/*
 * Created on 05.08.2003
 *
 * Part of the Diplomthesis with the title:
 *
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package nereus.communication.speachacts;

import java.util.Hashtable;


/**
 * Der Sprechakt dient zur Bestätigen des Empfangs von Facilitation-Nachrichten.
 *
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden
 * soll, dann muss der Sprechakt als abstract definiert werden und die
 * Subklassen dann wieder final.
 *
 * @author Daniel Friedrich
 */
public final class CommitSpeachact extends FacilitationSpeachact {
    
    /**
     * @param parameters
     */
    public CommitSpeachact(Hashtable parameters) {
        super(parameters);
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#isAnswer()
         */
    public boolean isAnswer() {
        return true;
    }
}
