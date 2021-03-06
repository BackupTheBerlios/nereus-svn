/*
 * Erzeugt        : 16. Oktober 2004
 * Letzte �nderung: 14. Februar 2005 durch Samuel Walz
 *
 * Teil des Softwarepraktikums mit dem Titel
 *
 *   "Design und Implementierung eines Szenarios f�r einen
 *    Multiagenten-Simulator"
 *
 * bei Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de)
 *
 * Autoren  : Philip Funck (mango.3@gmx.de)
 *            Samuel Walz (felix-kinkowski@gmx.net)
 *
 * copyright: Institut f�r Intelligente Systeme, Universit�t Stuttgart (2004)
 *            http://www.iis.uni-stuttgart.de
 */
package scenario.bienenstock;

import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import exceptions.InvalidAgentException;
import exceptions.NotEnoughEnergyException;
import exceptions.InvalidElementException;
import simulator.AbstractScenario;
import simulator.AbstractScenarioHandler;
import simulator.statistic.IStatisticComponent;
import simulator.statistic.IStatisticScenario;
import scenario.bienenstock.einfacheUmgebung.EinfacheKarte;
import scenario.bienenstock.umgebung.Karte;
import scenario.bienenstock.umgebung.Bienenstock;
import scenario.bienenstock.statistik.StatistikBiene;
import scenario.bienenstock.statistik.Statistikmodul;
import simulator.IInformationHandler;
import utils.Id;
import utils.ParameterDescription;


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
     * �bergeht. 
     */
    private int timeout;

    /**
     * ist eine Tabelle, die die Szenario-internen ID's in 
     * Simulator ID's �bersetzt. 
     */
    private Hashtable mAgentIDs = new Hashtable();

    /**
     * h�lt die Anfragen von Agenten, die zu fr�h anfragen.
     */
    private HashSet verfruehteAnfragen = new HashSet();

    /**
     * ist eine Liste aller auf der Karte befindlichen Bienenst�cke. 
     */
    private HashSet bienenStoecke;

    /**
     * enth�lt Dummy-objekte an denen jeweils ein Agent darauf wartet,
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
     * enth�lt die Parameter f�r das Szenario
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
     * @see statistik.Statistikmodul
     */
    private Statistikmodul statistik = null;
    
    /**
     * enth�lt alle Statistikagenten f�r die aktuelle Runde.
     */
    private Vector statAgentIds = null;

    /**
     * signalisiert der Bearbeitungsphase, ob die Anfrage des Agenten schon
     * abgearbeitet wurde, bevor sie anfangen will auf eben dieses Ereigniss
     * zu warten.
     */
    private boolean bearbeitungsphaseMussWarten = false;

    /**
     * Konstruktor.
     * 
     * @param gameId            ID des Spiels
     * @param visHandler        Der Visualisierungshandler f�r das Szenario
     * @param parameterTabelle  Die Parameter f�r das Spiel
     */
    public Scenario(Id gameId,
                                IInformationHandler visHandler,
                                Hashtable parameterTabelle) {
        super(gameId,
              visHandler,
              parameterTabelle);
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
     * setzt die Liste der �bergebenen Parameter und die Liste der 
     * Standardwerte zusammen.
     * 
     * Dabei werden haupts�chlich die �bergebenen Parameter verwendet,
     * fehlt jedoch ein Parameter in der �bergebenen Liste, oder ist ein 
     * Parameter der Standartparameter schreibgesch�tzt, so wird der
     * Parameter der Standardparameterliste verwendet.
     * 
     * @param parameterTabelle   Liste der vom Simulator �bergebenen Werte
     */
    private void setzeParameter(Hashtable parameterTabelle) {
        LinkedList defaults = getScenarioParameter();

        int i;
        for (i = 0; i < defaults.size(); i++) {
            utils.ParameterDescription aktuell 
                = (utils.ParameterDescription) defaults.get(i);
            if (aktuell.isChangeable() 
                    && parameterTabelle.contains(aktuell.getParameterName())) {
                parameter.setzeWert((aktuell.getParameterName()),
                    parameterTabelle.get(aktuell.getParameterName()));
            } else {
                parameter.setzeWert((aktuell.getParameterName()), 
                                               aktuell.getDefaultValue());
            }
            parameter.setzeSchreibschutz(aktuell.getParameterName());
        }
    }

    /**
     * pr�ft ob eine der Endbedingungen zutrifft.
     * 
     * Ist eine Bedingung auf null gesetzt, so wird sie nicht gepr�ft.
     * 
     * @return      Gibt einen Boolean-Wert zur�ck, der singnalisiert,
     *              ob eine der Endbedingungen zutrifft
     */
    private boolean endbedingungTrifftZu () {
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
     * Es wird davon ausgegangen, da� alle Agenten vorher schon 
     * am Simulator eingetragen sind. 
     */
    private void startphase() {

        System.out.println("*------.oOo.-------*");
        System.out.println("- Starte das Spiel -");
        System.out.println("*------------------*");

        phase = Konstanten.STARTPHASE;

        //Vorsicht: Hard gecodeter Dateiname
        spielkarte = new Karte("test2.gml", this);

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
            biene.aktionscodeSetzen(spielkarte.bieneEinfuegen(id, 
                                                             biene.gibVolksID(),
                                                              biene.getId()));
        }
        if (statistik != null) {
            statistik.neueWerteSetzen(
                    spielkarte.statistikBienenErstellen(statAgentIds));
        }
    }

    /**
     * ist die Phase, in der der SpielMeister auf Anfragen, 
     * die in dieser Runde bearbeitet werden sollen, wartet.
     * 
     * Nach einer gewissen Zeit, dem sogenannten <code>timeout</code>, 
     * oder wenn alle Agenten ihre Aktionsanfrage gestellt haben, 
     * geht der Spielmeister in die <code>bearbeitungsphase</code> �ber. 
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
        * Warten bis timeout in millisek. zum Ende f�hrt.
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
     * zuf�lligen Reihenfolge.
     * 
     * Abschlie�end wird gepr�ft, ob die Endbedingungen zutreffen, 
     * und gegebenenfalls in die <code>endpahse</code> �bergegangen.
     * Ansonsten geht der Spielmeister wieder in 
     * die <code>wartephase</code> �ber.
     * 
     * Aktionsanfragen,die zu diesem Zeitpunkt gestellt werden, werden, 
     * falls sie g�ltig sind, gehalten und in der n�chsten 
     * <code>bearbeitungsphase</code> bearbeitet. 
     */
    private void bearbeitungsphase() {
        int anzahlWartendeAnfragen = 0;

        // Flags setzen
        phase = Konstanten.BEARBEITUNGSPHASE;

        /*
        * Bearbeiten der Aktionsanfragen in zuf�lliger Reihenfolge.
        * Der Zufall entsteht durch die zuf�llige Wahl der Aktinscodes,
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
        if (statistik != null) {
            statistik.neueWerteSetzen(
                    spielkarte.statistikBienenErstellen(statAgentIds));
        }
    }

    /**
     * ist die Phase in der der Spielmeister der Statistik die letzten Werte 
     * mitteilt und alles abmeldet. 
     */
    private synchronized void endphase() {
        phase = Konstanten.ENDPHASE;
        if (statistik != null) {
            statistik.neueWerteSetzen(
                    spielkarte.statistikBienenErstellen(statAgentIds));
        }
        System.out.println("SPIEL BEENDET.");

    }

    /**
     * pr�ft einen aktionscode auf G�ltigkeit.
     * 
     * Es wird dabei �berpr�ft, ob der Aktionscode schon verwendet wurde, 
     * und ob er in der letzten Runde �berhaupt ausgegeben wurde.
     * Durch die �berpr�fung verliert der Aktionscode seine G�ltigkeit.
     * 
     * @param aktCode       Der Aktionscode eines Agenten
     * @return      Boolean-Wert, der angibt, ob der Aktionscode noch
     *              g�ltig war
     */
    private synchronized boolean aktionscodeVerwendbar (long aktCode) {
        return (spielkarte.aktionscodeEntwerten(aktCode));
    }

    /**
     * banachrichtig den Spielmeister, 
     * da� der Thread mit der Abarbeitung der Aktion fertig ist.
     * 
     * @param aktCode       Der Aktionscode eines Agenten
     */
    private void aktionAusgefuehrt (long aktCode) {
            synchronized (zeigerBearbeitungsphase) {
                bearbeitungsphaseMussWarten = false;
                zeigerBearbeitungsphase.notify();
            }
    }

    /**
     * dient zur Synchronisation der Anfragen der Agenten.
     * 
     * @param aktCode       Der Aktionscode eines Agenten
     * @return              gibt zur�ck, ob der Aktionscode verwendbar war
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

        //pr�fen, ob der aktionscode gueltig ist
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
     * Identifiziert den Agenten �ber seinen Aktionscode.
     * 
     * @param aktcode           der Aktionscode des Agenten.
     * @return EinfacheKarte    Der f�r den Agenten sichtbare Ausschnitt der
     *                          Spielkarte
     */
    public EinfacheKarte infoAusschnittHolen (long aktcode) {
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

        if (spielkarte.aktionscodeGueltig(aktcode)) {
            return spielkarte.ausschnittErstellen(aktcode);

        } else {
            return null;
        }
    }

    /**
     * versucht einen Agenten starten zu lassen.
     * 
     * Der Agent wird �ber seinen Aktionscode identifiziert und 
     * seine Anfrage wird synchronisiert - nach erfolgreicher 
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur�ckgegeben.
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode f�r den Agenten.
     *                      Ist die Anfrage missgl�ckt, so bekommt
     *                      er 0 zur�ck.
     */
    public long startenLassen (long aktCode) {

        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            // AUSF�HRENDERAKTION
            erfolgreich = spielkarte.bieneStartenLassen(aktCode);

            aktionAusgefuehrt(aktCode);

            return spielkarte.aktionscodeSetzen(aktCode);

        } else {
            return 0L;
        }
    }

    /**
     * versucht einen Agenten fliegen zu lassen.
     * 
     * Der Agent wird �ber seinen Aktionscode identifiziert und 
     * seine Anfrage wird synchronisiert - nach erfolgreicher 
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur�ckgegeben.
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @param zielFeld      Das Feld zu dem der Agent fliegen m�chte
     * @return              Der neue Aktionscode f�r den Agenten.
     *                      Ist die Anfrage missgl�ckt, so bekommt
     *                      er 0 zur�ck.
     */
    public long fliegenLassen (long aktCode, Koordinate zielFeld) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            // AUSF�HRENDERAKTION
            erfolgreich = spielkarte.bieneFliegenLassen(aktCode, zielFeld);

            aktionAusgefuehrt(aktCode);

            return spielkarte.aktionscodeSetzen(aktCode);

        } else {
            return 0L;
        }
    }

    /**
     * versucht einen Agenten landen zu lassen.
     * 
     * Der Agent wird �ber seinen Aktionscode identifiziert und 
     * seine Anfrage wird synchronisiert - nach erfolgreicher 
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur�ckgegeben.
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode f�r den Agenten.
     *                      Ist die Anfrage missgl�ckt, so bekommt
     *                      er 0 zur�ck.
     */
    public long landenLassen (long aktCode) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {
            // AUSF�HRENDERAKTION
            erfolgreich = spielkarte.bieneLandenLassen(aktCode);

            aktionAusgefuehrt(aktCode);


            return spielkarte.aktionscodeSetzen(aktCode);

        } else {
            return 0L;
        }
    }

    /**
     * versucht einen Agenten warten zu lassen.
     * 
     * Der Agent wird �ber seinen Aktionscode identifiziert und 
     * seine Anfrage wird synchronisiert - nach erfolgreicher 
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur�ckgegeben.
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode f�r den Agenten.
     *                      Ist die Anfrage missgl�ckt, so bekommt
     *                      er 0 zur�ck.
     */
    public long wartenLassen (long aktCode) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {

            // AUSF�HRENDERAKTION
            erfolgreich = spielkarte.bieneWartenLassen(aktCode);

            aktionAusgefuehrt(aktCode);

            return spielkarte.aktionscodeSetzen(aktCode);

        } else {
            return 0L;
        }
    }

    /**
     * versucht einen Agenten tanzen zu lassen.
     * 
     * Der Agent wird �ber seinen Aktionscode identifiziert und 
     * seine Anfrage wird synchronisiert - nach erfolgreicher 
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur�ckgegeben.
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @param zielX         Die x-Koordinate der mitgeteilten Position
     * @param zielY         Die y-Koordinate der mitgeteilten Position
     * @param richtung      Teilt mit, ob die Richtung mitgeteilt werden soll
     * @param entfernung    Teilt mit, ob die Entfernung mitgeteilt werden soll
     * @return              Der neue Aktionscode f�r den Agenten.
     *                      Ist die Anfrage missgl�ckt, so bekommt
     *                      er 0 zur�ck.
     */
    public long tanzenLassen (long aktCode,
                              int zielX,
                              int zielY,
                              boolean richtung,
                              boolean entfernung) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {

            // AUSF�HRENDERAKTION
            erfolgreich = spielkarte.bieneTanzenLassen(aktCode,
                                                  zielX,
                                                  zielY,
                                                  richtung,
                                                  entfernung);

            aktionAusgefuehrt(aktCode);

            return spielkarte.aktionscodeSetzen(aktCode);

        } else {
            return 0L;
        }
    }

    /**
     * versucht einen Agenten zuschauen zu lassen.
     * 
     * Der Agent wird �ber seinen Aktionscode identifiziert und 
     * seine Anfrage wird synchronisiert - nach erfolgreicher 
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur�ckgegeben.
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @param idBienie      Die ID der Biene, der der Agent beim Tanzen
     *                      zuschauen m�chte
     * @return              Der neue Aktionscode f�r den Agenten.
     *                      Ist die Anfrage missgl�ckt, so bekommt
     *                      er 0 zur�ck.
     */
    public long zuschauenLassen (long aktCode, int idBienie) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {

            // AUSF�HRENDERAKTION
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
     * Der Agent wird �ber seinen Aktionscode identifiziert und 
     * seine Anfrage wird synchronisiert - nach erfolgreicher 
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur�ckgegeben.
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @param menge         Die gew�nschte Honigmenge
     * @return              Der neue Aktionscode f�r den Agenten.
     *                      Ist die Anfrage missgl�ckt, so bekommt
     *                      er 0 zur�ck.
     */
    public long honigTankenLassen (long aktCode, int menge) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {

            // AUSF�HRENDERAKTION
            erfolgreich = spielkarte.bieneHonigTankenLassen(aktCode, menge);

            aktionAusgefuehrt(aktCode);

            return spielkarte.aktionscodeSetzen(aktCode);

        } else {
            return 0L;
        }
    }

    /**
     * versucht einen Agenten Nektar abliefern zu lassen.
     * 
     * Der Agent wird �ber seinen Aktionscode identifiziert und 
     * seine Anfrage wird synchronisiert - nach erfolgreicher 
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur�ckgegeben.
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @return              Der neue Aktionscode f�r den Agenten.
     *                      Ist die Anfrage missgl�ckt, so bekommt
     *                      er 0 zur�ck.
     */
    public long nektarAbliefernLassen (long aktCode) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {

            // AUSF�HRENDERAKTION
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
     * Der Agent wird �ber seinen Aktionscode identifiziert und 
     * seine Anfrage wird synchronisiert - nach erfolgreicher 
     * Abarbeitung der Anfrage wird der neue
     * Aktionscode an den Agenten zur�ckgegeben.
     * 
     * @param aktCode       Der Aktionscode des Agenten
     * @param menge         Die gew�nschte Nekarmenge
     * @return              Der neue Aktionscode f�r den Agenten.
     *                      Ist die Anfrage missgl�ckt, so bekommt
     *                      er 0 zur�ck.
     */
    public long nektarAbbauenLassen (long aktCode, int menge) {
        boolean erfolgreich;
        if (synchronisiereAktion(aktCode)) {

            // AUSF�HRENDERAKTION
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
     * Implementierer des konkreten Scenarios �berlassen.
     * 
     * @throws InvalidAgentException        Falscher Agententyp
     * @throws NotEnoughEnergyException     Ein Agent hat zu wenig Energie
     * @throws InvalidElementException      Falsches Element
     */
    public void simulateGame()
        throws InvalidAgentException,
                NotEnoughEnergyException,
                InvalidElementException {
        startphase();
        while (!endbedingungTrifftZu()) {
            wartephase();
            bearbeitungsphase();
        }
        endphase();

    }

    /**
     * initialisiert die Liste der erlaubten Sprechakte.
     * 
     * Jedes Szenario, dass den MessagingServer einsetzen will, muss die
     * Liste initialisieren.
     */
    public void initializeListOfAllowedSpeachactClasses() { }

    /**
     * sorgt f�r einen Reset aller Parameterwerte, 
     * wenn ein Spiel erneut simuliert werden soll.
     */
    public void reset() {
        startphase();
    }

    /**
     * gibt die Parameter des Szenarios zur�ck
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
     * ScenarioHandler wird aus Sicherheitsgr�nden implementiert. Er soll den 
     * Agenten einen indirekten Zugriff auf das Scenario erlauben, ohne das die 
     * Agenten das Scenario jemals in die Hand bekommen. 
     * 
     * @return      der Szenariohandler des Szenarios
     */
    public AbstractScenarioHandler createNewScenarioHandler() {
        return szenariohandler;
    }

    /**
     * gibt die Nummer der aktuellen Runde zur�ck.
     * 
     * @return      Die Nummer der aktuellen Runde
     */
    public int gibRundennummer() {
        return rundennummer;
    }

    /**
     * gibt eine Liste mit den Parametern, die dass Szenario ben�tigt zur�ck.
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

        return parameterListe;
    }

    /**
     * erstellt f�r alle �bergebenen Agenten neue Statistikkomponenten
     * 
     * @param agents     Liste der Agenten zu denen eine neue
     *                   Statistikkomponente erstellt werden soll
     * @return            die Statistikkomponente des Szenarios
     */
    public IStatisticComponent createNewStatisticComponent(Vector agents) {
        statAgentIds = agents;
        HashSet statBienenOhneWerte = new HashSet();
        //StatistikBienen instanziieren
        int i;
        for (i = 0; i < agents.size(); i++) {
            statBienenOhneWerte.add(new StatistikBiene((Id) agents.get(i)));
        }
//   statistik instanziieren
        statistik = new Statistikmodul(statBienenOhneWerte);
        return statistik;
    }

    /**
     * gibt eine Liste mit allen Ststistikparametern zur�ck.
     * 
     * @return      Liste aller Statistikparameter
     */
    public Hashtable getEnviromentStatisticParameters() {
        Hashtable rueckgabe = new Hashtable();
        rueckgabe.put("Szenario", parameter.gibWert("ScenarioName"));
        return rueckgabe;
    }
}
