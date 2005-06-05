/*
 * Dateiname      : AbstractAgent.java
 * Erzeugt        : 4. August 2003
 * Letzte Änderung:
 * Autoren        : Daniel Friedrich
 *
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
package nereus.simulatorinterfaces;

import nereus.utils.Id;
import nereus.exceptions.FullEnviromentException;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidElementException;

/**
 * Die Schnittstellte definiert alle Methoden, die ein Umwelt anbieten muss,
 * damit sie im Simulator einsetzbar ist.
 *
 * @author Daniel Friedrich
 */
public abstract class AbstractEnviroment {
    
    /**
     * Konstruktor.
     */
    public AbstractEnviroment() {
        super();
    }
    
    /**
     * Erstellt die Umwelt.
     *
     * @param graphFile
     * @param attributeFile
     * @throws InvalidElementException
     */
    public abstract void createEnviroment(
            String graphFile,
            String attributeFile)
            throws InvalidElementException;
    
    /**
     * Fügt der Umwelt den Agenten mit der Id agentId hinzu.
     *
     * @param agentId
     * @throws InvalidAgentException
     * @throws FullEnviromentException
     */
    public abstract void addAgentToEnviroment(Id agentId)
    throws InvalidAgentException,
            FullEnviromentException;
    
    /**
     * Entfernt den Agenten mit der Id agentId aus der Umwelt.
     *
     * @param agentId Id - Id des zu entfernenden Agenten.
     * @throws InvalidAgentException
     */
    public abstract void removeAgentFromEnviroment(Id agentId)
    throws InvalidAgentException;
    
}
