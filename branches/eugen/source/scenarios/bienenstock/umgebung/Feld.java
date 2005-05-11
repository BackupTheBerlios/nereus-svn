/*
 * Dateiname      : Feld.java
 * Erzeugt        : 19. Mai 2004
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

import java.util.HashSet;
import scenarios.bienenstock.agenteninfo.Koordinate;
import java.util.Iterator;

/**
 * repräsentiert das allgemeine Feld.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Feld {
    /**
     * Die IDdes Feldes
     */
    private int id;
    
    /**
     * Die Position des Feldes auf der Spielkarte.
     */
    private Koordinate position;
    
    /**
     * ist eine Liste de Angrenzenden Felder.
     *
     * @associates umgebung.Feld
     */
    private HashSet nachbarfelder = new HashSet();
    
    /**
     * legt fest, wie weit die Biene am Boden sehen kann.
     */
    private int sichtweiteAmBoden;
    
    /**
     * legt fest, wie weit die Biene in der Luft sehen kann.
     */
    private int sichtweiteInDerLuft;
    
    /**
     * beschreibt das Maximum der zeitgleich wartenden Biene auf dem Feld.
     */
    private int maximumWartendeBienen;
    
    /**
     * beschreibt das Maximum der zeitgleich fliegenden Biene auf dem Feld.
     */
    private int maximumFliegendeBienen;
    
    /**
     * beschreibt das Maximum der zeitgleich tanzenden Biene auf dem Feld.
     */
    private int maximumTanzendeBienen;
    
    /**
     * ist eien Liste der auf dem Feld Wartenden oder zuhörenden Bienen.
     *
     * @associates scenario.bienenstock.umgebung.Biene
     */
    private HashSet wartendeBienen = new HashSet();
    
    /**
     * ist eine Liste der über dem Feld fliegenden Bienen.
     * @associates scenario.bienenstock.umgebung.Biene
     */
    private HashSet fliegendeBienen = new HashSet();
    
    /**
     * ist eine Liste der auf dem Feld tanzenden Bienen.
     *
     * @associates scenario.bienenstock.umgebung.Biene
     */
    private HashSet tanzendeBienen = new HashSet();
    
    /**
     * Konstruktor.
     *
     * @param eigenID       die Id der Biene
     * @param sichtBoden    die sichtweite am Boden
     * @param sichtLuft     die Sichtweite in der Luft
     * @param maxWartendeBienen Maximum an wartenden Bienen
     * @param maxFliegendeBienen    Maximum an fliegenden Bienen
     * @param maxTanzendeBienen     Maximum an tanzenden Bienen
     * @param feldPosition      die Position des Feldes
     */
    Feld(int eigenID,
            int sichtBoden,
            int sichtLuft,
            int maxWartendeBienen,
            int maxFliegendeBienen,
            int maxTanzendeBienen,
            Koordinate feldPosition) {
        id = eigenID;
        sichtweiteAmBoden = sichtBoden;
        sichtweiteInDerLuft = sichtLuft;
        maximumWartendeBienen = maxWartendeBienen;
        maximumFliegendeBienen = maxFliegendeBienen;
        maximumTanzendeBienen = maxTanzendeBienen;
        position = feldPosition;
    }
    
    /**
     * trägt die Nachbarfelder des Feldes ein.
     *
     * @param nachbarfeld das einzutragende Nachbarfeld
     */
    void trageNachbarfeldEin(Feld nachbarfeld) {
        nachbarfelder.add(nachbarfeld);
    }
    
    /**
     * trägt die an dem Feld wartenden Bienen ein.
     *
     * @param wartendeBiene die wartende Biene
     * @return  true falls sie eingetragen werden konnte
     */
    boolean trageWartendeBieneEin(Biene wartendeBiene) {
        if (maximumWartendeBienen < 0) {
            wartendeBienen.add(wartendeBiene);
            return true;
        } else if (wartendeBienen.size() < maximumWartendeBienen) {
            wartendeBienen.add(wartendeBiene);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * trägt die über dem Feld fliegenden Bienen ein.
     *
     * @param fliegendeBiene    einzutragende Biene
     * @return  true falls sie eingetragen werden konnte
     */
    boolean trageFliegendeBieneEin(Biene fliegendeBiene) {
        if (maximumFliegendeBienen < 0) {
            fliegendeBienen.add(fliegendeBiene);
            return true;
        } else if (fliegendeBienen.size() < maximumFliegendeBienen) {
            fliegendeBienen.add(fliegendeBiene);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * trägt die auf dem Feld tanzenden Bienen ein.
     *
     * @param tanzendeBiene die einzutragende Biene
     * @return  true falls sie eingetragen werden konnte
     */
    boolean trageTanzendeBieneEin(Biene tanzendeBiene) {
        if (maximumTanzendeBienen < 0) {
            tanzendeBienen.add(tanzendeBiene);
            return true;
        } else if (tanzendeBienen.size() < maximumTanzendeBienen) {
            tanzendeBienen.add(tanzendeBiene);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * prüft ob die entsprechende Biene auf der Liste wartendeBienen
     * eingetragen ist und entfernt sie gegebenenfalls.
     *
     * Die Operation gibt zurück ob die Biene entfernt werden konnte.
     *
     * @param wartendeBiene zu löschende Biene
     * @throws RuntimeException Biene konnte nicht gelöscht werden
     */
    void entferneWartendeBiene(Biene wartendeBiene)
    throws RuntimeException {
        if (!wartendeBienen.remove(wartendeBiene)) {
            throw new RuntimeException("Wartende Biene existiert nicht "
                    + "- kann nicht geloescht werden.");
        }
    }
    
    /**
     * prüft ob die entsprechende Biene auf der Liste fliegendeBienen
     * eingetragen ist und entfernt sie gegebenenfalls.
     *
     * Die Operation gibt zurück ob die Biene entfernt werden konnte.
     *
     * @param fliegendeBiene    zu löschende Biene
     * @throws RuntimeException konnte nicht eingetragen werden
     */
    void entferneFliegendeBiene(Biene fliegendeBiene)
    throws RuntimeException {
        if (!fliegendeBienen.remove(fliegendeBiene)) {
            throw new RuntimeException("Fliegende Biene existiert nicht "
                    + "- kann nicht geloescht werden.");
        }
    }
    
    /**
     * prüft ob die entsprechende Biene auf der Liste tanzendeBienen
     * eingetragen ist und entfernt sie gegebenenfalls.
     *
     * Die Operation gibt zurück ob die Biene entfernt werden konnte.
     *
     * @param tanzendeBiene zu öschende Biene
     * @throws RuntimeException konnte nicht eingtragen werden
     */
    void entferneTanzendeBiene(Biene tanzendeBiene)
    throws RuntimeException {
        if (!tanzendeBienen.remove(tanzendeBiene)) {
            throw new RuntimeException("Tanzende Biene existiert nicht "
                    + "- kann nicht geloescht werden.");
        }
    }
    
    /**
     * gibt die Sichtweite am Boden zurück.
     *
     * @return  gibt die Sichtweite am Boden zurück
     */
    int gibSichtweiteAmBoden() {
        return sichtweiteAmBoden;
    }
    
    /**
     * gibt die Sichtweite in der Luft zurück.
     *
     * @return  gibt die Sichtweite in der Luft zurück
     */
    int gibSichtweiteInDerLuft() {
        return sichtweiteInDerLuft;
    }
    
    /**
     * gibt eine Liste der dort wartenden Bienen zurück.
     *
     * @return  gibt ein Hash mit den wartenden Bienen zurück
     */
    HashSet gibWartendeBienen() {
        return (HashSet) wartendeBienen.clone();
    }
    
    /**
     * gibt eine Liste der dort fliegenden Bienen zurück.
     *
     * @return  gibt ein Hash mit den fliegenden Bienen zurück
     */
    HashSet gibFliegendeBienen() {
        return (HashSet) fliegendeBienen.clone();
    }
    
    /**
     * gibt eine Liste der dort tanzenden Bienen zurück.
     *
     * @return  gibt ein hash mit den tanzenden Bienen zurück
     */
    HashSet gibTanzendeBienen() {
        return (HashSet) tanzendeBienen.clone();
    }
    
    /**
     * gibt die ID des Feldes zurück.
     *
     * @return  Id des Feldes
     */
    int gibID() {
        return id;
    }
    
    /**
     * gibt die Position des Feldes auf der Spielkarte zurück.
     *
     * @return  Koordinate des Feldes
     */
    Koordinate gibPosition() {
        return position;
    }
    
    /**
     * gibt die direkt angrenzenden Nachbarfelder des Feldes zurück.
     *
     * @return  Hash mit den Nachbarfeldern
     */
    HashSet gibNachbarfelder() {
        return (HashSet) nachbarfelder.clone();
    }
    
    /**
     * trägt die Biene unter allen Umständen in das Feld ein.
     *
     * @param einzutragendeBiene    die einzutragende Biene
     */
    void unbedingtesEintragen(Biene einzutragendeBiene) {
        wartendeBienen.add(einzutragendeBiene);
    }
    
    /**
     * sucht ein Feld in den Nachbarfeldern
     *
     * @param zuSuchen zu suchende Feld als Koordinate
     * @return   gibt das Feld zurück
     * @throws java.lang.RuntimeException    Fehler
     */
    Feld feldInNachbarfeldernSuchen(Koordinate zuSuchen)
    throws java.lang.RuntimeException {
        Iterator zugriffNachbarfelder = nachbarfelder.iterator();
        while (zugriffNachbarfelder.hasNext()) {
            Feld aktuellesFeld = (Feld) zugriffNachbarfelder.next();
            if (aktuellesFeld.gibPosition().equals(zuSuchen)) {
                return aktuellesFeld;
            }
        }
        
        //Wird Feld nicht gefunden, so gib nichts zurück (null)
        return null;
    }
}
