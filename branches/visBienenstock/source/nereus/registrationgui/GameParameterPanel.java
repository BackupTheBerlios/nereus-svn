/*
 * Dateiname      : GameParameterPanel.java
 * Erzeugt        : 5. August 2003
 * Letzte Änderung: 12. Juni 2005 durch Eugen Volk
 * Autoren        : Daniel Friedrich
 *                  Eugen Volk
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




package nereus.registrationgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;

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
import java.awt.Frame;
import java.awt.Component;

import nereus.registrationgui.JSelectionDialog;
import nereus.simulatorinterfaces.ICoordinator;
import nereus.utils.DataTransferObject;
import nereus.utils.Id;
import nereus.utils.GameConf;
import nereus.utils.ParameterDescription;
import nereus.visualisation.VisualisationDelegate;
import nereus.visualisationclient.VisualisationClient;
import nereus.registrationgui.ClientInfoObject;
import nereus.visualisationclient.VisualisationClient;
import nereus.simulatorinterfaces.IVisualisationOutput;


/**
 * Die Klasse stellt ein Panel zur Konfiguration der Spiel- und
 * Szenarioparameter bereit. Die Oberfläche des Panel wird automatisch an die
 * Anzahl und die Art der Parameter angepaßt.
 *
 * @author Daniel Friedrich
 *
 */
