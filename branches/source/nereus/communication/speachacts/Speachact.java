/*
 * Dateiname      : Speachact.java
 * Erzeugt        : 19. Juni 2003
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
import java.util.LinkedList;
import java.util.Vector;

import nereus.utils.Id;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import nereus.exceptions.InvalidElementException;


/**
 * Abstrakte Basisklasse für die unterschiedlichen Sprechakte.
 * 
 * @author Daniel Friedrich
 */
abstract public class Speachact {
	/**
	 * Konstante zur Abfrage der CommKosten
	 */
	public static final String SINGLECOMMCOSTS = "EnergyForCommunication";
	/**
	 * Konstante für Abfrage der Antwortkosten
	 */
	public static final String ANSWERCOSTS = "EnergyForAnswers";
	/**
	 * Konstante zur Abfrage der Kosten für Facilitation-Messages
	 */
	public static final String FACILITATIONCOSTS = "EnergyForFacilitationMsgs";

	/**
	 * Berechnungsfaktor für Beispiele, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten für eine Nachricht.
	 */
	private double m_examplesFactor = 1.0;		
	
	/**
	 * Berechnungsfaktor für Hypothesen, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten für eine Nachricht.
	 */
	private final double m_hypothesisFactor = 0.5;
	
	/**
	 * Berechnungsfaktor für Hintergrundwissen, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten für eine Nachricht.
	 */
	private double m_backgroundKnowledgeFactor = 0.25;	
	
	/**
	 * Berechnungsfaktor für Entscheidungen, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten für eine Nachricht.
	 */
	private double m_decisionFactor = 0.25;
	
	/**
	 * Berechnungsfaktor für leere Nachrichten.
	 */
	private double m_emptyMessageFactor = 0.25;	
	
	/**
	 * Berechnungsfaktor für leere Nachrichten.
	 */
	private double m_otherContentFactor = 1;	
	
	/**
	 * Parametername zum Durchreichen des Hypothesenfaktors. 
	 */
	public final String HYPOTHESISFACTOR = "HypothesisFactor";

	/**
	 * Parametername zum Durchreichen des Beispielfaktors. 
	 */
	public final String EXAMPLESFACTOR = "ExamplesFactor";	

	/**
	 * Parametername zum Durchreichen des Entscheidungsfaktors. 
	 */
	public final String DECISIONSFACTOR = "DecisionsFactor";
	
	/**
	 * Parametername zum Durchreichen des Entscheidungsfaktors. 
	 */
	public final String BACKGROUNDKNOWLEDGEFACTOR = "BackgroundKnowledgeFactor";			
	
	/**
	 * Parametername zum Durchreichen des Faktors für leere Nachrichten. 
	 */
	public final String EMPTYMESSAGEFACTOR = "EmptyMessageFactor";
	
	/**
	 * Parametername zum Durchreichen des Faktors für andere Inhalte. 
	 */
	public final String OTHERCONTENTFACTOR = "OtherContentFactor";				

	/**
	 * Id des Senders
	 */
    private Id m_sender;
    
    /**
     * Ids des Empfänger des Sprechakts
     */
	private Id m_receiver = null;
	
	/**
	 * Inhalt des Sprechakts
	 */
	protected Object m_content = null;
	
	/**
	 * Parameter des Spiels
	 */
	protected Hashtable m_parameters;
	
	/**
	 * Basiskosten für eine Nachricht.
	 */
	private double m_commCosts = 0.0;
	
	/**
	 * Basiskosten für eine Antwort.
	 */
	private double m_answerCosts = 0.0;
		
	/**
	 * Basiskosten für Facilitationnachrichten.
	 */
	private double m_facilitationCosts = 0.0;
	
	/**
	 * Werden für eine Antwort auch Kosten berechnet?, der Std. ist nein.
	 */
	private boolean m_extraCostsForAnswers = false;
	
