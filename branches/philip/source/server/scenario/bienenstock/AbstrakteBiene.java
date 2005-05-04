/*
 * Erzeugt        : 20. Januar 2005
 * Letzte Änderung: 25. Januar 2005 durch Samuel Walz
 *
 * Teil des Softwarepraktikums mit dem Titel
 *
 *   "Design und Implementierung eines Szenarios für einen
 *    Multiagenten-Simulator"
 *
 * bei Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de)
 *
 * Autoren  : Philip Funck (mango.3@gmx.de)
 *            Samuel Walz (felix-kinkowski@gmx.net)
 *
 * copyright: Institut für Intelligente Systeme, Universität Stuttgart (2004)
 *            http://www.iis.uni-stuttgart.de
 */
package scenario.bienenstock;

import simulator.AbstractAgent;
import simulator.AbstractScenarioHandler;
import utils.Id;

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
    public abstract void setHandler (AbstractScenarioHandler bHandler);
    
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
