/*
 * Dateiname      : IVisualisationServerIntern.java
 * Erzeugt        : 18. Mai 2005
 * Letzte Änderung: 26. Mai 1005 durch Samuel Walz
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *                  
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
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

package source.server.visualisation;

import java.io.Serializable;

/**
 * Die Schnittstelle der Server-Vis-Komponente für das Szenario.
 * 
 * @author  Samuel Walz
 */
public interface IVisualisationServerIntern {
    
    /**
     * Meldet ein Spiel für die Speicherung seiner Informationen an.
     * Gibt eine Zahl zurück, die als Authentifizierung für die Übergabe
     * der Informationen dient, damit diese dem richtigen Spiel zugeordnet
     * werden können. 
     *
     * @param spielID
     * @param wartezeit
     * @return             Eine natürliche Zahl größer Null.
     */
    public long spielAnmelden (int spielID, int wartezeit) throws Exception;
    
    /**
     * Speichert die Informationen eines Spiels.
     *
     * @param authCode       Eine natürliche Zahl größer Null.
     * @param information
     */
    public void speichereSpielInformationen (long authCode, 
            Serializable information);
    
    /**
     * Meldet ein Spiel für die Speicherungen von Informationen ab.
     *
     * @param authCode   Eine natürliche Zahl größer Null.
     */
    public void spielAbmelden (long authCode);
    
}
