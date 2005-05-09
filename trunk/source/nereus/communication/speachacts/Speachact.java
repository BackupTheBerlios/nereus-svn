/*
 * Dateiname      : Speachact.java
 * Erzeugt        : 19. Juni 2003
 * Letzte �nderung: 
 * Autoren        : Daniel Friedrich
 *                  
 *                  
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut f�r Intelligente Systeme
 * der Universit�t Stuttgart unter Betreuung von Dietmar Lippold
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
 * Abstrakte Basisklasse f�r die unterschiedlichen Sprechakte.
 * 
 * @author Daniel Friedrich
 */
abstract public class Speachact {
	/**
	 * Konstante zur Abfrage der CommKosten
	 */
	public static final String SINGLECOMMCOSTS = "EnergyForCommunication";
	/**
	 * Konstante f�r Abfrage der Antwortkosten
	 */
	public static final String ANSWERCOSTS = "EnergyForAnswers";
	/**
	 * Konstante zur Abfrage der Kosten f�r Facilitation-Messages
	 */
	public static final String FACILITATIONCOSTS = "EnergyForFacilitationMsgs";

	/**
	 * Berechnungsfaktor f�r Beispiele, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten f�r eine Nachricht.
	 */
	private double m_examplesFactor = 1.0;		
	
	/**
	 * Berechnungsfaktor f�r Hypothesen, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten f�r eine Nachricht.
	 */
	private final double m_hypothesisFactor = 0.5;
	
	/**
	 * Berechnungsfaktor f�r Hintergrundwissen, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten f�r eine Nachricht.
	 */
	private double m_backgroundKnowledgeFactor = 0.25;	
	
	/**
	 * Berechnungsfaktor f�r Entscheidungen, wird mit den Basiskosten 
	 * multipliziert zum Berechnen der Kosten f�r eine Nachricht.
	 */
	private double m_decisionFactor = 0.25;
	
	/**
	 * Berechnungsfaktor f�r leere Nachrichten.
	 */
	private double m_emptyMessageFactor = 0.25;	
	
	/**
	 * Berechnungsfaktor f�r leere Nachrichten.
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
	 * Parametername zum Durchreichen des Faktors f�r leere Nachrichten. 
	 */
	public final String EMPTYMESSAGEFACTOR = "EmptyMessageFactor";
	
	/**
	 * Parametername zum Durchreichen des Faktors f�r andere Inhalte. 
	 */
	public final String OTHERCONTENTFACTOR = "OtherContentFactor";				

	/**
	 * Id des Senders
	 */
    private Id m_sender;
    
    /**
     * Ids des Empf�nger des Sprechakts
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
	 * Basiskosten f�r eine Nachricht.
	 */
	private double m_commCosts = 0.0;
	
	/**
	 * Basiskosten f�r eine Antwort.
	 */
	private double m_answerCosts = 0.0;
		
	/**
	 * Basiskosten f�r Facilitationnachrichten.
	 */
	private double m_facilitationCosts = 0.0;
	
	/**
	 * Werden f�r eine Antwort auch Kosten berechnet?, der Std. ist nein.
	 */
	private boolean m_extraCostsForAnswers = false;
	
	/**
	 * Werden f�r Facilitationnachrichten extra Kosten berechnet?, 
	 * der Std. ist nein.
	 */
	private boolean m_extraCostsForFacilitation = false;
	
	
	/**
	 * Konstruktor
	 * 
	 * @param Id sender - Id des Senders
	 * @param Id receiver - Id des Empf�ngers
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
     * Liefert die Kosten f�r einen Facilitationnachricht zur�ck.
	 * @return double
	 */
	protected final double getFacilitationCosts() {
		if(m_extraCostsForFacilitation) {
			return m_facilitationCosts;
		}
    	return m_commCosts;
    }
    
	/**
	 * Liefert die Basiskosten f�r eine Nachricht zur�ck.
	 * @return double
	 */    
    protected final double getCommCosts() {
    	return m_commCosts;
    }
    
	/**
	 * Liefert die Basiskosten f�r eine Anwortnachricht zur�ck.
	 * @return double
	 */      
	protected final double getAnswerCosts() {
		return m_answerCosts;
	}
	
    /**
     * Gibt an ob die Nachricht eine R�ckantwort verlangt.
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
     * Gibt den Inhalt der Nachricht zur�ck.
     * 
	 * @return Object
	 */
	public Object getContent() {
		return m_content;
	}
	
	/**
	 * Gibt den Sender der Nachricht zur�ck.
	 * 
	 * return Id;
	 */
    public Id getSender() {
    	return m_sender;
    }
    
    /**
	 * Gibt den Empf�nger der Nachricht zur�ck.
	 * 
	 * return Id;
	 */
	public Id getReceiver() {
    	return m_receiver;
    }
    
    /**
     * Setzt den Inhalt der Nachricht.
	 * 
	 * F�r jeden Sprechakt gibt es spezielle Einschr�nkungen.
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
	 * Setzt die Id des Empf�ngers der Nachricht.
	 * 
	 * @param Id receiver - Id des Empf�ngers
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
	 * Liefert den Berechnungsfaktor zur�ck.
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
	 * Liefert die Gr��e des Inhalts zur�ck.
	 * 
	 * Entspricht der Inhalt nicht den Einschr�nkungen der Klasse, dann 
	 * wird der Faktor 100 zur�ckgeliefert, da so Manipulationen bestraft 
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
	 * Gibt zur�ck, ob es f�r den Sprechakt, extra Antwortkosten gibt.
	 * 
	 * @return
	 */
	protected boolean haveExtraAnswerCosts() {
		return m_extraCostsForAnswers;
	}
	
	/**
	 * Liefert eine Liste der aller einsatzf�higen Sprechakt-Klassen
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
