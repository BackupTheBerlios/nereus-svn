/*
 * Dateiname      : SimplerUnkoopBDIAgent.java
 * Erzeugt        : 6. Juni 2005
 * Letzte �nderung: 22. Juni 2005 durch Eugen Volk
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

package agents.bienenstock;
import nereus.simulatorinterfaces.AbstractScenarioHandler;
import scenarios.bienenstock.interfaces.AbstrakteBiene;
import scenarios.bienenstock.interfaces.IBienenstockSzenarioHandler;
import scenarios.bienenstock.einfacheUmgebung.*;
import nereus.utils.Id;
import nereus.agentutils.ActionTargetPair;
import nereus.utils.BooleanWrapper;

import java.util.Iterator;
import java.util.Random;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import nereus.agentutils.Dijkstra;
import scenarios.bienenstock.agenteninfo.Info;
import scenarios.bienenstock.agenteninfo.Koordinate;
import agents.bienenstock.utils.KoordWertPaar;
import agents.bienenstock.utils.DesireIntentionPlan;
import agents.bienenstock.utils.InfoBlume;

/**
 * Ein unkooperativer Bienen-Agent f�r Szenario Bienenstock.
 * Die Klasse ist ein Agent und ist aufgebaut nach der deliberativen Agenten-Architektur.
 */
