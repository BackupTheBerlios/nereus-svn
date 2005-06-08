/*
 * Dateiname      : Visualisierung.java
 * Erzeugt        : 06. Mai 2005
 * Letzte Änderung: 06. Juni 2005 durch Dietmar Lippold
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
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
public class Visualisierung extends Thread {
    
    /**
     * das frame der Visualisierung
     */
    private Bienenstockvisualisierung fenster;
    
    /**
     * die Liste mit den Karten, die visualisiert wurden und werden müssen. 
     */
    private LinkedList karten;
    
    /**
     * die nächste zu visualisierende Karte, als Position in der Liste. 
     */
    private int naechste = 0;
    
    /**
     * die Zeit in ms, die gewartet werden soll, bevor die nächste Karte frühestens 
     * visualisiert werden soll.
     */
    private long zeit = 1000L;
    
    /**
     * der Konstruktor initiiert das Frame und die Kartenliste
     *
     */
    public Visualisierung () {
        fenster = new BienenstockvisualisierungFenster(this);
        fenster.show();
        karten = new LinkedList();
    }
    
    /**
     * wird von dem Szenario aufgerufen um eine neue Karte hinzuzufügen
     * 
     * @param karte
     */
    public void visualisiere(VisKarte karte) {
        // hinzufügen der neuen Karte
        karten.addLast(karte);
    }
    
    /**
     * prüft, ob eine zu visualisierende Karte in <code>karten</code> ist und leitet 
     * die gegebenenfalls weiter. Anschließend wird <code>zeit</code> 
     * lang gewartet.
     */
    public void run () {
        while (fenster.isActive()) {
            // überprüfen ob eine neue Karte da ist
            if (naechste < karten.size()) {
                //neue Karte weiterleiten
                fenster.visualisiere((VisKarte)karten.get(naechste));
                //hochzählen
                naechste = naechste + 1;
                //warten
                try {
                    synchronized (this) {
                        this.wait(zeit);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Visualisierung wurde unterbrochen");
                }
            }
        }
    }
}

