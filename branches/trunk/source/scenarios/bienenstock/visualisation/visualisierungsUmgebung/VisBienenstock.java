/*
 * Erzeugt        : 6. Oktober 2004
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

package bienenstockVisualisierung.visualisierungsUmgebung;
import bienenstockVisualisierung.Koordinate;
import java.util.HashSet;

/**
 * repr�sentiert einen Bienenstock.
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
     * gibt die ID des zugeh�rigen Bienenvolks zur�ck.
     *
     * @return ID des Bienenvolkes zu dem der Stock geh�rt
     */
    int gibVolksID() {
        return volksID;
    }

    /**
     * gibt die Obergrenze f�r die lagerbare Nektarmenge zur�ck.
     *
     * @return Maximum an gelagerten Nektar
     */
    int gibMaxGelagerterNektar() {
        return maximumGelagerterNektar;
    }

    /**
     * gibt die Menge an vorhandenem Nektar zur�ck.
     *
     * @return vorhandener Nektar
     */
    int gibVorhandenerNektar() {
        return vorhandenerNektar;
    }

    /**
     * gibt zur�ck, wieviel Nektar pro Runde maximal in Honig
     * umgewandelt werden kann.
     *
     * @return Maximum an Nektar der pro Runde zu Honig gemacht wird
     */
    int gibMaxNektarZuHonigProRunde() {
        return maximumNektarZuHonigProRunde;
    }

    /**
     * gibt zur�ck, wieviel Honig man aus einer Einheit Nektar herstellen kann.
     *
     * @return Wechselkurs von Nektar zu Honig
     */
    int gibWechselkursNektarZuHonig() {
        return wechselkursNektarZuHonig;
    }

    /**
     * gibt zur�ck, wieviel Honig vorhanden ist.
     *
     * @return vorhandener Honig
     */
    int gibVorhandenerHonig() {
        return vorhandenerHonig;
    }

}
