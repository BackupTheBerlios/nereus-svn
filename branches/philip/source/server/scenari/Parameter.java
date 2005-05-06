/*
 * Erzeugt        : 20. Oktober 2004
 * Letzte Änderung: 14. Februar 2005 durch Samuel Walz
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

import java.util.Hashtable;
import java.util.HashMap;

/**
 * speichert die Parameter für das Szenario.
 * 
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Parameter {

    /**
     * enthält die Parameter.
     */
    private HashMap szenarioParameter = new HashMap();
    
    /**
     * enthält die Information, welche Parameter schreibgeschützt sind.
     */
    private Hashtable szenarioParameterSchreibschutz = new Hashtable();

    /**
     * Der Konstruktor.
     */
    public Parameter() {
    }

    /**
     * setzt einen Parameterwert, wenn dieser nicht schreibgeschützt ist.
     * 
     * @param schluessel    der Name des Parameters
     * @param wert          der Wert für den Parameter
     */
    public void setzeWert (String schluessel, Object wert) {
        if (szenarioParameterSchreibschutz.containsKey(schluessel)) {
            if (!((Boolean) szenarioParameterSchreibschutz.get(
                                schluessel)).booleanValue()) {
                szenarioParameter.put(schluessel, wert);
            }
        } else {
            szenarioParameterSchreibschutz.put(schluessel, new Boolean(false));
            szenarioParameter.put(schluessel, wert);
        }
    }

    /**
     * gibt den Wert eine statics Parameters als Objekt zurück.
     * 
     * Ist der Parameter nicht vorhanden, so wird <code>null</code> 
     * zurückgegeben.
     * 
     * @param schluessel    der Name des Parameters
     * @return              der zugehörige Wert als Objekt
     */
    public Object gibWert (String schluessel) {
        if (szenarioParameter.containsKey(schluessel)) {
            return szenarioParameter.get(schluessel);
        } else {
            System.out.print(schluessel);
            System.out.println(" ist nicht in szenarioParameter enthalten.");
            return null;
        }
    }

    /**
     * setzt für einen Parameter den Schreibschutz.
     * 
     * Einmal gesetzt, kann der Schreibschutz nicht mehr entfernt werden.
     * 
     * @param schluessel    der Name des Parameters, der Schreibschutz erhalten
     *                      soll
     */
    public void setzeSchreibschutz (String schluessel) {
        szenarioParameterSchreibschutz.put(schluessel, new Boolean(true));
    }

    /**
     * gibt zurück, ob für einen Parameter ein Schreibschutz gesetzt ist.
     * 
     * @param schluessel    der Name des fraglichen Parameters
     * @return boolean
     */
    public boolean gibSchreibschutz (String schluessel) {
        return ((Boolean) szenarioParameterSchreibschutz.get(
                                schluessel)).booleanValue();
    }
    
    /**
     * Gibt die Liste der Parameter zurück.
     * 
     * @return Hashtable
     */
    public HashMap gibParameterHashTabelle () {
        return (HashMap)szenarioParameter.clone();
    }
}


