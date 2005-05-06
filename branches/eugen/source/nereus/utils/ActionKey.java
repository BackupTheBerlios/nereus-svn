/*
 * Dateiname      : ActionKey.java
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

/**
 * Der Key wird eingesetzt zur Überprüfung der Identität eines Agenten.
 * 
 * @author Daniel Friedrich
 */
public class ActionKey {
	
	/**
	 * Schlüssel.
	 */
	private String m_key;
	
	/**
	 * Konstruktor. 
	 */
	public ActionKey() {
		super();
		m_key = Integer.toString(this.hashCode());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof ActionKey) {
			return m_key.equals(((ActionKey)obj).toString());			
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return m_key;
	}
}
