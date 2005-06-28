/*
 * Dateiname      : GMLParser.java
 * Erzeugt        : 24. Oktober 2004
 * Letzte �nderung: 21. Juni 2005 durch Eugen Volk
 *
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (samuel@gmx.info)
 *
 *
 *
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut f�r
 * Intelligente Systeme der Universit�t Stuttgart unter Betreuung von
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
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Enumeration;
import nereus.utils.Parameter;
import scenarios.bienenstock.agenteninfo.Koordinate;
import java.io.FileNotFoundException;

/**
 * Erstellt aus einer GML-Datei in mehreren Schritten ein Spielfeld f�r
 * das Szenario.
 *
 * @author Samuel Walz
 */
public class GMLParser {
    
    /**
     * Enth�lt den zuletzt erstellten GML-Baum.
     * Besteht aus Knoten der Klasse GMLBaumknoten.
     * @associates scenario.bienenstock.umgebung.GMLBaumknoten
     */
    private LinkedList gmlBaum;
    
    /**
     * Stellt den Zusammenhang zwischen Zeichenposition und
     * Zeile in der eingelesenen Datei her.
     * Der Index der LinkedList plus 1 entspricht den Zeilennummern der Datei
     * - der Inhalt gibt die absolute Zeichenanzahl bis zum Ende der
     * jeweiligen Zeile an.
     */
    private LinkedList gmlZeilenindex = new LinkedList();
    
    /**
     * Enh�lt den Inhalt der zuletzt eingelesenen GML-Datei, abz�glich aller
     * Zeilen, die mit einem Kommentarzeichen (#) beginnen,
     * oder zu lang sind (> 254 Zeichen).
     */
    private static String gmlCode;
    
    /**
     *
     */
    Parameter szenarioParameter = new Parameter();
    
    /**
     * Enth�lt die neu generierte Karte
     */
    Hashtable szenarioKarte = new Hashtable();
    
    
    private int mindestBlumenAbstand=0;
    
    /**
     * Dient der Erkennung eines korrekt formatierten GML-Schl�ssels.
     */
    private static Pattern patternSchluessel =
            Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
    
    /**
     * Dient der Erkennung eines korrekt formatierten GML-Wertes
     * vom Typ Integer.
     */
    private static Pattern patternInteger =
            Pattern.compile("[-+]?[0-9]+");
    
    /**
     * Dient der Erkennung eines korrekt formatierten GML-Wertes
     * vom Typ Double.
     */
    private static Pattern patternDouble =
            Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
    
    
    
    
    /**
     * Dient der Erkennung eines korrekt formatierten GML-Wertes
     * vom Typ String.
     * (Ohne die begrenzenden Anf�hrungszeichen.)
     */
    private static Pattern patternString =
            Pattern.compile("(&[\\p{ASCII}&&[^&]]+;|[\\p{ASCII}&&[^&]]*)*");
    
    
    /**
     * Konstruktor.
     *
     * @param gmlDateiname
     */
    public GMLParser(String gmlDateiname) {
        gmlDateiEinlesen(gmlDateiname);
        werteDateiInhaltAus();
    }
    
