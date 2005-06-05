/*
 * Dateiname      : IVisualisation.java
 * Erzeugt        : 17. Juli 2003
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
package nereus.visualisation;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Das Interface definiert die RMI-Methoden zur Aktualisierung der
 * Visualisierungskomponenten auf der Clientseite vom Server aus.
 * Es werden Information zur Energie des Agenten, seiner Lernrate und
 * seiner Kommunikation übergeben.
 *
 * @author Daniel Friedrich
 */
public interface IVisualisation extends Remote {
    
    // Konstante für den übertragenen Parameter.
    public static final int CommMsg = 0;
    public static final int StateMsg = 1;
    public static final int StatisticMsg = 2;
    public static final int LearningMsg = 3;
    
    public static final int LearningRate = 0;
    public static final int EnergyValue = 1;
    
    /**
     * Aktualisiert die grafische Darstellung des Parameters parameter.
     *
     * @param double value
     * @throws RemoteException
     */
    public void updateVisParameter(
            int type,
            double value) throws RemoteException;
    
    /**
     * Aktualisiert die grafische Darstellung des Parameters parameter.
     *
     * @param String value
     * @throws RemoteException
     */
    public void updateVisParameter(
            int type,
            String value) throws RemoteException;
    
    /**
     * Wieder auf Runde 0 zurück gehen.
     * @throws RemoteException
     */
    public void reset() throws RemoteException;
    
}
