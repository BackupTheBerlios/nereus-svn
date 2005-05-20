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
package source.client.visualisation;

import java.rmi.*;
import source.server.visualisation.IVisualisationServerExtern;
import source.client.visualisation.IVisualisation;
import java.util.LinkedList;

/**
 *
 * @author  sam
 */
public class VisualisationClient extends Thread {
    
    private static String serverAdresse = "127.0.0.1:1099/VisualisationServer";
    
    private static int spielID = 0;
    
    private int ausschnittsbeginn = 0;
    
    /** 
     * Creates a new instance of VisualisationClient 
     */
    public VisualisationClient(String adresse, int spiel) {
        serverAdresse = adresse;
        spielID = spiel;
        start();
    }
    
    public VisualisationClient(int spiel) {
        spielID = spiel;
        start();
    }
    
    public void run() {
        IVisualisationServerExtern visServer = 
            (IVisualisationServerExtern) Naming.lookup("rmi://" 
                                                        + serverAdresse);
        
        IVisualisation visClient = new IVisualisation();
        
        //Abholen der Spiel-Informationen am Server
        while (true) {
            LinkedList informationen = 
                visServer.gibSpielInformationen(spielID, ausschnittsbeginn);
            
            /*
             * Wird eine leere Liste übergeben, so wird auf weitere
             * Informationen gewartet.
             */
            if (informationen.isEmpty()) {
                synchronized (visServer) {
                    visServer.wait();
                }
            } else {
                //Weitergeben der Informationen an die Visualisierung
                
            }
        }
    }
    
    public void main(String args[]) {
        start();
    }
    
}
