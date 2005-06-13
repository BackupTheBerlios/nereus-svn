/*
 * Dateiname      : DesireIntentionPlan.java
 * Erzeugt        : 29. Mai 2005
 * Letzte Änderung: 6. Juni 2005 durch Eugen Volk
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
 * Die Klasse stelt eine Datenstruktur für einen Belief Desire Inteiontion (BDI)-Agenten
 * dar.
 * @author Eugen Volk
 */
public class DesireIntentionPlan {
    // Ziele des Agenten
    /** Konstante für Ziel: Umgebung erforschen */
    public static final int G_UMGEBUNGERFORSCHEN=0;
    /** Konstante für Ziel: Blumen suchen */
    public static final int G_FINDEEINEBLUME=1;
    /** Konstante für Ziel: Probe von einer Blume entnehmen */
    public static final int G_BLUMENPROBEENTNEHMEN=2;
    /** Konstante für Ziel: Blume bearbeiten */
    public static final int G_BLUMEBEARBEITEN=3;
    /** Konstante für Ziel: mit anderen Agenten kooperieren bzw. Inforamation austauschen */
    public static final int G_COOPERATION=4;
    
    public static final int G_FINDEDIEBLUME=5;
    
    // Absichten des Agenten
    /** Konstante für Absicht: zurück zum Bienenstock kehren und tanken */
    public static final int P_NACHHAUSETANKEN=10;
    /** Konstante für Absicht: fliegen zur bestimmten Koordinate */
    public static final int P_FLIEGENZURKOORDINATE=11;
    /** Konstante für Absicht: Nektar abbauen */
    public static final int P_NEKTARABBAUEN=12;
    /** Konstante für Absicht: Nekar abliefern */
    public static final int P_NEKTARABLIEFERNTANKEN=14;
    /** Konstante für Absicht: zurück zur blume kehren */
    public static final int P_ZURUECKZURBLUME=15;
    /** Konstante für Absicht: Kooperieren: tanzen oder zuschauen */
    public static final int P_COOPERATION=17;
    
    public static final int P_FINDEBLUME=18;
    
    
    // Schlüssel für PlanAktionen
    /** Konstante für atomare Aktion: tanken */
    public static final int A_TANKEN=100;
    /** Konstante für atomare Aktion: starten */
    public static final int A_STARTEN=101;
    /** Konstante für atomare Aktion: für fliegen zum nächst gelegenen Feld */
    public static final int A_FLIEGEN=102;
    /** Konstante für atomare Aktion: landen */
    public static final int A_LANDEN=103;
    /** Konstante für atomare Aktion: Nektar abbauen */
    public static final int A_NEKTARABBAUEN=104;
    /** Konstante für atomare Aktion: tanzen */
    public static final int A_TANZEN=105;
    /** Konstante für atomare Aktion: zuschauen */
    public static final int A_ZUSCHAUEN=106;
    /** Konstante für atomare Aktion: warten */
    public static final int A_WARTEN=107;
    
    public static final int A_NEKTARABLIEFERN=108;
    
    public static final int NOTSET=-1;
    
    /** Ziel des Agenten, codiert über DesireIntentionPlan.D_... */
    private int desireName;
    /** Koordinate für das Ziel */
    private Koordinate desireZiel;
    
    /** Absicht des Agenten, codiert über Absicht-Konstanten: DesireIntentionPlan.I_... */
    private int intentionName;
    /** Koordinate für die nächste Absicht */
    private Koordinate intentionZiel;
    
    /** Plan des Agenten für die jeweilige Absicht */
    private int planName;
    /** Plan des Agenten für die jeweilige Absicht. Ein Plan ist eine Liste aus Map,
     * wobei ein Map aus Paapr DesireInteintionPlan.P_...  und zugehörigen Koordinate */
    private LinkedList planListe;
    /** Iterator auf planListe */
    private Iterator planIt;
    /** gibt an, ob der Agent zurück zum Bienenstock fliegt */
    private boolean zurueck=false;
    
    
    /**
     * Erzeugt eine Instanz von DesireIntentionPlan. DesireIntentionPlan beschreibt den
     * internen Zustand eines Belief-Desire-Intention Agenten.
     */
    public DesireIntentionPlan() {
    }
    
    
    public void setDesire(int desireName){
        this.intentionZiel=null;   
        this.desireName=desireName;
    }
    
    public void setDesireZiel(Koordinate zielKoordinate){
        this.desireZiel=zielKoordinate;
    }
    
    /**
     * setzt die Absicht des Agenten.
     * @param intentionName Absicht des Agenten, codiert über die Klassenkonstanten.
     */
    public void setIntention(int intentionName){
        this.intentionName=intentionName;
    }
    
    /**
     * setzt das Absicht-Ziel des Agenten.
     * @param intentionKoordinate Absicht-Ziel des Agenten.
     */
    public void setIntentionZiel(Koordinate intentionKoordinate){
        this.intentionZiel=intentionKoordinate;
    }
    
    public void setPlan(int planName){
        this.planName=planName;
    }
    
    /**
     * setzt die Plan-Liste des Agenten.
     * @param planListe Plan-Liste des Agenten.
     */
    public void setPlanListe(LinkedList planListe){
        this.planListe=planListe;
    }
    
    /**
     * liefert den Wunsch des Agenten, codiert über die Klassenkonstanten.
     * @return Wunsch des Agenten
     */
    public int getDesire(){
        return this.desireName;
    }
    
    /**
     * liefert das Wunsch-Ziel des Agenten, codiert über die Klassenkonstanten.
     * @return Wunsch-Ziel
     */
    public Koordinate getDesireZiel(){
        return this.desireZiel;
    }
    
    /**
     * liefert die Absicht des Agenten, codiert über die Klassenkonstanten.
     * @return Absicht des Agenten
     */
    public int getIntention(){
        return this.intentionName;
    }
    
    /**
     * liefert die ZielKoordinate als nächste Absicht des Agenten, codiert über die Klassenkonstanten.
     * @return Ziel-Koordinate des Agenten.
     */
    public Koordinate getIntentionZiel(){
        return this.intentionZiel;
    }
    
    
    public int getPlanName(){
        return this.planName;
    }
    
    /**
     * gibt den Plan als Liste auszuführenden Aktionen zurück, codiert über die Klassenkonstanten.
     * @return Liste der auszuführenden Aktionen.
     */
    public LinkedList getPlanListe(){
        return this.planListe;
    }
    
    public Iterator getPlanIterator(){
        return this.planIt;
    }
    
    public void setPlanIterator(Iterator planIterator){
        this.planIt=planIterator;
    }
    
    /**
     * gibt an ob die Biene auf dem Weg nach Hause ist.
     * @return true, falls die Biene auf dem Weg zurück zum Bienenstock ist,
     * false sonst.
     */
    public boolean getAufDemRueckweg(){
        return this.zurueck;
    }
    
    /**
     * setzt den Status des Agenten: ob dieser auf dem Weg  nach Hause ist.
     * @param zurueck Ist der Agent auf dem Rueckweg zum Bienenstock?
     */
    public void setAufDemRueckweg(boolean zurueck){
        this.zurueck=zurueck;
    }
    
    
    
}
