/*
 * Dateiname      : DoppeltesSpielException.java
 * Erzeugt        : 30. Mai 2005
 * Letzte �nderung: 30. Mai 1005 durch Samuel Walz
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

/**
 *
 * @author  Samuel Walz
 */
public class DoppeltesSpielException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>doppeltesSpielException</code> without detail message.
     */
    public DoppeltesSpielException() {
    }
    
    
    /**
     * Constructs an instance of <code>doppeltesSpielException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DoppeltesSpielException(String msg) {
        super(msg);
    }
}