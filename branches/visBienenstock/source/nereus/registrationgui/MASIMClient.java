/*
 * Dateiname      : MASIMClient.java
 * Erzeugt        : 5. August 2003
 * Letzte Änderung: 9. Juni 2004 durch Eugen Volk
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

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import nereus.simulatorinterfaces.ICoordinator;
import nereus.utils.DataTableModel;
import nereus.utils.Id;
import nereus.utils.GameConf;
import java.io.File;

/**
 * Clientprogramm zur Steuerung und Verwaltung der Simulation von Spielen auf
 * dem Simulationsserver.
 *
 * @author Daniel Friedrich
 */
public class MASIMClient extends JFrame {
    
    /**
     * Name des Koordinators für den Zugriff auf den Simulationsserver
     */
    private String m_coordinatorName = "Coordinator";
    
    /**
     * Name des Simulationsservers
     */
    private String m_serverName;
    
    /**
     * Coordinator für Zugriff auf Server
     */
    private ICoordinator m_coordinator = null;
    
    /**
     * Agentenmodell
     */
    private DataTableModel m_gtm = null;
    
    /**
     * Spiele, d.h. für jedes Spiel ein Tab
     */
    private Vector m_gameTabs = new Vector();
    
    /**
     * Panels
     */
    private JPanel contentPane;
    
    /**
     * Menge aller angemeldeten Visualisierungskomponenten
     */
    private Vector m_visualisations = new Vector();
    
    /**
     * Panel für den oberen Bereich der GUI
     */
    private JPanel m_headPanel = new JPanel();
    /**
     * Layoutmanager für das headPanel
     */
    private BorderLayout m_headPanelLayout = new BorderLayout();
    /**
     * Label "Spiele: "
     */
    private JLabel m_gameLabel = new JLabel();
    /**
     * Label "offene Spiele "
     */
    private JLabel m_openGameLabel = new JLabel();
    
    /**
     * Panel für den mittleren Bereich der GUI.
     */
    private JPanel m_middlePanel = new JPanel();
    
    
    /**
     * Menubar
     */
    private JMenuBar m_menuBar = new JMenuBar();
    /**
     * Spielmenu
     */
    private JMenu m_spielMenu = new JMenu();
    /**
     * Neues Spieleintrag
     */
    private JMenuItem m_newGameEntry = new JMenuItem();
    /**
     * Öffne Spieleintrag
     */
    private JMenuItem m_openGameEntry = new JMenuItem();
    /**
     * Schliesse Clienteintrag
     */
    private JMenuItem m_exitEntry = new JMenuItem();
    /**
     * Tools-Menu
     */
    private JMenu m_toolsMenu = new JMenu();
    /**
     * Refresh der Visualisierung
     */
    private JMenuItem m_refreshEntry = new JMenuItem();
    /**
     * Hilfemenu
     */
    private JMenu m_helpMenu = new JMenu();
    
    /**
     * About-Fenster
     */
    private JMenuItem jMenuHelpAbout = new JMenuItem();
    
        /*
         * Layout-Manager
         */
    private BorderLayout borderLayout1 = new BorderLayout();
    private BorderLayout borderLayout2 = new BorderLayout();
    private BorderLayout borderLayout3 = new BorderLayout();
    private BorderLayout gameBorderLayout = new BorderLayout();
    
    /**
     * Pane für den Spiele-Bereich der GUI
     */
    private JTabbedPane m_tabbedPane = new JTabbedPane(); // Basisbereich
    
    /**
     * Srollpane für den Offene Spiele-Bereich
     */
    private JScrollPane m_openGamesScrollPane = new JScrollPane();
    /**
     * Tabelle für die offenen Spiele
     */
    private JTable m_openGamesTable = new JTable();
    
    /**
     * Unterer Bereich der GUI
     */
    private JPanel m_bottomPanel = new JPanel();
    /**
     * Unterer Bereich der GUI ScrollPan
     */
    private JScrollPane m_bottomScrollPane = new JScrollPane();
    /**
     * Label: "registrierte Agenten"
     */
    private JLabel m_registeredAgentLabel = new JLabel();
    /**
     * Layoutmanager für den unteren Bereich
     */
    private BorderLayout m_bottomPanelLayout = new BorderLayout();
    /**
     * Textfenster für Statusmeldungen.
     */
    private JTextArea m_bottomOutput = new JTextArea();
    
