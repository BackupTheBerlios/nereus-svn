/*
 * Dateiname      : ReadmeServer
 * Erzeugt        : 5. Mai 2005
 * Letzte Änderung: 5. Mai 2005 durch Eugen Volk
 * Autoren        : Eugen Volk 
 *                  
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Eugen Volk am Institut für Intelligente Systeme
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

Funktion vom Server
-----------------------

Der Server ist ein zentraler Teil des Nereus-Systems. Er hat
folgende Aufgaben:

1. Entgegennahme der Anmeldung der Benutzer bzw. der Clients an dem Simulations-Server.
2. Initierung eines neuen Spiels durch Einlesen der Parameter von Clients.
3. Registrierung der von den Clients übertragenen Agenten-Klassen eines Scenario beim Spiel.
4. Simuliert initierte Spiele. 

Benutzung vom Server
-----------------------

Das Distributionspaket vom Client enthält folgende Dateien:

 * Readme               	: Datei mit allgemeinen Hinweisen zu Architeuthis.
 * ReadmeServer         	: Diese Datei mit Hinweise zum Server.
 * Server.jar           	: Die Klassen des Servers.
 * server.policy        	: Die Festlegung der Rechte, die der Server auf dem
                         	  Rechner hat, auf dem er läuft.
 * setup.sh             	: Einstellungen zum Betrieb des jeweiligen
                           	  Server unter Linux/Unix.
 * setup.bat            	: Einstellungen zum Betrieb des jeweiligen
                          	  Server unter Windows.
 * NereusServer.sh      	: Script zum Starten des Servers unter Linux/Unix.
 * NereusServer.bat    		: Script zum Starten des Servers unter Windows.
 


Die Dateien zum Starten des Server liegen im Verzeichnis runScripts.

Zum Starten des Servers muß man die environment-Variable JAVA_HOME auf das Verzeichnis 
vom JRE oder JDK setzen. In der tcsh unter Linux/Unix kann der Befehl z.B. 
folgendermaßen aussehen:

  setenv JAVA_HOME /usr/java/jdk

Alternativ kann man auch die Variable JAVA in der Datei setup.sh bzw. in
setup.bat setzen. In dieser Datei kann bzw. muß man außerdem noch folgende
Einträge ändern:

 * SERVER_HOST       : Dieser Variablen muß der Name oder die IP-Nummer des
                       Rechners zugewiesen werden, auf dem der Simulations-Server
                       läuft.

 * CLASS_SERVER_PORT : Der Port auf dem der ClassFileServer erreichbar ist. Per 
			     default ist das der Port 2003. 
                      
 * BASIS_PFAD	   : Das Verzeichnis in dem sich alle anderen Ordner (scenario, 
			     runSkripts, config, dist) befinden.

 * DIST_DIR          : Das Verzeichnis mit der jar-Datei vom Server.
                       Vorgegeben ist dieses relativ zum Verzeichnis
                       runScripts. Man braucht diese Variable also nicht zu
                       ändern, wenn man den Client aus diesem Verzeichnis
                       startet.

 * CONFIG_DIR      :   Das Verzeichnis mit der Konfigurationsdatei
                       server.policy. Vorgegeben ist dieses relativ zum
                       Verzeichnis runScripts. Man braucht diese Variable
                       also nicht zu ändern, wenn man den Dispatcher aus
                       diesem Verzeichnis startet.

Zum Starten des Servers ruft man dann den Start-Script auf.
Unter Linux/Unix: ./NereusServer.sh
Unter Windows   : NereusServer.bat

