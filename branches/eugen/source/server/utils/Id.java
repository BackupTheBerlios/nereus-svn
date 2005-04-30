/*
 * Created on 20.06.2003
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

import java.io.Serializable;

/**
 * Die Klasse stellt eine Kaspelung der Id, als String dar. Zum Vergleich der 
 * Ids gibt es eine eigene Equals-Methode. Id == String wurden aus folgenden
 * Gr�nden getrennt. 
 * 1. Mit einem eigenen Id-Typ ist es m�glich Methoden anzubieten die entweder
 * 	  den Namen oder die Id eines Agenten oder Spiels verwenden.
 * 2. Die Logik der Id vergabe liegt jetzt direkt bei der Klasse selbst.
 * 3. Alle Methoden der Klasse String die eine Manipulation der Id bewirken
 *    k�nnen werden hiermit ausgeschaltet.
 * 
 * @author Daniel Friedrich
 */
public class Id implements Serializable {
	/**
	 * String die die ID enth�lt.
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
