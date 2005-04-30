/*
 * Created on 17.07.2003
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

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Das Interface definiert die RMI-Methoden zur Aktualisierung der 
 * Visualisierungskomponenten auf der Clientseite vom Server aus.
 * Es werden Information zur Energie des Agenten, seiner Lernrate und
 * seiner Kommunikation übergeben.
 * 
 * @author Daniel Friedrich
 */
public interface IVisualisation extends Remote {
	
	// Konstante für den übertragenen Parameter.
	public static final int CommMsg = 0;
	public static final int StateMsg = 1;
	public static final int StatisticMsg = 2;
	public static final int LearningMsg = 3;
	
	public static final int LearningRate = 0;
	public static final int EnergyValue = 1;

	/**
	 * Aktualisiert die grafische Darstellung des Parameters parameter.
	 * 
	 * @param double value
	 * @throws RemoteException
	 */
	public void updateVisParameter(
		int type,
		double value) throws RemoteException;
	
	/**
	 * Aktualisiert die grafische Darstellung des Parameters parameter.
	 * 
	 * @param String value
	 * @throws RemoteException
	 */
	public void updateVisParameter(
		int type,
		String value) throws RemoteException;
		
	/**
	 * Wieder auf Runde 0 zurück gehen.
	 * @throws RemoteException
	 */
	public void reset() throws RemoteException;	
		
}