    /**
     * Border für den Bereich
     */
    private TitledBorder titledBorder1;
    
    private String pathName;
    
    
    /**
     * Konstruktor.
     *
     * @param hostname
     * @param agentPath
     */
    public MASIMClient(String hostname, String path) {
        super();
        m_serverName = hostname;
        pathName = path;
                /*
                 * hole den Coordinator
                 */
        this.getCoordinator();
        if(m_coordinator != null) {
            try {
                m_gtm = this.createDataModelForOpenGameTable(false);
            }catch(Exception e) {
                System.out.println("Fehler: beim Laden der Liste der offenen Spiele");
                e.printStackTrace(System.out);
            }
        }
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Erstellt die GUI.
     *
     * @throws Exception
     */
    private void jbInit() throws Exception  {
                /*
                 * Basisfläche einstellen
                 */
        titledBorder1 = new TitledBorder("");
        this.setSize(new Dimension(1054, 720));
        this.setTitle("MultiagentSimulatorClient");
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        contentPane.setMaximumSize(new Dimension(1054, 700));
        contentPane.setMinimumSize(new Dimension(1054, 700));
        contentPane.setPreferredSize(new Dimension(1054, 700));
        contentPane.addKeyListener(new KeyCommandListener());
        
                /*
                 * Key-Management erstellen
                 */
        this.addKeyListener(new KeyCommandListener());
        
                /*
                 * Menu erstellen
                 */
        m_spielMenu.setActionCommand("Spiel");
        m_spielMenu.setText("Spiel");
        // Neues Spiel Entry
        m_newGameEntry.setText("neues Spiel");
        m_newGameEntry.setActionCommand("newGame");
        m_newGameEntry.addActionListener(new MultiagentSimulatorClient_m_newGameEntry_ActionAdapter(this));
        // Öffne Spiel Entry
        m_openGameEntry.setText("öffne Spiel");
        m_openGameEntry.setActionCommand("openGame");
        m_openGameEntry.addActionListener(new MultiagentSimulatorClient_m_openGameEntry_ActionAdapter(this));
        // Exit-Entry
        m_exitEntry.setActionCommand("Exit");
        m_exitEntry.setText("Exit");
        m_exitEntry.addActionListener(new MultiagentSimulatorClient_m_exitEntry_ActionAdapter(this));
        
        // Tool-Menu
        // Refresh -Entry
        m_toolsMenu.setActionCommand("Tools");
        m_toolsMenu.setText("Tools");
        
        m_refreshEntry.setActionCommand("Refresh");
        m_refreshEntry.setText("Refresh");
        m_refreshEntry.addActionListener(new MultiagentSimulatorClient_m_refreshEntry_ActionAdapter(this));
        
        // Hilfe-Menu
        m_helpMenu.setText("Help");
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new MultiagentSimulatorClient_jMenuHelpAbout_ActionAdapter(this));
        
        
        // Menu anordnen
        m_spielMenu.add(m_newGameEntry);
        m_spielMenu.add(m_openGameEntry);
        m_spielMenu.add(m_exitEntry);
        
        m_toolsMenu.add(m_refreshEntry);
        
        m_helpMenu.add(jMenuHelpAbout);
        
        m_menuBar.add(m_spielMenu);
        m_menuBar.add(m_toolsMenu);
        m_menuBar.add(m_helpMenu);
        this.setJMenuBar(m_menuBar);
        
                /*
                 * Head-Panel aufbauen
                 */
        m_headPanel.setMaximumSize(new Dimension(1054, 20));
        m_headPanel.setMinimumSize(new Dimension(1054, 20));
        m_headPanel.setPreferredSize(new Dimension(1054, 20));
        
        // rechter Teil
        m_openGameLabel.setMaximumSize(new Dimension(454, 20));
        m_openGameLabel.setMinimumSize(new Dimension(454, 20));
        m_openGameLabel.setPreferredSize(new Dimension(454, 20));
        m_openGameLabel.setText("Offene Spiele:");
        
        // linker Teil
        m_gameLabel.setText("Spiele:");
        
        // zusammensetzen
        m_headPanel.setLayout(m_headPanelLayout);
        m_headPanel.add(m_gameLabel,BorderLayout.WEST);
        m_headPanel.add(m_openGameLabel,BorderLayout.EAST);
        
            /*
             * Mittleren Bereich aufbauen
             */
        m_middlePanel.setBorder(BorderFactory.createEtchedBorder());
        m_middlePanel.setDebugGraphicsOptions(0);
        m_middlePanel.setMaximumSize(new Dimension(1054, 430));
        m_middlePanel.setMinimumSize(new Dimension(1054, 430));
        m_middlePanel.setPreferredSize(new Dimension(1054, 430));
        m_middlePanel.setLayout(gameBorderLayout);
        //tabbed Panel
        
        m_tabbedPane.setMaximumSize(new Dimension(600, 32767));
        m_tabbedPane.setMinimumSize(new Dimension(600, 430));
        m_tabbedPane.setPreferredSize(new Dimension(600, 430));
        
        gameBorderLayout.setHgap(0); //---- zw. Gamepanel und links----
        
        m_openGamesScrollPane.setAutoscrolls(true);
        m_openGamesScrollPane.setMaximumSize(new Dimension(300, 32767));
        
        //für Offene Spiele -- Size
        m_openGamesTable.setMaximumSize(new Dimension(454, 5000));
        m_openGamesTable.setMinimumSize(new Dimension(454, 500));
        m_openGamesTable.setPreferredSize(new Dimension(454, 800));
        m_openGamesTable.setCellSelectionEnabled(false);
        m_openGamesTable.setColumnSelectionAllowed(false);
        m_openGamesTable.setIntercellSpacing(new Dimension(1, 1));
        if(m_gtm == null) {
            m_gtm = this.createDataModelForOpenGameTable(false);
        }
        m_openGamesTable.setModel(m_gtm);
        m_openGamesTable.createDefaultColumnsFromModel();
        m_openGamesTable.setRowSelectionAllowed(true);
        // m_openGamesTable.setLayout(new BorderLayout());
        // Zusammenfügen Mittel-Pane
        m_openGamesScrollPane.getViewport().add(m_openGamesTable, BorderLayout.CENTER);
        m_middlePanel.add(m_openGamesScrollPane,BorderLayout.EAST);
        m_middlePanel.add(m_tabbedPane,  BorderLayout.CENTER);
        m_openGamesTable.createDefaultColumnsFromModel();
        
            /*
             * Unterer GUI-Bereich
             */
        // jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        m_bottomPanel.add(m_bottomScrollPane,java.awt.BorderLayout.CENTER );
        m_bottomPanel.setBorder(BorderFactory.createEtchedBorder());
        m_bottomPanel.setMaximumSize(new Dimension(1054, 200));
        m_bottomPanel.setMinimumSize(new Dimension(1054, 100));
        m_bottomPanel.setPreferredSize(new Dimension(1054,100));
        m_bottomPanel.setLayout(m_bottomPanelLayout);
        
        
        m_bottomScrollPane.setViewportView(m_bottomOutput);
        m_bottomOutput.setText("");
        m_bottomOutput.setEditable(false);
        m_bottomOutput.setSize(90,10);
        
        m_bottomPanel.add(m_bottomScrollPane, BorderLayout.CENTER);
        //     m_bottomPanel.add(m_bottomOutput, BorderLayout.CENTER);
        
        
                /*
                 * Alles Zusammenfügen
                 *
                 */
        javax.swing.JSplitPane jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        
        jSplitPane1.setLeftComponent(m_middlePanel);
        jSplitPane1.setRightComponent(m_bottomPanel);
        
        jSplitPane1.setResizeWeight(0.8);
        
        //    getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);
        
        contentPane.add(m_headPanel, BorderLayout.NORTH);
        contentPane.add(jSplitPane1, java.awt.BorderLayout.CENTER);
        
        // contentPane.add(m_middlePanel, BorderLayout.CENTER);
        // contentPane.add(m_bottomPanel, BorderLayout.SOUTH);
        
        
        
        
    }
    
