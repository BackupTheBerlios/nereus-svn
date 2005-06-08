/*
 * Dateiname      : VisBiene.java
 * Erzeugt        : 06. Oktober 2004
 * Letzte �nderung: 08. Juni 2005 durch Samuel Walz
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (samuel@gmx.info)
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


package scenarios.bienenstock.visualisierungsUmgebung;

import scenarios.bienenstock.agenteninfo.Info;
import scenarios.bienenstock.agenteninfo.Koordinate;

import java.io.Serializable;

/**
 * ist das Abbild des Agenten im Szenario.
 *
 * Dieses Abbild wird verwendet um die Werte der Agenten f�r etwaige
 * �berpr�fungen bereit zu halten. Dadurch werden beeinflussungen der Werte
 * durch den jeweiligen Agenten ausgeschlossen.
 * @author Philip Funck
 */
public class VisBiene implements Serializable {

    /**
     * Der Zustand der Biene.
     */
    private String zustand;

    /**
     * ist eine Nummer, die die Biene eindeutig kennzeichnet.
     * Ist identisch mit der Agent-ID die die Agenten im Simulator haben.
     */
    private int bienenID;

    /**
     * ist eine Nummer,
     * die die Zugeh�rigkeit einer Biene zu einem Bienenvolk
     * eindeutig kennzeichnet.
     */
    private int bienenvolkID;

    /**
     * Die Position der Biene auf der Spielkarte
     */
    private Koordinate position;

    /**
     * Die aktuell geladene Honigmenge der Biene.
     */
    private int geladeneHonigmenge;

    /**
     * Die aktuell geladene Nektarmenge der Biene
     */
    private int geladeneNektarmenge;

    /**
     * Maximale Beladung mit Honig.
     */
    private int maximumGeladenerHonig;

    /**
     * Maximale Beladung mit Nektar.
     */
    private int maximumGeladenerNektar;

    /**
     * ZUletzt getanzte Information der Biene.
     */
    private Info information;

    /**
     * Konstruktor.
     *
     * @param aktZustand    aktueller Zustand der Biene
     * @param eigenID       die ID der Biene
     * @param volkID        die VolksID der Biene
     * @param bienePosition die Position der Biene
     * @param gelHonigmenge geladenen Honigmenge
     * @param gelNektarmenge    geladene Nekarmenge
     * @param maxGeladenerHonig maximal gelagerter Honig
     * @param maxGeladenerNektar    maximal gelagerter Nektar
     */
    public VisBiene(String aktZustand,
            int eigenID,
            int volkID,
            Koordinate bienePosition,
            int gelHonigmenge,
            int gelNektarmenge,
            int maxGeladenerHonig,
            int maxGeladenerNektar) {
        
        zustand = aktZustand;
        bienenID = eigenID;
        bienenvolkID = volkID;
        position = bienePosition;
        geladeneHonigmenge = gelHonigmenge;
        geladeneNektarmenge = gelNektarmenge;
        maximumGeladenerHonig = maxGeladenerHonig;
        maximumGeladenerNektar = maxGeladenerNektar;
        information = null;
    }

    /**
     * Konstruktor f�r die Repr�sentation einer tanzenden Biene.
     *
     * @param aktZustand    aktueller Zustand der Biene
     * @param eigenID       die ID der Biene
     * @param volkID        die VolksID der Biene
     * @param bienePosition die Position der Biene
     * @param gelHonigmenge geladenen Honigmenge
     * @param gelNektarmenge    geladene Nekarmenge
     * @param maxGeladenerHonig maximal gelagerter Honig
     * @param maxGeladenerNektar    maximal gelagerter Nektar
     * @param infoObjekt die Information, die sie tr�gt
     */
    public VisBiene(String aktZustand,
            int eigenID,
            int volkID,
            Koordinate bienePosition,
            int gelHonigmenge,
            int gelNektarmenge,
            int maxGeladenerHonig,
            int maxGeladenerNektar,
            Info infoObjekt) {
        
        zustand = aktZustand;
        bienenID = eigenID;
        bienenvolkID = volkID;
        position = bienePosition;
        geladeneHonigmenge = gelHonigmenge;
        geladeneNektarmenge = gelNektarmenge;
        maximumGeladenerHonig = maxGeladenerHonig;
        maximumGeladenerNektar = maxGeladenerNektar;
        information = infoObjekt;
    }

    /**
     * gibt den Zustand der Biene zur�ck.
     *
     * @return der aktuelle Zustand
     */
    public String gibZustand() {
        return zustand;
    }

    /**
     * Gibt die ID des zugeh�rigen Bienenvolks zur�ck.
     *
     * @return die ID des Bienenvolkes dem sie angeh�rt
     */
    public int gibBienenvolkID() {
        return bienenvolkID;
    }

    /**
     * Gibt die ID der Biene zur�ck.
     *
     * @return die ID der Biene
     */
    public int gibBienenID() {
        return bienenID;
    }

    /**
     * Gibt die Position der Biene auf der Spielkarte zur�ck.
     *
     * @return die Position der Biene
     */
    public Koordinate gibPosition() {
        return position;
    }

    /**
     * Gibt die geladene Nektarmenge der Biene zur�ck.
     *
     * @return die geladenen Nektarmenge
     */
    public int gibGeladeneNektarmenge() {
        return geladeneNektarmenge;
    }

    /**
     * Gibt die geladeneHonigmenge der Biene zur�ck.
     *
     * @return die geladenen Honigmenge
     */
    public int gibGeladeneHonigmenge() {
        return geladeneHonigmenge;
    }

    /**
     * Gibt die maximal tragbare Nektarmenge zur�ck.
     *
     * @return das Maximum was sie an Nektar tragen kann
     */
    public int gibMaximumNektar() {
        return maximumGeladenerNektar;
    }

    /**
     * Gibt die maximal tragbare Honigmenge zur�ck.
     *
     * @return das Maximum, was sie an Honig tragen kann
     */
    public int gibMaximumHonig() {
        return maximumGeladenerHonig;
    }

    /**
     * Gibt die zuletzt getanzte Information der Biene zur�ck.
     *
     * @return die Information, die sie tr�gt <code>null</code>,
     *         wenn sie keine tr�gt
     */
    public Info gibInformation() {
        return information;
    }
}

