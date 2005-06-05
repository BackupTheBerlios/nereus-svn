/*
 * Dateiname      : Id.java
 * Erzeugt        : 20. Juni 2003
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
 * Die Klasse stellt eine Kaspelung der Id, als String dar. Zum Vergleich der
 * Ids gibt es eine eigene Equals-Methode. Id == String wurden aus folgenden
 * Gründen getrennt.
 * 1. Mit einem eigenen Id-Typ ist es möglich Methoden anzubieten die entweder
 * 	  den Namen oder die Id eines Agenten oder Spiels verwenden.
 * 2. Die Logik der Id vergabe liegt jetzt direkt bei der Klasse selbst.
 * 3. Alle Methoden der Klasse String die eine Manipulation der Id bewirken
 *    können werden hiermit ausgeschaltet.
 *
 * @author Daniel Friedrich
 */
public class Id implements Serializable {
    /**
     * String die die ID enthält.
     */
    private String m_Id;
    
    
    /**
     * Konstruktor.
     */
    public Id() {
        super();
        m_Id = Integer.toString(this.hashCode());
    }
    
    /**
     * Konstruktor.
     *
     * @param idString
     */
    public Id(String idString) {
        super();
        m_Id = idString;
    }
    
        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
    public boolean equals(Object obj) {
        if(obj instanceof Id) {
            return m_Id.equals(((Id)obj).toString());
        }
        return false;
    }
    
        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    public String toString() {
        return m_Id;
    }
    
    /**
     * Parsed einen String zu einer Id.
     *
     * @param idString
     * @return Id - Die aus dem geparsten String entstande Id.
     */
    public static Id parseString(String idString) {
        return new Id(idString);
    }
}
