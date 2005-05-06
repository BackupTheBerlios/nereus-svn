/*
 * Erzeugt        : 19. Mai 2004
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

import scenario.bienenstock.Koordinate;
import java.util.HashSet;

/**
 * repr�sentiert eine Blume.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Blume extends Feld {

    /**
     * ist das �u�ere Merkmal der Blume.
     */
    private int merkmal;

    /**
     * ist die vorhandene Nektarmenge.
     */
    private int vorhandenerNektar;

    /**
     * ist das Maximum, das von einer Biene pro Runde bezogen werden kann.
     */
    private int maximumNektarProRunde;

    /**
     * ist das Maximum an Bienen, die Zeitgleich Nektar abbauen k�nnen.
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
    }

    /**
     * fordert von der Blume Nektar an.
     * Bucht die entsprechende Menge Nektar ab.
     * Gibt die abgebaute Menge Nektar zur�ck.
     *
     *  @param gewuenschteNektarMenge   bestellte Menge an Nektar
     *  @return abgegebene Menge
     */
    int nektarAbgeben(int gewuenschteNektarMenge) {
        int abgabemenge = 0;

        //Ermitteln der zulaessigen Abgabemenge
        if (gewuenschteNektarMenge > maximumNektarProRunde
                && gewuenschteNektarMenge > vorhandenerNektar) {

            if (maximumNektarProRunde > vorhandenerNektar) {
                abgabemenge = vorhandenerNektar;
            } else {
                abgabemenge = maximumNektarProRunde;
            }
        } else if (gewuenschteNektarMenge > maximumNektarProRunde
                    && gewuenschteNektarMenge < vorhandenerNektar) {

            abgabemenge = maximumNektarProRunde;
        } else if (gewuenschteNektarMenge < maximumNektarProRunde
                    && gewuenschteNektarMenge > vorhandenerNektar) {

            abgabemenge = vorhandenerNektar;
        } else {
            abgabemenge = gewuenschteNektarMenge;
        }

        //Abbuchen der zulaessigen Menge
        vorhandenerNektar = vorhandenerNektar - abgabemenge;
        return abgabemenge;
    }

    /**
     * pr�ft ob die Kapazit�t der abbauenden Binen noch nicht voll ist,
     * und f�gt gegebenenfalls der Liste abbauendeBienen die
     * abbauendeBiene hinzu.
     * Gibt zur�ck ob die Biene eingetragen werden konnte.
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
     * pr�ft ob die abbauendeBiene in der Liste abbauendeBienen
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
                    " - kann nicht gel�scht werden.");
        }
    }

    /**
     * gibt den vorhandenen Nektar zur�ck.
     *
     * @return vorhandene Nektarmenge
     */
    int gibVorhandenerNektar() {
        return vorhandenerNektar;
    }

    /**
     * gibt das Merkmal der Blume zur�ck.
     *
     * @return  merkmal
     */
    int gibMerkmal() {
        return merkmal;
    }

    /**
     * gibt zur�ck, wieviel Nektar die Blume maxmimal pro Biene
     * und Runde abgeben kann.
     *
     * @return  maximum Nektar pro Runde
     */
    int gibMaxNektarProRunde() {
        return maximumNektarProRunde;
    }

    /**
     * gibt eine Liste aller zur Zeit auf dieser Blume abbauenden Bienen zur�ck.
     *
     * @return  Hash mit den abbauenden Bienen
     */
    HashSet gibAbbauendeBienen() {
        return (HashSet) abbauendeBienen.clone();
    }
}
