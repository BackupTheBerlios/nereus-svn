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

package bienenstockVisualisierung.visualisierungsUmgebung;
import java.util.Hashtable;
import bienenstockVisualisierung.Koordinate;

/**
 * Die Spielkartendarstellung f�r die Visualisierung.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class VisKarte {
    /**
     * Alle anwesenden Bienen.
     */
    private Hashtable bienen;

    /**
     * Das Spielfeld f�r die Visualisierung.
     *
     * @associates VisFeld
     */
    private Hashtable spielfeld;

    /**
     * Nummer der aktuellen Runde.
     */
    private int rundennummer;


    /**
     * Liste aller Kosten f�r die Aktionen der Bienen.
     */
    private Hashtable kosten;

    /**
     * Konstruktor.
     *
     * @param runde die aktuelle Runde
     * @param bienchen  die Bienen
     * @param feldchen  die Felder
     * @param kostenvoranschlag Kosten f�r die Aktionen
     */
    public VisKarte(int runde,
                    Hashtable bienchen,
                    Hashtable feldchen,
                    Hashtable kostenvoranschlag) {
        rundennummer = runde;
        bienen = bienchen;
        spielfeld = feldchen;
        kosten = kostenvoranschlag;
    }

    /**
     * gibt eine Liste der Kosten f�r alle Aktionen zur�ck.
     *
     * @return Kosten  f�r die Aktionen
     */
    public Hashtable gibHonigkosten() {
        return kosten;
    }

    /**
     * gibt eine Biene zur�ck, die in der Liste bienen mit bieneID
     * zu finden ist.
     * 
     * @param bieneID ID der Biene
     * @return eine Vis Biene 
     */
    public VisBiene gibBiene(int bieneID) {
        if (bienen.containsKey(new Integer(bieneID))) {
            return (VisBiene)bienen.get(new Integer(bieneID));
        }
        else {
            //Wurde Biene nicht gefunden, so gib nichts zur�ck (null)
            return null;
        }
    }

    /**
     * gibt eine Liste aller anwesenden Bienen zur�ck.
     *
     * @return alle Bienen
     */
    public Hashtable gibBienen() {
        return bienen;
    }

    /**
     * gibt ein Feld mit den Koordinaten ort zur�ck.
     *
     * @param ort zu suchendes Feld
     * @return  das Feld was gesucht werden sollte, wenn nicht vorhanden null
     */
    public VisFeld gibFeld(Koordinate ort) {
        if (spielfeld.containsKey(ort)) {
            return (VisFeld)spielfeld.get(ort);
        } else {
        //Wurde das Feld nicht gefunden, so gib nichts zur�ck (null)
        return null;
        }
    }

    /**
     * gibt das Spielfeld f�r die Visualisierung zur�ck.
     *
     * @return alle Felder
     */
    public Hashtable gibFelder() {
        return spielfeld;
    }
}
