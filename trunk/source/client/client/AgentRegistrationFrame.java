/*
 * Created on 05.08.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package client;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import simulator.ICoordinator;
import utils.Id;
import visualisation.VisualisationDelegate;

/**
 * Die Klasse stellt eine kleine Maske zum Registrieren eines Agenten bei einem
 * bestimmten Spiel dar. Ein Agent kann dabei sowohl mit Visualisierung, wie 
 * auch ohne Visualisierung angemeldet werden. Die Logik die dazu notwendig ist, 
 * wird ebenfalls in der Klasse implementiert.  
 * 
 * @author Daniel Friedrich
 */


public class AgentRegistrationFrame extends JFrame {
  	
  	/**
  	 * Inhaltspane
  	 */
  	private JPanel contentPane;
  	/**
  	 * Layoutmanager für die Maske
  	 */
  	private GridLayout m_gridLayout = new GridLayout();
  	/**
  	 * Label für "Agentenname: "
  	 */
  	private JLabel m_agentLabel = new JLabel();
  	/**
  	 * Label für "Agentenklasse: "
  	 */
  	private JLabel m_agentClass = new JLabel();
  	/**
  	 * Label für die URL bei der die Daten abgespeichert sind.
  	 */
  	private JLabel m_serverLabel = new JLabel();
  	/**
  	 * Feld zur Eingabe des Agentennamens.
  	 */
  	private JTextField m_agentTF = new JTextField();
  	/**
  	 * Auswahlcombobox zur Auswahl der Agentenklasse.
  	 */
  	private JComboBox m_agentClassCombo;
  	/**
  	 * Anmelden eines Agenten ohne Visualisierungskomponente.
  	 */
  	private JButton m_roVButton = new JButton();
  	/**
  	 * Button zum Anmelden eines Agenten mit einer Visualisierungskomponente.
  	 */
  	private JButton m_rwVAgentButton = new JButton();
  	/**
  	 * Titelborder
  	 */
  	private TitledBorder titledBorder1;
  	/**
  	 * Feld zur Eingabe der URL unter der die Agentenklassen zu finden sind.
  	 */
  	private JTextField m_serverTF = new JTextField();
  	/**
  	 * Koordinator für den Kontakt zum Simulationsserver
  	 */
  	private ICoordinator m_coordinator;
  	/**
  	 * Id des Spiels zu dem der Agent angemeldet werden soll.
  	 */
  	private Id m_gameId; 
  	/**
  	 * Spielparameter
  	 */
  	private Hashtable m_parameter;
  	/**
  	 * GameTab, dass das Spiel beschreibt, für das der Agent angemeldet werden soll.
  	 */
  	private GameTab m_parent;

	/**
	 * Name des Szenarios.
	 */
	private String m_scenarioName;
	
	/**
	 * ClientInfoObject mit den Konfigurationsdaten
	 */
	private ClientInfoObject m_clientInfoObject = null;

  	/**
	 * Konstruktor.
	 * 
	 * @param gameId - Id des Spiels für das der Agent angemeldet wird.
	 * @param params - Spielparameter
	 * @param coordinator - Koordinator zur Kommunikation mit dem Server
	 * @param agentPath - Pfad in dem sich die Agenten befinden.
	 * @param parent - Parentmaske
	 * @param hostname - Name des Simulationsservers
	 */
  	public AgentRegistrationFrame(
  		Id gameId, 
  		Hashtable params, 
  		ICoordinator coordinator,
  		GameTab parent,
  		String scenarioName) {
  		// ClientInfoObject holen	
  		m_clientInfoObject = ClientInfoObject.getInstance();	
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			m_coordinator = coordinator;
			m_parameter = params;
			m_gameId = gameId;
			m_parent = parent;
			m_scenarioName = scenarioName;
			// Oberfläche erstellen
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
  	}



