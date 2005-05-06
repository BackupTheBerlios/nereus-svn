/*
 * Created on Apr 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package scenario.bienenstock.visualisierung;

import scenario.bienenstock.visualisierungsUmgebung.*;
import scenario.bienenstock.Koordinate;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;

import java.awt.*;

/**
 * @author philip
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Bienenstockvisualisierung {
	
	private boolean initiiert = false;
	private VisKarte karte;
	private int groesseX = 0;
	private int groesseY = 0;
	JFrame fenster;
	Container inhalt;
	
	public Bienenstockvisualisierung() {
		fenster = new JFrame();
		fenster.setTitle("Bienenstockvisualisierung");
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		fenster.setSize((int)screen.getWidth(), (int)screen.getHeight() - 30);
		fenster.setLocation(0, fenster.getInsets().top);
		inhalt = fenster.getContentPane();
	}
	
	public void visualisiere (VisKarte neueKarte) {
		if (neueKarte != null) {
			karte = neueKarte;
			if (!initiiert) {
				setzeXY();
			}
			inhalt.removeAll();
			inhalt.setLayout(new GridLayout(groesseX, groesseY, 2, 2));
			karteZeichnen();			
			fenster.show();
		} else {
			System.out.println("Vis: Keine Gültige Karte übergeben bekommen.");
		}
	}
	
	private void setzeXY() {
		int minX = 1000000000; 
		int minY = 1000000000;
		int maxX = -1000000000;
		int maxY = -1000000000;
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
		groesseX = maxX - minX + 1;
		groesseY = maxY - minY + 1;
		initiiert = true;
	}
	
	private void karteZeichnen() {
		int i;
		for (i = 0; i < (groesseX * groesseY); i++) {
		inhalt.add(new JLabel(
				new ImageIcon(Bienenstockvisualisierung.class.getResource("kamel.gif"))));
		}
	}
	
	private void erstelleBild() {
		
	}
	
}