    /**
     * Eventhandling und Logik für das Erstellen eines neuen Spiels.
     *
     * @param e
     * @throws Exception
     */
    public void m_newGameEntry_actionPerformed(ActionEvent e) throws Exception {
        if(m_coordinator == null) {
            this.getCoordinator();
        }
        try {
            LinkedList scenarios = m_coordinator.getScenarioNames();
            scenarios.addFirst(" ");
            JSelectionDialog sdialog =
                    new JSelectionDialog(
                    this,
                    "Szenarioauswahl",
                    "Szenario",
                    scenarios);
            sdialog.show();
            String scenario = (String)sdialog.getSelected();
            
            
            // ClientInfoObject mit Servername und Agentenklassenpfad füllen
            ClientInfoObject clientInfoObject = ClientInfoObject.getInstance();
            clientInfoObject.setServerName(m_serverName);
            
            if((scenario != null)
            && (scenario.length() > 1)
            && (!scenarios.equals(" "))) {
                LinkedList tagNames=m_coordinator.getGameConfTags(scenario);
                GameConf gameConf;
                String tagName;
                if (tagNames.size()>=1){
                    if (tagNames.size()==1){
                        tagName=(String)tagNames.getFirst();
                    }else{
                        tagNames.addFirst(" ");
                        JSelectionDialog s2dialog =
                                new JSelectionDialog(
                                this,
                                "Spielkonfig.-Auswahl",
                                "Spielkonfig.",
                                tagNames);
                        s2dialog.show();
                        tagName= (String)s2dialog.getSelected();
                    }
                    if((tagName != null)
                    && (tagName.length() > 1)
                    && (!tagNames.equals(" "))){
                        
                        gameConf=this.m_coordinator.getGameConfToTag(tagName);
                        
                    } else {
                        return;
               /*         tagNames=this.m_coordinator.getGameConfTags(scenario);
                        tagName=(String) tagNames.getFirst();
                        gameConf=this.m_coordinator.getGameConfToTag(tagName); */
                        
                    }
                    
                    GameTab tab = new GameTab(
                            m_coordinator,
                            m_tabbedPane,
                            this,
                            scenario,
                            pathName,
                            gameConf);
                    
                    m_gameTabs.addElement(tab);
                    m_tabbedPane.add(tab, "Spiel");
                    tab.setMaximumSize(new Dimension(550, 32767));
                    tab.setMinimumSize(new Dimension(550, 5));
                    tab.setPreferredSize(new Dimension(550, 200));
                    m_tabbedPane.setSelectedComponent(tab);
                    //tab.requestDefaultFocus();
                }
            }
        }catch(Exception ex) {
            if(ex instanceof RemoteException) {
                new Exception("Fehler bei der Kommunikation mit dem Server: ", ex);
            }else {
                System.out.println("Fehler bei Anmelden eines Spiels.");
                ex.printStackTrace(System.out);
            }
        }
        this.repaint();
    }
    
