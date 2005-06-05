/*
 * Dateiname      : AskSpeachact.java
 * Erzeugt        : 29. Juli 2003
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
 * Die Klasse repräsentiert die Superklasse aller Frage-Sprechakte.
 *
 * Abstrakte Hierarchie-Sprechakte dürfen nicht in die Liste der erlaubten
 * Sprechakte beim Szenario aufgenommen werde. Ansonsten wird das Sicherheits-
 * konzept ausgehebelt.
 *
 * @author Daniel Friedrich
 */
public abstract class AskSpeachact extends Speachact {
    
    /**
     * Konstruktor.
     *
     * @param parameters
     */
    public AskSpeachact(Hashtable parameters) {
        super(parameters);
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#answerRequired()
         */
    public boolean answerRequired() {
        return true;
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#isAnswer()
         */
    public boolean isAnswer() {
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
    
    /**
     * Setzt den Inhalt der Nachricht.
     *
     * In einer AskNachricht sind alle Objekte als Inhalt erlaut. Bitte beachten
     * Sie aber dass alle Objekte ausser String, Instance, Instances, Boolean,
     * Id, null und Vectoren mit diesen Objekten die Größe 100 zugerechnet
     * bekommen, was ihren Transport entsprechend teuer macht.
     */
    public final void setContent(Object content)
    throws InvalidElementException {
        m_content = content;
    }
}
