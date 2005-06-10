/*
 * Dateiname      : InfoBlume.java
 * Erzeugt        : 30. Mai 2005
 * Letzte Änderung: 31. Juni 2005 durch Eugen Volk
 * Autoren        :  Eugen Volk
 *
 *
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
import java.util.*;


/**
 * InfoBlume ist eine Datenstruktur zur Verwaltung der Informatinen über eine Blume 
 * @author Eugen Volk
 */
public class InfoBlume {
    
    /** Koordinate der Blume */
    private Koordinate blumenKoordinate;
    /** Entfernung zum Bienenstock */
    private int entfernungZumBienenstock;
    /** ist eine Probe entnommen worden bzw. ist die Ausbeute bestimmt worden. */
    private boolean probeEntnommen=false;
    /** gibt die Ausbeute der Blume an, d.h. wie viel Nektar pro Runde gibt die Blume ab. */
    private int ausbeuteProRunde;
    /** Enthält die Blume noch Nektar */
    private boolean hatNektar=true;
    /** Welchen Nutzen hat die Blume. Errechnet sich aus: Gewinn(gesamelter Nektar * FaktorNektarHonig) 
     * - Aufwand(Honigverbrauch für hinfliegen, zurückfliegen und wie oft Nektar abgebaut wurde) */
    private double nutzen=0;
    
    /** InfoBlume ist eine Datenstruktur zur Verwaltung der Informatinen über eine Blume */
    public InfoBlume() {
    }
    
    
    /**
     * InfoBlume ist eine Datenstruktur zur Verwaltung der Informatinen über eine Blume 
     * @param blumenKoordinate Koordinate der Blume
     */
    public InfoBlume(Koordinate blumenKoordinate){
        super();
        this.blumenKoordinate=blumenKoordinate;
    }
    /**
     * InfoBlume ist eine Datenstruktur zur Verwaltung der Informatinen über eine Blume 
     *
     * @param blemenKoordinate Koordinate der Blume
     * @param entfernungZumBienenstock Entfernung (Anzahl der Felder) zum Bienenstock
     */
    public InfoBlume(Koordinate blumenKoordinate, int entfernungZumBienenstock){
        super();
        this.blumenKoordinate=blumenKoordinate;
        this.entfernungZumBienenstock=entfernungZumBienenstock;
    }
    
    /** gibt an, ob von der Blume eine Probe entnommen und damit die Ausbeute bestimmt wurde. */
    public boolean getProbeEntnommen(){
        return this.probeEntnommen;
    }
    
    /** setzt die die lokale Variable: probeEntnommen auf true. */
    public void setProbeEntnommen(){
        this.probeEntnommen=true;
    }
    
    /** 
     * gibt an, ob die Blume noch Nektar hat. 
     *
     * @param blumeHatNektar hat die Blume noch Nektar? 
     */
    public void setHatNektar(boolean blumeHatNektar){
        this.hatNektar=blumeHatNektar;
    }
    
    /**
     * gibt an, ob die Blume noch Nektar hat.
     * @return true, falls die Blume noch Nektar hat, false sonst.
     */
    public boolean getHatNektar(){
        return this.hatNektar;
    }
    
    /** 
     * setzt die Enfernung der Blume zum Bienenstock
     * @param entfernung Entfernung (Weglänge) zum Bienenstock
     */
    public void setEntfernungZumBienenstock(int entfernung){
        this.entfernungZumBienenstock=entfernung;
    }
    
    /**
     * liefert die Entfernung der Blume zum Bienenstock.
     * @return Entfernung (=Weglänge) zum Bienenstock
     */
    public int getEntfernungZumBienenstock(){
        return this.entfernungZumBienenstock;
    }
    
    /**
     * setzt die Ausbeute (d.h. abgegebene Menge an Nektar) der Blume.
     * @param nektarProRunde abgegebene Menge an Nektar pro Runde.
     */
    public void setAusbeuteProRunde(int nektarProRunde){
        this.ausbeuteProRunde=nektarProRunde;
    }
    
    /**
     * gibt die Ausbeute (d.h. abgegebene Menge an Nektar) der Blume zurück.
     * @return Ausbeute der Blume.
     */
    public int getAusbeuteProRunde(){
        return this.ausbeuteProRunde;
    }
    
    /**
     * setzt den Nutzen (Gewinn-Aufwand) der Blume.
     * @param nutzen Nutzen der Blume.
     */
    public void setNutzen(double nutzen){
         this.nutzen=nutzen;   
    }
    
    /**
     * liefert den Nutzen (Gewinn-Aufwand) der Blume.
     * @return Nutzen der Blume
     */
    public double getNutzen(){
        return this.nutzen;
    }
    
    /**
     * liefert die Koordinate der Blume
     * @return Koordinate der Blume
     */
    public Koordinate getBlumenKoordinate(){
        return this.blumenKoordinate.copy();
    }
}
