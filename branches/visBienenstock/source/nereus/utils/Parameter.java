/*
 * Dateiname      : Parameter.java
 * Erzeugt        : 20. Oktober 2004
 * Letzte �nderung: 14. Februar 2005 durch Samuel Walz
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
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

package nereus.utils;

import java.util.Hashtable;
import java.util.HashMap;

/**
 * speichert die Parameter f�r das Szenario.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Parameter {
    
    /**
     * enth�lt die Parameter.
     */
    private HashMap szenarioParameter = new HashMap();
    
    /**
     * enth�lt die Information, welche Parameter schreibgesch�tzt sind.
     */
    private Hashtable szenarioParameterSchreibschutz = new Hashtable();
    
    /**
     * Der Konstruktor.
     */
    public Parameter() {
    }
    
    /**
     * setzt einen Parameterwert, wenn dieser nicht schreibgesch�tzt ist.
     *
     * @param schluessel    der Name des Parameters
     * @param wert          der Wert f�r den Parameter
     */
    public void setzeWert(String schluessel, Object wert) {
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
     * gibt den Wert eine statics Parameters als Objekt zur�ck.
     *
     * Ist der Parameter nicht vorhanden, so wird <code>null</code>
     * zur�ckgegeben.
     *
     * @param schluessel    der Name des Parameters
     * @return              der zugeh�rige Wert als Objekt
     */
    public Object gibWert(String schluessel) {
        if (szenarioParameter.containsKey(schluessel)) {
            return szenarioParameter.get(schluessel);
        } else {
            System.out.print(schluessel);
            System.out.println(" ist nicht in szenarioParameter enthalten.");
            return null;
        }
    }
    
    /**
     * setzt f�r einen Parameter den Schreibschutz.
     *
     * Einmal gesetzt, kann der Schreibschutz nicht mehr entfernt werden.
     *
     * @param schluessel    der Name des Parameters, der Schreibschutz erhalten
     *                      soll
     */
    public void setzeSchreibschutz(String schluessel) {
        szenarioParameterSchreibschutz.put(schluessel, new Boolean(true));
    }
    
    /**
     * gibt zur�ck, ob f�r einen Parameter ein Schreibschutz gesetzt ist.
     *
     * @param schluessel    der Name des fraglichen Parameters
     * @return boolean
     */
    public boolean gibSchreibschutz(String schluessel) {
        return ((Boolean) szenarioParameterSchreibschutz.get(
                schluessel)).booleanValue();
    }
    
    /**
     * Gibt die Liste der Parameter zur�ck.
     *
     * @return Hashtable
     */
    public Hashtable gibParameterHashTabelle() {
        return (Hashtable) szenarioParameter.clone();
    }
}


