/*
 * Dateiname      : StatistikBienenStock.java
 * Erzeugt        : 20. Januar 2005
 * Letzte Änderung: 20. Januar 2005
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

import java.util.ArrayList;
import scenarios.bienenstock.umgebung.Bienenstock;
import scenarios.bienenstock.umgebung.Bienenstock;
import scenarios.bienenstock.agenteninfo.Koordinate;

/**
 * speichert die Werte der Biene zu jeder Runde.
 *
 * Zählt bei jedem Eintragen von Werten der Biene intern die Rundennummer hoch.
 * @author Philip Funck
 * @author Samuel Walz
 */
public class StatistikBienenstock {
    public StatistikBienenstock(int bieneID) {
    }
    
    public int gelagerterHonig(int runde) {
        return 0;
    }
    
    public int gelagerterNektar(int runde) {
        return 0;
    }
    
    public Koordinate position(int runde) {
        return null;
    }
    
    /**
     * liest von der zu beobachtenden Biene die Werte ein und speichert diese in einem neuen Eintrag.
     */
    public void rundenwerteSetzen(    ) {
    }
    
    public int volksID() {
        return 0;
    }
    
    private ArrayList werteliste;
    private Bienenstock zuBeobachtenderStock;
    private int volksID;
    
    public class werteBienenstock {
        private int gelagerterHonig;
        private int gelagerterNektar;
    }
}
