/*
 * Created on 07.07.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package utils;

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
