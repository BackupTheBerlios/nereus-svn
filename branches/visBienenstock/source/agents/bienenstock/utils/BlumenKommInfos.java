/*
 * Dateiname      : BlumenKommInfos.java
 * Erzeugt        : 21. Juli 2005
 * Letzte Änderung: 21. Juli 2005 durch Eugen Volk
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


package agents.bienenstock.utils;
import scenarios.bienenstock.agenteninfo.Koordinate;
import agents.bienenstock.utils.InfoBlume;
import java.util.*;

/**
 * Klasse zur Verwaltung der Kommunikationsinformationen, die eine Agent
 * während des ganzen Spiels empfangen hat.
 */
public class BlumenKommInfos {
    /** mitgeteilte Kommunikationsinformationen */
    private HashMap kommInformationen=new HashMap();
    /** blumen die der Agent kennt */
    private HashMap bekannteBlumen;
    /** mindestAbstand zwischen den Blumen */
    private double mindestAbstand=2.9;
    /**  mindestAbstand zwischen den bekannten Blumen und mitgeteilten Koordinaten  */
    private double minAbstand2=1.5;
    /** Koord häufigkeit paare, enthält zu jeden Koordinate die Anzahl ihrer Überschreibungen. */
    private HashMap haeufigkeit=new HashMap();
    
    /** Datenstruktur für die Sortierung der Blumeninformationen nach Nutzen*/
    private TreeSet reducedTreeSet;
    
    /**
     * verwaltet die Kommunikationsinformationen, die ein Agent
     * während des genzen Spiels empfangen hat.
     */
    public BlumenKommInfos() {
    }
    
    /**
     * verwaltet die Kommunikationsinformationen, die ein Agent
     * während des genzen Spiels empfangen hat.
     * @param blumen Blumen, die den Agenten bekannt sind.
     * @param mindestAbstand mindestAbstand zwischen den Blumen
     */
    public BlumenKommInfos(HashMap blumen, int mindestAbstand){
        this.bekannteBlumen=blumen;
        this.mindestAbstand=mindestAbstand;
    }
    
    /**
     * verwaltet die Kommunikationsinformationen, die ein Agent
     * während des genzen Spiels empfangen hat.
     * @param blumen Blumen, die den Agenten bekannt sind.
     */
    public BlumenKommInfos(HashMap blumen){
        this.bekannteBlumen=blumen;
    }
    
    /**
     * speichert die erhaltene Information über eine Blume .
     * @param koord Koordinate der Blume
     * @param nutzen Nutzen der Blume
     * @param rundenNr RundeNr., in der die Information mitgeteilt wurde.
     */
    public void addKommInfo(Koordinate koord, double nutzen, int rundenNr){
        
        KommInfo newKommInfo=new KommInfo(koord, nutzen, rundenNr);
        
        if (kommInformationen.containsKey(koord)){
            int hauf=((Integer)haeufigkeit.get(koord)).intValue();
            hauf=hauf+1;
            haeufigkeit.put(koord,new Integer(hauf));
            kommInformationen.put(koord, newKommInfo);
        } else{
            haeufigkeit.put(koord, new Integer(1));
            kommInformationen.put(koord, newKommInfo);
        }
    }
    
    
    /**
     * gruppiert die Koordinaten, die den mindestAbstand unterschreiten.
     * @return gruppierte Koordinaten in form (koord, HashSet)
     */
    private HashMap gruppiere(){
        HashMap gruppen=new HashMap();
        
        HashMap hauf1=(HashMap)haeufigkeit.clone();
        HashMap hauf2=(HashMap)haeufigkeit.clone();
        
        Iterator it1=hauf1.keySet().iterator();
        while (it1.hasNext()){
            Koordinate koord1=(Koordinate)it1.next();
            Iterator it2=hauf2.keySet().iterator();
            HashSet liste=new HashSet();
            while (it2.hasNext()){
                Koordinate koord2=(Koordinate)it2.next();
                if (abstand(koord1,koord2)<mindestAbstand){
                    liste.add(koord2);
                    it2.remove();
                }
            }
            if (liste.size()>0) gruppen.put(koord1, liste);
        }
        return gruppen;
    }
    
    
    /**
     * reduziert die gruppierten Koordinaten jeweils zu einer Koordinate
     * @param gruppen gruppierte Koordinaten in form von (koord, liste)
     * @return eine Liste der reduzierten gruppen in Form (koord, KommInfo)
     */
    private HashMap reduziere(HashMap gruppen){
        HashMap returnVal=new HashMap();
        
        Iterator it=gruppen.values().iterator();
        while (it.hasNext()){
            HashSet liste=(HashSet)it.next();
            Iterator listIt=liste.iterator();
            int gesamtHauf=0;
            double xSum=0;
            double ySum=0;
            int lastRundenNr=0;
            double bestNutzen=0;
            // berechnung des arithmetischen Mittelwertes jeder Koordinate
            while (listIt.hasNext()){
                Koordinate aKoord=(Koordinate)listIt.next();
                int hauf=((Integer)this.haeufigkeit.get(aKoord)).intValue();
                gesamtHauf=gesamtHauf+hauf;
                xSum=xSum+hauf*aKoord.gibXPosition();
                ySum=ySum+hauf*aKoord.gibYPosition();
                KommInfo aKommInfo=(KommInfo)kommInformationen.get(aKoord);
                int rundenNr=aKommInfo.getRundeNr();
                double myNutzen=aKommInfo.getNutzen();
                if (lastRundenNr<rundenNr) lastRundenNr=rundenNr;
                if (bestNutzen<myNutzen) bestNutzen=myNutzen;
            }
            double xMed=xSum/gesamtHauf;
            double yMed=ySum/gesamtHauf;
            int x=(int)Math.round(xMed);
            int y=(int)Math.round(yMed);
            Koordinate newKoordinate=new Koordinate(x,y);
            KommInfo newKommInfo=new KommInfo(newKoordinate,bestNutzen,lastRundenNr);
            returnVal.put(newKoordinate, newKommInfo);
        }
        return returnVal;
    }
    
