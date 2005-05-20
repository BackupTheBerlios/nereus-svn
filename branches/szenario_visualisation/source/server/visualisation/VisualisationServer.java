/*
 * Dateiname      : IVisualisationServer.java
 * Erzeugt        : 19. Mai 2005
 * Letzte Änderung: 19. Mai 1005 durch Samuel Walz
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *                  
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
package source.server.visualisation;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

/**
 *
 * @author Samuel Walz
 */
public class VisualisationServer extends Thread implements IVisualisationServerIntern, IVisualisationServerExtern {
    
    
    
    /** 
     * Creates a new instance of VisualisationServer 
     */
    public VisualisationServer() {
        start();
        UnicastRemoteObject.exportObject(this);
        Naming.rebind("VisualisationServer", this);
    }
    
    public LinkedList gibSpielInformationen(int spielID, int ausschnittsbeginn) {
    }
    
    public void speichereSpielInformationen(java.lang.Object information) {
    }
    
    public void run() {
        
    }
    
}