	/**
	 * Erstellt die grafische Oberfläche.
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception  {
		contentPane = (JPanel) this.getContentPane();
		m_agentLabel.setBorder(BorderFactory.createEtchedBorder());
		m_agentLabel.setMaximumSize(new Dimension(150, 20));
		m_agentLabel.setMinimumSize(new Dimension(150, 20));
		m_agentLabel.setPreferredSize(new Dimension(150, 20));
		m_agentLabel.setToolTipText("Name des Agenten");
		m_agentLabel.setText("Agenten Name:");
		m_gridLayout.setColumns(3);
		m_gridLayout.setHgap(10);
		m_gridLayout.setRows(4);
		m_gridLayout.setVgap(10);
		contentPane.setLayout(m_gridLayout);
		this.setSize(new Dimension(581, 212));
		this.setTitle("Agentenanmeldung");
		contentPane.setMaximumSize(new Dimension(300, 32767));
		contentPane.setMinimumSize(new Dimension(300, 80));
		contentPane.setPreferredSize(new Dimension(300, 80));
		m_agentClass.setBorder(BorderFactory.createEtchedBorder());
		m_agentClass.setText("Klasse des Agenten:");
		m_serverLabel.setBorder(BorderFactory.createEtchedBorder());
		m_serverLabel.setText("Source-Server:");
		m_agentTF.setText("");
		m_roVButton.setBorder(BorderFactory.createRaisedBevelBorder());
		m_roVButton.setActionCommand("Registriere Agent ohne Visualisierung");
		m_roVButton.setText("Registriere Agent ohne Visualisierung");
		m_roVButton.addActionListener(new AgentRegistrationFrame_m_roVButton_actionAdapter(this));
		m_rwVAgentButton.setBorder(BorderFactory.createRaisedBevelBorder());
		m_rwVAgentButton.setText("Registriere Agent mit Visualisierung");
		m_rwVAgentButton.addActionListener(new AgentRegistrationFrame_m_rwVAgentButton_actionAdapter(this));
		m_agentClassCombo = new JComboBox(this.getAgentClasses());
		m_serverTF.setText("http://localhost:2003/");
		contentPane.add(m_agentLabel, null);
		contentPane.add(m_agentTF, null);
		contentPane.add(m_agentClass, null);
		contentPane.add(m_agentClassCombo, null);
		contentPane.add(m_serverLabel, null);
		contentPane.add(m_serverTF, null);
		contentPane.add(m_roVButton, null);
		contentPane.add(m_rwVAgentButton, null);
  	}
  	
  	/* (non-Javadoc)
	 * @see java.awt.Window#processWindowEvent(java.awt.event.WindowEvent)
	 */
  	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			// Fenster schliessen.
		  	this.setVisible(false);
		  	this.dispose();
		}
  	}

	/**
	 * Eventhandling des Registriere Agent ohne Visualisierungs-Button.
	 * 
	 * @param ActionEvent e
	 */
 	void m_roVButton_actionPerformed(ActionEvent e) {
 		try {
 			if(m_coordinator.isGameRegisteredAndOpen(m_gameId)) {
 				m_parent.writeStatusMessage(
					"Registriere den Agenten " 
					+ m_agentLabel.getText() 
					+ " beim Spiel " 
					+ m_coordinator.getGameName(m_gameId ));
				/*
				 * Erstelle kompletten Pfad der Agentenklasse
				 */
				 if(m_parameter.containsKey("ScenarioName")) {
					m_scenarioName = (String)m_parameter.get("ScenarioName");
					System.out.println("Parameter ScenarioName vorhanden: " + m_scenarioName);
				 }else {
					m_scenarioName = "island";
				 }
				 String agentClass = "scenario." 
				 	+ m_scenarioName 
				 	+ "." 
					+ (String)m_agentClassCombo.getSelectedItem(); 
					//+ ".class";
				 System.out.println("Agent name and location: " + agentClass);
				
				m_coordinator.registerAgent(
					m_gameId,
					m_agentTF.getText(),
					agentClass,
					m_serverTF.getText());	
					
				m_parent.writeStatusMessage(
					"Registrierung von Agent " 
					+ m_agentLabel.getText() 
					+ "erfolgreich ausgeführt.");
 			}else {
 				MessageDialog md = new MessageDialog("Agentenregistrierung",
					"Der Agent kann nicht registriert werden, "
					+ "da das Spiel schon geschlossen ist.");
				md.show();	
 			}	
		}catch(Exception ex) {
			MessageDialog md = new MessageDialog("Agentenregistrierung",
				"Der Agent konnte nicht registriert werden, "
				+ "bitte beachten sie die Statusmeldung im Hauptfenster.");
			md.show();
			m_parent.writeStatusMessage(
				"Der Agent konnte nicht registriert werden.");
			System.out.println("Der Agent konnte nicht registriert werden.");
			ex.printStackTrace(System.out);
		}
		this.setVisible(false);
		this.dispose();
  	}

  	/**
  	 * Eventhandling des Registriere Agent mit Visualisierungs-Button.
  	 * 
	 * @param ActionEvent e
	 */
	void m_rwVAgentButton_actionPerformed(ActionEvent e) {
		try {
			/*
			 * Check, ob Spiel überhaupt noch offen ist.
			 */
			if(m_coordinator.isGameRegisteredAndOpen(m_gameId)) {
				m_parent.writeStatusMessage(
					"Registriere den Agenten " 
					+ m_agentLabel.getText() 
					+ " beim Spiel " 
					+ m_coordinator.getGameName(m_gameId ));
				/*
				 * Delegate für Visualisierungskomponente erstellen
				 */	
				VisualisationDelegate delegate = null;
				delegate = new VisualisationDelegate(
					m_parameter,
					AgentVisualisation.class,
					"Agent " + m_agentTF.getText());
					System.out.println(
						"MultiagentSimulatorClient: " 
						+ "	Visualisation wird versucht zu registrieren.");		

				/*
				 * Erstelle kompletten Pfad der Agentenklasse
				 */
				 if(m_parameter.containsKey("ScenarioName")) {
					m_scenarioName = (String)m_parameter.get("ScenarioName");	
				 }else {
					m_scenarioName = "islandParallel";
				 }
				 
				/*
				 * Agent und Visualisierung registrieren
				 */					 
				 String agentClass = "scenario." 
					+ m_scenarioName 
					+ "." 
					+ (String)m_agentClassCombo.getSelectedItem(); 
					//+ ".class";
				m_coordinator.registerAgent(
					m_gameId,
					m_agentTF.getText(),
					agentClass,
					m_serverTF.getText(),
					delegate);	
					
				/*
				 * Visualisierung starten
				 */		  
				delegate.show();					 
			}else {
				/*
				 * MElde das dem User.
				 */
				MessageDialog md = new MessageDialog("Agentenregistrierung",
					"Der Agent kann nicht registriert werden, "
					+ "da das Spiel schon geschlossen ist.");
				md.show(); 
			}
		}catch(Exception ex) {
			MessageDialog md = new MessageDialog("Agentenregistrierung",
				"Der Agent konnte nicht registriert werden, "
				+ "bitte beachten sie die Statusmeldung im Hauptfenster.");
			md.show();
			m_parent.writeStatusMessage(
				"Der Agent konnte nicht registriert werden.");
			System.out.println("Der Agent konnte nicht registriert werden.");
			ex.printStackTrace(System.out);
		}
		m_parent.writeStatusMessage(
			"Registrierung von Agent " 
			+ m_agentLabel.getText() 
			+ "erfolgreich ausgeführt.");
		this.setVisible(false);
		this.dispose();
  	}
  	
  	/**
  	 * Die Methode liefert die Namen der auswählbaren Agentenklassen zurück.
  	 * 
	 * @return Vector 
	 */
	private Vector getAgentClasses() {
  		Vector retval = new Vector();
		try {
			
			String packageName =  
				m_clientInfoObject.getAgentClassesPath().substring(
					m_clientInfoObject.getAgentClassesPath().lastIndexOf(
						m_clientInfoObject.getPathSeparator(),
						m_clientInfoObject.getAgentClassesPath().lastIndexOf(
							m_clientInfoObject.getPathSeparator())-1)+1,
						m_clientInfoObject.getAgentClassesPath().length()-1)
						+ "."
						+ m_scenarioName;
			String completeAgentClassPath =  
				m_clientInfoObject.getAgentClassesPath()
				+ m_scenarioName
				+ m_clientInfoObject.getPathSeparator();			
			System.out.println("PackageName: " + packageName);			
			System.out.println(
				"Fileseperator: " 
				+ m_clientInfoObject.getPathSeparator() 
				+ " muesste sein " + File.pathSeparator);
			System.out.println(
				"Agentpath: " 
				+ completeAgentClassPath);	
				
									
			//File pathName = new File(m_clientInfoObject.getAgentClassesPath());
			File pathName = new File(completeAgentClassPath);	
			File[] files = pathName.listFiles();
			
			for( int i = 0; i < files.length; i++ ) {
				/* 
				 * Das .class Ende abschneiden, bevor die Daten zurück gegeben 
				 * werden.
				 */
				if(!(files[i].isDirectory()) && (files[i].getName().endsWith("Agent.class"))) {
					/* 
					 * Erst mal testen, ob das auch eine Verwendbare 
					 * Agentenklasse ist.
					 */
					boolean runableAgent = true; 
					try {
						Class agentClass = Class.forName(
							packageName 
							+ "." 
							+  files[i].getName().substring(0,files[i].getName().length()-6));
						Method method = 
							agentClass.getDeclaredMethod(
								"isRunableAgent", new Class[]{});
						Object answer = method.invoke(null,new Object[]{});	
						if(answer instanceof Boolean) {
							boolean value = ((Boolean)answer).booleanValue();
							if(value == false) {
								runableAgent = false;
							}
						}	
					}catch (Exception e) {
						e.printStackTrace(System.out);
					}
					if(runableAgent) {
						System.out.println(
							"Agentclass " + files[i].getName() + " gefunden.");
						retval.addElement(
							files[i].getName().substring(0,files[i].getName().length()-6));						
					}
				}				
			}
			if(retval.size() > 0) {
				return retval;
			}
		}catch ( Exception ioE ) {
			System.out.println("Fehler: " + ioE.toString() );
			ioE.printStackTrace(System.out);
		}
  		return retval;
  	}

}

/**
 * @author Daniel Friedrich 
 *
 * Adapter-Klasse zum Eventhandling des m_roVButton - Buttons.
 */
class AgentRegistrationFrame_m_roVButton_actionAdapter implements java.awt.event.ActionListener {
  	AgentRegistrationFrame adaptee;

  	AgentRegistrationFrame_m_roVButton_actionAdapter(AgentRegistrationFrame adaptee) {
		this.adaptee = adaptee;
  	}
  	
  	public void actionPerformed(ActionEvent e) {
		adaptee.m_roVButton_actionPerformed(e);
 	}
}

/**
 * @author Daniel Friedrich
 *
 * Adapter-Klasse zum Eventhandling des rwVAgentButton - Buttons.
 */
class AgentRegistrationFrame_m_rwVAgentButton_actionAdapter implements java.awt.event.ActionListener {
  	AgentRegistrationFrame adaptee;

  	AgentRegistrationFrame_m_rwVAgentButton_actionAdapter(AgentRegistrationFrame adaptee) {
		this.adaptee = adaptee;
  	}
  	
  	public void actionPerformed(ActionEvent e) {
		adaptee.m_rwVAgentButton_actionPerformed(e);
  	}
}

