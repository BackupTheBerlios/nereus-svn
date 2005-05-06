/*
 * Dateiname      : GraphPanel.java
 * Erzeugt        : 5. August 2003
 * Letzte Änderung: 
 * Autoren        : Daniel Friedrich
 *                  
 *                  
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut für Intelligente Systeme
 * der Universität Stuttgart unter Betreuung von Dietmar Lippold
 * (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package nereus.client;

import java.awt.Graphics;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JPanel;

/**
 * Grafische Komponente zum Anzeigen von Werte-Verläufe. 
 * 
 * @author Daniel Friedrich
 */
public class GraphPanel extends JPanel implements Serializable {
	/**
	 * Titel des Graphs
	 */
	private String m_title = "";
	
	/**
	 * Vector mit den Werten die dargestellt werden sollen, der Vector ist
	 * geordnet nach der Reihenfolge in der die Werte auf der X-Achse dargestellt
	 * werden. values[0] beschreibt also den ersten Wert der auf Punkt 0 der
	 * X-Achse liegt.
	 */
	private Vector m_values = new Vector();

	/**
	 * Beschreibt die x-Position im Panel an der die Stelle 0/0 des Koordinaten-
	 * systems liegt.
	 */
	private int m_xBase;

	/**
	 * Beschreibt die y-Position im Panel an der die Stelle 0/0 des Koordinaten-
	 * systems liegt.
	 */
	private int m_yBase;

	/**
	 * vertikaler Offset
	 */
	private int m_veOffset = 25;

	/**
	 * horizontaler Offset
	 */
	private int m_hoOffset = 20;

	/**
	 * Der maximal darstellbare Wert auf der X-Achse.
	 */
	private double m_maxXValue = 0;

	/**
	 * Der maximal darstellbare Wert auf der Y-Achse.
	 */
	private double m_maxYValue = 0;

	/**
	 * Beschreibt die x-Koordinate an der Spitze des Pfeiles der X-Achse.
	 */
	private int m_maxX = 0;

	/**
	 * Beschreibt die y-Koordinate an der Spitze des Pfeiles der Y-Achse.
	 */
	private int m_maxY = 0;

	/**
	 * Y-Label
	 */
	private String m_yLabel = "Y";

	/**
	 * X-Label
	 */
	private String m_xLabel = "X";

	/**
	 * Aktueller Stand des Graphen auf der X-Achse und damit die Anzahl der
	 * zu zeichnenden Werten.
	 */
	private int m_actualNumberOfValues = 0;

	/**
	 * Maximale Anzahl an darstellbaren Werten
	 */
	private int m_maxNumOfValues = 1;

	/**
	 * Länge der Y-Achse in Punkten
	 */
	private int m_lengthYAxis = 1;

	/**
	 * Länge der X-Achse in Punkten
	 */
	private int m_lengthXAxis = 1;

	/**
	 * Gibt an, ob die Werte auf der Y-achse skaliert werden können.
	 */
	private boolean m_isYScaleFixed = false;

	/**
	 * Overhead auf der Y-Achse
	 */
	private int m_yOverHead = 0;
	
	/**
	 * Startwert auf der Y-Achse
	 */
	private double m_startYValue = 0.0;
	
	/**
	 * max. anzeigbarer Wert auf der Y-Achse.
	 */
	private int m_maxPrintableYValue = 0;

	/**
	 * Konstruktor
	 */
	public GraphPanel() {
		super();
		// Ersten Wert schon mal vorinitialisieren.
		m_values.add(0, new Double(0));
	}

	/**
	 * Setzt den Titel des Graphen.
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		m_title = title;
	}

	/**
	 * Setzt den Namen des Labels der X-Achse.
	 *
	 * @param xAxisLabel
	 */
	public void setXAxisLabel(String xAxisLabel) {
		m_xLabel = xAxisLabel;
	}

	/**
	 * Setzt den Namen des Labels der Y-Achse.
	 *
	 * @param yAxisLabel
	 */
	public void setYAxisLabel(String yAxisLabel) {
		m_yLabel = yAxisLabel;
	}

	/**
	 * Setzt die max. Anzahl an darstellbaren Werten.
	 *
	 * @param nov
	 */
	public void setMaxNumOfValues(int nov) {
		m_maxNumOfValues = nov;
	}

