/*
 * Dateiname      : Blume.java
 * Erzeugt        : 19. Mai 2004
 * Letzte Änderung: 21. Juni 2005 durch Eugen Volk
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

import scenarios.bienenstock.agenteninfo.Koordinate;
import java.util.HashSet;

/**
 * repräsentiert eine Blume.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Blume extends Feld {
    
    /**
     * ist das äußere Merkmal der Blume.
     */
    private int merkmal;
    
    /**
     * ist die vorhandene Nektarmenge.
     */
    private int vorhandenerNektar;
    
    private boolean nektarAuslesbar=false;
    
    /**
     * ist das Maximum, das von einer Biene pro Runde bezogen werden kann.
     */
    private int maximumNektarProRunde;
    
    /**
     * ist das Maximum an Bienen, die Zeitgleich Nektar abbauen können.
     */
    private int maximumAbbauendeBienen;
    
    /**
     * ist ene Liste aller zur Zeit Nektar abbauenden Bienen.
     * @associates scenario.bienenstock.umgebung.Biene
     */
    private HashSet abbauendeBienen = new HashSet();
    
    /**
     * Konstruktor.
     *
     * @param eigenID   die ID
     * @param sichtBoden    die sichtweite am Boden
     * @param sichtLuft     die Sichtweite in der Luft
     * @param vorhNektar    die vorhndene Nektarmenge
     * @param maxNektarProRunde maximale Nektarmenge pro runde
     * @param maxAbbauendeBienen    maximum abbauende Bienen
     * @param maxWartendeBienen     maximum wartende Bienen
     * @param maxFliegendeBienen    maximum fliegende Bienen
     * @param blumenMerkmal merkmal der Blume
     * @param feldPosition  koordinate der Blume
     * @param maxTanzendeBienen maximum der tanzenden Bienen
     * @param nektarAuslesenLassen erlaubt den Bienen die Information über den Nektargehalt auszulesen.
     */
    Blume(int eigenID,
            int sichtBoden,
            int sichtLuft,
            int vorhNektar,
            int maxNektarProRunde,
            int maxAbbauendeBienen,
            int maxWartendeBienen,
            int maxFliegendeBienen,
            int blumenMerkmal,
            Koordinate feldPosition,
            int maxTanzendeBienen,
            boolean nektarAuslesbar) {
        super(eigenID,
                sichtBoden,
                sichtLuft,
                maxWartendeBienen,
                maxFliegendeBienen,
                maxTanzendeBienen,
                feldPosition);
        /*id = eigenID;
        sichtweiteAmBoden = sichtBoden;
        sichtweiteInDerLuft = sichtLuft;
        maximumWartendeBienen = maxWartendeBienen;
        maximumFliegendeBienen = maxFliegendeBienen;
        maximumTanzendeBienen = maxTanzendeBienen;
        position = feldPosition;*/
        
        merkmal = blumenMerkmal;
        vorhandenerNektar = vorhNektar;
        maximumNektarProRunde = maxNektarProRunde;
        maximumAbbauendeBienen = maxAbbauendeBienen;
        this.nektarAuslesbar=nektarAuslesbar;
    }
    
    /**
     * Konstruktor.
     *
     * @param eigenID   die ID
     * @param sichtBoden    die sichtweite am Boden
     * @param sichtLuft     die Sichtweite in der Luft
     * @param vorhNektar    die vorhndene Nektarmenge
     * @param maxNektarProRunde maximale Nektarmenge pro runde
     * @param maxAbbauendeBienen    maximum abbauende Bienen
     * @param maxWartendeBienen     maximum wartende Bienen
     * @param maxFliegendeBienen    maximum fliegende Bienen
     * @param blumenMerkmal merkmal der Blume
     * @param feldPosition  koordinate der Blume
     * @param maxTanzendeBienen maximum der tanzenden Bienen
     * 
     */
    Blume(int eigenID,
            int sichtBoden,
            int sichtLuft,
            int vorhNektar,
            int maxNektarProRunde,
            int maxAbbauendeBienen,
            int maxWartendeBienen,
            int maxFliegendeBienen,
            int blumenMerkmal,
            Koordinate feldPosition,
            int maxTanzendeBienen) {
        super(eigenID,
                sichtBoden,
                sichtLuft,
                maxWartendeBienen,
                maxFliegendeBienen,
                maxTanzendeBienen,
                feldPosition);
        /*id = eigenID;
        sichtweiteAmBoden = sichtBoden;
        sichtweiteInDerLuft = sichtLuft;
        maximumWartendeBienen = maxWartendeBienen;
        maximumFliegendeBienen = maxFliegendeBienen;
        maximumTanzendeBienen = maxTanzendeBienen;
        position = feldPosition;*/
        
        merkmal = blumenMerkmal;
        vorhandenerNektar = vorhNektar;
        maximumNektarProRunde = maxNektarProRunde;
        maximumAbbauendeBienen = maxAbbauendeBienen;
        nektarAuslesbar=false;
    }
    
    
    
    
    /**
     * fordert von der Blume Nektar an.
     * Bucht die entsprechende Menge Nektar ab.
     * Gibt die abgebaute Menge Nektar zurück.
     *
     *  @param gewuenschteNektarMenge   bestellte Menge an Nektar
     *  @return abgegebene Menge
     */
    int nektarAbgeben(int gewuenschteNektarMenge) {
        int abgabemenge = 0;
        if (gewuenschteNektarMenge<0) return 0;
        if (gewuenschteNektarMenge > maximumNektarProRunde){
            gewuenschteNektarMenge = maximumNektarProRunde;
        }
        if (gewuenschteNektarMenge > vorhandenerNektar){
            abgabemenge=vorhandenerNektar;
        }else {
            abgabemenge=gewuenschteNektarMenge;
        }
               
        //Abbuchen der zulaessigen Menge
        vorhandenerNektar = vorhandenerNektar - abgabemenge;
        return abgabemenge;
    }
    
    /**
     * prüft ob die Kapazität der abbauenden Binen noch nicht voll ist,
     * und fügt gegebenenfalls der Liste abbauendeBienen die
     * abbauendeBiene hinzu.
     * Gibt zurück ob die Biene eingetragen werden konnte.
     *
     * @param abbauendeBienen   einzutragende Biene
     * @return  true, wenn sie eingetragen werden konnte
     */
    boolean trageAbbauendeBieneEin(Biene abbauendeBiene) {
        if (maximumAbbauendeBienen < 0) {
            abbauendeBienen.add(abbauendeBiene);
            return true;
        } else if (abbauendeBienen.size() < maximumAbbauendeBienen) {
            abbauendeBienen.add(abbauendeBiene);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * prüft ob die abbauendeBiene in der Liste abbauendeBienen
     * vorhanden ist und entfernt sie gegebenenfalls aus dieser.
     *
     * Wirft einen Fehler, wenn die Biene entfernt werden konnte.
     *
     * @param abbauendeBiene    zu entfernende Biene
     * @throws RuntimeException Fehler
     */
    void entferneAbbauendeBiene(Biene abbauendeBiene)
    throws RuntimeException {
        if (!abbauendeBienen.remove(abbauendeBiene)) {
            throw new RuntimeException("Abbauende Biene existiert nicht" +
                    " - kann nicht gelöscht werden.");
        }
    }
    
    /**
     * gibt den vorhandenen Nektar zurück.
     *
     * @return vorhandene Nektarmenge
     */
    int gibVorhandenerNektar() {
        return vorhandenerNektar;
    }
    
    /**
     * gibt das Merkmal der Blume zurück.
     *
     * @return  merkmal
     */
    int gibMerkmal() {
        return merkmal;
    }
    
    /**
     * gibt zurück, wieviel Nektar die Blume maxmimal pro Biene
     * und Runde abgeben kann.
     *
     * @return  maximum Nektar pro Runde
     */
    int gibMaxNektarProRunde() {
        return maximumNektarProRunde;
    }
    
    /**
     * gibt eine Liste aller zur Zeit auf dieser Blume abbauenden Bienen zurück.
     *
     * @return  Hash mit den abbauenden Bienen
     */
    HashSet gibAbbauendeBienen() {
        return (HashSet) abbauendeBienen.clone();
    }
    
    /**
     * Prüft, ob der Nektargehalt der Blume für die Biene auslesbar ist.
     *@return true, falls die Biene den BlumenNektarGehalt auslesen darf
     */
    boolean nektarAuslesbar(){
        return this.nektarAuslesbar;
    }
    
}