	/**
	 * Werden für Facilitationnachrichten extra Kosten berechnet?, 
	 * der Std. ist nein.
	 */
	private boolean m_extraCostsForFacilitation = false;
	
	
	/**
	 * Konstruktor
	 * 
	 * @param Id sender - Id des Senders
	 * @param Id receiver - Id des Empfängers
	 */    
    public Speachact(Hashtable parameters) {
    	m_parameters = parameters;
		if(m_parameters.containsKey(SINGLECOMMCOSTS)) {
			m_commCosts = 
				((Double)m_parameters.get(SINGLECOMMCOSTS)).doubleValue();
		}
		if(m_parameters.containsKey(ANSWERCOSTS)) {
			m_answerCosts = 
				((Double)m_parameters.get(ANSWERCOSTS)).doubleValue();
		}
		if(m_parameters.containsKey(FACILITATIONCOSTS)) {
			m_facilitationCosts =
				((Double)m_parameters.get(FACILITATIONCOSTS)).doubleValue();
		}	
		if(m_parameters.containsKey(HYPOTHESISFACTOR)) {
			m_commCosts = 
				((Double)m_parameters.get(HYPOTHESISFACTOR)).doubleValue();
		}
		if(m_parameters.containsKey(EXAMPLESFACTOR)) {
			m_examplesFactor = 
				((Double)m_parameters.get(EXAMPLESFACTOR)).doubleValue();
		}
		if(m_parameters.containsKey(DECISIONSFACTOR)) {
			m_decisionFactor =
				((Double)m_parameters.get(DECISIONSFACTOR)).doubleValue();
		}
		if(m_parameters.containsKey(EMPTYMESSAGEFACTOR)) {
			m_emptyMessageFactor =
				((Double)m_parameters.get(EMPTYMESSAGEFACTOR)).doubleValue();
		}	
		if(m_parameters.containsKey(OTHERCONTENTFACTOR)) {
			m_otherContentFactor =
				((Double)m_parameters.get(OTHERCONTENTFACTOR)).doubleValue();
		}
		if(m_parameters.containsKey(BACKGROUNDKNOWLEDGEFACTOR)) {
			m_backgroundKnowledgeFactor =
				((Double)m_parameters.get(BACKGROUNDKNOWLEDGEFACTOR)).doubleValue();
		}									
    }
    
    /**
     * Liefert die Kosten für einen Facilitationnachricht zurück.
	 * @return double
	 */
	protected final double getFacilitationCosts() {
		if(m_extraCostsForFacilitation) {
			return m_facilitationCosts;
		}
    	return m_commCosts;
    }
    
	/**
	 * Liefert die Basiskosten für eine Nachricht zurück.
	 * @return double
	 */    
    protected final double getCommCosts() {
    	return m_commCosts;
    }
    
	/**
	 * Liefert die Basiskosten für eine Anwortnachricht zurück.
	 * @return double
	 */      
	protected final double getAnswerCosts() {
		return m_answerCosts;
	}
	
    /**
     * Gibt an ob die Nachricht eine Rückantwort verlangt.
     * 
	 * @return boolean
	 */
	public abstract boolean answerRequired();
	
    /**
     * Gibt an ob die Nachricht eine Antwort ist.
     * 
	 * @return boolean
	 */
	public abstract  boolean isAnswer();
	
    /**
     * Gibt den Inhalt der Nachricht zurück.
     * 
	 * @return Object
	 */
	public Object getContent() {
		return m_content;
	}
	
	/**
	 * Gibt den Sender der Nachricht zurück.
	 * 
	 * return Id;
	 */
    public Id getSender() {
    	return m_sender;
    }
    
    /**
	 * Gibt den Empfänger der Nachricht zurück.
	 * 
	 * return Id;
	 */
	public Id getReceiver() {
    	return m_receiver;
    }
    
    /**
     * Setzt den Inhalt der Nachricht.
	 * 
	 * Für jeden Sprechakt gibt es spezielle Einschränkungen.
     * 
     * @param Object content
     */
	public abstract void setContent(Object content) 
		throws InvalidElementException; 
	
	/**
	 * Setzt die Id des Senders der Nachricht.
	 * 
	 * @param Id sender - Id des Senders
	 */
	public void setSender(Id sender) {
		m_sender = sender;
	}
	   
