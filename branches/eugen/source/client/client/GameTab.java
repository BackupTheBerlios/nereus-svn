/*
 * Dateiname      : GameTab.java
 * Erzeugt        : 5. August 2003
 * Letzte �nderung: 22. April 2004 durch Eugen Volk
 * Autoren        : Daniel Friedrich
 *                  Eugen Volk
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

package client;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;


import simulator.ICoordinator;
import utils.Id;
import java.util.Hashtable;

/**
 * Ein GameTab ist eine grafische Komponente mit der Spieldaten verwaltet,
 * Spiele neu registriert usw. werden k�nnen. Die Komponente setzt zur 
 * Verwaltung des Spiels das GameParameterPanel ein, dass die dazunotwendige
 * Logik bereits implementiert. Das GameTab bildet dann den scrollbaren Rahmen
 * daf�r und die geschicktere Kapselung nach aussen an.
 * 
 * @author Daniel Friedrich
 */
public class GameTab extends JScrollPane {
	
	/**
	 * Das Panel das die Spielparameter anzeigt.
	 */
  	private GameParameterPanel m_gpp; 
  	 	
  	/**
  	 * Die Schnittstelle zum Server.
  	 */
  	private ICoordinator m_coordinator;
  	
	/**
	 * Das TabbedPane in dem das GameTab erscheint.
	 */
	private JTabbedPane m_parent;
	
	/**
	 * Componente in der Hierarchie �ber einem.
	 */
	private Component m_application;
	
	/**
	 * Spielparameter
	 */
	private Hashtable m_parameter = null;
	
	/**
	 * Flag, das angibt ob ein Spiel bereits gespeichert ist oder nicht.
	 */
	private boolean m_isGameSaved = false;
	
	/**
	 * Name des Szenarios.
	 */
	private String m_scenarioName = null;

	private String pathName = null;
	
  	/**
	 * Konstruktor.
	 * 
	 * @param coordinator
	 * @param parent
	 * @param application
	 * @param scenarioName
	 * @param path
	 * @param hostname
	 */
	public GameTab(
  		ICoordinator coordinator, 
  		JTabbedPane parent,
  		Component application,
  		String scenarioName,
  		String path) {
	    super();
	    pathName = path;
		m_coordinator = coordinator;
		m_parent = parent;
		m_application = application;
		m_scenarioName = scenarioName;
	    this.jbInit();
            this.setVisible(true);
		
  	}
  	
  	/**
	 * Konstruktor.
	 * 
	 * @param coordinator
	 * @param parent
	 * @param application
	 * @param path
	 * @param scenarioName
	 * @param parameter
	 * @param hostname
	 */
	public GameTab(
  		ICoordinator coordinator,
  		JTabbedPane parent,
  		Component application,
		String scenarioName,
  		Hashtable parameter,
  		String hostname,
  		String path) {
  			
		super();
		pathName = path;
		m_coordinator = coordinator;
		m_parent = parent;
		m_application = application;
		m_parameter = parameter;
		m_scenarioName = scenarioName;
		this.jbInit();
		this.setVisible(true);	
               
  	}

  	/**
	 * Erstellt die GUI.
	 */
	public void jbInit() {

	    this.setMaximumSize(new Dimension(550, 5000));
	    if(m_parameter != null) {
			this.setMinimumSize(new Dimension(550, (m_parameter.size() * 20)));
	    }
	    this.setPreferredSize(new Dimension(550, 500));
	    this.setToolTipText("Spielparameter");
	    if(m_parameter != null) {
			m_isGameSaved = true;
	    }
		m_gpp = new GameParameterPanel(
			m_coordinator,
			this,
			m_scenarioName,
			m_parameter,
			pathName);
		this.setAutoscrolls(true);
                // Setzen der Gr��e des GameTab
		m_gpp.setMaximumSize(new Dimension(545, 5000));
		m_gpp.setMinimumSize(new Dimension(545, 600));
		m_gpp.setPreferredSize(new Dimension(545, 600));

        //  this.getViewport().add(m_gpp, BorderLayout.CENTER);        
	this.getViewport().add(m_gpp,null);
       
  	}
  	
  	
  	/**
	 * Entfernt das GameTab aus dem TabbedPane, das es umgibt.
	 */
	public void removeTab() {
  		m_parent.remove(this);
  	}
  	
  	/**
  	 * Schlie�t das Spiel und damit auch das Tab.
  	 * 
	 * @return boolean - True - Spiel korrekt geschlossen, ansonsten False
	 */
	private boolean closeGame() {
  		try {
  			if(m_coordinator.removeGame(m_gpp.getGameId())) {
  				return true;
  			}
  		}catch(Exception e) {
  			System.out.println("Fehler: Das Spiel konnte nicht gel�scht werden.");
  			e.printStackTrace(System.out);
  			MessageDialog md = new MessageDialog("Fehler", 
				"Das Spiel konnte nicht geschlossen werden, "
				+ "bitte beachten sie die Statusmeldung.");
  		}
  		return false;
  	}
  	
  	/**
  	 * �ndert den Namen des Reiters.
  	 * 
	 * @param name - Name des Tabs
	 */
	public void changeTabName(String name) {
  		/*
  		 * Hier kann automatisch immer null gew�hlt werden, da die Funktion nur
  		 * aufgerufen wird, wenn das Tab auch das aktuelle Tab ist.
  		 */
                 
  		// m_parent.setTitleAt(0,name);
            
                // ge�ndert von Eugen 
                int index=m_parent.getSelectedIndex();
  		m_parent.setTitleAt(index,name);
                
  	}
        
	/**
	 * Liefert die Id des Spiels zur�ck, dass das GameTab enth�lt.
	 * 
	 * @return boolean - Id des Spiel
	 */
	public Id getGameId() {
		return m_gpp.getGameId();
	}

	/**
	 * Sorgt daf�r das die Application neu gezeichnet wird.
	 */
	public void repaintApplication() {
		m_application.repaint();
	}
	
	/**
	 * Schreibt eine Statusmeldung in das Statusfeld
	 * 
	 * @param message - anzuzeigende Statusmeldung
	 */
	public void writeStatusMessage(String message) {
		if(m_application instanceof MASIMClient) {
			((MASIMClient)m_application).writeStatusMessage(message);
		}
	}
	
	/**
	 * Gibt zur�ck, ob das Spiel, dass vom Tab verwaltet wird bereits gespeichert ist.
	 * 
	 * @return boolean - True - Spiel gespeichert, ansonsten false.
	 */
	public boolean isGameSaved() {
		return m_isGameSaved;
	}
	
	/**
	 * Setzt das Flag, dass das Spiel bereits gespeichert ist.
	 * 
	 * @param value - gibt an ob das Spiel gespeichert ist.
	 */
	public void setGameSaved(boolean value) {
		m_isGameSaved = value;
	}

}
