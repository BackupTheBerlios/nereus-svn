/*
 * Dateiname      : Visualisierung.java
 * Erzeugt        : 06. Mai 2005
 * Letzte ?nderung: 12. Juni 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *
 * Diese Datei geh?rt zum Projekt Nereus (http://nereus.berlios.de/).
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
    private VisualisierungFenster fenster;
    
    /**
     * die Liste mit den Karten, die visualisiert wurden und werden m?ssen. 
     */
    private LinkedList karten;
    
    /**
     * die n?chste zu visualisierende Karte, als Position in der Liste. 
     */
    private int naechste = 0;
    
    /**
     * die Zeit in ms, die gewartet werden soll, bevor die n?chste Karte fr?hestens 
     * visualisiert werden soll.
     */
    private long zeit = 1000L;
    
    private boolean warten = false;
    
    /**
     * der Konstruktor initiiert das Frame und die Kartenliste
     *
     */
    public Visualisierung () {
        fenster = new VisualisierungFenster(this);
        fenster.show();
        karten = new LinkedList();
    }
    
    /**
     * wird von dem Szenario aufgerufen um eine neue Karte hinzuzuf?gen
     * 
     * @param karte
     */
    public void visualisiere(VisKarte karte) {
        // hinzuf?gen der neuen Karte
        karten.addLast(karte);
    }
    
    /**
     * setzt die zuVisualisierende Karte um einen runter
     */
    public void zurueck() {
        if (naechste > 0) {
            naechste = naechste - 1;
            fenster.visualisiere((VisKarte)karten.get(naechste));
        }
    }
    
    /**
     * setzt die zu visualisierende Karte um einen hoch
     */
    public void vor() {
        if ((naechste + 1)< karten.size()) {
            naechste = naechste + 1;
            fenster.visualisiere((VisKarte)karten.get(naechste));
        }
        
    }
    
    /**
     * ermoeglicht dem Fenster den Puffer schlafen zu legen
     */
    public void warte () {
        warten = true;
    }
    
    /**
     * ermoeglicht dem Fenster den Puffer wieder zu wecken
     */
    public void weiter() {
        warten = false;
    }
    
    /**
     * pr?ft, ob eine zu visualisierende Karte in <code>karten</code> ist und leitet 
     * die gegebenenfalls weiter. Anschlie?end wird <code>zeit</code> 
     * lang gewartet.
     */
    public void run () {
        while (fenster.isActive()) {
            if (warten) {
                try {
                    synchronized (this) {
                        this.wait(10);
                        }
                    } catch (InterruptedException e) {
                    }
            } else {
                // ueberpruefen ob eine neue Karte da ist
                if (naechste < karten.size()) {
                    //neue Karte weiterleiten
                    fenster.visualisiere((VisKarte)karten.get(naechste));
                    //hochz?hlen
                    naechste = naechste + 1;
                    //warten
                    try {
                        synchronized (this) {
                            this.wait(zeit);
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Visualisierung wurde unterbrochen");
                    }
                } else {
                    try {
                        synchronized (this) {
                            this.wait(10);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}

