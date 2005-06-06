/*
 * Dateiname      : VisualisationServer.java
 * Erzeugt        : 19. Mai 2005
 * Letzte �nderung: 06. Juni 2005 durch Dietmar Lippold
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


package nereus.simulator.visualisation;

import java.util.Random;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.io.Serializable;

import nereus.exceptions.DoppeltesSpielException;
import nereus.exceptions.AuthentifizierungException;
import nereus.simulatorinterfaces.IVisualisationServerIntern;
import nereus.simulatorinterfaces.IVisualisationServerExtern;

/**
 * Speichert die Informationen aller Spiele die f�r eine Visualisierung
 * notwendig sein k�nnten und gibt sie auf Anfrage an interessierte
 * Clients weiter.<P>
 * 
 * Die ankommenden Informationen der laufenden Spiele werden
 * sortiert, nach der ID des zugeh�rigen Spieles und der Reihenfolge
 * in der sie von einem Spiel angeliefert werden, abgespeichert.
 * Hierzu werden die Informationen eines Spiels mit der Zahl Null 
 * beginnend und aufsteigend durchnummeriert.
 * Ein interessierter Client gibt dann an, an welchen Informationen
 * er Interesse hat, indem er die ID des Spieles und den Ausschnittsbeginn
 * angibt. Er bekommt dann in einer Liste alle Informationen zur�ck,
 * deren Nummer gr��er oder gleich dem gew�nschten Ausschnittsbeginn ist.
 *
 * @author Samuel Walz
 */
