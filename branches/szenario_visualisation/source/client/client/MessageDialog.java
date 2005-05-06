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

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import java.util.Vector;

/**
 * Die Klasse erzeugt ein Dialogfenster zum Anzeigen von Textnachrichten f�r den 
 * User. Der Dialog ist modal und kann vom User durch dr�cken des OK-Buttons 
 * geschlossen werden. Die Gr��e des Dialogfensters pa�t sich in der H�he und der
 * Breite der L�nge des �bergebenen Textes an. 
 * 
 * @author Daniel Friedrich
 */
public class MessageDialog extends JDialog {

  private String m_message = "Testlayout";
  private BorderLayout borderLayout1 = new BorderLayout();
  private GridLayout messageLayout = new GridLayout();
  private JPanel m_buttonPanel = new JPanel();
  private JPanel m_placeHolder = new JPanel();
  private GridLayout m_buttonGridLayout = new GridLayout();
  private JPanel m_leftButtonPlaceHolder = new JPanel();
  private JPanel m_rigthButtonPlaceHolder = new JPanel();
  private JPanel m_buttonPlace = new JPanel();
  private JButton m_okButton = new JButton();
  private JPanel m_messagePanel = new JPanel();
  private JLabel m_messageLabel = new JLabel();
  private int m_heigth = 150;
  private int m_width = 300;

	/**
	 * Konstruktor
	 * 			
 	 * @throws HeadlessException
 	 */
	public MessageDialog() throws HeadlessException {
	    super();
	    try {
	      jbInit();
	    }catch(Exception e) {
	      e.printStackTrace();
	    }
  	}

	/**
	 * Konstruktor
	 * 	
	 * @param String title - Titel des Dialogs
	 * @param String message - Nachricht die im Dialog angezeigt werden soll.		
	 * @throws HeadlessException
	 */
	public MessageDialog(String title, String message) throws HeadlessException {
		super();
	    try {
	    	setTitle(title);
	        m_message = message;
	        jbInit();
	    }catch (Exception e) {
	        e.printStackTrace();
	    }
    }
    
  	/**
  	 * Initialisiert die GUI.
  	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
	    this.setSize(new Dimension(300,150));
	    this.setModal(true);
	    this.setResizable(false);
	    this.getContentPane().setLayout(borderLayout1);
	
	    m_buttonPanel.setLayout(m_buttonGridLayout);
	    m_buttonGridLayout.setColumns(3);
	    m_buttonGridLayout.setHgap(0);
	    m_buttonPanel.setOpaque(true);
	    m_okButton.addActionListener(new JMessageDialog_m_okButton_actionAdapter(this));
	    messageLayout.setColumns(1);
	    m_messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    m_messageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	    this.getContentPane().add(m_buttonPanel,  BorderLayout.SOUTH);
	    this.getContentPane().add(m_placeHolder, BorderLayout.CENTER);
	    m_buttonPanel.add(m_leftButtonPlaceHolder, null);
	    m_buttonPanel.add(m_buttonPlace,null);
	    m_buttonPanel.add(m_rigthButtonPlaceHolder,null);
	    m_okButton.setText("OK");
	    m_buttonPlace.add(m_okButton);
	    /*
	     * MessageText notfalls umbrechen, wenn mehr als 80 Zeichen breit.
	     */
	    //StringBuffer buffer = new StringBuffer("");
	    m_messagePanel.setLayout(messageLayout);
	
	    messageLayout.setHgap(0);
	    messageLayout.setVgap(0);
	    int rowCount = 1;
	    Vector lines = new Vector();
	    lines.addElement("");
	    int letterCount = 50;
	
	    while( m_message.length() > (letterCount * 9) ) {
	      letterCount = letterCount + 10;
	      // Breite des Dialogs anpassen
	      m_width = m_width + 50;
	    }
	    while( m_message.length() > letterCount ) {
	      int lastSpace = m_message.lastIndexOf(" ",letterCount);
	      lines.addElement(m_message.substring(0,lastSpace));
	      m_message = m_message.substring(lastSpace+1, m_message.length());
	      rowCount++;
	    }
	    lines.addElement(m_message);
	    rowCount++;
	    messageLayout.setRows(rowCount);
	    if(rowCount > 5) {
	      /*
	       * Vergr��ere die H�he des Dialogs.
	       */
	      int moreLines = rowCount - 5;
	      m_heigth = m_heigth + (moreLines * 10);
	    }
	    this.setSize(new Dimension(m_width, m_heigth));
	    m_messagePanel.setMaximumSize(new Dimension(m_width, m_heigth-80));
	    m_messagePanel.setMinimumSize(new Dimension(m_width, m_heigth-80));
	    m_messagePanel.setPreferredSize(new Dimension(m_width, m_heigth-80));
	
	    m_buttonPanel.setPreferredSize(new Dimension(m_width, 40));
	    m_leftButtonPlaceHolder.setPreferredSize(new Dimension(110, 40));
	    m_rigthButtonPlaceHolder.setPreferredSize(new Dimension(110, 40));
	    m_placeHolder.setPreferredSize(new Dimension(m_width, 40));
	    m_buttonPlace.setPreferredSize(new Dimension(m_width - 220, 40));
	    for(int i = 0; i < rowCount; i++) {
	      JLabel tmp = new JLabel((String)lines.get(i));
	      tmp.setVerticalAlignment(JLabel.CENTER);
	      tmp.setHorizontalAlignment(JLabel.CENTER);
	      m_messagePanel.add(tmp, null);
	    }
	    this.getContentPane().add(m_messagePanel,BorderLayout.NORTH);
  	}

  	/**
  	 * Eventhandling f�r den OK-Button
  	 * 
	 * @param Event e
	 */
	void m_okButton_actionPerformed(ActionEvent e) {
	    this.setVisible(false);
	    this.dispose();
  	}
}

/**
 * @author Daniel Friedrich
 *
 * Eventhandling-Adapterklasse f�r den OK-Button
 */
class JMessageDialog_m_okButton_actionAdapter
	implements java.awt.event.ActionListener {
	MessageDialog adaptee;

	/**
	* Konstruktor.
	* 
	* @param adaptee
	*/
	JMessageDialog_m_okButton_actionAdapter(MessageDialog adaptee) {
		this.adaptee = adaptee;
	}
	/* (non-Javadoc)
	* @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	*/
	public void actionPerformed(ActionEvent e) {
		adaptee.m_okButton_actionPerformed(e);
	}
}
