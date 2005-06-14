/*
 * Dateiname      : VisualisationClient.java
 * Erzeugt        : 19. Mai 2005
 * Letzte Änderung: 14. Juni 2005 durch Dietmar Lippold
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


package nereus.visualisationclient;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.io.Serializable;

import nereus.simulator.visualisation.Spielende;
import nereus.simulatorinterfaces.IVisualisationServerExtern;
import nereus.simulatorinterfaces.AbstractVisualisationClient;
import nereus.simulatorinterfaces.IVisualisationOutput;

/**
 *
 * @author  Samuel Walz
 * @author  Dietmar Lippold
 */
public class VisualisationClient extends AbstractVisualisationClient {

    private static Integer wartezeit = new Integer(2000);

    private IVisualisationServerExtern visServer = null;

    private IVisualisationOutput visualisierung;

    private String dienstAdresse;

    private String visSpielID;
    
    private String visSpielDurchlauf;

    private int ausschnittsbeginn = 0;

    private LinkedList spielinformationen = new LinkedList();

    private boolean alleInformationenAbgeholt = false;

    /** 
     * Gibt an, ob sich der Thread beenden soll.
     */
    private volatile boolean stop = false;

    /** 
     * Erzeugt eine neue Instanz.
     *
     * @param adresse         Die Adresse der RMI-Registry des Server,
     *                        optional mit Angabe des Ports hinter
     *                        Doppelpunkt.
     * @param spielID         Die Kennung des Spiels, das visualisiert werden
     *                        soll.
     * @param spielDurchlauf  Der Durchlauf des Spiels, der visualisiert
     *                        werden soll.
     */
    public VisualisationClient(String adresse, String spielID,
                               String spielDurchlauf)
                            throws MalformedURLException,
                                   NotBoundException,
                                   RemoteException {

        String dienstname = IVisualisationServerExtern.DIENST_NAME;
        this.visSpielID = spielID;
        this.visSpielDurchlauf = spielDurchlauf;

        dienstAdresse = "//" + adresse + "/" + dienstname;

        visServer = (IVisualisationServerExtern) Naming.lookup(dienstAdresse);
    }
    
    
    
    /**
     * Gibt eine Liste aller bereits bekannten Durchläufe zu einem Spiel zurück
     *
     * @param adresse  Die Adresse der RMI-Registry des Server.
     * @param port     Der Port der RMI-Registry des Server.
     * @param spielId  Die Kennung des Spiels, das visualisiert werden soll.
     *
     * @return     eine Liste aller Durchläufe
     */
    public static LinkedList gibDurchlaeufe (String adresse, 
                                             int port, 
                                             String spielID) 
                                        throws MalformedURLException,
                                               NotBoundException,
                                               RemoteException {
                                                 
        String tmpDienstname = IVisualisationServerExtern.DIENST_NAME;
        String tmpDienstAdresse = "//" + adresse + ":" + port + "/" 
                                  + tmpDienstname;
        
        IVisualisationServerExtern tmpVisServer = 
                (IVisualisationServerExtern) Naming.lookup(tmpDienstAdresse);
        
        return tmpVisServer.gibDurchlaeufe(spielID);
              
        
    }

    /**
     * Registriert eine Visualisierungskomponente bei der Client-Vis-Komponente.
     *
     * @param ausgabe  Die Komponente, die die zu visualisierenden Daten
     *                 ausgibt.
     */
    public void anmeldung(IVisualisationOutput ausgabe) {
        this.visualisierung = ausgabe;
    }

     /**
     * Gibt zurück ob das Spiel schon zuende ist.
     *
     * @return     ein boolean-wert
     */
    public boolean spielZuende() {
        return alleInformationenAbgeholt;
    }
    
    /**
     * Startet die Abfrage und Übergabe der zu visualisierenden Daten.
     */
    public void startUebertragung() {
        start();
    }

    /**
     * Beendet die Abfrage und Übergabe der zu visualisierenden Daten.
     */
    public synchronized void stopUebertragung() {
        stop = true;
    }



    public void run() {
            
        try {
             
             while ((! alleInformationenAbgeholt) && !stop) {
                
                // Empfohlene Wartezeit für neue Informationen erfragen
                wartezeit = new Integer(
                                visServer.gibWartezeit(visSpielID,
                                                       visSpielDurchlauf));
                
                LinkedList informationen = 
                    visServer.gibSpielInformationen(visSpielID,
                                                    visSpielDurchlauf,
                                                    ausschnittsbeginn);
                
                /*
                * Wird eine leere Liste übergeben, so wird auf weitere
                * Informationen gewartet.
                */
                if (informationen == null) {
                    
                    System.out.println("visClient: Das Spiel mit der ID " 
                                       + visSpielID
                                       + " ist dem Server noch nicht bekannt");
                    
                    synchronized (wartezeit) {
                        wartezeit.wait(wartezeit.intValue());
                    }
                    
                } else if (informationen.isEmpty()) {

                    System.out.println("visClient: "
                                     + "Keine neuen Informationen erhalten...");

                    synchronized (wartezeit) {
                        wartezeit.wait(wartezeit.intValue());
                    }

                } else {
                    ListIterator listenlaeufer = informationen.listIterator();
                    System.out.println("visClient: "
                                       + "Liste enthält Informationen...");
                    //Weitergeben der Informationen an die Visualisierung
                    while (listenlaeufer.hasNext()) {
                        Object information = 
                                (Object)listenlaeufer.next();
      
                        if (information instanceof Spielende) {
                            alleInformationenAbgeholt = true;
                        } else {
                        
                            synchronized (this) {
                                if (!stop) {
                                    System.out.println("visClient: "
                                            + "übergebe Objekt No. "
                                            + ausschnittsbeginn);
                                    visualisierung.visualisiere(information);
                                }
                            }
                            ausschnittsbeginn = ausschnittsbeginn + 1;
                        }
                        
                            
                    }
                 }
             }
        } catch(RemoteException fehler) {
            System.err.println("Verbindungsproblem!\n" + fehler.getMessage());
        } catch (InterruptedException fehler) {
            System.err.println("Warten fehlgeschlagen!\n" + fehler.getMessage());
        }
    }
}

