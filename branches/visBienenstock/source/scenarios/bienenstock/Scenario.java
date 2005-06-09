/*
 * Dateiname      : Scenario.java
 * Erzeugt        : 16. Oktober 2004
 * Letzte ?nderung: 08. Juni 2005 durch Samuel Walz
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (samuel@gmx.info)
 *                  Eugen Volk
 *
 *
 * Diese Datei geh?rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut f?r
 * Intelligente Systeme der Universit?t Stuttgart unter Betreuung von
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




package scenarios.bienenstock;

import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import nereus.exceptions.DoppeltesSpielException;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.NotEnoughEnergyException;
import nereus.exceptions.InvalidElementException;
import nereus.simulatorinterfaces.AbstractScenario;
import nereus.simulatorinterfaces.AbstractScenarioHandler;
import nereus.simulatorinterfaces.statistic.IStatisticComponent;
import nereus.simulatorinterfaces.statistic.IStatisticScenario;
import nereus.simulatorinterfaces.IVisualisationServerIntern;
import scenarios.bienenstock.einfacheUmgebung.EinfacheKarte;
import scenarios.bienenstock.umgebung.Karte;
import scenarios.bienenstock.umgebung.Bienenstock;
import scenarios.bienenstock.statistik.StatistikBienenAgent;
import scenarios.bienenstock.statistik.BienenStatistikKomponente;
import nereus.simulatorinterfaces.IInformationHandler;
import nereus.utils.Id;
import nereus.utils.ParameterDescription;
import nereus.simulator.ServerInfoObject;
import nereus.utils.GameConf;

import scenarios.bienenstock.visualisationgui.*;

import nereus.simulator.ServerInfoObject;

import nereus.utils.ScenarioXMLInputReader;
import nereus.utils.ScenarioXMLConfigHandler;
import scenarios.bienenstock.interfaces.AbstrakteBiene;
import nereus.utils.Parameter;
import scenarios.bienenstock.scenariomanagement.Konstanten;
import scenarios.bienenstock.agenteninfo.Koordinate;
import scenarios.bienenstock.scenariomanagement.BienenstockSzenarioHandler;
import scenarios.bienenstock.scenariomanagement.ObjektWartendeAnfragen;




/**
 * steuert den Ablauf des Spiels.
 *
 *  @author Philip Funck
 *  @author Samuel Walz
 */
