/*
 * Dateiname      : Info.java
 * Erzeugt        : 18. November 2004
 * Letzte Änderung: 14. Februar 2005 durch Samuel Walz
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
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
package scenarios.bienenstock.agenteninfo;

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
