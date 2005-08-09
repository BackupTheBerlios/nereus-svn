/*
 * Dateiname      : MinAbstandAgent.java
 * Erzeugt        : 21. Juli 2005
 * Letzte Änderung: 8. August 2005 durch Eugen Volk
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

package agents.bienenstock;
import nereus.simulatorinterfaces.AbstractScenarioHandler;
import scenarios.bienenstock.interfaces.AbstrakteBiene;
import scenarios.bienenstock.interfaces.IBienenstockSzenarioHandler;
import scenarios.bienenstock.einfacheUmgebung.*;
import nereus.utils.Id;
import nereus.utils.BooleanWrapper;
import nereus.agentutils.ActionTargetPair;

import java.util.Iterator;
import java.util.Random;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeSet;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import nereus.agentutils.Dijkstra;
import scenarios.bienenstock.agenteninfo.Info;
import scenarios.bienenstock.agenteninfo.Koordinate;
import agents.bienenstock.utils.DesireIntentionPlan;
import agents.bienenstock.utils.InfoBlume;
import agents.bienenstock.utils.KoordWertPaar;
import agents.bienenstock.utils.BlumenKommInfos;
import agents.bienenstock.utils.KommInfo;

/**
 * Ein Agent, der für die Suche den minimalen Abstand zwischen den Blumenwiesen ausnutzt.
 * Desweiteren ist dies ein kooperativer Agent, der die Information über den Nektarinhalt einer Blumenwiese
 * für die Kommunikation und Kooperation ausnutzt.
 * Die Klasse ist Agent und ist aufgebaut nach der deliberativen Agenten-Architektur.
 *
 */
