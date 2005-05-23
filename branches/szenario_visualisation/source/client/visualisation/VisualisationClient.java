/*
 * Dateiname      : VisualisationClient.java
 * Erzeugt        : 19. Mai 2005
 * Letzte Änderung: 22. Mai 1005 durch Samuel Walz
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

import java.rmi.Naming;
import source.server.visualisation.IVisualisationServerExtern;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author  sam
 */
public class VisualisationClient {
    
    private static String serverAdresse = "127.0.0.1:1099/VisualisationServer";
    
    private static int spielID = 0;
    
    private int ausschnittsbeginn = 0;
    
    private LinkedList spielinformationen = new LinkedList();
    
    /** 
     * Creates a new instance of VisualisationClient 
     */
    public VisualisationClient(String adresse, int spiel) {
        serverAdresse = adresse;
        spielID = spiel;
        kontaktiereServer();
    }
    
    public VisualisationClient(int spiel) {
        spielID = spiel;
        kontaktiereServer();
    }
    
    public void kontaktiereServer() {
        try {
             IVisualisationServerExtern visServer = 
                (IVisualisationServerExtern) Naming.lookup("rmi://" 
                                                       + serverAdresse);
             
            Visualisation visClient = new Visualisation();
            visClient.run();
        
            //Abholen der Spiel-Informationen am Server
            while (visClient.isAlive()) {
                LinkedList informationen = 
                    visServer.gibSpielInformationen(spielID, ausschnittsbeginn);
            
                ListIterator listenlaeufer = informationen.listIterator();
                /*
                * Wird eine leere Liste übergeben, so wird auf weitere
                * Informationen gewartet.
                */
                if (! listenlaeufer.hasNext()) {
                    synchronized (visServer) {
                        visServer.wait(2000);
                    }
                } else {
                    //Weitergeben der Informationen an die Visualisierung
                    while (listenlaeufer.hasNext()) {
                        visClient.visualisiere(listenlaeufer.next());
                        ausschnittsbeginn = ausschnittsbeginn + 1;
                    }
                
                }
            }
        } catch(Exception fehler) {
            System.out.println(fehler.getMessage());
        }
    }
    
    public static void main(String args[]) {
        System.out.println("Client ist gestartet...");
        if (args.length >= 2) {
            VisualisationClient unserClient = new VisualisationClient(args[0], 
                                                 Integer.parseInt(args[1]));
            //unserClient.kontaktiereServer();
        } else {
            System.out.println("Bitte geben Sie Serveradresse, Port, Servername"
                    + " und die Spiel-ID an.\n"
                    + "(z.B.: 127.0.0.1:1099/VisualisationServer 23)");
        }
        System.out.println("...Client wurde beendet.");
    }
    
}
