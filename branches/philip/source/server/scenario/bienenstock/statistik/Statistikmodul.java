/*
 * Erzeugt        : 20. Januar 2005
 * Letzte Änderung: 26. Januar 2005 durch Philip Funck
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
package scenario.bienenstock.statistik;

import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import utils.Id;
import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;
import simulator.statistic.IStatisticComponent;

/**
 * stellt die Statistik des Szenarios zur Verfügung.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Statistikmodul implements IStatisticComponent {

    /**
     * Liste mit mit den Statistikkomponenten aller Bienen
     */
    private HashSet statBienen;
    /**
     * Einheit des Parameters
     */
    private HashMap prefix = new HashMap();
    /**
     * die Werte
     */
    private HashMap werte = new HashMap();
    /**
     * Durchschnittswerte der Parameter
     */
    private HashMap ergebnisse = new HashMap();
    /**
     * die Schlüssel für den Zugriff
     */
    private Vector schluessel = new Vector();
    /**
     * verstorbene Bienen
     */
    private HashSet toteBienen = new HashSet();
    /**
     * die Position innerhalb der Vektoren
     */
    private HashMap posInVector = new HashMap();

    /**
     * Konstruktor.
     *
     * @param statistikBienen die zu überwachenden Bienen
     */
    public Statistikmodul(HashSet statistikBienen) {
        statBienen = statistikBienen;
        schluessel.addElement("NEKTAR");
        schluessel.addElement("HONIG");
        //schluessel.addElement("ZUSTAND");
        werte.put("NEKTAR", new Vector());
        werte.put("HONIG", new Vector());
        //werte.put("ZUSTAND", new Vector());
        prefix.put("NEKTAR", "Einheiten");
        prefix.put("HONIG", "Einheiten");
        //prefix.put("ZUSTAND", "N.a.");

        Iterator itStatBienen = statBienen.iterator();
        int pos = 0;
        while (itStatBienen.hasNext()) {

            StatistikBiene tmp = (StatistikBiene) itStatBienen.next();
            Vector vecTmp;
            ((Vector) werte.get(
                    "NEKTAR")).add(pos, new Integer(tmp.gibNektar()));
            ((Vector) werte.get("HONIG")).add(pos, new Integer(tmp.gibHonig()));
            posInVector.put(tmp.gibID(), new Integer(pos));
            pos++;
        }

    }

    /**
     * setzt bei allen Statistikkomponenten die aktuellen Werte der zugehörigen
     * Bienen
     *
     * @param statistikBienen die Bienen für 
     *        die neue Werte gesetzt werden sollen
     */
    public void neueWerteSetzen(HashSet statistikBienen) {

        Iterator itStatBienen = statBienen.iterator();
        int pos;
        while (itStatBienen.hasNext()) {
            StatistikBiene tmp = (StatistikBiene) itStatBienen.next();

            pos = ((Integer) posInVector.get(tmp.gibID())).intValue();

            if (statistikBienen.contains(tmp)) {
                ((Vector) werte.get(
                        "NEKTAR")).add(pos, new Integer(tmp.gibNektar()));
                ((Vector) werte.get(
                        "HONIG")).add(pos, new Integer(tmp.gibHonig()));
                //((Vector)werte.get("ZUSTAND")).add(pos, tmp.gibZustand());
            } else {
                ((Vector) werte.get("NEKTAR")).add(pos, new Integer(0));
                ((Vector) werte.get("HONIG")).add(pos, new Integer(0));
                //((Vector)werte.get("ZUSTAND")).add("tot");
            }
        }
        statBienen = statistikBienen;
    }

    /**
     * gibt den Wert eines gesuchten Parameters zurück.
     *
     * @param parameter der Parameter der zurüchgegeben werden soll
     * @return Vector der die Durchschnittswerte enthält
     */
    public Object getParameter(String parameter) {
        if (schluessel.contains(parameter)) {
            return ergebnisse.get(parameter);
        } else {
            return null;
        }
    }

    /**
     * gibt die Maßeinheit eines Parameters zurück.
     *
     * @param parameter spezifiziert den Parameter von 
     *          dem die Einheit zurückgegeben werden soll
     * @return gibt die Einheiten der Parameter zurück 
     */
    public String getParameterPrefix(String parameter) {
        if (prefix.containsKey(parameter)) {
            return (String) prefix.get(parameter);
        } else {
            return null;
        }
    }

    /**
     * Fügt der Statistik neue Werte für eine Biene hinzu.
     * 
     * @param agentId   die ID des Agenten
     * @param parameter die Parameter
     * @param value der Wert
     * @throws InvalidAgentException Agent nicht gefunden
     * @throws InvalidElementException Fehler
     */
    public void addInformation(Id agentId, String parameter, Object value)
    throws InvalidAgentException,
           InvalidElementException {
        if (schluessel.contains(parameter)
                    && posInVector.containsKey(agentId)) {
            ((Vector) werte.get(parameter)).add(
                    ((Integer) posInVector.get(agentId)).intValue(),
                    value);
        } else {
            throw new InvalidAgentException(agentId.toString());
        }
    }

    /**
     * Liefert eine Liste aller Parameternamen zurück.
     *
     * @return gibt die Parameternamen zurück
     */
    public Vector getParameters() {
        return schluessel;
    }

    /**
     * wird nicht genutzt.
     * 
     * @return gibt nichts zurück
     */
    public String getCSVDataset() {
        return null;
    }

    /**
     * wertet die statistischen Daten aus.
     */
    public void createStatistic() {
        int i;
        for (i = 0; i < schluessel.size(); i++) {
            Vector tmp = (Vector) werte.get(schluessel.get(i));
            int j;
            double summe = 0;
            for (j = 0; j < tmp.size(); j++) {
                summe = summe + ((Integer) tmp.get(j)).doubleValue();
            }
            summe = summe / (double) posInVector.size();
            ergebnisse.put(schluessel.get(i), new Double(summe));
        }
    }

    /**
     * Gibt die Zahl der statistisch erfassten Bienen zurück.
     * @return gibt die Anzahl der überwachten Agenten zurück
     */
    public int getNumOfAgents() {
        return statBienen.size();
    }
}