    /**
     * Liest eine Datei ein gibt ihren Inhalt als String
     * und ohne Umbr�che zur�ck.
     * Die Umbr�che werden durch jeweils ein Leerezeichen ersetzt.
     * In der LinkedList gmlZeilenindex wird gleichzeitig der Zusammenhang
     * zwischen Zeichenposition im zur�ckgegegeben String und der
     * Zeichenposition in der eingelesenen Datei aufgezeichnet.
     */
    private void gmlDateiEinlesen(String gmlDateiname) {
        String codezeile;
        int zeilennummer = 1;
        
        StringBuffer inputBuffer = new StringBuffer();
        try {
            File gmlfile = new File(gmlDateiname);
            BufferedReader dataReader =
                    new BufferedReader(new FileReader(gmlfile));
            while(dataReader.ready()) {
                codezeile = dataReader.readLine();
                if (codezeile.length() > 254) {
                    //Fehler: Codezeile zu lang, wird ignoriert
                    System.out.print("Zeile ");
                    System.out.print(zeilennummer);
                    System.out.print(" ist zu lang(maximal 254 Zeichen): "
                            + "wird ignoriert!\n\n");
                } else {
                    //Beginnt eine Zeile mit # dann wird sie ignoriert.
                    if ((codezeile.length() > 0)
                    && (codezeile.charAt(0) != '#')) {
                        inputBuffer.append(codezeile);
                        //Leerzeichen als Ersatz f�r den Zeilenumbruch anh�ngen
                        inputBuffer.append(" ");
                    }
                }
                zeilennummer = zeilennummer + 1;
                //Zeilenindex aktualisieren
                gmlZeilenindex.add(new Integer(inputBuffer.length()));
            }
            dataReader.close();
            gmlCode = inputBuffer.toString();
        }catch (FileNotFoundException e) {
            System.out.println("Der Graph " + gmlDateiname + " konnte nicht gefunden werden.");
            System.out.println("Die Datei sollte im Verzeichnis scenariosconfig/bienenstock/karten/ liegen.");
        }catch(IOException ioe) {
            ioe.printStackTrace(System.out);
        }
    }
    
    /**
     * Gibt zu der absoluten Zeichenposition in der Datei die zugeh�rige
     * Zeilennummer zur�ck.
     */
    private int holeZeilennummer(int codePosition) {
        int i = 0;
        int indexLaenge = gmlZeilenindex.size();
        while ((i < indexLaenge)
        && (((Integer)gmlZeilenindex.get(i)).intValue()
        <= codePosition)) {
            i = i + 1;
        }
        
        return (i + 1);
    }
    
    /**
     * Testet, ob ein String wie ein Schluessel des GML-Formats aufgebaut ist.
     */
    private boolean istSchluessel(String moeglicherSchluessel) {
        Matcher matcherSchluessel =
                patternSchluessel.matcher(moeglicherSchluessel);
        return matcherSchluessel.matches();
    }
    
    /**
     * Testet, ob ein String wie ein Wert vom Typ Integer des GML-Formats
     * aufgebaut ist.
     */
    private boolean istInteger(String moeglicherInteger) {
        Matcher matcherInteger =
                patternInteger.matcher(moeglicherInteger);
        return matcherInteger.matches();
    }
    
    /**
     * Testet, ob ein String wie ein Wert vom Typ Double des GML-Formats
     * aufgebaut ist.
     */
    private boolean istDouble(String moeglicherDouble) {
        Matcher matcherDouble =
                patternDouble.matcher(moeglicherDouble);
        return matcherDouble.matches();
    }
    
    /**
     * Testet, ob ein String wie ein Wert vom Typ Boolean des GML-Formats
     * aufgebaut ist.
     */
    private boolean istBoolean(String moeglicherBoolean){
        if ((moeglicherBoolean.equals("true")) ||
                (moeglicherBoolean.equals("false"))) return true;
        else return false;
    }
    
    /**
     * Testet, ob ein String wie ein Wert vom Typ String des GML-Formats
     * aufgebaut ist.
     */
    private boolean istString(String moeglicherString) {
        Matcher matcherString =
                patternString.matcher(moeglicherString);
        return matcherString.matches();
    }
    
    /**
     * Sucht ab einer vorgegebenen Position in einer Zeichenkette
     * die Position des n�chsten Zeichens,
     * das nicht mehr das vorgegebene Zeichen ist.
     */
    private int ueberspringeZeichen(int codePosition,
            char zuIgnorieren) {
        int i = codePosition;
        int codelaenge = gmlCode.length();
        while ((i < codelaenge) && (gmlCode.charAt(i) == zuIgnorieren)) {
            i = i + 1;
        }
        return i;
    }
    
