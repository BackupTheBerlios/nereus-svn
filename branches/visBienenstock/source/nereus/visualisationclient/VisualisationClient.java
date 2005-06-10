/*
 * Dateiname      : VisualisationClient.java
 * Erzeugt        : 19. Mai 2005
 * Letzte Änderung: 10. Juni 2005 durch Dietmar Lippold
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
import nereus.simulatorinterfaces.IVisualisationClient;
import nereus.simulatorinterfaces.IVisualisationServerExtern;
import nereus.simulatorinterfaces.AbstractVisualisation;

/**
 *
 * @author  Samuel Walz
 * @author  Dietmar Lippold
 */
public class VisualisationClient extends Thread implements IVisualisationClient {

    private static Integer wartezeit = new Integer(2000);

    private IVisualisationServerExtern visServer = null;

    private IVisualisationOutput ausgabe;

    private String dienstAdresse;

    private String spielID;

    private int runde;

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
     * @param adresse  Die Adresse der RMI-Registry des Server.
     * @param port     Der Port der RMI-Registry des Server.
     * @param spielId  Die Kennung des Spiels, das visualisiert werden soll.
     * @param runde    Die Runde des Spiels, die visualisiert werden soll.
     */
    public VisualisationClient(String adresse, int port, String spielId,
                               int runde)
        throws MalformedURLException,
               NotBoundException,
               RemoteException {

        this.dienstname = IVisualisationServerExtern.DIENST_NAME;
        this.spielID = spielId;
        this.runde = runde;

        dienstAdresse = "//" + adresse + ":" + port + "/" + dienstname;

        System.out.println("Suche den Server...");
        visServer = (IVisualisationServerExtern) Naming.lookup(serverAdresse);
    }

    /**
     * Registriert eine Visualisierungskomponente beim Spiel.
     *
     * @param ausgabe  Die Komponente, die die zu visualisierenden Daten
     *                 ausgibt.
     */
    public void anmeldung(IVisualisationOutput ausgabe) {
        this.ausgabe = ausgabe;
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
        System.out.println("Client ist gestartet...");
        
            
        try {
             // Die Visualisierung starten
             AbstractVisualisation visualisierung = new Visualisierung();
             visualisierung.start();

             while ((! alleInformationenAbgeholt) && !stop) {
                System.out.println("hole informationen...");
                LinkedList informationen = 
                    visServer.gibSpielInformationen(spielID, ausschnittsbeginn);

                ListIterator listenlaeufer = informationen.listIterator();
                /*
                * Wird eine leere Liste übergeben, so wird auf weitere
                * Informationen gewartet.
                */
                if (! listenlaeufer.hasNext()) {

                    System.out.println("Habe eine leere Liste erhalten...");

                    synchronized (wartezeit) {
                        wartezeit.wait(wartezeit.intValue());
                    }

                } else {

                    System.out.println("Liste enthält Informationen...");
                    //Weitergeben der Informationen an die Visualisierung
                    while (listenlaeufer.hasNext()) {
                        Object information = 
                                (Object)listenlaeufer.next();
      
                        if (! (information instanceof Spielende)) {
                            synchronized (this) {
                                if (!stop) {
                                    visualisierung.visualisiere(information);
                                }
                            }
                            ausschnittsbeginn = ausschnittsbeginn + 1;
                        } else {
                            alleInformationenAbgeholt = true;
                        }
                    }
                 }
             }
        } catch(RemoteException fehler) {
            System.err.println("Verbindungsproblem!\n" + fehler.getMessage());
        } catch (InterruptedException fehler) {
            System.err.println("Warten fehlgeschlagen!\n" + fehler.getMessage());
        }

        System.out.println("Client hat sich beendet...");
    }
}

