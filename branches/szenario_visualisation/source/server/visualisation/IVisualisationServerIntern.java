/*
 * Dateiname      : IVisualisationServer.java
 * Erzeugt        : 18. Mai 2005
 * Letzte Änderung: 19. Mai 1005 durch Samuel Walz
 * Autoren        : Samuel Walz (samuel@gmx.info)
 *                  
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
package source.server.visualisation;

/**
 * Speichert die Informationen aller Spiele die für eine Visualisierung
 * notwendig sein könnten und gibt sie auf Anfrage an interessierte
 * Clients weiter.
 * 
 * Die ankommenden Informationen der laufenden Spiele werden
 * sortiert, nach der ID des zugehörigen Spieles und der Reihenfolge
 * in der sie von einem Spiel angeliefert werden, abgespeichert.
 * Hierzu werden die Informationen eines Spiels mit der Zahl Null 
 * beginnend und aufsteigend durchnummeriert.
 * Ein interessierter Client gibt dann an, an welchen Informationen
 * er Interesse hat, indem er die ID des Spieles und den Ausschnittsbeginn
 * angibt. Er bekommt dann in einer Liste alle Informationen zurück,
 * deren Nummer größer oder gleich dem gewünschten Ausschnittsbeginn ist.
 * 
 * @author  Samuel Walz
 */
public interface IVisualisationServerIntern {
    
    /**
     * Speichert die Informationen eines Spiels.
     */
    void speichereSpielInformationen (java.lang.Object information);
    
}
