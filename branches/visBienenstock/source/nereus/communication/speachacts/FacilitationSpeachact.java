/*
 * Dateiname      : FacilitationSpeachact.java
 * Erzeugt        : 5. August 2003
 * Letzte Änderung:
 * Autoren        : Daniel Friedrich
 *
 *
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut für Intelligente Systeme
 * der Universität Stuttgart unter Betreuung von Dietmar Lippold
 * (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package nereus.communication.speachacts;

import java.util.Hashtable;

import nereus.exceptions.InvalidElementException;


/**
 * Superklasse für alle Sprechakte die Verwaltungstätigkeiten beschreiben.
 * Beispiele hierfür sind das Registrieren und Abmelden bei Komponenten.
 *
 * Abstrakte Hierarchie-Sprechakte dürfen nicht als Klasse in der Liste der
 * erlaubte Sprechakte eines Szenarios aufgenommen werden.
 *
 * @author Daniel Friedrich
 */
public abstract class FacilitationSpeachact extends Speachact {
    
    /**
     * Flag, da die Nachricht sowohl eine einfache Nachricht sein kann, wie
     * auch eine Antwort auf eine andere Facilitation Nachrich, z.B bei einem
     * Register.
     */
    private boolean m_isAnswer = false;
    
    /**
     * Konstruktor.
     *
     * @param parameters
     */
    public FacilitationSpeachact(Hashtable parameters) {
        super(parameters);
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#answerRequired()
         */
    public boolean answerRequired() {
        return false;
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#isAnswer()
         */
    public boolean isAnswer() {
        return m_isAnswer;
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#calculateCosts()
         */
    public final double calculateCosts() {
        return this.getFacilitationCosts();
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#calculateAnswerCosts()
         */
    public double calculateAnswerCosts() {
        if(this.haveExtraAnswerCosts()) {
            return this.calculateCosts();
        }
        return 0.0;
    }
    
    /**
     * Eine Facilitation-Nachricht erlaubt kein Setzen eines Inhaltes.
     */
    public void setContent(Object content) throws InvalidElementException {
        if(content != null) {
            throw new InvalidElementException();
        }
        m_content = null;
    }
    
    /**
     * Setzt ob die Nachricht eine Antwort darstellt.
     */
    public void setIsAnswer() {
        m_isAnswer = true;
    }
}
