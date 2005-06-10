/*
 * Dateiname      : IVisualisationServerExtern.java
 * Erzeugt        : 19. Mai 2005
 * Letzte �nderung: 10. Juni 2005 durch Samuel Walz
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
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


package nereus.simulatorinterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

import nereus.utils.Id;

/**
 * Die Schnittstelle der Server-Vis-Komponente f�r die Client-Vis-Komponente.
 *
 * @author Samuel Walz
 */
public interface IVisualisationServerExtern extends Remote {
    
    /**
     * Der Name unter dem die Server-Vis-Komponente bei RMI angemeldet wird
     */
    public final String VISUALISATIONSERVERNAME = "VisualisationServer";
    
    /**
     * Der Port an dem RMI lauscht
     */
    public final int SERVERPORT = 1099;
    
    /**
     * Die IP-Adresse unseres Servers
     */
    public final String SERVERIP = "127.0.0.1";

    /**
     * Gibt den Ausschnitt der Informationen zu einem Spiel ab einer 
     * gew�nschten Position zur�ck.
     *
     * @param spielID             ID des gew�nschten Spiels
     * @param ausschnittsbeginn   eine ganzzahlige Zahl gr��er -1 (>0)
     * @return                    eine Liste der gew�nschten Informationen
     */
    public LinkedList gibSpielInformationen (String spielID, int ausschnittsbeginn)
        throws RemoteException;
}

