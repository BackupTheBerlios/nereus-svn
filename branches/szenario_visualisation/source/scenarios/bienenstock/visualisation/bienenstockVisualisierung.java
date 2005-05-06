/*
 * Created on Mar 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package bienenstockVisualisierung;

import bienenstockVisualisierung.visualisierungsUmgebung.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.LinkedList;
/**
 * @author philip
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class bienenstockVisualisierung {

	private int maxX, maxY, minX, minY;
	VisKarte karte;
	private Hashtable felder;
	private LinkedList xRaster;
	private boolean initialisiert = false;
	
	public void setzeVisKarte(VisKarte visKarte) {
		if (visKarte != null) {
			karte = visKarte;
			felder = karte.gibFelder();
			if (!initialisiert) {
				initialisiert = true;
				findeMinMaxXY();
				erzeugeRaster();
			}
			visualisiere();
		} else {
			System.out.println("Die visKarte wurde nicht übergeben!");
		}
	}
	
	private void findeMinMaxXY() {
		maxX = 0;
		maxY = 0;
		minX = 0;
		minY = 0;
		Koordinate koord;
		Enumeration enumFelder = felder.elements();
		Koordinate koord;
		while (enumFelder.hasMoreElements()) {
			koord = enumFelder.nextElement().gibPosition();
			int x = koord.gibXPosition();
			int y = koord.gibYPosition();
			if (x > maxX ) {
				maxX = x;
			} else if (x < minX) {
				minX = x;
			} else if (y > maxY) {
				maxY = y;
			} else if (y < minY) {
				minY = y;
			}
		}
	}
	
	private LinkedList erzeugeRaster() {
		int sizeX = maxX - minX;
		int sizeY = maxY - minY;
		if (sizeX > 0)) && (sizeY > 0)) {
			for (int i = minX; i <= maxX; i++;) {
				xRaster.add(i, new LinkedList());
			}
		} else {
			System.out.println("Karte ist 0 Felder lang oder breit!");
		}
	}
	
	private void visualisiere() {
		
	}
	
	public static void main(String[] args) {
	}
}
