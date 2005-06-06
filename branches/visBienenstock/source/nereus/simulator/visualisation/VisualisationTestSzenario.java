/*
 * Dateiname      : VisualisationTestSzenario.java
 * Erzeugt        : 22. Mai 2005
 * Letzte Änderung: 06. Juni 2005 durch Samuel Walz
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


package nereus.simulator.visualisation;

import java.rmi.RemoteException;

/**
 *
 * @author  Samuel Walz
 */
public class VisualisationTestSzenario {

    private static long authCode = 0L;

    /** Creates a new instance of VisualisationTestSzenario */
    public VisualisationTestSzenario() {
        try {
            VisualisationServer unserServer = new VisualisationServer();
            System.out.println("Melde Spiel an...");
            authCode = unserServer.spielAnmelden(23, 3000);
            int i = 0;
            System.out.println("Speichere Spielinformationen...");
            for(i=0; i<=5; i++) {
                synchronized (this) {
                    wait(10000);
                }
                unserServer.speichereSpielInformationen(authCode, "hammer No." + i);
            }
            System.out.println("Melde Spiel ab...");
            unserServer.spielAbmelden(authCode);

            System.out.println("Warte...");
                synchronized(this) {
                    this.wait(120000);
                }

        } catch (DoppeltesSpielException fehler) {
            System.out.println("Spiel mit gleicher ID schon angemeldet!\n" 
                                + fehler.getMessage());
        } catch (RemoteException fehler) {
            System.out.println("Konnte Server nicht finden!\n" 
                                + fehler.getMessage());
        } catch (InterruptedException fehler) {
            System.out.println("Warten fehlgeschlagen!!\n" 
                                + fehler.getMessage());
        }
    }


    public static void main(String args[]) {
        VisualisationTestSzenario testSzenario = new VisualisationTestSzenario();
    }
}

