/*
 * Dateiname      : EinfacherAgent.java
 * Erzeugt        : 20. Januar 2004
 * Letzte Änderung: 8. August 2005 durch Eugen Volk
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *                  Eugen Volk
 *
 *
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut für
 * Intelligente Systeme der Universität Stuttgart unter Betreuung von
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

package agents.bienenstock;
import nereus.simulatorinterfaces.AbstractScenarioHandler;
import scenarios.bienenstock.interfaces.AbstrakteBiene;
import scenarios.bienenstock.interfaces.IBienenstockSzenarioHandler;
import scenarios.bienenstock.einfacheUmgebung.*;
import nereus.utils.Id;
import nereus.utils.BooleanWrapper;

import java.util.Iterator;
import java.util.Random;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import scenarios.bienenstock.agenteninfo.Info;
import scenarios.bienenstock.agenteninfo.Koordinate;
import agents.bienenstock.utils.InfoBlumeEinfach;

/**
 * Einfacher reaktiver Agent
 *
 */
public class EinfacherAgent
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
    /** Daten fuer die Lokalisation und Umweltwahrnehmung */
    private EinfacheKarte karte;
    /** Abbild der Biene  */
    private EinfacheBiene selbst;
    /**
     * Szenario-Handler für die Vermittlung  der Information
     * zwischen dem  Agenten und dem SzenarioManager
     */
    private IBienenstockSzenarioHandler handler;
    /** position der Biene als Feld */
    private EinfachesFeld position;
    /**
     * Flag, der signalisiert ob die Biene ersten Aktionscode bekommen hat und damit im Spiel ist.
     */
    boolean erstenAktCodeBekommen = false;
    /** Id der Biene */
    private int id = 0;
     /** Koordinate des Bienenstocks.  */
    private Koordinate posBienenstock = new Koordinate(100, 100);
    /** Weg von der Blume zum Bienenstock */
    LinkedList weg = new LinkedList();
    /** bekannte blumen */
    HashMap blumen = new HashMap();
    //ein paar honigkosten
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
    /** Name des Agenten */
    private String name = "";
    /** gesammleter Nektar */
    private int gesammelterNektar = 0;
    /**
     * falls eine beabsichtigte Aktion tatsächlich ausgeführt wurde, wird erfolg.value den Wert true haben.
     */
    private BooleanWrapper erfolg=new BooleanWrapper(false);
    /** Information über die zu bearbeitende Blume     */
    private InfoBlumeEinfach zuBearbeitendeBlume;
    /** vom Bienenstock hinweg */
    private boolean hinweg = true;
    
    /**
     * Zahl zur initialisierung des Zufallsgenerators.
     */
    private long samen = System.currentTimeMillis();
    
    /**
     * Wird zur Wahl der initialen Flugrichtung benötigt.
     */
    Random zufallsGenerator = new Random();
    
    /** Einfacher Agent -- reaktiver Agent
     * @param bName Name des Agenten
     * @param bHandler Handler des Agenten
     */
    public EinfacherAgent(String bName, AbstractScenarioHandler bHandler) {
        super(bName, bHandler);
        name = bName;
        handler = (IBienenstockSzenarioHandler)bHandler;
        volksID = 1;
        zufallsGenerator.setSeed(samen);
    }
    
    /** Einfacher Agent -- reaktiver Agent */
    public EinfacherAgent() {
        super();
        volksID = 1;
        zufallsGenerator.setSeed(samen);
    }
    /**
     * Einfacher Agent -- reaktiver Agent
     * @param bId Id des Agenten
     * @param bName Namen des Agenten
     */
    public EinfacherAgent(Id bId,String bName) {
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
     * setzt den Handler, der für den Agenten zuständig ist.
     * @param bHandler Handler des Agenten.
     */
    public void setHandler(AbstractScenarioHandler bHandler) {
        handler = (IBienenstockSzenarioHandler)bHandler;
    }
    
    /**
     * setzt den Aktionscode für den Agenten
     * @param aktionsCode alter Aktionscode
     * @return neuer Aktionscode
     */
    public boolean aktionscodeSetzen(long aktionsCode) {
        aktCode = aktionsCode;
        this.start();
        erstenAktCodeBekommen = true;
        return true;
    }
    
    
    /**
     * Run-Methode, zur Ausführung des Agenten-programms als Thread.
     */
    public void run() {
        int i;
        ausschnittHolen();
        posBienenstock = selbst.gibPosition();
        
        while (true) {
            starten();
            System.out.println(id + ": suche eine neue Blume.");
            while (!sucheBlume()) {}
            landen();
            if (blumeHatNektar()) {
                InfoBlumeEinfach plume = new InfoBlumeEinfach(weg, true);
                blumen.put(weg.getLast(), plume);
                zuBearbeitendeBlume = plume;
                vielAbbauen();
            } else {
                blumen.put(weg.getLast(), new InfoBlumeEinfach(weg, false));
            }
            heimFliegen();
        }
    }
    /** Aktionen, die am Bienenstock ausgeführt werden */
    private void amBienenstock() {
        if (selbst.gibGeladeneHonigmenge() < (honigTanken + honigAbliefern)) {
            tanken(1000);
        }
        if (selbst.gibGeladeneNektarmenge() > 0) {
            abliefern();
        }
        tanken(10000);
    }
    
    /** aktione für Nektar ablifern */
    private void abliefernGehen() {
        System.out.println(id + ": kehre heim (nektar voll / honig leer)");
        visualisiereBiene(selbst);
        starten();
        hinweg = false;
        int i = 0;
        for (i = (weg.size() - 2); i >= 0; i--) {
            fliegen((Koordinate)weg.get(i));
        }
        fliegen(posBienenstock);
        landen();
        amBienenstock();
        starten();
        //  hinweg = true;
        int wegSize=weg.size();
        for (i = 0; i < wegSize; i++) {
            fliegen((Koordinate)weg.get(i));
        }
        landen();
    }
    /** Funktion für Heimfliegen */
    private void heimFliegen() {
        System.out.println(id + ": kehre Heim (blume leer)");
        starten();
        hinweg = false;
        if (weg.size() > 0) {
            weg.removeLast();
        }
        while (weg.size() > 0) {
            fliegeIntelligent();
        }
        fliegeIntelligent();
        landen();
        amBienenstock();
    }
    /**
     * Funktion für weiter fliegen
     * @param offset Offset
     * @return true, falls weiterfliegen OK ist.
     */
    private boolean weiterfliegen(int offset) {
        
        boolean weiter;
        if (!((honigFliegen == 0) | (honigLanden == 0) | (honigStarten == 0))) {
            weiter = (selbst.gibGeladeneHonigmenge() >
                    ((weg.size() + 3)*honigFliegen + honigStarten + honigLanden
                    + honigTanken + offset));
            //System.out.println(id + ":     weiterfliegen = " + weiter);
            return weiter;
        } else {
            return true;
        }
    }
    
    /**
     * baut Nektar ab, bis die nektartank voll ist.
     */
    private void vielAbbauen() {
        int alterNektar = 0;
        boolean nektarBekommen = true;
        while (zuBearbeitendeBlume.besitztNektar()) {
            while (nektarBekommen && weiterfliegen(honigAbbauen)) {
                alterNektar = selbst.gibGeladeneNektarmenge();
                abbauen(100000);
                nektarBekommen = !(alterNektar == selbst.gibGeladeneNektarmenge());
            }
            if ((!nektarBekommen) && (selbst.gibGeladeneNektarmenge() < 100)) {
                zuBearbeitendeBlume.hatKeinenNektarMehr();
                heimFliegen();
            } else {
                abliefernGehen();
            }
        }
    }
    
    /**
     * enthält die Blume noch Nektar?
     * @param blume Blume
     * @return true, falls Blume Nektar enthält.
     */
    private boolean blumeVoll(EinfachesFeld blume) {
        if (blumen.containsKey(blume.gibPosition())) {
            return ((InfoBlumeEinfach)blumen.get(blume.gibPosition())).besitztNektar();
        } else {
            return true;
        }
    }
    
    /**
     * sucht aus den sichtbaren Feldern eine Blume
     * @return true, falls Blume gefunden wurde
     */
    private boolean sucheBlume() {
        //visualisiereFelder(karte);
        boolean blumeGefunden = false;
        Koordinate posBlume = new Koordinate(100, 100);
        Iterator itNachbarn = position.gibNachbarfelder().values().iterator();
        while ((!blumeGefunden) && itNachbarn.hasNext()) {
            EinfachesFeld tmp = (EinfachesFeld)itNachbarn.next();
            if (tmp instanceof EinfacheBlume && blumeVoll(tmp)) {
                posBlume = tmp.gibPosition();
                blumeGefunden = true;
            }
        }
        if (blumeGefunden) {
            fliegen(posBlume);
            System.out.println(id + ": Habe eine Blume gefunden");
            visualisiereBiene(selbst);
            return true;
        } else {
            System.out.println(id + ": suche Blume");
            fliegeIntelligent();
            return false;
        }
    }
    
    /**
     * Hat die Blume noch Nektar?
     * @return true, falls die Blume noch Nektar enthält.
     */
    private boolean blumeHatNektar() {
        int nektarVorher = selbst.gibGeladeneNektarmenge();
        abbauen(10000000);
        ausschnittHolen();
        return (nektarVorher < selbst.gibGeladeneNektarmenge());
    }
    
    /**
     * fliegt intelligent.
     */
    private void fliegeIntelligent() {
        
        ausschnittHolen();
        if (position.gibPosition().equals(posBienenstock)) {
            hinweg = true;
            landen();
            amBienenstock();
            starten();
        }
        if (hinweg) {
            if (weiterfliegen(honigFliegen)) {
                fliegenZufaellig();
            } else {
                if (position.gibNachbarfelder().containsKey(posBienenstock)) {
                    System.out.println(id + ": waehle benachbarten Bienenstock");
                    fliegen(posBienenstock);
                    weg.clear();
                } else {
                    hinweg = false;
                    if (weg.isEmpty()) {
                        System.out.println(id + ": rueckweg fehlt - kein bienenstock in sicht");
                    } else {
                        System.out.println(id + ": kehre um");
                        fliegen((Koordinate)weg.getLast());
                        weg.removeLast();
                    }
                    
                }
            }
        } else {
            if (position.gibNachbarfelder().containsKey(posBienenstock)) {
                System.out.println(id + ": bienenstock gefunden");
                fliegen(posBienenstock);
                weg.clear();
            } else {
                if (weg.isEmpty()) {
                    System.out.println(id + ": Weg endet im nichts (nicht am bienenstock)");
                } else {
                    System.out.println(id + ": auf dem rueckweg");
                    fliegen((Koordinate)weg.getLast());
                    weg.removeLast();
                }
            }
        }
    }
    
    
    /** fliegt zufälllig */
    public void fliegenZufaellig() {
        double zufall;
        //zufall=zufallsGenerator.nextDouble();
        zufall = Math.random();
        boolean nichtGeflogen = true;
        Koordinate ziel = new Koordinate(100, 100);
        HashMap direkteNachbarn = position.gibNachbarfelder();
        int zielnummer = (int)((zufall * (direkteNachbarn.size() - 1)) + 1.0);
        int i = 0;
        boolean alternativesFeldNotwendig = true;
        
        if (!(position == null)) {
            Iterator nachbarn = direkteNachbarn.values().iterator();
            
            while (nichtGeflogen && nachbarn.hasNext()) {
                ziel = ((EinfachesFeld)nachbarn.next()).gibPosition();
                
                i = i + 1;
                if (i == zielnummer) {
                    while (weg.contains(ziel) && alternativesFeldNotwendig) {
                        if (nachbarn.hasNext()) {
                            ziel = ((EinfachesFeld)nachbarn.next()).gibPosition();
                        } else {
                            nachbarn = direkteNachbarn.values().iterator();
                            ziel = ((EinfachesFeld)nachbarn.next()).gibPosition();
                            alternativesFeldNotwendig = false;
                        }
                    }
                    
                    //System.out.println("weg.contains(ziel) = " + weg.contains(ziel));
                    fliegen(ziel);
                    nichtGeflogen = false;
                    
                }
            }
            if (nichtGeflogen) {
                System.out.println(id + ": Konnte mich nicht fuer ein Feld entscheiden.");
            }
            //visualisiereBiene(selbst);
            //visualisiereFelder(karte);
        } else {
            System.out.println(id + ": Ausschnitt ist leer!");
        }
    }
    
    /**
     * ausführen der atomaren Aktion fliegen.
     * @param x X-Koordinate
     * @param y Y-Koordinate
     */
    public void fliegen(int x, int y) {
        Koordinate ziel = new Koordinate(x, y);
        int honigAlt = selbst.gibGeladeneHonigmenge();
        System.out.println(id + ": fliege nach (" + ziel.gibXPosition() + ", " + ziel.gibYPosition() + " )");
        long neuerAktCode = handler.aktionFliegen(aktCode, erfolg, ziel);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
            if (hinweg) {
                weg.add(ziel);
            }
        }
        ausschnittHolen();
        if ((honigFliegen == 0) && !(neuerAktCode == 0L)) {
            honigFliegen = honigAlt - selbst.gibGeladeneHonigmenge();
        }
        //visualisiereBiene(selbst);
    }
    
    /**
     * ausführen der atomaren Aktion fliegen.
     * @param ziel Koordinate
     *
     */
    public void fliegen(Koordinate ziel) {
        //  try{
        //visualisiereBiene(selbst);
        int honigAlt = selbst.gibGeladeneHonigmenge();
        System.out.println(id + ": fliege nach (" + ziel.gibXPosition() + ", " + ziel.gibYPosition() + " )");
        long neuerAktCode = handler.aktionFliegen(aktCode, erfolg, ziel);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
            if (hinweg) {
                weg.add(ziel);
            }
        }
        ausschnittHolen();
        if ((honigFliegen == 0) && !(neuerAktCode == 0L)) {
            honigFliegen = honigAlt - selbst.gibGeladeneHonigmenge();
        }
    }
    
    /**
     * ausführen der atomaren Aktion abbauen.
     * @param menge gewünschte Nektarmenge
     */
    public void abbauen(int menge) {
        int alterHonig = selbst.gibGeladeneHonigmenge();
        System.out.println(id + ": baue ab (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        long neuerAktCode = handler.aktionNektarAbbauen(aktCode, erfolg, menge);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        ausschnittHolen();
        if ((honigAbbauen == 0) && (!(neuerAktCode == 0L))) {
            honigAbbauen = alterHonig - selbst.gibGeladeneHonigmenge();
        }
        //visualisiereBiene(selbst);
    }
    
    /**
     * ausführen der atomaren Aktion starten.
     */
    public void starten() {
        System.out.println(id + ": starte (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        int honigAlt = selbst.gibGeladeneHonigmenge();
        long neuerAktCode = handler.aktionStarten(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        } else {
            System.out.println(id + ": konnte nicht starten");
        }
        ausschnittHolen();
        if (honigStarten == 0 && (!(neuerAktCode == 0L))) {
            honigStarten = honigAlt - selbst.gibGeladeneHonigmenge();
        }
        //visualisiereBiene(selbst);
    }
    
    /**
     * ausführen der atomaren Aktion landen.
     */
    public void landen() {
        System.out.println(id + ": lande (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        int honigAlt = selbst.gibGeladeneHonigmenge();
        long neuerAktCode = handler.aktionLanden(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        ausschnittHolen();
        if (honigLanden == 0 && (!(neuerAktCode == 0L))) {
            honigLanden = honigAlt - selbst.gibGeladeneHonigmenge();
        }
        
        //visualisiereBiene(selbst);
    }
    
    /**
     * ausführen der Atomaren Aktion landen.
     */
    public void warten() {
        System.out.println(id + ": daeumchendrehen... (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        long neuerAktCode = handler.aktionWarten(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        } else {
            System.out.println(id + ": konnte nicht warten");
        }
        ausschnittHolen();
        //visualisiereBiene(selbst);
    }
    
    /**
     * ausführen der atomaren Aktion tanken
     * @param menge zu tankende Menge
     */
    public void tanken(int menge) {
        int honigAlt = selbst.gibGeladeneHonigmenge();
        System.out.println(id + ": tanke (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        long neuerAktCode = handler.aktionHonigTanken(aktCode, erfolg, menge);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        } else {
            System.out.println(id + ": konnte nicht tanken");
        }
        ausschnittHolen();
        if ((honigTanken == 0) && (!(neuerAktCode == 0L))) {
            honigTanken = honigAlt - selbst.gibGeladeneHonigmenge();
        }
        //visualisiereBiene(selbst);
    }
    
    /**
     * ausführen der atomaren Aktion abliefern
     */
    public void abliefern() {
        System.out.println(id + ": liefere Nektar ab (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        int honigAlt = selbst.gibGeladeneHonigmenge();
        int nektarAlt = selbst.gibGeladeneNektarmenge();
        long neuerAktCode = handler.aktionNektarAbliefern(aktCode, erfolg);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        } else {
            System.out.println(id + ": konnte kein Nektar abliefern");
        }
        ausschnittHolen();
        if ((honigAbliefern == 0) && (!(neuerAktCode == 0L))) {
            honigAbliefern = honigAlt - selbst.gibGeladeneHonigmenge();
        }
        if (selbst.gibGeladeneNektarmenge() == 0) {
            gesammelterNektar = gesammelterNektar + nektarAlt;
            System.out.println(id + ": hat bisher " + gesammelterNektar + " Nektar abgeliefert.");
        }
        //visualisiereBiene(selbst);
    }
    
    /** nimmt die Umwelt wahr */
    public void ausschnittHolen() {
        
        EinfacheKarte map;
        if (handler != null) {
            map = handler.infoAusschnittHolen(aktCode);
            if (map != null) {
                karte = map;
                selbst = karte.gibSelbst();
                if (id == 0) {
                    id = selbst.gibBienenID();
                }
                position = (EinfachesFeld)karte.gibFelder().get(selbst.gibPosition());
                if (!(selbst.gibAktionsCode() == aktCode)) {
                    aktCode = selbst.gibAktionsCode();
                }
            } else {
                System.out.println(id + ": ich darf leider nicht weiterspielen :(");
                synchronized (selbst) {
                    try {
                        selbst.wait();
                    } catch (Exception e) {}
                }
            }
        } else {
            System.out.println("ich habe den Handler nicht in der Hand!!!");
        }
        
    }
    
    /** tanzt, um Informationen zu übermitteln */
    public void tanzen() {
        System.out.println(id + ": tanze (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        long neuerAktCode = handler.aktionTanzen(aktCode, erfolg, 2, 1, true, true);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        ausschnittHolen();
        //visualisiereBiene(selbst);
    }
    
    /** schaut den tanzenden Bienen zu */
    public void zuschauen() {
        //visualisiereFelder(karte);
        ausschnittHolen();
        System.out.println(id + ": schaue zu (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        if (!(position.gibIDsTanzendeBienen().isEmpty())) {
            int zielBiene = ((Integer)position.gibIDsTanzendeBienen().iterator().next()).intValue();
            long nAktCode = handler.aktionZuschauen(aktCode, erfolg, zielBiene);
            if (!( nAktCode == 0L)) {
                aktCode = nAktCode;
            }
            System.out.println(id +" habe Folgende INFORMATION : " );
            ausschnittHolen();
            visualisiereBiene(selbst);
            System.out.println(id + ": visualisation abgeschlossen");
            //   System.out.println(" INFO :" + "richtung " + selbst.gibInformation().gibRichtung());
        } else {
            System.out.println(id + "keine tanzenden Bienen anwesend!!!");
            long nAktCode = handler.aktionZuschauen(aktCode, erfolg, 111);
            if (!( nAktCode == 0L)) {
                aktCode = nAktCode;
            }
        }
    }
    
    /** wählt eine Aktion */
    private void aktionWaehlen() {
        if (!(position == null)) {
            if (position.gibIDsTanzendeBienen().size() > 0) {
                zuschauen();
            } else if (position instanceof EinfacherBienenstock) {
                if (selbst.gibGeladeneNektarmenge() > 0) {
                    abliefern();
                }
                if (selbst.gibGeladeneHonigmenge() < 50) {
                    tanken(1000);
                }
            } else if (position instanceof EinfacheBlume) {
                abbauen(1000);
            } else {
                tanzen();
                tanzen();
                warten();
            }
        }
    }
    
    /**
     * gibt den Status über die Biene aus
     * @param ich Abbild der Biene
     */
    private void visualisiereBiene(EinfacheBiene ich) {
        Koordinate pos = (Koordinate)ich.gibPosition();
        
        System.out.print("\n"
                + id + ": Die Biene selbst\n"
                
                + id + ": Rundennummer:  " + ich.gibRundennummer() + "\n"
                
                + id + ": Zustand:  " + ich.gibZustand() + "\n"
                
        /*System.out.print("    BienenID:  ");
        System.out.print(ich.gibBienenID());
        System.out.print("\n");*/
                
/*        System.out.print("    BienenvolkID:  ");
        System.out.print(ich.gibBienenvolkID());
        System.out.print("\n");*/
                
                + id + ": Position:  " + pos.gibXPosition() + " , "
                + pos.gibYPosition() + "\n"
                
                
                //System.out.println(id + ": Aktionscode:  " + ich.gibAktionsCode());
                
                //System.out.println("    gespeichert:  " + aktCode);
                
                + id + ": geladene Nektarmenge:  "
                + ich.gibGeladeneNektarmenge() + "\n"
                
                + id + ": geladene Honigmenge:  "
                + ich.gibGeladeneHonigmenge() + "\n");
        System.out.println( id + ": abgeliferter NEKTAR: " + gesammelterNektar);
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
     * gibt den Status der Felder aus
     * @param zuVis einfache Karte
     */
    private void visualisiereFelder(EinfacheKarte zuVis) {
        Koordinate pos;
        System.out.println(id + ": Die Felder\n");
        
        if (!(zuVis == null)) {
            Iterator felder = zuVis.gibFelder().values().iterator();
            while (felder.hasNext()) {
                Object tmp = felder.next();
                EinfachesFeld feld = (EinfachesFeld)tmp;
                
                if (tmp instanceof EinfacherBienenstock) {
                    EinfacherBienenstock stock = (EinfacherBienenstock)tmp;
                    System.out.println("Bienenstock mit volksID: " + ((EinfacherBienenstock)stock).gibVolksID());
                } else if (tmp instanceof EinfacheBlume) {
                    EinfacheBlume plume = (EinfacheBlume)tmp;
                    System.out.println("Blume mit Merkmal: " + ((EinfacheBlume)plume).gibMerkmal());
                    HashSet aBienen = (HashSet)((EinfacheBlume)plume).gibIDsAbbauendeBienen();
                    Iterator itABienen = aBienen.iterator() ;
                    System.out.print("IDsAbbauendeBienen: ");
                    while (itABienen.hasNext()) {
                        System.out.print(itABienen.next() + ", ");
                    }
                    System.out.print("\n");
                } else {
                    System.out.println("konventioneller Platz");
                }
                
                pos = feld.gibPosition();
                /*if (zuVis.gibSelbst().gibPosition().equals(pos)) {
                    System.out.println("Auf diesem Feld befindet sich Biene " + zuVis.gibSelbst().gibBienenID() + " selbst.");
                }*/
                
                System.out.println("Position:  (" + pos.gibXPosition() + ", " + pos.gibYPosition() + ")");
                
                if (feld.gibNachbarfelder() == null) {
                    System.out.println("hat KEINE Nachbarfelder!!!");
                } else {
                    System.out.print("Nachbarfelder: [");
                    Iterator nachbarn = feld.gibNachbarfelder().values().iterator();
                    while (nachbarn.hasNext()) {
                        pos = ((EinfachesFeld)nachbarn.next()).gibPosition();
                        System.out.print("(");
                        System.out.print(pos.gibXPosition());
                        System.out.print(", ");
                        System.out.print(pos.gibYPosition());
                        System.out.print("), ");
                    }
                    System.out.print("] \n");
                }
                
                System.out.print("ID's wartende Bienen: ");
                Iterator wBienen = feld.gibIDsWartendeBienen().iterator();
                while (wBienen.hasNext()) {
                    System.out.print(wBienen.next());
                    System.out.print(", ");
                }
                System.out.print("\n");
                
                System.out.print("ID's fliegende Bienen: ");
                Iterator fliBienen = feld.gibIDsFliegendeBienen().iterator();
                while (fliBienen.hasNext()) {
                    System.out.print(fliBienen.next());
                    System.out.print(", ");
                }
                System.out.print("\n");
                
                System.out.print("ID's tanzende Bienen: ");
                Iterator tanBienen = feld.gibIDsTanzendeBienen().iterator();
                while (tanBienen.hasNext()) {
                    System.out.print(tanBienen.next());
                    System.out.print(", ");
                }
                System.out.print("\n");
                System.out.print("\n");
            }
        } else {
            System.out.println(id + ": (TestAgentLeben.visFelder) zuVis == null!!!");
        }
    }
    
    /**
     * VolksId der Biene
     * @return Id des Bienevolkes
     */
    public int gibVolksID() {
        return volksID;
    }
    
    
    
}