    /**
     * Gibt die Zeichenfolge eines Strings ab einer vorgegebenen Position
     * bis zu einem vorgegebenen Begrenzungszeichen zur�ck.
     */
    private String liesStringBisBegrenzer(int codePosition,
            char begrenzer,
            int obergrenze) {
        StringBuffer zeichenkette = new StringBuffer();
        int i = codePosition;
        int codelaenge = gmlCode.length();
        while ((i < codelaenge) && (i <= obergrenze)
        && (gmlCode.charAt(i) != begrenzer)) {
            zeichenkette.append(gmlCode.charAt(i));
            i = i + 1;
        }
        
        return zeichenkette.toString();
    }
    
    /**
     * Wirft eine vereinheitlichte Exception.
     * Bekommt Zeilennummer, Position
     * und eine Beschreibung des Fehlers �bergeben.
     */
    private void gibFehlerAus(int zeilennummer, String nachricht,
            String codeausschnitt) throws Exception {
        StringBuffer fehlerMeldung = new StringBuffer();
        
        fehlerMeldung.append("Zeile ");
        fehlerMeldung.append(zeilennummer);
        fehlerMeldung.append(":\n    ");
        fehlerMeldung.append(nachricht);
        fehlerMeldung.append("\n    ");
        fehlerMeldung.append(codeausschnitt);
        fehlerMeldung.append("\n\n");
        
        throw new Exception(fehlerMeldung.toString());
    }
    
