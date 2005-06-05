/*
 * Dateiname      : AnswerExampleSpeachact.java
 * Erzeugt        : 5. September 2003
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
import java.util.Vector;

import weka.core.Instance;
import weka.core.Instances;
import nereus.exceptions.InvalidElementException;



/**
 * Sprechakt zum Antworten von Beispielen.
 *
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden
 * soll, dann muss der Sprechakt als abstract definiert werden und die
 * Subklassen dann wieder final.
 *
 * @author Daniel Friedrich
 */
public final class AnswerExamplesSpeachact extends AnswerSpeachact {
    
    /**
     * Berechnungsfaktor für Beispiele, wird mit den Basiskosten
     * multipliziert zum Berechnen der Kosten für eine Nachricht.
     */
    private final double m_examplesFactor = 1.0;
    
    /**
     * Parametername zum Durchreichen des Beispielfaktors.
     */
    public final String EXAMPLESFACTOR = "ExamplesFactor";
    
    /**
     * Flag, dass speichert, ob mehr als ein Beispiel geantwortet wird.
     * True - Es werden mehrere Beispiele geantwortet.
     * False - Es wird nur ein Beispiel geantwortet.
     */
    private boolean m_moreThanOneExample = false;
    
    /**
     * Konstruktor.
     *
     * @param parameters
     */
    public AnswerExamplesSpeachact(Hashtable parameters) {
        super(parameters);
    }
    
    /**
     * Setzt ein Flag, dass mehr als ein Beispiel geantwortet wird.
     */
    public void answersMoreThanOneExample(){
        m_moreThanOneExample = true;
    }
    
    /**
     * Gibt an, ob mehr als ein Beispiel geantwortet werden soll.
     *
     * @return boolean True (mehre Beispiele) / False (ein Beispiel)
     */
    public boolean moreThanOneExample() {
        return m_moreThanOneExample;
    }
    
    /**
     * Setzt den Inhalt der Nachricht.
     *
     * Für diesen Sprechakt gibt es die Beschränkung auf Instance, Vector und
     * Instances Objekte. Alle anderen lösen eine InvalidElementException aus.
     *
     * @param Object content
     */
    public void setContent(Object content)
    throws InvalidElementException	{
        if((content == null)
        || (content instanceof Vector)
        || (content instanceof Instance)
        || (content instanceof Instances)) {
            m_content = content;
        }else {
            throw new InvalidElementException();
        }
    }
}
