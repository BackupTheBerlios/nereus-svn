/*
 * Created on 07.07.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut f�r Intelligente Systeme, Universit�t Stuttgart (2003)
 */
package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Objekt zum �bertragen von Werten f�r die Parametereinstellungen eines
 * Spiels vom Client zum Server. Die Daten m�ssen serialisierbar sein.
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
	 * @param Hashtable parameters - zu �bertragende Parameter
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
