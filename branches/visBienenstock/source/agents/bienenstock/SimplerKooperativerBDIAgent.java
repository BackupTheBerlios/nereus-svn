/*
 * Dateiname      : SimplerKooperativerBDIAgent.java
 * Erzeugt        : 21. Mai 2005
 * Letzte Änderung: 16. Juni 2005 durch Eugen Volk
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

/**
 * Die Klasse ist Agent und ist aufgebaut nach der Belief-Desire-Intention Architektur.
 */
public class SimplerKooperativerBDIAgent
        extends AbstrakteBiene
        implements Runnable {
    
    private static final int MAX=1000;
    private int volksID;
    private long aktCode;
    private IBienenstockSzenarioHandler handler;
    /** spiegelt den externen Zustand der Biene */
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
    boolean erstenAktCodeBekommen = false;
    /** Id der Biene */
    private int id = 0;
    private Koordinate posBienenstock;
    
    
    /** zähler für die begrenzte Kooperationsbereitschaft der biene  (Z.B. Kooperatin für 5 Runden erhalten) */
    private int rundeNrKooperation=0;
    /** Konstante für die Erhaltung der Kooperation (warten auf Kommunikationspartner) über mehrere Runden */
    private int maxAnzahlKooperationsRunden=3;
    
    private boolean neueMitteilungErhalten=false;
    
    //   HashMap blumen = new HashMap();
    /** (Koordinate, InfoBlume) */
    HashMap bekannteBlumen=new HashMap();
    
    
    /** eine Navigationskarte als Gedächtnis über die wahrgenommenen Felder */
    HashMap gespeicherteFelder=new HashMap();
    
    /** wartende Bienen auf dem aktuellen Feld (ohne die Biene selbst) */
    HashSet wartendeBienen;
    /**  tanzende Bienen auf dem aktuellen Feld (ohne die Biene selbst) */
    HashSet tanzendeBienen;
    //ein paar honigkosten
    /** HonigMenge vor dem Start der Biene */
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
    private String name = "";
    /** gesammelter Nektar */
    private int gesammelterNektar = 0;
    /** kurs Nektar zu Honig */
    private double kursNektarHonig=1;
    /** getanzte Bienen, seit kooperatinReset */
    private HashSet getanzteBienen=new HashSet();
    /** habe ich schon seit kooperationReset getanzt? */
    boolean getanzt=false;
    /** wird gesetzt, falls die Biene wenig Honig hat und sofort zum Bienenstock zurückkehren muss */
    private boolean sofortNektarAbliefernTanken = false;
    /** mitgeteilte BlumenKoordinaten: (Koordinate, new Double(Nutzen))*/
    private HashMap mitgeteilteBlumenKoord=new HashMap();
    
    private boolean  bienenstockHatHonig=true;
    
    private HashSet ignorierteBienenId=new HashSet();
    
    
    /**
     * Zahl zur initialisierung des Zufallsgenerators.
     */
    private long samen = System.currentTimeMillis();
    
    /**
     * Wird zur Wahl der initialen Flugrichtung benötigt.
     */
    Random zufallsGenerator = new Random();
    
    
    public SimplerKooperativerBDIAgent(String bName, AbstractScenarioHandler bHandler) {
        super(bName, bHandler);
        name = bName;
        handler = (IBienenstockSzenarioHandler)bHandler;
        volksID = 1;
        //zufallsGenerator.setSeed(samen);
        
    }
    
    public SimplerKooperativerBDIAgent() {
        super();
        volksID = 1;
        //  zufallsGenerator.setSeed(samen);
    }
    
    public SimplerKooperativerBDIAgent(Id bId,String bName) {
        super(bId, bName);
        name = bName;
        volksID = 1;
    }
    
    public static boolean isRunableAgent() {
        return true;
    }
    
    public void setHandler(AbstractScenarioHandler bHandler) {
        handler = (IBienenstockSzenarioHandler)bHandler;
    }
    
    public boolean aktionscodeSetzen(long aktionsCode) {
        aktCode = aktionsCode;
        this.scenarioParameterEinlesen();
        this.start();
        erstenAktCodeBekommen = true;
        return true;
    }
    
    public int gibVolksID() {
        return volksID;
    }
    
    
    
    /* ###########################----Begin AgentenVerhalten festlegen---############################-----*/
    
    
    DesireIntentionPlan modus=new DesireIntentionPlan();
    public void run() {
        
        
        
        
        // DesireIntentionPlan modus=new DesireIntentionPlan();
        perception();
        posBienenstock = selbst.gibPosition().copy();
        modus.setDesire(modus.G_FINDEEINEBLUME);
        
        while(true){
            perception();
            System.out.println(id+": alter MODUS  Desire: " + modus.getDesire() + " Intention: "+ modus.getIntention());
            //   visualisiereBiene(selbst);
            evaluateGoal(modus);
            modifyGoalToAbstractPlan(modus);
            System.out.println(id+": neuer MODUS  Desire: " + modus.getDesire() + " Intention: "+ modus.getIntention());
            if (modus.getDesireZiel()!=null) System.out.println(id+": Desire Ziel " +  modus.getDesireZiel().toString());
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
                this.ignorierteBienenId.clear();
                LinkedList blumen=sucheBlumeInSichtbarenFeldern();
                LinkedList bekannteBlumenMitNektar=this.gibBekannteBlumenMitNektar();
                if ((blumen!=null) && (blumen.size()>0)) {
                    modus.setDesire(DesireIntentionPlan.G_BLUMENPROBEENTNEHMEN);
                    int anzBlumen=blumen.size()-1;
                    double zufall=Math.random();
                    if (zufall<0) zufall=zufall*(-1);
                    int myZahl=(int)Math.round(zufall*anzBlumen);
                    System.out.println(id+": MYZAHL = " + myZahl + " von max:" + blumen.size() + " zufall: " + zufall);
                    neuesZiel=(Koordinate)blumen.get(myZahl);
                    modus.setDesireZiel(neuesZiel.copy());
                    
                } else if ((bekannteBlumenMitNektar!=null) && (bekannteBlumenMitNektar.size()>0)){
                    modus.setDesire(DesireIntentionPlan.G_BLUMENPROBEENTNEHMEN);
                    int anzBlumen=bekannteBlumenMitNektar.size()-1;
                    if (anzBlumen>=3) anzBlumen=3;
                    double zufall=Math.random();
                    int myZahl=(int)Math.round(zufall*anzBlumen);
                    neuesZiel=(Koordinate)bekannteBlumenMitNektar.get(myZahl);
                    modus.setDesireZiel(neuesZiel.copy());
                }else{
                    if ((altesZiel==null) || (altesZiel.equals(myPosition)) || (myPosition.equals(this.posBienenstock))){
                        double zufall=Math.random();
                        int index;
                        if (zufall<0) zufall=zufall*(-1);
                        randListe=sucheRandFeld();
                        int anzBienen=this.positionFeld.gibIDsFliegendeBienen().size()+
                                this.positionFeld.gibIDsWartendeBienen().size()+
                                this.positionFeld.gibIDsTanzendeBienen().size()+
                                this.positionFeld.gibIDsTanzendeBienen().size();
                        int anzRander=randListe.size();
                        int minVal;
                        minVal=anzRander;
                        minVal= Math.min(anzBienen,anzRander);
                        
                        if (minVal>0) minVal=minVal-1;
                        int myWahl=(int) Math.round((minVal) * zufall);
                        //          System.out.println(id+ ": FINDEBLUME Zufall "+ zufall + " mw "+ myWahl + " minVal :" + minVal);
                        neuesZiel=(Koordinate)randListe.get(myWahl);
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
    public void modifyGoalToAbstractPlan(DesireIntentionPlan modus){
        // verlasse filter ohne neue Intention auszuwählen
        if (this.sofortNektarAbliefernTanken){
            System.out.println(id +": sofortNektarAbliefern ");
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
                if (selbst.gibGeladeneNektarmenge()>0) {
                    modus.setIntention(DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN);
                } else{
                    newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                    i_neuesZiel=d_Ziel;
                    if (!nextIntentionOK(newIntention, i_neuesZiel)){
                        if (selbst.gibGeladeneHonigmenge()<(this.startHonig-this.honigStarten)) {
                            modus.setIntention(DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN);
                        } else {
                            System.out.println(" KANN NICHT FLIEGEN");
                        }
                    }else{
                        modus.setIntention(newIntention);
                        modus.setIntentionZiel(i_neuesZiel);
                    }
                }
            }break;
            case DesireIntentionPlan.G_BLUMENPROBEENTNEHMEN:{
                i_neuesZiel=d_Ziel;
                if (d_Ziel.equals(myPosition)) newIntention=DesireIntentionPlan.P_NEKTARABBAUEN;
                else {
                    if (selbst.gibGeladeneNektarmenge()>0) {
                        newIntention=DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN;
                        i_neuesZiel=this.posBienenstock;
                    } else{
                        newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                        i_neuesZiel=d_Ziel;
                    }
                }
                if (!nextIntentionOK(newIntention, i_neuesZiel)) {
                    newIntention=DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN;
                    i_neuesZiel=this.posBienenstock;
                }
                modus.setIntention(newIntention);
                modus.setIntentionZiel(i_neuesZiel);
            }break;
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
                    if ((selbst.gibGeladeneNektarmenge()>0) ||
                            ((selbst.gibGeladeneHonigmenge()<(this.startHonig-this.honigStarten))
                            && ((this.rundeNrKooperation>this.maxAnzahlKooperationsRunden)
                            || (this.rundeNrKooperation==0)))){
                        newIntention=DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN;
                        modus.setIntention(newIntention);
                    } else if (kooperationsBereitschaft() || (this.rundeNrKooperation<=this.maxAnzahlKooperationsRunden)){
                        newIntention=DesireIntentionPlan.P_COOPERATION;
                        modus.setIntention(newIntention);
                        i_neuesZiel=d_Ziel;
                        modus.setIntentionZiel(i_neuesZiel);
                        // this.rundeNrKooperation++;
                        //     System.out.println(" RUNDEN NR: " + this.rundeNrKooperation);
                    }else if ((this.rundeNrKooperation>this.maxAnzahlKooperationsRunden) && this.neueMitteilungErhalten){
                        Koordinate neueZielKoord=this.findeBesteMitgeteteilteBlume(d_Ziel);
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
                    } else{ // fliege zur Blume
                        newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                        modus.setIntention(newIntention);
                        i_neuesZiel=d_Ziel;
                        modus.setIntentionZiel(i_neuesZiel);
                    }
                }
            }break;
            case DesireIntentionPlan.G_FINDEDIEBLUME:{
                modus.setIntention(DesireIntentionPlan.P_FLIEGENZURKOORDINATE);
                i_neuesZiel=i_altesZiel;
                // falls zur Koordinate keein Weg existiert, so werden Randknoten zur Nav verwendet.
                if (i_altesZiel==null) {
                    Set gespKoord=this.gespeicherteFelder.keySet();
                    if (gespKoord.contains(d_Ziel)){
                        i_neuesZiel=d_Ziel;
                        i_altesZiel=i_neuesZiel;  
                    }else{
                      i_neuesZiel=this.findeRandKoord(d_Ziel);
                      i_altesZiel=i_neuesZiel;                        
                    }     
                }
                
                if ((myPosition.equals(i_altesZiel)) && (!d_Ziel.equals(i_altesZiel))){
                    i_neuesZiel=this.findeRandKoord(d_Ziel);
                    i_altesZiel=i_neuesZiel;
                }
                modus.setIntentionZiel(i_neuesZiel);
               
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
                        System.out.println(id+" KEIN WEG zum fliegen nach " + i_ziel.toString() );
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
                //  if (planListe==null) System.out.println("PLANLISTE =NULL");
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
                EinfacheKarte localMap = handler.infoAusschnittHolen(aktCode);
                selbst=localMap.gibSelbst();
                int nektarNachAbbau=selbst.gibGeladeneNektarmenge();
                int ausbeute=nektarNachAbbau-nektarAnf;
                if ((nektarAnf!=this.maxGelNektar)){
                    InfoBlume infoBlume=(InfoBlume)bekannteBlumen.get(ziel);
                    if (!infoBlume.getProbeEntnommen()) {
                        infoBlume.setProbeEntnommen();
                        infoBlume.setAusbeuteProRunde(ausbeute);
                        berechneSetzeNutzen(infoBlume);
                    }
                    if ((ausbeute==0)) {
                        infoBlume.setHatNektar(false);
                    }
                } else {System.out.println(" NEKTAR VOLL ");}
            }break;
            case DesireIntentionPlan.A_NEKTARABLIEFERN:{
                this.nektarAbliefern();
            }break;
            case DesireIntentionPlan.A_TANZEN:{
                this.tanzen(ziel);
            }break;
            case DesireIntentionPlan.A_ZUSCHAUEN:{
                Koordinate caBlumenKoordinate;
                int zielId=act.getZielAgentId();
                caBlumenKoordinate=this.zuschauen(zielId);
                Info info=selbst.gibInformation();
                if (info!=null){
                    double nutzen=selbst.gibInformation().gibNutzen();
                    mitgeteilteBlumenKoord.put(caBlumenKoordinate,new Double(nutzen));
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
            LinkedList getztBienen=new LinkedList();
            
            tanzendeBieneId=((Integer)this.tanzendeBienen.iterator().next()).intValue();
            planListe=new LinkedList();
            actionNr=DesireIntentionPlan.A_ZUSCHAUEN;
            getanzteBienen.addAll(this.tanzendeBienen);
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
            int aBieneId=10000;
            while (it.hasNext()){
                aBieneId=((Integer)it.next()).intValue();
                if (aBieneId<myId) break;
            }
            if ((myId<aBieneId)){
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
                return planListe;
            }
        }else{
            planListe=new LinkedList();
            actionNr=DesireIntentionPlan.A_WARTEN;
            atp=new ActionTargetPair(actionNr,blumenKoord.copy());
            planListe.add(atp);
            return planListe;
            
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
     * Falls es keien Bessere gibt, so wird die Koordinate der aktuellen Blume übermittelt.
     * @param aktuelleBlume Koordinate der aktuellen zu bearbeitenden Blume
     * @return Koordinate der ausgewählten Blume
     */
    private Koordinate findeBesteMitgeteteilteBlume(Koordinate aktuelleBlume){
        InfoBlume infoBlume=(InfoBlume)this.bekannteBlumen.get(aktuelleBlume);
        double myNutzen=infoBlume.getNutzen();
        Koordinate returnKoord;
        double letzteSichereNutzen=0;
        double maxNutzen=0;
        LinkedList kandidaten=new LinkedList();
        TreeSet sortKoord=new TreeSet();
        Iterator setIt=this.mitgeteilteBlumenKoord.entrySet().iterator();
        while(setIt.hasNext()){
            Entry entry=(Entry)setIt.next();
            Koordinate koord=((Koordinate)entry.getKey()).copy();
            double nutzen=((Double)entry.getValue()).doubleValue();
            KoordWertPaar koordWertPaar=new KoordWertPaar(koord,nutzen);
            if (nutzen>myNutzen) sortKoord.add(koordWertPaar);
        }
        if (sortKoord.size()>0){
            double zufall=Math.random();
            if (zufall<0) zufall=zufall*(-1);
            KoordWertPaar koordWertPaar=new KoordWertPaar(aktuelleBlume,myNutzen);
            sortKoord.add(koordWertPaar);
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
     * gibt die Kosten an, um von der aktuellen Position zurück zum Bienenstock zu fliegen
     * und dort zu tanken.
     *
     * @return Kosten um zum Bienenstock zurück zu kehren und zu tanken.
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
            kosten=this.honigLanden+this.honigTanken + kostenFliegen;
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
                liste.add(infoBlume.getBlumenKoordinate());
                if (infoBlume.getProbeEntnommen()) {
                    if (infoBlume.getNutzen()>0) {
                        koordWertPaar=new KoordWertPaar(infoBlume.getBlumenKoordinate(),infoBlume.getNutzen());
                        sortNutzen.add(koordWertPaar);
                    }
                }
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
    
    
    /** sucht nach einem Feld das nicht erforscht wurde und am nächsten zur Biene
     * und am nächsten zum Bienenstock sich befindet.
     * @return KoordinateListe der Rand-Felder, sortiert nach der Entfernung zur gegenwärtigen Position
     * und zur Entfernung des Bienenstocks.
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
              sowohl der Biene als auch zum Bienenstock  */
            
            LinkedList sortRandFelderBienenstock;
            LinkedList sortRandFelder;
            
            sortRandFelderBienenstock=this.sortKoordinaten(randFelder,this.posBienenstock);
            sortRandFelder=this.sortKoordinaten(sortRandFelderBienenstock,this.selbst.gibPosition());
            
            return sortRandFelder;
        }
        
        
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
        if (bekannteBlumen.keySet().contains(posBlume)){
            infoBlume=(InfoBlume)bekannteBlumen.get(posBlume);
            if (infoBlume.getHatNektar()){
                if (infoBlume.getProbeEntnommen()) return (infoBlume.getNutzen()>0);
                else return true;
            } else return false;
        }else {
            int entfernung=kuerzesterWeg(this.posBienenstock,posBlume, this.gespeicherteFelder).size();
            infoBlume=new InfoBlume(posBlume, entfernung);
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
     * @blume Informationen zu einer Blume, wobei die Probe entnommen sein muss,
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
        int kosten0=2*(honigStarten + honigLanden + entfernung*honigFliegen);
        int maxAnzAbbau=(int)(maxGelNektar/ausbeute);
        int anzMoeglAbbau=(int)((this.startHonig-kosten0)/honigAbbauen);
        int tatsAbbau=Math.min(anzMoeglAbbau, maxAnzAbbau);
        int tatsKosten=kosten0 + honigAbliefern + (tatsAbbau * honigAbbauen);
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
        System.out.println(id + ": ich bin nicht mehr im Spiel! ");
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
            if (selbst.gibGeladeneNektarmenge()==0) this.ignorierteBienenId.clear();
            EinfacheBlume blume=(EinfacheBlume)positionFeld;
            this.ignorierteBienenId.addAll(blume.gibIDsAbbauendeBienen());
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
     * @lokaleFelder gegenwärtige Sicht des Agenten
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
    
    
    
    /* #######################   Beginn: primitive Aktionen    ######################## */
    
    
    /** lässt die Biene tanken */
    private void tanken(){
        System.out.println(selbst.gibBienenID()+ ": tanke " + selbst.gibPosition().toString());
        int mengeZuTanken=this.startHonig-selbst.gibGeladeneHonigmenge()+this.honigTanken;
        long neuerAktCode = handler.aktionHonigTanken(aktCode,mengeZuTanken);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        perception();
        if (selbst.gibGeladeneHonigmenge()<this.startHonig) bienenstockHatHonig=false;
    }
    
    /** lässt die Biene starten */
    private void starten(){
        System.out.println(selbst.gibBienenID()+ ": starte " + selbst.gibPosition().toString());
        long neuerAktCode = handler.aktionStarten(aktCode);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        bieneIstInDerLuft=true;
    }
    
    /** lässt die Biene landen */
    private void landen(){
        System.out.println(selbst.gibBienenID()+ ": lande " + selbst.gibPosition().toString());
        long neuerAktCode = handler.aktionLanden(aktCode);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        bieneIstInDerLuft=false;
    }
    
    
    /**
     * fliegt zur nächsten Position
     * @param nextZiel nächste ZielPosition
     */
    private void fliegen(Koordinate nextZiel){
        System.out.println(selbst.gibBienenID()+ ": fliege zur " + nextZiel.toString());
        long neuerAktCode = handler.aktionFliegen(aktCode, nextZiel);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
    }
    
    /** lässt die Biene Nektar abbauen */
    private void nektarAbbauen(){
        System.out.println(selbst.gibBienenID()+ ": baue Nektar ab " + selbst.gibPosition().toString());
        long neuerAktCode = handler.aktionNektarAbbauen(aktCode,MAX);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
    }
    
    /** lässt die Biene Nektar abliefern */
    private void nektarAbliefern(){
        System.out.println(selbst.gibBienenID()+ ": liefere Nektar ab " + selbst.gibPosition().toString());
        this.gesammelterNektar=this.gesammelterNektar + selbst.gibGeladeneNektarmenge();
        long neuerAktCode = handler.aktionNektarAbliefern(aktCode);
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
        long neuerAktCode = handler.aktionTanzen(aktCode, koordX, koordY, true, true,nutzen);
        System.out.println(selbst.gibBienenID()+ ": TANZE: habe übermittelt Koordinate : "+ blumenKoordinate.toString() +
                " Nutzen " + nutzen);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        this.getanzt=true;
    }
    
    /**
     * Lässt die Biene einer anderen Biene beim Tanzen zuschauen.
     * Die Information zur mitgeteilten Blume (entfernung und richtung) steht
     * nach der aktualisierung der perception in selbst.gitInfo().
     *
     * @param tanlzendeBieneID die Id der tanzenden Biene.
     * @return ungefäre Koordinate der Blume.
     */
    private Koordinate zuschauen(int tanzendeBieneID){
        Info infoAnf=selbst.gibInformation();
        Koordinate blumenKoord=null;
        long neuerAktCode = handler.aktionZuschauen(aktCode, tanzendeBieneID);
        if (!( neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
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
        if (blumenKoord!=null) System.out.println(id+": MITGETEILTE BLUMENKOORDINATE: " + blumenKoord.toString());
        return blumenKoord;
    }
    
    
    /**
     * versetzt die Biene in den Warte-Zustand.
     */
    public void warten() {
        System.out.println(id + ": warten " + selbst.gibPosition().toString() );
        long neuerAktCode = handler.aktionWarten(aktCode);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
    }
    
    
    /* ##################### Ende der Primitiven Aktionen ####################### */
    
    
}