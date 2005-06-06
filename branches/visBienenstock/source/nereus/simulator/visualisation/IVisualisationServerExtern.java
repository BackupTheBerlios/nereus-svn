/*
 * Dateiname      : IVisualisationServerExtern.java
 * Erzeugt        : 19. Mai 2005
 * Letzte Änderung: 06. Juni 2005 durch Dietmar Lippold
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
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


package nereus.simulator.visualisation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 * Die Schnittstelle der Server-Vis-Komponente für die Client-Vis-Komponente.
 *
 * @author Samuel Walz
 */
public interface IVisualisationServerExtern extends Remote {

    /**
     * Gibt den Ausschnitt der Informationen zu einem Spiel ab einer 
     * gewünschten Position zurück.
     *
     * @param spielID             ID des gewünschten Spiels
     * @param ausschnittsbeginn   eine ganzzahlige Zahl größer -1 (>0)
     * @return                    eine Liste der gewünschten Informationen
     */
    public LinkedList gibSpielInformationen (int spielID, int ausschnittsbeginn)
        throws RemoteException;
}

