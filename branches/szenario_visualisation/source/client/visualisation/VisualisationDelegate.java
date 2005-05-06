/*
 * Created on 29.07.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package visualisation;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

import javax.swing.JFrame;

import utils.Id;

/**
 * Die Klasse dient zur Übermittlung der während der Simulation auf dem Server
 * aufgetretenen Informationen an den Client. Eine Instanz der Klasse wird dazu
 * als Remote-Objekt an den Server übermittelt. Alle übergebenen Informationen 
 * delegiert das Objekt dann an via RMI-Callback-Mechanismus an die konkrete 
 * Implementierung. Die Klasse wird eingesetzt, damit der Server die Klasse der
 * konkreten Visualisierungs-Implementierung des Clients nicht kennen muss.
 * 
 * @author Daniel Friedrich
 */
public class VisualisationDelegate
	extends UnicastRemoteObject 
	implements IVisualisation,
	Serializable {

	/**
	 * wirkliche Visualisierungskomponente.
	 */
	private IVisualisation m_visualisation;
	
	/**
	 * Spielparameter.
	 */
	private Hashtable m_parameter = new Hashtable();
	
	/**
	 * Id des Agenten für den die Visualisierung angemeldet wird.
	 */
	private Id m_id;	

	/**
	 * @throws java.rmi.RemoteException
	 */
	public VisualisationDelegate(
		Hashtable params, 
		Class visualisationClass,
		String agentName) throws RemoteException {
		super();
		m_parameter = params;
		m_id = new Id();
		try {  			
			Constructor cons = visualisationClass.getConstructor(
				new Class[]{Hashtable.class,String.class});
			m_visualisation = 
				(IVisualisation)cons.newInstance(
					new Object[]{m_parameter,agentName});
		}catch(Exception e) {
			System.out.println(
				"Das Erzeugen der Visualisierungskomponente schlug fehl.");
			e.printStackTrace(System.out);
		}
		
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param port
	 * @throws java.rmi.RemoteException
	 */
	public VisualisationDelegate(int port) throws RemoteException {
		super(port);
	}	

	/**
	 * @param port
	 * @param csf
	 * @param ssf
	 * @throws java.rmi.RemoteException
	 */
	public VisualisationDelegate(
		int port,
		RMIClientSocketFactory csf,
		RMIServerSocketFactory ssf)
		throws RemoteException {
		super(port, csf, ssf);
	}
	
	/**
	 * Liefert die Id des Agenten zurück, zu dem die Visualisierung gehört.
	 * 
	 * @return Id
	 */
	public Id getId() {
		return m_id;
	}	

	/* (non-Javadoc)
	 * @see visualisation.IVisualisation#reset()
	 */
	public void reset() throws RemoteException {
		m_visualisation.reset();
	}

	/* (non-Javadoc)
	 * @see visualisation.IVisualisation#updateVisParameter(int, double)
	 */
	public void updateVisParameter(int type, double value)
		throws RemoteException {
		m_visualisation.updateVisParameter(type,value);

	}

	/* (non-Javadoc)
	 * @see visualisation.IVisualisation#updateVisParameter(int, java.lang.String)
	 */
	public void updateVisParameter(int type, String value)
		throws RemoteException {
		m_visualisation.updateVisParameter(type,value);
	}
	
	/**
	 * Zeigt die Visualisierung an.
	 */
	public void show() {
		if(m_visualisation instanceof JFrame) {
			((JFrame)m_visualisation).show();
		}
	}
}
