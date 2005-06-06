/*
 * Dateiname      : Bienenstockvisualisierung.java
 * Erzeugt        : 26. April 2005
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

import java.util.Hashtable;
import java.util.Enumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import scenarios.bienenstock.agenteninfo.Koordinate;
import scenarios.bienenstock.visualisierungsUmgebung.*;

/**
 * @author philip
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Bienenstockvisualisierung extends Frame {
	
    private boolean initiiert = false;
    private VisKarte karte;
    private int groesseX = 51;
    private int groesseY = 51;
    private Frame fenster;
    
    private Font schrift = new Font("Serif", Font.PLAIN, 12);
    
    private String pfad = "/home/philip/workspace/nereus/branches/philip/libs/";
    private Image bildBienenstock;
    private Image bildBlume;
    private Image bildPlatz;
    
    private int minX = 1000000000; 
    private int minY = 1000000000;
    private int maxX = -1000000000;
    private int maxY = -1000000000;

    private boolean fertig = true;

    private Visualisierung vis;


    public Bienenstockvisualisierung(Visualisierung visu) {
        fenster = this;
        vis = visu;
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
	        fenster.setSize((int)screen.getWidth(), (int)screen.getHeight() - 30);
	        fenster.setLocation(0, fenster.getInsets().top);

        //Bilder laden
        bildBienenstock = Toolkit.getDefaultToolkit().getImage(pfad + "bienenstock.gif");
        bildBlume = Toolkit.getDefaultToolkit().getImage(pfad + "blume.gif");
        bildPlatz = Toolkit.getDefaultToolkit().getImage(pfad + "platz.gif");
    }


    public void visualisiere (VisKarte neueKarte) {
        if (neueKarte != null) {
	    karte = neueKarte;
            if (!initiiert) {
                setzeXY();
            }
	    repaint();
        } else {
            System.out.println("Vis: Keine G�ltige Karte �bergeben bekommen.");
        }
    }

	
    private void setzeXY() {
        Enumeration felder = karte.gibFelder().elements();
        Koordinate pos;
        while (felder.hasMoreElements()) {
            pos = ((VisFeld)felder.nextElement()).gibPosition();
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
    }
	
    public void paint(Graphics g) {
        g.setColor(new Color(0, 0, 0));

        if (karte != null) {
            Hashtable felder = karte.gibFelder();
            VisFeld tmpFeld;
            int x, y;
            for (x = minX; x <= maxX; x++) {
                for (y = minY; y <= maxY; y++) {
                    int abstandOben = fenster.getInsets().top + 5;
                    int oben = 12;
                    int unten = 46;
                    int links = 2;
                    int rechts = 36;
                    Koordinate koord = new Koordinate(x, y);
                    if (felder.containsKey(koord)) {
                        tmpFeld = (VisFeld) felder.get(koord);
                        Image bild;
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
                            if (tmpStock.gibWartendeBienen().size() > 0) {
                                g.drawString("" + tmpStock.gibWartendeBienen().size(),
                                        ((x - minX) * groesseX) + rechts,
                                        ((y - minY) * groesseY) + abstandOben + unten);
                            }
                        } else if (tmpFeld instanceof VisBlume) {
                            VisBlume tmpBlume = (VisBlume) tmpFeld;
                            g.drawImage(bildBlume, 
                                    (x - minX) * groesseX, 
                                    ((y - minY) * groesseY) + abstandOben, 
                                    this);
                            g.drawString("" + tmpBlume.gibVorhandenerNektar(),
                                    ((x - minX) * groesseX) + links,
                                    ((y - minY) * groesseY) + abstandOben + oben);
                            if (tmpBlume.gibFliegendeBienen().size() > 0) {
                                g.drawString("" + tmpBlume.gibFliegendeBienen().size(),
                                        ((x - minX) * groesseX) + rechts,
                                        ((y - minY) * groesseY) + abstandOben + oben);
                            }
                            if (tmpBlume.gibWartendeBienen().size() > 0) {
                                g.drawString("" + tmpBlume.gibFliegendeBienen().size(),
                                        ((x - minX) * groesseX) + rechts,
                                        ((y - minY) * groesseY) + abstandOben + unten);
                            }
                            if (tmpBlume.gibAbbauendeBienen().size() > 0) {
                                g.drawString("" + tmpBlume.gibAbbauendeBienen().size(),
                                        ((x - minX) * groesseX) + links,
                                        ((y -minY) * groesseY) + abstandOben + unten);
                            }
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
                            if (tmpFeld.gibWartendeBienen().size() > 0) {
                                g.drawString("" + tmpFeld.gibWartendeBienen().size(),
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

