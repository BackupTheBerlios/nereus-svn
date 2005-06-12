/*
 * Dateiname      : BienenstockVisSteuerung.java
 * Erzeugt        : 06. Mai 2005
 * Letzte ?nderung: 12. Juni 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *
 * Diese Datei gehoert zum Projekt Nereus (http://nereus.berlios.de/).
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

import nereus.simulatorinterfaces.IVisualisationOutput;

import nereus.visualisationclient.VisualisationClient;

/**
 * @author philip
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BienenstockVisSteuerung extends Thread implements IVisualisationOutput{
    
    /**
     * das frame der Visualisierung
     */
    private BienenstockVisGui fenster;
    
    /**
     * die Liste mit den Karten, die visualisiert wurden und werden muessen. 
     */
    private LinkedList karten;
    
    /**
     * die naechste zu visualisierende Karte, als Position in der Liste. 
     */
    private int naechste = 0;
    
    /**
     * die Zeit in ms, die gewartet werden soll, bevor die naechste Karte fruehestens 
     * visualisiert werden soll.
     */
    private long zeit = 1000L;
    
    private boolean warten = false;
    
    VisualisationClient visClient;
    
    /**
     * der Konstruktor initiiert das Frame und die Kartenliste
     *
     */
    public BienenstockVisSteuerung (VisualisationClient vClient) {
        visClient = vClient;
        visClient.anmeldung(this);
        fenster = new BienenstockVisGui(this);
        fenster.show();
        karten = new LinkedList();
        this.start();
        visClient.starteUebertragung();
    }
    
    /**
     * wird von dem Szenario aufgerufen um eine neue Karte hinzuzufuegen
     * 
     * @param karte
     */
    public void visualisiere(Object visObject) {
        if (visObject instanceof VisKarte) {
            // hinzufuegen der neuen Karte
            karten.addLast(visObject);
        }
    }
    
    /**
     * setzt die zuVisualisierende Karte um einen runter
     */
    public void zurueck() {
        if (naechste > 0) {
            naechste = naechste - 1;
            fenster.visualisiere((VisKarte)karten.get(naechste));
            
            if (naechste == 0) {
                fenster.amAnfang(true);
            }
            //vor-button wieder sichtbar machen
            fenster.amEnde(false);
        } else {
            fenster.amAnfang(true);
        }
    }
    
    /**
     * setzt die zu visualisierende Karte um einen hoch
     */
    public void vor() {
        if ((naechste + 1) < karten.size()) {
            naechste = naechste + 1;
            fenster.visualisiere((VisKarte)karten.get(naechste));
            if (naechste == karten.size()) {
                fenster.amEnde(true);
            }
            //zurueck-button wieder sichtbar machen
            fenster.amAnfang(false);
        } else {
            fenster.amEnde(true);
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
     * veraendert die Zeit, die zwischen 2 Karten gewartet wird
     *
     * @param die neue Zeitspanne
     */
    public void setzeZeit (int t) {
        if (t > 0) {
            zeit = t;
        }
    }
    
    /**
     * prueft, ob eine zu visualisierende Karte in <code>karten</code> ist und leitet 
     * die gegebenenfalls weiter. Anschliessend wird <code>zeit</code> 
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
                    //zurueck-button wieder sichtbar machen
                    fenster.amAnfang(false);
                    //hochzaehlen
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
                    fenster.amEnde(true);            
                    try {
                        synchronized (this) {
                            this.wait(10);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        visClient.stoppeUebertragung();
    }
}

