/*
 * Dateiname      : Konstanten.java
 * Erzeugt        : 18. Oktober 2004
 * Letzte Änderung: 26. Januar 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
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

package scenarios.bienenstock.scenariomanagement;

/**
 * enthält alle Konstanten des Szenarios.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Konstanten {
    
    /**
     * Die Konstante für den Zustand "Fliegend".
     */
    public static final int FLIEGEND = 0;
    
    /**
     * Die Konstante für den Zustand "Wartend".
     */
    public static final int WARTEND = 1;
    
    /**
     * Die Konstante für den Zustand "Tanzend".
     */
    public static final int TANZEND = 2;
    
    /**
     * Die Konstante für den Zustand "Zuschauend".
     */
    public static final int ZUSCHAUEND = 3;
    
    /**
     * Die Konstante für den Zustand "Abbauend".
     */
    public static final int ABBAUEND = 4;
    
    /**
     * Die Konstante für den Zustand Tanken oder Abliefern
     */
    public static final int SONSTIGER=5;
    /**
     * Die Konstante für die Startphase.
     */
    public static final int STARTPHASE = 0;
    
    /**
     * Die Konstante für die Wartephase.
     */
    public static final int WARTEPHASE = 1;
    
    /**
     * Die Konstante für die Bearbeitungsphase.
     */
    public static final int BEARBEITUNGSPHASE = 2;
    
    /**
     * Die Konstante für die Endphase.
     */
    public static final int ENDPHASE = 3;
    
    /**
     * Der Konstruktor.
     */
    public Konstanten() {
    }
}
