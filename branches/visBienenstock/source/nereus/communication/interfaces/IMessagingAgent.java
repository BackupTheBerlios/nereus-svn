/*
 * Dateiname      : IMessagingAgent.java
 * Erzeugt        : 20.Juni 2003
 * Letzte Änderung:
 * Autoren        : Daniel Friedrich
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
package nereus.communication.interfaces;

import nereus.communication.speachacts.Speachact;

/**
 * Das Interface beschreibt die Methode, die ein Agent implementieren muss,
 * damit er an der Kommunikation über den MessagingServer teilnehmen kann.
 *
 * @author Daniel Friedrich
 */
public interface IMessagingAgent {
    
    /**
     * Die Methode muss implementiert sein, damit ein Agent Nachrichten senden
     * und empfangen kann.
     *
     * @param act - Sprechakt der empfangen wird.
     * @return Speachact - Antwort auf den empfangenen Sprechakt
     */
    public Speachact receiveMessage(Speachact act);
}
