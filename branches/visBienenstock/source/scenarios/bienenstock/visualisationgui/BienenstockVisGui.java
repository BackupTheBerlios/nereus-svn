/*
 * Dateiname      : BienenstockVisGui.java
 * Erzeugt        : 26. April 2005
 * Letzte ?nderung: 15. Juni 2005 durch Philip Funck
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

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import scenarios.bienenstock.agenteninfo.Koordinate;
import scenarios.bienenstock.visualisierungsUmgebung.*;

/**
 * das Frame fuer die Bienenstockvisualisierung.
 * 
 * @author philip
 */
public class BienenstockVisGui extends Frame {
	
    /**
     * ob die Visualisierung die erste Karte bekommen hat.
     */
    private volatile boolean initiiert = false;
    
    /**
     * die zu visualisierende Karte
     */
    private volatile VisKarte karte;
    
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
     * eine fett geschriebene Schrift, fuer die Anzahl der Bienen
     * auf einem Feld
     */
    private Font schriftFett = new Font("Serif", Font.BOLD, 12);
    
    /**
     * der Pfad zum Verzeichnis mit den Bildern
     */
    private String pfad;
    
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
    
    /**
     * das Panel fuer die Buttons zur Steuerung 
     * der darzustellenden Sequenz
     */
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
    
     /**
      * Das Label fuer die Rundennummer
      */
    Label runde = new Label("Runde: ");
    
    /**
     * der Button zum abschicken der Rundennummer
     */
    Button rundeButton = new Button("ok");
    
    /**
     * das Feld fuer die Eingabe der Rundennummer
     */
    TextField rundeFeld = new TextField("   0");
    
    /**
     * zum Speichern des letzten Wertes im rundeFeld
     */
    int alteRunde = 0;
    
    /**
     * das Label fuer die Einstellung der Zeit
     */
    Label zeitLabel = new Label("Geschwindigkeit:");
    
    /**
     * dasFeld fuer die Eingabe der neuen Zeitverzoegerung
     */
    TextField zeitFeld = new TextField("1000");
    
    /**
     * zum Speichern des letzten Wertes implements zeitFeld
     */
    int alteZeit = 1000;
    
    /**
     * der Button zum absenden eines neuen Zeitwertes
     */
    Button zeitButton = new Button("ok");
    
    /**
     * Verknuepfung mit dem Puffer
     */
    BienenstockVisSteuerung vis;

