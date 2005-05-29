/*
 * Dateiname      : IVisualisationServerIntern.java
 * Erzeugt        : 18. Mai 2005
 * Letzte �nderung: 26. Mai 1005 durch Samuel Walz
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *                  
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
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
 * Die Schnittstelle der Server-Vis-Komponente f�r das Szenario.
 * 
 * @author  Samuel Walz
 */
public interface IVisualisationServerIntern {
    
    /**
     * Meldet ein Spiel f�r die Speicherung seiner Informationen an.
     * Gibt eine Zahl zur�ck, die als Authentifizierung f�r die �bergabe
     * der Informationen dient, damit diese dem richtigen Spiel zugeordnet
     * werden k�nnen. 
     *
     * @param spielID
     * @param wartezeit
     * @return             Eine nat�rliche Zahl gr��er Null.
     */
    public long spielAnmelden (int spielID, int wartezeit) throws Exception;
    
    /**
     * Speichert die Informationen eines Spiels.
     *
     * @param authCode       Eine nat�rliche Zahl gr��er Null.
     * @param information
     */
    public void speichereSpielInformationen (long authCode, 
            Serializable information);
    
    /**
     * Meldet ein Spiel f�r die Speicherungen von Informationen ab.
     *
     * @param authCode   Eine nat�rliche Zahl gr��er Null.
     */
    public void spielAbmelden (long authCode);
    
}