public class GameParameterPanel
        extends JPanel {
    /**
     * Oberfläche
     */
    private JPanel contentPane;
    /**
     * Rechte Seite für die Buttons
     */
    private JPanel m_rightPane = new JPanel();
    /**
     * Platzhalter zwischen den Buttons.
     */
    private JPanel m_buttonPlaceHolder = new JPanel();
    /**
     * Panel für die Buttons
     */
    private JPanel m_buttonPanel = new JPanel();
    /**
     * Panel für die linke Seite, d.h. die Parameterdarstellung
     */
    private JPanel m_parameterPanel = new JPanel();
    /**
     * Layoutmanager für alles.
     */
    private BorderLayout m_basisLayout = new BorderLayout();
    /**
     * Layoutmanager für das m_rightPanel
     */
    private BorderLayout m_rightPaneLayout = new BorderLayout();
    /**
     * Layoutmanager für das Button-Panel
     */
    private GridLayout m_buttonGridLayout = new GridLayout(8,1);
    /**
     * Layoutmanager für das Panel der Parameternamen
     */
    private GridLayout m_parameterGridLayout;
    /**
     * Zugriff auf den Simulationsserver, wird zum Holen der Parameter benötigt
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
     * Button zum Visualisierung des Szenario
     */
    private JButton m_scenarioVisButton = new JButton();
    
    /**
     * Aktuelle Anzahl der Parameter im Tab.
     */
    private int m_aktParameterCount = 0;
    /**
     * Maximale Anzahl der Parameter im Tab.
     */
    private int m_maxParameterCount = 0;
    /**
     * Id des Spiels, dass das Tab repräsentiert.
     */
    private Id m_gameId;
    /**
     * Name des Spiels, dass das Tab repräsentiert.
     */
    private String m_gameName;
    /**
     * Parent-Component
     */
    private GameTab m_parent;
    
    /**
     * Enthält die Parameterbeschreibung der Parameter
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
     * KonfigurationStruktur mit Karten- und ScenarioKonfigDatei-Namen
     */
    private GameConf gameConf=null;
    
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
            String path,
            GameConf gameConf) {
        super();
        pathName = path;
        m_coordinator = coordinator;
        m_parent = parent;
        m_scenarioName = scenarioName;
        this.gameConf=gameConf;
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
     * Erstellt die Oberfläche.
     *
     * @throws Exception
     */
    private void jbInit() throws Exception  {
        m_buttonPanel.setLayout(m_buttonGridLayout);
        m_buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        m_buttonPanel.setDebugGraphicsOptions(0);
        m_buttonPanel.setMaximumSize(new Dimension(150, 300));
        m_buttonPanel.setMinimumSize(new Dimension(150, 300));
        m_buttonPanel.setPreferredSize(new Dimension(150, 300));
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
        
        
        m_scenarioVisButton.setEnabled(false);
        m_scenarioVisButton.setText("Szenario Visualisierung");
        m_scenarioVisButton.addActionListener(new GameParameterPanel_m_scenarioVisButton_actionAdapter(this));
        
        
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
        m_buttonPanel.add(m_scenarioVisButton,null);
        
                /*
                 * Rechtes Pane erstellen
                 */
        m_rightPane.setLayout(m_rightPaneLayout);
        m_rightPane.add(m_buttonPlaceHolder, BorderLayout.NORTH);
        m_rightPane.add(m_buttonPanel, BorderLayout.NORTH);
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
            
            LinkedList scenarioDescription = m_coordinator.getScenarioParameter(m_scenarioName, this.gameConf);
            if (scenarioDescription==null) {
                m_parent.writeStatusMessage(" Fehler! Konnte Parameter nicht lesen.");
                throw new RemoteException();
            }
            ListIterator iter = scenarioDescription.listIterator();
            while(iter.hasNext()) {
                description.addLast(iter.next());
            }
            
                /* Frage die Anzahl der Parameter ab, erzeuge ein Panel das
                 * groß genug ist alle Panels für jeden Parameter darzustellen
                 * und weise dem Panel ein GridLayout zu.
                 */
            m_maxParameterCount = description.size();
            
                    /*
                     * Aussenmasse des Parameter-Panels bestimmen
                     */
            int height = (22 * m_maxParameterCount);
            
            m_parameterGridLayout = new GridLayout(m_maxParameterCount,1);
            m_parameterPanel.setLayout(m_parameterGridLayout);
            
            m_parameterPanel.setMaximumSize(new Dimension(400, height));
            m_parameterPanel.setMinimumSize(new Dimension(400, height));
            m_parameterPanel.setPreferredSize(new Dimension(400, height));
            //      m_parameterPanel.setBorder(BorderFactory.createEtchedBorder());
            
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
                         * Infos für später zwischenspeichern
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
                                    520,
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
                //       panel.setBorder(BorderFactory.createEmptyBorder());
                panel.setMaximumSize(new Dimension(400,20));
                panel.setMinimumSize(new Dimension(400,20));
                panel.setPreferredSize(new Dimension(400,20));
                // Speichere Panel zur weiteren Verwendung
                m_panes.put(pName,panel);
                // Füge das Panel der Grundfläche hinzu.
                m_parameterPanel.add(panel);
            }
        }catch(RemoteException re) {
            System.out.println(
                    "Fehler: Die Parameterbeschreibung konnte nicht "
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
            m_scenarioVisButton.setEnabled(true);
            // starte Simulation
            this.m_parent.writeStatusMessage("Simulation gestartet.....");
            m_coordinator.startGame(m_gameId);
            this.m_parent.writeStatusMessage("Simulation ausgefuert!");
        } catch(Exception re) {
            System.out.println("Fehler: Das Spiel kann nicht gestartet werden.");
            this.m_parent.writeStatusMessage("Das Spiel konnte nicht gestartet werden, " +
                    "uberpruefen sie ob die Agenten registriert wurden.");
            re.printStackTrace(System.out);
        }
    }
    
    /**
     * Öffnet einen Frame zur Registrierung eines neuen Agenten.
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
                        pathName,
                        this);
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
        //  m_registerNewAgent.setEnabled(false);
        m_parent.repaintApplication();
    }
    
    /**
     * Speichert die Parameterwerte als Spiel beim Server.
     *
     * @param Event e
     */
    void m_gameSaveButton_actionPerformed(ActionEvent e) {
        m_parent.writeStatusMessage(
                "Beginne mit dem Speichern des Spiels.... ");
            /* Speichere die Daten, erzeuge dazu eine Hashmap mit allen Parameterwerten
             * drin.
             */
        boolean exceptionRaised=false;
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
                    try{
                        Boolean test=(Boolean)jpp.getParameterValue();
                    }catch (ClassCastException clCex){
                        m_parent.writeStatusMessage("Fehler bei der Konvertierung: " +
                                "Boolean-Type erwartet!");
                        exceptionRaised=true;
                        continue;
                    }
                    dto.put(jpp.getParameterName(),
                            (Boolean)jpp.getParameterValue());
                    m_parameter.put(jpp.getParameterName(),
                            (Boolean)jpp.getParameterValue());
                    jpp.setEditable(false);
                    break;}
                case ParameterDescription.DoubleType: {
                    try{
                        Double.valueOf((String)jpp.getParameterValue());
                    }catch (NumberFormatException nexIn){
                        m_parent.writeStatusMessage("Fehler bei der Konvertierung: " +
                                "Double-Type erwartet!");
                        exceptionRaised=true;
                        continue;
                    }
                    dto.put(jpp.getParameterName(),
                            Double.valueOf((String)jpp.getParameterValue()));
                    m_parameter.put(jpp.getParameterName(),
                            Double.valueOf((String)jpp.getParameterValue()));
                    jpp.setEditable(false);
                    break;}
                case ParameterDescription.FloatType: {
                    try{
                        Float.valueOf((String)jpp.getParameterValue());
                    }catch (NumberFormatException nexIn){
                        m_parent.writeStatusMessage("Fehler bei der Konvertierung: " +
                                "Float-Type erwartet!");
                        exceptionRaised=true;
                        continue;
                    }
                    dto.put(jpp.getParameterName(),
                            Float.valueOf((String)jpp.getParameterValue()));
                    m_parameter.put(jpp.getParameterName(),
                            Float.valueOf((String)jpp.getParameterValue()));
                    jpp.setEditable(false);
                    break;}
                case ParameterDescription.IntegerType: {
                    try{
                        Integer.valueOf((String)jpp.getParameterValue());
                    }catch (NumberFormatException nexIn){
                        m_parent.writeStatusMessage("Fehler bei der Konvertierung: " +
                                "Integer-Type erwartet!");
                        exceptionRaised=true;
                        continue;
                    }
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
        if ( !exceptionRaised ){
            try {
                /*
                 * Registriere ein Spiel beim Coordinator und speichere die Id, die vom
                 * Coordinator zurückgegeben wird.
                 */
                m_gameId = m_coordinator.registerGame(dto,this.gameConf);
                m_gameName = m_coordinator.getGameName(m_gameId);
                m_parent.writeStatusMessage(
                        "Speichern des Spiels " + m_gameName + " erfolgreich beendet.");
            }catch(Exception ex) {
                m_parent.writeStatusMessage(
                        "Das Speichern des Spiels konnte nicht durchgeführt werden.");
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
    }
    
    /**
     * Liefert den Namen des Spiels zurück.
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
    
    void setAgentRegisterButtonVisible(boolean visible){
        m_registerNewAgent.setEnabled(visible);
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
            deleted=true;
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
                        "Das Spiel enthält noch keine Agenten und deshalb kann "
                        + "keine Visualisierung angemeldet werden.");
                md.show();
            }else {
                                /*
                                 * Auswahl des Agenten den die Visualisierung betrachten soll.
                                 */
                SelectionDialog sd = new SelectionDialog(
                        "Agentenauswahl",
                        "Bitte wählen Sie den Agenten für die Visualisierung aus.",
                        "Agent: ",
                        agents);
                sd.show();
                // Holen den ausgewählten Agenten.
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
                    + "beachten Sie deshalb das Fenster für die Statusmeldungen.");
            md.show();
            m_parent.writeStatusMessage
                    ("Fehler: Bei der Anmeldung der Visualisierung trat der folgende"
                    + " Fehler: " + e.getMessage() + " auf.");
        }
        m_parent.repaintApplication();
    }
    
    /**
     * Speichere die Daten. bzw. Speichere Results.
     *
     * @param event
     */
    void m_saveResultsButton_actionPerformed(ActionEvent event) {
                /*
                 * Wähle das File aus.
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
    
    
    /**
     * Action die Visualisierung des Szenario aufruft
     */
    void m_scenarioVisButton_actionPerformed(ActionEvent event){
        try{
            int port=1099; // rmi port
            String gameId=this.m_gameId.toString();
            String serverName=ClientInfoObject.m_instance.getServerName();
            VisualisationClient visClient;
            String rundenNr="1";
            
       /*     // für den Test wird nach szenarien Konfigurationen gefragt.
            LinkedList tagNames=m_coordinator.getGameConfTags("bienenstock");
            String tagName="1";
            if ((tagNames!=null) && (tagNames.size()>=1)){
                if (tagNames.size()==1){
                    tagName=(String)tagNames.getFirst();
                }else{
                    //  tagNames.addFirst(" ");
        
                    Component cmp=this.m_parent.getParentComponent();
                    Frame frame=(Frame)cmp;
                    JSelectionDialog s2dialog =
                            new JSelectionDialog(
                            frame,
                            "Durchlauf-Auswahl",
                            "DurchlaufNr ",
                            tagNames);
                    s2dialog.show();
                    tagName= (String)s2dialog.getSelected();
                }
            }
            rundenNr=new String(tagName); */
            
            visClient=new VisualisationClient(serverName, port,this.m_gameId.toString(), rundenNr);
            
            String scenarioVisClassName=ClientInfoObject.m_instance.getVisualisationClassName(m_scenarioName);
            Class scenarioVisClass=Class.forName(scenarioVisClassName);
            IVisualisationOutput visOutput=(IVisualisationOutput)scenarioVisClass.newInstance();
            visOutput.initialize(visClient);
        }catch (Exception ex){
            ex.printStackTrace(System.out);
        }
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
        try{
            adaptee.m_gameSaveButton_actionPerformed(e);
        }catch (NumberFormatException nfe){}
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


class GameParameterPanel_m_scenarioVisButton_actionAdapter implements java.awt.event.ActionListener {
    GameParameterPanel adaptee;
    
    GameParameterPanel_m_scenarioVisButton_actionAdapter(GameParameterPanel adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(ActionEvent e) {
        adaptee.m_scenarioVisButton_actionPerformed(e);
    }
}


