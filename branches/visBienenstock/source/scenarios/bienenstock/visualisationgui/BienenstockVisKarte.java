/*
 * KarteFeld.java
 *
 * Created on June 16, 2005, 8:22 PM
 */

package scenarios.bienenstock.visualisationgui;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import scenarios.bienenstock.agenteninfo.Koordinate;
import scenarios.bienenstock.visualisierungsUmgebung.*;
/**
 *
 * @author  philip
 */
public class BienenstockVisKarte extends java.awt.Container {
    
    VisKarte karte;
    BienenstockVisGui fenster;
    ScrollPane scroll;
    int x, x1, y;
    
        
    /**
     * die minimale x Koordinate
     */
    private volatile int minX = 1000000000;
    
    /**
     * die minimale Y Koordinate
     */
    private volatile int minY = 1000000000;
    
    /**
     * die maximale X Koordinate
     */
    private volatile int maxX = -1000000000;
    
    /**
     * die maximale Y Koordiante
     */
    private volatile int maxY = -1000000000;
    
    int groesseX = 51;
    int groesseY = 51;
    
    private boolean initiiert = false;
    
    /**
     * der Pfad zum Verzeichnis mit den Bildern
     */
    private String pfad;
    
    /**
     * die Schrift der Elemente
     */
    private Font schrift = new Font("Serif", Font.PLAIN, 12);
    
    /**
     * eine fett geschriebene Schrift, fuer die Anzahl der Bienen
     * auf einem Feld
     */
    private Font schriftFett = new Font("Serif", Font.BOLD, 12);
    
    /**
     * das Bild vom Bienenstock
     */
    private Image bildBienenstock;
    
    /** 
     * das Bild von der Blume
     */
    private Image bildBlume;
    
    /**
     * das Bild vom Platz
     */
    private Image bildPlatz;
    
    /**
     * das Bild fuer die Biene
     */
    private Image bildBiene;
    
    /**
     * das Bild fuer die Biene implements tanzenden Zustand
     */
    private Image bildBieneTanzend;
    
    /** Creates a new instance of KarteFeld */
    public BienenstockVisKarte(BienenstockVisGui fenst,
                     String path,
                     ScrollPane scrol) {
        fenster = fenst;
        pfad = path;
        scroll = scrol;
        
        //Bilder laden
        bildBiene = Toolkit.getDefaultToolkit().getImage(pfad + "biene.gif");
        bildBieneTanzend = Toolkit.getDefaultToolkit().getImage(pfad + "bieneTanzend.gif");
        bildBienenstock = Toolkit.getDefaultToolkit().getImage(pfad + "bienenstock.gif");
        bildBlume = Toolkit.getDefaultToolkit().getImage(pfad + "blume.gif");
        bildPlatz = Toolkit.getDefaultToolkit().getImage(pfad + "platz.gif");
        if (bildBiene == null) {
            System.out.println("Bilder in " + pfad + " nicht gefunden");
        }
    }
    
    /**
     * berechnet die minimalen und maximalen x und y Koordinaten, sowie die
     * Masse in x und y Richtung
     *
     */
    void setzeXY() {
        Iterator felder = karte.gibFelder().values().iterator();
        Koordinate pos;
        int minBreite = 470;
        while (felder.hasNext()) {
            pos = ((VisFeld)felder.next()).gibPosition();
            if (pos.gibXPosition() < minX) {
                minX = pos.gibXPosition();
            } else if (pos.gibXPosition() > maxX){
                maxX = pos.gibXPosition();
            } 
            if (pos.gibYPosition() < minY) {
                minY = pos.gibYPosition();
            } else if (pos.gibYPosition() > maxY) {
                maxY = pos.gibYPosition();
            }
        }
        
        //Groesse und Position neu setzen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        x = ((maxX - minX) * groesseX) - 50;
        if (x < minBreite) {
            x1 = minBreite;
        } else {
            x1 = x;
        }
        y = ((maxY - minY + 1) * groesseY);
        System.out.println("x = " + x + " y = " + y);
        //die Karte ist groesser als der Screen
        if (x > screen.getWidth() | y > screen.getHeight()) {
            //fenster.add(scrollVert);
            //fenster.add(scrollHori);
            scroll.setSize((int)screen.getWidth() - 5, 
                            ((int)screen.getHeight() - 90));
        } else if (x == 0 | y == 0) {
            //die Karte ist 0 gross
            System.out.println("Karte der Groesse Null erhalten");
        } else {
            //die Fenstergroesse orientiert sich an der Karte
            setSize(x, y);
            scroll.setSize(
                x1, y);
//            fenster.setLocation(
//                (((int)screen.getWidth()) - x) / 2, 
//                ((((int)screen.getWidth()) - y) / 2) 
//                    + fenster.getInsets().top);
        }
        
        //fenster.setResizable(false);
        
        fenster.pack();
        
        //Fenster sichtbar machen
        fenster.setVisible(true);
        fenster.validate();
        
    }
    
    /**
     * Zeichnet das Fenster neu, ohne es vorher zu löschen.
     *
     * @g  Das zu aktualisierende Fenster.
     */
    public void update(Graphics g){
        paint(g);
    }
    
