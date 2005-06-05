/*
 * Dateiname      : EinfacheFeld.java
 * Erzeugt        : 6. Oktober 2004
 * Letzte Änderung: 14. Februar 2005 durch Philip Funck
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
package scenarios.bienenstock.einfacheUmgebung;
import scenarios.bienenstock.agenteninfo.Koordinate;
import java.util.HashSet;
import java.util.HashMap;

/**
 * vereinfachte Darstellung des Feldes.
 *
 * @author Philip Funck
 */
public class EinfachesFeld {
    
    /**
     * die Position des Feldes auf der Karte.
     */
    private Koordinate position;
    
    /**
     * die direkt angrenzenden Nachbarfelder des Feldes.
     *
     * @associates scenario.bienenstock.einfacheUmgebung.EinfachesFeld
     */
    private HashMap nachbarfelder;
    
    /**
     * Liste aller auf dem Feld wartenden Bienen.
     *
     * @associates java.lang.Integer
     */
    private HashSet wartendeBienen;
    
    /**
     * Liste aller über dem Feld fliegenden Bienen.
     *
     * @associates java.lang.Integer
     */
    private HashSet fliegendeBienen;
    
    /**
     * Liste aller auf dem Feld tanzenden Bienen.
     *
     * @associates java.lang.Integer
     */
    private HashSet tanzendeBienen;
    
    /**
     * Konstrukotor.
     *
     * @param pos       Position des Feldes auf der Karte
     * @param wBienen   am Feld wartende Bienen
     * @param fBienen   über dem Feld fliegende Bienen
     * @param tBienen   am Feld tanzende Bienen
     */
    public EinfachesFeld(Koordinate pos,
            HashSet wBienen,
            HashSet fBienen,
            HashSet tBienen) {
        position = pos;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;
    }
    
    /**
     * setzt die angrenzenden Nachbarfelder des Feldes.
     *
     * @param nachbarn  Liste aller angrenzenden Nachbarfelder
     */
    public void setzeNachbarfelder(HashMap nachbarn) {
        nachbarfelder = nachbarn;
    }
    
    /**
     * gibt die Position des Feldes auf der Karte zurück.
     *
     * @return Koordinate
     */
    public Koordinate gibPosition() {
        return position;
    }
    
    /**
     * gibt eine Liste der direkt an das Feld angrenzenden Nachbarfelder zurück.
     *
     * @return HashMap
     */
    public HashMap gibNachbarfelder() {
        return nachbarfelder;
    }
    
    /**
     * gibt eine Liste der IDs aller auf dem Feld wartenden Bienen zurück.
     *
     * @associates java.lang.Integer
     * @return HashSet
     */
    public HashSet gibIDsWartendeBienen() {
        return (HashSet) wartendeBienen.clone();
    }
    
    /**
     * gibt eine Liste der IDs aller über dem Feld fliegenden Bienen zurück.
     *
     * @associates java.lang.Integer
     * @return HashSet
     */
    public HashSet gibIDsFliegendeBienen() {
        return (HashSet) fliegendeBienen.clone();
    }
    
    /**
     * gibt eine Liste der IDs aller auf dem Feld tanzenden Bienen zurück.
     *
     * @associates java.lang.Integer
     * @return HashSet
     */
    public HashSet gibIDsTanzendeBienen() {
        return (HashSet) tanzendeBienen.clone();
    }
}
