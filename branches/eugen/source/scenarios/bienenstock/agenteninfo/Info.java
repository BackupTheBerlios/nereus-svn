/*
 * Dateiname      : Info.java
 * Erzeugt        : 18. November 2004
 * Letzte �nderung: 14. Februar 2005 durch Samuel Walz
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *                  
 *                  
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
package scenarios.bienenstock.agenteninfo;

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
