/*
 * Dateiname      : StatistikBiene.java
 * Erzeugt        : 20. Mai 2005
 * Letzte Änderung: 22. Mai 2005 durch Eugen Volk
 * Autoren        : Eugen Volk
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Eugen Volk am Institut für Intelligente Systeme
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


package scenarios.bienenstock.statistik;

import scenarios.bienenstock.agenteninfo.Koordinate;
import nereus.utils.Id;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.*;
import java.lang.*;

/**
 * speichert die Werte der Biene zu jeder Runde.
 *
 * Zählt bei jedem Eintragen von Werten der Biene intern die Rundennummer hoch.
 * @author Philip Funck
 * @author Samuel Walz
 */
public class StatistikBienenAgent {
    
    /**
     * ID der zugehörigen Biene.
     */
    private Id bienenID;
    
    
    /**
     * Nummer der aktuellen Runde.
     */
    private int runde;
    
    /**
     * Zustand der zugehörigen Biene.
     */
    private String zustand;
    
    /** Enthält eine Liste aus verwendeten Parameter schlüsseln */
    private Vector statisticParameterKeys;
    
    private Hashtable scenarioParamters;
    
    /**
     * Tabelle mit Datensätzen zu jeweiligem ParameterNamen
     * zur Berechnung der statistischen Werten.
     */
    private Hashtable statisticValues=new Hashtable();
    
    /** Tabelle mit  den berechneten statistischen Werten */
    private Hashtable statisticResults=new Hashtable();
    
    /** Flag, der tru wird sobald die Berechnung abgeschlossen ist */
    boolean berechnungFertig=false;
    
    boolean berechnungStarted=false;
    /**
     * Der Konstruktor
     *
     * @param id ID der Biene als String
     * @param statistikParameterKeys Parameter-Schlüssel der Statistik
     * @param scenarioParameters Paramter für das verwendete Szenario
     */
    public StatistikBienenAgent(Id id, Vector statisticParameterKeys, Hashtable scenarioParameters){
        this.bienenID=new Id(id.toString());
        this.statisticParameterKeys=statisticParameterKeys;
        this.scenarioParamters=scenarioParameters;
        
        Enumeration enumer=statisticParameterKeys.elements();
        while (enumer.hasMoreElements()){
            Object key=enumer.nextElement();
            statisticValues.put(key, new Vector());
        }
    }
    
    
    
    /**
     * Fügt der Statistik einen Wert hinzu
     *
     * @param parameter Statistik-Parameter-Name
     * @wert Wert des Parameters
     */
    public void addInformation(String parameter, Object wert){
        if (parameter.equals(BienenStatistikKomponente.HONIG)) {
            Vector honigWerte=(Vector)statisticValues.get(BienenStatistikKomponente.HONIG);
            honigWerte.add(new Integer(wert.toString()));
        }
        if (parameter.equals(BienenStatistikKomponente.NEKTAR)) {
            Vector nektarWerte=(Vector)statisticValues.get(BienenStatistikKomponente.NEKTAR);
            nektarWerte.add(new Integer(wert.toString()));
        }
    }
    
    
    
    /**
     * liefert die Menge an gesamten gesammelten Nektar
     *
     * @return Summe an gesammelten Nektar
     */
    
    public int gibGesamtNektar(Vector nektarWerte){
        int summeNektar=0;
        int oldVal=0;
        int newVal;
        int diff;
        Enumeration enumer=nektarWerte.elements();
        if (enumer.hasMoreElements()) {
            oldVal=((Integer)enumer.nextElement()).intValue();
            summeNektar=oldVal;
        }
        while(enumer.hasMoreElements()){
            newVal=((Integer)enumer.nextElement()).intValue();
            diff=newVal-oldVal;
            if (diff>0) summeNektar=summeNektar+diff;
            oldVal=newVal;
        }
        return summeNektar;
    }
    
    /**
     * gibt Menge an gesamten verbrauchtem Honig
     * @return Summe an verbrauchtem Honig
     */
    public int gibGesamtHonig(Vector honigWerte){
        int summeHonig=0;
        int oldVal=0;
        int newVal;
        int diff;
        Enumeration enumer=honigWerte.elements();
        if (enumer.hasMoreElements()) {
            oldVal=((Integer)enumer.nextElement()).intValue();
            summeHonig=0;
        }
        while(enumer.hasMoreElements()){
            newVal=((Integer)enumer.nextElement()).intValue();
            diff=oldVal-newVal;
            if (diff>0) summeHonig=summeHonig+diff;
            oldVal=newVal;
        }
        return summeHonig;
    }
    
    
    /**
     * Führt berechnungen durch, basierend auf den ermittelten Einzelwerten.
     * Die Einzelwerte sind in statisticValues(parameterName) als Vector gespeichert.
     *
     */
    public void createStatistic(){
        berechnungStarted=true;
        Enumeration enumer=this.statisticParameterKeys.elements();
        while(enumer.hasMoreElements()){
            double result=0.0;
            String parameter=(String)enumer.nextElement();
            // ermittle die Menge an gesammelten Honig
            if (parameter.equals(BienenStatistikKomponente.HONIG)){
                Vector honigWerte=(Vector)this.statisticValues.get(parameter);
                result=gibGesamtHonig(honigWerte);
            }
            // ermittle die Menge an gesammelten Nektar
            if (parameter.equals(BienenStatistikKomponente.NEKTAR)){
                Vector nektarWerte=(Vector)this.statisticValues.get(parameter);
                result=gibGesamtNektar(nektarWerte);
            }
            
            this.statisticResults.put(parameter, new Double(result));
            
        }
        // berechnung abgeschlossen
        this.berechnungFertig=true;
    }
    
    
    
    
    
    
    /**
     * Gibt den Wert des gewünschten Statistikparameters zurück.
     *
     * @param parameter - Parameter der Abgefragt wurde
     * @return Double - Wert des Statistikparameter
     */
    public Double getParameter(String parameter){
        if (berechnungStarted==false) createStatistic();
        while(!this.berechnungFertig){}
        if (this.statisticResults.containsKey(parameter)){
            Double returnValue;
            returnValue=(Double)statisticResults.get(parameter);
            return returnValue;
        } else return null;
    }
    
    
    
    /**
     * gibt die ID der zugehörigen Biene zurück.
     *
     * @return   eine positive ganze Zahl
     */
    public Id gibID() {
        return bienenID;
    }
    
    
}