public class MinAbstandAgent
        extends AbstrakteBiene
        implements Runnable {
    
      /**
     * ID des Volkes, zu der die Biene gehört.
     */
    private int volksID;
    /**
     * aktueller Aktionskode der Biene.
     */
    private long aktCode;
    /**
     * Szenario-Handler für die Vermittlung  der Information
     * zwischen dem  Agenten und dem SzenarioManager
     */
    private IBienenstockSzenarioHandler handler;
    /**
     * spiegelt den externen Zustand der Biene.
     * Ist true, falls die Biene in der Luft ist.
     * Ist false, falls die Biene nicht in der Luft ist.
     */
    private boolean bieneIstInDerLuft=false;
    
    /** Daten fuer die Lokalisation und Umweltwahrnehmung */
    private EinfacheKarte lokaleKarte;
    /** Abbild der Biene  */
    private EinfacheBiene selbst;
    /** position der Biene als Feld */
    private EinfachesFeld positionFeld;
    /** position der Biene als Koordinate  */
    private Koordinate positionKoordinate;
    /** für Agenten sichtbare Felder */
    private Hashtable sichtbareFelder;
    /** kritische Entfernung bis zur Blume */
    private int kritEntfernung=0;
    /**
     * Flag, der signalisiert ob die Biene ersten Aktionscode bekommen hat und damit im Spiel ist.
     */
    boolean erstenAktCodeBekommen = false;
    /** Id der Biene */
    private int id = 0;
    /**
     * Koordinate des Bienenstocks.
     */
    private Koordinate posBienenstock;
    /**
     * falls eine beabsichtigte Aktion tatsächlich ausgeführt wurde, wird erfolg.value den Wert true haben.
     */
    private BooleanWrapper erfolg=new BooleanWrapper(false);
    
    /**
     * Zähler für die begrenzte Kooperationsbereitschaft der Biene  (Z.B. Kooperatin für 5 Runden erhalten)
     */
    private int rundeNrKooperation=0;
    /**
     * Maximale Anzahl der Kooperationsrunden.
     * Konstante für die Erhaltung der Kooperation (warten auf Kommunikationspartner) über mehrere Runden.
     */
    private int maxAnzahlKooperationsRunden=5;
    /**
     * Max. Anzahl Kooperationsrunden bei der Suche
     * Konstante für die Erhaltung der Kooperation (warten auf Kommunikationspartner) über mehrere Runden
     * bei der Suche.
     */
    private int maxAnzahlKooperationsRundenFSuche=7;
    /**
     * maximale Anzahl der Warte-Runden bei der Suche
     * um auf Kooperationspartner zu warten.
     */
    private int maxAnzahlWarteRundenFSuche=4;
    /**
     * maximale Anzahl der Warte-Runden,
     * um auf Kooperationspartner zu warten.
     */
    private int maxAnzahlWarteRunden=2;
    /**
     * Ist neue Mitteilung erhalten worde,
     * so ist der Wert=true, sonst false.
     */
    private boolean neueMitteilungErhalten=false;
    
    /**
     * Wird für die Ausgabe (text) der Agenten-Aktionen verwendet.
     * soll jede Aktion des Agenten ausgegeben werden?
     */
    private boolean out=false;
    
    
    /**
     * Eine Datenstruktur zur speicherung der bekannten Blumen.
     * (Koordinate, InfoBlume)
     */
    HashMap bekannteBlumen=new HashMap();
    
    
    /**
     * Eine Navigationskarte als Gedächtnis für die wahrgenommenen Felder
     */
    HashMap gespeicherteFelder=new HashMap();
    
    /** wartende Bienen auf dem aktuellen Feld (ohne die Biene selbst) */
    HashSet wartendeBienen;
    /**  tanzende Bienen auf dem aktuellen Feld (ohne die Biene selbst) */
    HashSet tanzendeBienen;
    //ein paar honigkosten
    /**
     * HonigMenge vor dem Start der Biene,
     * bzw. maximale zuladbare Honigmenge der Biene.
     */
    private int startHonig=0;
    /** Honigverbrauch zum Starten */
    private int honigStarten = 0;
    /** Honigverbrauch zum Fliegen */
    private int honigFliegen = 0;
    /** Honigverbrauch zum  Landen */
    private int honigLanden = 0;
    /** Honigverbrauch zum  Abbauen */
    private int honigAbbauen = 0;
    /** Honigverbrauch zum  Tanken */
    private int honigTanken = 0;
    /** Honigverbrauch zum  Abliefern des gesammelten Nektars */
    private int honigAbliefern = 0;
    /** maximale zuladbare Nektarmenge */
    private int maxGelNektar=0;
    /**  Name der Biene.    */
    private String name = "";
    /** gesammelte Nektarmange. */
    private int gesammelterNektar = 0;
    /**  Wechselkurs für die Umwandlung von Nektar zu Honig. */
    private double kursNektarHonig=1;
    /** Reserver für den Fall dass Biene Warten muss */
    private int reserve=2;
    /** Id der getanzten Bienen, seit kooperatinReset.   */
    private HashSet getanzteBienen=new HashSet();
    /** hat die Biene schon seit kooperationReset getanzt?  */
    boolean getanzt=false;
    /** wird gesetzt, falls die Biene wenig Honig hat und sofort zum Bienenstock zurückkehren muss */
    private boolean sofortNektarAbliefernTanken = false;
    /** mitgeteilte BlumenKoordinaten: (Koordinate, new Double(Nutzen))*/
    private HashMap mitgeteilteBlumenKoord=new HashMap();
    /**
     * Gibt es noch Honig im Bienenstock?
     */
    private boolean  bienenstockHatHonig=true;
    /**
     * Bienen, die aktuelle Blume des Agenten mit bearbeiten, und sollen deshalb bei der Koomunikation nicht
     * berücksichtigt werden
     */
    private HashSet ignorierteBienenId=new HashSet();
    /** Nummer des Versuchs bei der Blumenprobeentnahme */
    private int probeEntnahmeVersuchNr=0;
    /** Max Anzahl des Versuchs bei der Blumenprobeentnahme, danach wird bei erfolgloser Nektarabbau,
     * die Blume als ohne Nektar angesehen */
    private int probeEntnahmeVersucheMax=2;
    
    /**
     * Enthält informationen über die empfangenen Blumenwiesen.
     */
    private BlumenKommInfos kommInfos=new BlumenKommInfos(bekannteBlumen);
    
    
    /** Bienen die aktuelle Blume des Agenten mit bearbeiten, und sollen deshalb bei der Berechnung
     * des alfa und beta Koeffizienten verwendet werden. */
    private HashSet bearbBlumeAgenten=new HashSet();
    
    
    /**
     * alfa-Wert ist ein Grenztwert für die Steuerung der Kommunikation,
     * in Abhängigkeit von der voraussichtl. Anzahl der Tours zu Blumenwiese.
     * Wird der alfa-Wert unterschritten, so erfolgt keine Weitergabe der Blumenkoordinate
     * an andere Bienen.
     */
    private double alfa=5; // 5 6
    
    /**
     * beta-Wert ist ein Grenzwert für die Steuerung der Koopeartion,
     * in Abhängigkeit von der voraussichtl. Anzahl der Tours zur Blumenwiese.
     * Wird der beta-Wert unterschritten, so wird ein Teil der Agenten die aktuelle Blumenwiese
     * nicht mehr bearbeiten.
     */
    private double beta=1; // 1
    
    /**
     * beschreibt den internen Zustand des deliberativen Agenten.
     */
    DesireIntentionPlan modus=new DesireIntentionPlan();
    
    /** Liste der Blumenkoordinate, die nicht weitergegeben werden sollen */
    HashSet noSend=new HashSet();
    
    /**
     * Zahl zur initialisierung des Zufallsgenerators.
     */
    private long samen = System.currentTimeMillis();
    
    /**
     * Wird zur Wahl der initialen Flugrichtung benötigt.
     */
    Random zufallsGenerator = new Random();
    
    
    /**
     * Ein Agent der für die Suche den minimalen Abstand zwischen den Blumenwiesen ausnutzt.
     * Desweiteren ist dies ein kooperativer Agent, der die Information über den Nektarinhalt einer Blumenwiese
     * für die Steuerung der Kommunikation und Kooperation ausnutzt.
     * @param bName Name des Agenten
     * @param bHandler Handler des Agenten
     */
    public MinAbstandAgent(String bName, AbstractScenarioHandler bHandler) {
        super(bName, bHandler);
        name = bName;
        handler = (IBienenstockSzenarioHandler)bHandler;
        volksID = 1;
        //zufallsGenerator.setSeed(samen);
        
    }
    
    /**
     * Ein Agent der für die Suche den minimalen Abstand zwischen den Blumenwiesen ausnutzt.
     * Desweiteren ist dies ein kooperativer Agent, der die Information über den Nektarinhalt einer Blumenwiese
     * für die Kommunikation und Kooperation ausnutzt.
     */
    public MinAbstandAgent() {
        super();
        volksID = 1;
        //  zufallsGenerator.setSeed(samen);
    }
    
    /**
     * Ein Agent der für die Suche den minimalen Abstand zwischen den Blumenwiesen ausnutzt.
     * Desweiteren ist dies ein kooperativer Agent, der die Information über den Nektarinhalt einer Blumenwiese
     * für die Kommunikation und Kooperation ausnutzt.
     * @param bId Id des Agenten
     * @param bName Name des Agenten
     */
    public MinAbstandAgent(Id bId,String bName) {
        super(bId, bName);
        name = bName;
        volksID = 1;
    }
    
    /**
     * signalisert, dass der Agent eine Run-Methode enthält.
     * @return true
     */
    public static boolean isRunableAgent() {
        return true;
    }
    
    /**
     * setzt den Handler für den Agenten
     * @param bHandler Handler für den Agenten, über den der Agent Informationen enthält und
     * ausgeführte Aktionen weiter gibt.
     */
    public void setHandler(AbstractScenarioHandler bHandler) {
        handler = (IBienenstockSzenarioHandler)bHandler;
    }
    
    /**
     * setzt den Aktionscode für den Agenten.
     * @param aktionsCode aktueller Aktinscode
     * @return neuer Aktionscode
     */
    public boolean aktionscodeSetzen(long aktionsCode) {
        aktCode = aktionsCode;
        this.scenarioParameterEinlesen();
        this.start();
        erstenAktCodeBekommen = true;
        return true;
    }
    
    /**
     * liefert die Volks-Id des Agenten
     * @return Nummer des Bienenvolkens, zu der die Biene gehört.
     */
    public int gibVolksID() {
        return volksID;
    }
    
    
    
    /* ###########################----Begin AgentenVerhalten festlegen---############################-----*/
    

    /**
     * Run-Methode, zur Ausführung des Agenten-programms als Thread.
     */
    public void run() {
        perception();
        posBienenstock = selbst.gibPosition().copy();
        modus.setDesire(modus.G_FINDEEINEBLUME);
        
        while(true){
            perception();
            //      System.out.println(id+": alter MODUS  Desire: " + modus.getDesire() + " Intention: "+ modus.getIntention());
            //   visualisiereBiene(selbst);
            evaluateGoal(modus);
            abstractPlanning(modus);
            if (out)  System.out.println(id+": neuer MODUS  Desire: " + modus.getDesire() + " Intention: "+ modus.getIntention());
            if (out && modus.getDesireZiel()!=null) System.out.println(id+": Desire Ziel " +  modus.getDesireZiel().toString());
            plan(modus);
            act(modus);
        }
    }
    
    
    
    /**
     * Nimmt die Umwelt wahr.
     */
    public void perception(){
        EinfacheKarte localMap;
        localMap = handler.infoAusschnittHolen(aktCode);
        if (localMap==null) exodus();
        this.lokaleKarte=localMap;
        selbst=this.lokaleKarte.gibSelbst();
        if (id == 0) id = selbst.gibBienenID();
        this.positionKoordinate=selbst.gibPosition();
        this.positionFeld=(EinfachesFeld)lokaleKarte.gibFelder().get(this.positionKoordinate);
        aktCode=selbst.gibAktionsCode();
        this.sichtbareFelder=lokaleKarte.gibFelder();
        // gespeicherte Felder aktualiesieren
        gespeicherteFelderAktualisieren(sichtbareFelder);
        // bekannte Blumen aktualisieren
        sucheBlumeInSichtbarenFeldern();
    }
    
    
    
    
    /**
     * Überprüft das Ziel des Agenten und wählt gegebenfalls einen Neues aus.
     * @param modus interner Zustand des Agenten als DesireInteionPlan-Typ.
     */
    public void evaluateGoal(DesireIntentionPlan modus){
        Koordinate myPosition=selbst.gibPosition();
        Koordinate neuesZiel;
        Koordinate altesZiel=modus.getDesireZiel();
        int desire=modus.getDesire();
        LinkedList randListe;
        if (this.sofortNektarAbliefernTanken) return;
        
        switch (desire){
            case DesireIntentionPlan.G_FINDEEINEBLUME:{
                LinkedList empfangListe;
                this.ignorierteBienenId.clear();
                LinkedList blumen=sucheBlumeInSichtbarenFeldern();
                LinkedList bekannteBlumenMitNektar=this.gibBekannteBlumenMitNektar();
                if ((blumen!=null) && (blumen.size()>0)) {
                    modus.setDesire(DesireIntentionPlan.G_BLUMENPROBEENTNEHMEN);
                    int anzBlumen=blumen.size()-1;
                    double zufall=Math.random();
                    if (zufall<0) zufall=zufall*(-1);
                    int myZahl=(int)Math.round(zufall*anzBlumen);
                    if (out) System.out.println(id+": MYZAHL = " + myZahl + " von max:" + blumen.size() + " zufall: " + zufall);
                    neuesZiel=(Koordinate)blumen.get(myZahl);
                    modus.setDesireZiel(neuesZiel.copy());
                    
                }else if(kommInfos.getAllKoordListe().size()>0){
                    empfangListe=kommInfos.getAllKoordListe();
                    KommInfo kommInfo=(KommInfo)empfangListe.getFirst();
                    Koordinate neueZielKoord=kommInfo.getKoordinate();
                    modus.setDesireZiel(neueZielKoord);
                    modus.setDesire(DesireIntentionPlan.G_FINDEDIEBLUME);
                    modus.setIntentionZiel(null);
                    if (this.positionKoordinate.equals(neueZielKoord)){
                        neuesZiel=this.gibNextRandKoord(neueZielKoord);
                        modus.setDesireZiel(neuesZiel.copy());
                    }
                }else if ((bekannteBlumenMitNektar!=null) && (bekannteBlumenMitNektar.size()>0)){
                    modus.setDesire(DesireIntentionPlan.G_BLUMENPROBEENTNEHMEN);
                    int anzBlumen=bekannteBlumenMitNektar.size()-1;
                    if (anzBlumen>=3) anzBlumen=3;
                    double zufall=Math.random();
                    int myZahl=(int)Math.round(zufall*anzBlumen);
                    neuesZiel=(Koordinate)bekannteBlumenMitNektar.get(myZahl);
                    modus.setDesireZiel(neuesZiel.copy());
                    
                }else{
                    if ((altesZiel==null) || (altesZiel.equals(myPosition))){ // || (myPosition.equals(this.posBienenstock))){
                        neuesZiel=this.gibtRandFeld();
                        LinkedList newRandfelder=gibNeueRandfelder();
                        if (newRandfelder.size()>0){
                            neuesZiel=(Koordinate)newRandfelder.getFirst();
                        }
                        modus.setDesireZiel(neuesZiel.copy());
                    }
                }
            }break;
            case DesireIntentionPlan.G_BLUMENPROBEENTNEHMEN:{
                if (myPosition.equals(altesZiel)){
                    InfoBlume infoBlume=(InfoBlume)bekannteBlumen.get(altesZiel);
                    if (infoBlume.getProbeEntnommen()){
                        if ((infoBlume.getNutzen()>0) && infoBlume.getHatNektar())
                            modus.setDesire(DesireIntentionPlan.G_BLUMEBEARBEITEN);
                        else{
                            modus.setDesire(DesireIntentionPlan.G_FINDEEINEBLUME);
                            modus.setDesireZiel(null);
                            evaluateGoal(modus);
                        }
                        
                    }
                }
            }break;
            case DesireIntentionPlan.G_BLUMEBEARBEITEN:{
                InfoBlume infoBlume=(InfoBlume)bekannteBlumen.get(altesZiel);
                if (!(infoBlume.getHatNektar() && infoBlume.getProbeEntnommen()
                && (infoBlume.getNutzen()>0))) {
                    modus.setDesire(DesireIntentionPlan.G_FINDEEINEBLUME);
                    modus.setDesireZiel(null);
                    evaluateGoal(modus);
                }
            }break;
            case DesireIntentionPlan.G_FINDEDIEBLUME:{
                if (myPosition.equals(altesZiel)){
                    if (bekannteBlumen.containsKey(altesZiel)){
                        modus.setDesire(DesireIntentionPlan.G_BLUMENPROBEENTNEHMEN);
                        evaluateGoal(modus);
                    }else {
                        Koordinate neueZielKoord=sucheBlumeInNachbarfelder();
                        if (neueZielKoord==null) modus.setDesire(DesireIntentionPlan.G_FINDEEINEBLUME);
                        else {
                            modus.setDesire(DesireIntentionPlan.G_BLUMENPROBEENTNEHMEN);
                            modus.setDesireZiel(neueZielKoord);
                            evaluateGoal(modus);
                        }
                    }
                    
                }
            }
        }
    }
    
    /**
     * Wandelt das Ziel des Agenten in einen abstrakten Plan (Intention) um.
     * @param modus interner Zustand des Agenten als DesireInteionPlan-Typ.
     */
    public void abstractPlanning(DesireIntentionPlan modus){
        // verlasse filter ohne neue Intention auszuwählen
        if (this.sofortNektarAbliefernTanken){
            if (out) System.out.println(id +": sofortNektarAbliefern ");
            return;
        }
        
        int newIntention;
        int oldIntention=modus.getIntention();
        Koordinate myPosition=selbst.gibPosition();
        Koordinate d_Ziel=modus.getDesireZiel();
        Koordinate i_neuesZiel;
        Koordinate i_altesZiel=modus.getIntentionZiel();
        int myHonigmege=selbst.gibGeladeneHonigmenge();
        int kostenNachHause=kostenNachHauseTanken(myPosition,this.bieneIstInDerLuft);
        int desire=modus.getDesire();
        
        switch (desire){
            case DesireIntentionPlan.G_FINDEEINEBLUME:{
                if (selbst.gibGeladeneNektarmenge()>(maxGelNektar/2)) {
                    modus.setIntention(DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN);
                } else if (myPosition.equals(this.posBienenstock) && !this.bieneIstInDerLuft) {
                    // am Bienenstock auf neue Blumenkoord warten
                    HashSet sonstBienen=positionFeld.gibIDsSonstigeBienen();
                    if (sonstBienen.contains(new Integer(id))) sonstBienen.remove(new Integer(id));
                    int sonstBienenSize=sonstBienen.size();
                    if ((kooperationsBereitschaft() || ((sonstBienenSize>0) && (this.rundeNrKooperation<=this.maxAnzahlWarteRundenFSuche)))
                    && ((this.rundeNrKooperation<=this.maxAnzahlKooperationsRundenFSuche) && (this.selbst.gibRundennummer()>5) )){
                        newIntention=DesireIntentionPlan.P_COOPERATION;
                        modus.setIntention(newIntention);
                        modus.setIntentionZiel(null);
                    }else { // nach der Kooperation
                        if (selbst.gibGeladeneHonigmenge()<(this.startHonig-this.honigStarten)) {
                            modus.setIntention(DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN);
                        } else if (this.neueMitteilungErhalten && (findeBesteMitgeteteilteBlume(null)!=null)){
                            Koordinate neueZielKoord=this.findeBesteMitgeteteilteBlume(null);
                            this.neueMitteilungErhalten=false;
                            modus.setDesireZiel(neueZielKoord);
                            modus.setDesire(DesireIntentionPlan.G_FINDEDIEBLUME);
                            modus.setIntentionZiel(null);
                        }  else {
                            this.neueMitteilungErhalten=false;
                            Koordinate neuesZiel=gibtRandFeld();
                            if (gibNeueRandfelder().size()>0){
                                neuesZiel=(Koordinate)this.gibNeueRandfelder().getFirst();
                            }
                            modus.setDesireZiel(neuesZiel.copy());
                            newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                            modus.setIntention(newIntention);
                            modus.setIntentionZiel(neuesZiel);
                        }
                    }
                }else{ // sonst, falls Biene  weder am Bienenstock ist noch Nektar geladen hat
                    this.kooperationReset();
                    newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                    i_neuesZiel=d_Ziel;
                    if (!nextIntentionOK(newIntention, i_neuesZiel)){
                        if (selbst.gibGeladeneHonigmenge()<(this.startHonig-this.honigStarten)) {
                            modus.setIntention(DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN);
                        } else {
                            if (out) System.out.println(" KANN NICHT FLIEGEN");
                        }
                    }else{
                        modus.setIntention(newIntention);
                        modus.setIntentionZiel(i_neuesZiel);
                    }
                }
            }break;
            case DesireIntentionPlan.G_BLUMENPROBEENTNEHMEN:{
                i_neuesZiel=d_Ziel;
                
                if (selbst.gibGeladeneNektarmenge()>(maxGelNektar/2)) {
                    newIntention=DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN;
                    i_neuesZiel=this.posBienenstock;
                } else if (d_Ziel.equals(myPosition)) newIntention=DesireIntentionPlan.P_NEKTARABBAUEN;
                else {
                    newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                    i_neuesZiel=d_Ziel;
                }
                if (!nextIntentionOK(newIntention, i_neuesZiel)) {
                    newIntention=DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN;
                    i_neuesZiel=this.posBienenstock;
                }
                modus.setIntention(newIntention);
                modus.setIntentionZiel(i_neuesZiel);
            }break;
            
            /*   Bearbeitung einer Blume                */
            case DesireIntentionPlan.G_BLUMEBEARBEITEN:{
                if (i_altesZiel==null)  {
                    modus.setIntentionZiel(d_Ziel);
                    i_altesZiel=d_Ziel;
                }
                if (myPosition.equals(d_Ziel)){ // an der Blume
                    kooperationReset();
                    addIgnoredBienen();
                    newIntention=DesireIntentionPlan.P_NEKTARABBAUEN;
                    if ((selbst.gibGeladeneNektarmenge()==this.maxGelNektar) ||
                            (!nextIntentionOK(newIntention, myPosition))) {
                        newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                        modus.setIntention(newIntention);
                        modus.setIntentionZiel(this.posBienenstock);
                    }else {
                        newIntention=DesireIntentionPlan.P_NEKTARABBAUEN;
                        modus.setIntention(newIntention);
                    }
                }else if (myPosition.equals(this.posBienenstock)){ // am Bienenstock
                    InfoBlume blume=(InfoBlume) this.bekannteBlumen.get(d_Ziel);
                    double vorsAnzTours=getVoraussichtlAnzTours(blume);
                    int anzAgenten=getAnzAgenten(blume);
                    
                    HashSet sonstBienen= this.positionFeld.gibIDsSonstigeBienen();
                    sonstBienen.remove(new Integer(id));
                    sonstBienen.removeAll(this.ignorierteBienenId);
                    int sonstBienenSize=sonstBienen.size();
                    
                    if ((selbst.gibGeladeneNektarmenge()>0) ||
                            ((selbst.gibGeladeneHonigmenge()<(this.startHonig-this.honigStarten))
                            && ((this.rundeNrKooperation>this.maxAnzahlKooperationsRunden)
                            || (this.rundeNrKooperation==0)))){
                        //System.out.println(id+": Nektar abliefertn tanken  Nr.Coop" + rundeNrKooperation );
                        newIntention=DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN;
                        modus.setIntention(newIntention);
                        /*   KOOPERATION   */
                    } else if ((kooperationsBereitschaft() || ((this.rundeNrKooperation<=this.maxAnzahlWarteRunden)
                    && (sonstBienenSize>0))) // sonstBienenSize>0 wenn es andere Bienen gibt die gerade tanken, oder nektarabliefern
                    && (this.rundeNrKooperation<=this.maxAnzahlKooperationsRunden) && !this.bieneIstInDerLuft){
                        newIntention=DesireIntentionPlan.P_COOPERATION;
                        modus.setIntention(newIntention);
                        if ((vorsAnzTours<alfa) || noSend.contains(d_Ziel)) { // keine Weitergabe der Blumenkoord
                            System.out.println(id + ": voraussichtl ANZAHL der TOURS " + vorsAnzTours + "  zu " + blume.getBlumenKoordinate()  );
                            modus.setIntentionZiel(null);
                        } else modus.setIntentionZiel(d_Ziel);
                    }else {
                        if (this.neueMitteilungErhalten){
                            // String my="false";
                            // if (this.kooperationsBereitschaft()) my="true";
                            // System.out.println(id+ ": neue mitteilung : KOOPERATIONSBEREITSCHAFT--------------> " + my);
                            Koordinate neueZielKoord;
                            Koordinate alteZielKoord;
                            System.out.println(id + ": voraussichtl ANZAHL der TOURS " + vorsAnzTours + "  zu " + blume.getBlumenKoordinate()  );
                            /* 1/2 der Agenten sollen eine andere Blume wählen, da Nektar wird knapp */
                            if (vorsAnzTours<beta) {
                                // die Hälfte der Agenten sollen ihr Ziel wechseln
                                double zufall=Math.random();
                                if (zufall<=0.5 || anzAgenten<4) {
                                    alteZielKoord=d_Ziel;
                                    noSend.add(d_Ziel);
                                } else{ // wechsle Ziel
                                    alteZielKoord=null;
                                    blume.setHatNektar(false);
                                    // System.out.println(id+ ":+ set NONEKTAR "  + blume.getBlumenKoordinate());
                                }
                            } else alteZielKoord=d_Ziel;
                            
                            neueZielKoord=this.findeBesteMitgeteteilteBlume(alteZielKoord);
                            this.neueMitteilungErhalten=false;
                            if (neueZielKoord.equals(d_Ziel)){ // fliege zurück zur Blume
                                newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                                modus.setIntention(newIntention);
                                modus.setIntentionZiel(d_Ziel);
                            } else{ // suche neue mitgeteilte Koordinate
                                modus.setDesireZiel(neueZielKoord);
                                modus.setDesire(DesireIntentionPlan.G_FINDEDIEBLUME);
                                modus.setIntentionZiel(null);
                            }
                        } else{ // wenn keine neue Mitteilung erhalten
                            
                            double zufall=Math.random();
                            if ((vorsAnzTours<beta)  && ((zufall>0.5) && (anzAgenten>3))) {
                                /*  Suche eine andere Blume  */
                                blume.setHatNektar(false);
                                LinkedList empfangListe;
                                empfangListe=kommInfos.getReducedKoordListe();
                                if (empfangListe.size()>0){
                                    KommInfo kommInfo=(KommInfo)empfangListe.getFirst();
                                    Koordinate neueZielKoord=kommInfo.getKoordinate();
                                    modus.setDesireZiel(neueZielKoord);
                                    modus.setDesire(DesireIntentionPlan.G_FINDEDIEBLUME);
                                    modus.setIntentionZiel(null);
                                    
                                }else{
                                    modus.setDesire(DesireIntentionPlan.G_FINDEEINEBLUME);
                                    modus.setDesireZiel(null);
                                    modus.setIntentionZiel(null);
                                }
                                
                                this.evaluateGoal(modus);
                                this.abstractPlanning(modus);
                            }else{// fliege zur Blume
                                newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                                modus.setIntention(newIntention);
                                i_neuesZiel=d_Ziel;
                                modus.setIntentionZiel(i_neuesZiel);
                                
                            }
                        }
                    }
                } else kooperationReset();
            }break;
            case DesireIntentionPlan.G_FINDEDIEBLUME:{
                Set gespKoord=this.gespeicherteFelder.keySet();
                newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                modus.setIntention(newIntention);
                i_neuesZiel=i_altesZiel;
                // falls zur Koordinate keein Weg existiert, so werden Randknoten zur Nav verwendet.
                if (i_altesZiel==null) {
                    if (gespKoord.contains(d_Ziel)){
                        i_neuesZiel=d_Ziel;
                        i_altesZiel=i_neuesZiel;
                    }else{
                        i_neuesZiel=this.findeRandKoord(d_Ziel);
                        i_altesZiel=i_neuesZiel;
                    }
                }
                
                if (gespKoord.contains(d_Ziel)) {
                    i_neuesZiel=d_Ziel;
                    i_altesZiel=d_Ziel;
                }else if ((myPosition.equals(i_altesZiel)) && (!d_Ziel.equals(i_altesZiel))){
                    i_neuesZiel=this.findeRandKoord(d_Ziel);
                    i_altesZiel=i_neuesZiel;
                }
                modus.setIntentionZiel(i_neuesZiel);
                if (!nextIntentionOK(newIntention, i_neuesZiel) ||
                        (selbst.gibGeladeneNektarmenge()> this.maxGelNektar/2)){
                    modus.setIntention(DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN);
                }
                
            }break;
        }
        
    }
    
    /**
     * wandelt den Abstrakten Plan des Agenten in einen konkreten Plan um.
     * @param modus interner Zustand des Agenten als DesireInteionPlan-Typ.
     */
    public void plan(DesireIntentionPlan modus){
        int intention=modus.getIntention();
        int actionNr;
        Koordinate i_ziel=modus.getIntentionZiel();
        LinkedList planListe=modus.getPlanListe();
        Koordinate myPosition=selbst.gibPosition();
        ActionTargetPair atp;
        switch (intention){
            case DesireIntentionPlan.P_FLIEGENZURKOORDINATE:{
                Koordinate zielKoord=new Koordinate(-1,-1);
                if ((planListe!=null) && (planListe.size()>0)) zielKoord=(Koordinate)((ActionTargetPair)planListe.getLast()).getTarget();
                if ((planListe==null)  || (!(zielKoord.equals(i_ziel)))){
                    planListe=new LinkedList();
                    if (!this.bieneIstInDerLuft) {
                        actionNr=DesireIntentionPlan.A_STARTEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                    }
                    LinkedList weg=this.kuerzesterWeg(myPosition, i_ziel, this.gespeicherteFelder);
                    actionNr=DesireIntentionPlan.A_FLIEGEN;
                    if (weg==null) { /* darf nicht passieren, da nur Koordinate mitgeteilt
                     werden zu denen ein Weg existierr*/
                        if (out) System.out.println(id+" KEIN WEG zum fliegen nach " + i_ziel.toString() );
                    } else{
                        Iterator it=weg.iterator();
                        while(it.hasNext()){
                            Koordinate aKoord=(Koordinate)it.next();
                            atp=new ActionTargetPair(actionNr,aKoord.copy());
                            planListe.add(atp);
                        }
                    }
                    
                    modus.setPlanListe(planListe);
                }
            }break;
            case DesireIntentionPlan.P_NEKTARABBAUEN:{
                planListe=new LinkedList();
                if (this.bieneIstInDerLuft) {
                    actionNr=DesireIntentionPlan.A_LANDEN;
                    atp=new ActionTargetPair(actionNr,myPosition.copy());
                    planListe.add(atp);
                }
                actionNr=DesireIntentionPlan.A_NEKTARABBAUEN;
                atp=new ActionTargetPair(actionNr,myPosition.copy());
                planListe.add(atp);
                modus.setPlanListe(planListe);
            }break;
            case DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN:{
                this.sofortNektarAbliefernTanken=true;
                boolean nektarAbgeliefert=false;
                boolean getankt=false;
                planListe=new LinkedList();
                if (myPosition.equals(this.posBienenstock)){
                    if (this.bieneIstInDerLuft) {
                        actionNr=DesireIntentionPlan.A_LANDEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                    }
                    if (selbst.gibGeladeneHonigmenge() < (honigTanken + honigAbliefern)) {
                        actionNr=DesireIntentionPlan.A_TANKEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                        getankt=true;
                    }
                    if (selbst.gibGeladeneNektarmenge() > 0) {
                        actionNr=DesireIntentionPlan.A_NEKTARABLIEFERN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                        nektarAbgeliefert=true;
                    }
                    if (nektarAbgeliefert || (selbst.gibGeladeneHonigmenge()<(this.startHonig-honigStarten))){
                        actionNr=DesireIntentionPlan.A_TANKEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                    }
                    modus.setPlanListe(planListe);
                    // sofortNektarAbliefernTanken zurücksetzen
                    if (sofortNektarAbliefernTanken && ((selbst.gibGeladeneNektarmenge()==0) &&
                            (selbst.gibGeladeneHonigmenge()>=(startHonig-honigStarten)))) sofortNektarAbliefernTanken=false;
                    
                }else {
                    if (!this.bieneIstInDerLuft) {
                        actionNr=DesireIntentionPlan.A_STARTEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                    }
                    LinkedList weg=kuerzesterWeg(myPosition, this.posBienenstock, this.gespeicherteFelder);
                    actionNr=DesireIntentionPlan.A_FLIEGEN;
                    Iterator it=weg.iterator();
                    while(it.hasNext()){
                        Koordinate aKoord=(Koordinate)it.next();
                        atp=new ActionTargetPair(actionNr,aKoord.copy());
                        planListe.add(atp);
                    }
                    modus.setPlanListe(planListe);
                }
            }break;
            case DesireIntentionPlan.P_COOPERATION:{
                int tanzendeBieneId;
                int wartendeBieneId;
                // TODO Prüfen was für bienen da sind (wartende, oder tanzende)
                // prüfen ob genug honig da ist
                // Kooperationsbereitschaft für max. 5. Runden erhalten.
                
                planListe=this.kooperationsProtokoll(modus, i_ziel);
                modus.setPlanListe(planListe);
                
                
            } break;
        }
    }
    
    /**
     * führt jeweilst erste Aktion aus der planListe aus.
     * @param modus interner Zustand des Agenten als DesireInteionPlan-Typ.
     */
    public void act(DesireIntentionPlan modus){
        LinkedList planListe=modus.getPlanListe();
        if ((planListe==null) || (planListe.size()==0)) return;
        ActionTargetPair act;
        act=(ActionTargetPair)planListe.getFirst();
        planListe.removeFirst();
        int aktionNr=act.getAction();
        if (aktionNr!=DesireIntentionPlan.A_NEKTARABBAUEN) probeEntnahmeVersuchNr=0;
        Koordinate ziel=(Koordinate)act.getTarget();
        switch (aktionNr){
            case DesireIntentionPlan.A_FLIEGEN:{
                fliegen(ziel);
            }break;
            case DesireIntentionPlan.A_STARTEN:{
                starten();
            }break;
            case DesireIntentionPlan.A_LANDEN:{
                landen();
            }break;
            case DesireIntentionPlan.A_TANKEN:{
                tanken();
            }break;
            case DesireIntentionPlan.A_WARTEN:{
                warten();
            }break;
            case DesireIntentionPlan.A_NEKTARABBAUEN:{
                int nektarAnf=selbst.gibGeladeneNektarmenge();
                nektarAbbauen();
                probeEntnahmeVersuchNr++;
                EinfacheKarte localMap = handler.infoAusschnittHolen(aktCode);
                selbst=localMap.gibSelbst();
                int nektarNachAbbau=selbst.gibGeladeneNektarmenge();
                int ausbeute=nektarNachAbbau-nektarAnf;
                if ((nektarAnf!=this.maxGelNektar) && (erfolg.getValue() ||
                        (probeEntnahmeVersuchNr>probeEntnahmeVersucheMax))){
                    InfoBlume infoBlume=(InfoBlume)bekannteBlumen.get(ziel);
                    if (!infoBlume.getProbeEntnommen()) {
                        infoBlume.setProbeEntnommen();
                        infoBlume.setAusbeuteProRunde(ausbeute);
                        berechneSetzeNutzen(infoBlume);
                    }
                    if ((ausbeute==0)) {
                        infoBlume.setHatNektar(false);
                    }
                }
            }break;
            case DesireIntentionPlan.A_NEKTARABLIEFERN:{
                this.nektarAbliefern();
            }break;
            case DesireIntentionPlan.A_TANZEN:{
                this.tanzen(ziel);
            }break;
            case DesireIntentionPlan.A_ZUSCHAUEN:{
                Koordinate caBlumenKoordinate=null;
                int zielId=act.getZielAgentId();
                caBlumenKoordinate=this.zuschauen(zielId);
                Info info=selbst.gibInformation();
                if ((caBlumenKoordinate!=null) && (info!=null)){
                    double nutzen=selbst.gibInformation().gibNutzen();
                    mitgeteilteBlumenKoord.put(caBlumenKoordinate,new Double(nutzen));
                    kommInfos.addKommInfo(caBlumenKoordinate.copy(), nutzen, selbst.gibRundennummer());
                    this.neueMitteilungErhalten=true;
                }
            }
            
        }
        
        
    }
    
    /* ####################### Ende der Festlegung von AgentenVerhalten ########### */
    
    /* ############################    HilfsFunktionen   ######################## */
    
    
    /**
     * setzt die Variablen für Kooperation zurück
     */
    private void kooperationReset(){
        if (getanzteBienen.size()>0 )this.getanzteBienen.clear();
        this.mitgeteilteBlumenKoord.clear();
        this.getanzt=false;
        this.rundeNrKooperation=0;
        this.neueMitteilungErhalten=false;
    }
    
    /**
     * auswahl der richtigen Kooperation: (tanzen oder zuschauen, wenn zuschauen, wem zuschauen)
     *
     * @param modus interner Zustand des Agenten als DesireIntionPlan-Typ.
     * @param blumenKoord Koordinate der mitzuteilenden Blume, ist diese nur, wird nur zugeschaut.
     * @return Liste der nächsten auszuführenden Aktion.
     */
    private LinkedList kooperationsProtokoll(DesireIntentionPlan modus, Koordinate blumenKoord){
        int tanzendeBieneId;
        int wartendeBieneId;
        int actionNr;
        ActionTargetPair atp;
        LinkedList planListe;
        
        this.rundeNrKooperation++;
        if ((this.tanzendeBienen!=null) && (this.tanzendeBienen.size()>0)){
            LinkedList tanzBienen=new LinkedList();
            double zufall=Math.random();
            if (zufall<0) zufall=zufall*(-1);
            int tztBienenSize=this.tanzendeBienen.size();
            tztBienenSize=tztBienenSize-1;
            int auswahl=(int)Math.round(zufall*tztBienenSize);
            tanzBienen.addAll(tanzendeBienen);
            tanzendeBieneId=((Integer)tanzBienen.get(auswahl)).intValue();
            planListe=new LinkedList();
            actionNr=DesireIntentionPlan.A_ZUSCHAUEN;
            getanzteBienen.addAll(this.tanzendeBienen);
            if (getanzt) this.ignorierteBienenId.addAll(getanzteBienen);
            
            // getanzteBienen.add(new Integer(tanzendeBieneId));
            atp=new ActionTargetPair(actionNr,tanzendeBieneId);
            planListe.add(atp);
            return planListe;
        }else if((this.wartendeBienen!=null) && (this.wartendeBienen.size()>0) && (blumenKoord!=null) && (!getanzt)){
            int myId=this.id;
            wartendeBieneId=((Integer)this.wartendeBienen.iterator().next()).intValue();
            HashSet nichtGetanzteBienen=new HashSet(wartendeBienen);
            nichtGetanzteBienen.removeAll(getanzteBienen);
            Iterator it=nichtGetanzteBienen.iterator();
            int nichtGetztSize=nichtGetanzteBienen.size();
            int aBieneId=10000;
            if (nichtGetztSize<=1) {
                planListe=new LinkedList();
                actionNr=DesireIntentionPlan.A_TANZEN;
                atp=new ActionTargetPair(actionNr,blumenKoord.copy());
                planListe.add(atp);
                getanzt=true;
                return planListe;
            } else {
                double zufall=Math.random();
                if (zufall<0) zufall=zufall*(-1);
                double myZahl=1.5/(nichtGetztSize);
                
                if (zufall<=myZahl){
                    planListe=new LinkedList();
                    actionNr=DesireIntentionPlan.A_TANZEN;
                    atp=new ActionTargetPair(actionNr,blumenKoord.copy());
                    planListe.add(atp);
                    getanzt=true;
                    return planListe;
                } else {
                    planListe=new LinkedList();
                    actionNr=DesireIntentionPlan.A_WARTEN;
                    atp=new ActionTargetPair(actionNr,blumenKoord.copy());
                    planListe.add(atp);
                    getanzt=true;
                    return planListe;
                }
                
                
            }
        } else {
            HashSet nichtGetanzteBienen=new HashSet(wartendeBienen);
            nichtGetanzteBienen.removeAll(getanzteBienen);
            
            if (getanzt && (nichtGetanzteBienen.size()==0)){
                /* Beenede die Kooperation, da alle haben getanzt */
                this.rundeNrKooperation=this.maxAnzahlKooperationsRunden+1;
                return new LinkedList();
            } else{
                planListe=new LinkedList();
                actionNr=DesireIntentionPlan.A_WARTEN;
                atp=new ActionTargetPair(actionNr,blumenKoord);
                planListe.add(atp);
                return planListe;
                
            }
        }
    }
    
    
    /**
     * prüft ob es auf dem aktuellen Feld wartende oder tanzende Bienen gibt
     *
     *
     * @return true, falls es tanzende oder wartende Bienen gibt.
     */
    private boolean kooperationsBereitschaft(){
        boolean bedarfWartende=false;
        boolean bedarfTanzende=false;
        this.wartendeBienen=this.positionFeld.gibIDsWartendeBienen();
        this.tanzendeBienen=this.positionFeld.gibIDsTanzendeBienen();
        if (wartendeBienen!=null) {
            wartendeBienen.remove(new Integer(this.id));
            wartendeBienen.removeAll(this.ignorierteBienenId);
            if (wartendeBienen.size()>0) bedarfWartende=true;
        }
        if (tanzendeBienen!=null) {
            tanzendeBienen.remove(new Integer(this.id));
            tanzendeBienen.removeAll(this.ignorierteBienenId);
            if (tanzendeBienen.size()>0) bedarfTanzende=true;
        }
        if ((bedarfWartende || bedarfTanzende) && (this.maxAnzahlKooperationsRunden >= this.rundeNrKooperation)) return true;
        else return false;
        
    }
    
    /**
     * überprüft, ob die nächste auszuführende Absicht durchgeführt werden kann.
     *
     * @param intention zu überprüfende Absicht
     * @param i_koordinate ZielKoordinate der Absicht.
     * @return true, falls die auszuführende Absicht durchführbar ist.
     */
    private boolean nextIntentionOK(int intention, Koordinate i_koordinate){
        if (i_koordinate==null) i_koordinate=selbst.gibPosition();
        int myHonig=selbst.gibGeladeneHonigmenge();
        Koordinate myPosition=selbst.gibPosition();
        // int kostenVonHierNachHause=this.kostenNachHauseTanken(selbst.gibPosition(),this.bieneIstInDerLuft);
        int kosten=10000;
        switch (intention){
            case DesireIntentionPlan.P_FLIEGENZURKOORDINATE:{
                if (i_koordinate.equals(posBienenstock)) return true;
                kosten=kostenFliegen(myPosition, i_koordinate) + kostenNachHauseTanken(i_koordinate, this.bieneIstInDerLuft);
            }break;
            case DesireIntentionPlan.P_NEKTARABBAUEN: {
                kosten=kostenNachHauseTanken(i_koordinate, false)+this.honigAbbauen;
                if (this.bieneIstInDerLuft) kosten=kosten + this.honigLanden;
                
            }break;
            case DesireIntentionPlan.P_NACHHAUSETANKEN: return true;
            case DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN: return true;
            case DesireIntentionPlan.P_ZURUECKZURBLUME: {
                kosten=kostenFliegen(myPosition, i_koordinate) + kostenNachHauseTanken(i_koordinate, this.bieneIstInDerLuft);
                if (!this.bieneIstInDerLuft) kosten=kosten + this.honigStarten;
                kosten=2*this.honigAbbauen;
            }
        }
        if (kosten>myHonig) {
            return false;
        } else return true;
        
    }
    
    /**
     * sucht aus den übermittelten Blumenkoordinaten die bessere (als seine eigene) aus.
     * Falls es keine Bessere gibt, so wird die Koordinate der aktuellen Blume übermittelt.
     * @param aktuelleBlume Koordinate der aktuellen zu bearbeitenden Blume
     * @return Koordinate der ausgewählten Blume
     */
    private Koordinate findeBesteMitgeteteilteBlume(Koordinate aktuelleBlume){
        InfoBlume infoBlume;
        double myNutzen=0;
        if (aktuelleBlume!=null){
            infoBlume=(InfoBlume)this.bekannteBlumen.get(aktuelleBlume);
            myNutzen=infoBlume.getNutzen();
        }
        Koordinate returnKoord;
        double letzteSichereNutzen=0;
        double maxNutzen=0;
        LinkedList kandidaten=new LinkedList();
        TreeSet sortKoord=new TreeSet();
        boolean noNektar;
        Iterator setIt=this.mitgeteilteBlumenKoord.entrySet().iterator();
        while(setIt.hasNext()){
            noNektar=false;
            Entry entry=(Entry)setIt.next();
            Koordinate koord=((Koordinate)entry.getKey()).copy();
          /*  Iterator blumenIt=bekannteBlumen.values().iterator();
            while(blumenIt.hasNext()){
                InfoBlume aBlume=(InfoBlume)blumenIt.next();
                Koordinate blumenKoord=aBlume.getBlumenKoordinate();
                if ((!aBlume.getHatNektar()) && abstand(koord,blumenKoord)<1.5) {
                    noNektar=true;
                    break;
                }
            }
            if (noNektar) continue; // falls die mitgeteilte Blume kein kein Nektar mehr hat
           */
            double nutzen=((Double)entry.getValue()).doubleValue();
            KoordWertPaar koordWertPaar=new KoordWertPaar(koord,nutzen);
            if (nutzen>myNutzen) sortKoord.add(koordWertPaar);
        }
        if (sortKoord.size()>0){
            double zufall=Math.random();
            if (zufall<0) zufall=zufall*(-1);
            if (aktuelleBlume!=null){
                KoordWertPaar koordWertPaar=new KoordWertPaar(aktuelleBlume,myNutzen);
                sortKoord.add(koordWertPaar);
            }
            int sortSize=sortKoord.size();
            sortSize=sortSize-1;
            int myAuswahl=(int)Math.round(zufall*sortSize);
            LinkedList liste=new LinkedList(sortKoord);
            LinkedList reverseList=new LinkedList();
            // Achtung, die Koordinaten mit dem kleineren Nutzen-Wert sind vorne
            returnKoord=((KoordWertPaar)liste.get(myAuswahl)).getKoordinate();
            
            //beste Blume ist am Ende der Liste
            //returnKoord=((KoordWertPaar)liste.getLast()).getKoordinate();
            
            
            
            return returnKoord;
        }else return aktuelleBlume;
    }
    
    
     /**
     * gibt die Kosten an, um von der vorgegebenen Position zurück zum Bienenstock zu fliegen
     * und dort zu tanken.
     * @return Kosten um zum Bienenstock zurück zu kehren und zu tanken.
     * @param vonPosition Berechnung der Kosten von der vorgebenen Position
     * @param bieneIstInLuft externer Zustand des Agenten.
     */
    private int kostenNachHauseTanken(Koordinate vonPosition, boolean bieneIstInLuft){
        int kosten;
        int kostenFliegen;
        if (vonPosition.equals(this.posBienenstock)){
            if (bieneIstInLuft) return (this.honigLanden+this.honigTanken);
            else return this.honigTanken;
        }else{
            kostenFliegen=kostenFliegen(vonPosition, this.posBienenstock);
            if (kostenFliegen>this.startHonig) kostenFliegen=kostenFliegen(this.posBienenstock, vonPosition);
            kosten=this.honigLanden+this.honigTanken + this.reserve + kostenFliegen;
            if (bieneIstInLuft) return kosten;
            else return (kosten+this.honigStarten);
        }
    }
    
    
    
    
    /**
     * Sucht die Blume in sichtbaren Feldern und gibt eine Liste der gefundenen
     * Blumen in einer sortierten (nach der Entfernung zum Bienenstock) Liste  aus.
     *
     * @return gibt eine (nach der Entfernung zum Bienenstock) sortierte Liste
     * der gefundenen BlumenKoordinaten, oder null, falls keine Blumen gefunden wurden.
     */
    private LinkedList sucheBlumeInSichtbarenFeldern(){
        LinkedList priorityWSBlumen=null;
        LinkedList retValue=null;
        boolean gefunden=false;
        LinkedList gefundeneBlumenKoord=new LinkedList();
        Set bekannteBlumenKoord=this.bekannteBlumen.keySet();
        Iterator keyIt=this.sichtbareFelder.keySet().iterator();
        while(keyIt.hasNext()){
            Koordinate keyKoord=(Koordinate)keyIt.next();
            EinfachesFeld aFeld=(EinfachesFeld)this.sichtbareFelder.get(keyKoord);
            if (!(aFeld instanceof EinfacheBlume)) continue;
            if (blumeIstAttraktiv(aFeld)){
                gefundeneBlumenKoord.add(aFeld.gibPosition());
                gefunden=true;
            }
        }
        if (gefunden){
            priorityWSBlumen=this.sortKoordinaten(gefundeneBlumenKoord,this.posBienenstock);
            retValue=this.sortKoordinaten(priorityWSBlumen,this.selbst.gibPosition());
            //  retValue=priorityWSBlumen;
            return retValue;
        } else {
            return null;
        }
    }
    
    /**
     * liefert bekannte Blumen mit Nektar
     * @return bekannte Blumen mit Nektar
     */
    private LinkedList gibBekannteBlumenMitNektar(){
        LinkedList liste=new LinkedList();
        InfoBlume infoBlume;
        Koordinate posBlume;
        TreeSet sortNutzen=new TreeSet();
        LinkedList mitProbe=new LinkedList();
        KoordWertPaar koordWertPaar;
        Iterator it=this.bekannteBlumen.entrySet().iterator();
        double nutzen;
        while (it.hasNext()){
            Entry entry=(Entry)it.next();
            infoBlume=(InfoBlume)entry.getValue();
            if (infoBlume.getHatNektar()){
                if (infoBlume.getProbeEntnommen()) {
                    if (infoBlume.getNutzen()>0) {
                        koordWertPaar=new KoordWertPaar(infoBlume.getBlumenKoordinate(),infoBlume.getNutzen());
                        sortNutzen.add(koordWertPaar);
                        liste.add(infoBlume.getBlumenKoordinate());
                    }
                }else liste.add(infoBlume.getBlumenKoordinate());
            }
        }
        LinkedList gefundeneBlumenKoord=liste;
        LinkedList priorityWSBlumen;
        LinkedList retValue;
        priorityWSBlumen=this.sortKoordinaten(gefundeneBlumenKoord,this.posBienenstock);
        retValue=this.sortKoordinaten(priorityWSBlumen,this.selbst.gibPosition());
        return retValue;
    }
    
    /**
     * Sucht Blume in Nachbar-Feldern
     * @return Koordinate der gefundenen Blume oder null, falls keine Blume gefunden wurde
     */
    private Koordinate sucheBlumeInNachbarfelder(){
        Koordinate posBlume=null;
        boolean blumeGefunden=false;
        Iterator itNachbarn = positionFeld.gibNachbarfelder().values().iterator();
        while (itNachbarn.hasNext()) {
            EinfachesFeld einfachesFeld = (EinfachesFeld)itNachbarn.next();
            if (einfachesFeld instanceof EinfacheBlume && blumeIstAttraktiv(einfachesFeld)) {
                posBlume = einfachesFeld.gibPosition().copy();
                blumeGefunden = true;
            }
        }
        return posBlume;
    }
    
    
    /** sucht nach Feldern die nicht erforscht wurden und am nächsten zur Biene
     * und am nächsten zum Bienenstock sich befindet.
     * @return KoordinateListe der Rand-Feldern, sortiert nach der Entfernung zur gegenwärtigen Position
     * und zur Entfernung zum Bienenstocks.
     */
    private LinkedList sucheRandFeld(){
        //  Suche RandFelder
        LinkedList randFelder=new LinkedList();
        Iterator itMap=this.gespeicherteFelder.entrySet().iterator();
        while (itMap.hasNext()){
            Map.Entry aMap=(Map.Entry)itMap.next();
            EinfachesFeld einfachesFeld=(EinfachesFeld)aMap.getValue();
            if (einfachesFeld==null) randFelder.add((Koordinate)aMap.getKey());
        }
        int size=randFelder.size();
        if (size==0) {
            // liefere zufälliges Nachbarfeld
            Koordinate position=selbst.gibPosition();
            EinfachesFeld aFeld=(EinfachesFeld)this.lokaleKarte.gibFelder().get(position);
            LinkedList nachbarFelder=new LinkedList(aFeld.gibNachbarfelder().keySet());
            int anzFelder=nachbarFelder.size();
            return nachbarFelder;
        }else{
             /*  Koordinaten sortiert, nach der Entfernung
              sowohl zur Biene als auch zum Bienenstock  */
            
            LinkedList sortRandFelderBienenstock;
            LinkedList sortRandFelder;
            
            sortRandFelderBienenstock=this.sortKoordinaten(randFelder,this.posBienenstock);
            sortRandFelder=this.sortKoordinaten(sortRandFelderBienenstock,this.selbst.gibPosition());
            
            return sortRandFelder;
        }
    }
    
    /**
     * sucht aus den Randfeldern ein Feld raus.
     * @return Koordinate eines Rand-Feldes.
     */
    private Koordinate gibtRandFeld(){
        double zufall=Math.random();
        int index;
        if (zufall<0) zufall=zufall*(-1);
        LinkedList randListe=sucheRandFeld();
        int anzBienen=this.positionFeld.gibIDsFliegendeBienen().size()+
                this.positionFeld.gibIDsWartendeBienen().size()+
                this.positionFeld.gibIDsTanzendeBienen().size()+
                this.positionFeld.gibIDsSonstigeBienen().size();
        int anzRander=randListe.size();
        int minVal;
        minVal=anzRander;
        minVal= Math.min(anzBienen,anzRander);
        
        if (minVal>0) minVal=minVal-1;
        int myWahl=(int) Math.round((minVal) * zufall);
        //          System.out.println(id+ ": FINDEBLUME Zufall "+ zufall + " mw "+ myWahl + " minVal :" + minVal);
        Koordinate neuesZiel=(Koordinate)randListe.get(myWahl);
        return neuesZiel;
    }
    
    
    /**
     * Prüft ob die Blume bakannt ist und ob die Blume für dan Agenten attraktiv ist.
     *
     * @param blume zu untersuchende Blume
     * @return true, falls die blume Nektar hat und ihre Entfernung zum Bienenstock
     *  einen kritischen Wert nicht übersteigt.
     *
     */
    private boolean blumeIstAttraktiv(EinfachesFeld blume){
        InfoBlume infoBlume;
        Koordinate posBlume=blume.gibPosition();
        if (!(blume instanceof EinfacheBlume)) return false;
        EinfacheBlume eBlume=((EinfacheBlume)blume);
        if (bekannteBlumen.keySet().contains(posBlume)){
            infoBlume=(InfoBlume)bekannteBlumen.get(posBlume);
            if (this.positionKoordinate.equals(posBlume) && eBlume.gibNektarAuslesbar()){
                infoBlume.aktualisiereNektarInhalt(eBlume.gibVorhandenerNektar(), this.selbst.gibRundennummer());
            }
            if (infoBlume.getHatNektar()){
                if (infoBlume.getProbeEntnommen()) return (infoBlume.getNutzen()>0);
                else return true;
            } else return false;
        }else {
            int entfernung=kuerzesterWeg(this.posBienenstock,posBlume, this.gespeicherteFelder).size();
            infoBlume=new InfoBlume(posBlume, entfernung);
            if (this.positionKoordinate.equals(posBlume) && eBlume.gibNektarAuslesbar()){
                infoBlume.aktualisiereNektarInhalt(eBlume.gibVorhandenerNektar(), this.selbst.gibRundennummer());
            }
            bekannteBlumen.put(posBlume.copy(), infoBlume);
            if (entfernung>this.kritEntfernung) {
                infoBlume.setProbeEntnommen();
                infoBlume.setNutzen(0);
                infoBlume.setHatNektar(false);
                return false;
            } else return true;
        }
        
    }
    
    
    /**
     * Berechnet den Nutzen der Blume in abhängigkeit von ihrer Ausbeute und Enfernung zum Bienenstock.
     *
     * @param blume Informationen zu einer Blume, wobei die Probe entnommen sein muss,
     * Ausbeute-NektarProRunde und die Entfernung zum Bienenstock bereits eingetragen sein muessen.
     */
    private void berechneSetzeNutzen(InfoBlume blume){
        if (!blume.getProbeEntnommen()) return;
        int ausbeute=blume.getAusbeuteProRunde();
        if (ausbeute==0) {
            blume.setNutzen(0);
            return;
        }
        int entfernung=blume.getEntfernungZumBienenstock();
        int kosten0=2*(honigStarten + honigLanden + entfernung*honigFliegen)+ honigTanken + honigAbliefern;
        int maxAnzAbbau=(int) Math.ceil(maxGelNektar/ausbeute);
        int anzMoeglAbbau=(int) Math.floor((this.startHonig-kosten0)/honigAbbauen);
        int tatsAbbau=Math.min(anzMoeglAbbau, maxAnzAbbau);
        int tatsKosten=kosten0  + (tatsAbbau * honigAbbauen);
        double nutzen=(tatsAbbau * ausbeute * kursNektarHonig) - tatsKosten;
        blume.setNutzen(nutzen);
    }
    
    
    
    
    
    /**
     * Berechnet die HonigKosten um von einer Position zur anderen Position zu fliegen
     * @param startPosition Start-Position
     * @param zielPosition Ziel-Position
     * @return Kosten um von startPositin zur zielPosition zu gelangen.
     */
    private int kostenFliegen(Koordinate startPosition, Koordinate zielPosition){
        int honigKosten=10000;
        LinkedList weg=this.kuerzesterWeg(startPosition,
                zielPosition,
                this.gespeicherteFelder);
        if (weg!=null){
            honigKosten=this.honigFliegen*(weg.size());
        }
        return honigKosten;
    }
    
    /**
     * wird aufgerufen, wenn der Agent aus dem Spiel
     * genommen wurde.
     */
    private void exodus(){
        if (out) System.out.println(id + ": ich bin nicht mehr im Spiel! ");
        synchronized (selbst) {
            try {
                selbst.wait();
            } catch (Exception e) {}
        }
    }
    
    
    /**
     * eine Prozedur die am Bienenstock ausgeführt wird und sorgt dafür,
     * dass Biene getankt wird und ihren Nektar abliefert.
     */
    private void amBienenstock() {
        if (this.bieneIstInDerLuft) landen();
        if (selbst.gibGeladeneHonigmenge() < (honigTanken + honigAbliefern)) {
            tanken();
        }
        if (selbst.gibGeladeneNektarmenge() > 0) {
            this.nektarAbliefern();
        }
        tanken();
    }
    
    /**
     * fügt der ignorierteBienenId Liste die Ids der ignorierten Bienen hinzu, um diese
     * bei der Kommunikation nicht zu berücksichtigen.
     */
    private void addIgnoredBienen(){
        if (this.positionFeld instanceof EinfacheBlume){
            if (selbst.gibGeladeneNektarmenge()==0) {
                this.ignorierteBienenId.clear();
                this.bearbBlumeAgenten.clear();
            }
            EinfacheBlume blume=(EinfacheBlume)positionFeld;
            this.ignorierteBienenId.addAll(blume.gibIDsAbbauendeBienen());
            this.bearbBlumeAgenten=(HashSet) this.ignorierteBienenId.clone();
        }
    }
    
    
    /**
     * liest die Szenario-Parameter in lokale Varialben ein.
     */
    private void scenarioParameterEinlesen(){
        try{
            String parameterString;
            parameterString=handler.getScenarioParameter("honigStarten");
            this.honigStarten=Integer.parseInt(parameterString);
            
            parameterString=handler.getScenarioParameter("honigFliegen");
            this.honigFliegen=Integer.parseInt(parameterString);
            
            parameterString=handler.getScenarioParameter("honigLanden");
            this.honigLanden=Integer.parseInt(parameterString);
            
            parameterString=handler.getScenarioParameter("honigNektarAbbauen");
            this.honigAbbauen=Integer.parseInt(parameterString);
            
            parameterString=handler.getScenarioParameter("honigNektarAbliefern");
            this.honigAbliefern=Integer.parseInt(parameterString);
            
            parameterString=handler.getScenarioParameter("honigHonigTanken");
            this.honigTanken=Integer.parseInt(parameterString);
            
            parameterString=handler.getScenarioParameter("honigHonigTanken");
            this.honigTanken=Integer.parseInt(parameterString);
            
            parameterString=handler.getScenarioParameter("maxGelNektar");
            this.maxGelNektar=Integer.parseInt(parameterString);
            
            parameterString=handler.getScenarioParameter("kursNektarHonig");
            this.kursNektarHonig=Double.parseDouble(parameterString);
            
            parameterString=handler.getScenarioParameter("maxGelHonig");
            this.startHonig=Integer.parseInt(parameterString);
            double honigVerbr;
            int honigReserve=this.honigFliegen;
            honigVerbr=startHonig - 2*(honigStarten + honigLanden) - honigTanken - honigAbbauen - honigReserve;
            this.kritEntfernung=(int) honigVerbr/this.honigFliegen;
        }catch(Exception ex){
            System.out.println("Fehler beim einlesen der Parameter");
            ex.printStackTrace();
        }
    }
    
    /**
     * Hilfsfunktion zur Ausgabe des Agenten-Status.
     * @param ich Biene bzw. Abbild der Biene mit allen Informationen über die Biene.
     */
    private void visualisiereBiene(EinfacheBiene ich) {
        Koordinate pos = (Koordinate)ich.gibPosition();
        
        System.out.print("\n"
                + id + ": Die Biene selbst\n"
                + id + ": Rundennummer:  " + ich.gibRundennummer() + "\n"
                + id + ": Zustand:  " + ich.gibZustand() + "\n"
                + id + ": Position:  " + pos.gibXPosition() + " , "
                + pos.gibYPosition() + "\n"
                + id + ": geladene Nektarmenge:  "
                + ich.gibGeladeneNektarmenge() + "\n"
                + id + ": geladene Honigmenge:  "
                + ich.gibGeladeneHonigmenge() + "\n");
        System.out.println( id + ": abgeliferte Nektarmenge: " + gesammelterNektar);
        System.out.println(id+": alter MODUS  Desire: " + modus.getDesire() + " Intention: "+ modus.getIntention());
        if (!(selbst.gibInformation() == null)) {
            System.out.println(id + ": Info");
            Info information = selbst.gibInformation();
            if (information.besitztEntfernung()) {
                System.out.println(id + ": Entfernung : " + information.gibEntfernung());
            }
            if (information.besitztRichtung()) {
                System.out.println(id + ": Richtung : " + information.gibRichtung());
            }
        }
    }
    
    
    /**
     * Ermittelt den kuerzesten Weg (als Liste) von dem Start-Knoten zum Ziel-Knoten.
     *
     * @param startKnoten Start-Knoten
     * @param zielKnoten Ziel-Knoten
     * @param bekannteFelder Mapping aus (Koordinate,EinfachesFeld), das bekannte Felder enthält.
     * @return Weg von startKnoten nach zeilKnoten, wobei der startKnoten nicht in der
     * Liste enthalten ist. Falls der weg nicht existiert, so wird null zurückgeliefert.
     */
    private LinkedList kuerzesterWeg(Koordinate startKnoten, Koordinate zielKnoten, HashMap bekannteFelder){
        LinkedList weg=new LinkedList(); // Rueckgabe: Weg von startKnoten zum zeilKnoten
        
        HashMap nachfolgeKnotenMap;
        nachfolgeKnotenMap=transformFelderZuNachfolgeKnoten(bekannteFelder);
        
        HashSet knotenMenge=new HashSet(bekannteFelder.keySet());
        weg=Dijkstra.shortestPath(startKnoten, zielKnoten, knotenMenge, nachfolgeKnotenMap);
        
        return weg;
    }
    
    
    /**
     * Transformiert eine Menge von Feldern in eine KantenMenge (Knoten, seine Nachfolger).
     * @param bekannteFelder eine Menge von Feldern.
     * @return eine KantenMenge (Knoten, seine Nachfolger) als HashMap.
     *
     */
    private HashMap transformFelderZuNachfolgeKnoten(HashMap bekannteFelder){
        HashMap nachfolgeKnotenMap=new HashMap();
        Iterator bFelderIt=bekannteFelder.keySet().iterator();
        while (bFelderIt.hasNext()){
            Koordinate keyKoord=(Koordinate)bFelderIt.next();
            EinfachesFeld einfachesFeld=(EinfachesFeld)bekannteFelder.get(keyKoord);
            HashSet nachfolgeKnoten=new HashSet(); // nachfolgeKnoten von keyKoord
            if (einfachesFeld==null) {
                nachfolgeKnotenMap.put(keyKoord,nachfolgeKnoten);
                continue;
            }
            HashMap hashNachbarn=einfachesFeld.gibNachbarfelder();
            if (hashNachbarn==null)  {
                nachfolgeKnotenMap.put(keyKoord,nachfolgeKnoten);
                continue;
            }else {
                nachfolgeKnoten.addAll(hashNachbarn.keySet());
                nachfolgeKnotenMap.put(keyKoord,nachfolgeKnoten);
            }
        }
        return nachfolgeKnotenMap;
    }
    
    
    
    
    
   /**
     * aktulisiert die gespeicherten Felder mit den lokalen aktuellen Daten
     *
     * @param lokaleFelder gegenwärtige Sicht des Agenten
     */
    private void gespeicherteFelderAktualisieren(Hashtable lokaleFelder){
        
        HashMap randFelder=new HashMap(); // Felder deren Nachbarn null sind
        if (lokaleFelder!=null) this.gespeicherteFelder.putAll(lokaleFelder);
        Iterator it=this.gespeicherteFelder.values().iterator();
        while (it.hasNext()){
            EinfachesFeld einfachesFeld=(EinfachesFeld)it.next();
            if (einfachesFeld==null)  continue; // fuer randFelder
            HashMap nachbarn=einfachesFeld.gibNachbarfelder();
            if (nachbarn==null)  continue;
            Iterator nachbarnKeyIt=nachbarn.keySet().iterator();
            while(nachbarnKeyIt.hasNext()){
                Koordinate keyKoord=(Koordinate)nachbarnKeyIt.next();
                if (!this.gespeicherteFelder.containsKey(keyKoord)){
                    randFelder.put(keyKoord.copy(), null);
                }
            }
        }
        this.gespeicherteFelder.putAll(randFelder);
    }
    
    
   /**
     * gibt die vorgegebene Koordinaten-Liste sortiert nach der Entfernung zur startKoordinate zurück.
     * @param koordList zu sortierenden Koordinaten-Liste
     * @param startKoordinate Start-Koordinate
     * @return eine KoordinatenListe, sortiert nach der Entfernung.
     */
    private LinkedList sortKoordinaten(LinkedList koordList, Koordinate startKoordinate){
        TreeSet sortSet=new TreeSet();
        HashMap bekannteFelder=this.gespeicherteFelder;
        HashSet knotenMenge=new HashSet(bekannteFelder.keySet());
        HashMap nachfolgeKnotenMap=transformFelderZuNachfolgeKnoten(bekannteFelder);
        HashMap vorgangerKnoten=Dijkstra.allShortestPath(startKoordinate, knotenMenge,nachfolgeKnotenMap);
        Koordinate zielKoordinate;
        LinkedList weg;
        if (koordList.contains(startKoordinate)) koordList.remove(startKoordinate);
        Iterator koordListIt=koordList.iterator();
        double entfernung;
        while (koordListIt.hasNext()){
            zielKoordinate=(Koordinate)koordListIt.next();
            weg=Dijkstra.shortestPath(startKoordinate, zielKoordinate, knotenMenge, nachfolgeKnotenMap);
            if (weg!=null) entfernung=weg.size();
            else entfernung=Double.POSITIVE_INFINITY;
            KoordWertPaar koordEntf=new KoordWertPaar(zielKoordinate, entfernung);
            sortSet.add(koordEntf);
        }
        LinkedList retValue=new LinkedList();
        Iterator it=sortSet.iterator();
        while (it.hasNext()){
            KoordWertPaar koordEntf=(KoordWertPaar)it.next();
            retValue.add(koordEntf.getKoordinate());
        }
        return retValue;
    }
    
    
    /**
     * liefert die Koordinate des Randfeldes, das dem unbekannten Feld
     * am nächsten liegt.
     * @param unbekannteKoord unbekannte Koordinate
     * @return Koordinate des Randfeldes
     *
     */
    private Koordinate findeRandKoord(Koordinate unbekannteKoord){
        LinkedList randKoord=sucheRandFeld();
        int uX=unbekannteKoord.gibXPosition();
        int uY=unbekannteKoord.gibYPosition();
        Koordinate tmpKoord=null;
        double diffMin=Integer.MAX_VALUE;
        int akt;
        Iterator it=randKoord.iterator();
        
        int xdiff,ydiff;
        double sumDiff;
        while(it.hasNext()){
            Koordinate koord=(Koordinate)it.next();
            xdiff=Math.abs(koord.gibXPosition()-uX);
            ydiff=Math.abs(koord.gibYPosition()-uY);
            sumDiff=Math.sqrt((xdiff *xdiff)+(ydiff * ydiff));
            if (diffMin>sumDiff){
                tmpKoord=koord;
                diffMin=sumDiff;
            }
            
        }
        if (tmpKoord!=null) return tmpKoord.copy();
        else return null;
    }
    
    
    /**
     * liefert die Nektarabbaumenge die pro Tour und Agenten zu Blume abgebaut werden kann.
     * @param blume zu betrachtende Blume
     * @return AbbauMenge an Nektar pro Tour
     */
    private int getAbbauMenge(InfoBlume blume){
        if (!blume.getProbeEntnommen()) return 0;
        
        int ausbeute=blume.getAusbeuteProRunde();
        int entfernung=blume.getEntfernungZumBienenstock();
        
        int kosten0=2*(honigStarten + honigLanden + entfernung*honigFliegen)+ honigTanken + honigAbliefern;
        int maxAnzAbbau=(int) Math.ceil(maxGelNektar/ausbeute);
        int anzMoeglAbbau=(int) Math.floor((this.startHonig-kosten0)/honigAbbauen);
        int tatsAbbau=Math.min(anzMoeglAbbau, maxAnzAbbau);
        int abbauMenge=tatsAbbau * ausbeute;
        return abbauMenge;
        
    }
    
    /**
     * gibt die voraussichtliche Anzahl der Tours zu der Blume,
     * unter Berücksichtigung der Agenten, die diese Blume bearbeiten und der Nektarmenge, die diese Blumen hat.
     *
     * @param blume zu betrachtende Blume
     * @return voraussichtliche Anzahl der Tours zur Blume
     */
    private double getVoraussichtlAnzTours(InfoBlume blume){
        int abbauMenge=getAbbauMenge(blume);
        int actualNektar=blume.getNektarInhalt();
        int anzTours=0;
        
        anzTours=actualNektar/(abbauMenge *  getAnzAgenten(blume));
        return anzTours;
        
    }
    
    /**
     * gibt die Anzahl der Agenten, die die aktuelle Blume bearbeiten
     * @param blume aktuelle Blume
     * @return Anzahl der Agenten, die die aktuelle Blume bearbeiten
     */
    private int getAnzAgenten(InfoBlume blume){
        int nektarDiff=blume.getNektarDifferenz();
        int abbauMenge=getAbbauMenge(blume);
        int actualNektar=blume.getNektarInhalt();
        int anzAgenten0=1; // Anz der Agenten die aus den die Blume bearbeitenden Agenten abgeleitet sind
        int anzAgenten1=1; // Anz der Agenten, die aus der Differenz der Nektarmenge und der Abbaumenge abgeleitet sind.
        int tatsAnzAgenten=1; // durch max zw. anzAgenten0 und anzAgenten1
        
        if (bearbBlumeAgenten.contains(new Integer(id))) anzAgenten0=this.bearbBlumeAgenten.size();
        else anzAgenten0=bearbBlumeAgenten.size() + 1;
        anzAgenten1= (int) Math.ceil(nektarDiff/abbauMenge);
        tatsAnzAgenten=Math.max(anzAgenten0, anzAgenten1);
        return tatsAnzAgenten;
    }
    
    
    /**
     * berechnet den euklidischen Abstand zwischen zwei Koordinaten
     * @param koord1 erste Koordinate
     * @param koord2 zweite Koordinate
     * @return Abstand zwischen zwei Koordinaten.
     */
    private double abstand(Koordinate koord1, Koordinate koord2){
        double abstand;
        double xDiff=koord1.gibXPosition()- koord2.gibXPosition();
        double yDiff=koord1.gibYPosition() - koord2.gibYPosition();
        abstand=Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
        return abstand;
    }
    
    /**
     * liefert neue Randfelder, nachdem aus den altenRandfeldern alle Randfelder die innerhalb der
     * bekannten Blumen oder mitgeteilten Koordinaten entfernt wurden.
     * @return eine Liste aus neuen Randfeldern
     */
    private LinkedList gibNeueRandfelder(){
        
        LinkedList alteRandfelder=this.sucheRandFeld();
        LinkedList neueRandfelder=(LinkedList)alteRandfelder.clone();
        LinkedList komKoord=this.kommInfos.getReducedKoordListe();
        int minAbstand=this.lokaleKarte.gibBlumenMindestAbstand();
        
        /* Entferne Randkoordinaten, die innerhalb des mindestAbstand von bekannte Blumenkoordinaten liegen */
        Iterator alteRandFelderIt= alteRandfelder.iterator();
        while (alteRandFelderIt.hasNext()){
            Koordinate altKoord=(Koordinate)alteRandFelderIt.next();
            
            Iterator blumenIt=bekannteBlumen.values().iterator();
            while (blumenIt.hasNext()){
                InfoBlume aBlume=(InfoBlume)blumenIt.next();
                Koordinate blumenKoord=aBlume.getBlumenKoordinate();
                if (abstand(blumenKoord,altKoord)<minAbstand) {
                    neueRandfelder.remove(altKoord);
                    break;
                }
            }
        }
        /* Entferne Randkoordinaten, die innerhalb des mindestAbstand von mitegeteilten Koordinaten liegen */
        Iterator kommIt=komKoord.iterator();
        while (kommIt.hasNext()){
            Koordinate kommKoord=(Koordinate)kommIt.next();
            Iterator altRandIt=((LinkedList)neueRandfelder.clone()).iterator();
            while (altRandIt.hasNext()){
                Koordinate altKoord=(Koordinate)altRandIt.next();
                if (abstand(altKoord,altKoord)<minAbstand) {
                    neueRandfelder.remove(altKoord);
                    break;
                }
            }
        }
        /* sortiere restliche Randfelder nach der Entfernung zum Bienenstock */
        LinkedList sortBS=sortKoordinaten(neueRandfelder,this.posBienenstock);
        /* dann nach der Entfernung zur Position der Biene */
        LinkedList retValue=this.sortKoordinaten(sortBS,this.selbst.gibPosition());
        if (retValue.size()==0){
                        
            /* FALLS alle Randfelder sind überdeckt von bekannten Blumenwiesen, dann suche ein Randfeld aus,
                das am weitesten von den Blumenwiesen entfern ist.*/
            TreeSet sortKoord=new TreeSet();
            
            alteRandfelder=this.sucheRandFeld();
            alteRandFelderIt= alteRandfelder.iterator();
            while (alteRandFelderIt.hasNext()){
                Koordinate altKoord=(Koordinate)alteRandFelderIt.next();
                
                Iterator blumenIt=bekannteBlumen.values().iterator();
                double minimum=Integer.MAX_VALUE;
                double abstand;
                
                while (blumenIt.hasNext()){
                    InfoBlume aBlume=(InfoBlume)blumenIt.next();
                    Koordinate blumenKoord=aBlume.getBlumenKoordinate();
                    
                    abstand=abstand(blumenKoord,altKoord);
                    if (abstand<minimum) minimum=abstand;
                }
                
                KoordWertPaar kwp=new KoordWertPaar(altKoord,minimum);
                sortKoord.add(kwp);
            }
            Iterator itSort=sortKoord.iterator();
            LinkedList newRetValue=new LinkedList();
            while (itSort.hasNext()){
                Koordinate myKoord=((KoordWertPaar)itSort.next()).getKoordinate();
                newRetValue.addFirst(myKoord);
                
            }
            
            // falls es keine Randfelder gibt.
            if (newRetValue.size()==0) newRetValue.add(posBienenstock);
            
            retValue=newRetValue;
        }
        return retValue;
        
    }
    
    
    
    
    /**
     * gibt die Randkoordinate, die der nextKoord am nächsten ist
     * @param nextKoord Koordinate
     * @return die Randkoordinate, die der nextKoord am nächsten ist
     */
    private Koordinate gibNextRandKoord(Koordinate nextKoord){
        LinkedList randListe=sucheRandFeld();
        
        LinkedList liste=sortKoordinaten(randListe, nextKoord);
        if (liste.size()>0) return ((Koordinate)liste.getFirst());
        else return nextKoord;
        
        
    }
    
    
    /* #######################   Beginn: primitive Aktionen    ######################## */
    
    
    /** lässt die Biene tanken */
    private void tanken(){
        if (out) System.out.println(selbst.gibBienenID()+ ": tanke " + selbst.gibPosition().toString());
        int mengeZuTanken=this.startHonig-selbst.gibGeladeneHonigmenge()+this.honigTanken;
        long neuerAktCode = handler.aktionHonigTanken(aktCode, erfolg, mengeZuTanken);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        if (erfolg.getValue()){
            perception();
            if (selbst.gibGeladeneHonigmenge()<this.startHonig) bienenstockHatHonig=false;
        }
    }
    
    /** lässt die Biene starten */
    private void starten(){
        if (out) System.out.println(selbst.gibBienenID()+ ": starte " + selbst.gibPosition().toString());
        long neuerAktCode = handler.aktionStarten(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        if (erfolg.getValue()) bieneIstInDerLuft=true;
        else starten();
    }
    
    /** lässt die Biene landen */
    private void landen(){
        if (out) System.out.println(selbst.gibBienenID()+ ": lande " + selbst.gibPosition().toString());
        long neuerAktCode = handler.aktionLanden(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        if (erfolg.getValue()) bieneIstInDerLuft=false;
        else {
            LinkedList liste=modus.getPlanListe();
            int actionNr=DesireIntentionPlan.A_LANDEN;
            ActionTargetPair atp=new ActionTargetPair(actionNr,positionKoordinate.copy());
            liste.addFirst(atp);
        }
    }
    
    
    /**
     * fliegt zur nächsten Position
     * @param nextZiel nächste ZielPosition
     */
    private void fliegen(Koordinate nextZiel){
        if (out) System.out.println(selbst.gibBienenID()+ ": fliege zur " + nextZiel.toString());
        long neuerAktCode = handler.aktionFliegen(aktCode, erfolg, nextZiel);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        if (!erfolg.getValue()){
            LinkedList liste=modus.getPlanListe();
            int actionNr=DesireIntentionPlan.A_FLIEGEN;
            ActionTargetPair atp=new ActionTargetPair(actionNr,nextZiel);
            liste.addFirst(atp);
        }
    }
    
    /** lässt die Biene Nektar abbauen */
    private void nektarAbbauen(){
        int max=1000;
        if (out) System.out.println(selbst.gibBienenID()+ ": baue Nektar ab " + selbst.gibPosition().toString());
        long neuerAktCode = handler.aktionNektarAbbauen(aktCode, erfolg,max);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        if (!erfolg.getValue()){
            LinkedList liste=modus.getPlanListe();
            int actionNr=DesireIntentionPlan.A_NEKTARABBAUEN;
            ActionTargetPair atp=new ActionTargetPair(actionNr,positionKoordinate);
            liste.addFirst(atp);
        }
    }
    
    /** lässt die Biene Nektar abliefern */
    private void nektarAbliefern(){
        if (out) System.out.println(selbst.gibBienenID()+ ": liefere Nektar ab " + selbst.gibPosition().toString());
        this.gesammelterNektar=this.gesammelterNektar + selbst.gibGeladeneNektarmenge();
        long neuerAktCode = handler.aktionNektarAbliefern(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
    }
    
    /**
     * lässt die Biene tanzen und teilt dadurch die Koordinate der Blume mit.
     *
     * @param blumenKoordinate mitzuteilende Koordinate der Blume
     */
    private void tanzen(Koordinate blumenKoordinate){
        double nutzen=-1;
        int koordX=blumenKoordinate.gibXPosition();
        int koordY=blumenKoordinate.gibYPosition();
        InfoBlume infoBlume=(InfoBlume)this.bekannteBlumen.get(blumenKoordinate);
        if (infoBlume.getProbeEntnommen()) nutzen=infoBlume.getNutzen();
        long neuerAktCode = handler.aktionTanzen(aktCode, erfolg, koordX, koordY, true, true,nutzen);
        if (out) System.out.println(selbst.gibBienenID()+ ": TANZE: habe übermittelt Koordinate : "+ blumenKoordinate.toString() +
                " Nutzen " + nutzen);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        if (erfolg.getValue()) this.getanzt=true;
    }
    
    /**
     * Lässt die Biene einer anderen Biene beim Tanzen zuschauen.
     * Die Information zur mitgeteilten Blume (entfernung und richtung) steht
     * nach der aktualisierung der perception in selbst.gitInfo().
     *
     * @param tanzendeBieneID die Id der tanzenden Biene.
     * @return ungefäre Koordinate der Blume.
     */
    private Koordinate zuschauen(int tanzendeBieneID){
        Info infoAnf=selbst.gibInformation();
        Koordinate blumenKoord=null;
        long neuerAktCode = handler.aktionZuschauen(aktCode, erfolg, tanzendeBieneID);
        if (!( neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        if (!erfolg.getValue()) return null;
        perception();
        Info info=selbst.gibInformation();
        if (infoAnf==info) return null;
        if ((info!=null) && info.besitztEntfernung() && info.besitztRichtung()) {
            double entfernung=info.gibEntfernung();
            double richtung=info.gibRichtung();
            double dX,dY;
            dX=Math.cos(richtung)*entfernung;
            dY=Math.sin(richtung)*entfernung;
            int xKoord,yKoord;
            xKoord=(int) Math.round(dX) + selbst.gibPosition().gibXPosition();
            yKoord=(int) Math.round(dY) + selbst.gibPosition().gibYPosition();
            blumenKoord=new Koordinate(xKoord, yKoord);
        }
        if (out && blumenKoord!=null) System.out.println(id+": MITGETEILTE BLUMENKOORDINATE: " + blumenKoord.toString());
        return blumenKoord;
    }
    
    
    /**
     * versetzt die Biene in den Warte-Zustand.
     */
    public void warten() {
        if (out) System.out.println(id + ": warten " + selbst.gibPosition().toString() );
        long neuerAktCode = handler.aktionWarten(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
    }
    
    
    /* ##################### Ende der Primitiven Aktionen ####################### */
    
    
}