    /**
     * Eventhandling und Logik zum Beenden des Clients.
     *
     * @param e
     */
    public void m_exitEntry_actionPerformed(ActionEvent e) {
        // alle Spiele schliessen die nur ein Tab erlauben
        Enumeration enum = m_gameTabs.elements();
        while(enum.hasMoreElements()) {
            GameTab tmpTab = (GameTab)enum.nextElement();
            Id gameId = tmpTab.getGameId();
            if(tmpTab.isGameSaved()) {
                try {
                    boolean tmp = ((Boolean)m_coordinator.getParameter(
                            gameId,
                            "MultipleGameTabsAllowed")).booleanValue();
                    if(!tmp) {
                        m_coordinator.removeGame(gameId);
                    }
                }catch(Exception ex) {
                    /* Exception wird nur dann ausgegeben, wenn das Spiel bereits
                       geschlossen ist. Deswegen folgende Ausgabe als Kommentar. E.V.*/
                /*    System.out.println("Fehler: Das Spiel konnte nicht gelöscht"
                            + " werden, bitte beachten sie die Statusmeldungen.");
                    ex.printStackTrace(System.out);
                    MessageDialog md = new MessageDialog("Fehler", "Das Spiel "
                            + gameId
                            + " konnte nicht geschlossen werden");  */
                }
            }
        }
        
        // Application schliessen
        System.exit(0);
    }
    
