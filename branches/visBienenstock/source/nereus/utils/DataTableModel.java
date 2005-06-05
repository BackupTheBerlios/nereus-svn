/*
 * Dateiname      : DataTableModel.java
 * Erzeugt        : 7. Juli 2003
 * Letzte Änderung:
 * Autoren        : Daniel Friedrich
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
package nereus.utils;

import java.util.Vector;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Implementierung des Standardtabellenmodells zum Füllen der Tabellen für die
 * Clients.
 *
 * @author Daniel Friedrich
 */
public class DataTableModel extends DefaultTableModel implements TableModel {
    
    /**
     * Die Zeilen des Modells.
     */
    private Vector m_rows = new Vector();
    
    /**
     * Anzahl der Zeilen.
     */
    private int m_rowCount = 0;
    
    /**
     * Anzahl der Spalten.
     */
    private int m_columnCount = 0;
    
    /**
     * Header der Tabelle.
     */
    private Vector m_headers = new Vector();
    
    /**
     * Gibt an, ob die Tabelle editierbar ist.
     */
    private boolean m_editable = true;
    
    /**
     * Konstruktor.
     *
     * @param editable - editierbar
     */
    public DataTableModel(boolean editable) {
        super();
        m_editable = editable;
    }
    
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getRowCount()
         */
    public int getRowCount() {
        return m_rowCount;
    }
    
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnCount()
         */
    public int getColumnCount() {
        return m_columnCount;
    }
    
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] row = (Object[]) m_rows.get(rowIndex);
        return row[columnIndex];
    }
    
        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#findColumn(java.lang.String)
         */
    public int findColumn(String columnName) {
        for (int i = 0; i < m_headers.size(); i++) {
            if (columnName.equals((String) m_headers.get(i))) {
                return i;
            }
        }
        return -1;
    }
    
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
         */
    public void addTableModelListener(TableModelListener l) {
        super.addTableModelListener(l);
    }
    
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnClass(int)
         */
    public Class getColumnClass(int columnIndex) {
        Object[] row = (Object[]) m_rows.get(0);
        return (row[columnIndex]).getClass();
    }
    
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnName(int)
         */
    public String getColumnName(int column) {
        return (String) m_headers.get(column);
    }
    
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#isCellEditable(int, int)
         */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return m_editable;
    }
    
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
         */
    public void removeTableModelListener(TableModelListener l) {
        super.removeTableModelListener(l);
    }
    
        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
         */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object[] row = (Object[]) m_rows.get(rowIndex);
        row[columnIndex] = aValue;
    }
    
    /**
     * Fügt eine neue Spalte dazu.
     *
     * @param columns - hinzuzufügente Spalte
     */
    public void addRow(Object[] columns) {
        m_rows.addElement(columns);
        m_rowCount++;
    }
    
    /**
     * Liefert die Daten der Zeile column.
     *
     * @param column - Zeilennummer
     * @return Object[] - Daten der Zeile
     */
    public Object[] getRowData(int column) {
        return (Object[]) m_rows.get(column);
    }
    
    /**
     * Fügt den Headers einen neuen Spaltennamen hinzu.
     *
     * @param String columnName - Spaltenname
     */
    public void addHeader(String columnName) {
        m_columnCount++;
        m_headers.addElement(columnName);
    }
    
    /**
     * Setzt den Namen der Spalte pos.
     *
     * @param int pos - Position
     * @param String columnName - Name der Spalte
     */
    public void addHeader(int pos, String columnName) {
        m_headers.add(pos, columnName);
    }
}
