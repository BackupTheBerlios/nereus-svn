/*
 * Erzeugt        : 18. November 2004
 * Letzte �nderung: 14. Februar 2005 durch Samuel Walz
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

package bienenstockVisualisierung;

/**
 * enth�lt Angaben �ber Richtung und Entfernung eines Ziels.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Info {
    /**
     * die Richtung eines Ziels
     */
    private double richtung;
    
    /**
     * die Entfernung eines Ziels
     */
    private double entfernung;
    
    /**
     * h�lt fest, ob die Richtung angegeben ist.
     */
    private boolean richtungMitteilen;
    
    /**
     * h�lt fest, ob die Entfernung angegeben ist.
     */
    private boolean entfernungMitteilen;

    /**
     * Der Konstruktor.
     * 
     * @param richtg            die neue Richtung
     * @param entf              die neue Entfernung
     * @param richtgMitteilen   wird die Richtung mitgeteilt?
     * @param entfMitteilen     wird die Entfernung mitgeteilt?
     */
    public Info(double richtg, double entf,
                boolean richtgMitteilen, boolean entfMitteilen) {
        richtung = richtg;
        entfernung = entf;
        richtungMitteilen = richtgMitteilen;
        entfernungMitteilen = entfMitteilen;
    }

    /**
     * gibt zur�ck,ob die Richtung enthalten ist.
     * 
     * @return      true: Richtung ist enthalten
     */
    public boolean besitztRichtung() {
        return richtungMitteilen;
    }

    /**
     * gibt zur�ck, ob die Entfernung enthalten ist.
     * 
     * @return      true: Entfernung ist enthalten
     */
    public boolean besitztEntfernung() {
        return entfernungMitteilen;
    }

    /**
     * gibt die Richtung zur�ck.
     * 
     * @return      Winkelangabe zwischen 0.0 und 360.0
     */
    public double gibRichtung() {
        return richtung;
    }

    /**
     * gibt die Entfernung zur�ck.
     * 
     * @return      Entfernung in L�ngeneinheiten
     */
    public double gibEntfernung() {
        return entfernung;
    }

    /**
     * gibt eine Kopie des Objekts zur�ck.
     * 
     * @return      Enh�lt Entfernungs und / oder Richtungsangaben
     */
    public Info klonen() {
        return new Info(richtung,
                        entfernung,
                        richtungMitteilen,
                        entfernungMitteilen);
    }
}
