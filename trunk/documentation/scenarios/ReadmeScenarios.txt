/*
 * Dateiname      : ReadmeScenarios.txt
 * Erzeugt        : 5. Juni 2005
 * Letzte Änderung: 6. Juni 2005 durch Dietmar Lippold
 * Autoren        : Eugen Volk
 *                  Dietmar Lippold
 *
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Eugen Volk am Institut für Intelligente Systeme
 * der Universität Stuttgart unter Betreuung von Dietmar Lippold
 * (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


Hinweise zum Erstellen eines neuen Szenarios
--------------------------------------------

Um neue Szenarien zu compilieren, müssen diese sich im Ordner
source/scenarios befinden. Die Datei Scenario.java muss sich im
Hauptverzeichnis des neuen Szenario-Ordners befinden, d.h. unter
source/scenarios/<scenario-name>/Scenario.java

Für die Agenten zum Szenario gelten folende Regeln:

 * Das Verzeichnis source/agents/<scenario-name>/ enthält die Agenten für
   den Client, wobei jeder lauffähige Agent mit "Agent" enden muss, z.B:
   BienenstockAgent.java.

 * Jeder Agent, der an einem Spiel teilnehmen sollte, muss in der Datei
   /config/clientconfig.xml eingetragen sein, um bei der Agentenanmeldung
   zur Verfügung zu stehen.

