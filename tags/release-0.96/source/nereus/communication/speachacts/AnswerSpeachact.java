/*
 * Dateiname      : AnswerSpeachact.java
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
 * Abstrakte Superklasse für Antwortsprechakte.
 *
 * @author Daniel Friedrich
 */
public abstract class AnswerSpeachact extends Speachact {
    
    /**
     * Konstruktor.
     *
     * @param parameters
     */
    public AnswerSpeachact(Hashtable parameters) {
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
        return true;
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#calculateCosts()
         */
    public double calculateCosts() {
        return 0.0;
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#calculateAnswerCosts()
         */
    public final double calculateAnswerCosts() {
        return
                this.getFactor() * this.getContentSize() * this.getAnswerCosts();
    }
    
        /* (non-Javadoc)
         * @see speachacts.Speachact#setContent(java.lang.Object)
         */
    public abstract void setContent(Object content)
    throws InvalidElementException;
}
