/*
 * Dateiname      : GMLBaumknoten.java
 * Erzeugt        : 31. Oktober 2004
 * Letzte Änderung: 26. Januar 2005 durch Samuel Walz
 *
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *                  
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
