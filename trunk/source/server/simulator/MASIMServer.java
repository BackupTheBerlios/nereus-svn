/*
 * Created on 22.05.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package simulator;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.io.File;


/**
 * Die Klasse dient nur als Startprogramm zum Ausführen des Coordinators von 
 * der Kommandozeile aus.
 * 
 * @author Daniel Friedrich
 */
public class MASIMServer {

	/**
	 * Zugriff auf den Coordinator, d.h. den eigentlichen Simulator.
	 */
    private static ICoordinator m_coordinator;    
    
	/**
	 * Methode zum Starten des Simulators.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(
				"MALServer: Coordinator wird versucht zu registrieren.");
			System.setSecurityManager(new RMISecurityManager());
			LocateRegistry.createRegistry(1099);
			String name;
			String basepath;
			String enviromentsPath;
			String hostname = "localhost";
			if(args.length == 1) {
				hostname = args[0];
				name = "rmi://" + hostname + ":1099" +"/Coordinator";
				System.out.println("register as: " + name);
			}else {
				name = "rmi://localhost/Coordinator";
			}
			String pathName = null;
			if (args.length > 1) {
			pathName = args[1]; 
			} else {
				File dFile = new File("");
				pathName = dFile.getAbsolutePath();
			}
			m_coordinator = new Coordinator(hostname, pathName);
			Naming.rebind(name, m_coordinator);
			System.out.println(
				"MultiAgentSimulator: Coordinator wurde erfolgreich registriert.");
			
			System.out.println("register ready");
		}catch (Exception e) {
			System.out.println(
				"Fehler: Der Coordinator konnte nicht bei der RMI-Registry registriert werden.");
			e.printStackTrace();
		}
	}
}
