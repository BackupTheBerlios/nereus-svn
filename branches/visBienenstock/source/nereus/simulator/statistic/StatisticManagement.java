/*
 * Dateiname      : StatisticManagement.java
 * Erzeugt        : 17. September 2003
 * Letzte Änderung: 28. Juni 2005 durch Eugen Volk
 * Autoren        : Daniel Friedrich
 *                  Eugen Volk
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
package nereus.simulator.statistic;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.HashMap;

import nereus.utils.Id;
import nereus.simulatorinterfaces.statistic.IStatisticComponent;

/**
 * Komponente zum Erstellen von Statistiken für ein Spiel mit einem beliebigen
 * Spiel.
 *
 * @author Daniel Friedrich
 */
public class StatisticManagement {
    
    /**
     * Id des Spiels für das die Statistik ist.
     */
    private Id m_gameId;
    
    /**
     * Name des Spiels für das die Statistik ist.
     */
    private String m_gameName;
    
    /**
     * Liste mit Parametern die ausgewertet werden sollen
     */
    private Vector m_parameters = new Vector();
    
    /**
     * Umweltparameter die ausgegeben werden sollen
     */
    private Hashtable m_envParameters = new Hashtable();
    
    /**
     * Statistiken der Spiele
     */
    private Vector m_statistics = new Vector();
    
    /**
     * Statistic
     */
    private String m_statisticText = null;
    
    /**
     * CSV-Ausgabe der Statistik
     */
    private String m_csvStatisticText = null;
    
    /**
     * Liste aller Agenten
     */
    private Hashtable m_agents = new Hashtable();
    
    /**
     * Hashtable mit den Prefixen der
     */
    private Hashtable m_parameterPrefixes = new Hashtable();
    
    
    /**
     * Konstruktor.
     */
    public StatisticManagement(
            Id gameId,
            String gameName,
            Hashtable envParameters) {
        super();
        m_gameId = gameId;
        m_gameName = gameName;
        m_envParameters = envParameters;
    }
    
    public void nextExperiment(IStatisticComponent statistic) {
        m_statistics.addElement(statistic);
    }
    
    /**
     * Liefert eine Trennlinie fär die Ausgabe.
     * @return String
     */
    private String getLine() {
        StringBuffer retval = new StringBuffer(
                "-------------------------------------------------");
        retval.append("-------------------------------\n");
        return retval.toString();
    }
    
