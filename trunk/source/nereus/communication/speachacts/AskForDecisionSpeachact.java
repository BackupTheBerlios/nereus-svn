/*
 * Dateiname      : AskForDecisionSpeachact.java
 * Erzeugt        : 29. Juli 2003
 * Letzte �nderung:
 * Autoren        : Daniel Friedrich
 *
 *
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut f�r Intelligente Systeme
 * der Universit�t Stuttgart unter Betreuung von Dietmar Lippold
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


/**
 * Der Sprechakt fragt nach einer Klassifizierung f�r ein Beispiel
 *
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht
 * m�glich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingef�hrt werden
 * soll, dann muss der Sprechakt als abstract definiert werden und die
 * Subklassen dann wieder final.
 *
 * @author Daniel Friedrich
 */
public final class AskForDecisionSpeachact extends AskSpeachact {
    
    /**
     * Berechnungsfaktor f�r Entscheidungen, wird mit den Basiskosten
     * multipliziert zum Berechnen der Kosten f�r eine Nachricht.
     */
    private final double m_decisionFactor = 0.25;
    
    /**
     * Parametername zum Durchreichen des Entscheidungsfaktors.
     */
    public final String DECISIONSFACTOR = "DecisionsFactor";
    
    /**
     * Konstruktor.
     *
     * @param parameters
     */
    public AskForDecisionSpeachact(Hashtable parameters) {
        super(parameters);
    }
}
