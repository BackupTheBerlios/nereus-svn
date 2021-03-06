/*
 * Created on 05.08.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut f�r Intelligente Systeme, Universit�t Stuttgart (2003)
 */
package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import simulator.ICoordinator;
import utils.DataTransferObject;
import utils.Id;
import utils.ParameterDescription;
import visualisation.VisualisationDelegate;

/**
 * Die Klasse stellt ein Panel zur Konfiguration der Spiel- und 
 * Szenarioparameter bereit. Die Oberfl�che des Panel wird automatisch an die 
 * Anzahl und die Art der Parameter angepa�t. 
 * 
 * @author Daniel Friedrich 
 */
public class GameParameterPanel 
	extends JPanel {
	/**
	 * Oberfl�che
	 */ 
  	private JPanel contentPane;
  	/**
  	 * Rechte Seite f�r die Buttons
  	 */
  	private JPanel m_rightPane = new JPanel();
  	/**
  	 * Platzhalter zwischen den Buttons.
  	 */
  	private JPanel m_buttonPlaceHolder = new JPanel();
  	/**
  	 * Panel f�r die Buttons
  	 */
	private JPanel m_buttonPanel = new JPanel();
	/**
	 * Panel f�r die linke Seite, d.h. die Parameterdarstellung
	 */
	private JPanel m_parameterPanel = new JPanel();
	/**
	 * Layoutmanager f�r alles.
	 */
  	private BorderLayout m_basisLayout = new BorderLayout();
  	/**
  	 * Layoutmanager f�r das m_rightPanel
  	 */
  	private BorderLayout m_rightPaneLayout = new BorderLayout();
 	/**
 	 * Layoutmanager f�r das Button-Panel
 	 */
  	private GridLayout m_buttonGridLayout = new GridLayout(7,1);
  	/**
  	 * Layoutmanager f�r das Panel der Parameternamen
  	 */
  	private GridLayout m_parameterGridLayout;
  	/**
  	 * Zugriff auf den Simulationsserver, wird zum Holen der Parameter ben�tigt
  	 * und zum abspeichern der Daten.
  	 */
  	private ICoordinator m_coordinator;

	/**
	 * Hashtable zum Ablegen der Parameterpanels, die dynamisch erzeugt werden.
	 */
  	private Hashtable m_panes = new Hashtable();
	
	/**
	 * Button zum Schliessen des Spiels.
	 */
  	private JButton m_closeGameButton = new JButton();
  	/**
  	 * Button zum Registrieren einer Visualisierungskomponente
  	 */
  	private JButton m_visRegisterButton = new JButton(); 
  	/**
  	 * Button zum Abrechen der Registratrierung.
  	 */
  	private JButton m_cancelDataButton = new JButton();
  	/**
  	 * Button zum Speichern der Daten
  	 */
 	private JButton m_gameSaveButton = new JButton();
  	/**
  	 * Button zum Start der Simulation des Spiels
  	 */
  	private JButton m_simulateGame = new JButton();
  	/**
  	 * Button zum registrieren eines Agenten.
  	 */
  	private JButton m_registerNewAgent = new JButton();
  	/**
  	 * Button zum Speichern der Statistikergebnisse.
  	 */
	private JButton m_saveResultsButton = new JButton();
	/**
	 * Aktuelle Anzahl der Parameter im Tab.
	 */
  	private int m_aktParameterCount = 0;
  	/**
  	 * Maximale Anzahl der Parameter im Tab.
  	 */
  	private int m_maxParameterCount = 0;
  	/**
   	 * Id des Spiels, dass das Tab repr�sentiert.
   	 */
  	private Id m_gameId;
  	/**
   	 * Name des Spiels, dass das Tab repr�sentiert.
   	 */
  	private String m_gameName;
  	/**
  	 * Parent-Component
  	 */
	private GameTab m_parent;
	
	/**
	 * Enth�lt die Parameterbeschreibung der Parameter
	 */
	private Hashtable m_paramDescription = new Hashtable(); 
	
	/**
	 * Pfad an dem sich die Agenten befinden.
	 */
	//private String m_agentPath;
	
	/**
	 * Parametereinstellungen
	 */
	private Hashtable m_parameter = null; 

	/**
	 * Name des Szenarios.
	 */
	private String m_scenarioName;
	
	private String pathName = null;
	
  	/**
	 * Konstruktor.
	 * 
	 * @param coordinator
	 * @param parent
	 * @param path
	 * @param scenarioName
	 * @param parameter
	 * @param hostname
	 */
	public GameParameterPanel(
		ICoordinator coordinator, 
		GameTab parent,
		//String path,
		String scenarioName,
		Hashtable parameter,
		String path) {
	    super();
	    pathName = path;
	    m_coordinator = coordinator;
	    m_parent = parent;
		m_scenarioName = scenarioName;
	    if(parameter == null) {
	    	m_parameter = new Hashtable();

	    }else {
	    	m_parameter = parameter;
	    }
	    try {
	    	if(parent.isGameSaved()) {
	    		m_gameId = m_coordinator.getGameId((String)m_parameter.get("GameName"));
	    	}
			this.jbInit(); 
			if((m_gameId != null) && (parent.isGameSaved())) {
				m_cancelDataButton.setEnabled(false);
				m_gameSaveButton.setEnabled(false);
				m_saveResultsButton.setEnabled(true);
				m_visRegisterButton.setEnabled(true);
				if(m_parameter.containsKey("MultipleGameTabsAllowed") && 
					((Boolean)m_parameter.get("MultipleGameTabsAllowed")).booleanValue()) {
					m_simulateGame.setEnabled(true);
					m_closeGameButton.setEnabled(true);
					if(m_coordinator.isGameRegisteredAndOpen(m_gameId)) {
						m_registerNewAgent.setEnabled(true);
					}else {
						m_registerNewAgent.setEnabled(false);	
					}
				}else {
					m_simulateGame.setEnabled(false);
					m_closeGameButton.setEnabled(false);
					m_registerNewAgent.setEnabled(false);
				}
			}
	    }catch(Exception e) {
	    	System.out.println("Fehler: Der Aufbau des GameParameterPanel ging schief.");
	    	e.printStackTrace(System.out);
	    }
	    
  	}

	/**
	 * Erstellt die Oberfl�che.
	 * 
	 * @throws Exception
	 */
  	private void jbInit() throws Exception  {
	    m_buttonPanel.setLayout(m_buttonGridLayout);
	    m_buttonPanel.setBorder(BorderFactory.createEtchedBorder());
	    m_buttonPanel.setDebugGraphicsOptions(0);
	    m_buttonPanel.setMaximumSize(new Dimension(150, 250));
	    m_buttonPanel.setMinimumSize(new Dimension(150, 250));
	    m_buttonPanel.setPreferredSize(new Dimension(150, 250));
	    m_buttonGridLayout.setHgap(0);
	    m_buttonGridLayout.setVgap(5);
	    /*
	     * Breche Speicherung ab.
	     */
	    m_cancelDataButton.setText("Abbrechen");
	    m_cancelDataButton.addActionListener(new GameParameterPanel_m_cancelDataButton_actionAdapter(this));
	    /*
	     * Speichere Spiel Daten
	     */
	    m_gameSaveButton.setText("Speichere Daten");
	    m_gameSaveButton.addActionListener(new GameParameterPanel_m_gameSaveButton_actionAdapter(this));
	    /*
	     * Simuliere Spiel - Button
	     */
	    m_simulateGame.setEnabled(false);
	    m_simulateGame.setText("Simuliere Spiel");
	    m_simulateGame.addActionListener(new GameParameterPanel_m_simulateGame_actionAdapter(this));		
		/*
		 * Registriere einen neuen Agenten
		 */
		m_registerNewAgent.setEnabled(false);
		m_registerNewAgent.setText("Registriere Agent"); 
		m_registerNewAgent.addActionListener(new GameParameterPanel_m_registerNewAgent_actionAdapter(this)); 
		/*
		 * Schliesse das Spiel
		 */
		m_closeGameButton.setEnabled(false);
		m_closeGameButton.setText("Schliesse Spiel"); 
		m_closeGameButton.addActionListener(new GameParameterPanel_m_closeGameButton_actionAdapter(this));
		/*
		 * Registriere eine neue Visualisierung
		 */ 
		m_visRegisterButton.setEnabled(false);
		m_visRegisterButton.setText("Neue Visualisierung"); 
		m_visRegisterButton.addActionListener(new GameParameterPanel_m_visRegisterButton_actionAdapter(this));
		/*
		 * Speichere die Resultate der Ergebnisse
		 */
		m_saveResultsButton.setEnabled(false);
		m_saveResultsButton.setText("Speichere Resultate"); 
		m_saveResultsButton.addActionListener(new GameParameterPanel_m_saveResultsButton_actionAdapter(this)); 
	    /*
	     * Buttons auf Panel setzen
	     */
		m_buttonPanel.add(m_registerNewAgent, null);
		m_buttonPanel.add(m_visRegisterButton,null);
		m_buttonPanel.add(m_cancelDataButton, null);
		m_buttonPanel.add(m_closeGameButton,null);
		m_buttonPanel.add(m_gameSaveButton, null);
		m_buttonPanel.add(m_simulateGame, null);
		m_buttonPanel.add(m_saveResultsButton,null);
		
		/*
		 * Rechtes Pane erstellen
		 */
		m_rightPane.setLayout(m_rightPaneLayout);
		m_rightPane.add(m_buttonPlaceHolder, BorderLayout.NORTH);
		m_rightPane.add(m_buttonPanel, BorderLayout.SOUTH);
		this.setLayout(m_basisLayout);
	    
	    this.add(m_rightPane, BorderLayout.EAST);
	    
	    //this.add(m_buttonPanel, BorderLayout.EAST);
	    this.add(m_parameterPanel, BorderLayout.CENTER);
	    this.buildParameterPane();
	}

  	private void buildParameterPane() {
        System.out.println("GameParameterPanel: Versuche Parameter zu erhalten");
		try {
			LinkedList description = m_coordinator.getGameParameter(); 
            System.out.println("GameParameterPanel: Was braucht das Szenario?"
                    + m_scenarioName);
	    	LinkedList scenarioDescription = m_coordinator.getScenarioParameter(m_scenarioName);
	    	ListIterator iter = scenarioDescription.listIterator();
	    	while(iter.hasNext()) {
	    		description.addLast(iter.next());
	    	} 
	      	/* Frage die Anzahl der Parameter ab, erzeuge ein Panel das
	      	 * gro� genug ist alle Panels f�r jeden Parameter darzustellen
	       	 * und weise dem Panel ein GridLayout zu.
	       	 */
		    m_maxParameterCount = description.size();
		    
		    /*
		     * Aussenmasse des Parameter-Panels bestimmen
		     */
			int height = (20 * m_maxParameterCount);
			m_parameterGridLayout = new GridLayout(m_maxParameterCount,1); 
			m_parameterPanel.setLayout(m_parameterGridLayout);
			m_parameterPanel.setMaximumSize(new Dimension(400, height));
			m_parameterPanel.setMinimumSize(new Dimension(400, height));
			m_parameterPanel.setPreferredSize(new Dimension(400, height));
			m_parameterPanel.setBorder(BorderFactory.createEtchedBorder());

			/*
			 * Aussenmasse des buttonPlaceHolderPanes bestimmen
			 */
			m_buttonPlaceHolder.setMaximumSize(
				new Dimension(150,(height-200)));
			m_buttonPlaceHolder.setMinimumSize(
				new Dimension(150,(height-200)));
			m_buttonPlaceHolder.setPreferredSize(
				new Dimension(150,(height-200)));
	
	        ListIterator enum = description.listIterator();
	        while(enum.hasNext()) {
		        ParameterDescription pd = (ParameterDescription)enum.next();
		        String pName = pd.getParameterName();
		        int classDescription = pd.getClassDescription();
		        /*
		         * Infos f�r sp�ter zwischenspeichern
		         */
				m_paramDescription.put(
					pName,
					new Integer(classDescription)); 
		        // create Panel for Element
		        JParameterPanel panel;
		        Object value = null;
		        switch(classDescription) {
		        	case 0 : {
						if(m_parameter.containsKey(pName)) {
							value = ((Boolean)m_parameter.get(pName));
						}else {
							if(pd.getDefaultValue() != null) {
								value = pd.getDefaultValue();	
							}							 
						}
						panel = new JParameterPanel(
							pName, 
							"Checkbox", 
							value,
							180,
							20,
							220,
							20); 
						if(m_parameter.containsKey(pName)) {
							panel.setEnabled(false);
							panel.setEditable(false);
						}
		        		break; }
					default: { 
						if(pName.equals("ScenarioName")) {
							if(m_parameter.containsKey(pName)) {
								value = new Vector();
								((Vector)value).addElement(m_parameter.get(pName));
							}else {
								value = m_coordinator.getScenarioNames();	
							}
							panel = new JParameterPanel(
								pName, 
								"ComboBox", 
								value,
								180,
								20,
								220,
								20);
							if(m_parameter.containsKey(pName)) {
								panel.setEnabled(false);
								panel.setEditable(false);
							}	
						}else {
							if(m_parameter.containsKey(pName)) {
								value = m_parameter.get(pName).toString();
							}else {
								if(pd.getDefaultValue() != null) {
									value = pd.getDefaultValue().toString();	
								}							 
							}
							panel = new JParameterPanel(
								pName, 
								"TextField", 
								value,
								180,
								20,
								220,
								20);
							if(m_parameter.containsKey(pName)) {
								panel.setEnabled(false);
								panel.setEditable(false);
							}	
						}
						if(!pd.isChangeable()) {
							panel.setEditable(false);
							panel.setEnabled(false);
						}						
						break; 
					}

		        }
		        panel.setBorder(BorderFactory.createEtchedBorder());
		        panel.setMaximumSize(new Dimension(400, 20));
		        panel.setMinimumSize(new Dimension(400, 20));
		        panel.setPreferredSize(new Dimension(400,20));
		        // Speichere Panel zur weiteren Verwendung
		        m_panes.put(pName,panel);
		        // F�ge das Panel der Grundfl�che hinzu.
				m_parameterPanel.add(panel);
	      	}
		}catch(RemoteException re) {
	    	System.out.println(
				"Fehler: Die Parameterbeschreibung wurden nicht "
				+ "korrekt gelesen.");
	        re.printStackTrace(System.out);
	    }
  	}

	/**
	 * Handelt das Event zum Starten eines Spiels.
	 * 
	 * @param e
	 */
	void m_simulateGame_actionPerformed(ActionEvent e) {
	    try {
	    	// starte Simulation
	      	m_coordinator.startGame(m_gameId);
	      	
			//m_parent.writeStatusMessage(
	    }catch(Exception re) {
	      	System.out.println("Fehler: Das Spiel kann nicht gestartet werden.");
	      	re.printStackTrace(System.out);
	    }
  	}
   
  	/**
  	 * �ffnet einen Frame zur Registrierung eines neuen Agenten.
  	 * 
	 * @param ActionEvent e
	 */
	void m_registerNewAgent_actionPerformed(ActionEvent e) {
		try {
			if(m_coordinator.isGameRegisteredAndOpen(m_gameId)) {
				AgentRegistrationFrame arf = new AgentRegistrationFrame(
					m_gameId,
					m_parameter,
					m_coordinator,
					m_parent,
					m_scenarioName,
					pathName);  
				arf.show();
			}else {
				m_parent.writeStatusMessage(
					"Bei diesem Spiel kann kein Agent mehr registriert werden, "
					+ "da es bereits geschlossen ist.");		
			}
			if(!m_coordinator.isGameRegisteredAndOpen(m_gameId)) {
				m_registerNewAgent.setEnabled(false);
			}
		}catch(Exception ex) {
			m_parent.writeStatusMessage(ex.getMessage());
		}
		m_parent.repaintApplication();
  	}

  	/**
  	 * Speichert die Parameterwerte als Spiel beim Server.
  	 * 
 	 * @param Event e
 	 */	
	void m_gameSaveButton_actionPerformed(ActionEvent e) {
		m_parent.writeStatusMessage(
			"Beginne mit dem Speichern des Spiels " + m_gameName);
	    /* Speichere die Daten, erzeuge dazu eine Hashmap mit allen Parameterwerten
	     * drin.
	     */
	    Enumeration enum = m_panes.elements();
	    DataTransferObject dto = new DataTransferObject();
	    while(enum.hasMoreElements()) {
	      	JParameterPanel jpp = (JParameterPanel)enum.nextElement();
	      	/*
	      	 * Typkonvertierung
	      	 */
	      	int type = ((Integer)m_paramDescription.get(jpp.getParameterName())).intValue();
	      	switch(type) {
	      		case ParameterDescription.BooleanType: {
					dto.put(jpp.getParameterName(),
						(Boolean)jpp.getParameterValue());
					m_parameter.put(jpp.getParameterName(),
						(Boolean)jpp.getParameterValue());	
					jpp.setEditable(false);
	      			break;}
				case ParameterDescription.DoubleType: {
					dto.put(jpp.getParameterName(),
						Double.valueOf((String)jpp.getParameterValue()));
					m_parameter.put(jpp.getParameterName(),	
						Double.valueOf((String)jpp.getParameterValue()));
					jpp.setEditable(false);
					break;}
				case ParameterDescription.FloatType: {						
					dto.put(jpp.getParameterName(),
						Float.valueOf((String)jpp.getParameterValue()));
					m_parameter.put(jpp.getParameterName(),	
						Float.valueOf((String)jpp.getParameterValue()));
					jpp.setEditable(false);
					break;}
				case ParameterDescription.IntegerType: {
					dto.put(jpp.getParameterName(),
						Integer.valueOf((String)jpp.getParameterValue()));
					m_parameter.put(jpp.getParameterName(),	
						Integer.valueOf((String)jpp.getParameterValue()));
					jpp.setEditable(false);
					break;}
				case ParameterDescription.StringType: {
					dto.put(jpp.getParameterName(),
						jpp.getParameterValue());
					m_parameter.put(jpp.getParameterName(),
						jpp.getParameterValue());
					jpp.setEditable(false);	
					break;}
	      	}
	      	jpp.setEnabled(false);
	    }
	    try {
	    	/*
	    	 * Registriere ein Spiel beim Coordinator und speichere die Id, die vom 
	    	 * Coordinator zur�ckgegeben wird.
	    	 */
			m_gameId = m_coordinator.registerGame(dto);
			m_gameName = m_coordinator.getGameName(m_gameId);
			m_parent.writeStatusMessage(
				"Speichern des Spiels erfolgreich beendet.");
	    }catch(Exception ex) {
			m_parent.writeStatusMessage(
				"Das Speichern des Spiels konnte nicht durchgef�hrt werden.");
			System.out.println("Fehler: Das Speichern des Spiels hat nicht geklappt.");
			ex.printStackTrace(System.out);
	    }
		m_parent.setGameSaved(true);
	    // Schreibe den Namen des Spiels in den Reiter des Tabs
	    m_parent.changeTabName(m_gameName);
	    // Schalte Buttons an und aus
	    m_gameSaveButton.setEnabled(false);
	    m_cancelDataButton.setEnabled(false);
	    m_simulateGame.setEnabled(true);
	    m_registerNewAgent.setEnabled(true);
	    //m_parent.repaint();
		m_saveResultsButton.setEnabled(true);
	    m_parent.repaintApplication();
	    m_closeGameButton.setEnabled(true);
		m_visRegisterButton.setEnabled(true);

  	}
  	
  	/**
  	 * Liefert den Namen des Spiels zur�ck.
  	 * 
	 * @return String
	 */
	public Id getGameId() {
  		return m_gameId;
  	}

  	void m_cancelDataButton_actionPerformed(ActionEvent e) {
  		m_parent.removeTab();
		m_parent.repaintApplication();
  	}
  	
	void m_closeGameButton_actionPerformed(ActionEvent event) {
		boolean deleted = false;
		try {
			deleted = m_coordinator.removeGame(m_gameId);
			if(deleted) {
				m_parent.writeStatusMessage(
					"Das Spiel " 
					+ m_gameName 
					+ " wurde geschlossen");
			}else {
				m_parent.writeStatusMessage(
					"Das Spiel " 
					+ m_gameName 
					+ " konnte nicht geschlossen werden");
			}
		}catch(Exception e) {
			m_parent.writeStatusMessage("Fehler: Das Schliessen des Spiel klappte nicht.");
			m_parent.writeStatusMessage("Grund: " + e.getMessage());
		}
		if(deleted) {
			m_parent.removeTab();
			m_parent.repaintApplication();
		}
	}
	
	/**
	 * Registriere ein neues Spiel.
	 * 
	 * @param event
	 */
	void m_visRegisterButton_actionPerformed(ActionEvent event) {
		try {
			Vector agents = m_coordinator.getAgentsForGame(m_gameId);
			if(agents.size() == 0) {
				MessageDialog md = new MessageDialog(
					"Nachricht",
					"Das Spiel enth�lt noch keine Agenten und deshalb kann "
					+ "keine Visualisierung angemeldet werden.");
				md.show();	
			}else {
				/*
				 * Auswahl des Agenten den die Visualisierung betrachten soll.
				 */
				SelectionDialog sd = new SelectionDialog(
					"Agentenauswahl",
					"Bitte w�hlen Sie den Agenten f�r die Visualisierung aus.",
					"Agent: ",
					agents);
				sd.show();
				// Holen den ausgew�hlten Agenten.
				String selectedAgent = (String)sd.getSelection();
				if(selectedAgent != null) {
					/*
					 * Visualisierung erzeugen.
					 */
					VisualisationDelegate delegate = null;
				   
					delegate = new VisualisationDelegate(
						m_parameter,
					   	AgentVisualisation.class,
					   	"Agent: " + selectedAgent);	
				   	m_coordinator.registerVisualisation(
					   	m_gameId,
					   	selectedAgent,
					   	delegate);	
					delegate.show();   		 
				}
			}			
		}catch(Exception e) {
			MessageDialog md = new MessageDialog(
				"Fehler",
				"Beim Anmelden der Visualisierung trat ein Fehler auf, bitte "
				+ "beachten Sie deshalb das Fenster f�r die Statusmeldungen.");
			md.show();
			m_parent.writeStatusMessage
				("Fehler: Bei der Anmeldung der Visualisierung trat der folgende"
				+ " Fehler: " + e.getMessage() + " auf.");	
		}
		m_parent.repaintApplication();
	}
	
	/**
	 * Speichere die Daten.
	 * 
	 * @param event
	 */
	void m_saveResultsButton_actionPerformed(ActionEvent event) {
		/*
		 * W�hle das File aus.
		 */
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooser.setDialogTitle("Speichere Ergebnisse");
		fileChooser.showSaveDialog(this);
		//fileChooser.show();
		/*
		 * Hole das Resultat
		 */
		try {
			String result = m_coordinator.getSimulationResult(m_gameId); 
			File selectedFile = fileChooser.getSelectedFile();
			FileWriter logFileWriter = new FileWriter(selectedFile);
			logFileWriter.write(result);
			logFileWriter.close();
		}catch(Exception e) {
			MessageDialog md = new MessageDialog(
				"Fehler",
				"Das holen und speichern der Ergebnisse hat nicht geklappt.");
		}
		m_parent.repaintApplication();
	}  	
	
	/* (non-Javadoc)
	 * @see java.awt.Component#repaint()
	 */
	public void repaint() {
		try {
			if(!m_coordinator.isGameRegisteredAndOpen(m_gameId)) {
				m_registerNewAgent.setEnabled(false);
			}
		}catch(Exception e) {}
		super.repaint();
	}

}

