/*
 * Dateiname      : IVisualisationClient.java
 * Erzeugt        : 26. Mai 2005
 * Letzte �nderung: 10. Juni 2005 durch Dietmar Lippold
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
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

/**
 * Definiert die Schnittstelle der Visualisierung auf Client-Seite zum
 * Simulator.
 *
 * @author  Samuel Walz
 * @author  Dietmar Lippold
 */
public interface IVisualisationClient {

    /**
     * Registriert eine Visualisierungskomponente beim Spiel.
     *
     * @param ausgabe  Die Komponente, die die zu visualisierenden Daten
     *                 ausgibt.
     */
    public void anmeldung(IVisualisationOutput ausgabe);

    /**
     * Startet die Abfrage und �bergabe der zu visualisierenden Daten.
     */
    public void start();

    /**
     * Beendet die Abfrage und �bergabe der zu visualisierenden Daten.
     */
    public void start();
}

