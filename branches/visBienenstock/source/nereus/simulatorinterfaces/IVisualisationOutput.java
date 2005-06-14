/*
 * Dateiname      : IVisualisationOutput.java
 * Erzeugt        : 26. Mai 2005
 * Letzte Änderung: 14. Juni 2005 durch Dietmar Lippold
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
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


package nereus.simulatorinterfaces;
import nereus.visualisationclient.VisualisationClient;

/**
 * Definiert die Schnittstelle zur Ausgabe der Date der Visualisierung auf
 * Client-Seite.
 *
 * @author  Dietmar Lippold
 */
public interface IVisualisationOutput {

    /**
     * Übernimmt einen neuen Datensatz zur Visualisierung.<P>
     *
     * Diese Methode darf nicht blockieren, wenn die implementierende Klasse
     * die Methode <CODE>IVisualisationClient.stop()</CODE> aufruft.
     *
     * @param visObject  Der auszugebende Datensatz.
     */
    public void visualisiere(Object visObject);
    
    /**
     * Initialisiert das Objekt.
     *
     * @param vClient      VisualisierungsClient
     * @param verzeichnis  Ein Verzeichnis mit Daten für die Visualisierung.
     */
    public void initialize(VisualisationClient vClient, String verzeichnis);
}

