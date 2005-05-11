/*
 * Dateiname      : ParameterDescription.java
 * Erzeugt        : 17. Juli 2003
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

import java.io.Serializable;

/**
 * Mit der Klasse läßt sich ein Parameter beschreiben.
 *
 * @author Daniel Friedrich
 */
public class ParameterDescription implements Serializable {
    
    /**
     * @see java.security.PublicKey.serialVersionUID
     */
    static final long serialVersionUID = 1L;
    
        /*
         * Konstanten für die Typen des Parameters.
         */
    public static final int BooleanType = 0;
    public static final int StringType = 1;
    public static final int IntegerType = 2;
    public static final int DoubleType = 3;
    public static final int RealType = 4;
    public static final int FloatType = 5;
    
    /**
     * Name des Parameters.
     */
    private String m_name;
    
    /**
     * Typ des Parameters.
     */
    private int m_classDescription;
    
    /**
     * Defaultwert für den Parameter.
     */
    private Object m_defaultValue = null;
    
    /**
     * Gibt an ob der Parameter veränderbar ist.
     */
    private boolean m_isChangeable = true;
    
    /**
     * Konstruktor.
     *
     * @param name
     * @param classDescription
     */
    public ParameterDescription(String name, int classDescription) {
        super();
        m_name = name;
        m_classDescription = classDescription;
    }
    
    /**
     * Konstruktor.
     *
     * @param name
     * @param classDescription
     * @param value
     */
    public ParameterDescription(
            String name,
            int classDescription,
            Object value) {
        super();
        m_name = name;
        m_classDescription = classDescription;
        m_defaultValue = value;
    }
    
    /**
     * Konstruktor.
     *
     * @param name
     * @param classDescription
     * @param value
     * @param changeable
     */
    public ParameterDescription(
            String name,
            int classDescription,
            Object value,
            boolean changeable) {
        super();
        m_name = name;
        m_classDescription = classDescription;
        m_defaultValue = value;
        m_isChangeable = changeable;
    }
    
    /**
     * Gibt den Typ des Parameter zurück.
     *
     * @return int - Parametertyp
     */
    public int getClassDescription() {
        return m_classDescription;
    }
    
    /**
     * Gibt den Parameternamen zurück.
     *
     * @return String - Name des Parameters.
     */
    public String getParameterName() {
        return m_name;
    }
    
    /**
     * Gibt den Defaultwert für den Parameter zurück.
     *
     * @return Object - Defaultwert
     */
    public Object getDefaultValue() {
        return m_defaultValue;
    }
    
    /**
     * Gibt zurück, wenn der Parameter veränderbar ist.
     *
     * @return boolean
     */
    public boolean isChangeable() {
        return m_isChangeable;
    }
}