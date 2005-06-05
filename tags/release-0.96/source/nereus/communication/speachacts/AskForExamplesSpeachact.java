/*
 * Created on 29.07.2003
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
 * Sprechakt zur Nachfrage nach einem oder mehreren Beispielen.
 *
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden
 * soll, dann muss der Sprechakt als abstract definiert werden und die
 * Subklassen dann wieder final.
 *
 * @author Daniel Friedrich
 */
public final class AskForExamplesSpeachact extends AskSpeachact {
    
    /**
     * Gibt an, ob mehr nach mehr als einen Beispiel nachgefragt wird.
     */
    private boolean m_askForOneExample = false;
    
    /**
     * Konstruktor.
     *
     * @param parameters
     */
    public AskForExamplesSpeachact(Hashtable parameters) {
        super(parameters);
    }
    
    /**
     * Setzt das Flag, dass nach mehr als einem Beispiel nachgefragt wird.
     */
    public void setAskForMoreThanOneExample() {
        m_askForOneExample =true;
    }
    
    /**
     * Gibt an, ob nach mehr als einem Beispiel nachgefragt wird.
     *
     * True - Es wird nach mehr als einem Beispiel nachgefragt.
     * False - Es wird nur nach einem Beispiel nachgefragt.
     *
     * @return boolean
     */
    public boolean askForMoreThanOneExample() {
        return m_askForOneExample;
    }
}
