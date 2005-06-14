/*
 * Dateiname      : VisualisationServer.java
 * Erzeugt        : 19. Mai 2005
 * Letzte Änderung: 13. Juni 2005 durch Samuel Walz
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
import java.util.TreeMap;
import java.io.Serializable;

import nereus.utils.Id;
import nereus.exceptions.DoppelterDurchlaufException;
import nereus.simulatorinterfaces.IVisualisationServerIntern;
import nereus.simulatorinterfaces.IVisualisationServerExtern;

/**
 * Speichert die Informationen aller Spiele die für eine Visualisierung
 * notwendig sein könnten und gibt sie auf Anfrage an interessierte
 * Clients weiter.<P>
 * 
 * Die ankommenden Informationen der laufenden Spiele werden
 * sortiert, nach der ID des zugehörigen Spieles und der Reihenfolge
 * in der sie von einem Spiel angeliefert werden, abgespeichert.
 * Hierzu werden die Informationen eines Spiels mit der Zahl Null 
 * beginnend und aufsteigend durchnummeriert.
 * Ein interessierter Client gibt dann an, an welchen Informationen
 * er Interesse hat, indem er die ID des Spieles und den Ausschnittsbeginn
 * angibt. Er bekommt dann in einer Liste alle Informationen zurück,
 * deren Nummer größer oder gleich dem gewünschten Ausschnittsbeginn ist.
 *
 * @author Samuel Walz
 */