    /**
     * Parst die GML-Zeichenkette, pr�ft die Syntax
     * und erstellt eine �quivalebte Baumstruktur
     * aus Objekten der Klasse GMLBaumknoten.
     */
    private LinkedList erstelleGMLBaum(int startposition, int stopposition)
    throws Exception {
        int codePosition = startposition;
        String aktuellerWert = new String();
        String aktuellerSchluessel = new String();
        boolean schluesselErhalten = false;
        LinkedList aktuelleKnoten = new LinkedList();
        GMLBaumknoten aktuellerKnoten = new GMLBaumknoten();
        
        while ((codePosition <= stopposition) || schluesselErhalten) {
            if (schluesselErhalten) {
                //Zum ersten Zeichen, das kein Leerzeichen ist, springen
                codePosition = ueberspringeZeichen(codePosition, ' ');
                
                //sind wir noch innerhalb der Grenzen?
                if (codePosition > stopposition) {
                    gibFehlerAus(holeZeilennummer(codePosition),
                            "Wert zum Schluessel fehlt "
                            + "- Vorzeitiges Ende der aktuellen Liste!",
                            aktuellerSchluessel);
                    return null;
                }
                
                //Datentyp erkennen: String, Liste, Integer oder Double?
                if (gmlCode.charAt(codePosition) == '"') {
                    codePosition = codePosition + 1;
                    //String zusammensetzen
                    aktuellerWert = liesStringBisBegrenzer(codePosition, '"',
                            stopposition);
                    
                    //codeposition nachkorrigieren
                    codePosition = codePosition + aktuellerWert.length() + 1;
                    
                    //sind wir noch innerhalb der Grenzen?
                    if ((codePosition > stopposition)
                    || (gmlCode.charAt(codePosition - 1) != '"')) {
                        gibFehlerAus(holeZeilennummer(codePosition
                                - aktuellerWert.length() - 2),
                                "Dem zugehoerigen String fehlt das "
                                + "abschliessende Anfuehrungszeichen!",
                                aktuellerSchluessel);
                        return null;
                    } else if (! istString(aktuellerWert)) {
                        gibFehlerAus(holeZeilennummer(codePosition
                                - aktuellerWert.length() - 2),
                                "Zugehoeriger String ist fehlerhaft!",
                                aktuellerSchluessel);
                        return null;
                    } else {
                        aktuellerKnoten.setzeString(aktuellerWert);
                    }
                    
                    
                } else  if (gmlCode.charAt(codePosition) == '[') {
                    int endposition = codePosition + 1;
                    int klammerebene = 1;
                    //Suchen der zugehoerigen schliessenden Klammer
                    while ((endposition <= stopposition)
                    &&  (klammerebene > 0 )) {
                        if (gmlCode.charAt(endposition) == '[') {
                            klammerebene = klammerebene + 1;
                        } else if (gmlCode.charAt(endposition) == ']') {
                            klammerebene = klammerebene - 1;
                        }
                        endposition = endposition + 1;
                    }
                    
                    if (klammerebene == 0) {
                        
                        aktuellerKnoten.setzeListe(erstelleGMLBaum(
                                (codePosition + 1),
                                (endposition - 2)));
                        codePosition = endposition;
                    } else {
                        System.out.println(klammerebene);
                        gibFehlerAus(holeZeilennummer(codePosition),
                                "Der zugehoerigen Liste fehlt die "
                                + "abschliessende Klammer!",
                                aktuellerSchluessel);
                        return null;
                    }
                    
                } else {
                    //Ist es ein Integer oder ein Double?
                    aktuellerWert = liesStringBisBegrenzer(codePosition, ' ',
                            stopposition);
                    
                    //Position korrigieren
                    codePosition = codePosition + aktuellerWert.length();
                    
                    if (istInteger(aktuellerWert)) {
                        aktuellerKnoten.setzeInteger(Integer.parseInt(aktuellerWert));
                    } else if (istDouble(aktuellerWert)) {
                        aktuellerKnoten.setzeDouble(Double.parseDouble(aktuellerWert));
                    } else if (this.istBoolean(aktuellerWert)){
                        boolean wert=Boolean.valueOf(aktuellerWert).booleanValue();
                        aktuellerKnoten.setzeBoolean(wert);
                    } else {
                        gibFehlerAus(holeZeilennummer(codePosition
                                - aktuellerWert.length()),
                                "Wert ist weder Integer noch Double!",
                                aktuellerWert);
                    }
                    
                }
                
                
                aktuelleKnoten.add(aktuellerKnoten);
                schluesselErhalten = false;
            } else {
                /*
                 * Haben wir noch keinen Schl�ssel erhalten,
                 * so muss der aktuelle Token ein Schl�ssel sein,
                 * oder ein Listenende... nicht zu vergessen!
                 * daher pr�fen wir ihn auf Korrektheit
                 */
                //Naechstes Zeichen ermitteln, das kein Leerzeichen ist
                codePosition = ueberspringeZeichen(codePosition, ' ');
                aktuellerSchluessel = liesStringBisBegrenzer(codePosition, ' ',
                        stopposition);
                
                //Codeposition nachkorrigieren
                codePosition = codePosition + aktuellerSchluessel.length();
                
                if(istSchluessel(aktuellerSchluessel)) {
                    aktuellerKnoten = new GMLBaumknoten();
                    aktuellerKnoten.setzeSchluessel(aktuellerSchluessel);
                    schluesselErhalten = true;
                    //System.out.println(aktuellerToken);
                } else if (codePosition > stopposition) {
                    //Am Ende der Liste angekommen. sch�n.
                    
                } else {
                    /*
                     * Schluessel "aktuellerToken" fehlerhaft
                     * Dennoch ein bisschen differenzieren:
                     */
                    if (aktuellerSchluessel.equals("]")) {
                        gibFehlerAus(holeZeilennummer(codePosition
                                - aktuellerSchluessel.length()),
                                "Ueberzaehlige schliessende Klammer!",
                                aktuellerSchluessel);
                    } else {
                        gibFehlerAus(holeZeilennummer(codePosition
                                - aktuellerSchluessel.length()),
                                "Fehlerhafter Schluessel!",
                                aktuellerSchluessel);
                    }
                    return null;
                }
                
            }
            
        }
        
        return aktuelleKnoten;
    }
    
