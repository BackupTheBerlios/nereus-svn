/*
 * Dateiname      : UnkooperativerBDIAgent.java
 * Erzeugt        : 21. Mai 2005
 * Letzte Änderung: 1. Juni 2005 durch Eugen Volk
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
import nereus.utils.ActionTargetPair;

import java.util.Iterator;
import java.util.Random;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;
import nereus.utils.Dijkstra;
import scenarios.bienenstock.agenteninfo.Info;
import scenarios.bienenstock.agenteninfo.Koordinate;
import agents.bienenstock.DesireIntentionPlan;
import agents.bienenstock.InfoBlume;

/**
 * Die Klasse ist Agent und ist aufgebaut nach der Belief-Desire-Intention Architektur.
 */
public class UnkooperativerBDIAgent
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
    
    //   HashMap blumen = new HashMap();
    /** (Koordinate, InfoBlume) */
    HashMap bekannteBlumen=new HashMap();
    
    
    /** eine Navigationskarte als Gedächtnis über die wahrgenommenen Felder */
    HashMap gespeicherteFelder=new HashMap();
    
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
    
    
    private boolean sofortNektarAbliefernTanken = false;
    
    /**
     * Zahl zur initialisierung des Zufallsgenerators.
     */
    private long samen = System.currentTimeMillis();
    
    /**
     * Wird zur Wahl der initialen Flugrichtung benötigt.
     */
    Random zufallsGenerator = new Random();
    
    
    public UnkooperativerBDIAgent(String bName, AbstractScenarioHandler bHandler) {
        super(bName, bHandler);
        name = bName;
        handler = (IBienenstockSzenarioHandler)bHandler;
        volksID = 1;
        zufallsGenerator.setSeed(samen);
        
    }
    
    public UnkooperativerBDIAgent() {
        super();
        volksID = 1;
        zufallsGenerator.setSeed(samen);
    }
    
    public UnkooperativerBDIAgent(Id bId,String bName) {
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
        modus.setDesire(modus.D_BLUMENSUCHEN);
        
        while(true){
            perception();
            this.visualisiereBiene(selbst);
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
     * Überprüft den Wusch (Desire-Ziel) des Agenten und wählt gegebenfalls einen Neuen aus.
     * @param modus interner Zustand des Agenten als DesireInteionPlan-Typ.
     */
    public void evaluate(DesireIntentionPlan modus){
        Koordinate myPosition=selbst.gibPosition();
        Koordinate neuesZiel;
        Koordinate altesZiel=modus.getDesireZiel();
        int desire=modus.getDesire();
        
        if (this.sofortNektarAbliefernTanken) return;
        
        switch (desire){
            case DesireIntentionPlan.D_UMGEBUNGERFORSCHEN:{
                if (altesZiel==null || (altesZiel.equals(myPosition))){
                    neuesZiel=sucheRandFeld();
                    modus.setDesireZiel(sucheRandFeld());
                }
            }break;
            case DesireIntentionPlan.D_BLUMENSUCHEN:{
                LinkedList blumen=sucheBlumeInSichtbarenFeldern();
                if (blumen!=null) {
                    modus.setDesire(DesireIntentionPlan.D_BLUMENPROBEENTNEHMEN);
                    neuesZiel=(Koordinate)blumen.getFirst();
                    modus.setDesireZiel(neuesZiel.copy());
                }else{
                    if (altesZiel==null || (altesZiel.equals(myPosition))){
                        neuesZiel=sucheRandFeld();
                        modus.setDesireZiel(neuesZiel);
                    }
                }
            }break;
            case DesireIntentionPlan.D_BLUMENPROBEENTNEHMEN:{
                if (myPosition.equals(altesZiel)){
                    InfoBlume infoBlume=(InfoBlume)bekannteBlumen.get(altesZiel);
                    if (infoBlume.getProbeEntnommen()){
                        if ((infoBlume.getNutzen()>0) && infoBlume.getHatNektar())
                            modus.setDesire(DesireIntentionPlan.D_BLUMEBEARBEITEN);
                        else{
                            modus.setDesire(DesireIntentionPlan.D_BLUMENSUCHEN);
                            modus.setDesireZiel(null);
                            evaluate(modus);
                        }
                        
                    }
                }
            }break;
            case DesireIntentionPlan.D_BLUMEBEARBEITEN:{
                InfoBlume infoBlume=(InfoBlume)bekannteBlumen.get(altesZiel);
                if (!(infoBlume.getHatNektar() && infoBlume.getProbeEntnommen()
                && (infoBlume.getNutzen()>0))) {
                    modus.setDesire(DesireIntentionPlan.D_BLUMENSUCHEN);
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
        // verlasse filter ohne neue Intention auszuwählen
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
            case DesireIntentionPlan.D_UMGEBUNGERFORSCHEN:{
                modus.setIntention(DesireIntentionPlan.I_FLIEGENZURKOORDINATE);
                modus.setIntentionZiel(d_Ziel);
            }break;
            case DesireIntentionPlan.D_BLUMENSUCHEN:{
                if (selbst.gibGeladeneNektarmenge()>0) {
                    modus.setIntention(DesireIntentionPlan.I_NEKTARABLIEFERNTANKEN);
                } else {
                    newIntention=DesireIntentionPlan.I_FLIEGENZURKOORDINATE;
                    i_neuesZiel=d_Ziel;
                    if (!nextIntentionOK(newIntention, i_neuesZiel)){
                        modus.setIntention(DesireIntentionPlan.I_NEKTARABLIEFERNTANKEN);
                    }else{
                        modus.setIntention(newIntention);
                        modus.setIntentionZiel(i_neuesZiel);
                    }
                }
            }break;
            case DesireIntentionPlan.D_BLUMENPROBEENTNEHMEN:{
                i_neuesZiel=d_Ziel;
                if (d_Ziel.equals(myPosition)) newIntention=DesireIntentionPlan.I_NEKTARABBAUEN;
                else {
                    if (selbst.gibGeladeneNektarmenge()>0) {
                        newIntention=DesireIntentionPlan.I_NEKTARABLIEFERNTANKEN;
                        i_neuesZiel=this.posBienenstock;
                    } else{
                        newIntention=DesireIntentionPlan.I_FLIEGENZURKOORDINATE;
                        i_neuesZiel=d_Ziel;
                    }
                }
                 if (!nextIntentionOK(newIntention, i_neuesZiel)) {
                        newIntention=DesireIntentionPlan.I_NEKTARABLIEFERNTANKEN;
                        i_neuesZiel=this.posBienenstock;
                    }
                modus.setIntention(newIntention);
                modus.setIntentionZiel(i_neuesZiel);
            }break;
            case DesireIntentionPlan.D_BLUMEBEARBEITEN:{
                if (i_altesZiel==null)  {
                    modus.setIntentionZiel(d_Ziel);
                    i_altesZiel=d_Ziel;
                }
                if (myPosition.equals(i_altesZiel)){
                    if (i_altesZiel.equals(d_Ziel)){ // an der Blume
                        newIntention=DesireIntentionPlan.I_NEKTARABBAUEN;
                        if ((selbst.gibGeladeneNektarmenge()==this.maxGelNektar) ||
                                (!nextIntentionOK(newIntention, i_altesZiel))) {
                            newIntention=DesireIntentionPlan.I_FLIEGENZURKOORDINATE;
                            modus.setIntentionZiel(this.posBienenstock);
                        }
                        modus.setIntention(newIntention);
                    } else if (i_altesZiel.equals(this.posBienenstock)){ // am Bienenstock
                        if ((selbst.gibGeladeneNektarmenge()>0) ||
                                (selbst.gibGeladeneHonigmenge()<(this.startHonig-this.honigStarten))) {
                            newIntention=DesireIntentionPlan.I_NEKTARABLIEFERNTANKEN;
                            modus.setIntention(newIntention);
                        } else {
                            newIntention=DesireIntentionPlan.I_FLIEGENZURKOORDINATE;
                            modus.setIntention(newIntention);
                            i_neuesZiel=d_Ziel;
                            modus.setIntentionZiel(i_neuesZiel);
                        }
                    }
                }else {
                    newIntention=DesireIntentionPlan.I_FLIEGENZURKOORDINATE;
                    if (!nextIntentionOK(newIntention, i_altesZiel)) {
                        newIntention=DesireIntentionPlan.I_FLIEGENZURKOORDINATE;
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
            case DesireIntentionPlan.I_FLIEGENZURKOORDINATE:{
                Koordinate zielKoord=new Koordinate(-1,-1);
                if ((planListe!=null) && (planListe.size()>0)) zielKoord=(Koordinate)((ActionTargetPair)planListe.getLast()).getTarget();
                if ((planListe==null)  || (!(zielKoord.equals(i_ziel)))){
                    planListe=new LinkedList();
                    if (!this.bieneIstInDerLuft) {
                        actionNr=DesireIntentionPlan.P_STARTEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                    }
                    LinkedList weg=this.kuerzesterWeg(myPosition, i_ziel, this.gespeicherteFelder);
                    actionNr=DesireIntentionPlan.P_FLIEGEN;
                    Iterator it=weg.iterator();
                    while(it.hasNext()){
                        Koordinate aKoord=(Koordinate)it.next();
                        atp=new ActionTargetPair(actionNr,aKoord.copy());
                        planListe.add(atp);
                    }
                    modus.setPlanListe(planListe);
                }
            }break;
            case DesireIntentionPlan.I_NEKTARABBAUEN:{
                planListe=new LinkedList();
                if (this.bieneIstInDerLuft) {
                    actionNr=DesireIntentionPlan.P_LANDEN;
                    atp=new ActionTargetPair(actionNr,myPosition.copy());
                    planListe.add(atp);
                }
                actionNr=DesireIntentionPlan.P_NEKTARABBAUEN;
                atp=new ActionTargetPair(actionNr,myPosition.copy());
                planListe.add(atp);
                modus.setPlanListe(planListe);
            }break;
            case DesireIntentionPlan.I_NEKTARABLIEFERNTANKEN:{
                this.sofortNektarAbliefernTanken=true;
                boolean nektarAbgeliefert=false;
                boolean getankt=false;
                planListe=new LinkedList();
                if (myPosition.equals(this.posBienenstock)){
                    if (this.bieneIstInDerLuft) {
                        actionNr=DesireIntentionPlan.P_LANDEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                    }
                    if (selbst.gibGeladeneHonigmenge() < (honigTanken + honigAbliefern)) {
                        actionNr=DesireIntentionPlan.P_TANKEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                        getankt=true;
                    }
                    if (selbst.gibGeladeneNektarmenge() > 0) {
                        actionNr=DesireIntentionPlan.P_NEKTARABLIEFERN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                        nektarAbgeliefert=true;
                    }
                    if (nektarAbgeliefert || (selbst.gibGeladeneHonigmenge()<(this.startHonig-honigStarten))){
                        actionNr=DesireIntentionPlan.P_TANKEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                    }
                    modus.setPlanListe(planListe);
                    // notfall zurücksetzen
                    if (sofortNektarAbliefernTanken && (selbst.gibGeladeneNektarmenge()==0) &&
                            (selbst.gibGeladeneHonigmenge()>=(startHonig-honigStarten))) sofortNektarAbliefernTanken=false;
                    
                }else {
                    if (!this.bieneIstInDerLuft) {
                        actionNr=DesireIntentionPlan.P_STARTEN;
                        atp=new ActionTargetPair(actionNr,myPosition.copy());
                        planListe.add(atp);
                    }
                    LinkedList weg=kuerzesterWeg(myPosition, this.posBienenstock, this.gespeicherteFelder);
                    actionNr=DesireIntentionPlan.P_FLIEGEN;
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
            case DesireIntentionPlan.P_FLIEGEN:{
                fliegen(ziel);
            }break;
            case DesireIntentionPlan.P_STARTEN:{
                starten();
            }break;
            case DesireIntentionPlan.P_LANDEN:{
                landen();
            }break;
            case DesireIntentionPlan.P_TANKEN:{
                tanken();
            }break;
            case DesireIntentionPlan.P_NEKTARABBAUEN:{
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
                } else {System.out.println(" NEKTAR VOLL VOLL");}
            }break;
            case DesireIntentionPlan.P_NEKTARABLIEFERN:{
                this.nektarAbliefern();
            }break;
        }
        
        
    }
    
    /* ####################### Ende der Festlegung von AgentenVerhalten ########### */
    
    /* ############################    HilfsFunktionen   ######################## */
    
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
            case DesireIntentionPlan.I_FLIEGENZURKOORDINATE:{
                if (i_koordinate.equals(posBienenstock)) return true;
                kosten=kostenFliegen(myPosition, i_koordinate) + kostenNachHauseTanken(i_koordinate, this.bieneIstInDerLuft);
            }break;
            case DesireIntentionPlan.I_NEKTARABBAUEN: {
                kosten=kostenNachHauseTanken(i_koordinate, false)+this.honigAbbauen;
                if (this.bieneIstInDerLuft) kosten=kosten + this.honigLanden;
                
            }break;
            case DesireIntentionPlan.I_NACHHAUSETANKEN: return true;
            case DesireIntentionPlan.I_NEKTARABLIEFERNTANKEN: return true;
            case DesireIntentionPlan.I_AMBIENENSTOCK: return true;
            case DesireIntentionPlan.I_ZURUECKZURBLUME: {
                kosten=kostenFliegen(myPosition, i_koordinate) + kostenNachHauseTanken(i_koordinate, this.bieneIstInDerLuft);
                if (!this.bieneIstInDerLuft) kosten=kosten + this.honigStarten;
                kosten=2*this.honigAbbauen;
            }
        }
        if (kosten>myHonig) return false;
        else return true;
        
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
            } else{ /* sortiere Blumen nach Entfernung zum Bienenstock und ggf gib die nächste Blume zum Bienenstock als gesuchte Blume */
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
    
    
    /** sucht nach einem Feld das nicht erforscht wurde und am nächsten zur Biene
     * und am nächsten zum Bienenstock sich befindet. */
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
            // liefere zufälliges Nachbarfeld
            Koordinate position=selbst.gibPosition();
            EinfachesFeld aFeld=(EinfachesFeld)this.lokaleKarte.gibFelder().get(position);
            LinkedList nachbarFelder=new LinkedList(aFeld.gibNachbarfelder().keySet());
            int anzFelder=nachbarFelder.size()-1;
            int indexNr=(int) Math.round(Math.random()*(anzFelder-1));
            return (Koordinate)nachbarFelder.get(indexNr);
        }else{
             /*   TODO    ergänzen mit der Auswahl der Koordinate, die
              sowohl der Biene als auch dem Bienenstock am nächsten ist */
            return (Koordinate)randFelder.getFirst();
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
    
    
    
    /* #######################   Beginn: primitive Aktionen    ######################## */
    
    
    /** lässt die Biene tanken */
    private void tanken(){
        System.out.println(selbst.gibBienenID()+ ": tanke " + selbst.gibPosition().toString());
        int mengeZuTanken=this.startHonig-selbst.gibGeladeneHonigmenge();
        long neuerAktCode = handler.aktionHonigTanken(aktCode,mengeZuTanken);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
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
        int koordX=blumenKoordinate.gibXPosition();
        int koordY=blumenKoordinate.gibYPosition();
        long neuerAktCode = handler.aktionTanzen(aktCode, koordX, koordY, true, true);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
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
        return blumenKoord;
    }
    
    /* ##################### Ende der Primitiven Aktionen ####################### */
    
    
}