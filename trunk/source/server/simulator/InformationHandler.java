/*
 * Created on 13.05.2003
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
import exceptions.InvalidElementException;
import exceptions.InvalidGameException;

/**
 * Die Klasse dient als Bindeglied um die Visualisierungs-Informationen vom 
 * Server zum Client weiter geben zu können. Das Interface IInformationHandler
 * wird implementiert, den Zugriff vom Client aus, auf das Modul 
 * MultiagentSimulatorInterfaces zu beschränken. 
 * 
 * @author Daniel Friedrich
 */
public class InformationHandler implements IInformationHandler {
	/** 
	 * Liste aller registrierten Visualisierungkomponenten der Agenten.
	 * Der Schlüssel ist die AgentId, als Value diente eine Liste(Vector) mit
	 * allen registrierten Visualisierungskomponenten des Agenten.
	 */
	private Hashtable m_visComponents = new Hashtable();

	/** 
	 * Liste aller registrierten Visualisierungkomponenten der Agenten-Umgebungen.
	 * Der Schlüssel ist die GameId, als Value diente die registrierte Visualisierungskomponenten der jeweiligen Umgebung.
	 * @since 13 juli 2004
	 */
	private Hashtable m_environments = new Hashtable();
	
	/**
	 * Liste aller registrierten Agenten, sortiert nach den Spielen an denen
	 * sie teilnehmen. Als Schlüssel dient die ID des Spiels und der Value ist 
	 * ein Vector mit Agenten.
	 */
	private Hashtable m_agents = new Hashtable();
	
	/**
	 * Liste der Logs aller registrierten Agenten.
	 */
	private Hashtable m_agentLogs = new Hashtable();
	
	/**
	 * Liste die die Logs der Spiele enthält.
	 */
	private Hashtable m_gameLogs = new Hashtable();

	/**
	 * Liefert die passende Id zum Namen eines registrierten Agenten.
	 *  
	 * @param Id gameId - Id des Spiels bei dem der Agent angemeldet ist.
	 * @param String agentName - Name des Agenten.
	 * @return Id - Id des Agenten mit dem Namen agentName
	 */
	public Id resolveAgentName(Id gameId, String agentName) {
		if(m_agents.containsKey(gameId.toString())) {
			Vector agents = (Vector)m_agents.get(gameId.toString());
			boolean agentFounded = false;
			Enumeration agentenum = agents.elements();
			while(agentenum.hasMoreElements()) {
				Thread tmpAgent = (Thread)agentenum.nextElement();
				if(tmpAgent.getName().equals(agentName)) {
					return ((AbstractAgent)tmpAgent).getId();
				}
			}					
		}
		return null;
	}
	
	/**
	 * Registriert einen Agenten für die spätere Namensauflösung.
	 * 
	 * @param gameId - Id des Spiels
	 * @param agent - Agent der zu regisitrieren ist.
	 */
	public void registerAgentInfo(
		Id gameId, 
		AbstractAgent agent) {
		if(m_agents.containsKey(gameId.toString())) {
			// den Eintrag ergänzen
			Vector agents = (Vector)m_agents.get(gameId.toString());			
			agents.addElement(agent);
			//m_agents.remove(gameId);
			m_agents.put(gameId.toString(),agents);		
		}else {
			// neuen Eintrag anlegen			
			Vector agents = new Vector();
			agents.addElement(agent);
			m_agents.put(gameId.toString(),agents);
		}
		if(!m_gameLogs.containsKey(gameId.toString())) {
			// GesamtLog anlegen
			m_gameLogs.put(gameId.toString(), new StringBuffer());
		}
		// Log für Agenten anlegen
		m_agentLogs.put(agent.getId().toString(), new StringBuffer());		
	}
	
	
	/* (non-Javadoc)
	 * @see simulator.IInformationHandler#registerVisualisation(utils.Id, visualisation.IVisualisation)
	 */
	public void registerVisualisation(
		Id agentId, 
		Id gameId,
		IVisualisation component)
		throws InvalidAgentException,
				InvalidGameException,
				InvalidElementException {
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
			if(e instanceof InvalidGameException) {
				throw new InvalidGameException(gameId.toString());
			}else if(e instanceof InvalidAgentException){
				throw new InvalidAgentException(agentId.toString());
			}else {
				throw new InvalidElementException("",e);
			}
			
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
				System.out.println("Richtiger Fehler: " + e.getMessage());
				e.printStackTrace(System.out);
				if(e instanceof InvalidGameException) {
					throw (InvalidGameException)e;
				}else {
					throw new InvalidAgentException();
				}
			}
	}
	
	

	/* (non-Javadoc)
	 * @see simulator.IInformationHandler#updateVisParameter(java.lang.String, utils.Id, java.lang.String)
	 */
	public void updateVisParameter(int parameter, Id agentId, Id gameId, String content)
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
								content);
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
				if(e instanceof InvalidGameException) {
					throw (InvalidGameException)e;
				}else {
					throw new InvalidAgentException();
				}
			}	
	}
	
    /**
     * @since 13 juli 2004 
     */
    public void updateVisEnvironment(  Id gameId, Object environment) {
    }

    /**
     * Registriert eine Umgebung zur Kommunikation
     * @since 13 juli 2004 
     */
    public void registerEnvironmentForCommunication(Id gameId) {
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
			
		if(m_agentLogs.containsKey(agentId.toString())) {
			((StringBuffer)m_agentLogs.get(
				agentId.toString())).append(logInfo + "\n");
		}else {
			throw new InvalidAgentException(
				"Fehler: Für den Agent " 
				+ agentId.toString() 
				+ " existiert kein Log.");
		}
		if(m_gameLogs.containsKey(gameId.toString())) {
			((StringBuffer)m_gameLogs.get(
				gameId.toString())).append( logInfo + "\n");
		}else {
			throw new InvalidGameException(
				gameId.toString() + "(fehlendes Logfile");
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
			throw new InvalidAgentException(
				"Fehler: Für den Agenten gibt es kein Log.");
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
			throw new InvalidGameException(
				gameId.toString() 
				+ "(fehlendes Log)");
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
		if(m_gameLogs.containsKey(gameId.toString())) {
			(((StringBuffer)m_gameLogs.get(
				gameId.toString()))).append(logInfo + "\n");	
		}else {
			throw new InvalidGameException(
				gameId.toString() 
				+ "(fehlendes Log)");			
		}
	}
	
	/* (non-Javadoc)
	 * @see simulator.IInformationHandler#resetVisualisations(java.util.Hashtable)
	 */
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
