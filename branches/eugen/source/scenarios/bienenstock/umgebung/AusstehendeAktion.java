/*
 * Dateiname      : AusstehendeAktion.java
 * Erzeugt        : 13. Dezember 2004
 * Letzte Änderung: 26. Januar 2005 durch Philip Funck
 *
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *
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

package scenarios.bienenstock.umgebung;

/**
 * dient zur Verwaltung ausstehender Aktionen der Agenten.
 *
 * @author Philip Funck
 */
public class AusstehendeAktion {
    /**
     * ob die Aktion ausstehend ist
     */
    private boolean ausstehend;
    /**
     * id der zugehörigen Biene
     */
    private int id;
    
    /**
     * Konstruktor.
     *
     * @param ident Id der zugehörigen Biene
     */
    public AusstehendeAktion(int ident) {
        id = ident;
        ausstehend = true;
    }
    
    /**
     * gibt die ID der zugehörigen Biene zurück.
     *
     * @return int
     */
    public int gibID() {
        return id;
    }
    
    /**
     * gibt zurück, ob die Aktion noch aussteht.
     *
     * @return boolean
     */
    public boolean gibAusstehend() {
        return ausstehend;
    }
    
    /**
     * setzt es auf wahr, das die aktionnoch austeht.
     */
    public void setzeFalse() {
        ausstehend = false;
    }
    
    /**
     * setzt es auf falsch, das die aktion noch aussteht.
     *
     */
    public void setzeTrue() {
        ausstehend = true;
    }
}
