/*
 * Dateiname      : DataTransferObject.java
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

import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Objekt zum Übertragen von Werten für die Parametereinstellungen eines
 * Spiels vom Client zum Server. Die Daten müssen serialisierbar sein.
 * 
 * @author Daniel Friedrich 
 */
public class DataTransferObject extends HashMap {

	/**
	 * Konstruktor.
	 * 
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public DataTransferObject(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param initialCapacity
	 */
	public DataTransferObject(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Konstruktor.
	 */
	public DataTransferObject() {
		super();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param Map m
	 */
	public DataTransferObject(Map m) {
		super(m);
	}
	
	/**
	 * Konstruktor. 
	 * 
	 * @param Hashtable parameters - zu übertragende Parameter
	 */
	public DataTransferObject(Hashtable parameter) {
		super();
		//HashMap map = new HashMap();
		Enumeration keys = parameter.keys();
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
			this.put(key, parameter.get(key) );
		}
	}
}