    /**
     * zeichnet den Fensterinhalt
     */
    public void paint(Graphics g) {
        
        //Breite der headline
        int abstandOben = 0; //fenster.getInsets().top + 5;
        
        //zentrieren der Karte
        int imagePosXZentrieren = 0;
                //(fenster.getWidth() - ((maxX - minX + 1) * groesseX)) / 2;
        
        //saeubern des Bereiches fuer die Karte
        //g.clearRect(
        //    0, 
        //    0, 
        //    ((maxX - minX + 1) * groesseX) + imagePosXZentrieren, 
        //    ((maxY - minY + 1) * groesseY) + 50);
        
        Color farbe1 = new Color(0,0,0);
        Color farbe2 = new Color(254, 254, 254);
        g.setColor(farbe1);
        
        if (karte != null) {
                    
            Point scrollWerte = scroll.getScrollPosition();
            fenster.setzeRundeFeld(karte.gibRundennummer() + "");
            fenster.setzeAlteRunde(karte.gibRundennummer());
            HashMap felder = karte.gibFelder();
            VisFeld tmpFeld;
            int x, y;
            for (x = minX; x <= maxX; x++) {
                for (y = minY; y <= maxY; y++) {
           
                    //Positionierung der Schrift
                    int oben = 12;
                    int unten = 46;
                    int links = 2;
                    int rechts = 36;
                    int bildBieneX = - 5;
                    int bildBieneY = 0;
                    
                    // temporaerer Speicher fuer die anzahl der am Boden 
                    // befindlichen Bienen
                    int tmpBienen = 0;
                    
                    int imagePosX = (x - minX) * groesseX 
                            + imagePosXZentrieren;
                    int imagePosY = ((y - minY) * groesseY) + abstandOben;
                    
                    Koordinate koord = new Koordinate(x, y);
                    //gibt es das Feld ueberhaupt
                    if (felder.containsKey(koord)) {
                        tmpFeld = (VisFeld) felder.get(koord);
                        Image bild;
                        /*
                         *          BIENENSTOCK
                         */
                        if (tmpFeld instanceof VisBienenstock) {
                            VisBienenstock tmpStock = (VisBienenstock) tmpFeld;
                            g.drawImage(bildBienenstock, 
                                    imagePosX, 
                                    imagePosY, 
                                    this);
                            g.drawString("" + tmpStock.gibVorhandenerHonig(),
                                    ((x - minX) * groesseX) + links + imagePosXZentrieren,
                                    ((y - minY) * groesseY) + abstandOben + oben);
                            g.drawString("" + tmpStock.gibVorhandenerNektar(),
                                    ((x - minX) * groesseX) + links + imagePosXZentrieren,
                                    ((y - minY) * groesseY) + abstandOben + unten);
                            if (tmpStock.gibFliegendeBienen().size() > 0) {
                                g.drawImage(bildBiene,
                                        ((x - minX) * groesseX) + rechts + imagePosXZentrieren,
                                        ((y - minY) * groesseY) + abstandOben + oben -12,
                                        this);
                                if (tmpStock.gibFliegendeBienen().size() > 1) {
                                
                                    g.setColor(farbe2);
                                    fenster.setFont(schriftFett);
                                    g.drawString("" + tmpStock.gibFliegendeBienen().size(),
                                            ((x - minX) * groesseX) + rechts 
                                                    - bildBieneX  + imagePosXZentrieren,
                                            ((y - minY) * groesseY) + abstandOben 
                                                    + oben - bildBieneY);
                                    g.setColor(farbe1);
                                    fenster.setFont(schrift);
                                }
                            }
                            tmpBienen = tmpStock.gibWartendeBienen().size() 
                                    + tmpStock.gibTanzendeBienen().size()
                                    + tmpStock.gibSonstigeBienen().size();
                            if (tmpBienen > 0) {
                                if (tmpStock.gibTanzendeBienen().size() > 0) {
                                    g.drawImage(bildBieneTanzend,
                                        ((x - minX) * groesseX) + rechts + imagePosXZentrieren,
                                        ((y - minY) * groesseY) + abstandOben + unten - 12,
                                        this);
                                } else {
                                    g.drawImage(bildBiene,
                                            ((x - minX) * groesseX) + rechts + imagePosXZentrieren,
                                            ((y - minY) * groesseY) + abstandOben + unten - 12,
                                            this);
                                }
                                if (tmpBienen > 1) {
                                    g.setColor(farbe2);
                                    fenster.setFont(schriftFett);
                                    g.drawString("" + tmpBienen,
                                          ((x - minX) * groesseX) + rechts 
                                                - bildBieneX + imagePosXZentrieren,
                                          ((y - minY) * groesseY) + abstandOben 
                                                    + unten - bildBieneY);
                                    g.setColor(farbe1);
                                    fenster.setFont(schrift);
                                }
                            }
                            /*
                             *      BLUME
                             */
                        } else if (tmpFeld instanceof VisBlume) {
                            VisBlume tmpBlume = (VisBlume) tmpFeld;
                            g.drawImage(bildBlume, 
                                    imagePosX, 
                                    imagePosY, 
                                    this);
                            g.drawString("" + tmpBlume.gibVorhandenerNektar(),
                                    ((x - minX) * groesseX) + links + imagePosXZentrieren,
                                    ((y - minY) * groesseY) + abstandOben + unten);
                            if (tmpBlume.gibFliegendeBienen().size() > 0) {
                                g.drawImage(bildBiene,
                                        ((x - minX) * groesseX) + rechts + imagePosXZentrieren,
                                        ((y - minY) * groesseY) + abstandOben + oben - 12,
                                        this);
                                if (tmpBlume.gibFliegendeBienen().size() > 1) {
                                    g.setColor(farbe2);
                                    fenster.setFont(schriftFett);
                                    g.drawString("" + tmpBlume.gibFliegendeBienen().size(),
                                            ((x - minX) * groesseX) + rechts 
                                                    - bildBieneX + imagePosXZentrieren,
                                            ((y - minY) * groesseY) + abstandOben 
                                                    + oben - bildBieneY);
                                    g.setColor(farbe1);
                                    fenster.setFont(schrift);
                                }
                            }
                            tmpBienen = tmpBlume.gibWartendeBienen().size() 
                                    + tmpBlume.gibTanzendeBienen().size()
                                    + tmpBlume.gibAbbauendeBienen().size()
                                    + tmpBlume.gibSonstigeBienen().size();
                            if (tmpBienen > 0) {
                                if (tmpBlume.gibTanzendeBienen().size() > 0) {
                                    g.drawImage(bildBieneTanzend,
                                        ((x - minX) * groesseX) + rechts + imagePosXZentrieren,
                                        ((y - minY) * groesseY) + abstandOben + unten - 12,
                                        this);
                                } else {
                                  g.drawImage(bildBiene,
                                        ((x - minX) * groesseX) + rechts + imagePosXZentrieren,
                                        ((y - minY) * groesseY) + abstandOben + unten - 12,
                                        this);
                                }
                                if (tmpBienen > 1) {
                                    g.setColor(farbe2);
                                    fenster.setFont(schriftFett);
                                    g.drawString("" + tmpBienen,
                                          ((x - minX) * groesseX) + rechts 
                                                - bildBieneX + imagePosXZentrieren,
                                          ((y - minY) * groesseY) + abstandOben 
                                                    + unten - bildBieneY);
                                    g.setColor(farbe1);
                                    fenster.setFont(schrift);
                                }
                            }                            
                            /*
                             *          PLATZ
                             */
                        } else if (tmpFeld instanceof VisPlatz) {
                            g.drawImage(bildPlatz, 
                                    imagePosX, 
                                    imagePosY, 
                                    this);
                            if (tmpFeld.gibFliegendeBienen().size() > 0) {
                                g.drawImage(bildBiene,
                                        ((x - minX) * groesseX) + rechts + imagePosXZentrieren,
                                        ((y - minY) * groesseY) + abstandOben + oben - 12,
                                        this);
                                if (tmpFeld.gibFliegendeBienen().size() > 1) {
                                    g.setColor(farbe2);
                                    fenster.setFont(schriftFett);
                                    g.drawString("" + tmpFeld.gibFliegendeBienen().size(),
                                            ((x - minX) * groesseX) + rechts 
                                                    - bildBieneX + imagePosXZentrieren,
                                            ((y - minY) * groesseY) + abstandOben 
                                                    + oben - bildBieneY);
                                    g.setColor(farbe1);
                                    fenster.setFont(schrift);
                                }
                            }
                            tmpBienen = tmpFeld.gibWartendeBienen().size()
                                    + tmpFeld.gibTanzendeBienen().size()
                                    + + tmpFeld.gibSonstigeBienen().size();
                            if (tmpBienen > 0) {
                                if (tmpFeld.gibTanzendeBienen().size() > 0) {
                                    g.drawImage(bildBieneTanzend,
                                        ((x - minX) * groesseX) + rechts + imagePosXZentrieren,
                                        ((y - minY) * groesseY) + abstandOben + unten - 12,
                                        this);
                                } else {
                                  g.drawImage(bildBiene,
                                            ((x - minX) * groesseX) + rechts + imagePosXZentrieren,
                                            ((y - minY) * groesseY) + abstandOben + unten - 12,
                                            this);
                                }
                                if (tmpBienen > 1) {
                                    g.setColor(farbe2);
                                    fenster.setFont(schriftFett);
                                    g.drawString("" + tmpBienen,
                                          ((x - minX) * groesseX) + rechts 
                                                - bildBieneX + imagePosXZentrieren,
                                          ((y - minY) * groesseY) + abstandOben 
                                                    + unten - bildBieneY);
                                    g.setColor(farbe1);
                                    fenster.setFont(schrift);
                                }
                            }
                        } else {
                            System.out.println("Vis: ungueltiger FeldTyp!");
                        }
                    }
                }
            }
            scroll.setScrollPosition(scrollWerte);
        }
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(x, y);
    }
    
    void setzeKarte(VisKarte map) {
        karte = map;
    }
    
}
