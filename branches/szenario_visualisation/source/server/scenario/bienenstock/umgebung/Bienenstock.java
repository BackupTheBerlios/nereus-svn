/*
 * Erzeugt        : 19. Mai 2004
 * Letzte Änderung: 26. Januar 2005 durch Samuel Walz
 *
 * Teil des Softwarepraktikums mit dem Titel
 *
 *   "Design und Implementierung eines Szenarios für einen
 *    Multiagenten-Simulator"
 *
 * bei Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de)
 *
 * Autoren  : Philip Funck (mango.3@gmx.de)
 *            Samuel Walz (felix-kinkowski@gmx.net)
 *
 * copyright: Institut für Intelligente Systeme, Universität Stuttgart (2004)
 *            http://www.iis.uni-stuttgart.de
 */
package scenario.bienenstock.umgebung;

import scenario.bienenstock.Koordinate;

/**
 * repräsentiert einen Bienenstock.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Bienenstock extends Feld {
    /**
     * ist das Maximum des im Bienenstock lagerbaren Nektars.
     */
    private int maximumGelagerterNektar;

    /**
     * ist der im Bienenstock vorhandene Nektar.
     */
    private int vorhandenerNektar;

    /**
     * ist die Menge des Nektars der pro Runde zu Honig gemacht werden kann.
     */
    private int maximumNektarZuHonigProRunde;

    /**
     * ist der im Bienenstock vorhandene Honig.
     */
    private int vorhandenerHonig;

    /**
     * ist eine Nummer, die das Bienenvolk eindeutig kennzeichnet.
     */
    private int volksID;

    /**
     * Konstruktor.
     *
     * @param eigenID   die ID
     * @param sichtBoden    die Sichtweite am Boden
     * @param sichtLuft     Sichtweite in der Luft
     * @param maxGelagerterNektar   maximum gelagerter Nektar
     * @param vorhNektar    vorhenadener Nektar
     * @param maxNektarZuHonigProRunde  maximum nektar zu honig pro runde
     * @param vorhHonig vorhandener Honig
     * @param maxWartendeBienen maximum wartende Bienen
     * @param maxFliegendeBienen    maximum fliegende Bienen
     * @param bienenvolksID die ID des zugehörigen bienenvolkes
     * @param feldPosition  die koordinate des Stockes
     * @param maxTanzendeBienen maximum tanzende bienen
     */
    Bienenstock(int eigenID,
        int sichtBoden,
        int sichtLuft,
        int maxGelagerterNektar,
        int vorhNektar,
        int maxNektarZuHonigProRunde,
        int vorhHonig,
        int maxWartendeBienen,
        int maxFliegendeBienen,
        int bienenvolksID,
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

        maximumGelagerterNektar = maxGelagerterNektar;
        vorhandenerNektar = vorhNektar;
        maximumNektarZuHonigProRunde = maxNektarZuHonigProRunde;
        vorhandenerHonig = vorhHonig;
        volksID = bienenvolksID;
    }

    /**
     * macht aus einer Menge von Nektar Honig,
     * wobei pro Runde nur maximumNektarZuHonig viel Nektar
     * verarbeitet werden kann.
     * @param maximumNektarZuHonigProRunde max entstehender Honig
     * @param wechselkursNektarZuHonig derkurs in dem nekatr in honig wandelt
     */
    void nektarZuHonigVerarbeiten(int maximumNektarZuHonigProRunde,
                                  double wechselkursNektarZuHonig) {

        if (vorhandenerNektar > maximumNektarZuHonigProRunde) {
            vorhandenerHonig = vorhandenerHonig
                                  + (int) ((double) maximumNektarZuHonigProRunde
                                        * wechselkursNektarZuHonig);
            vorhandenerNektar = vorhandenerNektar
                                - maximumNektarZuHonigProRunde;
        } else {
            vorhandenerHonig = vorhandenerHonig
                               + (int) ((double) vorhandenerNektar
                                       * wechselkursNektarZuHonig);
            vorhandenerNektar = 0;
        }
    }

    /**
     * verringert <code>vorhandenerHonig</code> um
     * <code>gewuenschteHonigmenge</code> und gibt
     * zurück wieviel abgebucht werden konnte.
     *
     *  @param gewuenschteHonigmenge diemenge, die die Biene haben will
     *  @return wieviel sie bekommen hat
     */
    int honigAbgeben(int gewuenschteHonigmenge) {
        if (vorhandenerHonig < gewuenschteHonigmenge) {
            int abgabemenge = vorhandenerHonig;

            vorhandenerHonig = 0;
            return abgabemenge;
        } else {
            vorhandenerHonig = vorhandenerHonig - gewuenschteHonigmenge;
            return gewuenschteHonigmenge;
        }
    }

    /**
     * fügt <code>vorhandenerNektar</code> Nektarmenge hinzu,
     * bis <code>maximumgelagerterNektar</code> ereicht ist
     * und gibt dabei zurück wieviel abgegeben wurde.
     *
     *  @param nektarmenge die Menge des abzunehmenden Nektars
     *  @return wieviel abgenommen wurde
     */
    int nektarAbnehmen(int nektarmenge) {
        if (nektarmenge + vorhandenerNektar > maximumGelagerterNektar) {
            int aufnahmemenge = maximumGelagerterNektar - vorhandenerNektar;

            vorhandenerNektar = maximumGelagerterNektar;
            return aufnahmemenge;
        } else {
            vorhandenerNektar = vorhandenerNektar + nektarmenge;
            return nektarmenge;
        }
    }

    /**
     * gibt zurück, wieviel Nektar maximal gelagert werden kann.
     *
     * @return int
     */
    int gibMaxGelagerterNektar() {
        return maximumGelagerterNektar;
    }

    /**
     * gibt zurück, wieviel Nektar pro Runde maximal zu Honig umgewandelt
     * werden kann.
     *
     * @return int
     */
    int gibMaximumNektarZuHonig() {
        return maximumNektarZuHonigProRunde;
    }

    /**
     * gibt zurück, wieviel Honig vorhanden ist.
     *
     * @return int
     */
    public int gibVorhandenerHonig() {
        return vorhandenerHonig;
    }

    /**
     * gibt zurück, wieviel Nektar vorhanden ist.
     *
     * @return int
     */
    public int gibVorhandenerNektar() {
        return vorhandenerNektar;
    }

    /**
     * gibt die ID des dem Stock angehörigen Bienenvolks zurück.
     *
     * @return int
     */
    int gibVolksID() {
        return volksID;
    }
}
