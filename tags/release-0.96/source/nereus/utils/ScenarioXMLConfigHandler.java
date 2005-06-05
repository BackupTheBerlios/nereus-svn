/*
 * Dateiname      : ScenarioXMLConfigHandler.java
 * Erzeugt        : 29. April 2005
 * Letzte Änderung: 29. April 2005 durch Eugen Volk
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


package nereus.utils;

import org.xml.sax.helpers.DefaultHandler;
import java.util.LinkedList;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;





/**
 * BienenstockXMLConfigHandler erbt von org.xml.sax.helpert.DefaultHandler
 * und wird verwendet um die Eventbehandlung, die durch den SAXParser angestossen wird,
 * durchzuführen. Wird verwendet von scenario.bienenstock.getScenarioParameter() um
 * die Clients mit vorgegebenen Szenario-Werten zu initialisieren.
 *
 */
public class ScenarioXMLConfigHandler  extends DefaultHandler{
    
    /** Creates a new instance of BienenstockXMLConfigHandler */
    public ScenarioXMLConfigHandler() {
    }
    
    /**
     * Eine verketete Liste von PrameterDescription, als Konfiguration für die Szenario-Werte.
     */
    private LinkedList parameterListe;
    
    
    /**
     * überschreibt das StandardInterface und wird ausgeführt
     * zu Beginn des Parsens.
     */
    public void startDocument(){
        this.parameterListe = new LinkedList();
    }
    
    
    /**
     * überschreibt das StandardInterface und wird stets ausgeführt,
     * wenn ein Element aus der XML-Liste eingelesen wird.
     *
     * @param URI Angabe zur position der Datei
     * @param localname enthält den Namen des Elements
     * @param atts eine Liste der zum Element gehörenden Attribute
     */
    public void startElement(String URI, String localname, String name, Attributes atts){
        
        String propertyName;
        String propertyValue;
        String propertyType;
        String propertyChangeable;
        
        int length=atts.getLength();
        if (name.equals("property") && (length>0)){
            propertyName=atts.getValue("name");
            propertyValue=atts.getValue("wert");
            propertyType=atts.getValue("type");
            propertyChangeable=atts.getValue("changeable");
            
            parameterListe.add(
                    new ParameterDescription(
                    new String(propertyName),
                    this.getType(propertyType),
                    this.getTypeValue(propertyType, propertyValue),
                    this.getChangeable(propertyChangeable)));
        }
    }
    
    
    
    /**
     * Wandelt den eingelesenen Wert der als String notiert ist,
     * in ein entsprechendes (Integer, Double, ..) Wrapper-Objekt um.
     *
     * @param type Typ des Wertes als String
     * @param value der Wert als String
     * @return Wrapper Objekt mit dem entsprechenden Wert und Typ.
     */
    private Object getTypeValue(String type, String value){
        if (type.equals("int")) return new Integer(value);
        if (type.equals("float")) return new Float(value);
        if (type.equals("double")) return new Double(value);
        if (type.equals("string")) return new String(value);
        if (type.equals("String")) return new String(value);
        if (type.equals("boolean")) return new Boolean(value);
        else return null;
    }
    
    /**
     * Wandelt den Typ des eingelesenen Wertes, der als String notiert ist,
     * in ein entsprechend der utils#ParameterDescription() fesgelegten Integer-Wert um.
     * @param type Type des eingelesenen Wertes
     * @return
     */
    private int getType(String type){
        if (type.equals("int")) return ParameterDescription.IntegerType;
        if (type.equals("float")) return ParameterDescription.FloatType;
        if (type.equals("double")) return ParameterDescription.DoubleType;
        if (type.equals("string")) return ParameterDescription.StringType;
        if (type.equals("String")) return ParameterDescription.StringType;
        if (type.equals("boolean")) return ParameterDescription.BooleanType;
        else return 100;
    }
    
    /**
     * Wandelt den boolschen Wert der als String dargestellt ist
     * in die boolsche Darstellung um.
     *
     * @param changeable boolscher Wert als String
     * @return boolscher Wert
     */
    private boolean getChangeable(String changeable){
        if (changeable.equals("false")) return false;
        else return true;
    }
    
    
    /**
     * Liefert eine verketete Liste von utils#ParameterDescription Werten.
     * Die verketete Liste enthält die Konfiguration des aktuellen Szenarios.
     * @retun verketete Liste von PrameterDescription, als Konfiguration des Szenario-Werten.
     */
    public LinkedList getParameterListe(){
        
        return this.parameterListe;
    }
}
