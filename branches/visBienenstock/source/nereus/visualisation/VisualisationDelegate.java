/*
 * Dateiname      : VisualisationDelegate.java
 * Erzeugt        : 17. Juli 2003
 * Letzte �nderung: 
 * Autoren        : Daniel Friedrich
 *                  
 *                  
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
package nereus.visualisation;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

import javax.swing.JFrame;

import nereus.utils.Id;

/**
 * Die Klasse dient zur �bermittlung der w�hrend der Simulation auf dem Server
 * aufgetretenen Informationen an den Client. Eine Instanz der Klasse wird dazu
 * als Remote-Objekt an den Server �bermittelt. Alle �bergebenen Informationen 
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
	 * Id des Agenten f�r den die Visualisierung angemeldet wird.
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
	 * Liefert die Id des Agenten zur�ck, zu dem die Visualisierung geh�rt.
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
