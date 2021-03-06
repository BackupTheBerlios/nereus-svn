/*
 * Dateiname      : BienenstockVisualisierung.java
 * Erzeugt        : 10. Juni 2005
 * Letzte �nderung: 14. Juni 2005 durch Dietmar Lippold
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *                  Dietmar Lippold
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
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


package scenarios.bienenstock.visualisationgui;

import java.lang.NumberFormatException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.net.MalformedURLException;

import nereus.visualisationclient.VisualisationClient;

/**
 * Startet die Visualisierung f�r das Bienenstock-Szenario.
 *
 * @author  Samuel Walz
 * @author  Dietmar Lippold
 */
public class BienenstockVisualisierung {
    
    /**
     * Startet die Visualisierung.
     *
     * @param args  Die Kommandozeilen-Parameter, bestehend aus dem
     *              Servernamen, dem Port, der Spiel-ID, der Rundennummer und
     *              dem Namen des Verzeichnisses mit den Bildern.
     */
    public static void main(String args[]) {
        VisualisationClient     visClient;
        BienenstockVisSteuerung bienenVis;
        String                  servername, spielId, runde, verzeichnis;

        if (args.length == 4) {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }

            try {
                servername = args[0];
                verzeichnis = args[1];
                spielId = args[2];
                runde = args[3];
                visClient = new VisualisationClient(servername, spielId, runde);
                bienenVis = new BienenstockVisSteuerung(visClient, verzeichnis);
            } catch (MalformedURLException fehler) {
                System.err.println("Server-URL fehlerhaft!\n"
                        + fehler.getMessage());
            } catch (RemoteException fehler) {
                System.err.println("Verbindungsproblem!\n"
                        + fehler.getMessage());
            } catch (NotBoundException fehler) {
                System.err.println("Server-Vis-Komponente nicht gefunden!\n"
                        + fehler.getMessage());
            }
        } else {
            System.out.println("Bitte geben Sie Serveradresse, das Verzeichnis"
                               + " mit den Bildern, die Rundennummer und die"
                               + " Spiel-ID an.\n"
                    + "(z.B.: 127.0.0.1 config/bilder spiel 1)");
        }
    }
}

