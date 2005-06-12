/*
 * Dateiname      : IVisualisationServerIntern.java
 * Erzeugt        : 18. Mai 2005
 * Letzte �nderung: 12. Juni 2005 durch Samuel Walz
 * Autoren        : Samuel Walz (samuel@gmx.info)
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


package nereus.simulatorinterfaces;

import java.io.Serializable;

import nereus.exceptions.DoppeltesSpielException;
//import nereus.utils.Id;

/**
 * Die Schnittstelle der Server-Vis-Komponente f�r das Szenario.
 * 
 * @author  Samuel Walz
 */
public interface IVisualisationServerIntern {

    /**
     * Meldet ein Spiel f�r die Speicherung seiner Informationen an.
     *
     * @param spielKennung     die Kennung des Spiels
     *
     * @throws doppeltesSpielException  Wenn ein Spiel mit der gleichen Kennung
     *                                  bereits angemeldet ist.
     */
    public void spielAnmelden (String spielKennung) 
        throws DoppeltesSpielException;

    /**
     * �bergibt der Server-Vis-Komponente die Empfohlene Wartezeit f�r die 
     * Client-Vis-Komponente beim warten auf neue Informationen des 
     * aufrufenden Szenarios.
     * 
     * @param empfohleneWartezeit    die Zeit in Millisekunden
     */
    public void setzeWartezeit(String spielKennung);
    
    /**
     * Speichert die Informationen eines Spiels.
     *
     * @param spielKennung     die Kennung des Spiels.
     * @param information      die zu speichernden Informationen
     */
    public void speichereSpielInformation (String spielKennung, 
                                           Serializable information);

    /**
     * Meldet ein Spiel f�r die Speicherungen von Informationen ab.
     *
     * @param spielKennung    die Kennung des Spiels.
     */
    public void spielAbmelden (String spielKennung);
}