	/**
	 * Setzt den maximalen Wert der auf der Y-Achse angezeigt werden kann.
	 *
	 * @param value
	 */
	public void setMaxYValue(double value) {
		m_maxYValue = value;
	}

	/**
	 * Skaliert den übergebenen Wert auf die Größe der Y-Achse.
	 *
	 * @param value
	 * @return in
	 */
	private int scaleYValue(double value) {

		return (new Double(((double)m_maxPrintableYValue) / ((double)m_maxYValue) * value).intValue());
		//return (new Double(((double)(m_lengthYAxis - m_yOverHead)) / ((double)m_maxYValue) * value).intValue());
		/*
		return (
			(new Double(((m_lengthYAxis - m_yOverHead) / m_maxYValue) * value))
				.intValue());*/
	}

	/**
	 * Skaliert den übergebenen Wert auf die Größe der X-Achse
	 *
	 * @param value
	 * @return in 
	 */
	private int scaleXValue(int value) {
		return (
			(new Double((m_lengthXAxis / (m_maxNumOfValues + 1)) * value))
				.intValue());
	}

	/**
	 * Fügt einen neuen Eintrag auf dem Display hinzu.
	 * 
	 * @param value - hinzuzufügender Wert
	 * @throws Exception
	 */
	public void addValueDoDisplay(double value) throws Exception {
		/*
		 * kontrolliere ob noch Platz im Graph ist, wenn ja füge den Eintrag
		 * hinzu, ansonsten werfe ein Exception.
		 */
		if (m_actualNumberOfValues < m_maxNumOfValues + 1) {
			m_actualNumberOfValues++;
			m_values.add(m_actualNumberOfValues, new Double(value));		
			repaint();
		} else {
			// Display ist schon voll
			System.out.println("Displaying ging schief.");
			// Display ist schon voll
			System.out.println("Displaying ging schief.");
			System.out.println(
				"Max. Werte: " 
				+ m_maxNumOfValues 
				+ " dies aber Eintrag " 
				+ m_actualNumberOfValues);			
		}
	}

	/**
	 * Aktualisiert alle Bilddaten.
	 */
	private void refreshData() {
		// Reset der beiden Basispunkte zum berechnen des Graphs
		m_yBase = this.getHeight() - m_hoOffset;
		m_xBase = 0 + m_veOffset;

		/*
		 * Berechnen der Endpunkte und speichern dieser als Maxwerte für die
		 * Achsen.
		 */
		m_maxX = this.getWidth() - (2 * m_veOffset); // X- Endpunkt des Graphen
		m_maxY = m_hoOffset; // Y-Endpunkt des Graphen
		m_lengthXAxis = m_maxX - m_xBase; // Länge der X-Achse
		m_lengthYAxis = m_yBase - m_maxY -10; // Länge der Y-Achse
	}

	/**
	 * Zeichnet die Koordinatenachsen.
	 */
	private void drawAxis(Graphics g) {
		//		male die Achsen
		// X-Achse
		g.drawLine(m_xBase, m_yBase, m_maxX, m_yBase);
		// Pfeil
		Polygon xArrow = new Polygon();
		xArrow.addPoint(m_xBase - 3, m_maxY + 5);
		xArrow.addPoint(m_xBase, m_maxY);
		xArrow.addPoint(m_xBase + 3, m_maxY + 5);
		g.drawPolygon(xArrow);

		// Y-Achse
		g.drawLine(m_xBase, m_yBase, m_xBase, m_maxY);
		// Pfeil
		Polygon yArrow = new Polygon();
		yArrow.addPoint(m_maxX - 5, m_yBase - 3);
		yArrow.addPoint(m_maxX, m_yBase);
		yArrow.addPoint(m_maxX - 5, m_yBase + 3);
		g.drawPolygon(yArrow);
	}

	/**
	 * Gibt an, ob der Wert neu skaliert werden muss. 
	 * 
	 * @param value
	 * @return boolean - True neu skalieren 
	 */
	private boolean calculate(double value) {
		double result = m_maxYValue / value;
		if (result - (new Double(result)).intValue() > 0.3) {
			return false;
		}
		return true;
	}

