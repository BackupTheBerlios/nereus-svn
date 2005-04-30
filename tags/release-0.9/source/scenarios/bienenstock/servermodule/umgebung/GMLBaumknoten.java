/*
 * Erzeugt        : 31. Oktober 2004
 * Letzte �nderung: 26. Januar 2005 durch Samuel Walz
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
package scenario.bienenstock.umgebung;
import java.util.LinkedList;

/**
 * ein Knoten im GML-Baum.
 *
 * @author Samuel Walz
 */
public class GMLBaumknoten {

    /**
     * GML-Schl�ssel.
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
     * H�lt fest, ob der Wert zum Repr�sentierten GML-Schl�ssel
     * als Integer gespeichert wurde.
     */
    private boolean integerGesetzt = false;

    /**
     * H�lt fest, ob der Wert zum Repr�sentierten GML-Schl�ssel
     * als Double gespeichert wurde.
     */
    private boolean doubleGesetzt = false;

    /**
     * H�lt fest, ob der Wert zum Repr�sentierten GML-Schl�ssel
     * als String gespeichert wurde.
     */
    private boolean stringGesetzt = false;

    /**
     * H�lt fest, ob der Wert zum Repr�sentierten GML-Schl�ssel
     * als Liste gespeichert wurde.
     */
    private boolean listeGesetzt = false;

    /**
     * Speichert einen GML-Schl�ssel.
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
     * Gibt zur�ck, ob der GML-Wert als Integer gespeichert wurde.
     */
    boolean istInteger() {
        return integerGesetzt;
    }

    /**
     * Gibt zur�ck, ob der GML-Wert als Double gespeichert wurde.
     */
    boolean istDouble() {
        return doubleGesetzt;
    }

    /**
     * Gibt zur�ck, ob der GML-Wert als String gespeichert wurde.
     */
    boolean istString() {
        return stringGesetzt;
    }

    /**
     * Gibt zur�ck, ob der GML-Wert als Liste gespeichert wurde.
     */
    boolean istListe() {
        return listeGesetzt;
    }

    /**
     * Gibt den GML-Schl�ssel zur�ck.
     */
    String gibSchluessel() {
        return wertSchluessel;
    }

    /**
     * Gibt den GML-Wert vom Typ Integer zur�ck.
     */
    int gibInteger() {
        return wertInteger;
    }

    /**
     * Gibt den GML-Wert vom Typ Double zur�ck.
     */
    double gibDouble() {
        return wertDouble;
    }

    /**
     * Gibt den GML-Wert vom Typ String zur�ck.
     */
    String gibString() {
        return wertString;
    }

    /**
     * Gibt den GML-Wert vom Typ Liste zur�ck.
     */
    LinkedList gibListe() {
        return wertListe;
    }


}
