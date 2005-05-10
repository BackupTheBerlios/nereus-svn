/*
 * Dateiname      : SelectionDialog.java
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
package nereus.registrationgui;

import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
 * Dialogkomponente zur Auswahl eines Strings aus einer vorgegebenen Menge 
 * von Strings.
 * 
 * @author Daniel Friedrich
 */
public class SelectionDialog extends JDialog {
	
	/**
	 * Auswahlmenge
	 */
	private Vector m_data;
	/**
	 * Auswahl
	 */
	private Object selection;
	/**
	 * Layoutmanager contentPanel
	 */
	private BorderLayout m_contentLayout = new BorderLayout();
	/**
	 * Layoutmanager headPanel
	 */
	private BorderLayout m_headLayout = new BorderLayout();
	/**
	 * HeadPanel
	 */
	private JPanel m_headPanel = new JPanel();
	/**
	 * Panel für die Buttons
	 */
	private JPanel m_buttonPanel = new JPanel();
	/**
	 * Platzhalter für zwischen die Buttons
	 */
	private JPanel m_placeHolderPanel = new JPanel();
	/**
	 * Panel für Labels
	 */
	private JPanel m_labelPanel = new JPanel();
	/**
	 * Panel für das Auswahlfenster
	 */
	private JPanel m_selectionPanel = new JPanel();
	/**
	 * Panel
	 */
	private JPanel m_bottomPHPanel = new JPanel();
	/**
	 * Label für die Selektion
	 */
	private JLabel m_selectionTextLabel = new JLabel();
	/**
	 * Text für die Selektion, steht im Label
	 */
	private String m_selectionText;
	/**
	 * Layoutmanager
	 */
	private BorderLayout m_buttonBorderLayout = new BorderLayout();
	/**
	 * Platzhalter
	 */
	private JPanel m_leftButtonPlaceHolder = new JPanel();
	/**
	 * Panel für Buttons
	 */
	private JPanel m_buttonPlace = new JPanel();
	/**
	 * Platzhalter
	 */
	private JPanel m_rightButtonPlaceHolder = new JPanel();
	/**
	 * Layoutmanager
	 */
	private BorderLayout m_buttonPlaceLayout = new BorderLayout();
	/**
	 * OK-Button für Auswahl
	 */
	private JButton m_okButton = new JButton();
	/**
	 * Abbrechen-Button
	 */
	private JButton m_cancelButton = new JButton();
	/**
	 * Border
	 */
	private TitledBorder titledBorder1;
	/**
	 * Layoutmanager
	 */
	private BorderLayout m_selectionLayout = new BorderLayout();
	/**
	 * Selektiontext
	 */
	private JLabel m_selectionTextL = new JLabel();
	/**
	 * Combobox zur Auswahl
	 */
	private JComboBox m_selectionBox;
	/**
	 * ausgewähltes Element.
	 */
	private Object m_selectedItem;

