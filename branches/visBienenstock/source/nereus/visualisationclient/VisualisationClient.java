/*
 * Dateiname      : VisualisationClient.java
 * Erzeugt        : 19. Mai 2005
 * Letzte �nderung: 30. Mai 1005 durch Samuel Walz
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *                  
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

package source.client.visualisation;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.lang.InterruptedException;
import source.server.visualisation.IVisualisationServerExtern;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.io.Serializable;
import source.server.visualisation.Spielende;

/**
 *
 * @author  sam
 */
public class VisualisationClient extends Thread implements IVisualisationClient{
    
    private static String serverAdresse = "127.0.0.1:1099/VisualisationServer";
    
    private static int spielID = 0;
    
    private static Integer wartezeit = new Integer(2000);
    
    private int ausschnittsbeginn = 0;
    
    private LinkedList spielinformationen = new LinkedList();
    
    private boolean alleInformationenAbgeholt = false;
    
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
        System.out.println("Client ist gestartet...");
        try {
            // Die Client-Vis-Komponente an der Server-Vis-Komponente anmelden
             IVisualisationServerExtern visServer = 
                (IVisualisationServerExtern) Naming.lookup("rmi://" 
                                                       + serverAdresse);
             
             // Die empfohlene Wartezeit erfragen
             
             
             // Die Visualisierung starten
             Visualisation visualisierung = new Visualisation();
             visualisierung.start();
             
             while ((! alleInformationenAbgeholt) && visualisierung.isAlive()) {
                System.out.println("hole informationen...");
                LinkedList informationen = 
                    visServer.gibSpielInformationen(spielID, ausschnittsbeginn);
            
                ListIterator listenlaeufer = informationen.listIterator();
                /*
                * Wird eine leere Liste �bergeben, so wird auf weitere
                * Informationen gewartet.
                */
                if (! listenlaeufer.hasNext()) {
                    
                    System.out.println("Habe eine leere Liste erhalten...");
                    
                    synchronized (wartezeit) {
                        wartezeit.wait(wartezeit.intValue());
                    }
                    
                } else {
                    
                    System.out.println("Liste enth�lt Informationen...");
                    //Weitergeben der Informationen an die Visualisierung
                    while (listenlaeufer.hasNext()) {
                        Object information = 
                                (Object)listenlaeufer.next();
      
                        if (! (information instanceof Spielende)) {
                            visualisierung.visualisiere(information);
                            ausschnittsbeginn = ausschnittsbeginn + 1;
                        } else {
                            alleInformationenAbgeholt = true;
                        }
                    }
                
                }
                
             }
        } catch (MalformedURLException fehler) {
            System.out.println("Server-URL fehlerhaft!\n" 
                                + fehler.getMessage());
        } catch (RemoteException fehler) {
            System.out.println("Server nicht gefunden!\n" 
                                + fehler.getMessage());
        } catch (NotBoundException fehler) {
            System.out.println("Server-Vis-Komponente nicht gefunden!\n" 
                                + fehler.getMessage());
        } catch (InterruptedException fehler) {
            System.out.println("Warten fehlgeschlagen!\n" 
                                + fehler.getMessage());
        }
        
        System.out.println("Client hat sich beendet...");
    }
    
    public static void main(String args[]) {
        System.out.println("Client wird gestartet...");
        if (args.length >= 2) {
            VisualisationClient unserClient = new VisualisationClient(args[0], 
                                                 Integer.parseInt(args[1]));
            //unserClient.kontaktiereServer();
        } else {
            System.out.println("Bitte geben Sie Serveradresse, Port, Servername"
                    + " und die Spiel-ID an.\n"
                    + "(z.B.: 127.0.0.1:1099/VisualisationServer 23)");
        }
    }
    
    
    
}