class GameParameterPanel_m_registerNewAgent_actionAdapter implements java.awt.event.ActionListener {
	GameParameterPanel adaptee;

	GameParameterPanel_m_registerNewAgent_actionAdapter(GameParameterPanel adaptee) {
		this.adaptee = adaptee;
  	}
  	
 	public void actionPerformed(ActionEvent e) {
		adaptee.m_registerNewAgent_actionPerformed(e);
  	}
  	
}


class GameParameterPanel_m_simulateGame_actionAdapter implements java.awt.event.ActionListener {
	GameParameterPanel adaptee;

	GameParameterPanel_m_simulateGame_actionAdapter(GameParameterPanel adaptee) {
    	this.adaptee = adaptee;
 	}
  
  	public void actionPerformed(ActionEvent e) {
    	adaptee.m_simulateGame_actionPerformed(e);
  	}
}

class GameParameterPanel_m_gameSaveButton_actionAdapter implements java.awt.event.ActionListener {
	GameParameterPanel adaptee;

	GameParameterPanel_m_gameSaveButton_actionAdapter(GameParameterPanel adaptee) {
    	this.adaptee = adaptee;
  	}
  
  	public void actionPerformed(ActionEvent e) {
    	adaptee.m_gameSaveButton_actionPerformed(e);
  	}
}

