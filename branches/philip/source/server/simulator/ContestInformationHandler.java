/*
 * Created on 04.10.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package simulator;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import utils.Id;
import visualisation.IVisualisation;
import exceptions.InvalidAgentException;
import exceptions.InvalidGameException;

/**
 * Informationhandler für den Simulators des FGML-Multiagenten-Contest.
 * 
 * @author Daniel Friedrich
 */
public class ContestInformationHandler implements IInformationHandler {

	/**
	 * Liste der Logs aller registrierten Agenten.
	 */
	private Hashtable m_agentLogs = new Hashtable();
	
	/**
	 * Liste die die Logs der Spiele enthält.
	 */
	private Hashtable m_gameLogs = new Hashtable();	

	/** 
	 * Liste aller registrierten Visualisierungkomponenten der Agenten.
	 * Der Schlüssel ist die AgentId, als Value diente eine Liste(Vector) mit
	 * allen registrierten Visualisierungskomponenten des Agenten.
	 */
	private Hashtable m_visComponents = new Hashtable();
	

	/**
	 * Gibt an, ob während dem Spiel gelogged werden soll oder nicht.
	 */
	private boolean m_shouldLogging = true;

	/**
	 * 
	 */
	public ContestInformationHandler() {
		super();
	}
	
	public ContestInformationHandler(boolean value) {
		super();
		m_shouldLogging = value;
	}	

