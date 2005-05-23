/*
 * Dateiname      : VisualisationServer.java
 * Erzeugt        : 19. Mai 2005
 * Letzte �nderung: 19. Mai 1005 durch Samuel Walz
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
package source.server.visualisation;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.HashMap;

/**
 *
 * @author Samuel Walz
 */
public class VisualisationServer extends UnicastRemoteObject 
        implements IVisualisationServerIntern, IVisualisationServerExtern {
    
    private static HashMap informationsspeicher = new HashMap();
    
    /** 
     * Creates a new instance of VisualisationServer 
     */
    public VisualisationServer() throws RemoteException{
        System.out.println("Melde den VisualisationServer an...");
        UnicastRemoteObject.unexportObject((Remote)this, true);
        UnicastRemoteObject.exportObject((Remote)this);
        try {
            Naming.rebind("VisualisationServer", this);
        } catch(Exception fehler) {
            System.out.println(fehler.getMessage());
        }
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
        
        LinkedList listenausschnitt = new LinkedList();
        ListIterator listenlaeufer = liste.listIterator(ausschnittsbeginn);
        while (listenlaeufer.hasNext()) {
            listenausschnitt.add(listenlaeufer.next());
        }
        
        return listenausschnitt;
    }
    
    /**
     * Gibt die Spielinformationen zu einem bestimmten Spiel zur�ck.
     *
     * @param spielID
     * @param ausschnittsbeginn      nat�rliche Zahl gr��er -1
     */
    public LinkedList gibSpielInformationen(int spielID, int ausschnittsbeginn) 
            throws RemoteException {
        //Informationen suchen und zur�ckgeben
        if (informationsspeicher.containsKey(new Integer(spielID))) {
            return erstelleAusschnitt(
                (LinkedList)informationsspeicher.get(new Integer(spielID)), 
                ausschnittsbeginn);
        } else {
            //Sind noch keine Informationen vorhanden, wird null zur�ckgegeben
            return new LinkedList();
        }
    }
    
    /**
     * 
     */
    public void speichereSpielInformationen(int spielID, 
            java.lang.Object information) {
        //Existiert f�r das Spiel schon eine List?
        if (! informationsspeicher.containsKey(new Integer(spielID))) {
            //Falls nicht, dann legen wir eine neue an
            informationsspeicher.put(new Integer(spielID), new LinkedList());
        }
        
        //Die neue Information an die Liste anh�ngen
        ((LinkedList)informationsspeicher.get(new Integer(spielID))).add(information);
        
        //Eventuell wartende Threads wecken
        this.notifyAll();
        
    }
    
}
