/*
 * Dateiname      : MASIMServer.java
 * Erzeugt        : 22. Mai 2003
 * Letzte Änderung: Eugen Volk 11.05.05
 * Autoren        : Daniel Friedrich
 *                  Eugen Volk
 *
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
package nereus.simulator;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.io.File;
import nereus.simulatorinterfaces.ICoordinator;
import nereus.simulator.ServerInfoObject;

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
                    "MASIMServer: Coordinator wird versucht zu registrieren.");
            System.setSecurityManager(new RMISecurityManager());
            LocateRegistry.createRegistry(1099);
            String name;
            String scenarioPath;
            String basePath;
            String hostname = "localhost";
            String configFileURI=null;
            ServerInfoObject serverInfoObject;
            if (args.length==4) {
                hostname = args[0];
                name = "rmi://" + hostname + ":1099" +"/Coordinator";
                System.out.println("register as: " + name);
                
                basePath=args[1];
                configFileURI=args[2];
                scenarioPath=args[3];
                
                
                serverInfoObject=ServerInfoObject.getInstance(basePath, configFileURI, scenarioPath);
                m_coordinator = new Coordinator(hostname, basePath);
                Naming.rebind(name, m_coordinator);
                System.out.println(
                        "MultiAgentSimulator: Coordinator wurde erfolgreich registriert.");
                
                System.out.println("register ready");
            }else {
                System.out.println(" Fehler !");
                System.out.println(" Erwartet: ");
                System.out.println(" hostname  basisPfad ConfigFileURI Scenario-Pfad");
            }
            
        }catch (Exception e) {
            System.out.println(
                    "Fehler: Der Coordinator konnte nicht bei der RMI-Registry registriert werden.");
            e.printStackTrace();
        }
    }
}
