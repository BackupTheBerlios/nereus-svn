/*
 * Dateiname      : Visualisation.java
 * Erzeugt        : 19. Mai 2005
 * Letzte Änderung: 26. Mai 1005 durch Samuel Walz
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *                  
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package source.client.visualisation;

import source.client.visualisation.IVisualisation;

/**
 * Die Dummy-Klasse Visualisation.
 *
 * @author  Samuel Walz
 */
public class Visualisation extends Thread implements IVisualisation {
    
    /** Creates a new instance of Visualisation */
    public Visualisation() {
    }
    
    public void run() {
        System.out.println("Visualisierung wurde gestartet...");
        synchronized(this) {
            try {
                this.wait(60000);
            } catch(Exception fehler) {
                System.out.println(fehler.getMessage());
            }
        }
        System.out.println("...und beendet.");
    }
    
    public void visualisiere(java.lang.Object information) {
        System.out.println((String)information);
    }
    
}
