/*
 * Dateiname      : BienenstockVisGui.java
 * Erzeugt        : 26. April 2005
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

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import scenarios.bienenstock.agenteninfo.Koordinate;
import scenarios.bienenstock.visualisierungsUmgebung.*;

/**
 * das Frame f?r die Bienenstockvisualisierung.
 * 
 * @author philip
 */
public class BienenstockVisGui extends Frame {
	
    /**
     * ob die Visualisierung die erste Karte bekommen hat.
     */
    private boolean initiiert = false;
    
    /**
     * die zu visualisierende Karte
     */
    private VisKarte karte;
    
    /**
     * die groesse der Karte in x Richtung
     */
    private int groesseX = 51;
    
    /**
     * die groesse der Karte in Y Richtung
     */
    private int groesseY = 51;
    
    /**
     * das Fenster
     */
    private Frame fenster;
    
    /**
     * die Schrift der Elemente
     */
    private Font schrift = new Font("Serif", Font.PLAIN, 12);
    
    /**
     * der Pfad zu den librarys
     */
    private String pfad = "/home/philip/workspace/nereus/branches/visBienenstock/libs/bilder/";
    
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
     * die minimale x Koordinate
     */
    private int minX = 1000000000;
    
    /**
     * die minimale Y Koordinate
     */
    private int minY = 1000000000;
    
    /**
     * die maximale X Koordinate
     */
    private int maxX = -1000000000;
    
    /**
     * die maximale Y Koordiante
     */
    private int maxY = -1000000000;
    
    Panel knoepfe = new Panel();
    
    /**
     * der Knopf zum Pausieren
     */
    Button pause = new Button("Pause");
    
    /**
     * der zurueck Knopf
     */
    Button zurueck = new Button("<");
    
    /**
     * der vor Knopf
     */
    Button vor = new Button(">");
    
    Label runde = new Label("Runde: ");
    
    Label rundenNr = new Label("0 ");
    
    /**
     * Verknuepfung mit dem Puffer
     */
    BienenstockVisSteuerung vis;

