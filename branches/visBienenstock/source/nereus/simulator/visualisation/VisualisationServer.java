/*
 * Dateiname      : VisualisationServer.java
 * Erzeugt        : 19. Mai 2005
 * Letzte Änderung: 12. Juni 2005 durch Samuel Walz
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

    // Die Informationen der Spiele, sortiert nach der Spiel-ID.
    private static HashMap informationsspeicher = new HashMap();

    // Die empfohlenen Wartezeiten für die Client-Vis-Komponenten
    private static HashMap wartezeiten = new HashMap();

    // Die Standardwartezeit in Millisekunden
    private static int standardwartezeit = 2000;

    // Die Haltbarkeit gespeicherter Spielinformationen zu einem beendeten Spiel
    private static long informationshaltbarkeit = (1000 * 60 * 60);



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
                ausschnitt.addLast(listenlaeufer.next());
            }
        } 
        
        return ausschnitt;
    }

    /**
     * Löscht alle Spielinformationen aus dem Speicher, deren Haltbarkeit
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
            
                    // Ist das zugehörige Spiel zuende?
                    if ((letzterEintrag instanceof Spielende)) {
                        /*
                        * Ist die Haltbarkeit abgelaufen, 
                        * so lösche die Informationen
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
        
        System.out.println("visServer: visClient fordert Informationen ab "
                            + "Position " + ausschnittsbeginn + " vom Spiel "
                            + spielKennung + " an.");
        // Informationen suchen und zurückgeben
        if (informationsspeicher.containsKey(spielID)) {
            
            HashMap durchlaeufe = (HashMap)informationsspeicher.get(spielID);
            
            if (durchlaeufe.containsKey(spielDurchlauf)) {
                
                return erstelleAusschnitt(
                    (LinkedList)durchlaeufe.get(spielDurchlauf), 
                 ausschnittsbeginn);
                
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
            return standardwartezeit;
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
                                              
        if (informationsspeicher.containsKey(spielID)) {
            
            HashMap durchlaeufe = (HashMap)informationsspeicher.get(spielID);
            
            if (durchlaeufe.containsKey(spielDurchlauf)) {    
                // Die neue Information an die Liste anhängen
                LinkedList informationen = 
                       (LinkedList)durchlaeufe.get(spielDurchlauf);
                informationen.addLast(information);
                
            } else {
                gibFehlerAus("speichereSpielInformationen",
                             "Falscher Durchlauf:" + spielDurchlauf);
            }
            
        } else {
            gibFehlerAus("speichereSpielInformationen", 
                         "Falsche SpielID:" + spielID);
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
        if (! informationsspeicher.containsKey(spielID)) {
            
            // Eine neuen Speicher für die Durchläufe des Spiels anlegen
            informationsspeicher.put(spielID, new HashMap());
            
            // Eine neue Liste für die Informationen eines Durchlaufs anlegen
            ((HashMap)informationsspeicher.get(spielID)).put(spielDurchlauf, 
                                                             new LinkedList());
            
            
            // Standardwartezeit setzen
            wartezeiten.put(spielKennung, 
                            new Integer(standardwartezeit));
            
            // Spielinformationsbereinigung starten
            bereinigeInformationsspeicher(informationshaltbarkeit);
           
            
        } else {
            
            HashMap durchlaeufe = (HashMap)informationsspeicher.get(spielID);
            
            if (! durchlaeufe.containsKey(spielDurchlauf)) {
            } else {
                // Hat das Anmelden nicht geklappt, wird ein Fehler geworfen
                throw new DoppelterDurchlaufException("Ein Spiel mit der ID " 
                            + spielID
                            + " ist schon angemeldet!");
            }
        }
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
        if (empfohleneWartezeit != 0) {
                wartezeiten.put(spielKennung, 
                                new Integer(empfohleneWartezeit));
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
                                
        if (informationsspeicher.containsKey(spielID)
            && ((HashMap)informationsspeicher.get(spielID)).containsKey(spielDurchlauf)) {
            
                
            // Spielende markieren
            HashMap durchlaeufe = (HashMap)informationsspeicher.get(spielID);
            LinkedList informationen = 
                            (LinkedList)durchlaeufe.get(spielDurchlauf);
            
            informationen.addLast(new Spielende(System.currentTimeMillis()));
            
            // Den Wartezeiteintrag für das Spiel löschen
            wartezeiten.remove(spielKennung);
            
            
        } else {
            // Falscher Authentifizierungscode!
            gibFehlerAus("spielAbmelden", 
                    "Falsche Spielkennung:" + spielKennung);
        }
    }
    
    
}

