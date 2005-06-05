/*
 * Dateiname      : ScenarioXMLInputReader.java
 * Erzeugt        : 26. April 2005
 * Letzte Änderung: 26. April 2005 durch Eugen Volk
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


import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;


/**
 * Liest die Daten für die Konfiguration des Szenario aus der
 * XML-Konfigurationsdatei aus.
 */
public class ScenarioXMLInputReader {
    
    /** Liest die Daten für die Konfiguration des Szenarios aus der
     * XML-Konfigurationsdatei aus.
     *
     * @param uri Position der Konfiguratinsdatei.
     * @param handler erbt von org.xml.sax.helpers.DefaultHandler und ist für
     * das Parsen der Daten in der Konfigurationsdatei zuständig ist.
     *
     *
     */
    public ScenarioXMLInputReader(String uri, DefaultHandler handler) {
        SAXParserFactory factory=SAXParserFactory.newInstance();
        // Konfiguration des Parsers
        factory.setNamespaceAware(false); // true setzt Namespaces
        factory.setValidating(false); // false validiert den Dokument nicht
        try{
            //Parser wird erstellt
            SAXParser parser=factory.newSAXParser();
            
            //Parsen, das ergebnis steht im handler
            parser.parse(uri,handler);
        } catch (IOException ioex){
            System.out.println(" Datei nicht gefunden "+ uri);
        } catch (SAXException saxex){
            System.out.println(" Fehler beim Parsen der XML-Datei " + uri);
        } catch (Exception exc){ System.out.println(" Fehler in ScenarioXMLInputReader ");}
        
        
        
    }
    
}
