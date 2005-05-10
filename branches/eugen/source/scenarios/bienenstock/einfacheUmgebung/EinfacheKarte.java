/*
 * Dateiname      : EinfacheKarte.java
 * Erzeugt        : 6. Oktober 2004
 * Letzte �nderung: 14. Februar 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
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


package scenarios.bienenstock.einfacheUmgebung;
import java.util.Hashtable;

/**
 * eine vereinfachte Darstellung der Spielkarte f�r den Agenten.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
public class EinfacheKarte {
    
    /**
     * vereinfachte Darstellung des Agenten.
     */
    private EinfacheBiene selbst;

    /**
     * der f�r den Agenten sichtbare Ausschnitt der Spielkarte.
     * 
     * @associates EinfachesFeld
     */
    private Hashtable spielfeldAusschnitt;
    
    /**
     * Konstruktor.
     * 
     * @param selber    die vereinfachte Darstellung des Agenten
     * @param felder    die f�r den Agenten aktuell sichtbaren Felder der
     *                  Spielkarte
     */
    public EinfacheKarte(EinfacheBiene selber, Hashtable felder) {
        selbst = selber;
        spielfeldAusschnitt = felder;
    }

    /**
     * Gibt die vereinfachte Darstellung des Agenten zur�ck.
     * 
     * @return EinfacheBiene
     */
    public EinfacheBiene gibSelbst() {
        return selbst;
    }

    /**
     * gibt eine Liste aller f�r den Aganten sichtbaren Felder zur�ck.
     * 
     * @return Hashtable
     */
    public Hashtable gibFelder() {
        return spielfeldAusschnitt;
    }

}