	/**
	 * Zeichnet die Labels an der Y-Achse.
	 */
	private void drawYAxisLabels(Graphics g) {
		// zeichne das Achsen-Label
		g.drawString(m_yLabel, m_xBase - 20, m_maxY - 5);		
			
		int numOfLabels = 10;
		// Abstand zwischen den Labels bestimmen
		int distance =
			(new Double(m_lengthYAxis / (numOfLabels + 1))).intValue();
			
		m_yOverHead = (new Double(distance)).intValue();	
		int value = 0;
		for (int a = 1; a < numOfLabels + 1; a++) {
		
			g.drawLine(
				m_xBase - 2,
				m_yBase - (a * distance),
				m_xBase + 2,
				m_yBase - (a * distance));
	
			int tmpValue =
				(new Double(m_maxYValue / numOfLabels * a)).intValue();
		
			g.drawString(
				Integer.toString(tmpValue),
				m_xBase - 25,
				m_yBase - (a * distance) + 5);

			value = (a * distance);
		}
		m_maxPrintableYValue = value;
	}

	/**
	 * Zeichnet die Labels der X-Achse
	 * @param g
	 */
	private void drawXAxisLabels(Graphics g) {
		// zeichne das Achsen-Label
		g.drawString(m_xLabel, m_maxX - 20, m_yBase + 20);
		// Abstand zwischen Labels berechnen
		double distance = m_lengthXAxis / (m_maxNumOfValues + 1);
		// Labels beschreiben, die Position 0/0 bekommt kein Label
		for (int i = 1; i < m_maxNumOfValues + 1; i++) {
			g.drawString(
				Integer.toString(i),
				(new Double(m_xBase - 5 + (i * distance))).intValue(),
				m_yBase + 15);
			g.drawLine(
				(new Double(m_xBase + (i * distance))).intValue(),
				m_yBase - 2,
				(new Double(m_xBase + (i * distance))).intValue(),
				m_yBase + 2);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		// male den normalen Panel-Background
		super.paintComponent(g);

		// Refresh der Daten
		refreshData();
		// Koordinatensystem zeichnen
		drawAxis(g);
		drawXAxisLabels(g);
		drawYAxisLabels(g);
		if (!m_isYScaleFixed) {
			// Massstab der Y-Achse bestimmen
			boolean dimension = true;
			while (dimension) {
				/*
				 * Im Optimalfall, d.h. wenn kein Wert größer als die Achsenlänge
				 * ist, reicht ein Durchlauf, deshalb bis auf Wiederruf Flag zum
				 * Beenden setzten.
				 */
				dimension = false;
				// jeden Wert von 0 bis aktuellen(letzten) Wert bearbeiten
				for (int i = 0; i < m_actualNumberOfValues + 1; i++) {
					// Dimension kontrollieren
					if (((Double) m_values.get(i)).doubleValue()
						> (0.8 * m_maxYValue)) {
						// nochmal durchlaufen
						dimension = true;
						// Maximaldarstellbarer Wert auf der Y-Achse erhöhen
						m_maxYValue = m_maxYValue * 1.2;
					}
				}
			}
		}

		// Werte einzeichnen
		for (int i = 1; i < m_actualNumberOfValues + 1; i++) {
			g.drawLine(m_xBase + scaleXValue(i - 1),
			// x-Wert des letzten Punkten
			m_yBase - scaleYValue(((Double) m_values.get(i - 1)).doubleValue()),
				m_xBase + scaleXValue(i),
				m_yBase
					- scaleYValue(((Double) m_values.get(i)).doubleValue()));
		}
	}

	/**
	 * Setzt den Startwert auf der Y-Achse
	 * 
	 * @param value - Startwert auf der Y-Achse
	 */
	public void setStartYValue(double value) {
		m_values.remove(0);
		m_values.add(0, new Double(value));
		m_startYValue = value;
	}


	/**
	 * Setzt ob die Y-Achse skaliert werden soll. 
	 * 
	 * @param value
	 */
	public void setFixedYScale(boolean value) {
		m_isYScaleFixed = value;
	}

	/**
	 * Setzt alle Werte auf Null.
	 */
	public void setToNullNumofValues() {
		m_values.clear();
		m_values.add(0, new Double(0));
		m_actualNumberOfValues = 0;
	}
	
	/**
	 * Führt ein reset aus.
	 */
	public void reset() {
		m_actualNumberOfValues = 0;
		m_values.clear();
		m_values.add(0, new Double(m_startYValue));		
	}
}