public class UnkooperativerAgent
        extends AbstrakteBiene
        implements Runnable {
    
  /**
     * ID des Volkes, zu der die Biene geh�rt.
     */
    private int volksID;
    /**
     * aktueller Aktionskode der Biene.
     */
    private long aktCode;
    /**
     * Szenario-Handler f�r die Vermittlung  der Information
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
    /** f�r Agenten sichtbare Felder */
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
    /** falls eine beabsichtigte Aktiin tats�chlich ausgef�hrt wurde, wird erfold.value den Wert true haben. */
    private BooleanWrapper erfolg=new BooleanWrapper(false);
    
    /**
     * Eine Datenstruktur zur Speicherung der bekannten Blumen.
     * (Koordinate, InfoBlume)
     */
    HashMap bekannteBlumen=new HashMap();;
        
    /** eine Navigationskarte als Ged�chtnis �ber die wahrgenommenen Felder */
    HashMap gespeicherteFelder=new HashMap();
    
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
    /**  Wechselkurs f�r die Umwandlung von Nektar zu Honig. */
    private double kursNektarHonig=1;
    /** Reserver f�r den Fall dass Biene Warten muss */
    private int reserve=2;
    /** Id der getanzten Bienen, seit kooperatinReset.   */
    private HashSet getanzteBienen=new HashSet();
    /** hat die Biene schon seit kooperationReset getanzt?  */
    boolean getanzt=false;
    /** wird gesetzt, falls die Biene wenig Honig hat und sofort zum Bienenstock zur�ckkehren muss */
    private boolean sofortNektarAbliefernTanken = false;
    
     /** gibt es noch Honig im Bienenstock */
    private boolean  bienenstockHatHonig=true;
    
     /** Nummer des Versuchs bei der Blumenprobeentnahme */
    private int probeEntnahmeVersuchNr=0;
    
    /** Max Anzahl des Versuchs bei der Blumenprobeentnahme, danach wird bei erfolgloser Nektarabbau,
     * die Blume als ohne Nektar angesehen */
    private int probeEntnahmeVersucheMax=2;
    
    /**
     * Zahl zur initialisierung des Zufallsgenerators.
     */
    private long samen = System.currentTimeMillis();
    
    /**
     * Wird zur Wahl der initialen Flugrichtung ben�tigt.
     */
    Random zufallsGenerator = new Random();
    
    
    /**
     * Ein unkooperativer Bienen-Agent f�r
     * das Szenario Bienenstock.
     * @param bName Name des Agenten
     * @param bHandler Handler des Agenten
     */
    public UnkooperativerAgent(String bName, AbstractScenarioHandler bHandler) {
        super(bName, bHandler);
        name = bName;
        handler = (IBienenstockSzenarioHandler)bHandler;
        volksID = 1;
        zufallsGenerator.setSeed(samen);
        
    }
    
    /**
     * Ein unkooperativer Bienen-Agent f�r
     * das Szenario Bienenstock.
     */
    public UnkooperativerAgent() {
        super();
        volksID = 1;
        zufallsGenerator.setSeed(samen);
    }
    
    /**
     * Ein unkooperativer Bienen-Agent f�r
     * das Szenario Bienenstock.
     * @param bId Id des Agenten
     * @param bName Name des Agenten
     */
    public UnkooperativerAgent(Id bId,String bName) {
        super(bId, bName);
        name = bName;
        volksID = 1;
    }
    
    /**
     * signalisert, dass der Agent eine Run-Methode enth�lt.
     * @return true
     */
    public static boolean isRunableAgent() {
        return true;
    }
    
    /**
     * setzt den Handler, der f�r den Agenten zust�ndig ist.
     * @param bHandler Handler des Agenten
     */
    public void setHandler(AbstractScenarioHandler bHandler) {
        handler = (IBienenstockSzenarioHandler)bHandler;
    }
    
    /**
     * setzt den Aktionskode f�r den Agenten
     * @param aktionsCode alter Aktionskode
     * @return neuer Aktionskode
     */
    public boolean aktionscodeSetzen(long aktionsCode) {
        aktCode = aktionsCode;
        this.scenarioParameterEinlesen();
        this.start();
        erstenAktCodeBekommen = true;
        return true;
    }
    
    /**
     * Id des Volkes, zu der die Biene geh�rt.
     * @return Nummer des Bienenvolkes, zu
     * der der Agent geh�rt.
     */
    public int gibVolksID() {
        return volksID;
    }
    
    
    
    /* ###########################----Begin AgentenVerhalten festlegen---############################-----*/
    
    
    /**
     * beschreibt den internen Zustand des deliberativen Agenten.
     */
    DesireIntentionPlan modus=new DesireIntentionPlan();
    /**
     * Run-Methode, zur Ausf�hrung des Agenten-programms als Thread.
     */
    public void run() {
        // DesireIntentionPlan modus=new DesireIntentionPlan();
        perception();
        posBienenstock = selbst.gibPosition().copy();
        modus.setDesire(modus.G_FINDEEINEBLUME);
        
        while(true){
            perception();
      //      this.visualisiereBiene(selbst);
            evaluate(modus);
            filter(modus);
            System.out.println(id+": neuer MODUS  Desire: " + modus.getDesire() + " Intention: "+ modus.getIntention());
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
        gespeicherteFelderAktualisieren(sichtbareFelder);
    }
    
    
    
    
    /**
     * �berpr�ft den Wusch (Desire-Ziel) des Agenten und w�hlt gegebenfalls einen Neuen aus.
     * @param modus interner Zustand des Agenten als DesireInteionPlan-Typ.
     */
    public void evaluate(DesireIntentionPlan modus){
        Koordinate myPosition=selbst.gibPosition();
        Koordinate neuesZiel;
        Koordinate altesZiel=modus.getDesireZiel();
        int desire=modus.getDesire();
        
        if (this.sofortNektarAbliefernTanken) return;
        
        switch (desire){
            case DesireIntentionPlan.G_UMGEBUNGERFORSCHEN:{
                if (altesZiel==null || (altesZiel.equals(myPosition))){
                    neuesZiel=sucheRandFeld();
                    modus.setDesireZiel(sucheRandFeld());
                }
            }break;
            case DesireIntentionPlan.G_FINDEEINEBLUME:{
                LinkedList blumen=sucheBlumeInSichtbarenFeldern();
                if (blumen!=null) {
                    modus.setDesire(DesireIntentionPlan.G_BLUMENPROBEENTNEHMEN);
                    int anzBlumen=blumen.size()-1;
                    double zufall=Math.random();
                    if (zufall<0) zufall=zufall*(-1);
                    int myZahl=(int)Math.round(zufall*anzBlumen);
                    System.out.println(id+": MYZAHL = " + myZahl);
                    neuesZiel=(Koordinate)blumen.get(myZahl);
                    modus.setDesireZiel(neuesZiel.copy());
                }else{
                    if (altesZiel==null || (altesZiel.equals(myPosition))){
                        neuesZiel=sucheRandFeld();
                        modus.setDesireZiel(neuesZiel);
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
                            evaluate(modus);
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
                    evaluate(modus);
                }
            }break;
        }
    }
    
    
    
    /**
     * Wandelt den Wunsch des Agenten in einen abstrakten Plan (Intention) um.
     * @param modus interner Zustand des Agenten als DesireInteionPlan-Typ.
     */
    public void filter(DesireIntentionPlan modus){
        // verlasse filter ohne neue Intention auszuw�hlen
        if (this.sofortNektarAbliefernTanken) return;
        
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
                } else {
                    newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                    i_neuesZiel=d_Ziel;
                    if (!nextIntentionOK(newIntention, i_neuesZiel)){
                        modus.setIntention(DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN);
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
                    if (selbst.gibGeladeneNektarmenge()>(maxGelNektar/2)) {
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
                if (myPosition.equals(i_altesZiel)){
                    if (i_altesZiel.equals(d_Ziel)){ // an der Blume
                        newIntention=DesireIntentionPlan.P_NEKTARABBAUEN;
                        if ((selbst.gibGeladeneNektarmenge()==this.maxGelNektar) ||
                                (!nextIntentionOK(newIntention, i_altesZiel))) {
                            newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                            modus.setIntentionZiel(this.posBienenstock);
                        }
                        modus.setIntention(newIntention);
                    } else if (i_altesZiel.equals(this.posBienenstock)){ // am Bienenstock
                        if ((selbst.gibGeladeneNektarmenge()>0) ||
                                (selbst.gibGeladeneHonigmenge()<(this.startHonig-this.honigStarten))) {
                            newIntention=DesireIntentionPlan.P_NEKTARABLIEFERNTANKEN;
                            modus.setIntention(newIntention);
                        } else {
                            newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                            modus.setIntention(newIntention);
                            i_neuesZiel=d_Ziel;
                            modus.setIntentionZiel(i_neuesZiel);
                        }
                    }
                }else {
                    newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                    if (!nextIntentionOK(newIntention, i_altesZiel)) {
                        newIntention=DesireIntentionPlan.P_FLIEGENZURKOORDINATE;
                        modus.setIntentionZiel(this.posBienenstock);
                    }
                }
            }
        }
        
    }
    
    /**
     * wandelt die Absicht des Agenten in einen konkreten Plan um.
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
                    Iterator it=weg.iterator();
                    while(it.hasNext()){
                        Koordinate aKoord=(Koordinate)it.next();
                        atp=new ActionTargetPair(actionNr,aKoord.copy());
                        planListe.add(atp);
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
                    } else if (selbst.gibGeladeneNektarmenge() > 0) {
                        actionNr=DesireIntentionPlan.A_NEKTARABLIEFERN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                    } else if ((selbst.gibGeladeneHonigmenge()<(this.startHonig-honigStarten))){
                        actionNr=DesireIntentionPlan.A_TANKEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                    } else sofortNektarAbliefernTanken=false;
                    modus.setPlanListe(planListe);
                    // notfall zur�cksetzen
                    if (sofortNektarAbliefernTanken && (selbst.gibGeladeneNektarmenge()==0) &&
                            (selbst.gibGeladeneHonigmenge()>=(startHonig-honigStarten))) sofortNektarAbliefernTanken=false;
                    
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
        }
    }
    
    /**
     * f�hrt jeweilst erste Aktion aus der planListe aus.
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
        }
        
        
    }
    
    /* ####################### Ende der Festlegung von AgentenVerhalten ########### */
    
    /* ############################    HilfsFunktionen   ######################## */
    
    /**
     * �berpr�ft, ob die n�chste auszuf�hrende Absicht durchgef�hrt werden kann.
     *
     * @param intention zu �berpr�fende Absicht
     * @param i_koordinate ZielKoordinate der Absicht.
     * @return true, falls die auszuf�hrende Absicht durchf�hrbar ist.
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
        if (kosten>myHonig) return false;
        else return true;
        
    }
    
    
    
    
    /**
     * gibt die Kosten an, um von der vorgebenen Position zur�ck zum Bienenstock zu fliegen
     * und dort zu tanken.
     * 
     * @param vonPosition Berechnung der Kosten von der vorgegebenen Position
     * @param bieneIstInLuft ist die Biene in der Luft?
     * @return Kosten um zum Bienenstock zur�ck zu kehren und zu tanken.
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
        LinkedList priorityWSBlumen=new LinkedList();
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
            if (gefundeneBlumenKoord.size()==1) {
                priorityWSBlumen.add((Koordinate)gefundeneBlumenKoord.getFirst());
                return priorityWSBlumen;
            } else{ /* sortiere Blumen nach Entfernung zum Bienenstock und ggf gib die n�chste Blume zum Bienenstock als gesuchte Blume */
                Iterator it=gefundeneBlumenKoord.iterator();
                int minEntf=Integer.MAX_VALUE;
                int aktEntf;
                while(it.hasNext()){
                    Koordinate zielKoord=(Koordinate)it.next();
                    LinkedList weg=this.kuerzesterWeg(this.posBienenstock,
                            zielKoord,
                            this.gespeicherteFelder);
                    aktEntf=weg.size();
                    if (aktEntf<minEntf) {
                        minEntf=aktEntf;
                        priorityWSBlumen.addFirst(zielKoord);
                    }else priorityWSBlumen.addLast(zielKoord);
                    
                }
            } return priorityWSBlumen;
        }else return null;
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
    
    
    /**
     * sucht nach einem Feld das nicht erforscht wurde und am n�chsten zur Biene
     * und am n�chsten zum Bienenstock sich befindet.
     * @return Koordinate des Randfeldes als n�chster Navigationspunkt.
     */
    private Koordinate sucheRandFeld(){
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
            // liefere zuf�lliges Nachbarfeld
            Koordinate position=selbst.gibPosition();
            EinfachesFeld aFeld=(EinfachesFeld)this.lokaleKarte.gibFelder().get(position);
            LinkedList nachbarFelder=new LinkedList(aFeld.gibNachbarfelder().keySet());
            int anzFelder=nachbarFelder.size();
            int indexNr=(int) Math.round(Math.random()*(anzFelder-1));
            return (Koordinate)nachbarFelder.get(indexNr);
        }else{
             /*   TODO    erg�nzen mit der Auswahl der Koordinate, die
              sowohl der Biene als auch dem Bienenstock am n�chsten ist */
          /*  int indexNr=(int) Math.round(Math.random()*(size-1));
            return (Koordinate)randFelder.get(indexNr); */
            LinkedList sortRandFelderBienenstock;
            LinkedList sortRandFelder;
            
            sortRandFelderBienenstock=this.sortKoordinaten(randFelder,this.posBienenstock);
            sortRandFelder=this.sortKoordinaten(sortRandFelderBienenstock,this.selbst.gibPosition());
            
            double zufall=Math.random();
            int index;
            if (zufall<0) zufall=zufall*(-1);
            LinkedList randListe=sortRandFelder;
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
            return (Koordinate)sortRandFelder.get(myWahl);
        }
        
        
    }
    
    
    /**
     * Pr�ft ob die Blume bakannt ist und ob die Blume f�r dan Agenten attraktiv ist.
     *
     * @param blume zu untersuchende Blume
     * @return true, falls die blume Nektar hat und ihre Entfernung zum Bienenstock
     *  einen kritischen Wert nicht �bersteigt.
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
     * Berechnet den Nutzen der Blume in Abh�ngigkeit von ihrer Ausbeute und Enfernung zum Bienenstock.
     * @blume Informationen zu einer Blume, wobei die Probe entnommen sein muss,
     * Ausbeute-NektarProRunde und die Entfernung zum Bienenstock bereits eingetragen sein muessen.
     * @param blume Blume, deren Nutzen berechnet werden soll.
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
     * eine Prozedur die am Bienenstock ausgef�hrt wird und sorgt daf�r,
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
     * gibt den Status des Agenten als Text aus.
     * @param ich Abbild der Biene mit den zugeh�rigen Informationen.
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
     * @param bekannteFelder Mapping aus (Koordinate,EinfachesFeld), das bekannte Felder enth�lt.
     * @return Weg von startKnoten nach zeilKnoten, wobei der startKnoten nicht in der
     * Liste enthalten ist. Falls der weg nicht existiert, so wird null zur�ckgeliefert.
     */
    private LinkedList kuerzesterWeg(Koordinate startKnoten, Koordinate zielKnoten, HashMap bekannteFelder){
        LinkedList weg=new LinkedList(); // Rueckgabe: Weg von startKnoten zum zeilKnoten
        /* transformiere die gespeicherten Felder in nachfolgeKnotenMap */
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
        /* transformation abgeschlossen, nachfolgeKnotenMap hat nun zu je Knoten seine Nachfolger als HashSet */
        HashSet knotenMenge=new HashSet(bekannteFelder.keySet());
        weg=Dijkstra.shortestPath(startKnoten, zielKnoten, knotenMenge, nachfolgeKnotenMap);
        
        return weg;
    }
    
    
    
    /**
     * aktulisiert die gespeicherten Felder mit den lokalen aktuellen Daten
     *
     * @param lokaleFelder gegenw�rtige Sicht des Agenten
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
     * gibt die vorgegebene Koordinaten-Liste sortiert nach der Entfernung zur startKoordinate zur�ck.
     * @param koordList zu sortierenden Koordinaten-Liste
     * @param startKoordinate Start-Koordinate
     * @return Koordinaten-Liste, sortiert nach der Entfernung zur startKoordinate
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
    
    
    
    /* #######################   Beginn: primitive Aktionen    ######################## */
    
    
    
    /** l�sst die Biene tanken */
    private void tanken(){
        System.out.println(selbst.gibBienenID()+ ": tanke " + selbst.gibPosition().toString());
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
    
    /** l�sst die Biene starten */
    private void starten(){
        System.out.println(selbst.gibBienenID()+ ": starte " + selbst.gibPosition().toString());
        long neuerAktCode = handler.aktionStarten(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        if (erfolg.getValue()) bieneIstInDerLuft=true;
        else starten();
    }
    
    /** l�sst die Biene landen */
    private void landen(){
        System.out.println(selbst.gibBienenID()+ ": lande " + selbst.gibPosition().toString());
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
     * fliegt zur n�chsten Position
     * @param nextZiel n�chste ZielPosition
     */
    private void fliegen(Koordinate nextZiel){
        System.out.println(selbst.gibBienenID()+ ": fliege zur " + nextZiel.toString());
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
    
    /** l�sst die Biene Nektar abbauen */
    private void nektarAbbauen(){
        int max=1000;
        System.out.println(selbst.gibBienenID()+ ": baue Nektar ab " + selbst.gibPosition().toString());
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
    
    /** l�sst die Biene Nektar abliefern */
    private void nektarAbliefern(){
        System.out.println(selbst.gibBienenID()+ ": liefere Nektar ab " + selbst.gibPosition().toString());
        this.gesammelterNektar=this.gesammelterNektar + selbst.gibGeladeneNektarmenge();
        long neuerAktCode = handler.aktionNektarAbliefern(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
    }
    
    /**
     * l�sst die Biene tanzen und teilt dadurch die Koordinate der Blume mit.
     *
     * @param blumenKoordinate mitzuteilende Koordinate der Blume
     */
    private void tanzen(Koordinate blumenKoordinate){
        int koordX=blumenKoordinate.gibXPosition();
        int koordY=blumenKoordinate.gibYPosition();
        long neuerAktCode = handler.aktionTanzen(aktCode, erfolg, koordX, koordY, true, true);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
    }
    
    /**
     * L�sst die Biene einer anderen Biene beim Tanzen zuschauen.
     * Die Information zur mitgeteilten Blume (entfernung und richtung) steht
     * nach der aktualisierung der perception in selbst.gitInfo().
     * 
     * @param tanzendeBieneID Id der tanzenden Biene
     * @return ungef�re Koordinate der Blume.
     */
    private Koordinate zuschauen(int tanzendeBieneID){
        Info infoAnf=selbst.gibInformation();
        Koordinate blumenKoord=null;
        long neuerAktCode = handler.aktionZuschauen(aktCode, erfolg, tanzendeBieneID);
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
        return blumenKoord;
    }
    
    
    /**
     * versetzt die Biene in den Warte-Zustand.
     */
    public void warten() {
        System.out.println(id + ": warten " + selbst.gibPosition().toString());
        long neuerAktCode = handler.aktionWarten(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
    }
    
    
    /* ##################### Ende der Primitiven Aktionen ####################### */
    
    
}