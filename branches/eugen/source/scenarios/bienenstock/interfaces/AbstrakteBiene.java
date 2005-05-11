/*
 * Dateiname      : AbstrakteBiene.java
 * Erzeugt        : 20. Januar 2005
 * Letzte Änderung: 25. Januar 2005 durch Samuel Walz
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *
 *
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut für
 * Intelligente Systeme der Universität Stuttgart unter Betreuung von
 * Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de).
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
package scenarios.bienenstock.interfaces;

import nereus.simulatorinterfaces.AbstractAgent;
import nereus.simulatorinterfaces.AbstractScenarioHandler;
import nereus.utils.Id;

/**
 *
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public abstract class AbstrakteBiene extends AbstractAgent {
    /**
     * Die ID des Bienenvolks, der die Biene angehört.
     */
    private int volksID;
    
    /**
     * Der Konstruktor.
     */
    public AbstrakteBiene() {
        super();
    }
    
    /**
     * Der Konstruktor.
     *
     * @param name          Name des Agenten
     * @param handler       Szenariohandler für den Agenten
     */
    public AbstrakteBiene(String name, AbstractScenarioHandler handler) {
        super(name, handler);
    }
    
    /**
     * Der Konstruktor.
     *
     * @param id            Id des Agenten
     * @param name          Name des Agenten
     */
    public AbstrakteBiene(Id id, String name) {
        super(id, name);
    }
    
    /**
     * Setzt den Szenariohandler für den Agenten.
     *
     * @param bHandler      Der Szenariohandler für den Agenten
     */
    public abstract void setHandler(AbstractScenarioHandler bHandler);
    
    /**
     * Setzt für den Agenten einen neuen Aktionscode.
     *
     * @param neuerAktionscode  Der neue Aktionscode für den Agenten
     * @return              Ein Boolean-Wert, der das geglückte setzen
     *                      des Aktionscodes bestätigt
     */
    public abstract boolean aktionscodeSetzen(long neuerAktionscode);
    
    /**
     * Gibt die ID des Bienenvolkes zurück, zu dem der Agent gehört.
     *
     * @return              Die ID des Bienenvolkes, dem der Agent
     *                      angehört
     */
    public int gibVolksID() {
        return volksID;
    };
}
