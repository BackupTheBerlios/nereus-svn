/*
 * Dateiname      : DesireIntentionPlan.java
 * Erzeugt        : 29. Mai 2005
 * Letzte �nderung: 6. Juni 2005 durch Eugen Volk
 * Autoren        :  Eugen Volk
 *
 *
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Eugen Volk am Institut f�r Intelligente Systeme
 * der Universit�t Stuttgart unter Betreuung von Dietmar Lippold
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
 * Die Klasse stelt eine Datenstruktur f�r einen Belief Desire Inteiontion (BDI)-Agenten
 * dar.
 * @author Eugen Volk
 */
public class DesireIntentionPlan {
    // Ziele des Agenten
    /** Konstante f�r Ziel: Umgebung erforschen */
    public static final int G_UMGEBUNGERFORSCHEN=0;
    /** Konstante f�r Ziel: Blumen suchen */
    public static final int G_FINDEEINEBLUME=1;
    /** Konstante f�r Ziel: Probe von einer Blume entnehmen */
    public static final int G_BLUMENPROBEENTNEHMEN=2;
    /** Konstante f�r Ziel: Blume bearbeiten */
    public static final int G_BLUMEBEARBEITEN=3;
    /** Konstante f�r Ziel: mit anderen Agenten kooperieren bzw. Inforamation austauschen */
    public static final int G_COOPERATION=4;
    
    public static final int G_FINDEDIEBLUME=5;
    
    // Absichten des Agenten
    /** Konstante f�r Absicht: zur�ck zum Bienenstock kehren und tanken */
    public static final int P_NACHHAUSETANKEN=10;
    /** Konstante f�r Absicht: fliegen zur bestimmten Koordinate */
    public static final int P_FLIEGENZURKOORDINATE=11;
    /** Konstante f�r Absicht: Nektar abbauen */
    public static final int P_NEKTARABBAUEN=12;
    /** Konstante f�r Absicht: Nekar abliefern */
    public static final int P_NEKTARABLIEFERNTANKEN=14;
    /** Konstante f�r Absicht: zur�ck zur blume kehren */
    public static final int P_ZURUECKZURBLUME=15;
    /** Konstante f�r Absicht: Kooperieren: tanzen oder zuschauen */
    public static final int P_COOPERATION=17;
    
    public static final int P_FINDEBLUME=18;
    
    
    // Schl�ssel f�r PlanAktionen
    /** Konstante f�r atomare Aktion: tanken */
    public static final int A_TANKEN=100;
    /** Konstante f�r atomare Aktion: starten */
    public static final int A_STARTEN=101;
    /** Konstante f�r atomare Aktion: f�r fliegen zum n�chst gelegenen Feld */
    public static final int A_FLIEGEN=102;
    /** Konstante f�r atomare Aktion: landen */
    public static final int A_LANDEN=103;
    /** Konstante f�r atomare Aktion: Nektar abbauen */
    public static final int A_NEKTARABBAUEN=104;
    /** Konstante f�r atomare Aktion: tanzen */
    public static final int A_TANZEN=105;
    /** Konstante f�r atomare Aktion: zuschauen */
    public static final int A_ZUSCHAUEN=106;
    /** Konstante f�r atomare Aktion: warten */
    public static final int A_WARTEN=107;
    
    public static final int A_NEKTARABLIEFERN=108;
    
    public static final int NOTSET=-1;
    
    /** Ziel des Agenten, codiert �ber DesireIntentionPlan.D_... */
    private int desireName;
    /** Koordinate f�r das Ziel */
    private Koordinate desireZiel;
    
    /** Absicht des Agenten, codiert �ber Absicht-Konstanten: DesireIntentionPlan.I_... */
    private int intentionName;
    /** Koordinate f�r die n�chste Absicht */
    private Koordinate intentionZiel;
    
    /** Plan des Agenten f�r die jeweilige Absicht */
    private int planName;
    /** Plan des Agenten f�r die jeweilige Absicht. Ein Plan ist eine Liste aus Map,
     * wobei ein Map aus Paapr DesireInteintionPlan.P_...  und zugeh�rigen Koordinate */
    private LinkedList planListe;
    /** Iterator auf planListe */
    private Iterator planIt;
    /** gibt an, ob der Agent zur�ck zum Bienenstock fliegt */
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
     * @param intentionName Absicht des Agenten, codiert �ber die Klassenkonstanten.
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
     * liefert den Wunsch des Agenten, codiert �ber die Klassenkonstanten.
     * @return Wunsch des Agenten
     */
    public int getDesire(){
        return this.desireName;
    }
    
    /**
     * liefert das Wunsch-Ziel des Agenten, codiert �ber die Klassenkonstanten.
     * @return Wunsch-Ziel
     */
    public Koordinate getDesireZiel(){
        return this.desireZiel;
    }
    
    /**
     * liefert die Absicht des Agenten, codiert �ber die Klassenkonstanten.
     * @return Absicht des Agenten
     */
    public int getIntention(){
        return this.intentionName;
    }
    
    /**
     * liefert die ZielKoordinate als n�chste Absicht des Agenten, codiert �ber die Klassenkonstanten.
     * @return Ziel-Koordinate des Agenten.
     */
    public Koordinate getIntentionZiel(){
        return this.intentionZiel;
    }
    
    
    public int getPlanName(){
        return this.planName;
    }
    
    /**
     * gibt den Plan als Liste auszuf�hrenden Aktionen zur�ck, codiert �ber die Klassenkonstanten.
     * @return Liste der auszuf�hrenden Aktionen.
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
     * @return true, falls die Biene auf dem Weg zur�ck zum Bienenstock ist,
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
