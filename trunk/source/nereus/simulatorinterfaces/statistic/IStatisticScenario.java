/*
 * Dateiname      : IStatisticScenario.java
 * Erzeugt        : 4. August 2003
 * Letzte Änderung:
 * Autoren        : Daniel Friedrich
 *
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
package nereus.simulatorinterfaces.statistic;

import java.util.Hashtable;
import java.util.Vector;


/**
 * Die Schnittstelle definiert die Methoden, die ein Szenario implementieren
 * muss, wenn es für es eine Statistik erstellt werden soll.
 *
 * @author Daniel Friedrich
 */
public interface IStatisticScenario {
    
    /**
     * Erstellt eine neue szenariospezifische Statistikkomponente.
     *
     * Das Szenario muss eine Statistikkomponente erstellen, die der
     * Schnittstelle IStatisticComponent genügt. Die Komponente ist dazu da,
     * während des Ablaufs des Spiels eigenständig die statistischen Daten zu
     * erfassen und auszuwerten.
     *
     * @param agents - Menge der Agenten
     * @return IStatisticComponent - Statistikkomponente des Szenarios.
     */
    public IStatisticComponent createNewStatisticComponent(Vector agents);
    
    /**
     * Gibt eine Hashtable zurück in der die Parameter aufgelistet sind, die statistisch erfasst werden.
     *
     * @return Hashtable - Hashtable mit den zu erfassenden Parametern
     */
    public Hashtable getEnviromentStatisticParameters();
}