	/* (non-Javadoc)
	 * @see simulator.IInformationHandler#registerVisualisation(utils.Id, visualisation.IVisualisation)
	 */
	public void registerVisualisation(
		Id agentId,
		Id gameId, 
		IVisualisation component)
		throws InvalidAgentException,
				InvalidGameException {
		
		try {			
			// kontrollieren ob im Hashtable schon enthalten.
			if(m_visComponents.containsKey(agentId.toString())) {
				/* 
				 * Vector mit Visualisierungen für den Agenten holen und 
				 * erweitern.
				 */
				Vector tmpVector = (Vector)m_visComponents.get(agentId.toString()); 
				tmpVector.addElement(component);
				m_visComponents.put(agentId.toString(),tmpVector);	
			}else {
				// neuen Agenten-Eintrag vornehmen			
				Vector tmpVector = new Vector();
				tmpVector.addElement(component);
				m_visComponents.put(agentId.toString(), tmpVector); 
			}			
		}catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see simulator.IInformationHandler#updateVisParameter(java.lang.String, utils.Id, double)
	 */
	public void updateVisParameter(
		int parameter, 
		Id agentId, 
		Id gameId,
		double value)
		throws InvalidAgentException,
				InvalidGameException {			
					
		try {
			// kontrollieren ob im Hashtable schon enthalten.
			if(m_visComponents.containsKey(agentId.toString())) {
				/* 
				 * Vector mit Visualisierungen für den Agenten holen und 
				 * erweitern.
				 */
				Vector tmpVector = (Vector)m_visComponents.get(agentId.toString()); 
				Enumeration components = tmpVector.elements();
				Vector newVector = new Vector();
				while(components.hasMoreElements()) {
					/*
					 * Um Fehler bei der Registrierung abzusichern, wird hier
					 * jedesmal der Vector durchgecheckt und am Ende zurück
					 * gespeichert.
					 */
					Object component = 
						components.nextElement();	
					if(component != null) {
						// eigentliche Arbeit
						((IVisualisation)component).updateVisParameter(
							parameter,
							value);
						//	add Element to new Vector	
						newVector.addElement(component);
					}

				}
				// alten vector löschen
				m_visComponents.remove(agentId.toString());	
				// zurückspeichern
				m_visComponents.put(agentId.toString(), newVector);
				tmpVector = null;				
			}
		}catch(Exception e) {
			throw new InvalidAgentException();
		}			 
	}

	/* (non-Javadoc)
	 * @see simulator.IInformationHandler#updateVisParameter(java.lang.String, utils.Id, java.lang.String)
	 */
	public void updateVisParameter(
		String parameter,
		Id agentId,
		Id gameId,
		String content)
		throws InvalidAgentException,
				InvalidGameException {
	}

	/* (non-Javadoc)
	 * @see simulator.IInformationHandler#log(utils.Id, java.lang.String)
	 */
	public void log(
		Id agentId, 
		Id gameId,
		int type, 
		String logInfo) 
		throws InvalidAgentException,
				InvalidGameException {
			
		if(m_shouldLogging) {		
			/*
			 * Erst mal das Logfile füllen
			 */		
			if(m_agentLogs.containsKey(agentId.toString())) {
				((StringBuffer)m_agentLogs.get(
					agentId.toString())).append(logInfo + "\n");
			}else {
				throw new InvalidAgentException(agentId.toString());
			}
			if(m_gameLogs.containsKey(gameId.toString())) {
				((StringBuffer)m_gameLogs.get(
					gameId.toString())).append( logInfo + "\n");
			}else {
				throw new InvalidGameException(gameId.toString());
			}
		}				
		/*
		 * Die Visualisierungskomponente aktualisieren
		 */
		if(m_visComponents.containsKey(agentId.toString())){
			 Vector vis = (Vector)m_visComponents.get(agentId.toString());
			 Enumeration enum = vis.elements();
			 while(enum.hasMoreElements()) {
				IVisualisation ivis = (IVisualisation)enum.nextElement();
				try {
					ivis.updateVisParameter(
						type,
						logInfo); 
				}catch(Exception e) {}
			 }
		}
	}
	
	/* (non-Javadoc)
	 * @see simulator.IInformationHandler#getLog(utils.Id)
	 */
	public String getLog(Id agentId) throws InvalidAgentException {
		if(m_agentLogs.containsKey(agentId.toString())) {
			return 
				((StringBuffer)m_agentLogs.get(agentId.toString())).toString();
		}else {
			throw new InvalidAgentException(agentId.toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see simulator.IInformationHandler#getCompleteLog()
	 */
	public String getCompleteLog(Id gameId) throws InvalidGameException {
		if(m_gameLogs.containsKey(gameId.toString())) {
			return 
				((StringBuffer)m_gameLogs.get(gameId.toString())).toString();
		}else {
			throw new InvalidGameException(gameId.toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see simulator.IInformationHandler#log(utils.Id, java.lang.String)
	 */
	public void log(
		Id gameId,
		int type, 
		String logInfo) 
		throws InvalidGameException {
		if(m_shouldLogging) {	
			if(m_gameLogs.containsKey(gameId.toString())) {
				(((StringBuffer)m_gameLogs.get(
					gameId.toString()))).append(logInfo + "\n");	
			}else {
				throw new InvalidGameException(gameId.toString());			
			}
		}
	}	

	/**
	 * Registriert einen Agenten zur Kommunikation
	 * 
	 * @param ActionKey key - Key zur Identifikation
	 * @param Id agentId - Id des Agenten der die Info benötigt.
	 * 
	 * @throws InvalidActionKeyException
	 */
	public void registerAgentForCommunication(Id agentId, Id gameId) {
		if(!m_gameLogs.containsKey(gameId.toString())) {
			// GesamtLog anlegen
			m_gameLogs.put(gameId.toString(), new StringBuffer());
		}
		// Log für den Agenten anlegen
		m_agentLogs.put(agentId.toString(), new StringBuffer());
		try {			
			this.log(
				agentId, 
				 gameId,
				 IVisualisation.StateMsg,
				 "Für den Agenten " 
					 + agentId.toString() 
					+ " wird eine neue Visualsierung angemeldet. ");
		}catch(Exception e){} 
	}
	
	public void resetVisualisations(Hashtable agents) 
		throws InvalidAgentException {
		Enumeration agentKeys = agents.keys();
		while(agentKeys.hasMoreElements())  {
			try {
				String agentKey = (String)agentKeys.nextElement();
				if(m_visComponents.containsKey(agentKey)) {
					Vector visualisations = 
						(Vector)m_visComponents.get(agentKey);
					Enumeration visEnum = visualisations.elements();
					while(visEnum.hasMoreElements()) {
						IVisualisation vis = (IVisualisation)visEnum.nextElement();
						vis.reset();	
					}
				}
			}catch(Exception e) {
				System.out.println(
					"Fehler beim Zurücksetzen der Visualisierungskomponente");
				e.printStackTrace(System.out);	
			}
		}
	}

}
