/*
 * Dateiname      : JParameterPanel.java
 * Erzeugt        : 5. August 2003
 * Letzte �nderung: am 25.06.05 durch Eugen Volk
 * Autoren        : Daniel Friedrich
 *
 *
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut f�r Intelligente Systeme
 * der Universit�t Stuttgart unter Betreuung von Dietmar Lippold
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Dialogkomponente zur Auswahl von vorgegebenen Werten.
 *
 * @author Daniel Friedrich
 */
public class JSelectionDialog extends JDialog {
    
    /**
     * Button zur Best�tigung der Auswahl
     */
    private JButton m_okButton = null;
    
    /**
     * Panel mit dem darzustellenden Parameter.
     */
    private JParameterPanel	m_panel = null;
    
    /**
     * Ausgew�hltes Element.
     */
    private Object m_selected = null;
    
    /**
     * Auswahlliste
     */
    private LinkedList m_selectables = new LinkedList();
    
    /**
     * Name des Parameters
     */
    private String m_name = "";
    
    
    /**
     * Konstruktor.
     *
     * @param owner
     * @param title
     * @param name
     * @param selectables
     * @throws HeadlessException
     */
    public JSelectionDialog(Frame owner, String title, String name, LinkedList selectables) throws HeadlessException {
        super(owner, title ,true);
        m_selectables = selectables;
        m_name = name;
        this.jbInit();
    }
    
     /**
     * Konstruktor.
     *
     * @param owner
     * @param title
     * @param name
     * @param selectables
     * @param width Breite des Fensters, default ist 200
     * @throws HeadlessException
     */
    public JSelectionDialog(Frame owner, String title, String name, LinkedList selectables, int width) throws HeadlessException {
        super(owner, title ,true);
        m_selectables = selectables;
        m_name = name;
        this.jbInit();
           
        Dimension ownDimension = new Dimension(width,90);
        this.setSize(ownDimension);
        this.setBounds(650,500,ownDimension.width,ownDimension.height);
    }
    
    
    
    /**
     * Erstellt die Oberfl�che.
     */
    private void jbInit() {
        
        m_panel = new JParameterPanel(
                m_name + ":",
                "ComboBox",
                m_selectables,
                100,
                20,
                100,
                20);
        m_panel.setSize(new Dimension(200,20));
        m_okButton = new JButton("OK");
        m_okButton.setBorder(BorderFactory.createRaisedBevelBorder());
        m_okButton.setActionCommand("OK");
        m_okButton.setText("  OK   ");
        m_okButton.setSize(new Dimension(100,30));
        m_okButton.addActionListener(new JSelectionDialog_m_okButton_actionAdapter(this));
        GridLayout gridLayout = new GridLayout(1,3,10,10);
        JPanel panel = new JPanel();
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setMaximumSize(new Dimension(100,30));
        buttonPanel.setMinimumSize(new Dimension(100,30));
        buttonPanel.setPreferredSize(new Dimension(100,30));
        buttonPanel.add(m_okButton);
        leftPanel.setMaximumSize(new Dimension(50,30));
        leftPanel.setMinimumSize(new Dimension(50,30));
        leftPanel.setPreferredSize(new Dimension(50,30));
        rightPanel.setMinimumSize(new Dimension(50,30));
        rightPanel.setMaximumSize(new Dimension(50,30));
        rightPanel.setPreferredSize(new Dimension(50,30));
        panel.setLayout(gridLayout);
        panel.add(leftPanel);
        panel.add(buttonPanel);
        panel.add(rightPanel);
        
        BorderLayout layout = new BorderLayout();
        this.getContentPane().setLayout(layout);
        this.getContentPane().add(m_panel,BorderLayout.NORTH);
        this.getContentPane().add(panel, BorderLayout.SOUTH);
        Dimension ownDimension = new Dimension(200,90);
        this.setSize(ownDimension);
        this.setBounds(650,500,ownDimension.width,ownDimension.height);
    }
    
    /**
     * Eventhandling f�r den OK-Button.
     *
     * @param e
     */
    public void m_okButton_actionPerformed(ActionEvent e) {
        m_selected = m_panel.getParameterValue();
        this.dispose();
    }
    
    /**
     * Liefert den selektierten Wert zur�ck.
     *
     * @return Object
     */
    public Object getSelected() {
        return m_selected;
    }
}

/**
 * @author Daniel Friedrich
 *
 * Eventhandling-Adapterklasse f�r den OK-Button.
 */
class JSelectionDialog_m_okButton_actionAdapter implements java.awt.event.ActionListener {
    JSelectionDialog adaptee;
    
    /**
     * Konstruktor.
     *
     * @param adaptee
     */
    JSelectionDialog_m_okButton_actionAdapter(JSelectionDialog adaptee) {
        this.adaptee = adaptee;
    }
    
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
    public void actionPerformed(ActionEvent e) {
        adaptee.m_okButton_actionPerformed(e);
    }
}
