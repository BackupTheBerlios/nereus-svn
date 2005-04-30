/*
 * Erzeugt        : 20. Januar 2005
 * Letzte �nderung: 25. Januar 2005 durch Samuel Walz
 *
 * Teil des Softwarepraktikums mit dem Titel
 *
 *   "Design und Implementierung eines Szenarios f�r einen
 *    Multiagenten-Simulator"
 *
 * bei Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de)
 *
 * Autoren  : Philip Funck (mango.3@gmx.de)
 *            Samuel Walz (felix-kinkowski@gmx.net)
 *
 * copyright: Institut f�r Intelligente Systeme, Universit�t Stuttgart (2004)
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
     * Die ID des Bienenvolks, der die Biene angeh�rt.
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
     * @param handler       Szenariohandler f�r den Agenten
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
     * Setzt den Szenariohandler f�r den Agenten.
     * 
     * @param bHandler      Der Szenariohandler f�r den Agenten
     */
    public abstract void setHandler (AbstractScenarioHandler bHandler);
    
    /**
     * Setzt f�r den Agenten einen neuen Aktionscode.
     * 
     * @param neuerAktionscode  Der neue Aktionscode f�r den Agenten
     * @return              Ein Boolean-Wert, der das gegl�ckte setzen
     *                      des Aktionscodes best�tigt
     */
    public abstract boolean aktionscodeSetzen(long neuerAktionscode);
    
    /**
     * Gibt die ID des Bienenvolkes zur�ck, zu dem der Agent geh�rt.
     * 
     * @return              Die ID des Bienenvolkes, dem der Agent
     *                      angeh�rt
     */
    public int gibVolksID() {
        return volksID;
    };
}