    /**
     * erzeugt eine Statistik zu mehreren Spieldurchläufen
     */
    public void createStatistic() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getHead());
        buffer.append(this.calculateStatistic());
        m_statisticText = buffer.toString();
    }
    
    /**
     * gibt die Statistik zurück
     * @return Statistik
     */
    public String getGameStatistic() {
        return m_statisticText;
    }
    
    /**
     * Statistik-Werte zu einzelnen Agenten
     * @return Statistik Werte zu einzelnen Agenten.
     */
    public String getAgentStatistics() {
        StringBuffer buffer = new StringBuffer();
        Enumeration keys = m_agents.keys();
        while(keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object id = m_agents.get(key);
            for(int i=0; i < m_statistics.size(); i++) {
                //@TODO
            }
        }
        return buffer.toString();
    }
    
    /**
     * erzeugt eine Header für die Statistik
     * @return Header für die Statistik
     */
    public String getHead() {
        IStatisticComponent component =
                (IStatisticComponent)m_statistics.get(0);
        m_envParameters.put(
                "Agentenanzahl",
                new Integer(component.getNumOfAgents()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(getLine());
        buffer.append("Statistische Auswertung\n");
        buffer.append(getLine());
        buffer.append("Spiel: " + m_gameName + "\n");
        Date today = new Date();
        buffer.append("Datum: " + today.toString() + "\n");
        buffer.append("Experimente: " + m_statistics.size() + "\n");
        buffer.append(getLine());
        buffer.append("Umwelt: " + "\n");
        Enumeration keys = m_envParameters.keys();
        while(keys.hasMoreElements()) {
            Object key = keys.nextElement();
            buffer.append(
                    key.toString()
                    +  ": "
                    + m_envParameters.get(key).toString()
                    + "\n");
        }
        buffer.append(getLine());
        return buffer.toString();
    }
    
    /**
     * Berechnt die statistischen Daten über mehrere Spieldurchläufe.
     * @return statistische Daten als String
     */
    public String calculateStatistic() {
        StringBuffer retval = new StringBuffer();
        StringBuffer csvBuffer = new StringBuffer();
        /* Mittel-Wert zur Berechnung der Varianz */
        Hashtable mittelWerte=new Hashtable();
        double min=Double.POSITIVE_INFINITY;
        double max=Double.NEGATIVE_INFINITY;
        // Statistiken berechnen lassen
        for(int i=0; i < m_statistics.size();i++) {
            IStatisticComponent stat =
                    (IStatisticComponent)m_statistics.get(i);
            try {
                stat.createStatistic();
            }catch(Exception e) {
                e.printStackTrace(System.out);
            }
        }
                /*
                 * Neueste Statistischen Parameter abfragen und die Berechnung
                 * vorbereiten.
                 */
        IStatisticComponent statComponent =
                (IStatisticComponent)m_statistics.get(0);
        m_parameters = statComponent.getParameters();
        Enumeration elements = m_parameters.elements();
        while(elements.hasMoreElements()) {
            // Parametername holen
            String sKey = (String)elements.nextElement();
            // Prefix dafür holen
            m_parameterPrefixes.put(
                    sKey,statComponent.getParameterPrefix(sKey));
            // Im CSV-Buffer eine Spalte erstellen
            csvBuffer.append(sKey + ";");
        }
        // CSV-Header abschliessen
        csvBuffer.append("\n");
        retval.append("\nBerechnete Mittelwerte:  \n\n");
        // Werte berechnen
        Enumeration params = m_parameters.elements();
        while(params.hasMoreElements()) {
            String param = (String)params.nextElement();
            double amount = 0.0;
            int counter = 0;
            for(int i=0; i < m_statistics.size(); i++) {
                IStatisticComponent stat =
                        (IStatisticComponent)m_statistics.get(i);
                try {
                    // Wert für Parameter abfragen
                    Object sValue = stat.getParameter(param);
                    // Testen ob der überhaupt erfasst wurde
                    if(sValue != null) {
                        double wert=((Double)sValue).doubleValue();
                        amount = amount + wert;
                        // mitzählen, wie oft erfasst.
                        counter++;
                    }
                }catch(Exception e) {
                    e.printStackTrace(System.out);
                }
            }
            // Die Summe noch durch die Anzahl der erfassten Daten teilen
            double result = amount / ((double)counter);
            mittelWerte.put(param, new Double(result));
            // Daten wegspeichern
            retval.append(
                    param
                    + ": "
                    + Double.toString(result)
                    + statComponent.getParameterPrefix(param)
                    + "\n");
        }
        
        // Footer nach unten
        retval.append(getLine());
        
        /*  Standardabweichung bestimmen (sagt etwas über die Abweichung der Werte vom Mittelwert aus) */
        
        if (m_statistics.size()>1){
            retval.append("\nEinzelwerte und Standardabweichungen :    \n\n");
            params = m_parameters.elements();
            while(params.hasMoreElements()) {
                String param = (String)params.nextElement();
                retval.append("\nEinzelwerte zu " + param +" :  ");
                double sum = 0.0;
                min=Double.POSITIVE_INFINITY;
                max=Double.NEGATIVE_INFINITY;
                double mittelWert=((Double)mittelWerte.get(param)).doubleValue();
                int counter = 0;
                for(int i=0; i < m_statistics.size(); i++) {
                    IStatisticComponent stat =
                            (IStatisticComponent)m_statistics.get(i);
                    try {
                        // Wert für Parameter abfragen
                        Object sValue = stat.getParameter(param);
                        // Testen ob der überhaupt erfasst wurde
                        if(sValue != null) {
                            double wert=((Double)sValue).doubleValue();
                            if (wert<min) min=wert;
                            if (wert>max) max=wert;
                            /* Summme der quadr Abweichung vom Mittelwert bestimmen */
                            sum = sum + ((wert - mittelWert) * (wert - mittelWert));
                            // mitzählen, wie oft erfasst.
                            counter++;
                            retval.append(printDouble(wert) + " / ");
                        }
                    }catch(Exception e) {
                        e.printStackTrace(System.out);
                    }
                }
                
                retval.append("\nMinimaler Wert : "+ printDouble(min));
                retval.append(" ;  Maximaler Wert : " + printDouble(max));
                
                
                /* Varianz berechnen */
                double varianz = sum / ((double)counter);
                double stdAbweichung=Math.sqrt(varianz);
                retval.append("\nStandartabweichung zu  "+
                        param
                        + ": "
                        + printDouble(stdAbweichung)
                        + statComponent.getParameterPrefix(param)
                        + "\n");
            }
            
            retval.append(getLine());
        }
        
        // csv erstellen
        for(int index=0; index < m_statistics.size();index++) {
            IStatisticComponent sComponent =
                    (IStatisticComponent)m_statistics.get(index);
            csvBuffer.append(sComponent.getCSVDataset());
        }
        m_csvStatisticText = csvBuffer.toString();
        this.saveCSVStatistic();
        return retval.toString();
    }
    
    /**
     * speichert das Ergebnis des Spiels im CSV-Format ab.
     */
    public void saveCSVStatistic() {
        try {
            File statFile = new File("statistic" + m_gameId + ".csv");
            FileWriter logFileWriter = new FileWriter(statFile);
            logFileWriter.write(m_csvStatisticText);
            logFileWriter.close();
        }catch(Exception e) {
            e.printStackTrace(System.out);
        }
    }
    
    /**
     * gibt den double-Wert formatiert aus, mit zwei Stellen nach der Komma.
     * @param value zu formatierende Zahl
     * @return formatierte Zahl als String
     */
    private String printDouble(double value){
        String format="#0.00";
        DecimalFormat df = new DecimalFormat(format);
        return new String(df.format(value));
    }
    
}
