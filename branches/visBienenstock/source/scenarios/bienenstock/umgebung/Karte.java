/*
 * Dateiname      : Karte.java
 * Erzeugt        : 26. Juli 2004
 * Letzte Änderung: 16. Juni 2005 durch Dietmar Lippold
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (samuel@gmx.info)
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


package scenarios.bienenstock.umgebung;

import java.util.Hashtable;
import java.util.HashSet;
import java.util.Random;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import nereus.utils.Id;
import nereus.simulatorinterfaces.AbstractEnviroment;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.FullEnviromentException;
import nereus.exceptions.InvalidElementException;
import scenarios.bienenstock.scenariomanagement.Konstanten;
import scenarios.bienenstock.agenteninfo.Koordinate;
import scenarios.bienenstock.statistik.StatistikBienenAgent;
import nereus.utils.Parameter;
import scenarios.bienenstock.Scenario;
import scenarios.bienenstock.agenteninfo.Info;
//import scenarios.bienenstock.einfacheUmgebung.;
import scenarios.bienenstock.einfacheUmgebung.EinfacherPlatz;
import scenarios.bienenstock.einfacheUmgebung.EinfacheBlume;
import scenarios.bienenstock.einfacheUmgebung.EinfacherBienenstock;
import scenarios.bienenstock.einfacheUmgebung.EinfacheBiene;
import scenarios.bienenstock.einfacheUmgebung.EinfacheKarte;
import scenarios.bienenstock.einfacheUmgebung.EinfachesFeld;
import scenarios.bienenstock.visualisierungsUmgebung.VisKarte;
import scenarios.bienenstock.visualisierungsUmgebung.VisPlatz;
import scenarios.bienenstock.visualisierungsUmgebung.VisBiene;
import scenarios.bienenstock.visualisierungsUmgebung.VisFeld;
import scenarios.bienenstock.visualisierungsUmgebung.VisBienenstock;
import scenarios.bienenstock.visualisierungsUmgebung.VisBlume;
import nereus.simulatorinterfaces.statistic.IStatisticComponent;
import scenarios.bienenstock.statistik.BienenStatistikKomponente;

/**
 * Die Spielkarte des Szanarios.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Karte extends AbstractEnviroment {
    
    /**
     * speichert die in einer Runde noch ausstehenden Aktionen
     */
    private Hashtable ausstehendeAktionen;
    
    /**
     * Alle angemeldeten Bienen sortiert nach ihrem Aktionscode.
     * @associates scenario.bienenstock.umgebung.Biene
     */
    private Hashtable bienenNachAC; // = new HashMap();
    
    /**
     * Alle angemeldeten Bienen sortiert nach ihrer ID.
     */
    private Hashtable bienenNachID; // = new HashMap();
    
    /**
     * Alle Aktionscodes, die in der aktuellen Spielrunde
     * schon verwendet wurden.
     */
    private HashSet verwendeteAktionscodes = new HashSet();
    
    /**
     * Die gültigen Aktionscodes für die aktuelle Runde.
     */
    private HashSet gueltigeAktionscodes = new HashSet();
    
    /**
     * Alle Bienenstöcke die sich auf dem Spielfeld befinden.
     */
    private HashSet bienenstoecke = new HashSet();
    
    /**
     * Zahl zur initialisierung des Zufallsgenerators.
     */
    private final long samen = System.currentTimeMillis();
    
    /**
     * Wird zur erzeugung der Aktionscodes benötigt.
     */
    private Random zufallsGenerator = new Random();
    
    /**
     * Die Parameter für das aktuelle Spiel.
     */
    private Parameter parameter;
    
    /**
     * Der Spielmeister des aktuellen Spiels.
     */
    private Scenario spielmeister;
    
    
    /**
     * Alle Felder die die aktuelle Spielkarte ausmachen.
     * @associates umgebung.Feld
     */
    private Hashtable spielfeld;
    
    /** enthält (bienennId, rundenNr) in der getanzt wurde */
    private Hashtable getanztInRundeNr=new Hashtable();
    
    /**
     * Tanz-Tabelle, die zu jeder getanzten Biene in der jeweiligen Runde
     * die TanzInfomation speichert.
     * tanzTab[0] enthält Infomationen aus den geraden Runden,
     * tanzTab[1] enthält Infomationen aus den ungeraden Runden.
     */
    HashMap tanzTab[]={new HashMap(), new HashMap()};
    
    
    /**
     * Erstellt eine neue Instanz der Karte, bekommt mitgeteilt,
     * wo sich die gewünschte GML-Datei befindet und
     * welche Instanz des Spielmeisters die aufrufende ist.
     * @param gmlDateiname derName der GML-Datei
     * @param meister   ein Verweis auf den Spielmeister
     */
    public Karte(String gmlDateiname, Scenario meister) {
        
        spielmeister = meister;
        GMLParser kartenZeichner = new GMLParser(gmlDateiname);
        spielfeld = kartenZeichner.gibSpielfeld();
        parameter = spielmeister.gibParameter();
        
        bienenstoecke = bienenstoeckeSuchen();
        
        bienenNachAC = new Hashtable();
        bienenNachID = new Hashtable();
        ausstehendeAktionen = new Hashtable();
        
        zufallsGenerator.setSeed(samen);
        
    }
    
    
    /**
     * Nimmt die mitgeteilte Position eines Feldes und verfälscht diese.
     * @return Info
     * @param pos die position der tanzenden Biene
     * @param infoX die x koord. es ziels
     * @param infoY die y koord. des Ziels
     * @param richtungMitteilen ob die richtung mitgeteilt werden soll
     * @param entfernungMitteilen ob die Entfernung mitgeteilt werden soll
     * @param nutzen Nutzen der Blume für Agenten
     */
    private Info konvertiereInfo(Koordinate pos,
            int infoX,
            int infoY,
            boolean richtungMitteilen,
            boolean entfernungMitteilen,
            double nutzen) {
        
        double relativX = (double) infoX -(double) pos.gibXPosition();
        double relativY = (double) infoY -(double) pos.gibYPosition();
        double entfernung;
        double richtung;
        
        double zufallsZahl = ((zufallsGenerator.nextDouble() * 2.00) - 1.00);
        entfernung = Math.sqrt((relativX * relativX) + (relativY * relativY));
        entfernung = entfernung
                + (entfernung
                * zufallsZahl
                * ((Double) parameter.gibWert(
                "unschaerfeEntfernung")).doubleValue());
        
        zufallsZahl = ((zufallsGenerator.nextDouble() * 2.00) - 1.00);
        richtung = Math.atan2(relativY,relativX);
        richtung = richtung
                + (richtung
                * zufallsZahl
                * ((Double) parameter.gibWert(
                "unschaerfeWinkel")).doubleValue());
        
        if (richtungMitteilen && entfernungMitteilen) {
            return new scenarios.bienenstock.agenteninfo.Info(richtung,
                    entfernung,
                    true,
                    true, nutzen);
        } else if (richtungMitteilen) {
            return new scenarios.bienenstock.agenteninfo.Info(richtung, 0, true, false, nutzen);
        } else if (entfernungMitteilen) {
            return new scenarios.bienenstock.agenteninfo.Info(0, entfernung, false, true, nutzen);
        } else {
            return new scenarios.bienenstock.agenteninfo.Info(0, 0, false, false, -1);
        }
    }
    
    /**
     * Extrahiert die IDs der Bienen in einem HashSet und gibt diese zurück.
     *
     * @return ein Hashset mit einfachen Bienen
     * @param vollstHash die vollständigen Bienen
     */
    private HashSet konvertiereHash(HashSet vollstHash) {
        HashSet idHash = new HashSet();
        Iterator laeufer = vollstHash.iterator();
        while (laeufer.hasNext()) {
            idHash.add(new Integer(((Biene) laeufer.next()).gibBienenID()));
        }
        return idHash;
    }
    
    /**
     * Wandelt einenPlatz in einen einfachen Platz um.
     *
     * @return EinfachesFeld
     * @param vollstFeld das komplette Feld
     */
    private EinfacherPlatz konvertierePlatz(Platz vollstFeld) {
        return new EinfacherPlatz(
                new Koordinate(vollstFeld.gibPosition().gibXPosition(),
                vollstFeld.gibPosition().gibYPosition()),
                konvertiereHash(vollstFeld.gibWartendeBienen()),
                konvertiereHash(vollstFeld.gibFliegendeBienen()),
                konvertiereHash(vollstFeld.gibTanzendeBienen())
                );
    }
    
    /**
     * Wandelt eine Blume in eine einfache Blume um.
     *
     * @return EinfacheBlume
     * @param vollstFeld die vollständige Blume
     */
    private EinfacheBlume konvertiereBlume(Blume vollstFeld) {
        return new EinfacheBlume(
                new Koordinate(vollstFeld.gibPosition().gibXPosition(),
                vollstFeld.gibPosition().gibYPosition()),
                konvertiereHash(vollstFeld.gibWartendeBienen()),
                konvertiereHash(vollstFeld.gibFliegendeBienen()),
                konvertiereHash(vollstFeld.gibTanzendeBienen()),
                vollstFeld.gibMerkmal(),
                konvertiereHash(vollstFeld.gibAbbauendeBienen())
                );
    }
    
    /**
     * Wandelt einen Bienenstock in einen einfachen Bienenstock um.
     * @return EinfacherBienenstock
     * @param vollstFeld der vollständige Blumenstock
     */
    private EinfacherBienenstock konvertiereBienenstock
            (Bienenstock vollstFeld) {
        return new EinfacherBienenstock(
                new Koordinate(vollstFeld.gibPosition().gibXPosition(),
                vollstFeld.gibPosition().gibYPosition()),
                konvertiereHash(vollstFeld.gibWartendeBienen()),
                konvertiereHash(vollstFeld.gibFliegendeBienen()),
                konvertiereHash(vollstFeld.gibTanzendeBienen()),
                vollstFeld.gibVolksID()
                );
    }
    
    /**
     * Sucht auf der Karte alle Felder zusammen,
     * die von einem bestimmten Ausgangsfeld aus zu sehen sind.
     *
     * @param tiefe die sichtweite ist die Rekursionstiefe
     * @param ursprung das Feld von dem aus geschaut wird
     * @param alleSichtbarenFelder rekursiver Übergabeparameter
     */
    private void rekursivNachbarfelderEintragen(int tiefe,
            Feld ursprung,
            Hashtable alleSichtbarenFelder
            ) {
        Iterator nachbarn = ursprung.gibNachbarfelder().iterator();
        Feld nachbar;
        
        //wenn es nicht enthalten ist, dann eintragen
        if (!alleSichtbarenFelder.contains(ursprung)) {
            alleSichtbarenFelder.put(
                    ursprung.gibPosition(),
                    ursprung);
            //wenn die Rekursionstiefe größer 0 ist rekursiv weitermachen
            if (tiefe > 0) {
                while (nachbarn.hasNext()) {
                    rekursivNachbarfelderEintragen(
                            tiefe - 1,
                            (Feld) nachbarn.next(),
                            alleSichtbarenFelder);
                }
            }
        }
    }
    
    /**
     * Gibt eine Liste von Bienen für die Visualisierung zurück.
     *
     * @param originalSet das zu konvertierende Hash
     * @return Hashtable mit einfachen Bienen
     */
    private HashMap konvertiereBienenHash(Hashtable originalSet) {
        Iterator original = originalSet.values().iterator();
        HashMap neuesTable = new HashMap();
        
        while (original.hasNext()) {
            Biene originalBiene = (Biene) original.next();
            
            VisBiene visSelbst;
            
            int listenKenn=originalBiene.gibListenKennung();
            
            if ((listenKenn == Konstanten.ZUSCHAUEND)
            || (listenKenn== Konstanten.TANZEND)){
                
                Info info;
                
                if (listenKenn== Konstanten.ZUSCHAUEND) {
                    info=originalBiene.gibInformation().klonen();
                } else info = originalBiene.gibGesendeteInformation().klonen();
                
                visSelbst = new VisBiene(
                        konvertiereZustand(originalBiene.gibListenKennung()),
                        originalBiene.gibBienenID(),
                        originalBiene.gibBienenvolkID(),
                        new Koordinate(
                        originalBiene.gibPosition().gibXPosition(),
                        originalBiene.gibPosition().gibYPosition()),
                        originalBiene.gibGeladeneHonigmenge(),
                        originalBiene.gibGeladeneNektarmenge(),
                        ((Integer) parameter.gibWert("maxGelHonig")).intValue(),
                        ((Integer) parameter.gibWert("maxGelNektar")).intValue(),
                        info);
            } else {
                visSelbst = new VisBiene(
                        konvertiereZustand(originalBiene.gibListenKennung()),
                        originalBiene.gibBienenID(),
                        originalBiene.gibBienenvolkID(),
                        new Koordinate(
                        originalBiene.gibPosition().gibXPosition(),
                        originalBiene.gibPosition().gibYPosition()),
                        originalBiene.gibGeladeneHonigmenge(),
                        originalBiene.gibGeladeneNektarmenge(),
                        ((Integer) parameter.gibWert("maxGelHonig")).intValue(),
                        ((Integer) parameter.gibWert("maxGelNektar")).intValue());
            }
            
            neuesTable.put(
                    new Integer(originalBiene.gibBienenID()),
                    visSelbst);
        }
        return neuesTable;
    }
    
    /**
     * Gibt eine Liste von Bienen für die Visualisierung zurück.
     *
     * @param originalSet das zu konvertierende Set
     * @return HashSet aus einfachen Bienen
     */
    private HashSet konvertiereBienenHash(HashSet originalSet) {
        Iterator original = originalSet.iterator();
        HashSet neuesSet = new HashSet();
        
        while (original.hasNext()) {
            Biene originalBiene = (Biene) original.next();
            
            VisBiene visSelbst;
            
            int listenKenn=originalBiene.gibListenKennung();
            
            if ((listenKenn == Konstanten.ZUSCHAUEND)
            || (listenKenn== Konstanten.TANZEND)){
                
                Info info;
                
                if (listenKenn== Konstanten.ZUSCHAUEND) {
                    info=originalBiene.gibInformation().klonen();
                } else info = originalBiene.gibGesendeteInformation().klonen();
                
                visSelbst = new VisBiene(
                        konvertiereZustand(originalBiene.gibListenKennung()),
                        originalBiene.gibBienenID(),
                        originalBiene.gibBienenvolkID(),
                        new Koordinate(
                        originalBiene.gibPosition().gibXPosition(),
                        originalBiene.gibPosition().gibYPosition()),
                        originalBiene.gibGeladeneHonigmenge(),
                        originalBiene.gibGeladeneNektarmenge(),
                        ((Integer) parameter.gibWert("maxGelHonig")).intValue(),
                        ((Integer) parameter.gibWert("maxGelNektar")).intValue(),
                        info);
            } else {
                visSelbst = new VisBiene(
                        konvertiereZustand(originalBiene.gibListenKennung()),
                        originalBiene.gibBienenID(),
                        originalBiene.gibBienenvolkID(),
                        new Koordinate(
                        originalBiene.gibPosition().gibXPosition(),
                        originalBiene.gibPosition().gibYPosition()),
                        originalBiene.gibGeladeneHonigmenge(),
                        originalBiene.gibGeladeneNektarmenge(),
                        ((Integer) parameter.gibWert("maxGelHonig")).intValue(),
                        ((Integer) parameter.gibWert("maxGelNektar")).intValue());
            }
            
            neuesSet.add(visSelbst);
        }
        return neuesSet;
    }
    
    /**
     * lässt die Biene eine alternative Aktion ausführen.
     * Wird nur genutzt, wenn die Biene die gewünschte
     * Aktion nicht ausführen kann.
     * Bei der Wahl der alternativen Aktion wird berücksichtigt,
     * was für eine Aktion die Biene zuletzt ausgeführt hat und
     * welche Aktion für sie am energetisch günstigsten ist.
     *
     * @param zielBiene die Biene, die die alternative Aktion ausführen soll
     */
    private void alternativeAktionAusfuehrenLassen(Biene zielBiene) {
        //Feld der Biene
        Feld zielFeld = feldSuchen(zielBiene.gibPosition());
        
        //Letzte Aktion überprüfen
        if (zielBiene.gibAmBoden()) {
            if (zielBiene.gibGeladeneHonigmenge()
            < ((Integer) parameter.gibWert("honigWarten")).intValue()) {
                bieneLoeschen(zielBiene.gibAktionsCode());
            } else {
                //Biene aus der ursprünglichen Liste entfernen
                bieneAusFeldLoeschen(zielBiene, zielBiene.gibListenKennung());
                //Biene wieder eintragen
                zielFeld.unbedingtesEintragen(zielBiene);
                zielBiene.setzeZustand(Konstanten.WARTEND);
                //Honigverbrauch anrechnen
                zielBiene.setzeGeladeneHonigmenge(
                        zielBiene.gibGeladeneHonigmenge()
                        - ((Integer) parameter.gibWert(
                        "honigWarten")).intValue());
            }
            
        } else {
            /*
             * Ist die Biene in der Luft, so fliegt sie auch weiterhin
             * solange sie dafür ausreichend Honig hat
             */
            if (zielBiene.gibGeladeneHonigmenge()
            < ((Integer) parameter.gibWert(
                    "honigFliegen")).intValue()) {
                bieneLoeschen(zielBiene.gibAktionsCode());
            } else {
                /*
                 * Nachdem die Biene schon in der Luft ist, wird nur noch
                 * der Zustand gesetzt und die Energie angerechnet
                 */
                zielBiene.setzeZustand(Konstanten.FLIEGEND);
                //Honigverbrauch anrechnen
                zielBiene.setzeGeladeneHonigmenge(
                        zielBiene.gibGeladeneHonigmenge()
                        - ((Integer) parameter.gibWert(
                        "honigFliegen")).intValue());
            }
            
        }
    }
    
    /**
     * Trägt eine Biene in ein gewünschtes Feld ein.
     * Ist die Biene dort schon eingetragen,
     * so wird sie nicht erneut eingetragen.
     *
     * @param zielBiene die einzutragende Biene
     * @param liste die Liste, in die die Biene eingetragen werden soll
     * @return ob sie eingtragen werden konnte
     */
    private boolean bieneInFeldEintragen(Biene zielBiene, int liste) {
        Feld zielFeld = feldSuchen(zielBiene.gibPosition());
        
        switch (liste) {
            case Konstanten.FLIEGEND:
                //Testen
                if (zielFeld.gibFliegendeBienen().contains(zielBiene)) {
                    return true;
                } else {
                    return zielFeld.trageFliegendeBieneEin(zielBiene);
                }
                
            case Konstanten.WARTEND:
                if (zielFeld.gibWartendeBienen().contains(zielBiene)) {
                    return true;
                } else {
                    return zielFeld.trageWartendeBieneEin(zielBiene);
                }
                
            case Konstanten.TANZEND:
                if (zielFeld.gibTanzendeBienen().contains(zielBiene)) {
                    return true;
                } else {
                    return zielFeld.trageTanzendeBieneEin(zielBiene);
                }
                
            case Konstanten.ABBAUEND:
                if (((Blume) zielFeld).gibAbbauendeBienen().contains(
                        zielBiene)) {
                    return true;
                } else {
                    return ((Blume) zielFeld).trageAbbauendeBieneEin(zielBiene);
                }
            case Konstanten.TANKENABLIEFERN:
                if (zielFeld.gibSonstigeBienen().contains(
                        zielBiene)) {
                    return true;
                } else {
                    return zielFeld.trageSonstigeBieneEin(zielBiene);
                }
                
            default:
                return false;
        }
        
    }
    
    /**
     * Löscht eine Biene aus einem Feld. Muss dazu wissen,
     * in welcher Liste innerhalb des Feldes sich die Biene befindet.
     *
     * @param zielBiene die zu entfernende Biene
     * @param liste die Liste, in die sie eingtragen werden soll
     *
     */
    private void bieneAusFeldLoeschen(Biene zielBiene, int liste) {
        
        switch (liste) {
            case Konstanten.FLIEGEND:
                feldSuchen(zielBiene.gibPosition()).entferneFliegendeBiene(
                        zielBiene);
                break;
                
            case Konstanten.WARTEND:
                feldSuchen(zielBiene.gibPosition()).entferneWartendeBiene(
                        zielBiene);
                break;
                
            case Konstanten.TANZEND:
                feldSuchen(zielBiene.gibPosition()).entferneTanzendeBiene(
                        zielBiene);
                break;
                
            case Konstanten.ABBAUEND:
                ((Blume) feldSuchen(
                        zielBiene.gibPosition())).entferneAbbauendeBiene(
                        zielBiene);
                break;
                
            case Konstanten.ZUSCHAUEND:
                feldSuchen(zielBiene.gibPosition()).entferneWartendeBiene(
                        zielBiene);
                break;
            case Konstanten.TANKENABLIEFERN:{
                feldSuchen(zielBiene.gibPosition()).entferneSonstigeBiene(
                        zielBiene);
            }break;
        }
        
    }
    
    /**
     * gibt eine Biene zurück,
     * in der Liste liste mit der ID bieneID zu finden ist.
     *
     * @param bieneID ID der zu suchenden Biene
     * @return die gesuchte Biene, oder null,
     *         wenn sie nicht gefunden werden konnte
     */
    private Biene bieneSuchen(int bieneID) {
        if (bienenNachID.containsKey(new Integer(bieneID))) {
            return (Biene) bienenNachID.get(new Integer(bieneID));
        } else {
            //Wurde Biene nicht gefunden, so gib nichts zurück (null)
            return null;
        }
    }
    
    /**
     * gibt eine Biene aus der Liste liste zurück,
     * die den Aktionscode aktCode hat.
     *
     * @param aktCode Aktionscode der zu suchenden Biene
     * @return Biene, odernull, wenn sie nicht gefunden werden konnte
     */
    private  Biene bieneSuchen(long aktCode) {
        synchronized(bienenNachAC){
            if (bienenNachAC.containsKey(new Long(aktCode))) {
                return (Biene) bienenNachAC.get(new Long(aktCode));
            } else {
                //Wurde Biene nicht gefunden, so gib nichts zurück (null)
                return null;
            }
        }
    }
    
    /**
     * gibt ein Feld mit den Koordinaten ort zurück.
     *
     * @param ort die Koordinate des zu suchenden Feldes
     * @return Feld, oder null, wenn es nicht gefunden werden konnte
     */
    private Feld feldSuchen(Koordinate ort) {
        if (spielfeld.containsKey(ort)) {
            return (Feld) spielfeld.get(ort);
        } else {
            //Wurde das Feld nicht gefunden, so gib nichts zurück (null)
            return null;
        }
    }
    
    /**
     * Sucht den Bienenstock eines bestimmten Bienenvolkes.
     *
     * @param volxID die ID des zu suchenden Bienenstockes
     * @return Bienenstock, oder null, wenn er nicht gefunden werden konnte
     */
    private Bienenstock bienenstockSuchen(int volxID) {
        Enumeration felder = spielfeld.elements();
        Feld feldTmp;
        while (felder.hasMoreElements()) {
            feldTmp = (Feld) felder.nextElement();
            if (feldTmp instanceof Bienenstock
                    && ((Bienenstock) feldTmp).gibVolksID() == volxID) {
                return (Bienenstock) feldTmp;
            }
        }
        return null;
    }
    
    /**
     * Gibt für die Zustandskennung einen lesbaren String zurück.
     *
     * @param listenkennung die Nummer der Liste, in der sich die Biene befindet
     * @return der Name der Liste als String
     */
    private String konvertiereZustand(int listenkennung) {
        switch(listenkennung) {
            case Konstanten.FLIEGEND :
                return "fliegend";
            case Konstanten.WARTEND :
                return "wartend";
            case Konstanten.TANZEND :
                return "tanzend";
            case Konstanten.ZUSCHAUEND :
                return "zuschauend";
            case Konstanten.ABBAUEND :
                return "abbauend";
            case Konstanten.TANKENABLIEFERN :
                return "tankenabliefern";
            default:
                return "FEHLER";
        }
    }
    
    
    /**
     * setzt die Aktion in der KArte als ausgeführt
     *
     * @param id die ID der Biene, die die Aktion ausgeführt hat
     */
    private void aktionAusgefuehrt(int id) {
        synchronized (ausstehendeAktionen) {
            ausstehendeAktionen.put(new Integer(id), Boolean.FALSE);
        }
    }
    
    /**
     * Sucht alle Bienenstöcke, die im aktuellen Spielfeld enthalten sind.
     *
     * @return alle Bienenstöcke auf der Karte
     */
    public HashSet bienenstoeckeSuchen() {
        HashSet stoecke = new HashSet();
        Enumeration felder = spielfeld.elements();
        Feld feldTmp;
        while (felder.hasMoreElements()) {
            feldTmp = (Feld) felder.nextElement();
            if (feldTmp instanceof Bienenstock) {
                stoecke.add(feldTmp);
            }
        }
        return stoecke;
    }
    
    /**
     * Prüft, ob noch Bienen leben.
     *
     * @return ob alle Bienen verstorben sind
     */
    public boolean alleBienenTot() {
        return bienenNachID.isEmpty();
    }
    
    
    /**
     * gibt einen Ausschnitt der Karte zurück,
     * der über Schnittstelle.infoAusschnittHolen an
     * den Agenten weitergereicht wird.
     *
     * @param aktCode der Aktionscode des Agenten
     * @return den zu erstellenden Ausschnitt
     */
    public synchronized EinfacheKarte ausschnittErstellen(long aktCode) {
        
        if (bienenNachAC.containsKey(new Long(aktCode))) {
            
            Biene sehendeBiene = bieneSuchen(aktCode);
            Hashtable alleSichtbarenFelder = new Hashtable();
            Hashtable einfacheSichtbareFelder = new Hashtable();
            
            Feld aktuellesFeld = feldSuchen(sehendeBiene.gibPosition());
            
            //Sichtweite berechnen
            int sichtweite;
            if (sehendeBiene.gibAmBoden()) {
                sichtweite = aktuellesFeld.gibSichtweiteAmBoden();
            } else {
                sichtweite = aktuellesFeld.gibSichtweiteInDerLuft();
            }
            
            //Alle sichtbaren Felder in alleFelder eintragen
            rekursivNachbarfelderEintragen(
                    sichtweite,
                    aktuellesFeld,
                    alleSichtbarenFelder);
            
            //Felder konvertieren
            Enumeration aSiFelder = alleSichtbarenFelder.elements();
            while (aSiFelder.hasMoreElements()) {
                Feld feldTmp = (Feld) aSiFelder.nextElement();
                if (feldTmp instanceof Blume) {
                    einfacheSichtbareFelder.put(
                            feldTmp.gibPosition(),
                            konvertiereBlume((Blume) feldTmp));
                } else if (feldTmp instanceof Bienenstock) {
                    einfacheSichtbareFelder.put(
                            feldTmp.gibPosition(),
                            konvertiereBienenstock((Bienenstock) feldTmp));
                } else {
                    einfacheSichtbareFelder.put(
                            feldTmp.gibPosition(),
                            konvertierePlatz((Platz) feldTmp));
                }
            }
            
            //Nachbarfelder setzen
            Enumeration eSiFelder = einfacheSichtbareFelder.elements();
            //System.out.println("NEUE WHILE");
            //über alle sichtbaren Felder gehen
            while (eSiFelder.hasMoreElements()) {
                //System.out.println("am anfang der while");
                EinfachesFeld eFeldTmp = (EinfachesFeld) eSiFelder.nextElement();
                Iterator nachbarn =
                        ((HashSet) feldSuchen(
                        eFeldTmp.gibPosition()).gibNachbarfelder()).iterator();
                //die Hashtable, die eingetragen wird
                HashMap nachbarfelder = new HashMap();
                //System.out.println("vor der inneren while");
                //über alle Nachbarfelder gehen
                while (nachbarn.hasNext()) {
                    Feld feldTmp = (Feld) nachbarn.next();
                    //System.out.println("neues feldtmp gesetzt");
                    Koordinate koordTmp = null;
                    if (feldTmp == null) {
                        System.out.println("feldTmp == null");
                    } else {
                        koordTmp = feldTmp.gibPosition();
                    }
                    if (einfacheSichtbareFelder.size() == 0) {
                        System.out.println("einfSichtbFelder.size == 0");
                    }
                    nachbarfelder.put(
                            koordTmp,
                            einfacheSichtbareFelder.get(koordTmp));
                    //System.out.println("zu nachbarn hinzugefuegt");
                }
                //System.out.println("nach der inneren while");
                //Nachbarfelder eintragen
                eFeldTmp.setzeNachbarfelder(nachbarfelder);
                //System.out.println("nachbarfelder gesetzt");
            }
            
            
            EinfacheBiene einfachSelbst;
            
        /*if (sehendeBiene == null) {
            System.out.println("sehendeBiene == null");
        }*/
            if (sehendeBiene.gibListenKennung() == Konstanten.WARTEND) {
                //System.out.println("biene zuschauend");
                
                Info info=sehendeBiene.gibInformation();
                Info returnInfo=null;
                if (info!=null) returnInfo=info.klonen();
                
                einfachSelbst = new EinfacheBiene(
                        spielmeister.gibRundennummer(),
                        konvertiereZustand(sehendeBiene.gibListenKennung()),
                        sehendeBiene.gibBienenID(),
                        sehendeBiene.gibBienenvolkID(),
                        new Koordinate(
                        sehendeBiene.gibPosition().gibXPosition(),
                        sehendeBiene.gibPosition().gibYPosition()),
                        sehendeBiene.gibAktionsCode(),
                        sehendeBiene.gibGeladeneHonigmenge(),
                        sehendeBiene.gibGeladeneNektarmenge(),
                        returnInfo);
                
            } else {
                //System.out.println("nicht zuschauend");
                einfachSelbst = new EinfacheBiene(
                        spielmeister.gibRundennummer(),
                        konvertiereZustand(sehendeBiene.gibListenKennung()),
                        sehendeBiene.gibBienenID(),
                        sehendeBiene.gibBienenvolkID(),
                        new Koordinate(
                        sehendeBiene.gibPosition().gibXPosition(),
                        sehendeBiene.gibPosition().gibYPosition()),
                        sehendeBiene.gibAktionsCode(),
                        sehendeBiene.gibGeladeneHonigmenge(),
                        sehendeBiene.gibGeladeneNektarmenge());
            }
            
            //System.out.println("vor dem zurueckgeben");
            
            //einface Karte instanziieren und zurückgeben
            return new EinfacheKarte(
                    einfachSelbst,
                    einfacheSichtbareFelder
                    );
        } else {
            System.out.println("ungueltiger aktCode: " + aktCode);
            return null;}
    }
    
    
    
    /**
     * Gibt einen Schnappschuss der gesamten Karte
     * speziell für die Visualisierung zurück.
     *
     * @return eine visualisierungs Implementierung der Karte
     */
    public VisKarte visualisieren() {
        
        HashMap bienen = new HashMap();
        Enumeration alleFelder = spielfeld.elements();
        HashMap neuesSpielfeld = new HashMap();
        Feld vollstFeld;
        
        //instanziieren aller Bienen
        bienen = konvertiereBienenHash(bienenNachID);
        
        //instanziieren aller Felder
        while (alleFelder.hasMoreElements()) {
            vollstFeld = (Feld) alleFelder.nextElement();
            //prüfen welcher Typ von Feld es ist
            if (vollstFeld instanceof Platz) {
                neuesSpielfeld.put(vollstFeld.gibPosition(),
                        new VisPlatz(
                        //Konstruktor Parameter
                        new Koordinate(
                        vollstFeld.gibPosition().gibXPosition(),
                        vollstFeld.gibPosition().gibYPosition()),
                        vollstFeld.gibSichtweiteAmBoden(),
                        vollstFeld.gibSichtweiteInDerLuft(),
                        konvertiereBienenHash(vollstFeld.gibWartendeBienen()),
                        konvertiereBienenHash(vollstFeld.gibFliegendeBienen()),
                        konvertiereBienenHash(vollstFeld.gibTanzendeBienen()),
                        konvertiereBienenHash(vollstFeld.gibSonstigeBienen())
                        ));
            } else if (vollstFeld instanceof Bienenstock) {
                neuesSpielfeld.put(vollstFeld.gibPosition(),
                        new VisBienenstock(
                        //Konstruktor Parameter
                        new Koordinate(
                        vollstFeld.gibPosition().gibXPosition(),
                        vollstFeld.gibPosition().gibYPosition()),
                        vollstFeld.gibSichtweiteAmBoden(),
                        vollstFeld.gibSichtweiteInDerLuft(),
                        konvertiereBienenHash(vollstFeld.gibWartendeBienen()),
                        konvertiereBienenHash(vollstFeld.gibFliegendeBienen()),
                        konvertiereBienenHash(vollstFeld.gibTanzendeBienen()),
                        konvertiereBienenHash(vollstFeld.gibSonstigeBienen()),
                        ((Bienenstock) vollstFeld).gibMaxGelagerterNektar(),
                        ((Bienenstock) vollstFeld).gibVorhandenerNektar(),
                        ((Bienenstock) vollstFeld).gibVorhandenerHonig(),
                        ((Bienenstock) vollstFeld).gibVolksID())
                        );
                
            } else if (vollstFeld instanceof Blume) {
                neuesSpielfeld.put(vollstFeld.gibPosition(),
                        new VisBlume(
                        //Konstruktor Parameter
                        new Koordinate(
                        vollstFeld.gibPosition().gibXPosition(),
                        vollstFeld.gibPosition().gibYPosition()),
                        vollstFeld.gibSichtweiteAmBoden(),
                        vollstFeld.gibSichtweiteInDerLuft(),
                        konvertiereBienenHash(vollstFeld.gibWartendeBienen()),
                        konvertiereBienenHash(vollstFeld.gibFliegendeBienen()),
                        konvertiereBienenHash(vollstFeld.gibTanzendeBienen()),
                        konvertiereBienenHash(vollstFeld.gibSonstigeBienen()),
                        ((Blume) vollstFeld).gibMerkmal(),
                        ((Blume) vollstFeld).gibVorhandenerNektar(),
                        ((Blume) vollstFeld).gibMaxNektarProRunde(),
                        konvertiereBienenHash(
                        ((Blume) vollstFeld).gibAbbauendeBienen())
                        )
                        );
            }
        }
        //Nachbarfelder setzen
        VisFeld tmpVisFeld;
        
        alleFelder = spielfeld.elements();
        while (alleFelder.hasMoreElements()) {
            vollstFeld = (Feld) alleFelder.nextElement();
            Iterator originalNachbarfelder
                    = vollstFeld.gibNachbarfelder().iterator();
            
            Hashtable neueNachbarfelder = new Hashtable();
            while (originalNachbarfelder.hasNext()) {
                Koordinate nachbarkoordinate =
                        ((Feld) originalNachbarfelder.next()).gibPosition();
                neueNachbarfelder.put(nachbarkoordinate,
                        neuesSpielfeld.get(nachbarkoordinate));
            }
            ((VisFeld)neuesSpielfeld.get(vollstFeld.gibPosition())).setzeNachbarfelder(neueNachbarfelder);
        }
        return new VisKarte(spielmeister.gibRundennummer(),
                bienen,
                neuesSpielfeld,
                parameter.gibParameterHashTabelle());
    }
    
    /**
     * prüft, ob die Biene in der Liste wartendeBienen des
     * entsprechenden Feldes enthalten ist,
     * ob die Kapazität auf dem entsprechenden Feld vorhanden ist und
     * trägt gegebenenfalls die  Biene in
     * die Liste fliegendeBienen des Feldes ein.
     *
     * Zurückgegeben wird dabei,
     * ob die Aktion ausgeführt werden konnte oder nicht.
     *
     * @param aktionscode der Aktionscode der Biene
     * @return ob die Aktion ausgeführt werden konnte
     */
    public boolean bieneStartenLassen(long aktionscode) {
        //Biene zwischenspeichern
        Biene zielBiene = bieneSuchen(aktionscode);
        Feld zielFeld = feldSuchen(zielBiene.gibPosition());
        int liste = zielBiene.gibListenKennung();
        
        aktionAusgefuehrt(zielBiene.gibBienenID());
        //wenn genug Honig da ist, dann
        if ((zielBiene.gibGeladeneHonigmenge()
        >= ((Integer) parameter.gibWert("honigStarten")).intValue())
        && zielBiene.gibAmBoden()) {
            
            if (zielFeld.trageFliegendeBieneEin(zielBiene)) {
                //Werte des Abbilds setzen
                zielBiene.setzeGeladeneHonigmenge(
                        zielBiene.gibGeladeneHonigmenge()
                        - ((Integer) parameter.gibWert(
                        "honigStarten")).intValue());
                zielBiene.setzeZustand(Konstanten.FLIEGEND);
                
                //wenn sie eingetragen werden konnte aus der alten Liste löschen
                bieneAusFeldLoeschen(zielBiene, liste);
                return true;
            }
        }
        
        /*
         * Ist die Funktion noch nicht beendet, so konnte Biene nicht starten
         * also muss eine passende alternative gefunden und ausgeführt werden
         */
        alternativeAktionAusfuehrenLassen(zielBiene);
        return false;
    }
    
    /**
     * prüft, ob die Biene in der Liste fliegendeBienen des
     * entsprechenden Feldes enthalten ist,
     * ob die Kapazität auf dem entsprechenden Feld vorhanden ist
     * und bewegt gegebenenfalls die  Biene zu dem Feld Ziel.
     *
     * Zurückgegeben wird dabei,
     * ob die Aktion ausgeführt werden konnte oder nicht.
     *
     * @param aktionscode der AktCode der Biene
     * @param zielFeldPosition die Koordinate des Zielfeldes
     * @return ob die Aktion ausgeführt werden konnte
     */
    public boolean bieneFliegenLassen(long aktionscode,
                                      Koordinate zielFeldPosition) {
        Biene zielBiene = bieneSuchen(aktionscode);
        Feld ursprung = feldSuchen(zielBiene.gibPosition());
        Feld zielFeld = feldSuchen(zielFeldPosition);
        
        aktionAusgefuehrt(zielBiene.gibBienenID());
        
        //Existiert das Zielfeld?
        //genug Honig zum Fliegen vorhanden?
        if ((zielFeld != null)
            && ((zielFeld.equals(ursprung)
                || (ursprung.gibNachbarfelder().contains(zielFeld))))
            && (zielBiene.gibGeladeneHonigmenge()
                >= ((Integer) parameter.gibWert("honigFliegen")).intValue())) {
            
            //Fliegt die Biene schon?
            if (!ursprung.gibFliegendeBienen().contains(zielBiene)) {
                return false;
                
                //Ist noch genügend Kapazität für eine ankommende Biene vorhanden?
            } else if (zielFeld.trageFliegendeBieneEin(zielBiene)) {
                //Bei Biene neue Werte setzen
                zielBiene.setzeGeladeneHonigmenge(
                        zielBiene.gibGeladeneHonigmenge()
                        - ((Integer) parameter.gibWert(
                        "honigFliegen")).intValue());
                zielBiene.setzeZustand(Konstanten.FLIEGEND);
                zielBiene.setzePosition(zielFeldPosition);
                
                //entferne Biene aus Ursprungsfeld wenn sie das Feld wechselt
                if (zielFeld != ursprung) {
                    ursprung.entferneFliegendeBiene(zielBiene);
                }
                
                return true;
                
            }
            
        }
        
        alternativeAktionAusfuehrenLassen(zielBiene);
        return false;
    }
    
    /**
     * prüft, ob die Biene in der Liste fliegendeBienen des
     * entsprechenden Feldes enthalten ist und trägt gegebenenfalls die
     * Biene in die Liste wartendeBienen des Feldes ein.
     * Falls das Feld ein Bienenstock ist,
     * so dürfen nur Bienen mit der gleichen VolksID wie der Stock landen.
     *
     *
     * Zurückgegeben wird dabei,
     * ob die Aktion ausgeführt werden konnte oder nicht.
     *
     * @param aktionscode der Aktionscode der Biene
     * @return ob die Aktio ausgeführt werden konnte
     */
    public boolean bieneLandenLassen(long aktionscode) {
        Biene zielBiene = bieneSuchen(aktionscode);
        Feld zielFeld = feldSuchen(zielBiene.gibPosition());
        
        aktionAusgefuehrt(zielBiene.gibBienenID());
        //genug Honig zum Fliegen vorhanden?
        if (zielBiene.gibGeladeneHonigmenge()
        >= ((Integer) parameter.gibWert("honigLanden")).intValue()) {
            
            //ist sie in der richtigen Liste? und dann
            //ist noch Kapazität in der neuen Liste vorhanden?
            if (zielFeld.gibFliegendeBienen().contains(zielBiene)
            && zielFeld.trageWartendeBieneEin(zielBiene)) {
                
                //Neue Werte setzen
                zielBiene.setzeGeladeneHonigmenge(
                        zielBiene.gibGeladeneHonigmenge()
                        - ((Integer) parameter.gibWert(
                        "honigLanden")).intValue());
                zielBiene.setzeZustand(Konstanten.WARTEND);
                
                //Biene aus der alten Liste entfernen
                zielFeld.entferneFliegendeBiene(zielBiene);
                
                return true;
            }
            
        }
        
        alternativeAktionAusfuehrenLassen(zielBiene);
        return false;
    }
    
    /**
     * prüft, ob die Biene in der Liste wartendeBienen, abbauendeBienen oder
     * tanzendeBienen des entsprechenden Feldes enthalten ist,
     * ob die Kapazität in der Liste wartendeBienen vorhanden ist und
     * trägt gegebenenfalls die  Biene in die Liste ein.
     *
     * Zurückgegeben wird dabei,
     * ob die Aktion ausgeführt werden konnte oder nicht.
     *
     * @param aktionscode der Aktionscode der Biene
     * @return ob die Aktio ausgeführt werden konnte
     */
    public boolean bieneWartenLassen(long aktionscode) {
        Biene zielBiene = bieneSuchen(aktionscode);
        Feld zielFeld = feldSuchen(zielBiene.gibPosition());
        
        aktionAusgefuehrt(zielBiene.gibBienenID());
        if (zielBiene.gibGeladeneHonigmenge()
        >= ((Integer) parameter.gibWert("honigWarten")).intValue()) {
            
            if (zielBiene.gibAmBoden()
            && zielFeld.trageWartendeBieneEin(zielBiene)) {
                
                //Biene aus der alten Liste entfernen wenn sie diese verlässt
                if (zielBiene.gibListenKennung() != Konstanten.WARTEND) {
                    bieneAusFeldLoeschen(zielBiene,
                            zielBiene.gibListenKennung());
                }
                
                //Neue Werte setzen
                zielBiene.setzeGeladeneHonigmenge(
                        zielBiene.gibGeladeneHonigmenge()
                        - ((Integer) parameter.gibWert("honigWarten")).intValue());
                zielBiene.setzeZustand(Konstanten.WARTEND);
                
                return true;
            }
        }
        
        alternativeAktionAusfuehrenLassen(zielBiene);
        return false;
    }
    
    
    
    /**
     * prüft, ob die Biene in der Liste wartendeBienen des
     * entsprechenden Feldes enthalten ist,
     * ob die Kapazität in der Liste tanzendeBienen vorhanden ist und
     * trägt gegebenenfalls die  Biene in die Liste ein.
     *
     * Zurückgegeben wird dabei,
     * ob die Aktion ausgeführt werden konnte oder nicht.
     *
     * @param aktionscode der Aktionscode der Biene
     * @param infoX Zielkoordinate x position
     * @param infoY Zielkoordinate y position
     * @param richtung ob die Richtung mitgeteilt werden soll
     * @param entfernung ob die entfernung mitgeteilt werden soll
     * @param nutzen Nutzen der Blume
     * @return ob die Aktion ausgeführt werden konnte
     */
    public boolean bieneTanzenLassen(long aktionscode,
            int infoX,
            int infoY,
            boolean richtung,
            boolean entfernung,
            double nutzen) {
        Biene zielBiene = bieneSuchen(aktionscode);
        if (!(zielBiene == null)) {
            Feld zielFeld = feldSuchen(zielBiene.gibPosition());
            aktionAusgefuehrt(zielBiene.gibBienenID());
            int kosten = 0;
            if (richtung && entfernung) {
                kosten
                        = ((Integer) parameter.gibWert(
                        "honigTanzenAlles")).intValue();
            } else if (richtung) {
                kosten
                        = ((Integer) parameter.gibWert(
                        "honigTanzenRichtung")).intValue();
            } else if (entfernung) {
                kosten
                        = ((Integer) parameter.gibWert(
                        "honigTanzenEntfernung")).intValue();
            } else {
                alternativeAktionAusfuehrenLassen(zielBiene);
                return false;
            }
            
            //Hat die Biene noch genügend Honig?
            if (zielBiene.gibGeladeneHonigmenge() >= kosten) {
                
                if (zielBiene.gibAmBoden()
                && zielFeld.trageTanzendeBieneEin(zielBiene)) {
                    
                    //Biene aus der alten Liste entfernen
                    //wenn sie diese verlässt
                    if (zielBiene.gibListenKennung() != Konstanten.TANZEND) {
                        try{
                            bieneAusFeldLoeschen(zielBiene,
                                    zielBiene.gibListenKennung());
                        }catch (Exception ex){}
                    }
                    
                    //Neue Werte setzen
                    zielBiene.setzeZustand(Konstanten.TANZEND);
                    zielBiene.setzeGeladeneHonigmenge(
                            zielBiene.gibGeladeneHonigmenge()
                            - kosten);
                    Info konvInfo=konvertiereInfo(
                            zielBiene.gibPosition(),
                            infoX,
                            infoY,
                            richtung,
                            entfernung,
                            nutzen);
                    int rundenNr=this.spielmeister.gibRundennummer();
                    this.addTanzInfo(zielBiene.gibBienenID(), konvInfo, rundenNr);
                    zielBiene.setzeGesendeteInformation(konvInfo.klonen());
                    /** damit auf die Information auch eine Runde später zugegriffen werden kann */
                    Integer rundenNrInteger=new Integer(this.spielmeister.gibRundennummer());
                    Integer bienenIdInteger=new Integer(zielBiene.gibBienenID());
                    this.getanztInRundeNr.put(bienenIdInteger,rundenNrInteger);
                    return true;
                }
            }
            
            alternativeAktionAusfuehrenLassen(zielBiene);
            return false;
        } else {
            alternativeAktionAusfuehrenLassen(zielBiene);
            return false;
        }
    }
    
    /**
     * prüft, ob die Biene auf der Liste wartendeBienen eingetragen ist und
     * ob die Biene IDTanzendeBiene tanzt.
     *
     * Gibt zurück, ob die Aktion ausgeführt werden kann.
     * @param aktionscodeSitzendeBiene der Aktionscode der Biene
     * @param idTanzendeBiene die Id der Biene der zugeschaut werden soll
     * @return ob die Aktio ausgeführt werden konnte
     */
    public boolean bieneZuschauenLassen(long aktionscodeSitzendeBiene,
            int idTanzendeBiene) {
        Biene tanzendeBiene
                = (Biene) bienenNachID.get(new Integer(idTanzendeBiene));
        Biene zielBiene = (Biene) bieneSuchen(aktionscodeSitzendeBiene);
        Feld zielFeld = feldSuchen(zielBiene.gibPosition());
        aktionAusgefuehrt(zielBiene.gibBienenID());
        //Haben wir sie gefunden?
        if ((!(zielBiene == null))
                /*
                 * hat sie genug Honig?
                 */
        && (zielBiene.gibGeladeneHonigmenge()
        >= ((Integer) parameter.gibWert(
                "honigZuschauen")).intValue())
                /*
                 * eistiert die tanzende Biene ueberhaupt?
                 */
                && (!(tanzendeBiene == null))
                /*
                 * ist die tanzende Biene auf dem selben Feld
                 * und tanzt sie überhaupt?
                 */
                && (tanzendeBiene.gibPosition().equals(zielBiene.gibPosition()))
                && ( (tanzendeBiene.gibListenKennung() == Konstanten.TANZEND)
                || this.getantzInLetzterRunde(tanzendeBiene.gibBienenID()))) {
            
            if (zielBiene.gibAmBoden()
            && zielFeld.trageWartendeBieneEin(zielBiene)) {
                
                //Biene aus der alten Liste entfernen wenn sie diese verlässt
                if (zielBiene.gibListenKennung() != Konstanten.WARTEND) {
                    bieneAusFeldLoeschen(zielBiene,
                            zielBiene.gibListenKennung());
                }
                
                //Neue Werte setzen
                zielBiene.setzeGeladeneHonigmenge(
                        zielBiene.gibGeladeneHonigmenge()
                        - ((Integer) parameter.gibWert(
                        "honigZuschauen")).intValue());
                
                int rundenNr=this.spielmeister.gibRundennummer();
                int vorletzteRundenNr=rundenNr-1;
                zielBiene.setzeZustand(Konstanten.ZUSCHAUEND);
                Info tanzInfo=this.getTanzInfo(tanzendeBiene.gibBienenID(), vorletzteRundenNr);
                zielBiene.setzeInformation(tanzInfo);
                
                return true;
            }
        }
        
        System.out.println(zielBiene.gibBienenID() + ": kann nicht zuschauen");
        alternativeAktionAusfuehrenLassen(zielBiene);
        return false;
    }
    
    /**
     * füllt den Tank geladeneHonigmenge um gewünschteHonigmenge Einheiten auf.
     *
     * Zurückgegeben wird ob noch entsprechend viel Platz im Tank war.
     *
     * @param aktionscode der Aktionscode der Biene
     * @param gewuenschteHonigmenge die Menge an Honig, die die Biene haben will
     * @return ob die Aktion ausgeführt werden konnte
     */
    public boolean bieneHonigTankenLassen(long aktionscode,
            int gewuenschteHonigmenge) {
        Biene zielBiene = bieneSuchen(aktionscode);
        Feld zielFeld = feldSuchen(zielBiene.gibPosition());
        int wunschmenge;
        if (gewuenschteHonigmenge > 0) {
            wunschmenge = gewuenschteHonigmenge;
        } else {
            wunschmenge = 0;
        }
        /* im falle dass wunschmenge=Integer.MAX_VALUE,
          gibt es ein overflow und die summe (wunschmenge + geladenerHonig) wird negativ */
        if (wunschmenge>1000000) wunschmenge=1000000;
        int geladenerHonig = zielBiene.gibGeladeneHonigmenge();
        
        
        aktionAusgefuehrt(zielBiene.gibBienenID());
        
        //wenn es ein Bienenstock ist und dann
        //die BienenvolkID stimmt
        if ((feldSuchen(zielBiene.gibPosition()) instanceof Bienenstock)
        && (zielBiene.gibBienenvolkID()
        == ((Bienenstock) zielFeld).gibVolksID())) {
            
            //wenn genug Honig da ist
            if (geladenerHonig
                    >= ((Integer) parameter.gibWert(
                    "honigHonigTanken")).intValue()) {
                //wenn sie in wartendeBienen eingetragen ist
                if (zielBiene.gibAmBoden()) {
                    
                    /*
                     * Biene aus der alten Liste entfernen wenn sie
                     * diese verlässt
                     */
                    /*if (zielBiene.gibListenKennung() == Konstanten.TANZEND) {
                        zielFeld.entferneTanzendeBiene(zielBiene);
                    }*/
                    bieneAusFeldLoeschen(zielBiene, zielBiene.gibListenKennung());
                    //fairerweise erst Abbuchen
                    zielBiene.setzeGeladeneHonigmenge(
                            geladenerHonig
                            - ((Integer) parameter.gibWert(
                            "honigHonigTanken")).intValue());
                    geladenerHonig=geladenerHonig-((Integer) parameter.gibWert(
                            "honigHonigTanken")).intValue();
                /*    zielBiene.setzeZustand(Konstanten.WARTEND);
                    zielFeld.trageWartendeBieneEin(zielBiene);*/
                    zielBiene.setzeZustand(Konstanten.TANKENABLIEFERN);
                    zielFeld.trageSonstigeBieneEin(zielBiene);
                    //wieviel kann sie tanken?
                    if ((geladenerHonig + wunschmenge)
                    > ((Integer) parameter.gibWert(
                            "maxGelHonig")).intValue()) {
                        
                        zielBiene.setzeGeladeneHonigmenge(geladenerHonig
                                + ((Bienenstock) zielFeld).honigAbgeben(
                                ((Integer) parameter.gibWert(
                                "maxGelHonig")).intValue()
                                - geladenerHonig));
                    } else {
                        zielBiene.setzeGeladeneHonigmenge(geladenerHonig
                                + ((Bienenstock) zielFeld).honigAbgeben(
                                wunschmenge));
                        
                    }
                    
                    return true;
                }
            }
        }
        
        alternativeAktionAusfuehrenLassen(zielBiene);
        return false;
    }
    
    /**
     * prüft, ob die Biene im richtigen Bienenstock ist und fügt der
     * dort vorhandenen Nektarmenge die Menge des von der
     * Biene mitgeführten Nektars hinzu.
     *
     * Dann wird das Attribut geladeneNektarmenge auf 0 gesetzt.
     *
     * @param aktionscode der Aktionscode der Biene
     * @return ob die Aktion ausgeführt werden konnte
     */
    public boolean bieneNektarAbliefernLassen(long aktionscode) {
        Biene zielBiene = bieneSuchen(aktionscode);
        Feld zielFeld = feldSuchen(zielBiene.gibPosition());
        int geladenerHonig = zielBiene.gibGeladeneHonigmenge();
        
        aktionAusgefuehrt(zielBiene.gibBienenID());
        
        if (zielBiene.gibAmBoden()) {
            //wenn es ein Bienenstock ist und dann
            //die BienenvolkID stimmt und dann
            //ob die Biene am Boden war.
            if ((zielFeld instanceof Bienenstock)
            && (zielBiene.gibBienenvolkID()
            == ((Bienenstock) zielFeld).gibVolksID())) {
                
                if (zielBiene.gibGeladeneHonigmenge()
                >= ((Integer) parameter.gibWert(
                        "honigNektarAbliefern")).intValue()) {
                    
                    zielBiene.setzeGeladeneHonigmenge(
                            zielBiene.gibGeladeneHonigmenge()
                            - ((Integer) parameter.gibWert(
                            "honigNektarAbliefern")).intValue());
                    
                    //Biene aus der alten Liste entfernen
                    //wenn sie diese verlässt
                /*    if (zielBiene.gibListenKennung() == Konstanten.TANZEND) {
                        zielFeld.entferneTanzendeBiene(zielBiene);
                    }*/
                    bieneAusFeldLoeschen(zielBiene, zielBiene.gibListenKennung());
                    //Nektar abbuchen
                    zielBiene.setzeGeladeneNektarmenge(
                            zielBiene.gibGeladeneNektarmenge()
                            - ((Bienenstock) zielFeld).nektarAbnehmen(
                            zielBiene.gibGeladeneNektarmenge()));
                    //Aktion setzen
                    zielBiene.setzeZustand(Konstanten.TANKENABLIEFERN);
                    zielFeld.trageSonstigeBieneEin(zielBiene);
                    return true;
                }
            }
        }
        
        alternativeAktionAusfuehrenLassen(zielBiene);
        return false;
    }
    
    /**
     * prüft, ob die Biene in der Liste wartendeBienen des
     * entsprechenden Feldes enthalten ist,
     * ob die Kapazität vorhanden ist und trägt gegebenenfalls die
     * Biene in die Liste abbauendeBienen der Blume ein.
     *
     * Zurückgegeben wird dabei,
     * ob die Aktion ausgeführt werden konnte oder nicht.
     *
     * @param aktionscode der Aktionscode der Biene
     * @param gewuenschteNektarmenge bestellte Menge an Nektar
     * @return ob die Aktio ausgeführt werden konnte
     */
    public boolean bieneNektarAbbauenLassen(long aktionscode,
            int gewuenschteNektarmenge) {
        Biene zielBiene = bieneSuchen(aktionscode);
        Feld zielFeld = feldSuchen(zielBiene.gibPosition());
        int idBienie = zielBiene.gibBienenID();
        int honigkosten
                = ((Integer) parameter.gibWert("honigNektarAbbauen")).intValue();
        int wunschmenge;
        if (gewuenschteNektarmenge > 0) {
            wunschmenge = gewuenschteNektarmenge;
        } else {
            //System.out.println("Karte: illegal value -> set to zero");
            wunschmenge = 0;
        }
        if (wunschmenge>1000000) wunschmenge=1000000;
        aktionAusgefuehrt(zielBiene.gibBienenID());
        
        // Wenn alter Zustand richtig, es eine Blume ist, sie genug Honig hat
        // dann versuchen wirs mal
        if (zielBiene.gibAmBoden() && (zielFeld instanceof Blume)
        && (zielBiene.gibGeladeneHonigmenge()
        >= honigkosten)) {
            
            //ist noch Platz auf der Blume?
            if (((Blume) zielFeld).trageAbbauendeBieneEin(zielBiene)) {
                
                //Biene aus der alten Liste entfernen wenn sie diese verlässt
                if (zielBiene.gibListenKennung() != Konstanten.ABBAUEND) {
                    bieneAusFeldLoeschen(zielBiene,
                            zielBiene.gibListenKennung());
                }
                
                
                //Neue Werte setzen
                zielBiene.setzeGeladeneHonigmenge(
                        zielBiene.gibGeladeneHonigmenge()
                        - honigkosten);
                zielBiene.setzeZustand(Konstanten.ABBAUEND);
                //wieviel will sie denn haben?
                int nektarmenge = 0;
                if (wunschmenge + zielBiene.gibGeladeneNektarmenge()
                > ((Integer) parameter.gibWert(
                        "maxGelNektar")).intValue()) {
                    nektarmenge = ((Blume) zielFeld).nektarAbgeben(
                            ((Integer) parameter.gibWert(
                            "maxGelNektar")).intValue()
                            - zielBiene.gibGeladeneNektarmenge());
                } else {
                    nektarmenge = ((Blume) zielFeld).nektarAbgeben(wunschmenge);
                    
                }
                /*
                 * muss die Biene mit den neuen werten in
                 * abbauendeBienen neu gespeichert werden?
                 */
                zielBiene.setzeGeladeneNektarmenge(nektarmenge
                        + zielBiene.gibGeladeneNektarmenge());
                
                return true;
            }
        }
        
        alternativeAktionAusfuehrenLassen(zielBiene);
        return false;
    }
    
    /**
     * Fügt eine Biene in die Spielkarte ein.
     * Startposition ist der Bienenstock ihres Volkes.
     *
     * @param id ID der einzufuegenden Biene
     * @param volxID die ID des Volkes der einzufügenden Biene
     * @param simId die ID im Simulator
     * @return der neue Aktionscode der Biene
     */
    public long bieneEinfuegen(int id, int volxID, Id simId) {
        Bienenstock stock = (Bienenstock) bienenstockSuchen(volxID);
        long aktCode = 0L;
        if (!(stock == null)) {
            Biene biene = new Biene(
                    id,
                    volxID,
                    stock.gibPosition(),
                    ((Integer) parameter.gibWert("startHonig")).intValue(),
                    ((Integer) parameter.gibWert("startNektar")).intValue(),
                    simId
                    );
            
            stock.unbedingtesEintragen(biene);
            bienenNachID.put(new Integer(id), biene);
            synchronized (ausstehendeAktionen) {
                ausstehendeAktionen.put(new Integer(biene.gibBienenID()),
                        new Boolean(true));
            }
            aktCode = aktionscodeSetzen(id);
            System.out.print("Biene ");
            System.out.print(id);
            System.out.println(" eingefuegt. \n");
        } else {
            System.out.print("Kann Biene ");
            System.out.print(id);
            System.out.print(" nicht einfuegen, falsche VolksID. \n");
        }
        return aktCode;
    }
    
    
    /**
     * entfernt eine Biene aus der Simulation.
     *
     * @param aktionscode der Aktionscode der zu löschenden Biene
     */
    public void bieneLoeschen(long aktionscode) {
        Biene zielBiene = bieneSuchen(aktionscode);
        
        //Agenten auf seine Löschung hinweisen
        //zielBiene
        
        
        System.out.println("Biene " + zielBiene.gibBienenID()
        + " wurde geloescht.");
        //Aktionscodes entwerten
        gueltigeAktionscodes.remove(new Long(aktionscode));
        synchronized (ausstehendeAktionen) {
            ausstehendeAktionen.remove(new Integer(zielBiene.gibBienenID()));
        }
        
        //dem MEISTER mitteilen, daß biene gelöscht wurde
        spielmeister.removeAgent(zielBiene.gibBienenID());
        
        //aus den Listen bienenNachXX löschen
        bienenNachAC.remove(new Long(zielBiene.gibAktionsCode()));
        bienenNachID.remove(new Integer(zielBiene.gibBienenID()));
        
        //aus der entsprechenden Liste des entsprechenden Feldes löschen
        //eigentlich falsch, weil es nicht entferneXXXBiene nutzt
        bieneAusFeldLoeschen(zielBiene, zielBiene.gibListenKennung());
        
    }
    
    /**
     * Setzt für die Biene, identifiziert durch ihren alten Aktionscode,
     * einen neuen Aktionscode.
     * Rückgabewert ist der neue Aktionscode.
     *
     * @param alterAktionscode der alte Aktionscode
     * @return der neue Aktionscode
     */
    public synchronized long aktionscodeSetzen(long alterAktionscode) {
        
        Biene zielBiene = bieneSuchen(alterAktionscode);
        if (zielBiene == null) {
            return 0L;
        } else {
            
            long zufallsZahl = zufallsGenerator.nextLong();
            //so lange neue Zahlen probieren, bis eine gefunden wurde,
            //die noch nicht verwendet wurde
            synchronized(verwendeteAktionscodes){
                while (verwendeteAktionscodes.contains(new Long(zufallsZahl))
                || (zufallsZahl == 0L)) {
                    zufallsZahl = zufallsGenerator.nextLong();
                }
                //neuen Code dem Hash hinzufügen
                verwendeteAktionscodes.add(new Long(zufallsZahl));
                gueltigeAktionscodes.add(new Long(zufallsZahl));
            }
            //Verweis löschen wenn existiert
            if (bienenNachAC.containsKey(new Long(
                    zielBiene.gibAktionsCode()))){
                synchronized(bienenNachAC){
                    bienenNachAC.remove(new Long(zielBiene.gibAktionsCode()));
                }
            }
            
            //neuen Code in der Biene aus bienen setzen
            zielBiene.setzeAktionsCode(zufallsZahl);
            synchronized(bienenNachAC){
                //Verweise neu setzen
                bienenNachAC.put(new Long(zielBiene.gibAktionsCode()), zielBiene);
            }
            return zufallsZahl;
        }
    }
    
    /**
     * Setzt für die Biene, dentifiziert durch ihre ID,
     * einen neuen Aktionscode.
     * Rückgabewert ist der neue Aktionscode.
     *
     * @param bieneID die ID der Biene die einen neuen Aktionscode bekommt
     * @return der neue Aktionscode
     */
    public synchronized long aktionscodeSetzen(int bieneID) {
        Biene zielBiene = bieneSuchen(bieneID);
        
        if (zielBiene == null) {
            return 0L;
        } else {
            long zufallsZahl = zufallsGenerator.nextLong();
            //so lange neue Zahlen probieren, bis eine gefunden wurde,
            //die noch nicht verwendet wurde
            while (verwendeteAktionscodes.contains(new Long(zufallsZahl))
            || (zufallsZahl == 0L)) {
                zufallsZahl = zufallsGenerator.nextLong();
            }
            //neuen Code dem Hash hinzufügen
            verwendeteAktionscodes.add(new Long(zufallsZahl));
            gueltigeAktionscodes.add(new Long(zufallsZahl));
            
            //Verweis löschen wenn existiert
            if (bienenNachAC.containsKey(new Long(
                    zielBiene.gibAktionsCode()))) {
                bienenNachAC.remove(new Long(zielBiene.gibAktionsCode()));
            }
            
            //neuen Code in der Biene aus bienen setzen
            zielBiene.setzeAktionsCode(zufallsZahl);
            
            //Verweise neu setzen
            bienenNachAC.put(new Long(zielBiene.gibAktionsCode()), zielBiene);
            
            
            return zufallsZahl;
        }
    }
    
    /**
     * Prüft, ob ein Aktionscode gültig ist.
     *
     * @param aktCode der Aktionscode der geprüft werden soll
     * @return ob der Aktionscode gueltig ist
     */
    public synchronized boolean aktionscodeGueltig(long aktCode) {
        boolean retValue=gueltigeAktionscodes.contains(new Long(aktCode));
        return retValue;
    }
    
    /**
     * entwertet einen Aktionscode.
     *
     * @param aktCode der zu entwertende Aktionscode
     * @return ob der Aktionscode gültig war
     */
    public boolean aktionscodeEntwerten(long aktCode) {
        boolean retValue=gueltigeAktionscodes.remove(new Long(aktCode));
        return retValue;
    }
    
    /**
     * Informiert die Karte darüber, das eine neue Runde beginnt.
     * Ale Agenten, die noch keine Aktion ausgefuehrt haben werden hier warten
     * gelassen und die Bienenstoecke verarbeiten ihren Nektar
     *
     */
    public void neueRunde() {
        
        Hashtable neueAusstehendeAktionen = new Hashtable();
        Iterator iterKeyAusstehendeAktionen
                = ausstehendeAktionen.keySet().iterator();
        boolean tmpVal;
        int tmpKey;
        synchronized (ausstehendeAktionen) {
            while (iterKeyAusstehendeAktionen.hasNext()) {
                tmpKey
                        = ((Integer) iterKeyAusstehendeAktionen.next()).intValue();
                tmpVal
                        = ((Boolean) ausstehendeAktionen.get(
                        new Integer(tmpKey))).booleanValue();
                if (tmpVal) {
                    alternativeAktionAusfuehrenLassen(bieneSuchen(tmpKey));
                }
                neueAusstehendeAktionen.put(new Integer(tmpKey), Boolean.TRUE);
            }
        }
        
        ausstehendeAktionen = neueAusstehendeAktionen;
        
        Iterator stoecke = bienenstoecke.iterator();
        Bienenstock tmpStock;
        int maxNektarZuHonigWert;
        double kursNektarHonigWert;
        Object obj;
        while (stoecke.hasNext()) {
            //            System.out.println("(Karte.neueRunde) stoecke hat noch einen");
            tmpStock=(Bienenstock) stoecke.next();
            obj=parameter.gibWert("maxNektarZuHonig");
            maxNektarZuHonigWert=Integer.parseInt(obj.toString());
            obj=parameter.gibWert("kursNektarHonig");
            kursNektarHonigWert=Double.parseDouble(obj.toString());
            
            tmpStock.nektarZuHonigVerarbeiten(maxNektarZuHonigWert,kursNektarHonigWert);
            
            //((Bienenstock) stoecke.next()).nektarZuHonigVerarbeiten(
            
        }
    }
    
    /**
     * Fügt der Umwelt den Agenten mit der Id agentId hinzu.
     *
     * @param agentId id des agenten, er hinzugefügt werden soll
     * @throws exceptions.InvalidAgentException illegaler Agent
     * @throws exceptions.FullEnviromentException Fehler
     */
    public void addAgentToEnviroment(Id agentId)
    throws InvalidAgentException,
            FullEnviromentException { }
    
    /**
     * Erstellt die Umwelt.
     *
     * @param graphFile das File der Karte
     * @param attributeFile das File der Attribute
     * @throws exceptions.InvalidElementException Fehler
     */
    public void createEnviroment(
            String graphFile,
            String attributeFile)
            throws InvalidElementException { }
    
    /**
     * Entfernt den Agenten mit der Id agentId aus der Umwelt.
     *
     * @param agentId Id - Id des zu entfernenden Agenten.
     * @throws exceptions.InvalidAgentException Fehler
     */
    public void removeAgentFromEnviroment(Id agentId)
    throws InvalidAgentException { }
    
    /**
     * liest die statistischen Werte von jeder Biene aus.
     *
     * @param statistik statistische Komponente zur Erfassung der statistischen
     * Werten.
     * @return true, falls die Efassung erfolgreich war.
     *
     */
    public boolean createRoundStatistik(IStatisticComponent statistik){
        if (statistik==null) return false;
        Set bienenIDs=bienenNachID.keySet();
        Iterator idIt=bienenIDs.iterator();
        boolean statistikErfolgreich=true;
        while(idIt.hasNext()){
            Id id=(Id)new Id(idIt.next().toString());
            Biene biene=(Biene)bienenNachID.get(new Integer(id.toString()));
            try{
                int honig=biene.gibGeladeneHonigmenge();
                statistik.addInformation(id, BienenStatistikKomponente.HONIG, new Integer(honig));
                int nektar=biene.gibGeladeneNektarmenge();
                statistik.addInformation(id, BienenStatistikKomponente.NEKTAR, new Integer(nektar));
            }catch (Exception exc){
                System.out.println("FEHLER bei der Statistik Erfassung");
                exc.printStackTrace();
                statistikErfolgreich=false;
            }
        }
        return statistikErfolgreich;
    }
    
    /**
     * liefert true, falls die Biene in der letzten Runde getanzt hat
     * @param bienenId Id der Biene
     * @return true, falls die Biene in der vergangenen Runde oder in jetzigen
     * Runde getanzt hat.
     */
    private boolean getantzInLetzterRunde(int bienenId){
        Integer myId=new Integer(bienenId);
        int aktuelleRundenNr=this.spielmeister.gibRundennummer();
        int getanztInRdNr=-1;
        
        if (this.getanztInRundeNr.containsKey(myId)){
            getanztInRdNr=((Integer)this.getanztInRundeNr.get(myId)).intValue();
        }
        if ((aktuelleRundenNr-getanztInRdNr)<=1) return true;
        else return false;
        
        
    }
    
    
    
    /**
     * fügt neue TanzInfomation in die Tanz-Tabelle hinzu.
     * @param bienenId Id der Tanz-Biene
     * @rundenNr Nummer der aktuellen Runde
     */
    private void addTanzInfo(int bienenId, Info tanzInfo, int rundenNr){
        int rest=rundenNr % 2;
        TanzInfo ts=new TanzInfo(bienenId, tanzInfo, rundenNr);
        tanzTab[rest].put(new Integer(bienenId), ts);
    }
    
    /**
     * liefert mitgeteilte Informationen aus der verhergehenden Rude
     * @param bienenId Id der Tanz-Biene
     * @param rundenNr Runden-Nummer in der getanzt wurde
     * @return mitgeteilte Infomation
     */
    private Info getTanzInfo(int bienenId, int rundenNr){
        int rest=rundenNr % 2;
        TanzInfo ts;
        ts=(TanzInfo)tanzTab[rest].get(new Integer(bienenId));
        if (ts==null) {
            return null;
        }
        int rundeGetanzt=ts.getRundenNr();
        if (rundenNr==rundeGetanzt) return ts.getInfo();
        else return null;
        
    }
    
    
    /**  eine Datenstruktur, die Bienen-Id, Runden-Nummer und Tanz-Infomation  enthält */
    class TanzInfo{
        /** Id der Tanz-Biene */
        int bienenId;
        /** RundenNummer in der getanzt wurde */
        int rundenNr;
        /** konvertierte TanzInfomation */
        Info tanzInfo;
        
        /** eine Datenstruktur, die Bienen-Id, Runden-Nummer und Tanz-Infomation  enthält
         * @param bienenId Id der TanzBiene
         * @param tanzInfo konvertierte Tanz-Infomation
         * @param rundenNr RundeNummer in der getanzt wurde
         */
        TanzInfo(int bienenId, Info tanzInfo, int rundenNr){
            this.bienenId=bienenId;
            this.rundenNr=rundenNr;
            this.tanzInfo=tanzInfo;
        }
        /** liefert die gespeicherte Tanz-Information
         * @return TanzInfomation
         */
        Info getInfo(){
            return tanzInfo.klonen();
        }
        /** liefet die RundenNr in der getanzt wurde
         * @return Runden-Nummer
         */
        int getRundenNr(){
            return this.rundenNr;
        }
        
    }
    
    
    
}