    /**
     * der windowListener fuer das Einstellen der Zeit
     */
    ActionListener zeitAktion = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
            try {
                int tmp = Integer.parseInt(zeitFeld.getText());
                vis.setzeZeit(tmp);
                alteZeit = tmp;
            } catch (NumberFormatException ex) {
                zeitFeld.setText("" + alteZeit);
            }
            zeitButton.setVisible(false);
            knoepfe.validate();
        }
    };

    /**
     * der windowListener fuer das Einstellen der Zeit
     */
    ActionListener rundeAktion = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
            try {
                int tmp = Integer.parseInt(rundeFeld.getText());
                vis.setzeNaechste(tmp);
                alteRunde = tmp;
            } catch (NumberFormatException ex) {
                rundeFeld.setText(alteRunde + "");
            }
        }
    };
    
    /**
     * die Aktion die bei einer Pause ausgefuehrt wird
     */
    ActionListener pauseAktion = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
            pause.setLabel("Weiter");
            rundeButton.setVisible(true);
            knoepfe.validate();
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
            if (vor.isVisible() == true) {
                pause.setLabel("Pause");
                rundeButton.setVisible(false);
                knoepfe.validate();
                vis.weiter();
                pause.addActionListener(pauseAktion);
                pause.removeActionListener(this);
                knoepfe.validate();
            }
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
     * die Aktion, die bei Aenderung des Inhaltes des TextFeldes zeitFeld
     * ausgefuehrt werden soll
     */
    TextListener zeitListen = new TextListener() {
        public void textValueChanged(TextEvent e) {
            zeitButton.setVisible(true);
            knoepfe.validate();
        }
    };
    
    /**
     * der Konstruktor
     *
     */
    public BienenstockVisGui(BienenstockVisSteuerung visu, String pfad) {

        this.setVisible(false);
        this.validate();
        this.vis = visu;
        this.pfad = pfad;

        //die ActionListener zu den Buttons hinzufuegen
        pause.addActionListener(pauseAktion);
        zurueck.addActionListener(zurueckAktion);
        vor.addActionListener(vorAktion);
        zeitButton.addActionListener(zeitAktion);
        rundeButton.addActionListener(rundeAktion);
        zeitFeld.addTextListener(zeitListen);
        
        //das Panel knoepfe fuellen
        knoepfe.setLayout(new FlowLayout());
        knoepfe.add(runde);
        //rundeFeld.setSize(20, 12);
        knoepfe.add(rundeFeld);
        knoepfe.add(rundeButton);
        knoepfe.add(zurueck);
        knoepfe.add(pause);
        knoepfe.add(vor);
        knoepfe.add(zeitLabel);
        knoepfe.add(zeitFeld);
        knoepfe.add(zeitButton);
        knoepfe.validate();
        
        //das Panel knoepfe hinzufuegen
        add(knoepfe, BorderLayout.SOUTH);
        
        //das Fenster beschriften
        fenster = this;
	fenster.setTitle("Bienenstockvisualisierung");
        fenster.setFont(schrift);
        
        //Schliessen ermoeglichen
	addWindowListener( new WindowAdapter() {
            public void windowClosing ( WindowEvent e ) {
                fenster.dispose();
                vis.beenden();
            }
        });

        //Groesse und Position setzen
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        //Groesse wird auf 0 gesetzt, da 
        //<code>fenster.setVisible(false);</code> nicht funktioniert
	fenster.setSize(0,0);

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
     * berechnet die minimalen und maximalen x und y Koordinaten, sowie die
     * Masse in x und y Richtung
     *
     */
    private void setzeXY() {
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
        initiiert = true;
        
                
        //werden erst wieder sichtbar, wenn der Inhalt geaendert wird
        rundeButton.setVisible(false);
        zeitButton.setVisible(false);
        knoepfe.validate();
        
        //Groesse und Position neu setzen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = ((maxX - minX + 1) * groesseX) + 10;
        if (x < minBreite) {
            x = minBreite;
        }
        int y = ((maxY - minY + 2) * groesseY) 
            + fenster.getInsets().top;
        if (x > screen.getWidth() | y > screen.getHeight()) {
            fenster.setSize((int)screen.getWidth(), 
                            ((int)screen.getHeight() + fenster.getInsets().top));
        } else if (x == 0 | y == 0) {
            System.out.println("Karte der Groesse Null erhalten");
        } else {
            fenster.setSize(
                x, y);
            fenster.setLocation(
                (((int)screen.getWidth()) - x) / 2, 
                ((((int)screen.getWidth()) - y) / 2) 
                    + fenster.getInsets().top);
        }
        
        fenster.setResizable(false);
        
        //Fenster sichtbar machen
        fenster.setVisible(true);
        this.validate();
        
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
        int abstandOben = fenster.getInsets().top + 5;
        
        //zentrieren der Karte
        int imagePosXZentrieren = 
                (fenster.getWidth() - ((maxX - minX + 1) * groesseX)) / 2;
        
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
            rundeFeld.setText(karte.gibRundennummer() + "");
            alteRunde = karte.gibRundennummer();
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
        }
    }
    
    /**
     * wird benutzt um den vor-Button unsichtbar zu machen, wenns nicht weiter vor geht
     */
    public void amEnde(boolean ende) {
        if (vor.isVisible() == ende) {
            vor.setVisible(!ende);
            pause.setLabel("Weiter");
            rundeButton.setVisible(true);
            knoepfe.validate();
            vis.warte();
            pause.addActionListener(weiterAktion);
            pause.removeActionListener(pauseAktion);
        }
    }
    
    /**
     * wird benutzt um den zurueck-Button unsichtbar zu machen, 
     * wenns nicht weiter zurueck geht
     */
    public void amAnfang(boolean anfang) {
        if(zurueck.isVisible() == anfang) {
            zurueck.setVisible(!anfang);
            knoepfe.validate();
        }
    }
}

