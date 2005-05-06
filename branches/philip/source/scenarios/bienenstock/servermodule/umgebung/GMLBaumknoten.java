/*
 * Erzeugt        : 31. Oktober 2004
 * Letzte Änderung: 26. Januar 2005 durch Samuel Walz
 *
 * Teil des Softwarepraktikums mit dem Titel
 *
 *   "Design und Implementierung eines Szenarios für einen
 *    Multiagenten-Simulator"
 *
 * bei Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de)
 *
 * Autoren  : Philip Funck (mango.3@gmx.de)
 *            Samuel Walz (felix-kinkowski@gmx.net)
 *
 * copyright: Institut für Intelligente Systeme, Universität Stuttgart (2004)
 *            http://www.iis.uni-stuttgart.de
 */
package scenario.bienenstock.umgebung;
import java.util.LinkedList;

/**
 * ein Knoten im GML-Baum.
 *
 * @author Samuel Walz
 */
public class GMLBaumknoten {

    /**
     * GML-Schlüssel.
     */
    private String wertSchluessel;

    /**
     * GML-Wert vom Typ Integer.
     */
    private int wertInteger = 0;

    /**
     * GML-Wert vom Typ Double.
     */
    private double wertDouble = 0.0;

    /**
     * GML-Wert vom Typ String.
     */
    private String wertString = null;

    /**
     * GML-Wert vom Typ Liste.
     */
    private LinkedList wertListe = null;

    /**
     * Hält fest, ob der Wert zum Repräsentierten GML-Schlüssel
     * als Integer gespeichert wurde.
     */
    private boolean integerGesetzt = false;

    /**
     * Hält fest, ob der Wert zum Repräsentierten GML-Schlüssel
     * als Double gespeichert wurde.
     */
    private boolean doubleGesetzt = false;

    /**
     * Hält fest, ob der Wert zum Repräsentierten GML-Schlüssel
     * als String gespeichert wurde.
     */
    private boolean stringGesetzt = false;

    /**
     * Hält fest, ob der Wert zum Repräsentierten GML-Schlüssel
     * als Liste gespeichert wurde.
     */
    private boolean listeGesetzt = false;

    /**
     * Speichert einen GML-Schlüssel.
     */
    void setzeSchluessel(String schluessel) {
        wertSchluessel = schluessel;
    }

    /**
     * Speichert einen GML-Wert vom Typ Integer.
     */
    void setzeInteger(int wert) {
        wertInteger = wert;
        integerGesetzt = true;
    }

    /**
     * Speichert einen GML-Wert vom Typ Double.
     */
    void setzeDouble(double wert) {
        wertDouble = wert;
        doubleGesetzt = true;
    }

    /**
     * Speichert einen GML-Wert vom Typ String.
     */
    void setzeString(String wert) {
        wertString = wert;
        stringGesetzt = true;
    }

    /**
     * Speichert einen GML-Wert vom Typ Liste.
     */
    void setzeListe(LinkedList wert) {
        wertListe = wert;
        listeGesetzt = true;
    }

    /**
     * Gibt zurück, ob der GML-Wert als Integer gespeichert wurde.
     */
    boolean istInteger() {
        return integerGesetzt;
    }

    /**
     * Gibt zurück, ob der GML-Wert als Double gespeichert wurde.
     */
    boolean istDouble() {
        return doubleGesetzt;
    }

    /**
     * Gibt zurück, ob der GML-Wert als String gespeichert wurde.
     */
    boolean istString() {
        return stringGesetzt;
    }

    /**
     * Gibt zurück, ob der GML-Wert als Liste gespeichert wurde.
     */
    boolean istListe() {
        return listeGesetzt;
    }

    /**
     * Gibt den GML-Schlüssel zurück.
     */
    String gibSchluessel() {
        return wertSchluessel;
    }

    /**
     * Gibt den GML-Wert vom Typ Integer zurück.
     */
    int gibInteger() {
        return wertInteger;
    }

    /**
     * Gibt den GML-Wert vom Typ Double zurück.
     */
    double gibDouble() {
        return wertDouble;
    }

    /**
     * Gibt den GML-Wert vom Typ String zurück.
     */
    String gibString() {
        return wertString;
    }

    /**
     * Gibt den GML-Wert vom Typ Liste zurück.
     */
    LinkedList gibListe() {
        return wertListe;
    }


}
