/*
 * Dateiname      : VisKarte.java
 * Erzeugt        : 6. Oktober 2004
 * Letzte Änderung: 08. Juni 2005 durch Samuel Walz
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (samuel@gmx.info)
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

package scenarios.bienenstock.visualisierungsUmgebung;

import java.util.HashMap;
import java.io.Serializable;

import scenarios.bienenstock.agenteninfo.Koordinate;


/**
 * Die Spielkartendarstellung für die Visualisierung.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class VisKarte implements Serializable{
    /**
     * Alle anwesenden Bienen.
     */
    private HashMap bienen;
    
    /**
     * Das Spielfeld für die Visualisierung.
     *
     * @associates VisFeld
     */
    private HashMap spielfeld;
    
    /**
     * Nummer der aktuellen Runde.
     */
    private int rundennummer;
    
    
    /**
     * Liste aller Kosten für die Aktionen der Bienen.
     */
    private HashMap kosten;
    
    /**
     * Konstruktor.
     *
     * @param runde die aktuelle Runde
     * @param bienchen  die Bienen
     * @param feldchen  die Felder
     * @param kostenvoranschlag Kosten für die Aktionen
     */
    public VisKarte(int runde,
            HashMap bienchen,
            HashMap feldchen,
            HashMap kostenvoranschlag) {
        rundennummer = runde;
        bienen = bienchen;
        spielfeld = feldchen;
        kosten = kostenvoranschlag;
    }
    
    /**
     * gibt eine Liste der Kosten für alle Aktionen zurück.
     *
     * @return Kosten  für die Aktionen
     */
    public HashMap gibHonigkosten() {
        return kosten;
    }
    
    /**
     * gibt eine Biene zurück, die in der Liste bienen mit bieneID
     * zu finden ist.
     *
     * @param bieneID ID der Biene
     * @return eine Vis Biene
     */
    public VisBiene gibBiene(int bieneID) {
        if (bienen.containsKey(new Integer(bieneID))) {
            return (VisBiene)bienen.get(new Integer(bieneID));
        } else {
            //Wurde Biene nicht gefunden, so gib nichts zurück (null)
            return null;
        }
    }
    
    /**
     * gibt eine Liste aller anwesenden Bienen zurück.
     *
     * @return alle Bienen
     */
    public HashMap gibBienen() {
        return bienen;
    }
    
    /**
     * gibt ein Feld mit den Koordinaten ort zurück.
     *
     * @param ort zu suchendes Feld
     * @return  das Feld was gesucht werden sollte, wenn nicht vorhanden null
     */
    public VisFeld gibFeld(Koordinate ort) {
        if (spielfeld.containsKey(ort)) {
            return (VisFeld)spielfeld.get(ort);
        } else {
            //Wurde das Feld nicht gefunden, so gib nichts zurück (null)
            return null;
        }
    }
    
    /**
     * gibt das Spielfeld für die Visualisierung zurück.
     *
     * @return alle Felder
     */
    public HashMap gibFelder() {
        return spielfeld;
    }
}
