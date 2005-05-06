/*
 * Created on 17.07.2003
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
 * Mit der Klasse l��t sich ein Parameter beschreiben.
 * 
 * @author Daniel Friedrich
 */
public class ParameterDescription implements Serializable {

	/*
	 * Konstanten f�r die Typen des Parameters.
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
	 * Defaultwert f�r den Parameter.
	 */
	private Object m_defaultValue = null;

	/**
	 * Gibt an ob der Parameter ver�nderbar ist.
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
	 * Gibt den Typ des Parameter zur�ck.
	 * 
	* @return int - Parametertyp
	*/
	public int getClassDescription() {
		return m_classDescription;
	}

	/**
	 * Gibt den Parameternamen zur�ck.
	 * 
	* @return String - Name des Parameters. 
	*/
	public String getParameterName() {
		return m_name;
	}

	/**
	 * Gibt den Defaultwert f�r den Parameter zur�ck.
	 * 
	* @return Object - Defaultwert
	*/
	public Object getDefaultValue() {
		return m_defaultValue;
	}

	/**
	 * Gibt zur�ck, wenn der Parameter ver�nderbar ist.
	 *  
	* @return boolean 
	*/
	public boolean isChangeable() {
		return m_isChangeable;
	}
}