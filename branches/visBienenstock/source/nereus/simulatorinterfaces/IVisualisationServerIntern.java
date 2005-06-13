/*
 * Dateiname      : IVisualisationServerIntern.java
 * Erzeugt        : 18. Mai 2005
 * Letzte Änderung: 13. Juni 2005 durch Samuel Walz
 * Autoren        : Samuel Walz (samuel@gmx.info)
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


package nereus.simulatorinterfaces;

import java.io.Serializable;

import nereus.exceptions.DoppelterDurchlaufException;
//import nereus.utils.Id;

/**
 * Die Schnittstelle der Server-Vis-Komponente für das Szenario.
 * 
 * @author  Samuel Walz
 */
public interface IVisualisationServerIntern {

    /**
     * Meldet ein Spiel für die Speicherung seiner Informationen an.
     *
     * @param spielID                Die ID eines Spiels
     * @param spielDurchlauf           Der Ducrhlauf eines Spiels
     *
     * @throws doppeltesSpielException  Wenn ein Spiel mit der gleichen Kennung
     *                                  bereits angemeldet ist.
     */
    public void spielAnmelden (String spielID,
                               String spielDurchlauf) 
                               throws DoppelterDurchlaufException;

    /**
     * Übergibt der Server-Vis-Komponente die Empfohlene Wartezeit für die 
     * Client-Vis-Komponente beim warten auf neue Informationen des 
     * aufrufenden Szenarios.
     * 
     * @parem spielID                die ID eines Spiels
     * @param spielDurchlauf        der Durchlauf eines Spiels
     * @param empfohleneWartezeit    die Zeit in Millisekunden
     */
    public void setzeWartezeit(String spielID, 
                               String spielDurchlauf,
                               int empfohleneWartezeit);
    
    /**
     * Speichert die Informationen eines Spiels.
     *
     * @param spielID               die ID eines Spiels
     * @param spielDurchlauf    der Durchlauf eines Spiels
     * @param information      die zu speichernden Informationen
     */
    public void speichereSpielInformation (String spielID, 
                                           String spielDurchlauf,
                                           Serializable information);

    /**
     * Meldet ein Spiel für die Speicherungen von Informationen ab.
     *
     * @param spielKennung    die ID eines Spiels
     * @param spielDurchlauf   der Durchlauf eines Spiels
     */
    public void spielAbmelden (String spielID, String spielDurchlauf);
}