	/**
	 * Konstruktor.
	 * 
	 * @param title
	 * @param selectionText
	 * @param selectionItemText
	 * @param data
	 * @throws HeadlessException
	 */
	public SelectionDialog(
		String title,
		String selectionText,
		String selectionItemText,
		Vector data)
		throws HeadlessException {
		try {
			this.setTitle(title);
			if (selectionText == null) {
				selectionText = "Bitte wählen sie aus.";
			}
			if (selectionItemText == null) {
				selectionItemText = "Auswahl";
			}
			m_selectionText = selectionText;
			m_selectionTextL.setText(selectionItemText);
			m_data = data;
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Erstellt die GUI.
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		this.setModal(true);
		titledBorder1 = new TitledBorder("");
		this.setResizable(false);
		this.setSize(new Dimension(300, 160)); // Test ansonsten 300,130
		this.getContentPane().setLayout(m_contentLayout);
		m_selectionTextLabel.setText(m_selectionText);
		m_selectionBox = new JComboBox(m_data);
		m_labelPanel.setMaximumSize(new Dimension(300, 40));
		m_labelPanel.setMinimumSize(new Dimension(300, 40));
		m_labelPanel.setPreferredSize(new Dimension(300, 40));
		//m_selectionTextLabel.setText("LayoutTest");
		m_selectionTextLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		m_selectionTextLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		m_selectionTextLabel.setMaximumSize(new Dimension(300, 25));
		m_selectionTextLabel.setMinimumSize(new Dimension(300, 25));
		m_selectionTextLabel.setPreferredSize(new Dimension(300, 25));
		m_selectionTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		m_selectionTextLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		m_buttonPanel.setLayout(m_buttonBorderLayout);
		m_buttonPanel.setBorder(null);
		m_buttonPanel.setMaximumSize(new Dimension(80, 30));
		m_buttonPanel.setMinimumSize(new Dimension(80, 30));
		m_buttonPanel.setPreferredSize(new Dimension(80, 30));
		m_leftButtonPlaceHolder.setMaximumSize(new Dimension(70, 30));
		m_leftButtonPlaceHolder.setMinimumSize(new Dimension(70, 30));
		m_leftButtonPlaceHolder.setPreferredSize(new Dimension(70, 30));
		m_buttonPlace.setMaximumSize(new Dimension(120, 25));
		m_buttonPlace.setMinimumSize(new Dimension(120, 25));
		m_buttonPlace.setPreferredSize(new Dimension(120, 25));
		m_buttonPlace.setLayout(m_buttonPlaceLayout);
		m_rightButtonPlaceHolder.setBorder(null);
		m_rightButtonPlaceHolder.setMaximumSize(new Dimension(70, 30));
		m_rightButtonPlaceHolder.setMinimumSize(new Dimension(70, 30));
		m_rightButtonPlaceHolder.setPreferredSize(new Dimension(70, 30));
		m_okButton.setMaximumSize(new Dimension(75, 30));
		m_okButton.setMinimumSize(new Dimension(75, 30));
		m_okButton.setPreferredSize(new Dimension(75, 30));
		m_okButton.setActionCommand("m_okButton");
		m_okButton.setSelected(false);
		m_okButton.setText("OK");
		m_okButton.addActionListener(
			new SelectionDialog_m_okButton_actionAdapter(this));
		m_cancelButton.setMaximumSize(new Dimension(75, 30));
		m_cancelButton.setMinimumSize(new Dimension(75, 30));
		m_cancelButton.setPreferredSize(new Dimension(75, 30));
		m_cancelButton.setActionCommand("m_cancelButton");
		m_cancelButton.setText("Cancel");
		m_cancelButton.addActionListener(
			new SelectionDialog_m_cancelButton_actionAdapter(this));
		m_buttonPlaceLayout.setHgap(5);
		m_buttonPlaceLayout.setVgap(5);
		m_placeHolderPanel.setMaximumSize(new Dimension(300, 20));
		m_placeHolderPanel.setMinimumSize(new Dimension(300, 20));
		m_placeHolderPanel.setPreferredSize(new Dimension(300, 20));
		m_selectionPanel.setMaximumSize(new Dimension(300, 30));
		m_selectionPanel.setMinimumSize(new Dimension(300, 30));
		m_selectionPanel.setLayout(m_selectionLayout);
		m_headPanel.setBorder(null);
		m_headPanel.setMaximumSize(new Dimension(300, 90));
		m_headPanel.setMinimumSize(new Dimension(300, 90));
		m_headPanel.setPreferredSize(new Dimension(300, 90));
		m_bottomPHPanel.setBorder(null);
		m_bottomPHPanel.setMaximumSize(new Dimension(300, 10));
		m_selectionTextL.setMaximumSize(new Dimension(150, 20));
		m_selectionTextL.setMinimumSize(new Dimension(150, 20));
		m_selectionTextL.setPreferredSize(new Dimension(150, 20));
		//m_selectionTextL.setText("jLabel1");
		m_selectionBox.setMaximumSize(new Dimension(150, 20));
		m_selectionBox.setMinimumSize(new Dimension(150, 20));
		m_selectionBox.setPreferredSize(new Dimension(150, 20));
		m_labelPanel.add(m_selectionTextLabel, null);
		m_selectionPanel.setPreferredSize(new Dimension(300, 30));
		m_headPanel.setPreferredSize(new Dimension(300, 90));
		m_headPanel.setLayout(m_headLayout);
		m_headPanel.add(m_labelPanel, BorderLayout.NORTH);
		m_headPanel.add(m_selectionPanel, BorderLayout.CENTER);
		m_headPanel.add(m_placeHolderPanel, BorderLayout.SOUTH);
		this.getContentPane().add(m_headPanel, BorderLayout.NORTH);
		//this.getContentPane().add(m_placeHolderPanel, BorderLayout.CENTER);
		this.getContentPane().add(m_buttonPanel, BorderLayout.CENTER);
		this.getContentPane().add(m_bottomPHPanel, BorderLayout.SOUTH);
		m_bottomPHPanel.setPreferredSize(new Dimension(300, 10));
		m_buttonPanel.add(m_leftButtonPlaceHolder, BorderLayout.WEST);
		m_buttonPanel.add(m_buttonPlace, BorderLayout.CENTER);
		m_buttonPanel.add(m_rightButtonPlaceHolder, BorderLayout.EAST);
		m_buttonPlace.add(m_okButton, BorderLayout.WEST);
		m_buttonPlace.add(m_cancelButton, BorderLayout.EAST);
		m_selectionPanel.add(m_selectionTextL, BorderLayout.WEST);
		m_selectionPanel.add(m_selectionBox, BorderLayout.EAST);
	}

	/**
	 * Eventhandling für die Auswahl des selektierten Elements
	 * 
	 * @param e
	 */
	void m_okButton_actionPerformed(ActionEvent e) {
		m_selectedItem = m_selectionBox.getSelectedItem();
		this.setVisible(false);
	}
	
	/**
	 * Eventhandling für das Abbrechen der Selektion
	 * 
	 * @param e
	 */
	void m_cancelButton_actionPerformed(ActionEvent e) {
		m_selectedItem = null;
		this.setVisible(false);
	}

	/**
	 *Gestattet das Auslesen des selektierten elements nach dem Beenden des Dialogs.
	 * 
	 * @return Object - ausgewählte Element.
	 */
	public Object getSelection() {
		return m_selectedItem;
	}

}

/**
 * @author Daniel Friedrich
 *
 * Eventhandling-Adapterklasse für den OK-Button
 */
class SelectionDialog_m_okButton_actionAdapter
	implements java.awt.event.ActionListener {
	SelectionDialog adaptee;

	/**
	 * Konstruktor.
	 * 
	 * @param adaptee
	 */
	SelectionDialog_m_okButton_actionAdapter(SelectionDialog adaptee) {
		this.adaptee = adaptee;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		adaptee.m_okButton_actionPerformed(e);
	}
}

/**
 * @author Daniel Friedrich
 *
 * Eventhandling-Adapterklasse für den Cancel-Button
 */
class SelectionDialog_m_cancelButton_actionAdapter
	implements java.awt.event.ActionListener {
	SelectionDialog adaptee;

	/**
	 * Konstruktor.
	 * 
	 * @param adaptee
	 */
	SelectionDialog_m_cancelButton_actionAdapter(SelectionDialog adaptee) {
		this.adaptee = adaptee;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		adaptee.m_cancelButton_actionPerformed(e);
	}
}