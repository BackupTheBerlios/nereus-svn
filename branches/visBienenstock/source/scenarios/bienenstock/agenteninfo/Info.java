/*
 * Dateiname      : Info.java
 * Erzeugt        : 18. November 2004
 * Letzte Änderung: 14. Juni 2005 durch Eugen Volk
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *                  Eugen Volk
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
package scenarios.bienenstock.agenteninfo;
import java.io.Serializable;
/**
 * enthält Angaben über Richtung und Entfernung eines Ziels.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Info implements Serializable {
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
    
    /** Nutzen der Blume für den Agenten; wird berechnet aus der Entfernung zum 
     * Bienenstock und Ausbeute der Blume. */
    private double nutzen=-1;
    
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
     * Der Konstruktor.
     *
     * @param richtg            die neue Richtung
     * @param entf              die neue Entfernung
     * @param richtgMitteilen   wird die Richtung mitgeteilt?
     * @param entfMitteilen     wird die Entfernung mitgeteilt?
     * @param nutzen            Nutzen der Blume für den Agenten
     */
    public Info(double richtg, double entf,
            boolean richtgMitteilen, boolean entfMitteilen, double nutzen) {
        richtung = richtg;
        entfernung = entf;
        richtungMitteilen = richtgMitteilen;
        entfernungMitteilen = entfMitteilen;
        this.nutzen=nutzen;
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
     * gibt den Nutzen der Blume zurück; wird berechnet aus der Entfernung zum Bienenstock
     * und Ausbeute der Blume. Falls der Nutzen  nicht gesetzt wurde, so wird -1 zurückgegeben.
     * 
     *
     * @return Nutzen der Blume
     */
    public double gibNutzen(){
        return nutzen;
    }
    
    /**
     * gibt eine Kopie des Objekts zurück.
     *
     * @return      Enhält Nutzen, Entfernungs und / oder Richtungsangaben
     */
    public Info klonen() {
        return new Info(richtung,
                entfernung,
                richtungMitteilen,
                entfernungMitteilen, 
                nutzen);
    }
}
