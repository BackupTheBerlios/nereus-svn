/*
 * Erzeugt        : 30. November 2004
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

import java.util.LinkedList;
import java.io.FileInputStream;
import java.util.Properties;
import utils.ParameterDescription;
import java.io.FileNotFoundException;

/**
 * Hilfsmittel zum auslesen von Konfigurationsdateien.
 * 
 * @author Samuel Walz
 */
public class ParameterParser {
    
    /**
     * Nimmt eine Parameterliste entgegen und sucht in einer
     * Konfigurationsdatei nach identischen Schlüsseln - wird so ein
     * Schlüssel gefunden, so wird sein Wert als neuer Wert gesetzt
     * und der betroffene Parameter bekommt einen Schreibschutz.
     * Erwartet eine LinkedList,
     * die Objekte der Klasse utils.ParameterDescription enthält.
     * 
     * @param parameter         Die eingehende Parameterliste
     * @param dateiname         Die Datei, aus der die Parameter 
     *                          ausgelesen werden sollen
     * @return                  Die aktualisierte Parameterliste
     */
    public LinkedList setzePassendeParameter(LinkedList parameter,
                                             String dateiname) {
        Properties konfiguration = new Properties();
        LinkedList neueParameterListe = new LinkedList();
        
        ParameterDescription aktuellerParameter;
        String aktuellerSchluessel;
        int aktuelleKlasse;
        Object aktuellerDefaultwert;
        String neuerWert;
        
        FileInputStream konfigurationsdatei;

        try {
            konfigurationsdatei = new FileInputStream(dateiname);
            
            // Inhalt der Konfigurationsdatei parsen
            try {
                konfiguration.load(konfigurationsdatei);
            } catch (Exception fehler) {
                System.out.println("Konfigurationsdatei " + dateiname
                               + " konnte nicht geladen werden!");
            }
        } catch (FileNotFoundException fehler) {
            System.out.println("Konfigurationsdatei " + dateiname
                               + " nicht gefunden!");
        }
        
        
        
        /*
        * suchen welche Parameter auch in der Datei vorhanden sind und diese
        * übernehmen und schreibschützen
        */
        int i;
        for (i = 0; i < parameter.size(); i++) {
            aktuellerParameter = (ParameterDescription) parameter.get(i);
            // Wie ist der aktuelle Schlüssel?
            aktuellerSchluessel = aktuellerParameter.getParameterName();
            aktuelleKlasse = aktuellerParameter.getClassDescription();
            aktuellerDefaultwert = aktuellerParameter.getDefaultValue();

            /*
            * Existiert er auch in der Konfigurationsdatei?
            * Falls ja, dann Schreibschutz und den neuen Defaultwert setzen
            */
            if (konfiguration.containsKey(aktuellerSchluessel)) {
                // neuen Defaultwert holen
                neuerWert
                    = konfiguration.getProperty(aktuellerSchluessel);
                // und passend Konvertieren
                switch (aktuelleKlasse) {
                    case ParameterDescription.BooleanType:
                        aktuellerDefaultwert = new Boolean(neuerWert);
                        break;
                    case ParameterDescription.DoubleType:
                        aktuellerDefaultwert = new Double(neuerWert);
                        break;
                    case ParameterDescription.FloatType:
                        aktuellerDefaultwert = new Float(neuerWert);
                        break;
                    case ParameterDescription.IntegerType:
                        aktuellerDefaultwert = new Integer(neuerWert);
                        break;
                    case ParameterDescription.RealType:
                        aktuellerDefaultwert = new Double(neuerWert);
                        break;
                    case ParameterDescription.StringType:
                        aktuellerDefaultwert = neuerWert;
                        break;
                    default:
                        aktuellerDefaultwert = neuerWert;
                        break;
                }
                aktuellerDefaultwert 
                    = konfiguration.getProperty(aktuellerSchluessel);
                neueParameterListe.add(
                    new ParameterDescription(
                        aktuellerSchluessel,
                        aktuelleKlasse,
                        aktuellerDefaultwert,
                        false
                        )
                    );
            } else {
                // Parameter aus der alten Liste einfach in die neue übertragen
                neueParameterListe.add(
                    new ParameterDescription(
                        aktuellerSchluessel,
                        aktuelleKlasse,
                        aktuellerDefaultwert,
                        aktuellerParameter.isChangeable()
                        )
                    );
            }

        }

        return neueParameterListe;

    }

}