class GameParameterPanel_m_cancelDataButton_actionAdapter implements java.awt.event.ActionListener {
	GameParameterPanel adaptee;

	GameParameterPanel_m_cancelDataButton_actionAdapter(GameParameterPanel adaptee) {
    	this.adaptee = adaptee;
  	}
  
  	public void actionPerformed(ActionEvent e) {
    	adaptee.m_cancelDataButton_actionPerformed(e);
  	}
}

class GameParameterPanel_m_closeGameButton_actionAdapter implements java.awt.event.ActionListener {
	GameParameterPanel adaptee;

	GameParameterPanel_m_closeGameButton_actionAdapter(GameParameterPanel adaptee) {
		this.adaptee = adaptee;
	}
  
	public void actionPerformed(ActionEvent e) {
		adaptee.m_closeGameButton_actionPerformed(e);
	}
}

class GameParameterPanel_m_visRegisterButton_actionAdapter implements java.awt.event.ActionListener {
	GameParameterPanel adaptee;

	GameParameterPanel_m_visRegisterButton_actionAdapter(GameParameterPanel adaptee) {
		this.adaptee = adaptee;
	}
  
	public void actionPerformed(ActionEvent e) {
		adaptee.m_visRegisterButton_actionPerformed(e);
	}
}

class GameParameterPanel_m_saveResultsButton_actionAdapter implements java.awt.event.ActionListener {
	GameParameterPanel adaptee;

	GameParameterPanel_m_saveResultsButton_actionAdapter(GameParameterPanel adaptee) {
		this.adaptee = adaptee;
	}
  
	public void actionPerformed(ActionEvent e) {
		adaptee.m_saveResultsButton_actionPerformed(e);
	}
}