public class VisualisationServer extends UnicastRemoteObject
    implements IVisualisationServerIntern, IVisualisationServerExtern {

    // Die Informationen der Spiele, sortiert nach der Spiel-ID.
    private static HashMap informationsspeicher = new HashMap();

    // Die empfohlenen Wartezeiten f�r die Client-Vis-Komponenten
    private static HashMap wartezeiten = new HashMap();

    // Die Zuordnung der Authentifizierungscodes zu den Spiel-IDs.
    private static HashMap authZuordnung = new HashMap();

    // Die Standardwartezeit in Millisekunden
    private static int standardwartezeit = 2000;

    //
    private static long informationshaltbarkeit = (1000 * 60 * 60);

    /*
     * Die angemeldeten Client-Vis-Komponenten, sortiert nach den Spielen,
     * f�r die sie sich interessieren.
     */
    // Zum Erzeugen zuf�lliger Werte
    private final long samen = System.currentTimeMillis();

    private Random zufallsGenerator = new Random();


    /** 
     * Creates a new instance of VisualisationServer 
     */
    public VisualisationServer() throws RemoteException{
        // Zufallsgenerator initialisieren
        zufallsGenerator.setSeed(samen);
        
        System.out.println("Melde den VisualisationServer an...");
        UnicastRemoteObject.unexportObject((Remote)this, true);
        UnicastRemoteObject.exportObject((Remote)this);
        try {
            Naming.rebind("VisualisationServer", this);
        } catch (MalformedURLException fehler) {
            System.out.println(fehler.getMessage());
        }
    }

    /**
     * Gibt Fehlermeldungen aus.
     *
     * @param methodenname           Name der Methode in der der Fehler auftritt
     * @param fehlerbeschreibung     Eine grobe Beschreibung des Fehlers
     */
    private void gibFehlerAus(String methodenname, String fehlerbeschreibung) {
        System.out.println("Server-Vis-Komponente : " + methodenname + " : \n"
                + "    " + fehlerbeschreibung);
    }

    /**
     * Gibt die zu einem Authentifizierungscode geh�rende Spiel-ID zur�ck.
     *
     * Wird eine Exception geworfen, so war der Authentifizierungscode ung�ltig.
     *
     * @param authCode
     * @return           Eine ganze Zahl gr��er -1
     */
    private int gibSpielID (long authCode) throws AuthentifizierungException {

        if (authZuordnung.containsKey(new Long(authCode))) {
            return ((Integer)authZuordnung.get(new Long(authCode))).intValue();
        } else {
            throw new AuthentifizierungException("Falscher Authentifizierungscode: " 
                                + authCode);
        }
    }

    /**
     * Erzeugt einen Athentifizierungscode, der eindeutig einer Spiel-ID
     * zugeordnet und zuf�llig generiert ist und speichert diese Zuordnung
     * ab.
     *
     * Diese Methode ist synchronisiert, damit bei einem parallelen Aufruf
     * nicht zuf�llig mehrfach der selbe Code erzeugt werden kann.
     *
     * @param spielID      Die Spiel-ID der der Code zugeordnet werden soll
     */
    private synchronized long authCodeErzeugen(int spielID) {
        
        long authCode = 0L;
        /*
         * Erzeugen eines Authentifizierungscodes, der noch nicht verwendet
         * wird.
         */
        do {
            authCode = zufallsGenerator.nextLong();
        } while (authZuordnung.containsKey(new Long(authCode)));
        
        // Speichern der Zuordung des Code zur Spiel-ID
        authZuordnung.put(new Long(authCode), new Integer(spielID));
        
        return authCode;
    }

    /**
     * Erstellt eine neue Liste, die den Inhalt der �bergebenen ab einer
     * bestimmten Position enth�lt.
     *
     * @param liste                Eine Liste
     * @param ausschnittsbeginn    nat�rliche Zahl gr��er Null
     * @return                     der gew�nschte Ausschnitt
     */
    private LinkedList erstelleAusschnitt (LinkedList liste, 
                                           int ausschnittsbeginn) {
        
        LinkedList ausschnitt = new LinkedList();                                       
                                               
        if (liste.size() >= ausschnittsbeginn) {
            //return liste.subList(ausschnittsbeginn, liste.size());
            ListIterator listenlaeufer = liste.listIterator(ausschnittsbeginn);
            
            while (listenlaeufer.hasNext()) {
                ausschnitt.addLast(listenlaeufer.next());
            }
        } 
        
        return ausschnitt;
    }

    /**
     * L�scht alle Spielinformationen aus dem Speicher, deren Haltbarkeit
     * abgelaufen ist.
     *
     * @param haltbarkeit             eine positive Zahl
     */
    private void bereinigeInformationsspeicher(long haltbarkeit) {
        // Aktuelle Zeit
        long zeit = System.currentTimeMillis();
        
        synchronized (informationsspeicher) {
            // Alle Informationslisten durchgehen
            Set alleSpiele = informationsspeicher.keySet();
            Iterator spiellaeufer = alleSpiele.iterator();
            while (spiellaeufer.hasNext()) {
                Object spielID = spiellaeufer.next();
                LinkedList informationen = 
                        (LinkedList)informationsspeicher.get(spielID);
                
                // Wenn die Liste nicht leer ist
                if (! informationen.isEmpty()) {
                    Object letzterEintrag = informationen.getLast();
            
                    // Ist das zugeh�rige Spiel zuende?
                    if ((letzterEintrag instanceof Spielende)) {
                        /*
                        * Ist die Haltbarkeit abgelaufen, 
                        * so l�sche die Informationen
                        */
                        long eintragszeit = 
                                ((Spielende)letzterEintrag).gibEndZeit();
                        if ((eintragszeit + haltbarkeit) < zeit) {
                            informationsspeicher.remove(spielID);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gibt die Spielinformationen zu einem bestimmten Spiel zur�ck.
     *
     * @param spielID
     * @param ausschnittsbeginn      nat�rliche Zahl gr��er -1
     */
    public LinkedList gibSpielInformationen(int spielID, int ausschnittsbeginn) throws RemoteException {
        // Informationen suchen und zur�ckgeben
        if (informationsspeicher.containsKey(new Integer(spielID))) {
            return erstelleAusschnitt(
                (LinkedList)informationsspeicher.get(new Integer(spielID)), 
                ausschnittsbeginn);
        } else {
            // Sind noch keine Informationen vorhanden, wird null zur�ckgegeben
            return new LinkedList();
        }
    }

    /**
     * Speichert die Spielinformationen der einzelnen Spiele.
     *
     * @param authCode
     * @param information      die zu speichernde Information
     */
    public void speichereSpielInformationen(long authCode, 
                                            Serializable information) {
        try {
            // Die zugeh�rige Spiel-ID
            int spielID = gibSpielID(authCode);
        
            // Die neue Information an die Liste anh�ngen
            LinkedList informationen = 
                    (LinkedList)informationsspeicher.get(new Integer(spielID));
            informationen.addLast(information);
            
        } catch(Exception fehler) {
            
            gibFehlerAus("speichereSpielInformationen", 
                         fehler.getMessage());
        }
    }

    /**
     * Meldet ein Spiel f�r die Informationsspeicherung an.
     * 
     * Wird als Wartezeit 0 angegeben, so wird der Defaultwert verwendet.
     *
     * @param spielID        nat�rliche Zahl
     * @param wartezeit      nat�rliche Zahl >= null (Zeit in Millisekunden)
     * @return               der Authentifizierungscode
     */
    public long spielAnmelden(int spielID, int wartezeit) 
        throws DoppeltesSpielException{
        
        if (! informationsspeicher.containsKey(new Integer(spielID))) {
            // Eine neue Liste f�r die Informationen des Spiels anlegen
            informationsspeicher.put(new Integer(spielID), new LinkedList());
            
            // Die empfohlene Wartezeit eintragen
            if (wartezeit == 0) {
                wartezeiten.put(new Integer(spielID), 
                                new Integer(standardwartezeit));
            } else {
                wartezeiten.put(new Integer(spielID), 
                                new Integer(wartezeit));
            }
            
            // Spielinformationsbereinigung starten
            bereinigeInformationsspeicher(informationshaltbarkeit);
            
            // Den zugeh�rigen Authentifizierungscode zur�ckgeben
            return authCodeErzeugen(spielID);
            
        } else {
            // Hat das Anmelden nicht geklappt, wird ein Fehler geworfen
            throw new DoppeltesSpielException("Ein Spiel mit der ID " + spielID
                        + " ist schon angemeldet!");
        }
    }

    /**
     * Meldet ein Spiel f�r die Informationsspeicherung ab.
     *
     * @param authCode
     */
    public void spielAbmelden(long authCode) {
        try {
            int spielID = gibSpielID(authCode);
            
            // Spielende markieren
            LinkedList informationen = 
                    (LinkedList)informationsspeicher.get(new Integer(spielID));
            
            informationen.addLast(new Spielende(System.currentTimeMillis()));
            
            // Den Wartezeiteintrag f�r das Spiel l�schen
            wartezeiten.remove(new Integer(spielID));
            
            
        } catch (Exception fehler) {
            // Falscher Authentifizierungscode!
            gibFehlerAus("spielAbmelden", 
                    "Falscher Authentifizierungscode: " + authCode);
        }
    }
}

