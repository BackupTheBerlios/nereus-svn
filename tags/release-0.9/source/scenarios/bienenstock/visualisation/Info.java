/*
 * Erzeugt        : 18. November 2004
 * Letzte Änderung: 14. Februar 2005 durch Samuel Walz
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

package bienenstockVisualisierung;

/**
 * enthält Angaben über Richtung und Entfernung eines Ziels.
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
     * hält fest, ob die Richtung angegeben ist.
     */
    private boolean richtungMitteilen;
    
    /**
     * hält fest, ob die Entfernung angegeben ist.
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
     * gibt zurück,ob die Richtung enthalten ist.
     * 
     * @return      true: Richtung ist enthalten
     */
    public boolean besitztRichtung() {
        return richtungMitteilen;
    }

    /**
     * gibt zurück, ob die Entfernung enthalten ist.
     * 
     * @return      true: Entfernung ist enthalten
     */
    public boolean besitztEntfernung() {
        return entfernungMitteilen;
    }

    /**
     * gibt die Richtung zurück.
     * 
     * @return      Winkelangabe zwischen 0.0 und 360.0
     */
    public double gibRichtung() {
        return richtung;
    }

    /**
     * gibt die Entfernung zurück.
     * 
     * @return      Entfernung in Längeneinheiten
     */
    public double gibEntfernung() {
        return entfernung;
    }

    /**
     * gibt eine Kopie des Objekts zurück.
     * 
     * @return      Enhält Entfernungs und / oder Richtungsangaben
     */
    public Info klonen() {
        return new Info(richtung,
                        entfernung,
                        richtungMitteilen,
                        entfernungMitteilen);
    }
}
