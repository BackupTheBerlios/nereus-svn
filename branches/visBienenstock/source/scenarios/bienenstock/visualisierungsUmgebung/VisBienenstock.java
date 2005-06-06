/*
 * Dateiname      : VisBienenstock.java
 * Erzeugt        : 06. Oktober 2004
 * Letzte Änderung: 06. Juni 2005 durch Dietmar Lippold
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
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


package scenarios.bienenstock.visualisierungsUmgebung;

import java.util.HashSet;

import scenarios.bienenstock.agenteninfo.Koordinate;

/**
 * repräsentiert einen Bienenstock.
 *
 * @author Philip Funck
 */
public class VisBienenstock extends VisFeld {

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
     * ist der Faktor, der angibt wieviel Honig aus einer
     * bestimmten Menge Nektar wird.
     *
     * mengeNektar * wechselkursNektarZuHonig = mengeHonig
     */
    private int wechselkursNektarZuHonig;

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
     * @param feldPosition  Position
     * @param sichtBoden    Sichtweite am Boden
     * @param sichtLuft     Sichtweite in der Luft
     * @param wBienen       wartende Bienen
     * @param fBienen       fliegende Bienen
     * @param tBienen       tanzende Bienen
     * @param maxGelagerterNektar Maximum an gelagertem Nektar
     * @param vorhNektar    vorhandener Nektar
     * @param vorhHonig     vorhandener Honig
     * @param bienenvolksID Volks ID des Stockes
     */
    public VisBienenstock(Koordinate feldPosition,
            int sichtBoden,
            int sichtLuft,
            HashSet wBienen,
            HashSet fBienen,
            HashSet tBienen,
            int maxGelagerterNektar,
            int vorhNektar,
            int vorhHonig,
            int bienenvolksID
            ) {
        super(feldPosition,
                sichtBoden,
                sichtLuft,
                wBienen,
                fBienen,
                tBienen);
        
        /*position = feldPosition;
        sichtweiteAmBoden = sichtBoden;
        sichtweiteInDerLuft = sichtLuft;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;*/
        
        maximumGelagerterNektar = maxGelagerterNektar;
        vorhandenerNektar = vorhNektar;
        vorhandenerHonig = vorhHonig;
        volksID = bienenvolksID;
    }

    /**
     * gibt die ID des zugehörigen Bienenvolks zurück.
     *
     * @return ID des Bienenvolkes zu dem der Stock gehört
     */
    public int gibVolksID() {
        return volksID;
    }

    /**
     * gibt die Obergrenze für die lagerbare Nektarmenge zurück.
     *
     * @return Maximum an gelagerten Nektar
     */
    public int gibMaxGelagerterNektar() {
        return maximumGelagerterNektar;
    }

    /**
     * gibt die Menge an vorhandenem Nektar zurück.
     *
     * @return vorhandener Nektar
     */
    public int gibVorhandenerNektar() {
        return vorhandenerNektar;
    }

    /**
     * gibt zurück, wieviel Nektar pro Runde maximal in Honig
     * umgewandelt werden kann.
     *
     * @return Maximum an Nektar der pro Runde zu Honig gemacht wird
     */
    public int gibMaxNektarZuHonigProRunde() {
        return maximumNektarZuHonigProRunde;
    }

    /**
     * gibt zurück, wieviel Honig man aus einer Einheit Nektar herstellen kann.
     *
     * @return Wechselkurs von Nektar zu Honig
     */
    public int gibWechselkursNektarZuHonig() {
        return wechselkursNektarZuHonig;
    }

    /**
     * gibt zurück, wieviel Honig vorhanden ist.
     *
     * @return vorhandener Honig
     */
    public int gibVorhandenerHonig() {
        return vorhandenerHonig;
    }
}

