/*
 * Erzeugt        : 6. Oktober 2004
 * Letzte �nderung: 26. Januar 2005 durch Philip Funck
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

package scenario.bienenstock.visualisierungsUmgebung;

import scenario.bienenstock.Koordinate;
import scenario.bienenstock.Info;

/**
 * ist das Abbild des Agenten im Szenario.
 *
 * Dieses Abbild wird verwendet um die Werte der Agenten f�r etwaige 
 * �berpr�fungen bereit zu halten. Dadurch werden beeinflussungen der Werte 
 * durch den jeweiligen Agenten ausgeschlossen.
 * @author Philip Funck
 */
public class VisBiene {

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
    String gibZustand() {
        return zustand;
    }

    /**
     * Gibt die ID des zugeh�rigen Bienenvolks zur�ck.
     *
     * @return die ID des Bienenvolkes dem sie angeh�rt
     */
    int gibBienenvolkID() {
        return bienenvolkID;
    }

    /**
     * Gibt die ID der Biene zur�ck.
     *
     * @return die ID der Biene
     */
    int gibBienenID() {
        return bienenID;
    }

    /**
     * Gibt die Position der Biene auf der Spielkarte zur�ck.
     *
     * @return die Position der Biene
     */
    Koordinate gibPosition() {
        return position;
    }

    /**
     * Gibt die geladene Nektarmenge der Biene zur�ck.
     *
     * @return die geladenen Nektarmenge
     */
    int gibGeladeneNektarmenge() {
        return geladeneNektarmenge;
    }

    /**
     * Gibt die geladeneHonigmenge der Biene zur�ck.
     *
     * @return die geladenen Honigmenge
     */
    int gibGeladeneHonigmenge() {
        return geladeneHonigmenge;
    }

    /**
     * Gibt die maximal tragbare Nektarmenge zur�ck.
     *
     * @return das Maximum was sie an Nektar tragen kann
     */
    int gibMaximumNektar() {
        return maximumGeladenerNektar;
    }

    /**
     * Gibt die maximal tragbare Honigmenge zur�ck.
     *
     * @return das Maximum, was sie an Honig tragen kann
     */
    int gibMaximumHonig() {
        return maximumGeladenerHonig;
    }

    /**
     * Gibt die zuletzt getanzte Information der Biene zur�ck.
     *
     * @return die Information, die sie tr�gt <code>null</code>, 
     *         wenn sie keine tr�gt
     */
    Info gibInformation() {
        return information;
    }
}
