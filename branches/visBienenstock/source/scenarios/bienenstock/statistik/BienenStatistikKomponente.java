/*
 * Dateiname      : BienenStatistikKomponente.java
 * Erzeugt        : 20. Mai 2005
 * Letzte Änderung: 27. Juni 2005 durch Eugen Volk
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

import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.Enumeration;
import nereus.utils.Id;
import java.util.Hashtable;
import nereus.exceptions.InvalidAgentException;
import nereus.exceptions.InvalidElementException;
import nereus.simulatorinterfaces.statistic.IStatisticComponent;


public class BienenStatistikKomponente implements IStatisticComponent {
    
    /** verbrauchte Honig-Menge */
    public static final String HONIGVERBRAUCHT="HonigVerbraucht";
    
    /**  Wie oft Nektar abgebaut */
    public static final String NEKTARABGEBAUT="NektarAbgebaut";
    
    public static final String NEKTARGESAMMELT="NektarGesammelt";
    
    
    public static final String HONIG="HONIG";
    public static final String NEKTAR="NEKTAR";
    
    public static final String ANZ_TANZEN="ANZ_TANZEN";
    
    public static final String ANZ_WARTEN="ANZ_WARTEN";
    
    public static final String ANZ_ZUSCHAUEN="ANZ_ZUSCHAUEN";
    
   
    
    /** viewiele Blumen wurden besucht */
    public static final String BLUMENBESUCHT="BlumenBesucht";
    
    /** wieviele Blumen wurden eigenständig (ohne fremde Hilfe) gefunden  */
    public static final String BLUMENSELBSTGEFUNDEN="BlumenSelbstGefunden";
    
    
    
    
    /**
     * Tabelle mit Szenaioparametern
     */
    private Hashtable scenarioParameters;
    /**
     * Liste mit mit den Statistikkomponenten aller Bienen
     */
    private HashMap statistikBienen=new HashMap();
    /**
     * Einheiten des Parameters
     */
    private HashMap statisticParameterPrefix = new HashMap();
    /**
     * erfasste Werte fuer die Statistik-Parameter
     */
    private HashMap statisticValues = new HashMap();
    /**
     * Durchschnittswerte der Parameter
     */
    private HashMap statisticResults = new HashMap();
    /**
     * die Schlüssel für den Zugriff
     */
    private Vector statisticParameterKeys = new Vector();
    
    private Vector bienenIds;
    
    /**
     * die Position innerhalb der Vektoren
     */
    private HashMap posInVector_ = new HashMap();
    
    boolean berechnungFertig=false;
    
    boolean berechnungStarted=false;
    /**
     * Konstruktor.
     *
     * @param statistikBienen die zu überwachenden Bienen
     */
    public BienenStatistikKomponente(Vector agentsIds,Hashtable scenarioParameters) {
        this.scenarioParameters=scenarioParameters;
        this.bienenIds=new Vector();
        statisticParameterKeys.add(this.NEKTAR);
        statisticParameterKeys.add(this.HONIG);
        statisticParameterKeys.add(this.ANZ_TANZEN);
        statisticParameterKeys.add(this.ANZ_WARTEN);
        statisticParameterKeys.add(this.ANZ_ZUSCHAUEN);
        
        statisticValues.put(this.NEKTAR, new Vector());
        statisticValues.put(this.HONIG, new Vector());
        
        statisticParameterPrefix.put(this.NEKTAR, " Einheiten");
        statisticParameterPrefix.put(this.HONIG, " Einheiten");
        statisticParameterPrefix.put(this.ANZ_TANZEN, " Runden");
        statisticParameterPrefix.put(this.ANZ_WARTEN, " Runden");
        statisticParameterPrefix.put(this.ANZ_ZUSCHAUEN, " Runden");
        
        Iterator itAgentsIds=agentsIds.iterator();
        
        while(itAgentsIds.hasNext()){
            Id agentId=(Id)itAgentsIds.next();
            this.bienenIds.add(agentId.toString());
            StatistikBienenAgent sBiene=new StatistikBienenAgent(agentId,
                    this.statisticParameterKeys ,this.scenarioParameters);
            /* biene in die Liste der StatistikBienen aufnehmen */
            this.statistikBienen.put(agentId.toString(),sBiene);
        }
        
    }
    
    
    
    
    
    
    /**
     * gibt den Wert eines gesuchten Parameters zurück.
     *
     * @param parameter der Parameter der zurüchgegeben werden soll
     * @return Vector der die Durchschnittswerte enthält
     */
    public Object getParameter(String parameter) {
        if (berechnungStarted==false) createStatistic();
        while(!berechnungFertig){}
        if (statisticResults.containsKey(parameter)) {
            return statisticResults.get(parameter);
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
        if (statisticParameterPrefix.containsKey(parameter)) {
            return (String) statisticParameterPrefix.get(parameter);
        } else {
            return null;
        }
    }
    
    
    
    /**
     * Fügt der Statistik neue Werte für eine Biene hinzu.
     *
     * @param agentId   die ID des Agenten
     * @param parameter der Parameter
     * @param value der Wert
     * @throws InvalidAgentException Agent nicht gefunden
     * @throws InvalidElementException Fehler
     */
    public void addInformation(Id agentId, String parameter, Object value)
    throws InvalidAgentException,
            InvalidElementException {
        if (this.bienenIds.contains(agentId.toString())) {
            StatistikBienenAgent sBiene=(StatistikBienenAgent)this.statistikBienen.get(agentId.toString());
            if (this.statisticParameterKeys.contains(parameter)){
                sBiene.addInformation(parameter, value);
            }else throw new InvalidElementException();
            
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
        return new Vector(statisticParameterKeys);
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
        berechnungStarted=true;
        int bienenSize=this.statistikBienen.size();
        Enumeration enumer=this.statisticParameterKeys.elements();
        while(enumer.hasMoreElements()){
            double result;
            double sum=0.0;
            String parameter=(String)enumer.nextElement();
            Iterator bienenIt=this.statistikBienen.values().iterator();
            while(bienenIt.hasNext()){
                Double bienenResult;
                try{
                StatistikBienenAgent sBiene=(StatistikBienenAgent)bienenIt.next();
                bienenResult=(Double)sBiene.getParameter(parameter);
                if (bienenResult==null) continue;
                else sum=sum+bienenResult.doubleValue();
                }catch (Exception exc){exc.printStackTrace();}
            }
            result=sum/bienenSize;
            this.statisticResults.put(parameter, new Double(result));
        }
        // berechnung abgeschlossen
        this.berechnungFertig=true;
        
    }
    
    
    
    
    /**
     * Gibt die Zahl der statistisch erfassten Bienen zurück.
     * @return gibt die Anzahl der überwachten Agenten zurück
     */
    public int getNumOfAgents() {
        return statistikBienen.size();
    }
    
    
    
    
}
