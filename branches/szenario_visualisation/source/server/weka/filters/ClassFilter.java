/*
 * Created on 22.07.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package weka.filters;

import weka.core.Instance;
import weka.filters.Filter;

/**
 * Mit diesem Filter wird die korrekte Klasse eines Beispiels entfernt.
 * Dies ist immer dann nötig, wenn der Agent die wahrnehmbaren Attributwerte 
 * eines Platzes erhält, damit der Agent zur Klassifikation nicht einfach die
 * Klasse ausliesst. 
 * 
 * @author Daniel Friedrich
 */
public class ClassFilter extends Filter{

	/**
	 * Das Beispiel, dessen Klasse weggefiltert werden soll.
	 */
	private Instance m_filteredInstance; 

	/**
	 * Konstruktor.
	 */
	public ClassFilter() {
		super();
	}

	/* (non-Javadoc)
	 * @see weka.filters.Filter#input(weka.core.Instance)
	 */
	public boolean input(Instance instance) throws Exception {
		try {
			// Schneide die Klasse ab.
			m_filteredInstance = new Instance(instance);
			m_filteredInstance.setDataset(instance.dataset()); 
			m_filteredInstance.setClassMissing();
			
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see weka.filters.Filter#output()
	 */
	public Instance output() {		
		return m_filteredInstance;
		
	}
}