public class Scenario
        extends AbstractScenario
        implements IStatisticScenario {
    
    /**
     * @see java.security.PublicKey.serialVersionUID
     */
    static final long serialVersionUID = 1L;
    /**
     * Verzeichnis fuer Szenario-Karten
     */
    private String KARTENORDNER="karten";
    /**
     * Dateiname fuer Szenario-Karte
     */
    private String KARTENDATEINAME;//="test2.gml"; // Default-Wert
    
    /**
     * Name des Szenario
     */
    private String SZENARIONAME="bienenstock";
    
    /**
     * Verzeichnis in dem sich die XML-Config-Datei mit Szenario-parametern
     * befindet.
     */
    private String SZENARIOCONFIGORDNER="parameters";
    
    /**
     * Name der XML-Config-Datei mit Szenario-parametern
     */
    private String SZENARIOCONFIGDATEINAME;//="bienenstockconfig.xml"; //Default-Wert
    
    /**
     * ist die Nummer der aktuellen Runde.
     */
    private int rundennummer = 0;
    
    /**
     * gibt die Phase wieder in der sich der Spielmeister derzeit befindet.
     *
     * siehe AblaufdiagrammSpielMeister
     */
    private int phase;
    
    /**
     * die anzahl der am Spiel angemeldeten Agenten.
     */
    private int anzahlAngemeldeterAgenten = 0;
    
    /**
     * ist die Zeit, die der SpielMeister wartet bis er von dem Start der
     * <code>wartephase</code> zur <code>bearbeitungsphase</code>
     * ?bergeht.
     */
    private int timeout;
    
    /**
     * ist eine Tabelle, die die Szenario-internen ID's in
     * Simulator ID's ?bersetzt.
     */
    private Hashtable mAgentIDs = new Hashtable();
    
    /**
     * h?lt die Anfragen von Agenten, die zu fr?h anfragen.
     */
    private HashSet verfruehteAnfragen = new HashSet();
    
    /**
     * ist eine Liste aller auf der Karte befindlichen Bienenst?cke.
     */
    private HashSet bienenStoecke;
    
    /**
     * enth?lt Dummy-objekte an denen jeweils ein Agent darauf wartet,
     * das seine Anfrage abgearbeitet wird.
     */
    private ObjektWartendeAnfragen wartendeAnfragen
            = new ObjektWartendeAnfragen();
    
    /**
     * ist das Objekt an dem die Bearbeitungsphase wartet, solange sie darauf
     * wartet, das die Anfrage eines Agenten abgearbeitet wird.
     */
    private Object zeigerBearbeitungsphase = new Object();
    
    /**
     * ist das Objekt an dem die Wartephase wartet, solange sie darauf wartet,
     * das alle Agenten ihre Anfragen stellen oder das Timeout erreicht wird.
     */
    private Object zeigerWartephase = new Object();
    
    /**
     * enth?lt die Parameter f?r das Szenario
     */
    private Parameter parameter = new Parameter();
    
    /**
     * @see umgebung.Karte
     */
    private Karte spielkarte;
    
    /**
     * @see BienenstockSzenarioHandler
     */
    private BienenstockSzenarioHandler szenariohandler
            = new BienenstockSzenarioHandler(this);
    
    /**
     * @see statistik.BienenStatistikKomponente
     */
    private BienenStatistikKomponente statistik = null;
    
    /**
     * enth?lt alle Statistikagenten f?r die aktuelle Runde.
     */
    private Vector statAgentIds = null;
    
    /**
     * signalisiert der Bearbeitungsphase, ob die Anfrage des Agenten schon
     * abgearbeitet wurde, bevor sie anfangen will auf eben dieses Ereigniss
     * zu warten.
     */
    private boolean bearbeitungsphaseMussWarten = false;
    
    /**
     * ServerInfoObject zum auslesen der Informationen ueber die verwendenden
     * Karten und Scenario-XML-Konfig-Dateien.
     */
    private ServerInfoObject serverInfoObject;
    
    /**
     * Konfigurationsdatei, in der Dateiname fuer Karte und ScenarioKonfigurationsdatei
     * enthalten sind.
     */
    private GameConf gameConf;
    
    /**
     * Authentifizierungscode zum ?bertragen der Informationsobjekte an die
     * Server-Vis-Komponente
     */
    private long visAuthCode = 0L;    
    
    
    private Visualisierung vis;
    
    /**
     * Konstruktor.
     *
     * @param gameId            ID des Spiels
     * @param visHandler        Der Visualisierungshandler f?r das Szenario
     * @param parameterTabelle  Die Parameter f?r das Spiel
     * @param visServer         Die Server-Vis-Komponente
     */
    public Scenario(Id gameId,
            IInformationHandler visHandler,
            Hashtable parameterTabelle,
            IVisualisationServerIntern visServer) {
        super(gameId,
                visHandler,
                parameterTabelle,
                visServer);
        this.serverInfoObject=ServerInfoObject.m_instance;
        this.gameConf=(GameConf)serverInfoObject.getGameConf(this.SZENARIONAME);
        this.KARTENDATEINAME=this.gameConf.getKartenDateiName();
        this.SZENARIOCONFIGDATEINAME=this.gameConf.getParameterDateiName();
        setzeParameter(parameterTabelle);
    }
    
    /**
     * Konstruktor.
     *
     */
    public Scenario() {
        super();
        
    }
    
    
    
    
    
    /**
     * Initialisiert die Werte m_gameId, visHandler und parameter.
     * Dient als ersatz des parametrisierten Konstruktors.
     *
     * @parm gameId Id des Spiels
     * @param InformationHandler vishandler - InformationHandler
     * @param Hashtable parameterTabelle - Spielparameter
     */
    public void initialize(
            Id gameId,
            IInformationHandler visHandler,
            Hashtable parameterTabelle,
            GameConf gameConf,
            IVisualisationServerIntern visServer){
        super.m_gameId = gameId;
        super.m_visHandler = visHandler;
        super.m_parameter = parameterTabelle;
        super.m_visualisationServer = visServer;
        
        this.serverInfoObject=ServerInfoObject.m_instance;
        this.gameConf=gameConf;
        this.KARTENDATEINAME=this.gameConf.getKartenDateiName();
        this.SZENARIOCONFIGDATEINAME=this.gameConf.getParameterDateiName();
        setzeParameter(parameterTabelle);
    }
    
    
    
    /**
     * setzt die Liste der ?bergebenen Parameter und die Liste der
     * Standardwerte zusammen.
     *
     * Dabei werden haupts?chlich die ?bergebenen Parameter verwendet,
     * fehlt jedoch ein Parameter in der ?bergebenen Liste, oder ist ein
     * Parameter der Standartparameter schreibgesch?tzt, so wird der
     * Parameter der Standardparameterliste verwendet.
     *
     * @param parameterTabelle   Liste der vom Simulator ?bergebenen Werte
     */
    private void setzeParameter(Hashtable parameterTabelle) {
        LinkedList defaults=getScenarioParameter();
        
        int i;
        String aktuellParName;
        for (i = 0; i < defaults.size(); i++) {
            nereus.utils.ParameterDescription aktuell
                    = (nereus.utils.ParameterDescription) defaults.get(i);
            aktuellParName=aktuell.getParameterName();
            if (aktuell.isChangeable()
            && parameterTabelle.containsKey(aktuellParName)) {
                parameter.setzeWert(aktuellParName,
                        parameterTabelle.get(aktuellParName));
            } else {
                parameter.setzeWert(aktuellParName,
                        aktuell.getDefaultValue());
            }
            parameter.setzeSchreibschutz(aktuellParName);
        }
    }
    
    /**
     * pr?ft ob eine der Endbedingungen zutrifft.
     *
     * Ist eine Bedingung auf null gesetzt, so wird sie nicht gepr?ft.
     *
     * @return      Gibt einen Boolean-Wert zur?ck, der singnalisiert,
     *              ob eine der Endbedingungen zutrifft
     */
    private boolean endbedingungTrifftZu() {
        if (((Integer) parameter.gibWert("endbedingungMaxNektar")).intValue()
        > 0) {
            Iterator stoecke = bienenStoecke.iterator();
            while (stoecke.hasNext()) {
                Bienenstock tmp = (Bienenstock) stoecke.next();
                if (tmp.gibVorhandenerNektar()
                >= ((Integer) parameter.gibWert(
                        "endbedingungMaxNektar")).intValue()) {
                    System.out.println("endbedingungMaxNektar erreicht.");
                    return true;
                }
            }
            
        } else if (((Integer) parameter.gibWert(
                "endbedingungMaxHonig")).intValue() > 0) {
            Iterator stoecke = bienenStoecke.iterator();
            while (stoecke.hasNext()) {
                Bienenstock tmp = (Bienenstock) stoecke.next();
                System.out.println(
                        "(Spielmeister.endbedtrifftzu)Honig im stock: "
                        + tmp.gibVorhandenerHonig());
                if (tmp.gibVorhandenerHonig()
                >= ((Integer) parameter.gibWert(
                        "endbedingungMaxHonig")).intValue()) {
                    System.out.println("endbedingungMaxHonig erreicht.");
                    return true;
                }
            }
            
        } else if (rundennummer
                == ((Integer) parameter.gibWert(
                "endbedingungMaxRunden")).intValue()) {
            System.out.println("endbedingungMaxRunden erreicht.");
            return true;
        } else if (spielkarte.alleBienenTot()) {
            System.out.println("Alle Bienen sind verstorben.");
            return true;
        }
        
        return false;
        
    }
    
    /**
     * ist die Phase, in der das Spiel initiiert wird.
     *
     * Es wird davon ausgegangen, da? alle Agenten vorher schon
     * am Simulator eingetragen sind.
     */
    private void startphase() {
        
        System.out.println("*------.oOo.-------*");
        System.out.println("- Starte das Spiel -");
        System.out.println("*------------------*");
        
        phase = Konstanten.STARTPHASE;
        
        ServerInfoObject sio=ServerInfoObject.m_instance;
        String pathSep=sio.getPathSeparator();
        String scenarioPath=sio.getScenarioPath();
        String kartenPath;
        kartenPath=scenarioPath + pathSep + SZENARIONAME + pathSep
                + KARTENORDNER + pathSep + KARTENDATEINAME;
        //Vorsicht: Hard gecodeter Dateiname
        //    spielkarte = new Karte("scenario/bienenstock/gmlFile/test2.gml", this);
        spielkarte = new Karte(kartenPath, this);
        timeout = ((Integer) parameter.gibWert("timeout")).intValue();
        bienenStoecke = spielkarte.bienenstoeckeSuchen();
        anzahlAngemeldeterAgenten = m_agents.size();
        
        //Bienen instanziieren
        Enumeration mAgentEnum = m_agents.elements();
        while (mAgentEnum.hasMoreElements()) {
            AbstrakteBiene biene = (AbstrakteBiene) mAgentEnum.nextElement();
            int id = mAgentIDs.size() + 1;
            mAgentIDs.put(new Integer(id), biene.getId().toString());
            biene.setHandler(szenariohandler);
            long myActionCode=spielkarte.bieneEinfuegen(id,
                    biene.gibVolksID(), biene.getId());
            biene.aktionscodeSetzen(myActionCode);
        }
        //zum testen der visualisierung
        //vis = new Visualisierung();
    }
    
    /**
     * ist die Phase, in der der SpielMeister auf Anfragen,
     * die in dieser Runde bearbeitet werden sollen, wartet.
     *
     * Nach einer gewissen Zeit, dem sogenannten <code>timeout</code>,
     * oder wenn alle Agenten ihre Aktionsanfrage gestellt haben,
     * geht der Spielmeister in die <code>bearbeitungsphase</code> ?ber.
     */
    private void wartephase() {
        
        //System.out.println("(SM) neue Runde");
        
        // Rundennummer inkrementieren
        rundennummer = rundennummer + 1;
        
        //Karte den Start der neuen Runde mitteilen
        spielkarte.neueRunde();
        
        // Flags setzen
        phase = Konstanten.WARTEPHASE;
        System.out.println("\n" + "Neue Runde Nr.: " + rundennummer);
        
        synchronized (verfruehteAnfragen) {
            verfruehteAnfragen.notifyAll();
        }
        
        /*
         * Warten bis timeout in millisek. zum Ende f?hrt.
         */
        //System.out.println("agenten im spiel: " + anzahlAngemeldeterAgenten);
        if (anzahlAngemeldeterAgenten > wartendeAnfragen.gibAnzahl()) {
            try {
                synchronized (zeigerWartephase) {
                    zeigerWartephase.wait(timeout);
                }
            } catch (InterruptedException fehler) {
                System.out.println(fehler.getMessage());
            }
        }
        synchronized (wartendeAnfragen) {
            //System.out.println(wartendeAnfragen.gibAnzahl()
            //+ " Aktionen angefragt.");
        }
    }
    
    /**
     * bearbeitet die von den Agenten gestellten Aktionsanfragen in einer
     * zuf?lligen Reihenfolge.
     *
     * Abschlie?end wird gepr?ft, ob die Endbedingungen zutreffen,
     * und gegebenenfalls in die <code>endpahse</code> ?bergegangen.
     * Ansonsten geht der Spielmeister wieder in
     * die <code>wartephase</code> ?ber.
     *
     * Aktionsanfragen,die zu diesem Zeitpunkt gestellt werden, werden,
     * falls sie g?ltig sind, gehalten und in der n?chsten
     * <code>bearbeitungsphase</code> bearbeitet.
     */
    private void bearbeitungsphase() {
        int anzahlWartendeAnfragen = 0;
        
        // Flags setzen
        phase = Konstanten.BEARBEITUNGSPHASE;
        
        /*
         * Bearbeiten der Aktionsanfragen in zuf?lliger Reihenfolge.
         * Der Zufall entsteht durch die zuf?llige Wahl der Aktinscodes,
         * nach denen <code>verwendeteAktionsCodes</code> sortiert ist.
         */
        anzahlWartendeAnfragen = wartendeAnfragen.gibAnzahl();
        int i;
        for (i = 0; i < anzahlWartendeAnfragen; i++) {
            bearbeitungsphaseMussWarten = true;
            synchronized (wartendeAnfragen) {
                wartendeAnfragen.notify();
            }
            
            synchronized (zeigerBearbeitungsphase) {
                if (bearbeitungsphaseMussWarten) {
                    try {
                        zeigerBearbeitungsphase.wait();
                    } catch (InterruptedException fehler) {
                        System.out.println(fehler.getMessage());
                    }
                }
            }
            
            
        }
        synchronized (wartendeAnfragen) {
            wartendeAnfragen.setzeNull();
        }
    }
    
    /**
     * ist die Phase in der der Spielmeister der Statistik die letzten Werte
     * mitteilt und alles abmeldet.
     */
    private synchronized void endphase() {
        phase = Konstanten.ENDPHASE;
        System.out.println("SPIEL BEENDET.");
        
    }
    
    /**
     * pr?ft einen aktionscode auf G?ltigkeit.
     *
     * Es wird dabei ?berpr?ft, ob der Aktionscode schon verwendet wurde,
     * und ob er in der letzten Runde ?berhaupt ausgegeben wurde.
     * Durch die ?berpr?fung verliert der Aktionscode seine G?ltigkeit.
     *
     * @param aktCode       Der Aktionscode eines Agenten
     * @return      Boolean-Wert, der angibt, ob der Aktionscode noch
     *              g?ltig war
     */
    private synchronized boolean aktionscodeVerwendbar(long aktCode) {
        return (spielkarte.aktionscodeEntwerten(aktCode));
    }
    
    /**
     * banachrichtig den Spielmeister,
     * da? der Thread mit der Abarbeitung der Aktion fertig ist.
     *
     * @param aktCode       Der Aktionscode eines Agenten
     */
    private void aktionAusgefuehrt(long aktCode) {
        synchronized (zeigerBearbeitungsphase) {
            bearbeitungsphaseMussWarten = false;
            zeigerBearbeitungsphase.notify();
        }
    }
    
    /**
     * dient zur Synchronisation der Anfragen der Agenten.
     *
     * @param aktCode       Der Aktionscode eines Agenten
     * @return              gibt zur?ck, ob der Aktionscode verwendbar war
     */
    private boolean synchronisiereAktion(long aktCode) {
        if (phase == Konstanten.STARTPHASE
                | phase == Konstanten.BEARBEITUNGSPHASE) {
            try {
                synchronized (verfruehteAnfragen) {
                    verfruehteAnfragen.wait();
                }
            } catch (InterruptedException fehler) {
                System.out.println("kam zu frueh und konnte nicht warten");
            }
        }
        
        //pr?fen, ob der aktionscode gueltig ist
        if (aktionscodeVerwendbar(aktCode)) {
            
            synchronized (wartendeAnfragen) {
                wartendeAnfragen.inkrementiere();
                // war es die letzte Anfrage?
                if (anzahlAngemeldeterAgenten == wartendeAnfragen.gibAnzahl()) {
                    synchronized (zeigerWartephase) {
                        zeigerWartephase.notify();
                        
                    }
                }
                
                try {
                    wartendeAnfragen.wait();
                } catch (InterruptedException fehler) {
                    System.out.println(fehler.getMessage());
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * liefert den Ausschnitt der Karte, der in Sichtweite des Agenten liegt.
     *
     * Identifiziert den Agenten ?ber seinen Aktionscode.
     *
     * @param aktcode           der Aktionscode des Agenten.
     * @return EinfacheKarte    Der f?r den Agenten sichtbare Ausschnitt der
     *                          Spielkarte
     */
    public EinfacheKarte infoAusschnittHolen(long aktcode) {
        if (phase == Konstanten.STARTPHASE
                | phase == Konstanten.BEARBEITUNGSPHASE) {
            
            try {
                synchronized (verfruehteAnfragen) {
                    verfruehteAnfragen.wait();
                }
            } catch (InterruptedException fehler) {
                System.out.println("Bienenstock:infoAusschnittHolen: "
                        + "Konnte Anfrage nicht synchronisieren!");
            }
        }
        //(this.spielkarte!=null) &&
        if (spielkarte.aktionscodeGueltig(aktcode)) {
            return spielkarte.ausschnittErstellen(aktcode);
            
        } else {
            return null;
        }
    }
    
    /**
     * versucht einen Agenten starten zu lassen.
     *
     * Der Agent wird ?ber seinen Aktionscode identifiziert und
     * seine Anfrage wird synchronisiert - nach erfolgreicher
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur?ckgegeben.
     *
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode f?r den Agenten.
     *                      Ist die Anfrage missgl?ckt, so bekommt
     *                      er 0 zur?ck.
     */
    public long startenLassen(long aktCode) {
        
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            // AUSF?HRENDERAKTION
            erfolgreich = spielkarte.bieneStartenLassen(aktCode);
            aktionAusgefuehrt(aktCode);
            long newCode=spielkarte.aktionscodeSetzen(aktCode);
            return newCode;
        } else {
            return 0L;
        }
    }
    
    /**
     * versucht einen Agenten fliegen zu lassen.
     *
     * Der Agent wird ?ber seinen Aktionscode identifiziert und
     * seine Anfrage wird synchronisiert - nach erfolgreicher
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur?ckgegeben.
     *
     * @param aktCode       Der Aktionscode des Agenten
     * @param zielFeld      Das Feld zu dem der Agent fliegen m?chte
     * @return              Der neue Aktionscode f?r den Agenten.
     *                      Ist die Anfrage missgl?ckt, so bekommt
     *                      er 0 zur?ck.
     */
    public long fliegenLassen(long aktCode, Koordinate zielFeld) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            // AUSF?HRENDERAKTION
            erfolgreich = spielkarte.bieneFliegenLassen(aktCode, zielFeld);
            
            aktionAusgefuehrt(aktCode);
            long retValue=spielkarte.aktionscodeSetzen(aktCode);
            return retValue;
            
        } else {
            return 0L;
        }
    }
    
    /**
     * versucht einen Agenten landen zu lassen.
     *
     * Der Agent wird ?ber seinen Aktionscode identifiziert und
     * seine Anfrage wird synchronisiert - nach erfolgreicher
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur?ckgegeben.
     *
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode f?r den Agenten.
     *                      Ist die Anfrage missgl?ckt, so bekommt
     *                      er 0 zur?ck.
     */
    public long landenLassen(long aktCode) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            // AUSF?HRENDERAKTION
            erfolgreich = spielkarte.bieneLandenLassen(aktCode);
            
            aktionAusgefuehrt(aktCode);
            
            long retValue=spielkarte.aktionscodeSetzen(aktCode);
            return retValue;
            
        } else {
            return 0L;
        }
    }
    
    /**
     * versucht einen Agenten warten zu lassen.
     *
     * Der Agent wird ?ber seinen Aktionscode identifiziert und
     * seine Anfrage wird synchronisiert - nach erfolgreicher
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur?ckgegeben.
     *
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode f?r den Agenten.
     *                      Ist die Anfrage missgl?ckt, so bekommt
     *                      er 0 zur?ck.
     */
    public long wartenLassen(long aktCode) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            
            // AUSF?HRENDERAKTION
            erfolgreich = spielkarte.bieneWartenLassen(aktCode);
            
            aktionAusgefuehrt(aktCode);
            
            long retValue=spielkarte.aktionscodeSetzen(aktCode);
            return retValue;
            
            
        } else {
            return 0L;
        }
    }
    
    /**
     * versucht einen Agenten tanzen zu lassen.
     *
     * Der Agent wird ?ber seinen Aktionscode identifiziert und
     * seine Anfrage wird synchronisiert - nach erfolgreicher
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur?ckgegeben.
     *
     * @param aktCode       Der Aktionscode des Agenten
     * @param zielX         Die x-Koordinate der mitgeteilten Position
     * @param zielY         Die y-Koordinate der mitgeteilten Position
     * @param richtung      Teilt mit, ob die Richtung mitgeteilt werden soll
     * @param entfernung    Teilt mit, ob die Entfernung mitgeteilt werden soll
     * @return              Der neue Aktionscode f?r den Agenten.
     *                      Ist die Anfrage missgl?ckt, so bekommt
     *                      er 0 zur?ck.
     */
    public long tanzenLassen(long aktCode,
            int zielX,
            int zielY,
            boolean richtung,
            boolean entfernung) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            
            // AUSF?HRENDERAKTION
            erfolgreich = spielkarte.bieneTanzenLassen(aktCode,
                    zielX,
                    zielY,
                    richtung,
                    entfernung);
            
            aktionAusgefuehrt(aktCode);
            
            long retValue=spielkarte.aktionscodeSetzen(aktCode);
            return retValue;
            
        } else {
            return 0L;
        }
    }
    
    /**
     * versucht einen Agenten zuschauen zu lassen.
     *
     * Der Agent wird ?ber seinen Aktionscode identifiziert und
     * seine Anfrage wird synchronisiert - nach erfolgreicher
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur?ckgegeben.
     *
     * @param aktCode       Der Aktionscode des Agenten
     * @param idBienie      Die ID der Biene, der der Agent beim Tanzen
     *                      zuschauen m?chte
     * @return              Der neue Aktionscode f?r den Agenten.
     *                      Ist die Anfrage missgl?ckt, so bekommt
     *                      er 0 zur?ck.
     */
    public long zuschauenLassen(long aktCode, int idBienie) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            
            // AUSF?HRENDERAKTION
            erfolgreich = spielkarte.bieneZuschauenLassen(aktCode, idBienie);
            
            aktionAusgefuehrt(aktCode);
            
            return spielkarte.aktionscodeSetzen(aktCode);
            
        } else {
            return 0L;
        }
    }
    
    /**
     * versucht einen Agenten Honig tanken zu lassen.
     *
     * Der Agent wird ?ber seinen Aktionscode identifiziert und
     * seine Anfrage wird synchronisiert - nach erfolgreicher
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur?ckgegeben.
     *
     * @param aktCode       Der Aktionscode des Agenten
     * @param menge         Die gew?nschte Honigmenge
     * @return              Der neue Aktionscode f?r den Agenten.
     *                      Ist die Anfrage missgl?ckt, so bekommt
     *                      er 0 zur?ck.
     */
    public long honigTankenLassen(long aktCode, int menge) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            
            // AUSF?HRENDERAKTION
            erfolgreich = spielkarte.bieneHonigTankenLassen(aktCode, menge);
            
            aktionAusgefuehrt(aktCode);
            
            long retValue=spielkarte.aktionscodeSetzen(aktCode);
            return retValue;
            
        } else {
            return 0L;
        }
    }
    
    /**
     * versucht einen Agenten Nektar abliefern zu lassen.
     *
     * Der Agent wird ?ber seinen Aktionscode identifiziert und
     * seine Anfrage wird synchronisiert - nach erfolgreicher
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur?ckgegeben.
     *
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode f?r den Agenten.
     *                      Ist die Anfrage missgl?ckt, so bekommt
     *                      er 0 zur?ck.
     */
    public long nektarAbliefernLassen(long aktCode) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            
            // AUSF?HRENDERAKTION
            erfolgreich = spielkarte.bieneNektarAbliefernLassen(aktCode);
            
            aktionAusgefuehrt(aktCode);
            
            return spielkarte.aktionscodeSetzen(aktCode);
            
        } else {
            return 0L;
        }
    }
    
    /**
     * versucht einen Agenten Nektar abbauen zu lassen.
     *
     * Der Agent wird ?ber seinen Aktionscode identifiziert und
     * seine Anfrage wird synchronisiert - nach erfolgreicher
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur?ckgegeben.
     *
     * @param aktCode       Der Aktionscode des Agenten
     * @param menge         Die gew?nschte Nekarmenge
     * @return              Der neue Aktionscode f?r den Agenten.
     *                      Ist die Anfrage missgl?ckt, so bekommt
     *                      er 0 zur?ck.
     */
    public long nektarAbbauenLassen(long aktCode, int menge) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            
            // AUSF?HRENDERAKTION
            erfolgreich = spielkarte.bieneNektarAbbauenLassen(aktCode, menge);
            
            aktionAusgefuehrt(aktCode);
            
            return spielkarte.aktionscodeSetzen(aktCode);
        } else {
            return 0L;
        }
    }
    
    /**
     * entfernt einen Agenten aus dem Szenario.
     *
     * @param idBiene   ID des Agenten, der entfernt werden soll
     */
    public void removeAgent(int idBiene) {
        mAgentIDs.remove(new Integer(idBiene));
        anzahlAngemeldeterAgenten = anzahlAngemeldeterAgenten - 1;
    }
    
    /**
     * simuliert das komplette Spiel.
     *
     * Ob das Spiel rundenbasiert oder ohne Runden stattfindet, bleibt dem
     * Implementierer des konkreten Scenarios ?berlassen.
     *
     * @throws InvalidAgentException        Falscher Agententyp
     * @throws NotEnoughEnergyException     Ein Agent hat zu wenig Energie
     * @throws InvalidElementException      Falsches Element
     */
    public void simulateGame()
    throws InvalidAgentException,
            NotEnoughEnergyException,
            InvalidElementException {
        // Szenario an der Server-Vis-Komponente anmelden
        try {
            visAuthCode = m_visualisationServer.spielAnmelden(m_gameId, 2000);
        } catch (DoppeltesSpielException fehler) {
            System.out.println("Konnte Spiel f?r die Visualisierung nicht "
                                + "anmelden: Doppelte ID!");
        }
                
        startphase();
        
        //zum Test derVisualisierung
        //vis.start();
        //vis.visualisiere(spielkarte.visualisieren());
        
        while (!endbedingungTrifftZu()) { 
            // uebertragen der Spielinformationen an die Server-Vis-Komponente
            m_visualisationServer.speichereSpielInformationen(visAuthCode, 
                                                 spielkarte.visualisieren());
            wartephase();
            bearbeitungsphase();
            spielkarte.createRoundStatistik(this.statistik);
            
            //vis.visualisiere(spielkarte.visualisieren());
        }
        endphase();
        
        // Spiel von der Server-Vis-Komponente abmelden
        m_visualisationServer.spielAbmelden(visAuthCode);
        
    }
    
    /**
     * initialisiert die Liste der erlaubten Sprechakte.
     *
     * Jedes Szenario, dass den MessagingServer einsetzen will, muss die
     * Liste initialisieren.
     */
    public void initializeListOfAllowedSpeachactClasses() { }
    
    /**
     * sorgt f?r einen Reset aller Parameterwerte,
     * wenn ein Spiel erneut simuliert werden soll.
     */
    public void reset() {
        rundennummer = 0;
        anzahlAngemeldeterAgenten = 0;
        this.bienenStoecke.clear();
        //     this.m_parameter.clear();
        this.verfruehteAnfragen.clear();
        this.m_agentsEnergy.clear();
        this.m_agents.clear();
        this.mAgentIDs.clear();
        this.m_scenarioHandler=null;
        this.createNewScenarioHandler();
        
        this.bearbeitungsphaseMussWarten=false;
        this.spielkarte=null;
        this.wartendeAnfragen=null;
        this.wartendeAnfragen = new ObjektWartendeAnfragen();
        
        //  this.wartendeAnfragen.setzeNull();
        //    this.wartendeAnfragen.notify();
        //   startphase();
    }
    
    
    
    
   /* public void reset() {
                // Alte Identit?ten wegwerfen
                m_actionKeys.clear();
                // Alle Parameterwerte reseten
                m_activeAgents.clear();
                m_numOfFinishedAgents = 0;
                m_numOfActiveAgents = 0;
                m_agentsEnergy.clear();
                Enumeration agents = m_agents.elements();
                // Flags wieder zur?cksetzen.
                m_canGetCalculations = false;
                m_canStartNextRound = false;
                m_enviroment = new IslandEnviroment();
                try {
                        m_enviroment.createEnviroment(m_graphFile, m_attributeFile);
                } catch (Exception e) {
                        System.out.println("Fehler:");
                        e.printStackTrace(System.out);
                }
                // Visualisierungen reseten
                try {
                        m_visHandler.resetVisualisations(m_agents);
                } catch (Exception e) {
                        e.printStackTrace(System.out);
                }
                m_agents.clear();
        }*/
    
    /**
     * gibt die Parameter des Szenarios zur?ck
     *
     * @return      Die Liste der Parameter des Szenarios
     */
    public  Parameter gibParameter() {
        return parameter;
    }
    
    /**
     * erzeugt einen neuen ScenarioHandler.
     *
     * Das konkrete Scenario muss hier seinen ScenarioHandler erzeugen. Der
     * ScenarioHandler wird aus Sicherheitsgr?nden implementiert. Er soll den
     * Agenten einen indirekten Zugriff auf das Scenario erlauben, ohne das die
     * Agenten das Scenario jemals in die Hand bekommen.
     *
     * @return      der Szenariohandler des Szenarios
     */
    public AbstractScenarioHandler createNewScenarioHandler() {
        BienenstockSzenarioHandler handler=new BienenstockSzenarioHandler(this);
        if (this.szenariohandler==null) {
            this.szenariohandler=handler;
        }
        return handler;
    }
    
    /**
     * gibt die Nummer der aktuellen Runde zur?ck.
     *
     * @return      Die Nummer der aktuellen Runde
     */
    public int gibRundennummer() {
        return rundennummer;
    }
    
    /**
     * Liest die Scenario-Parameter aus der XML-Datei aus.
     */
    public LinkedList getScenarioParameter(GameConf gameConf){
        if (gameConf==null) return getScenarioParameter();
        SZENARIOCONFIGDATEINAME=gameConf.getParameterDateiName();
        ServerInfoObject sio=ServerInfoObject.m_instance;
        String pathSep=sio.getPathSeparator();
        String scenarioPath=sio.getScenarioPath();
        String scenarioConfigXMLpath;
        scenarioConfigXMLpath=scenarioPath + pathSep + SZENARIONAME + pathSep
                + SZENARIOCONFIGORDNER + pathSep + SZENARIOCONFIGDATEINAME;
        
        ScenarioXMLConfigHandler handler=new ScenarioXMLConfigHandler();
        ScenarioXMLInputReader sceanrioXMLInputReader=new ScenarioXMLInputReader(scenarioConfigXMLpath,handler);
        
        return handler.getParameterListe();
    }
    
    
    
    /**
     * gibt eine Liste mit den Parametern, die dass Szenario ben?tigt zur?ck.
     *
     * @return Liste der Parameter vorbelegt mit Defaultwerten
     */
    public LinkedList getScenarioParameter() {
        System.out.println("BienenstockScenario: werde gebeten die Parameter"
                + " zu uebergeben...");
        LinkedList parameterListe = new LinkedList();
        
        parameterListe.add(
                new ParameterDescription(
                "numExps",
                ParameterDescription.IntegerType,
                new Integer(1)));
        parameterListe.add(
                new ParameterDescription(
                "ScenarioName",
                ParameterDescription.StringType,
                new String("Bienenstock"),
                false));
        parameterListe.add(
                new ParameterDescription(
                "StartEnergy",
                ParameterDescription.DoubleType,
                new Double(150.0)));
        parameterListe.add(
                new ParameterDescription(
                "honigStarten",
                ParameterDescription.IntegerType,
                new Integer(2)));
        parameterListe.add(
                new ParameterDescription(
                "honigFliegen",
                ParameterDescription.IntegerType,
                new Integer(5)));
        parameterListe.add(
                new ParameterDescription(
                "honigLanden",
                ParameterDescription.IntegerType,
                new Integer(2)));
        parameterListe.add(
                new ParameterDescription(
                "honigWarten",
                ParameterDescription.IntegerType,
                new Integer(1)));
        parameterListe.add(
                new ParameterDescription(
                "honigTanzenAlles",
                ParameterDescription.IntegerType,
                new Integer(10)));
        parameterListe.add(
                new ParameterDescription(
                "honigTanzenRichtung",
                ParameterDescription.IntegerType,
                new Integer(5)));
        parameterListe.add(
                new ParameterDescription(
                "honigTanzenEntfernung",
                ParameterDescription.IntegerType,
                new Integer(5)));
        parameterListe.add(
                new ParameterDescription(
                "honigZuschauen",
                ParameterDescription.IntegerType,
                new Integer(5)));
        parameterListe.add(
                new ParameterDescription(
                "honigHonigTanken",
                ParameterDescription.IntegerType,
                new Integer(1)));
        parameterListe.add(
                new ParameterDescription(
                "honigNektarAbliefern",
                ParameterDescription.IntegerType,
                new Integer(1)));
        parameterListe.add(
                new ParameterDescription(
                "honigNektarAbbauen",
                ParameterDescription.IntegerType,
                new Integer(10)));
        
        //initialisierungswerte der Biene
        parameterListe.add(
                new ParameterDescription(
                "startHonig",
                ParameterDescription.IntegerType,
                new Integer(100)));
        parameterListe.add(
                new ParameterDescription(
                "startNektar",
                ParameterDescription.IntegerType,
                new Integer(100)));
        parameterListe.add(
                new ParameterDescription(
                "maxGelHonig",
                ParameterDescription.IntegerType,
                new Integer(100)));
        parameterListe.add(
                new ParameterDescription(
                "maxGelNektar",
                ParameterDescription.IntegerType,
                new Integer(100)));
        
        //Weitere Parameter
        parameterListe.add(
                new ParameterDescription(
                "timeout",
                ParameterDescription.IntegerType,
                new Integer(10000)));
        parameterListe.add(
                new ParameterDescription(
                "kursNektarHonig",
                ParameterDescription.FloatType,
                new Double(2.0)));
        parameterListe.add(
                new ParameterDescription(
                "maxNektarZuHonig",
                ParameterDescription.IntegerType,
                new Integer(50)));
        parameterListe.add(
                new ParameterDescription(
                "unschaerfeWinkel",
                ParameterDescription.FloatType,
                new Double(0.1)));
        parameterListe.add(
                new ParameterDescription(
                "unschaerfeEntfernung",
                ParameterDescription.FloatType,
                new Double(0.1)));
        parameterListe.add(
                new ParameterDescription(
                "endbedingungMaxNektar",
                ParameterDescription.IntegerType,
                new Integer(0)));
        parameterListe.add(
                new ParameterDescription(
                "endbedingungMaxHonig",
                ParameterDescription.IntegerType,
                new Integer(0)));
        parameterListe.add(
                new ParameterDescription(
                "endbedingungMaxRunden",
                ParameterDescription.IntegerType,
                new Integer(2000)));
        parameterListe.add(
                new ParameterDescription(
                "endbedingung",
                ParameterDescription.IntegerType,
                new Integer(2000)));
        
        return parameterListe;
    }
    
    /**
     * erstellt f?r alle ?bergebenen Agenten neue Statistikkomponenten
     *
     * @param agents     Liste der Agenten zu denen eine neue
     *                   Statistikkomponente erstellt werden soll
     * @return            die Statistikkomponente des Szenarios
     */
    public IStatisticComponent createNewStatisticComponent(Vector agentIds) {
        this.mAgentIDs.get(agentIds);
        int size=agentIds.size();
        /* die Agenten IDs innerhalb von Scenario beginnen mit 1 ...*/
        Vector scenarioAgentIDs=new Vector();
        for (int i=1; i<=size; i++) {
            scenarioAgentIDs.add(new Id(String.valueOf(i)));
        }
        //   statistik instanziieren
        this.statistik = new BienenStatistikKomponente(scenarioAgentIDs, m_parameter);
        return this.statistik;
    }
    
    
    
    /**
     * gibt eine Liste mit allen Ststistikparametern zur?ck.
     *
     * @return      Liste aller Statistikparameter
     */
    public Hashtable getEnviromentStatisticParameters() {
        Hashtable rueckgabe = new Hashtable();
        rueckgabe.put("Szenario", parameter.gibWert("ScenarioName"));
        rueckgabe.putAll(this.m_parameter);
        return rueckgabe;
    }
    
    /**
     * liefert zu einem Szenario-Parameter-Namen den zugeh?rigen 
     * Wert als String zur?ck.
     *
     * @throws InvalidElementException  parameterName ist in der Parameter-Liste 
     * nicht vorhanden.
     * @param parameterName der Name des Parameters
     */
    public String getScenarioParameter(String parameterName) 
    throws InvalidElementException{
        if (this.m_parameter.containsKey(parameterName)){
            Object obj=this.m_parameter.get(parameterName);
            return new String(obj.toString());
        }else throw new InvalidElementException();
    }
}
