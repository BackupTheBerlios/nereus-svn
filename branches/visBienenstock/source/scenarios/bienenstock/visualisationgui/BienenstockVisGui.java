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
    
    ScrollPane scroll = new ScrollPane();
    
    BienenstockVisKarte karteFeld;
    
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

        karteFeld = new BienenstockVisKarte(this, pfad, scroll);
        
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
        
        scroll.add(karteFeld);
        
        //das Panel knoepfe hinzufuegen
        add(knoepfe, BorderLayout.SOUTH);
        add(scroll, BorderLayout.CENTER);
        
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
        scroll.setSize(0, 0);
        
        //werden erst wieder sichtbar, wenn der Inhalt geaendert wird
        rundeButton.setVisible(false);
        zeitButton.setVisible(false);
        knoepfe.validate();
        //pack();
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
            karteFeld.setzeKarte(karte);
            if (!initiiert) {
                
                karteFeld.setzeXY();
                initiiert = true;
                karteFeld.repaint();
                pack();
                zeitButton.setVisible(false);
            } else {
                karteFeld.repaint();
            }
        } else {
            System.out.println("Vis: Keine Gueltige Karte uebergeben bekommen.");
        }
    }
    
    /**
     * wird benutzt um den vor-Button unsichtbar zu machen, wenns nicht weiter vor geht
     */
    public void amEnde(boolean ende) {
        if (vor.isVisible() == ende) {
            vor.setVisible(!ende);
            if (initiiert) {
                pause.setLabel("Weiter");
                rundeButton.setVisible(true);
                vis.warte();
                pause.addActionListener(weiterAktion);
                pause.removeActionListener(pauseAktion);
            }
            knoepfe.validate();
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
    
    void setzeAlteRunde(int olle) {
        alteRunde = olle;
    }
    void setzeRundeFeld(String round) {
        rundeFeld.setText(round);
        knoepfe.validate();
    }
    
    int gibHoeheKnoepfe() {
        return knoepfe.getHeight();
    }
}