public class VisualisationServer extends UnicastRemoteObject
    implements IVisualisationServerIntern, IVisualisationServerExtern {

    /**
     * Die Informationen der Spiele, sortiert nach der Spiel-ID.
     */
    private static HashMap informationsspeicher = new HashMap();

    /** 
     * Die empfohlenen Wartezeiten für die Client-Vis-Komponenten
     */
    private static HashMap wartezeiten = new HashMap();
    
    /**
     * Die mimimale Wartezeit in Millisekunden
     */
    private final int MIN_WARTEZEIT = 50;
    
    /**
     * Die Standardwartezeit in Millisekunden (größer als MIN_WARTEZEIT !)
     */
    private final int STANDARDWARTEZEIT = 2000;

    /**
     * Die maximale Menge speicherbarer Spielinformationen
     */
    private final int  MAX_INFORMATIONEN = 1300;
    
    /**
     * Der gewünschte Abstand der Anzahl der gespeicherten Informationen
     * von der Obergrenze MAX_INFORMATIONEN.
     * Sollte maximal 25% von MAX_INFORMATIONEN sein.
     */
    private final int ABSTAND_MAX_INFORMATIONEN = 350;

   

    /** 
     * Creates a new instance of VisualisationServer 
     */
    public VisualisationServer() throws RemoteException{
        
        System.out.println("Melde den VisualisationServer an...");
        UnicastRemoteObject.unexportObject((Remote)this, true);
        UnicastRemoteObject.exportObject((Remote)this);
        
        String visServerAdresse = "//" + SERVER_NAME + ":" + SERVER_PORT + "/"
                                  + DIENST_NAME;
        
        try {
            Naming.rebind(visServerAdresse, this);
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
     * Erstellt eine neue Liste, die den Inhalt der übergebenen ab einer
     * bestimmten Position enthält.
     *
     * @param liste                Eine Liste
     * @param ausschnittsbeginn    natürliche Zahl größer oder gleich Null
     * @return                     der gewünschte Ausschnitt
     */
    private LinkedList erstelleAusschnitt (LinkedList liste, 
                                           int ausschnittsbeginn) {
        
        LinkedList ausschnitt = new LinkedList();                                       
                                               
        if (liste.size() >= ausschnittsbeginn) {
            //return liste.subList(ausschnittsbeginn, liste.size());
            ListIterator listenlaeufer = liste.listIterator(ausschnittsbeginn);
            
            while (listenlaeufer.hasNext()) {
                Object inhalt = listenlaeufer.next();
                if (! (inhalt instanceof Spielanfang)) {
                    ausschnitt.addLast(inhalt);
                }
            }
        } 
        
        return ausschnitt;
    }

    /**
     * Löscht so viele Spielinformationen aus dem Speicher, das die Anzahl der 
     * Informationsobjekte eine gewünschte Entferneung zur Obergrenze hat.
     *
     * Die Informationen zu den Spieldurchläufen werden dem Alter nach gelöscht,
     * bis wieder ausreichend Platz vorhanden ist.
     *
     * @param obergrenze             maximale Anzahl der speicherbaren Objekte
     * @param obergrenzeAbstand      gewünschter Abstand zur Obergrenze
     */
    private void bereinigeInformationsspeicher(int obergrenze,
                                               int obergrenzeAbstand) {
        
        System.out.println(
                "\n\n"
                + "visServer: Bereinigung des Informationsspeichers angestossen:\n"
                + "           Maximale Anzahl zu speichernder Informationsobjekte: "
                + obergrenze + "\n"
                + "           Mindestabstand der Anzahl gespeicherter Objekte von der "
                + "maximalen Anzahl: " + obergrenzeAbstand + "\n"
                + "\n"
                + "           Ermittle den Inhalt des Informationsspeichers...\n"
                + "           [spielID].[spielDurchlauf] - [Informationsobjekte] - "
                + "[Alter]");
        
        synchronized (informationsspeicher) {
            // Speichert alle Spielkennungen nach ihrem Anmelde-Zeitstempel
            TreeMap NachAlterSortiert = new TreeMap();
            
            // Speichert die Anzahl der gescpeicherten Informationsobjekte
            int gespeicherteObjekte = 0;
            
            /*
             * Einfügen aller Spielkennungen und zählen der Informationsobjekte
             */
            Iterator spielIDs = informationsspeicher.keySet().iterator();
            while (spielIDs.hasNext()) {
                Object aktuelleSpielID = spielIDs.next();
                
                HashMap durchlaeufe = 
                        (HashMap)informationsspeicher.get(aktuelleSpielID);
                
                Iterator durchlaufNummern = durchlaeufe.keySet().iterator();
                while (durchlaufNummern.hasNext()) {
                    Object aktuellerSpielDurchlauf = durchlaufNummern.next();
                    
                    LinkedList informationen = 
                           (LinkedList)durchlaeufe.get(aktuellerSpielDurchlauf);
                    
                    if (informationen.getFirst() instanceof Spielanfang) {
                        
                        Spielanfang aktuellerSpielanfang = 
                                       (Spielanfang)informationen.getFirst();
                        int aktuelleInformationsMenge = informationen.size();
                        long aktuelleAnmeldungszeit  = 
                                       aktuellerSpielanfang.gibAnmeldungsZeit();
                        HashMap neueBeschreibung = new HashMap();
                        
                        
                        neueBeschreibung.put("spielID", 
                                             aktuelleSpielID);
                        neueBeschreibung.put("spielDurchlauf", 
                                             aktuellerSpielDurchlauf); 
                        neueBeschreibung.put("informationsMenge",
                                        new Integer(aktuelleInformationsMenge));
                        
                        NachAlterSortiert.put(new Long(aktuelleAnmeldungszeit), 
                                              neueBeschreibung);
                        
                        gespeicherteObjekte = gespeicherteObjekte 
                                              + aktuelleInformationsMenge;
                        
                        System.out.println("visServer: " + aktuelleSpielID + "." 
                                           + aktuellerSpielDurchlauf 
                                           + " - " + aktuelleInformationsMenge
                                           + " St. - "
                                           + aktuelleAnmeldungszeit);
                    }
                }
                
            }
            
           
            /*
             * Löschen von Informationen zu Durchläufen, bis die Anzahl der 
             * gespeicherten Informationsobjekte wieder gering genug ist.
             */
            System.out.println("\n"
                    + "visServer: "
                    + "Loesche Informationen zu den Durchlaeufen...\n"
                    + "           "
                    + "[spielID].[spielDurchlauf] - [Informationsobjekte]");
            Iterator informationsDaten = NachAlterSortiert.keySet().iterator();
            while (informationsDaten.hasNext()
                   && (gespeicherteObjekte + obergrenzeAbstand > obergrenze)) {
                
                HashMap beschreibung = 
                       (HashMap)NachAlterSortiert.get(informationsDaten.next());
                
                Object aktuelleSpielID = 
                        beschreibung.get("spielID");
                Object aktuellerSpielDurchlauf = 
                        beschreibung.get("spielDurchlauf");
                Integer aktuelleInformationsMenge =
                        (Integer)beschreibung.get("informationsMenge");
                
                
                // Löschen der Informationen zum ältesten Durchlauf
                HashMap durchlaeufe = 
                            (HashMap)informationsspeicher.get(aktuelleSpielID);
                durchlaeufe.remove(aktuellerSpielDurchlauf);
                
                /*
                 * Ist die Liste der Durchläufe leer, so kann auch der Eintrag
                 * für das Spiel gelöscht werden.
                 */
                if (durchlaeufe.isEmpty()) {
                    informationsspeicher.remove(aktuelleSpielID);
                }
                
                /*
                 * Abziehen der gelöschten Informationsobjekte von der 
                 * Gesamtanzahl.
                 */
                gespeicherteObjekte = gespeicherteObjekte
                                      - aktuelleInformationsMenge.intValue();
                
                System.out.println("visServer: " + aktuelleSpielID + "." 
                                           + aktuellerSpielDurchlauf 
                                           + " - " + aktuelleInformationsMenge
                                           + " St.");
            }
            
            System.out.println("visServer: "
                    + "Bereinigung des Informationsspeichers abgeschlossen.\n"
                    + "           Verbleibende Informationsobjekte: "
                    + gespeicherteObjekte);
        }
        
    }

    /**
     * Gibt die Spielinformationen zu einem bestimmten Spiel zurück.
     *
     * @param spielID            die ID des gewünschten Spiels
     * @param spielDurchlauf      der gewünschte Durchlauf des Spiels
     * @param ausschnittsbeginn      natürliche Zahl größer -1
     *
     * @return   Eine Liste der gewünschten Spielinformationen
     */
    public LinkedList gibSpielInformationen(String spielID,
                                            String spielDurchlauf,
                                            int ausschnittsbeginn) 
                                            throws RemoteException {
                                                
        String spielKennung = spielID + "." + spielDurchlauf;
        int beginn = ausschnittsbeginn;
        
        System.out.println("visServer: visClient fordert Informationen ab "
                            + "Position " + ausschnittsbeginn + " vom Spiel "
                            + spielKennung + " an.");
        
        // Ausschnittsbeginn korrigieren
        if (beginn < 0) {
            beginn = 0;
        }
        
        // Informationen suchen und zurückgeben
        if (informationsspeicher.containsKey(spielID)) {
            
            HashMap durchlaeufe = (HashMap)informationsspeicher.get(spielID);
            
            if (durchlaeufe.containsKey(spielDurchlauf)) {
                
                return erstelleAusschnitt(
                        (LinkedList)durchlaeufe.get(spielDurchlauf), 
                        beginn);
                
            } else {
               /*
                * Ist der gewünschte Durchlauf eines bekannten Spiels noch nicht
                * vorhanden, so wird eine leere Liste zurückgegeben
                */ 
                return new LinkedList();
            }
        } else {
            /* 
             * Ist ein Spiel mit dieser ID noch nicht bekannt, 
             * so wird null zurückgegeben
             */
            return null;
        }
    }
    
    /**
     * Gibt die empfohlene Wartezeit zwischen den Informationsanfragen zu
     * einem Spiel zurück.
     * 
     * @param spielID       ID des gewünschten Spiels.
     * @parem spielDurchlauf   gewünschter Durchlauf des Spiels
     *
     * @return   Die empfohlene Wartezeit in Millisekunden
     */
    public int gibWartezeit(String spielID,
                            String spielDurchlauf) 
                            throws RemoteException {
        
        String spielKennung = spielID + "." + spielDurchlauf;
        
        // Informationen suchen und zurückgeben
        if (wartezeiten.containsKey(spielKennung)) {
            return ((Integer)wartezeiten.get(spielKennung)).intValue();
        } else {
            // Ist keine Wartezeit vorhanden, standard zurückgeben
            return STANDARDWARTEZEIT;
        }
    }
    
    /**
     * Gibt eine Liste aller bereits bekannten Durchläufe zu einem Spiel zurück
     *
     * @param spielID           ID des gewünschten Spiels
     *
     * @return     eine Liste aller Durchläufe
     */
    public LinkedList gibDurchlaeufe (String spielID) throws RemoteException {
        
        LinkedList spielDurchlaeufe = new LinkedList();
        
        // Suchen der bekannten Durchlaeufe zu einem Spiel
        if (informationsspeicher.containsKey(spielID)) {
            HashMap durchlaeufe = (HashMap)informationsspeicher.get(spielID);
            
            Iterator durchlaufNummern = durchlaeufe.keySet().iterator();
            
            while (durchlaufNummern.hasNext()) {
                spielDurchlaeufe.addLast(durchlaufNummern.next());
            }
        } else {
            gibFehlerAus("gibDurchlaeufe",
                         "Ein Spiel mit der ID" + spielID 
                         + "ist noch nicht bekannt.");
        }
        
        return spielDurchlaeufe;
    }

    /**
     * Speichert die Spielinformationen der einzelnen Spiele.
     *
     * @param spielID        die ID des Spiels
     * @param spielDurchlauf  der Durchlauf des Spiels
     * @param information      die zu speichernde Information
     */
    public void speichereSpielInformation(String spielID,
                                          String spielDurchlauf,
                                          Serializable information) {
        synchronized (informationsspeicher) {                                      
            if (informationsspeicher.containsKey(spielID)) {
            
                // Liste aller Durchlaeufe
                HashMap durchlaeufe = (HashMap)informationsspeicher.get(spielID);
            
                if (durchlaeufe.containsKey(spielDurchlauf)) {    
                
                    // Liste aller Spielinformationen
                    LinkedList informationen = 
                        (LinkedList)durchlaeufe.get(spielDurchlauf);
                
                    if (! (informationen.getLast() instanceof Spielende)) {
                    
                        // Die neue Information an die Liste anhängen
                        informationen.addLast(information);
                    
                    } else {
                        gibFehlerAus("speichereSpielInformationen",
                                    "SpielID: " + spielID 
                                    + " Durchlauf:" + spielDurchlauf
                                    + " es können keine weiteren Informationen"
                                    + " mehr gespeichert werden"
                                    + " (Liste abgeschlossen).");
                    }
                
                } else {
                    gibFehlerAus("speichereSpielInformationen",
                                "SpielID: " + spielID 
                                + " Falscher Durchlauf:" + spielDurchlauf);
                }
            
            } else {
                gibFehlerAus("speichereSpielInformationen", 
                         "Falsche SpielID:" + spielID);
            }
        }
    }

    /**
     * Meldet einen Spieldurchlauf für die Informationsspeicherung an.
     * 
     * Wird als Wartezeit 0 angegeben, so wird der Defaultwert verwendet.
     *
     * @param spielID        die ID des Spiels
     * @param spielDurchlauf  der Durchlauf des Spiels
     * @param wartezeit      natürliche Zahl >= null (Zeit in Millisekunden)
     *
     * @return               der Authentifizierungscode
     */
    public void spielAnmelden(String spielID,
                              String spielDurchlauf) 
                                throws DoppelterDurchlaufException{
                                    
        String spielKennung = spielID + "." + spielDurchlauf;                            
        
        System.out.println("visServer: Ein Spiel versucht sich mit der Kennung "
                           + spielKennung + " anzumelden...");
        
        synchronized (informationsspeicher) {
            if (! informationsspeicher.containsKey(spielID)) {
            
                // Eine neuen Speicher für die Durchläufe des Spiels anlegen
                informationsspeicher.put(spielID, new HashMap());
            }
            
            HashMap durchlaeufe = (HashMap)informationsspeicher.get(spielID);
            
            if (! durchlaeufe.containsKey(spielDurchlauf)) {
                
                // Eine neue Liste für die Informationen eines Durchlaufs anlegen
                durchlaeufe.put(spielDurchlauf, new LinkedList());
            
                // Spielanfang markieren
                LinkedList informationen = 
                                (LinkedList)durchlaeufe.get(spielDurchlauf);
            
                informationen.addFirst(new Spielanfang(System.currentTimeMillis()));
            
                // Standardwartezeit setzen
                wartezeiten.put(spielKennung, 
                               new Integer(STANDARDWARTEZEIT));
           
            } else {
                // Hat das Anmelden nicht geklappt, wird ein Fehler geworfen
                throw new DoppelterDurchlaufException(
                                            "Ein Spiel mit der Kennung " 
                                            + spielKennung
                                            + " ist schon angemeldet!");
            }
            
        
            
        }
        
        // Spielinformationsbereinigung starten
        bereinigeInformationsspeicher(MAX_INFORMATIONEN, 
                                      ABSTAND_MAX_INFORMATIONEN);
    }

    /**
     * Übergibt der Server-Vis-Komponente die Empfohlene Wartezeit für die 
     * Client-Vis-Komponente beim warten auf neue Informationen des 
     * aufrufenden Szenarios.
     *
     * @param spielID        die ID des Spiels
     * @param spielDurchlauf  der Durchlauf des Spiels
     * @param empfohleneWartezeit    die Zeit in Millisekunden
     */
    public void setzeWartezeit(String spielID, 
                               String spielDurchlauf,
                               int empfohleneWartezeit) {
        String spielKennung = spielID + "." + spielDurchlauf;
        
        // Die empfohlene Wartezeit eintragen
        if (empfohleneWartezeit >= MIN_WARTEZEIT) {
            wartezeiten.put(spielKennung, 
                            new Integer(empfohleneWartezeit));
        } else {
            wartezeiten.put(spielKennung, 
                            new Integer(MIN_WARTEZEIT));
        }
    }
    
    /**
     * Meldet einen Spieldurchlauf für die Informationsspeicherung ab.
     *
     * @param spielID        die ID des Spiels
     * @param spielDurchlauf  der Durchlauf des Spiels
     */
    public void spielAbmelden(String spielID, 
                              String spielDurchlauf) {
        String spielKennung = spielID + "." + spielDurchlauf;                          
        
        synchronized (informationsspeicher) {
            if (informationsspeicher.containsKey(spielID)) {
            
                HashMap durchlaeufe = (HashMap)informationsspeicher.get(spielID);
            
                if (durchlaeufe.containsKey(spielDurchlauf)) {
            
                
                // Spielende markieren
                LinkedList informationen = 
                                (LinkedList)durchlaeufe.get(spielDurchlauf);
            
                informationen.addLast(new Spielende(System.currentTimeMillis()));
            
                // Den Wartezeiteintrag für das Spiel löschen
                wartezeiten.remove(spielKennung);
            
                } else {
                    gibFehlerAus("spielAbmelden", 
                                 "SpielID:" + spielID
                                 + " falscher Durchlauf: " + spielDurchlauf);
                }
            
            } else {
                // Falsche Spiel-ID
                gibFehlerAus("spielAbmelden", 
                             "Falsche SpielID:" + spielID);
            }
        }
    }
    
    
}

