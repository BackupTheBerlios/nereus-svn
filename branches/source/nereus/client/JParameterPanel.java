/*
 * Dateiname      : JParameterPanel.java
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * Kleine grafische Komponente zur Visualsierung eines Parameters und seines
 * Parameterwertes.
 *
 * @author Daniel Friedrich
 */
public class JParameterPanel extends JPanel {
    
    /**
     * Layoutmanager für die Komponente
     */
    private BorderLayout m_panelLayout = new BorderLayout();
    
    /**
     * Name des darzustellenden Parameters.
     */
    private String m_parameterName;
    /**
     * Name der Elternkomponente
     */
    private String m_componentName;
    /**
     * Elternkomponente
     */
    private Component m_component;
    /**
     * Label für Komponente
     */
    private JLabel m_label;
    /**
     * Parameterwert
     */
    private Object m_values;
    /**
     * Breite des Textlabels
     */
    private int m_labelWidth;
    /**
     * Höhe des Textlabels
     */
    private int m_labelHeight;
    /**
     * Breite der Komponente
     */
    private int m_compWidth;
    /**
     * Höhe der Komponente
     */
    private int m_compHeight;
    
    /**
     * Konstruktor.
     *
     * @param parameterName
     * @param componentName
     * @param values
     * @param lwidth
     * @param lheight
     * @param cwidth
     * @param cheight
     */
    public JParameterPanel(
            String parameterName,
            String componentName,
            Object values,
            int lwidth,
            int lheight,
            int cwidth,
            int cheight) {
        m_parameterName = parameterName;
        m_componentName = componentName;
        m_values = values;
        m_labelWidth = lwidth;
        m_labelHeight = lheight;
        m_compWidth = cwidth;
        m_compHeight = cheight;
        try {
            jbInit();
        }catch(Exception e) {
            System.out.println("Fehler: Der Parameterpanel für den Parameter "
                    + parameterName + " konnte nicht erstellt werden.");
            e.printStackTrace(System.out);
        }
    }
    
    /**
     * Erstellt die Oberfläche.
     *
     * @throws Exception
     */
    private void jbInit() throws Exception  {
        m_label = new JLabel(m_parameterName,JLabel.LEFT);
        if(m_componentName.equals("ComboBox")) {
            if((m_values != null) && (m_values instanceof LinkedList)) {
                Vector content = new Vector();
                ListIterator iterator = ((LinkedList)m_values).listIterator();
                while(iterator.hasNext()) {
                    content.addElement(iterator.next());
                }
                m_component = new JComboBox(content);
            }else {
                m_component = new JComboBox();
                ((JComboBox)m_component).setPreferredSize(
                        new Dimension(m_compWidth, m_compHeight));
                ((JComboBox)m_component).setMaximumSize(
                        new Dimension(m_compWidth, m_compHeight));
                ((JComboBox)m_component).setMinimumSize(
                        new Dimension(m_compWidth, m_compHeight));
            }
        }else if(m_componentName.equals("Checkbox")) {
            m_component = new JCheckBox();
            ((JCheckBox)m_component).setPreferredSize(
                    new Dimension(m_compWidth, m_compHeight));
            ((JCheckBox)m_component).setMaximumSize(
                    new Dimension(m_compWidth, m_compHeight));
            ((JCheckBox)m_component).setMinimumSize(
                    new Dimension(m_compWidth, m_compHeight));
            if((m_values != null) && (m_values instanceof Boolean)) {
                ((JCheckBox)m_component).setSelected(
                        ((Boolean)m_values).booleanValue());
            }
        }else {
            m_component = new JTextField("", 20);
            ((JTextField)m_component).setEditable(true);
            ((JTextField)m_component).setPreferredSize(
                    new Dimension(m_compWidth, m_compHeight));
            ((JTextField)m_component).setMaximumSize(
                    new Dimension(m_compWidth, m_compHeight));
            ((JTextField)m_component).setMinimumSize(
                    new Dimension(m_compWidth, m_compHeight));
            if((m_values != null) && (m_values instanceof String)) {
                ((JTextField)m_component).setText((String)m_values);
            }
        }
       
    //    this.setLayout(new java.awt.GridLayout(1, 2));
        this.setLayout(m_panelLayout);
        
        m_label.setPreferredSize(new Dimension(m_labelWidth, m_labelHeight));
        m_label.setMaximumSize(new Dimension(m_labelWidth, m_labelHeight));
        m_label.setMinimumSize(new Dimension(m_labelWidth, m_labelHeight));
        
        m_component.setSize(new Dimension(m_compWidth, m_compHeight));
        this.add(m_label,BorderLayout.WEST);
        this.add(m_component, BorderLayout.EAST);
       
        
    }
    
    /**
     * Liefert den Namen des Parameters den die Komponente betreut zurück.
     * @return String - Parametername
     */
    public String getParameterName() {
        return m_parameterName;
    }
    
    /**
     * Setzt die Eigenschaft, ob der Parameter editierbar ist.
     * @param ed
     */
    public void setEditable(boolean ed) {
        if (m_component instanceof JTextField) {
            ((JTextField) m_component).setEditable(ed);
        } else if (m_component instanceof JComboBox) {
            ((JComboBox) m_component).setEditable(ed);
        } else if (m_component instanceof JCheckBox) {
            ((JCheckBox) m_component).setEnabled(false);
        }
    }
    
    /**
     * Liefert den Werte des von der Komponente dargestellten Parameters zurück.
     * @return Object - Parameterwert
     */
    public Object getParameterValue() {
        if (m_componentName.equals("ComboBox")) {
            JComboBox jb = (JComboBox) m_component;
            return jb.getSelectedItem();
        } else if (m_componentName.equals("Checkbox")) {
            JCheckBox jc = (JCheckBox) m_component;
            return new Boolean(jc.isSelected());
        } else {
            return ((JTextField) m_component).getText();
        }
    }
}