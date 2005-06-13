/*
 * Dateiname      : Dijkstra.java
 * Erzeugt        : 23. Mai 2005
 * Letzte Änderung: 26. Mai 2005 durch Eugen Volk
 * Autoren        : Eugen Volk
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Eugen Volk am Institut für Intelligente Systeme
 * der Universität Stuttgart unter Betreuung von Dietmar Lippold
 * (dietmar.lippold@informatik.uni-stuttgart.de).
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

package nereus.agentutils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedList;
/**
 * die Klasse enthält Dijksta's Algorithmus zur Berechnung kürzeste Pfade in einem Graphen
 */

public class Dijkstra {
    
    /**
     * Dijksta's Algorithmus zur Berechnung kürzeste Pfade in einem Graphen
     *
     * @param start Startknoten
     * @param knotenMenge die Menge der Knoten zur Bestimmung des kürzesten Pfades
     * @param nachfolgeKnoten HashMap die zu jedem Knoten alle seine Nachfolger enthält
     *
     * @return HashMap mit Vorgaengern zu je Knoten
     */
    public static HashMap allShortestPath(Object start, Set knotenMenge, HashMap nachfolgeKnoten){
        
        HashMap distanceTable=new HashMap(); // Distance
        HashMap vorgaenger=new HashMap();  // vorgänger
        HashSet baumKnoten=new HashSet();  // Baumknoten
        HashSet randKnoten=new HashSet();  // Randknoten
        HashSet unbekannteKnoten=new HashSet();  // Unbekannte Menge
        Object aktuellerKnoten,tmpKnoten;
        // Initialisierung
        baumKnoten.add(start);
        unbekannteKnoten.addAll(knotenMenge);
        aktuellerKnoten=start;
        unbekannteKnoten.remove(start);
        vorgaenger.put(start, null);
        distanceTable.put(start, new Double(0));
        while(aktuellerKnoten!=null){
            HashSet nachfolger=(HashSet)nachfolgeKnoten.get(aktuellerKnoten);
            if (nachfolger==null) nachfolger=new HashSet();
            Iterator nachfIt=nachfolger.iterator();
            while(nachfIt.hasNext()){
                tmpKnoten=nachfIt.next();
                // Rand-Knoten aktualisieren
                if (unbekannteKnoten.contains(tmpKnoten)){
                    double distY=((Double)distanceTable.get(aktuellerKnoten)).doubleValue();
                    distanceTable.put(tmpKnoten, new Double(distY+1));
                    vorgaenger.put(tmpKnoten,aktuellerKnoten);unbekannteKnoten.remove(tmpKnoten);
                    randKnoten.add(tmpKnoten);
                }else if (randKnoten.contains(tmpKnoten)){
                    double distX=((Double)distanceTable.get(aktuellerKnoten)).doubleValue();
                    double distY=((Double)distanceTable.get(tmpKnoten)).doubleValue();
                    // kürzeter Weg über x bzw. aktuellerKnoten
                    if ((distX+1)<distY){
                        distanceTable.put(tmpKnoten, new Double(distX+1));
                        vorgaenger.put(tmpKnoten, aktuellerKnoten);
                    }
                }
            }
            /* suche x \in randKnoten mit min. Abstand */
            aktuellerKnoten=null; double alfa=Double.POSITIVE_INFINITY;
            Iterator randIt=randKnoten.iterator();
            while(randIt.hasNext()){
                tmpKnoten=randIt.next();
                double distY=((Double)distanceTable.get(tmpKnoten)).doubleValue();
                if (distY<alfa) {aktuellerKnoten=tmpKnoten; alfa=distY;}
            }
            // verschiebe aktuellerKnoten von randKnoten nach baumKnoten
            baumKnoten.add(aktuellerKnoten);
            randKnoten.remove(aktuellerKnoten);
        }
        return vorgaenger;
    }
    
    
    /**
     * Ermittelt den kuerzesten Weg (als Liste) von dem Start-Knoten zum Ziel-Knoten.
     *
     * @param startKnoten Start-Knoten 
     * @param zielKnoten Ziel-Knoten 
     * @param nachfolgeKnoten HashMap die zu jedem Knoten alle seine Nachfolger enthält
     * @return Weg von startKnoten nach zeilKnoten, wobei der startKnoten nicht in der
     * Liste enthalten ist. Falls der weg nicht existiert, so wird null zurückgeliefert.
     */
    public static LinkedList shortestPath(Object startKnoten, Object zielKnoten, Set knotenMenge, HashMap nachfolgeKnoten){
        HashMap vorgaengerKnotenMap;
        vorgaengerKnotenMap=allShortestPath(startKnoten, knotenMenge, nachfolgeKnoten);
        Object knoten;
        Object vorgaenger;
        LinkedList weg=new LinkedList();
        knoten=zielKnoten;
        while((knoten!=null)){
            weg.addFirst(knoten);
            vorgaenger=vorgaengerKnotenMap.get(knoten);
            knoten=vorgaenger;
        }
        /* prüfung, ob der oberste Element in der liste startKnoten ist */
        if (weg.getFirst().equals(startKnoten)) {
            weg.removeFirst(); 
        }else weg=null; // false nicht, dann gibt es kein weg
        
        return weg;
    }
    
     /**
     * Ermittelt den kuerzesten Weg (als Liste) von dem Start-Knoten zum Ziel-Knoten.
     *
     * @param startKnoten Start-Knoten 
     * @param zielKnoten Ziel-Knoten 
     * @param vorgaengerKnoten eine HashMap, die zu jedem Knoten seinen Vorganger angibt;
     * nach der Bestimmung von allen kürzesten Pfaden (z.B. mittels Dijkstra#allShortestPath() )
     * @param nachfolgeKnoten HashMap die zu jedem Knoten alle seine Nachfolger enthält
     * @return Weg von startKnoten zum zeilKnoten, wobei der startKnoten nicht in der
     * Liste enthalten ist. Falls der weg nicht existiert, so wird null zurückgeliefert.
     */
    public static LinkedList shortestPath(Object startKnoten, Object zielKnoten, HashMap vorgaengerKnoten){
        HashMap vorgaengerKnotenMap;
        Object knoten;
        Object vorgaenger;
        LinkedList weg=new LinkedList();
        knoten=zielKnoten;
        while((knoten!=null)){
            weg.addFirst(knoten);
            vorgaenger=vorgaengerKnoten.get(knoten);
            knoten=vorgaenger;
        }
        /* prüfung, ob der oberste Element in der liste startKnoten ist */
        if (weg.getFirst().equals(startKnoten)) {
            weg.removeFirst(); 
        }else weg=null; // false nicht, dann gibt es kein weg
        
        return weg;
    }
    
    
    
}