    /**
     * Eventhandling und Logik zum Anzeigen des About-Menus
     *
     * @param e
     */
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
        MALSIMClient_AboutBox dlg = new MALSIMClient_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.show();
    }
    
        /* (non-Javadoc)
         * @see java.awt.Window#processWindowEvent(java.awt.event.WindowEvent)
         */
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            m_exitEntry_actionPerformed(null);
        }
    }
    
    /**
     * Liefert den Zugriff auf den Coordinator auf der Serverseite.
     *
     * @return
     */
    private void getCoordinator() {
        System.setSecurityManager(new RMISecurityManager());
        String targetName = "rmi://" + m_serverName + ":1099" + "/" + m_coordinatorName;
        System.out.println("Ziel: " + targetName);
        try {
            //m_coordinator = (ICoordinator)Naming.lookup(m_serverName);
            m_coordinator = (ICoordinator)Naming.lookup(targetName);
            //return coordinator;
        } catch (Exception e) {
            System.out.println(
                    "Fehler: Der Coordinator konnte nicht geholt werden.");
            e.printStackTrace();
        }
    }
    
    /**
     * Startmethode.
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 4) {
            
                      
            System.out.println("Starte den MALClient");
            MASIMClient client = null;
            ClientInfoObject.getInstance(args[1], args[2], args[3]);
            client=new MASIMClient(args[0], args[2]);
            
            client.setVisible(true);
            Dimension dim = new Dimension(1054,700);
            client.setSize(dim);
            client.setBounds(200,200,dim.width,dim.height);
            client.show();
            System.out.println("Beende den MALClient");
        }else {
            System.out.println(
                    "Syntaxfehler: Bitte versuchen Sie es das nächste mal mit ");
            System.out.println(
                    "MALClient <hostname> <BaisPfad> <Pfad zu Client-KonfigDatei> <Agenten-Pfad>");
        }
    }
    
    
    
        /* (non-Javadoc)
         * @see java.awt.Component#repaint()
         */
    public void repaint() {
        m_gtm = this.createDataModelForOpenGameTable(false);
        m_openGamesTable.setModel(m_gtm);
        m_openGamesTable.createDefaultColumnsFromModel();
        super.repaint();
    }
    
    /**
     * erstellt ein neues DatenModel für die Tabelle der Offenen Spiele.
     *
     * @param value
     * @return
     */
    private DataTableModel createDataModelForOpenGameTable(boolean value) {
        DataTableModel retval = new DataTableModel(value);
        try {
            Vector tmp = m_coordinator.listAllOpenedGames();
            retval.addHeader("Id");
            retval.addHeader("Name");
            retval.addHeader("max. Agenten-Anz.");
            retval.addHeader("akt. Agenten-Anz.");
            if(tmp != null) {
                Enumeration enum = tmp.elements();
                while(enum.hasMoreElements()) {
                    retval.addRow((Object[])enum.nextElement());
                }
            }
        }catch(Exception e) {
            System.out.println("Fehler: beim Laden der Liste der offenen Spiele");
            e.printStackTrace(System.out);
        }
        return retval;
    }
    
    /**
     * Schreibt eine Statusmeldung in das Statusfeld
     *
     * @param message
     */
    public void writeStatusMessage(String message) {
        m_bottomOutput.append("\n");
        m_bottomOutput.append("Msg: ");
        m_bottomOutput.append(message);
    }
    
    /**
     * Sorgt dafür das beim Auftreten des Events ein Repaint der Anwendung durchgeführt wird.
     *
     * @param ActionEvnt event
     */
    public void m_refreshEntry_actionPerformed(ActionEvent event) {
        this.repaint();
    }
    
    /**
     * Sorgt dafür ein Dialog zum Öffnen eines Spieles geöffnet wird.
     *
     * @param ActionEvnt event
     */
    public void m_openGameEntry_actionPerformed(ActionEvent event) {
        if(m_coordinator == null) {
            this.getCoordinator();
        }
        try {
            Vector gameNames = m_coordinator.getAvailableGames();
            SelectionDialog sd = new SelectionDialog(
                    "Öffne Spiel",
                    "Wählen Sie das Spiel aus das sie öffnen wollen."
                    , "Spiel: "
                    , gameNames);
            sd.show();
            Object tmp = sd.getSelection();
            if(tmp != null) {
                String gameName = (String)tmp;
                Id gameId = m_coordinator.getGameId(gameName);
                Hashtable parameter = new Hashtable(m_coordinator.getParameters(gameId));
                String scenario = (String)parameter.get("ScenarioName");
                GameTab tab = new GameTab(
                        m_coordinator,
                        m_tabbedPane,
                        this,
                        scenario,
                        parameter,
                        m_serverName,
                        pathName);
                m_gameTabs.addElement(tab);
                m_tabbedPane.add(tab, gameName);
                tab.setMaximumSize(new Dimension(550, 32767));
                tab.setMinimumSize(new Dimension(550, 50));
                tab.setPreferredSize(new Dimension(550, 200));
            }
        }catch(Exception e) {
            MessageDialog md = new MessageDialog("Nachricht", "Das Spiel konnte" +
                    "leider nicht geladen werden.");
            md.show();
            this.writeStatusMessage("Das Spiel konnte nicht geladen werden, "
                    + "Grund: "
                    + e.getMessage());
        }
        this.repaint();
    }
    
    /**
     * Setzt eine Visualisierungskomponente für einen Agenten
     * @param av
     */
    public void setAVs(AgentVisualisation av) {
        m_visualisations.addElement(av);
    }
    
}



