/*
 * Dateiname      : SendHypothesisSpeachact.java
 * Erzeugt        : 7. September 2003
 * Letzte Änderung: 
 * Autoren        : Daniel Friedrich
 *                  
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
package nereus.communication.speachacts;

import java.util.Hashtable;

import weka.classifiers.Classifier;

import nereus.exceptions.InvalidElementException;


/**
 * Die Klasse repräsentiert einen Sprechakt zum Versenden einer Hypothesen.
 * 
 * Die Klasse ist nicht durch Vererbung erweiterbar, damit es den Agenten nicht 
 * möglich ist, einen Sprechakt einzusetzen, der sich als Subklasse dieses
 * Sprechaktes tarnt. Wenn eine Hierarchie unter dieser Klasse eingeführt werden 
 * soll, dann muss der Sprechakt als abstract definiert werden und die 
 * Subklassen dann wieder final.
 * 
 * @author Daniel Friedrich
 */
public final class SendHypothesisSpeachact extends SendSpeachact {

	/**
	 * Konstruktor.
	 * 
	 * @param parameters
	 */
	public SendHypothesisSpeachact(Hashtable parameters) {
		super(parameters);
	}

	/**
	 * Die Methode setzt den Inhalt des Sprechaktes. 
	 * 
	 * Der Inhalt dieses Sprechaktes kann nur aus einer einzelnen Hypothese 
	 * oder aus null bestehen. Wird versucht etwas anderes als Inhalt 
	 * zu übertragen, dann wird eine InvalidElementException geworfen. 
	 */
	public void setContent(Object content) throws InvalidElementException {
		if((content == null) || (content instanceof Classifier)) {
			m_content = content;
		}else {
			throw new InvalidElementException(
				"SendHypothesisSpeachact - fehlerhaftes Inhaltselement: " 
				+ content.toString());
		}
	}
}
