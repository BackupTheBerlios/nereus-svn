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

/**
 * Der Key wird eingesetzt zur �berpr�fung der Identit�t eines Agenten.
 * 
 * @author Daniel Friedrich
 */
public class ActionKey {
	
	/**
	 * Schl�ssel.
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
