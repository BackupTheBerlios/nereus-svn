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
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import visualisation.IVisualisation;

/**
 * Die Klasse stellt eine Oberfläche zur Visualisierung der von eines Agenten
 * während der Simulation erzielten Ergebnisse dar. Sie muss die Schnittstellen
 * IVisualisation und Serializable erfüllen, damit sie vom VisualisationDelegate
 * beim RMI-Callback eingesetzt werden kann.
 * 
 * @author Daniel Friedrich
 */
public class AgentVisualisation 
	extends JFrame 	
	implements IVisualisation, 
				Serializable {
					
	/**
	 * Oberfläche
	 */				
	private JPanel m_contentPane;
	/**
	 * Layoutmanager.
	 */
	private GridLayout m_contentLayout = new GridLayout();
	/**
	 * Graphen-Komponente zur Darstellung der Energierate eines Agenten.
	 */
	private GraphPanel m_energyPane = new GraphPanel();
	/**
	 * Graphen-Komponente zur Darstellung der Lernrate eines Agenten.
	 */	
	private GraphPanel m_learnRatePane = new GraphPanel();
	/**
	 * Multipletab für mehre Textfenster, in die Daten geschrieben werden.
	 */	
	private JTabbedPane m_multipleTextTab = new JTabbedPane();
	/**
	 * Titelkomponente
	 */	
	private TitledBorder m_titledBorder1;
	/**
	 * Scrollpane für das Kommunikationstexttab.
	 */	
	private JScrollPane m_intraAgentCommPane = new JScrollPane();
	/**
	 * Scrollpane für das Lernnachrichtentab.
	 */		
	private JScrollPane m_learningPane = new JScrollPane();
	/**
	 * Scrollpane für das Spielinfotab.
	 */		
	private JScrollPane m_gameInfoPane = new JScrollPane();
	/**
	 * Textfenster für Nachrichten über die Kommunikation zwischen den Agenten.
	 */		
	private JTextArea m_commAgentTextArea = new JTextArea();
	/**
	 * Textfenster für Nachrichten über das Lernen des Agenten.
	 */		
	private JTextArea m_learningTextArea = new JTextArea();
	/**
	 * Textfenster für Nachrichten über den Spielablauf.
	 */		
	private JTextArea m_statisticTextArea = new JTextArea();
	/**
	 * Spielparameter.
	 */
	private Hashtable m_parameter = null; 


	/**
	 * Konstruktor.
	 * 
	 * @param params
	 * @param agentName
	 */	
	public AgentVisualisation(Hashtable params, String agentName) {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
		  	m_parameter = params;
		  	this.setTitle(agentName);
			jbInit();
		}
		catch(Exception e) {
		  	e.printStackTrace();
		}
	}
	
	/**
	 * Erstellt die Oberfläche.
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception  {
		m_contentPane = (JPanel) this.getContentPane();
		m_titledBorder1 = new TitledBorder("");
		m_contentPane.setLayout(m_contentLayout);
		this.setSize(new Dimension(556, 770));
		this.setTitle("Multiagent-Simulator - Agent Visulisation Client");
		m_contentLayout.setColumns(1);
		m_contentLayout.setRows(3);
		m_multipleTextTab.setBorder(null);
		m_commAgentTextArea.setText("Inter-Agenten-Kommunikation:");
		m_commAgentTextArea.setLineWrap(true);
		m_learningTextArea.setText("Lernverfahren");
		m_learningTextArea.setLineWrap(true);
		m_statisticTextArea.setText("Statusmeldungen");
		m_statisticTextArea.setLineWrap(true);
		m_statisticTextArea.setWrapStyleWord(false);
		m_energyPane.setToolTipText("aktuelle Energie des Agenten");
		m_learnRatePane.setToolTipText("Lernrate des Agenten");
		m_contentPane.add(m_energyPane, null);
		m_contentPane.add(m_learnRatePane, null);
		m_contentPane.add(m_multipleTextTab, null);
		m_multipleTextTab.add(m_gameInfoPane,  "Statusmeldungen");
		m_multipleTextTab.add(m_learningPane,  "Lernverfahren");
		m_multipleTextTab.add(m_intraAgentCommPane,  "Agenten-Kommunikation");
		m_intraAgentCommPane.getViewport().add(m_commAgentTextArea, null);
		m_learningPane.getViewport().add(m_learningTextArea, null);
		m_gameInfoPane.getViewport().add(m_statisticTextArea, null);
		/*
		 * Energie-Graph konfigurieren
		 */
		m_energyPane.setXAxisLabel("Schritte");
		m_energyPane.setYAxisLabel("Energie");
		m_energyPane.setMaxNumOfValues(
			((Integer)m_parameter.get("RoundLimit")).intValue());
		m_energyPane.setMaxYValue(
		((Double)m_parameter.get("StartEnergy")).doubleValue() * 2);
		m_energyPane.setStartYValue(
			((Double)m_parameter.get("StartEnergy")).doubleValue());
		/*
		 * Lernrate-graph konfigurieren
		 */
		m_learnRatePane.setXAxisLabel("Schritte");
		m_learnRatePane.setYAxisLabel("Lernrate");
		m_learnRatePane.setMaxNumOfValues(
			((Integer)m_parameter.get("RoundLimit")).intValue());	
		m_learnRatePane.setMaxYValue(100);
		m_learnRatePane.setStartYValue(0);
		m_learnRatePane.setFixedYScale(true);
 	}
 	
	//Overridden so we can exit when window is closed
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			// Visualisierungskomponente schliessen.
			this.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see visualisation.IVisualisation#reset()
	 */
	public void reset() throws RemoteException {
		// Textfenster alle Löschen
		
		System.out.println("RESET wird aufgerufen.");
		
		m_commAgentTextArea.setText("");
		m_learningTextArea.setText("");
		m_statisticTextArea.setText("");
		
		
		/*
		 * Energie-Graph konfigurieren
		 */
		m_energyPane = new GraphPanel();
		m_energyPane.setXAxisLabel("Schritte");
		m_energyPane.setYAxisLabel("Energie");
		m_energyPane.setMaxNumOfValues(
			((Integer)m_parameter.get("RoundLimit")).intValue());
		m_energyPane.setMaxYValue(
		((Double)m_parameter.get("StartEnergy")).doubleValue() * 2);
		m_energyPane.setStartYValue(
			((Double)m_parameter.get("StartEnergy")).doubleValue());
			
			
		/*
		 * Lernrate-graph konfigurieren
		 */
		m_learnRatePane = new GraphPanel(); 
		m_learnRatePane.setXAxisLabel("Schritte");
		m_learnRatePane.setYAxisLabel("Lernrate");
		m_learnRatePane.setMaxNumOfValues(
			((Integer)m_parameter.get("RoundLimit")).intValue());	
		m_learnRatePane.setMaxYValue(100);
		m_learnRatePane.setStartYValue(0);
		this.repaint();

	}

	/* (non-Javadoc)
	 * @see visualisation.IVisualisation#updateVisParameter(int, double)
	 */
	public void updateVisParameter(int type, double value)
		throws RemoteException {
		try {	
			if(type == IVisualisation.LearningRate) {
				m_learnRatePane.addValueDoDisplay(value);
				m_learningTextArea.append(
					"Aktuell bestimmte Lernrate: " 
					+ Double.toString(value) 
					+ " %\n");
			}else if(type == IVisualisation.EnergyValue) {
				m_energyPane.addValueDoDisplay(value);
			}else {
				throw new RemoteException();
			}
			//System.out.println("GraphPanel: " + type + Double.toString(value));	
			this.repaint();
		}catch(Exception e) {
			throw new RemoteException(e.getMessage(),e);
		}
	}

	/* (non-Javadoc)
	 * @see visualisation.IVisualisation#updateVisParameter(int, java.lang.String)
	 */
	public void updateVisParameter(int type, String value)
		throws RemoteException {
		try{		
			if(type == IVisualisation.CommMsg) {
				m_commAgentTextArea.append("\n");
				m_commAgentTextArea.append(value);			
			}else if(type == IVisualisation.StateMsg) {
				m_statisticTextArea.append("\n");
				m_statisticTextArea.append(value);
			}else if(type == IVisualisation.StatisticMsg) {
				m_statisticTextArea.append("\n");
				m_statisticTextArea.append(value);
			}else if(type == IVisualisation.LearningMsg) {
				m_learningTextArea.append("\n");
				m_learningTextArea.append(value);
			}else {
				throw new RemoteException();
			}
			
			this.repaint();
		}catch(Exception e) {
			throw new RemoteException(e.getMessage(),e);
		}
	}
}