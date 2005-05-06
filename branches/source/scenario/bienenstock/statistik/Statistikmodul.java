/*
 * Dateiname      : Statistikmodul.java
 * Erzeugt        : 20. Januar 2005
 * Letzte �nderung: 26. Januar 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *                  
 *                  
 *                  
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut f�r
 * Intelligente Systeme der Universit�t Stuttgart unter Betreuung von
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
package scenario.bienenstock.statistik;

import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import nereus.utils.Id;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidElementException;
import nereus.simulatorinterfaces.statistic.IStatisticComponent;

/**
 * stellt die Statistik des Szenarios zur Verf�gung.
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
     * die Schl�ssel f�r den Zugriff
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
     * @param statistikBienen die zu �berwachenden Bienen
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
     * setzt bei allen Statistikkomponenten die aktuellen Werte der zugeh�rigen
     * Bienen
     *
     * @param statistikBienen die Bienen f�r 
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
     * gibt den Wert eines gesuchten Parameters zur�ck.
     *
     * @param parameter der Parameter der zur�chgegeben werden soll
     * @return Vector der die Durchschnittswerte enth�lt
     */
    public Object getParameter(String parameter) {
        if (schluessel.contains(parameter)) {
            return ergebnisse.get(parameter);
        } else {
            return null;
        }
    }

    /**
     * gibt die Ma�einheit eines Parameters zur�ck.
     *
     * @param parameter spezifiziert den Parameter von 
     *          dem die Einheit zur�ckgegeben werden soll
     * @return gibt die Einheiten der Parameter zur�ck 
     */
    public String getParameterPrefix(String parameter) {
        if (prefix.containsKey(parameter)) {
            return (String) prefix.get(parameter);
        } else {
            return null;
        }
    }

    /**
     * F�gt der Statistik neue Werte f�r eine Biene hinzu.
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
     * Liefert eine Liste aller Parameternamen zur�ck.
     *
     * @return gibt die Parameternamen zur�ck
     */
    public Vector getParameters() {
        return schluessel;
    }

    /**
     * wird nicht genutzt.
     * 
     * @return gibt nichts zur�ck
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
     * Gibt die Zahl der statistisch erfassten Bienen zur�ck.
     * @return gibt die Anzahl der �berwachten Agenten zur�ck
     */
    public int getNumOfAgents() {
        return statBienen.size();
    }
}
