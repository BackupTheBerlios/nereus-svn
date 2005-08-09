/*
 * Dateiname      : GameXMLConfigHandler.java
 * Erzeugt        : 11. Mai 2005
 * Letzte Änderung: 11. Mai 2005 durch Eugen Volk
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
 * GameXMLConfigHandler erbt von org.xml.sax.helpert.DefaultHandler
 * und wird verwendet um die Eventbehandlung, die durch den SAXParser angestossen wird,
 * durchzuführen. Wird verwendet um die Konfiguration fuer den Simulator
 * aus einer XML-Konfigurationsdatei einzulesen.
 *
 */
public class GameXMLConfigHandler extends DefaultHandler{
    
    /** Creates a new instance of BienenstockXMLConfigHandler */
    public GameXMLConfigHandler() {
    }
    
    /** Eine Liste die aus GameConf besteht */
    private LinkedList gameConfList;
    
    /**
     * überschreibt das StandardInterface und wird ausgeführt
     * zu Beginn des Parsens.
     */
    public void startDocument(){
        this.gameConfList = new LinkedList();
    }
    
    
    /**
     * überschreibt das StandardInterface und wird stets ausgeführt,
     * wenn ein Element aus der XML-Liste eingelesen wird.
     *
     * @param URI Angabe zur position der Datei
     * @param localname enthält den Namen des Elements
     * @param name des Elements
     * @param atts eine Liste der zum Element gehörenden Attribute
     */
    public void startElement(String URI, String localname, String name, Attributes atts){
        
        String tagName;
        String scenarioName;
        String kartenDateiName;
        String parameterDateiName;
        //  AgentStruct agentStruct=null;
        
        if (name.equals("gameconf") && (atts.getLength()>0)) {
            tagName=atts.getValue("name");
            scenarioName=atts.getValue("scenario");
            kartenDateiName=atts.getValue("karte");
            parameterDateiName=atts.getValue("parameters");
            GameConf gameConf = new GameConf(tagName, scenarioName,
                    kartenDateiName, parameterDateiName);
            this.gameConfList.add(gameConf);
        }
    }
    
    
    /**
     * Liefert eine Liste aus SpielKonfigurationen, die zur Auswahl stehen.     *
     * @return eine Liste aus mehreren Spielkonfigurationen.
     */
    public LinkedList getGameConfList(){
        
        return this.gameConfList;
    }
}
