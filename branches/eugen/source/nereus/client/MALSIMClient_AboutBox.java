/*
 * Dateiname      : MALSIMClient_AboutBox.java
 * Erzeugt        : 5. August 2003
 * Letzte Änderung: 19. April 2004 durch Eugen Volk
 * Autoren        : Daniel Friedrich
 *                  Eugen Volk
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

package nereus.registrationgui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * About Box für den Client
 * 
 * @author Daniel Friedrich
 */
public class MALSIMClient_AboutBox extends JDialog implements ActionListener {

  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();
  JPanel insetsPanel1 = new JPanel();
  JPanel insetsPanel2 = new JPanel();
  JPanel insetsPanel3 = new JPanel();
  JButton button1 = new JButton();
  JLabel imageLabel = new JLabel();
  JLabel label1 = new JLabel();
  JLabel label2 = new JLabel();
  JLabel label3 = new JLabel();
  JLabel label4 = new JLabel();
  ImageIcon image1 = new ImageIcon();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  GridLayout gridLayout1 = new GridLayout();
  String product = " Projekt Nereus (http://nereus.berlios.de/)  ";
  String version = " Version 0.9";
  String copyright = " GNU General Public License";
  String comments = " Nereus dient zur Simulation der MAS";
  public MALSIMClient_AboutBox(Frame parent) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
	 /**
	  * Oberfläche erstellen.
	  * 
	  * @throws Exception
	  */
	 private void jbInit() throws Exception  {
	// 	image1 = new ImageIcon(client.MASIMClient.class.getResource("about.png"));
	//    imageLabel.setIcon(image1);
	    this.setTitle("About");
	    panel1.setLayout(borderLayout1);
	    panel2.setLayout(borderLayout2);
	    insetsPanel1.setLayout(flowLayout1);
	    insetsPanel2.setLayout(flowLayout1);
	    insetsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    gridLayout1.setRows(4);
	    gridLayout1.setColumns(1);
	    label1.setText(product);
	    label2.setText(version);
	    label3.setText(copyright);
	    label4.setText(comments);
	    insetsPanel3.setLayout(gridLayout1);
	    insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    button1.setText("Ok");
	    button1.addActionListener(this);
	   // insetsPanel2.add(imageLabel, null);
	    panel2.add(insetsPanel2, BorderLayout.WEST);
	    this.getContentPane().add(panel1, null);
	    insetsPanel3.add(label1, null);
	    insetsPanel3.add(label2, null);
	    insetsPanel3.add(label3, null);
	    insetsPanel3.add(label4, null);
	    panel2.add(insetsPanel3, BorderLayout.CENTER);
	    insetsPanel1.add(button1, null);
	    panel1.add(insetsPanel1, BorderLayout.SOUTH);
	    panel1.add(panel2, BorderLayout.NORTH);
	    setResizable(true);
	  }
  	
  	/* (non-Javadoc)
   	 * @see java.awt.Window#processWindowEvent(java.awt.event.WindowEvent)
     */
  	protected void processWindowEvent(WindowEvent e) {
   		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      		cancel();
    	}
    	super.processWindowEvent(e);
  	}
  
  	/**
   	 * Schliesst den Dialog.
     */
  	void cancel() {
    	dispose();
  	}
  
  	/**
  	 * Eventhandling zum Schliessen des Dialogs.
  	 */
	public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == button1) {
      		cancel();
    	}
  	}
}