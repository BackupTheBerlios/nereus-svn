/*
 * Dateiname      : Readme
 * Erzeugt        : 5. Mai 2005
 * Letzte �nderung: 5. Mai 2005 durch Eugen Volk
 * Autoren        : Eugen Volk 
 *                  
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Eugen Volk am Institut f�r Intelligente Systeme
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


Funktion von Nereus
-------------------------
Nereus ist ein Multiagentensimulator, programmiert in der plattformunabh�ngigen 
Sprache Java. Der Multiagentensimulator arbeitet als Server und erm�glicht es, 
mehrere Szenarien f�r Agenten gleichzeitig laufen zu lassen und l�sst sowohl 
rundenbasierte Spiele (Instanzen der Szenarien) als auch echtzeitbasierte Spiele
zu. Das Starten von neuen Spielen, deren �berwachung und das Anmelden von Agenten 
an den Spielen geschieht �ber Clients, die �ber das Internet auf den Server mit 
den dort vorhandenen Szenarien zugreifen.



Benutzung von Nereus
--------------------------

Es sind folgende Distributionspakete verf�gbar:

 * Server.tar.gz			: Enth�lt die Dateien mit dem Server.
 * Client.tar.gz			: Enth�lt die Dateien mit dem Client.


Nachdem man sich die gew�nschten Distributionspakete runtergeladen hat,
packt man diese jeweils mit folgendem Befehl unter Linux/Unix aus ("paket"
steht f�r den Namen des jeweiligen Distributionspakets):

  tar -zxvf paket.tar.gz

Dabei wird ein Unterverzeichnis im aktuellen Verzeichnis erzeugt. Darin
liegt ein Readme zu der entsprechenden Komponente, in der sich weitere
Hinweise finden.

F�r die Erzeugung der Distributionspakete aus den Quelldateien mit der
vorhandenen Datei build.xml mu� ant (http://ant.apache.org/) installiert
sein. Hinweise zur Erzeugung erh�lt man durch Eingabe von "ant" im
Verzechnis mit der Datei build.xml. Falls ant gcj benutzt und es dabei
Probleme gibt, sollte man das JDK von Sun oder IBM installieren und die
environment-Variable JAVA_HOME auf dieses JDK setzen.

Allgemein wird f�r Nereus als JDK das J2SE Version 1.4.2_07 oder
die Version von IBM ab 1.4.1 empfohlen.


Entwickler von Nereus
---------------------------

Nereus wurde am Institut f�r Intelligente Systeme der Universit�t
unter Betreuun von Dietmar Lippold
Stuttgart (http://www.iis.uni-stuttgart.de/) entwickelt und ist 
bei berlios (http://www.berlios.de/) gehostet. An der
Entwicklung waren bzw. sind folgende Personen beteiligt:

* Daniel Friedrich
* Eugen Volk (custos@developer.berlios.de)
* Philip Funck (mango.3@gmx.de)
* Samuel Walz (felix-kinkowski@gmx.net)
* Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de)

 * J�rgen Heit, juergen.heit@gmx.de (bis Version 1.0)
 * Andreas Heydlauff, AndiHeydlauff@gmx.de (bis Version 1.0)
 * Achim Linke, achim81@gmx.de (bis Prototyp)
 * Dietmar Lippold, dietmar.lippold@informatik.uni-stuttgart.de
 * Ralf Kible, ralf_kible@gmx.de (bis Prototyp)
 * Michael Wohlfart, michael.wohlfart@zsw-bw.de (ab Version 1.3.2)

Die Homepage von Nereus ist
http://nereus.berlios.de/
