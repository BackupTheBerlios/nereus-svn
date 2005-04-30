/*
 * Dateiname      : Bienenstock.java
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