    /**
     * Bekommt den Inhalt eines Knoten in Form einer LinkedList �bergeben
     * und erstellt aus ihm, je nach angegebenen Feldtyp,
     * ein Objekt der Klasse Bienenstock, Blume oder Platz.
     * Kann der angegebene Typ nicht korrekt erkannt werden,
     * so wird der Knoten als Objekt der Klasse Platz verstanden.
     */
    private Feld erstelleFeld(LinkedList knoten) {
        
        //Art des Felds
        String feldTyp = new String();
        //Die Kapazitaeten
        int kapazitaetWartend = 0;
        int kapazitaetFliegend = 0;
        int kapazitaetTanzend = 0;
        int kapazitaetAbbauend = 0;
        //Die Sichtweiten
        int sichtweiteBoden = 0;
        int sichtweiteLuft = 0;
        //Die Koordinate
        int koordinateX = 0;
        int koordinateY = 0;
        //Sonstige Attribute
        int nektar = 0; //Stock & Blume
        int nektarMax = 0; //Stock
        int nektarProRunde = 0; //Blume
        int nektarZuHonig = 0; //Stock
        int honig = 0; //Stock
        int honigMax = 0; //Bienenstock
        int volksID = 0; //Bienenstock
        int merkmal = 0; //Blume
        boolean nektarAuslesbar=false;
        int id = 0; //Eindeutige ID des Feldes f�r die Kanten
        
        int i;
        for (i = 0; i < knoten.size(); i++) {
            if (((GMLBaumknoten)knoten.get(i)).gibSchluessel().equals("id")) {
                id = ((GMLBaumknoten)knoten.get(i)).gibInteger();
            } else if (((GMLBaumknoten)knoten.get(i)).gibSchluessel().equals("kapazitaet")) {
                LinkedList attribute
                        = ((GMLBaumknoten)knoten.get(i)).gibListe();
                
                int e;
                for (e = 0; e < attribute.size(); e++) {
                    if (((GMLBaumknoten)attribute.get(e)).gibSchluessel().equals("wartend")) {
                        kapazitaetWartend
                                = ((GMLBaumknoten)attribute.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)attribute.get(e)).gibSchluessel().equals("fliegend")) {
                        kapazitaetFliegend
                                = ((GMLBaumknoten)attribute.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)attribute.get(e)).gibSchluessel().equals("tanzend")) {
                        kapazitaetTanzend
                                = ((GMLBaumknoten)attribute.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)attribute.get(e)).gibSchluessel().equals("abbauend")) {
                        kapazitaetAbbauend
                                = ((GMLBaumknoten)attribute.get(e)).gibInteger();
                    } else {
                        //Sonstige Schluessel werden ignoriert
                    }
                }
                
            } else if (((GMLBaumknoten)knoten.get(i)).gibSchluessel().equals("sichtweite")) {
                LinkedList attribute
                        = ((GMLBaumknoten)knoten.get(i)).gibListe();
                
                int e;
                for (e = 0; e < attribute.size(); e++) {
                    if (((GMLBaumknoten)attribute.get(e)).gibSchluessel().equals("boden")) {
                        sichtweiteBoden
                                = ((GMLBaumknoten)attribute.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)attribute.get(e)).gibSchluessel().equals("luft")) {
                        sichtweiteLuft
                                = ((GMLBaumknoten)attribute.get(e)).gibInteger();
                    } else {
                        //Sonstige Schluessel werden ignoriert
                    }
                }
            } else if (((GMLBaumknoten)knoten.get(i)).gibSchluessel().equals("koordinate")) {
                LinkedList koordinaten
                        = ((GMLBaumknoten)knoten.get(i)).gibListe();
                
                int e;
                for (e = 0; e < koordinaten.size(); e++) {
                    if (((GMLBaumknoten)koordinaten.get(e)).gibSchluessel().equals("x")) {
                        koordinateX
                                = ((GMLBaumknoten)koordinaten.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)koordinaten.get(e)).gibSchluessel().equals("y")) {
                        koordinateY
                                = ((GMLBaumknoten)koordinaten.get(e)).gibInteger();
                    } else {
                        //Sonstige Schluessel werden ignoriert
                    }
                }
            } else if (((GMLBaumknoten)knoten.get(i)).gibSchluessel().equals("stock")
            || ((GMLBaumknoten)knoten.get(i)).gibSchluessel().equals("blume")
            || ((GMLBaumknoten)knoten.get(i)).gibSchluessel().equals("platz")) {
                feldTyp = ((GMLBaumknoten)knoten.get(i)).gibSchluessel();
                LinkedList merkmale = ((GMLBaumknoten)knoten.get(i)).gibListe();
                
                int e;
                for (e = 0; e < merkmale.size(); e++) {
                    if (((GMLBaumknoten)merkmale.get(e)).gibSchluessel().equals("nektar")) {
                        nektar = ((GMLBaumknoten)merkmale.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)merkmale.get(e)).gibSchluessel().equals("nektarMax")) {
                        nektarMax
                                = ((GMLBaumknoten)merkmale.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)merkmale.get(e)).gibSchluessel().equals("nektarProRunde")) {
                        nektarProRunde
                                = ((GMLBaumknoten)merkmale.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)merkmale.get(e)).gibSchluessel().equals("nektarZuHonig")) {
                        nektarZuHonig
                                = ((GMLBaumknoten)merkmale.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)merkmale.get(e)).gibSchluessel().equals("honig")) {
                        honig = ((GMLBaumknoten)merkmale.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)merkmale.get(e)).gibSchluessel().equals("honigMax")) {
                        honigMax
                                = ((GMLBaumknoten)merkmale.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)merkmale.get(e)).gibSchluessel().equals("volksID")) {
                        volksID = ((GMLBaumknoten)merkmale.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)merkmale.get(e)).gibSchluessel().equals("merkmal")) {
                        merkmal = ((GMLBaumknoten)merkmale.get(e)).gibInteger();
                    } else if (((GMLBaumknoten)merkmale.get(e)).gibSchluessel().equals("nektarAuslesbar")){
                        nektarAuslesbar=((GMLBaumknoten)merkmale.get(e)).gibBoolean();
                    }else {
                        //Sonstige Schluessel werden ignoriert
                    }
                }
                
            } else {
                //Sonstige Schluessel werden ignoriert
            }
        }
        
        if (feldTyp.equals("stock")) {
            return new Bienenstock(
                    id,
                    sichtweiteBoden,
                    sichtweiteLuft,
                    nektarMax,
                    nektar,
                    nektarZuHonig,
                    honig,
                    kapazitaetWartend,
                    kapazitaetFliegend,
                    volksID,
                    new Koordinate(koordinateX, koordinateY),
                    kapazitaetTanzend);
        } else if (feldTyp.equals("blume")) {
            return new Blume(
                    id,
                    sichtweiteBoden,
                    sichtweiteLuft,
                    nektar,
                    nektarProRunde,
                    kapazitaetAbbauend,
                    kapazitaetWartend,
                    kapazitaetFliegend,
                    merkmal,
                    new Koordinate(koordinateX, koordinateY),
                    kapazitaetTanzend,
                    nektarAuslesbar);
        } else {
                /*
                 * Falls der Typ nicht korrekt erkannt werden konnte,
                 * ist es per default ein Platz.
                 */
            return new Platz(
                    id,
                    sichtweiteBoden,
                    sichtweiteLuft,
                    kapazitaetWartend,
                    kapazitaetFliegend,
                    kapazitaetTanzend,
                    new Koordinate(koordinateX, koordinateY));
        }
    }
    
    
    /**
     * Erstellt aus dem GML-Baum die Karte.
     */
    private Hashtable erstelleFrischeKarte(LinkedList kartenGraph) {
        Hashtable frischeKarteNachID = new Hashtable();
        Hashtable frischeKarteNachKoordinate = new Hashtable();
        HashSet frischeKanten = new HashSet();
        this.mindestBlumenAbstand=0;
        /*
         * Erstellen aller Knoten der Karte - alle gefundenen Kanten werden
         * zwischengespeichert und sp�ter in die Karte eingef�gt.
         */
        int i;
        for (i = 0; i < kartenGraph.size(); i++) {
            if (((GMLBaumknoten)kartenGraph.get(i)).gibSchluessel().equals("node")) {
                Feld neuesFeld =
                        erstelleFeld(((GMLBaumknoten)kartenGraph.get(i)).gibListe());
                frischeKarteNachID.put(new Integer(neuesFeld.gibID()),
                        neuesFeld);
            } else if (((GMLBaumknoten)kartenGraph.get(i)).gibSchluessel().equals("edge")) {
                frischeKanten.add(((GMLBaumknoten)kartenGraph.get(i)).gibListe());
            } else if (((GMLBaumknoten)kartenGraph.get(i)).gibSchluessel().equals("blumenMindestAbstand")){
                this.mindestBlumenAbstand=((GMLBaumknoten)kartenGraph.get(i)).gibInteger();
            } else {
                //Sonstige Schluessel werden ignoriert
            }
        }
        
        /*
         * �bertragen der Kanten in die Karte.
         */
        Iterator kantenZugriff = frischeKanten.iterator();
        while (kantenZugriff.hasNext()) {
            int ausgangsknotenID = 0;
            int zielknotenID = 0;
            
            LinkedList aktuelleKante = (LinkedList)kantenZugriff.next();
            
            for (i = 0; i < aktuelleKante.size(); i++) {
                if (((GMLBaumknoten)aktuelleKante.get(i)).gibSchluessel().equals("source")) {
                    ausgangsknotenID = ((GMLBaumknoten)aktuelleKante.get(i)).gibInteger();
                } else if (((GMLBaumknoten)aktuelleKante.get(i)).gibSchluessel().equals("target")) {
                    
                    zielknotenID = ((GMLBaumknoten)aktuelleKante.get(i)).gibInteger();
                } else {
                    //Sonstige Schl�ssel werden ignoriert.
                }
            }
            
            //Die beiden Knoten der Kante suchen
            Feld ausgangsknoten = (Feld)frischeKarteNachID.get(new Integer(ausgangsknotenID));
            Feld zielknoten = (Feld)frischeKarteNachID.get(new Integer(zielknotenID));
            
            if (ausgangsknoten == zielknoten) {
                //Einfach ignorieren
            } else {
                //Da wir von einem ungerichteten Graphen ausgehen:
                ausgangsknoten.trageNachbarfeldEin(zielknoten);
                zielknoten.trageNachbarfeldEin(ausgangsknoten);
            }
            
        }
        
        /*
         * Umwandeln der Hashtable die nach ID verwaltet ist in eine
         * die nach Koordinaten verwaltet.
         */
        Enumeration kartenZugriff = frischeKarteNachID.elements();
        while (kartenZugriff.hasMoreElements()) {
            Feld aktuellesFeld = (Feld)kartenZugriff.nextElement();
            frischeKarteNachKoordinate.put(aktuellesFeld.gibPosition(),
                    aktuellesFeld);
        }
        
        return frischeKarteNachKoordinate;
    }
    
    /**
     * F�hrt die Konvertierung von der GML-Beschreibung in eine Karte aus.
     * Geht von einem ungerichteten Graphen aus, wird daher jede gefundene Kante in beiden Richtungen eintragen.
     */
    private void werteDateiInhaltAus() {
        LinkedList GMLBaum = new LinkedList();
        try {
            GMLBaum = erstelleGMLBaum(0, (gmlCode.length() - 1));
        } catch (Exception codeFehler) {
            System.out.print(codeFehler.getMessage());
        }
        
        LinkedList kartenGraph = new LinkedList();
        LinkedList spielparameter = new LinkedList();
        
        /*
         * Im GML-Baum interessiert uns nur der Graph:
         * (Man k�nnte in sp�teren Versionen
         * Konfigurationen in einer weiteren Liste auf gleicher Ebene
         * mit dem Graph speichern.)
         */
        int i;
        for (i = 0; i < GMLBaum.size(); i++) {
            if (((GMLBaumknoten)GMLBaum.get(i)).gibSchluessel().equals("graph")) {
                kartenGraph = ((GMLBaumknoten)GMLBaum.get(i)).gibListe();
            } else if (((GMLBaumknoten)GMLBaum.get(i)).gibSchluessel().equals("spielparameter")) {
                spielparameter = ((GMLBaumknoten)GMLBaum.get(i)).gibListe();
            } else {
                //Due restlichen Schluessel werden ignoriert
            }
            
        }
        
        
        //szenarioParameter = erstelleFrischeParameter(spielparameter);
        szenarioKarte = erstelleFrischeKarte(kartenGraph);
    }
    
    /**
     * gibt die fertige Spielkarte zur�ck.
     *
     * @return
     */
    Hashtable gibSpielfeld() {
        return szenarioKarte;
    }
    
    public int gibBlumenMindestAbstand(){
        return this.mindestBlumenAbstand;
    }
    
    /**
     * Testroutine zum Ausgeben des gesamten GML-Baums.
     */
    public void gmlBaumAusgeben() {
        gmlBaumTeilAusgeben(gmlBaum, 0);
    }
    
    /**
     * Testroutine zum ausgeben eines Baumeteils vom Typ GMLBaumknoten.
     *
     * @param gmlBaumteil
     * @param baumtiefe
     */
    public void gmlBaumTeilAusgeben(LinkedList gmlBaumTeil, int baumtiefe) {
        GMLBaumknoten aktuellerKnoten;
        StringBuffer abstand = new StringBuffer();
        int i;
        for (i = 0; i <= (baumtiefe * 4); i++) {
            abstand.append(" ");
        }
        
        for (i = 0; i < gmlBaumTeil.size(); i++) {
            System.out.print(abstand);
            aktuellerKnoten = (GMLBaumknoten)gmlBaumTeil.get(i);
            System.out.print(aktuellerKnoten.gibSchluessel());
            System.out.print(" ");
            
            if (aktuellerKnoten.istInteger()) {
                System.out.print(aktuellerKnoten.gibInteger());
                System.out.print("\n");
            } else if (aktuellerKnoten.istDouble()) {
                System.out.print(aktuellerKnoten.gibDouble());
                System.out.print("\n");
            } else if (aktuellerKnoten.istString()) {
                System.out.print("\"");
                System.out.print(aktuellerKnoten.gibString());
                System.out.print("\"\n");
            } else if (aktuellerKnoten.istListe()) {
                System.out.print("[\n");
                gmlBaumTeilAusgeben(aktuellerKnoten.gibListe(), (baumtiefe + 1));
                System.out.print(abstand);
                System.out.print("]\n");
            } else {
                //Echter Fehler!
            }
        }
        
    }
    
    /**
     * Testprozedur um den GMLParser unabh�ngig vom Szenario testen zu k�nnen.
     * Daf�r notwendig ist nur die Klasse GMLBaumknoten und eine GML-Datei
     * deren Name als erstes Argument beim Aufruf �bergeben wird.
     */
    public static void main(String[] args) {
        System.out.println("Erstelle Spielfeld...");
        GMLParser testParser = new GMLParser(args[0]);
        System.out.println("Gebe Spielfeld aus...");
        Hashtable testSpielfeld = testParser.gibSpielfeld();
        testParser.gmlBaumAusgeben();
        System.out.println("Fertig.");
    }
    
    
    
}