    /**
     * die Aktion die bei einer Pause ausgefuehrt wird
     */
    ActionListener pauseAktion = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
            pause.setLabel("Weiter");
            vis.warte();
            pause.addActionListener(weiterAktion);
            pause.removeActionListener(pauseAktion);
        }
    };
    
    /**
     * die Aktion die zum weitermachen ausgefuehrt wird
     */
    ActionListener weiterAktion = new ActionListener () {
        public void actionPerformed(ActionEvent e) {
            pause.setLabel("Pause");
            vis.weiter();
            pause.addActionListener(pauseAktion);
            pause.removeActionListener(this);
        }
    };
    
    /**
     * die Aktion die zum zurueckspulen ausgefuehrt wird
     */
    ActionListener zurueckAktion = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            vis.zurueck();
        }
    };
    
    /**
     * die Aktion, die zm vorspulen ausgefuehrt wird
     */
    ActionListener vorAktion = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            vis.vor();
        }
    };
    
    /**
     * der Konstruktor
     *
     */
    public BienenstockVisGui(BienenstockVisSteuerung visu) {
        vis = visu;
        pause.addActionListener(pauseAktion);
        zurueck.addActionListener(zurueckAktion);
        vor.addActionListener(vorAktion);
        knoepfe.setLayout(new FlowLayout());
//        knoepfe.add(zurueck, FlowLayout.LEFT);
//        knoepfe.add(pause, FlowLayout.CENTER);
//        knoepfe.add(vor, FlowLayout.RIGHT);
//        knoepfe.add(runde, FlowLayout.LEFT);
//        knoepfe.add(rundenNr, FlowLayout.LEADING);
        knoepfe.add(zurueck);
        knoepfe.add(pause);
        knoepfe.add(vor);
        knoepfe.add(runde);
        knoepfe.add(rundenNr);
        add(knoepfe, BorderLayout.SOUTH);
        fenster = this;
        fenster.setVisible(false);
	fenster.setTitle("Bienenstockvisualisierung");
        fenster.setFont(schrift);
        //Schliessen
	        addWindowListener( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
              System.exit(0);
            }
          } );

        //Groesse und Position setzen
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	fenster.setSize(0,0);
	fenster.setLocation(0, fenster.getInsets().top);

        //Bilder laden
        bildBienenstock = Toolkit.getDefaultToolkit().getImage(pfad + "bienenstock.gif");
        bildBlume = Toolkit.getDefaultToolkit().getImage(pfad + "blume.gif");
        bildPlatz = Toolkit.getDefaultToolkit().getImage(pfad + "platz.gif");
    }

    /**
     * ruft gegebenenfalls die Funktion zur Berechnung der Masse der Karte auf, und
     * veranlasst eine neue Zeichnung des Fensterinhaltes
     * 
     * @param neueKarte
     */
    public void visualisiere (VisKarte neueKarte) {
        if (neueKarte != null) {
	    karte = neueKarte;
            if (!initiiert) {
                setzeXY();
            }
	    repaint();
        } else {
            System.out.println("Vis: Keine Gueltige Karte uebergeben bekommen.");
        }
    }

    /**
     * berechnet die minimalen und maximaken x und y Koordinaten, sowie die
     * Ma?e in x und y Richtung
     *
     */
    private void setzeXY() {
        Iterator felder = karte.gibFelder().values().iterator();
        Koordinate pos;
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
        initiiert = true;
        
        //Groesse und Position neu setzen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = ((maxX - minX + 1) * groesseX) + 10;
        int y = ((maxY - minY + 2) * groesseY) 
            + fenster.getInsets().top;
	fenster.setSize(
            x, y);
	fenster.setLocation(
            (((int)screen.getWidth()) - x) / 2, 
            ((((int)screen.getWidth()) - y) / 2) 
                + fenster.getInsets().top);
        
        //Fenster sichtbar machen
        fenster.setVisible(true);
        
    }
    
    /**
     * zeichnet den Fensterinhalt
     */
    public void paint(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        
        if (karte != null) {
            rundenNr.setLabel(karte.gibRundennummer() + " ");
            HashMap felder = karte.gibFelder();
            VisFeld tmpFeld;
            int x, y;
            for (x = minX; x <= maxX; x++) {
                for (y = minY; y <= maxY; y++) {
                    //Breite der headline
                    int abstandOben = fenster.getInsets().top + 5;
                    //Positionierung der Schrift
                    int oben = 12;
                    int unten = 46;
                    int links = 2;
                    int rechts = 36;
                    
                    // temporaerer Speicher f?r die anzahl der am Boden 
                    // befindlichen Bienen
                    int tmpBienen = 0;
                    
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
                                    (x - minX) * groesseX, 
                                    ((y - minY) * groesseY) + abstandOben, 
                                    this);
                            g.drawString("" + tmpStock.gibVorhandenerHonig(),
                                    ((x - minX) * groesseX) + links,
                                    ((y - minY) * groesseY) + abstandOben + oben);
                            g.drawString("" + tmpStock.gibVorhandenerNektar(),
                                    ((x - minX) * groesseX) + links,
                                    ((y - minY) * groesseY) + abstandOben + unten);
                            if (tmpStock.gibFliegendeBienen().size() > 0) {
                                g.drawString("" + tmpStock.gibFliegendeBienen().size(),
                                        ((x - minX) * groesseX) + rechts,
                                        ((y - minY) * groesseY) + abstandOben + oben);
                            }
                            tmpBienen = tmpStock.gibWartendeBienen().size() 
                                    + tmpStock.gibTanzendeBienen().size();
                            if (tmpBienen > 0) {
                                g.drawString("" + tmpBienen,
                                        ((x - minX) * groesseX) + rechts,
                                        ((y - minY) * groesseY) + abstandOben + unten);
                            }
                            /*
                             *      BLUME
                             */
                        } else if (tmpFeld instanceof VisBlume) {
                            VisBlume tmpBlume = (VisBlume) tmpFeld;
                            g.drawImage(bildBlume, 
                                    (x - minX) * groesseX, 
                                    ((y - minY) * groesseY) + abstandOben, 
                                    this);
                            g.drawString("" + tmpBlume.gibVorhandenerNektar(),
                                    ((x - minX) * groesseX) + links,
                                    ((y - minY) * groesseY) + abstandOben + unten);
                            if (tmpBlume.gibFliegendeBienen().size() > 0) {
                                g.drawString("" + tmpBlume.gibFliegendeBienen().size(),
                                        ((x - minX) * groesseX) + rechts,
                                        ((y - minY) * groesseY) + abstandOben + oben);
                            }
                            tmpBienen = tmpBlume.gibWartendeBienen().size() 
                                    + tmpBlume.gibTanzendeBienen().size()
                                    + tmpBlume.gibAbbauendeBienen().size();
                            if (tmpBienen > 0) {
                                g.drawString("" + tmpBienen,
                                        ((x - minX) * groesseX) + rechts,
                                        ((y - minY) * groesseY) + abstandOben + unten);
                            }                            
                            /*
                             *          PLATZ
                             */
                        } else if (tmpFeld instanceof VisPlatz) {
                            g.drawImage(bildPlatz, 
                                    (x - minX) * groesseX, 
                                    ((y - minY) * groesseY) + abstandOben, 
                                    this);
                            if (tmpFeld.gibFliegendeBienen().size() > 0) {
                                g.drawString("" + tmpFeld.gibFliegendeBienen().size(),
                                        ((x - minX) * groesseX) + rechts,
                                        ((y - minY) * groesseY) + abstandOben + oben);
                            }
                            tmpBienen = tmpFeld.gibWartendeBienen().size()
                                    + tmpFeld.gibTanzendeBienen().size();
                            if (tmpBienen > 0) {
                                g.drawString("" + tmpBienen,
                                        ((x - minX) * groesseX) + rechts,
                                        ((y - minY) * groesseY) + abstandOben + unten);
                            }
                        } else {
                            System.out.println("Vis: ungueltiger FeldTyp!");
                        }
                    }
                }
            }
        }
    }
}

