/*
 * Created on 29.07.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package speachacts;

import java.util.Hashtable;
import java.util.Vector;

import utils.Id;
import exceptions.InvalidElementException;

/**
 * Einzelnachricht zum Versenden von Ids.
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich.
 */
public final class SendIdSpeachact extends SendSpeachact {
	
	/**
	 * Der zu übertragende Schlüssel.
	 */
	private Object m_key = null; 
	
	/**
	 * Konstruktor.
	 */
	public SendIdSpeachact(Hashtable params) {
		super(params);
	}

	/**
	 * Liefert den Key für die Id.
	 */
	public Object getKey() {
		return m_key;
	}
	/**
	 * Die Methode setzt den Inhalt des Sprechaktes. 
	 * 
	 * Der Inhalt dieses Sprechaktes kann nur aus einer Id, einem Vector mit Ids
	 * oder null bestehen. Wird versucht etwas anderes als Inhalt 
	 * zu übertragen, dann wird eine InvalidElementException geworfen. 
	 */
	public void setContent(Object content) throws InvalidElementException {
		if( (content == null) 
			|| (content instanceof Id)
			|| (((content instanceof Vector)  
				&& ((((Vector)content).size() == 0) 
					|| ((((Vector)content).size() > 0) 
						&& (((Vector)content).get(0) instanceof Id)))))) {
			m_content = content;
		}else {
			throw new InvalidElementException(
				"SendHypothesisSpeachact - fehlerhaftes Inhaltselement: " 
				+ content.toString());
		}
	}	

}
