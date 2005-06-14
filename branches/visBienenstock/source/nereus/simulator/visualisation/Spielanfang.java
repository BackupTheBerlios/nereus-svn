/*
 * Dateiname      : Spielanfang.java
 * Erzeugt        : 13. Mai 2005
 * Letzte �nderung: 13. Juni 2005 durch Samuel Walz
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


package nereus.simulator.visualisation;

import java.io.Serializable;

/**
 * Markiert in einer LinkedList mit Informationsobjekten den Anfang eines
 * Spiels.
 *
 * @author  Samuel Walz
 */
public class Spielanfang implements Serializable {
    
    /**
     * Die Zeit der Anmeldung in Millisekunden
     */
    private long zeitDerAnmeldung = 0L;

    /**
     * Der Konstruktor
     *
     * @param startZeit   die Zeit der Anmeldung in Millisekunden
     */
    public Spielanfang(long anmeldungsZeit) {
        zeitDerAnmeldung = anmeldungsZeit;
    }
    
    /**
     * Gibt die Zeit der Anmeldung zur�ck.
     * 
     * @return     die Zeit in Millisekunden
     */
    public long gibAnmeldungsZeit() {
        return zeitDerAnmeldung;
    }
}