    /**
     * berechnet den euklidischen Abstand zwischen zwei Koordinaten
     * @param koord1 erste Koordinate
     * @param koord2 zweite Koordinate
     * @return Abstand zwischen zwei Koordinaten
     */
    private double abstand(Koordinate koord1, Koordinate koord2){
        double abstand;
        double xDiff=koord1.gibXPosition()- koord2.gibXPosition();
        double yDiff=koord1.gibYPosition() - koord2.gibYPosition();
        abstand=Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
        return abstand;
    }
    
    
    /**
     * gibt eine Liste aus KommInfo Elementen, die Blumenkoordinaten enthalten,
     * sortiert nach ihrem Nutzen.
     *
     * @return sortierte Liste aus KommInfo-Elementen der mitgeteilten Blumenkoordinaten mit Nektar
     */
    public LinkedList getReducedKoordListe(){
        HashMap gruppen=gruppiere();
        HashMap redGrup=reduziere(gruppen);
        /* menge mit gruppierten Koordinaten */
        Set redGrupKeySet=redGrup.keySet();
        // entferne Koordinate, deren Blumen kein Nektar haben.
        Iterator blumenIt=bekannteBlumen.values().iterator();
        while(blumenIt.hasNext()){
            InfoBlume infoBlume=(InfoBlume)blumenIt.next();
            if (infoBlume.getHatNektar()==false){
                Koordinate blumenKoord=infoBlume.getBlumenKoordinate();
                if (redGrupKeySet.contains(blumenKoord)) redGrupKeySet.remove(blumenKoord);
                else{
                    HashSet redGrHS=new HashSet(redGrupKeySet);
                    Iterator it=redGrHS.iterator();
                    while(it.hasNext()){
                        Koordinate keyKoord=(Koordinate)it.next();
                        if (abstand(keyKoord,blumenKoord)<mindestAbstand) redGrupKeySet.remove(keyKoord);
                    }
                }
            }
        }
        TreeSet sort=new TreeSet();
        sort.addAll(redGrup.values());
        reducedTreeSet=sort;
        
        
        
        LinkedList sortListe=new LinkedList(sort);
        return sortListe;
    }
    
      
    
    /**
     * liefert eine Liste mit allen Blumenkoordinaten, sortiert nach Nutzen
     * @return eine Liste mit allen Blumen, deren Nutzen bekannt sind.
     */
    
    public LinkedList getAllKoordListe(){
        getReducedKoordListe();
        
        Iterator blumenIt=bekannteBlumen.values().iterator();
        while(blumenIt.hasNext()){
            InfoBlume infoBlume=(InfoBlume)blumenIt.next();
            if (infoBlume.getHatNektar()==true && infoBlume.getProbeEntnommen() && (infoBlume.getNutzen()>0)){
                Koordinate newKoordinate=infoBlume.getBlumenKoordinate();
                double nutzen=infoBlume.getNutzen();
                int rundenNr=0;
                KommInfo newKommInfo=new KommInfo(newKoordinate,nutzen,rundenNr);
                reducedTreeSet.add(newKommInfo);
            }
            
        }
        
        HashSet sortListe=new HashSet(reducedTreeSet);
        
        Iterator it=((HashSet)sortListe.clone()).iterator();
        while (it.hasNext()){
            KommInfo aKommInfo=(KommInfo)it.next();
            Koordinate kommKoord=aKommInfo.getKoordinate();
            
            blumenIt=bekannteBlumen.values().iterator();
            while(blumenIt.hasNext()){
                InfoBlume infoBlume=(InfoBlume)blumenIt.next();
                Koordinate blumenKoord=infoBlume.getBlumenKoordinate();
                if ((!blumenKoord.equals(kommKoord)) && abstand(blumenKoord,kommKoord)<=minAbstand2){
                    sortListe.remove(aKommInfo);
                    aKommInfo.setKoordinate(blumenKoord);
                    sortListe.add(aKommInfo);
                }
            }
     
        }
        
        TreeSet sort=new TreeSet();
        sort.addAll(sortListe);
        return new LinkedList(sort);
    }
    
     
}
