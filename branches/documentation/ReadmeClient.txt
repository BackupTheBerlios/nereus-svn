/*
 * Dateiname      : ReadmeClient
 * Erzeugt        : 5. Mai 2005
 * Letzte ƒnderung: 5. Mai 2005 durch Eugen Volk
 * Autoren        : Eugen Volk 
 *                  
 *
 * Diese Datei gehˆrt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Eugen Volk am Institut f¸r Intelligente Systeme
 * der Universit‰t Stuttgart unter Betreuung von Dietmar Lippold
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

Funktion vom Client
-----------------------

Der Client ist ein Teil des Nereus-Systems. Er hat
folgende Aufgaben:

1. Anmeldung des Benutzers an dem Simulations-Server.
2. Veranlasst den Server ein neues Spiel zu initieren durch Parametereingabe und 
   Auswahl des Szenario.
3. Uebertragung  (mithilfe von NereusClientWebserver) der Agenten-Klassen eines Scenario zum 
   Simulations-Server.
4. Veranlasst den Simulations-Server das initierte Spiel zu simulieren. 

Ein Spiel das von einem Benutzer initiert wurde, kann auch den anderen Benutzern
zugaenglich gemacht werden (durch Setzen des MultipleTabsAllowed Haeckchen).
Andere Benutzer koennen dann das Spiel (durch: Spiel-> Oeffne Spiel --> das 
initierte Spiel) auswahlen und eigene Agenten dem Spiel hinzufuegen.


Benutzung vom Client
-----------------------

Das Distributionspaket vom Client enth‰lt folgende Dateien:

 * Readme               	: Datei mit allgemeinen Hinweisen zu Architeuthis.
 * ReadmeClient         	: Diese Datei mit Hinweise zum Client.
 * Client.jar           	: Die Klassen der Client.
 * client.policy        	: Die Festlegung der Rechte, die der Client auf dem
                         	  Rechner hat, auf dem er l‰uft.
 * setup.sh             	: Einstellungen zum Betrieb des jeweiligen
                           	  Clients unter Linux/Unix.
 * setup.bat            	: Einstellungen zum Betrieb des jeweiligen
                          	  Clients unter Windows.
 * NereusClient.sh      	: Script zum Starten des Clients unter Linux/Unix.
 * NereusClient.bat    		: Script zum Starten des Clients unter Windows.
 * NereusClientWebserver.sh 	: Script zum Starten des ClassFileServers unter Linux/Unix.
 * NereusClientWebserver.bat 	: Script zum Starten des ClassFileServers unter Windows.


Die Dateien zum Starten des Clients liegen im Verzeichnis runScripts.

Zum Starten des Clients und des ClassFileServers muﬂ man die environment-Variable 
JAVA_HOME auf das Verzeichnis vom JRE oder JDK setzen. In der tcsh unter Linux/Unix 
kann der Befehl z.B. folgendermaﬂen aussehen:

  setenv JAVA_HOME /usr/java/jdk

Alternativ kann man auch die Variable JAVA in der Datei setup.sh bzw. in
setup.bat setzen. In dieser Datei kann bzw. muﬂ man auﬂerdem noch folgende
Eintr‰ge ‰ndern:

 * SERVER_HOST       : Dieser Variablen muﬂ der Name oder die IP-Nummer des
                       Rechners zugewiesen werden, auf dem der Simulations-Server
                       l‰uft.

 * CLASS_SERVER_PORT : Der Port auf dem der ClassFileServer erreichbar ist. Per 
			     default ist das der Port 2003. 
                      
 * BASIS_PFAD	   : Das Verzeichnis in dem sich alle anderen Ordner (scenario, 
			     runSkripts, config, dist) befinden.

 * DIST_DIR          : Das Verzeichnis mit der jar-Datei vom Client.
                       Vorgegeben ist dieses relativ zum Verzeichnis
                       runScripts. Man braucht diese Variable also nicht zu
                       ‰ndern, wenn man den Client aus diesem Verzeichnis
                       startet.

 * CONFIG_DIR      :   Das Verzeichnis mit der Konfigurationsdatei
                       client.policy. Vorgegeben ist dieses relativ zum
                       Verzeichnis runScripts. Man braucht diese Variable
                       also nicht zu ‰ndern, wenn man den Dispatcher aus
                       diesem Verzeichnis startet.

Bevor der Client gestartet wird, muss der ClassFileServer (als NereusClientWebserver 
bezeichnet), der fuer die Uebermittlung der Agenten an den Simulator zustaendig ist, 
gestartet werden.
Deswegen wird zun‰chst in eigenem Command-Fenster aufgerufen:
Unter Linux/Unix: ./NereusClientWebserver.sh
Unter Windows   : NereusClientWebserver.bat

Zum Starten des Client ruft man dann den Start-Script auf.
Unter Linux/Unix: ./NereusClient.sh
Unter Windows   : NereusClient.bat

