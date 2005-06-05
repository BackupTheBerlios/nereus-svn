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
 * Die Klasse repräsentiert eine Einzelnachricht an den oder die Empfänger.
 * Der Sprechakt ist zur Realisierung von Push-Diensten gedacht, deshalb kann
 * er oder eine seiner Subklassen, nie eine Antwort-Nachricht sein.
 *
 * @author Daniel Friedrich
 */
public abstract class SendSpeachact extends Speachact {
    
    /**
     * Konstruktor.
     *
     * @param parameters
     */
    public SendSpeachact(Hashtable parameters) {
        super(parameters);
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#answerRequired()
         */
    public final boolean answerRequired() {
        return false;
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#isAnswer()
         */
    public final boolean isAnswer() {
        return false;
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#calculateCosts()
         */
    public final double calculateCosts() {
        return
                this.getFactor() * this.getContentSize() * this.getCommCosts();
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#calculateAnswerCosts()
         */
    public final double calculateAnswerCosts() {
        return 0.0;
    }
}
