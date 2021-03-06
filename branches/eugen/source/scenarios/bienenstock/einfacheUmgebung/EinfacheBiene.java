/*
 * Dateiname      : EinfacheBiene.java
 * Erzeugt        : 20. Januar 2004
 * Letzte �nderung: 14. Februar 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *
 *
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut f�r
 * Intelligente Systeme der Universit�t Stuttgart unter Betreuung von
 * Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package scenarios.bienenstock.einfacheUmgebung;

import scenarios.bienenstock.agenteninfo.Koordinate;
import scenarios.bienenstock.agenteninfo.Info;

/**
 * ist das Abbild des Agenten im Szenario.
 *
 * Dieses Abbild wird verwendet um die Werte der Agenten f�r
 * etwaige �berpr�fungen bereit zu halten.
 * Dadurch werden Beeinflussungen der Werte durch den
 * jeweiligen Agenten ausgeschlossen.
 *
 * @author Philip Funck
 */
public class EinfacheBiene {
    
    /**
     * die Nummer der aktuellen Runde.
     */
    private int rundennummer;
    
    /**
     * der Zustand
     */
    private String zustand;
    
    /**
     * ist eine Nummer, die die Biene eindeutig kennzeichnet.
     * Ist identisch mit der Agent-ID die die Agenten im Simulator haben.
     */
    private int bienenID;
    
    /**
     * ist eine Nummer, die die Zugeh�rigkeit einer Biene
     * zu einem Bienenvolk eindeutig kennzeichnet.
     */
    private int bienenvolkID;
    
    /**
     * ist die aktuelle Position der Biene auf der Spielkarte
     */
    private Koordinate position;
    
    /**
     * ist der Code, der vom Agenten ben�tigt wird um eine Aktion auszuf�hren.
     */
    private long aktionsCode;
    
    /**
     * die aktuell geladene Honigmenge.
     */
    private int geladeneHonigmenge;
    
    /**
     * die aktuell geladene Nektarmenge
     */
    private int geladeneNektarmenge;
    
    /**
     * enth�ltdie Information, die der Agent bei seinem letzten Tanz
     * mitgeteilt hat.
     */
    private Info information;
    
    /**
     * Der Konsturuktor.
     *
     * @param runde             Die Nummer der Runde
     * @param zust              Der Zustand der Biene
     * @param eigenID           Die ID der Biene
     * @param volkID            Die VolksID der Biene
     * @param bienePosition     Die Position der Biene
     * @param aktCode           Der Aktionscode der Biene
     * @param gelHonigmenge     Die geladene Honigmenge
     * @param gelNektarmenge    Die geladene Nektarmenge
     */
    public EinfacheBiene(int runde,
            String zust,
            int eigenID,
            int volkID,
            Koordinate bienePosition,
            long aktCode,
            int gelHonigmenge,
            int gelNektarmenge) {
        
        rundennummer = runde;
        zustand = zust;
        bienenID = eigenID;
        bienenvolkID = volkID;
        position = bienePosition;
        aktionsCode = aktCode;
        geladeneHonigmenge = gelHonigmenge;
        geladeneNektarmenge = gelNektarmenge;
        information = null;
        
    }
    
    /**
     * Konstruktor f�r die tanzende Biene.
     *
     * @param runde             Die Nummer der Runde
     * @param zust              Der Zustand der Biene
     * @param eigenID           Die ID der Biene
     * @param volkID            Die VolksID der Biene
     * @param bienePosition     Die Position der Biene
     * @param aktCode           Der Aktionscode der Biene
     * @param gelHonigmenge     Die geladene Honigmenge
     * @param gelNektarmenge    Die geladene Nektarmenge
     * @param infoObjekt        Die mitzuteilende Information
     */
    public EinfacheBiene(int runde,
            String zust,
            int eigenID,
            int volkID,
            Koordinate bienePosition,
            long aktCode,
            int gelHonigmenge,
            int gelNektarmenge,
            Info infoObjekt) {
        
        rundennummer = runde;
        zustand = zust;
        bienenID = eigenID;
        bienenvolkID = volkID;
        position = bienePosition;
        aktionsCode = aktCode;
        geladeneHonigmenge = gelHonigmenge;
        geladeneNektarmenge = gelNektarmenge;
        information = infoObjekt;
    }
    
    /**
     * gibt die die nummer der aktuellen Runde zur�ck.
     *
     * @return    eine positive ganze Zahl
     */
    public int gibRundennummer() {
        return rundennummer;
    }
    
    /**
     * gibt den Zustand des Agenten zur�ck.
     *
     * @return  der Zustand der Biene als Wort
     */
    public String gibZustand() {
        return zustand;
    }
    
    /**
     * Gibt die Bienenid des Agenten zur�ck
     *
     * @return    eine positive ganze Zahl
     */
    public int gibBienenID() {
        return bienenID;
    }
    
    /**
     * gibt die ID des Bienenvolkes zur�ck, dem der Agent angeh�rt.
     *
     * @return      eine positive ganze Zahl
     */
    public int gibBienenvolkID() {
        return bienenvolkID;
    }
    
    /**
     * gibt die aktuelle Position des Agenten zur�ck.
     *
     * @return    x und y koordinate der Position des Agenten
     */
    public Koordinate gibPosition() {
        return position;
    }
    
    /**
     * gibt den aktuell g�ltigen Aktionscode des Agenten zur�ck.
     *
     * @return    eine positive ganze Zahl
     */
    public long gibAktionsCode() {
        return aktionsCode;
    }
    
    /**
     * gibt die geladene Nektarmenge des Agenten zur�ck.
     *
     * @return   eine positive ganze Zahl
     */
    public int gibGeladeneNektarmenge() {
        return geladeneNektarmenge;
    }
    
    /**
     * gibt die geladene Honigmenge des Agenten zur�ck.
     *
     * @return  eine positive ganze Zahl
     */
    public int gibGeladeneHonigmenge() {
        return geladeneHonigmenge;
    }
    
    /**
     * gibt die Information zur�ck, die der Agent bei seinem letzten Tanz
     * mitteilte.
     *
     * @return   Enh�lt Richtung und / oder Entfernung eines Ortes
     */
    public Info gibInformation() {
        return information;
    }
}
