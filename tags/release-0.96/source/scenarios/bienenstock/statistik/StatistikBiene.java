/*
 * Dateiname      : StatistikBiene.java
 * Erzeugt        : 20. Januar 2005
 * Letzte Änderung: 14. Februar 2005 durch Philip Funck
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

package scenarios.bienenstock.statistik;

import scenarios.bienenstock.agenteninfo.Koordinate;
import nereus.utils.Id;

/**
 * speichert die Werte der Biene zu jeder Runde.
 *
 * Zählt bei jedem Eintragen von Werten der Biene intern die Rundennummer hoch.
 * @author Philip Funck
 * @author Samuel Walz
 */
public class StatistikBiene {
    
    /**
     * ID der zugehörigen Biene.
     */
    private Id bienenID;
    
    /**
     * Menge des geladenen Honigs.
     */
    private int honig;
    
    /**
     * Menge des geladenen Nektars.
     */
    private int nektar;
    
    /**
     * Nummer der aktuellen Runde.
     */
    private int runde;
    
    /**
     * Zustand der zugehörigen Biene.
     */
    private String zustand;
    
    /**
     * Der Konstruktor.
     *
     * @param id        ID der Biene
     * @param sHonig    geladener Honig der Biene
     * @param sNektar   geladener Nektar der Biene
     * @param sZustand  Zustand der Biene
     */
    public StatistikBiene(Id id, int sHonig, int sNektar, String sZustand) {
        honig = sHonig;
        nektar = sNektar;
        bienenID = id;
        zustand = sZustand;
    }
    
    /**
     * Konstruktor.
     *
     * @param id  Die ID der Biene
     */
    public StatistikBiene(Id id) {
        bienenID = id;
        honig = 0;
        nektar = 0;
        zustand = "";
    }
    
    /**
     * gibt die Menge des geladenen Honigs zurück.
     *
     * @return   eine positive ganze Zahl
     */
    public int gibHonig() {
        return honig;
    }
    
    /**
     * gibt die Menge des geladenen Nektars zurück.
     *
     * @return    eine positive ganze Zahl
     */
    public int gibNektar() {
        return nektar;
    }
    
    /**
     * gibt die Position der Biene auf der Karte zurück.
     *
     * @return  die x-und die y- Koordinate der Position
     */
    public Koordinate gibPosition() {
        return null;
    }
    
    /**
     * gibt den Zustand der Biene zurück.
     *
     * @return  Der Zustand der Biene als Wort
     */
    public String gibZustand() {
        return zustand;
    }
    
    /**
     * gibt die ID der zugehörigen Biene zurück.
     *
     * @return   eine positive ganze Zahl
     */
    public Id gibID() {
        return bienenID;
    }
    
    /**
     * enthält die Werte der zugehörigen Biene.
     *
     * @author Philip Funck
     */
    public class WerteBiene {
        /**
         * menge an geladenen Honig
         */
        private int geladenerHonig;
        /**
         * Menge an geladene Nektar
         */
        private int geladenerNektar;
        /**
         * Position der Biene
         */
        private int position;
        /**
         * Status der Biene
         */
        private int status;
    }
}