	/**
	 * Setzt die Id des Empfängers der Nachricht.
	 * 
	 * @param Id receiver - Id des Empfängers
	 */	
	public void setReceiver(Id receiver) {
		m_receiver = receiver;
	}
	
	/**
	 * Berechnet die Kosten eines Kommunikationsvorgangs.
	 * 
	 * @return double - berechnete Kosten.
 	 */
	public abstract double calculateCosts();
	
	/**
	 * Berechnet die Kosten eines Antworts-Kommunikationsvorgangs.
	 * 
	 * @return double - berechnete Kosten.
	 */
	public abstract double calculateAnswerCosts();
	
	/**
	 * Liefert den Berechnungsfaktor zurück.
	 * @return
	 */
	protected final double getFactor() {
		if(m_content == null) {
			return m_emptyMessageFactor;
		}
		Object testObject = m_content;
		if(testObject instanceof Vector) {
			if(((Vector)testObject).size() > 0) {
				testObject = ((Vector)testObject).get(0);
			}else {
				return m_emptyMessageFactor;
			}
		}
		if(testObject instanceof Instance) {
			return m_examplesFactor;
		}if(testObject instanceof Instances) {
			return m_examplesFactor;
		}if(testObject instanceof Boolean) {
			return m_decisionFactor;
		}if(testObject instanceof Classifier) {
			return m_hypothesisFactor;
		}
		if(testObject instanceof Id) {
			return m_backgroundKnowledgeFactor;
		}
		return m_otherContentFactor;
	}
	
	/**
	 * Liefert die Größe des Inhalts zurück.
	 * 
	 * Entspricht der Inhalt nicht den Einschränkungen der Klasse, dann 
	 * wird der Faktor 100 zurückgeliefert, da so Manipulationen bestraft 
	 * werden.
	 */
	protected final int getContentSize() {
		if(this.getContent() == null) {
			return 1;
		}
		if(this.getContent() instanceof Instance) {
			return 1;
		}
		if(this.getContent() instanceof Instances) {
			return ((Instances)this.getContent()).numInstances();
		}		
		if(this.getContent() instanceof Vector){
			return ((Vector)this.getContent()).size();
		}
		if(this.getContent() instanceof Id){
			return 1;
		}
		if(this.getContent() instanceof Boolean){
			return 1;
		}	
		if(this.getContent() instanceof Classifier) {
			return 1;
		}
		if(this.getContent() instanceof String){
			return ((String)this.getContent()).getBytes().length;
		}							
		return 100;
	}
	
	/**
	 * Gibt zurück, ob es für den Sprechakt, extra Antwortkosten gibt.
	 * 
	 * @return
	 */
	protected boolean haveExtraAnswerCosts() {
		return m_extraCostsForAnswers;
	}
	
	/**
	 * Liefert eine Liste der aller einsatzfähigen Sprechakt-Klassen
	 * 
	 * @return LinkedList - Liste mit allen 
	 */
	public static LinkedList getListOfAllUseableSpeachactClasses() {
		LinkedList retval = new LinkedList();
		retval.add(AnswerDecisionSpeachact.class);
		retval.add(AnswerExamplesSpeachact.class);
		retval.add(AnswerHypothesisSpeachact.class);
		retval.add(AskForDecisionSpeachact.class);
		retval.add(AskForExamplesSpeachact.class);
		retval.add(AskForHypothesisSpeachact.class);
		retval.add(SendExamplesSpeachact.class);
		retval.add(SendHypothesisSpeachact.class);
		retval.add(SendIdSpeachact.class);
		retval.add(CommitRecievingSpeachact.class);
		retval.add(CommitSpeachact.class);
		retval.add(DenyAnswerSpeachact.class);
		retval.add(NotificationSpeachact.class);
		retval.add(RegisterSpeachact.class);
		retval.add(UnRegisterSpeachact.class);
		retval.add(StopCommunicationSpeachact.class);		
		return retval;
	}
}
