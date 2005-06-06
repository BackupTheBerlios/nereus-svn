/*
 * Dateiname      : Visualisierung.java
 * Erzeugt        : 06. Mai 2005
 * Letzte �nderung: 06. Juni 2005 durch Dietmar Lippold
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
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


package scenarios.bienenstock.visualisationgui;

import java.util.LinkedList;

import java.awt.*;

import scenarios.bienenstock.visualisierungsUmgebung.*;

/**
 * @author philip
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Visualisierung {
    
    private Bienenstockvisualisierung fenster;
    private LinkedList karten;
    private boolean initiiert = false;
    private int naechste = 0;
    private long zeit = 1000L;
    
    public Visualisierung () {
        fenster = new Bienenstockvisualisierung(this);
        fenster.show();
        karten = new LinkedList();
    }
    
    public void visualisiere(VisKarte karte) {
        karten.addLast(karte);
        System.out.println("karten.size() = " + karten.size());
        if (naechste < karten.size()) {
            try {
                synchronized (this) {
                    this.wait(zeit);
                }
            } catch (InterruptedException e) {
                System.out.println("wurde unterbrochen");
            }
            fenster.visualisiere((VisKarte)karten.get(naechste));
            naechste = naechste + 1;
        }
    }
}