/**
 * @author Daniel Friedrich
 *
 * Key-Kommandolistener, der dafür sorgen soll, dass sich auf Druck der F5 Taste
 * die Oberfläche akualisiert.
 */
class KeyCommandListener implements KeyListener {
    
        /* (non-Javadoc)
         * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
         */
    public void keyPressed(KeyEvent event) {
                /*
                 * Beim drücken von "F5" wird ein Refresh durchgeführt, wird ESC
                 * gedrückt, dann wird das Programm beendet und alle Spiele werden ge
                 * schlossen.
                 */
        MASIMClient parent =
                (MASIMClient)event.getSource();
                /*
                 * Bei F5 neuzeichnen
                 */
        if(event.getKeyCode() == KeyEvent.VK_F5) {
            // neuzeichnen
            parent.repaint();
        }else if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // unsichtbar machen
            parent.setVisible(false);
            // alle Spiele schliessen
            
            // die Applikation schliessen
            parent.dispose();
        }
        
        
    }
    
        /* (non-Javadoc)
         * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
         */
    public void keyReleased(KeyEvent e) {}
    
        /* (non-Javadoc)
         * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
         */
    public void keyTyped(KeyEvent e) {}
}

/**
 * @author Daniel Friedrich
 *
 * Eventhandling-Adapterklasse.
 */
class MultiagentSimulatorClient_m_openGameEntry_ActionAdapter implements ActionListener {
    MASIMClient adaptee;
    
    MultiagentSimulatorClient_m_openGameEntry_ActionAdapter(MASIMClient adaptee) {
        this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
        adaptee.m_openGameEntry_actionPerformed(e);
    }
}


/**
 * @author Daniel Friedrich
 *
 * Eventhandling-Adapterklasse.
 */
class MultiagentSimulatorClient_m_refreshEntry_ActionAdapter
        implements ActionListener {
    MASIMClient adaptee;
    
    /**
     * Konstruktor.
     *
     * @param adaptee
     */
    MultiagentSimulatorClient_m_refreshEntry_ActionAdapter(MASIMClient adaptee) {
        this.adaptee = adaptee;
    }
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
    public void actionPerformed(ActionEvent e) {
        adaptee.m_refreshEntry_actionPerformed(e);
    }
}

/**
 * @author Daniel Friedrich
 *
 * Eventhandling-Adapterklasse.
 */
class MultiagentSimulatorClient_m_newGameEntry_ActionAdapter
        implements ActionListener {
    MASIMClient adaptee;
    
    /**
     * Konstruktor.
     *
     * @param adaptee
     */
    MultiagentSimulatorClient_m_newGameEntry_ActionAdapter(MASIMClient adaptee) {
        this.adaptee = adaptee;
    }
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
    public void actionPerformed(ActionEvent e) {
        try {
            adaptee.m_newGameEntry_actionPerformed(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

/**
 * @author Daniel Friedrich
 *
 * Eventhandling-Adapterklasse.
 */
class MultiagentSimulatorClient_m_exitEntry_ActionAdapter
        implements ActionListener {
    MASIMClient adaptee;
    
    /**
     * Konstruktor.
     *
     * @param adaptee
     */
    MultiagentSimulatorClient_m_exitEntry_ActionAdapter(MASIMClient adaptee) {
        this.adaptee = adaptee;
    }
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
    public void actionPerformed(ActionEvent e) {
        adaptee.m_exitEntry_actionPerformed(e);
    }
}

/**
 * @author Daniel Friedrich
 *
 * Eventhandling-Adapterklasse.
 */
class MultiagentSimulatorClient_jMenuHelpAbout_ActionAdapter
        implements ActionListener {
    MASIMClient adaptee;
    
    /**
     * Konstruktor.
     *
     * @param adaptee
     */
    MultiagentSimulatorClient_jMenuHelpAbout_ActionAdapter(MASIMClient adaptee) {
        this.adaptee = adaptee;
    }
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
    public void actionPerformed(ActionEvent e) {
        adaptee.jMenuHelpAbout_actionPerformed(e);
    }
}
