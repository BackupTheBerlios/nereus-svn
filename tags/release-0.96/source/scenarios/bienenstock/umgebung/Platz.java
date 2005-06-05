/*
 * Dateiname      : Platz.java
 * Erzeugt        : 19. Mai 2004
 * Letzte Änderung: 26. Januar 2005 durch Philip Funck
 * Autoren        : Philip Funck (mango.3@gmx.de)
 *                  Samuel Walz (felix-kinkowski@gmx.net)
 *
 *
 *
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen eines
 * Software-Praktikums von Philip Funck und Samuel Walz am Institut für
 * Intelligente Systeme der Universität Stuttgart unter Betreuung von
 * Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de).
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
package scenarios.bienenstock.umgebung;

import scenarios.bienenstock.agenteninfo.Koordinate;

/**
 * repräsentiert einen Platz.
 *
 * @author Philip Funck
 * @author Samuel Walz
 */
public class Platz extends Feld {
    
    /**
     * Konstruktor.
     *
     * @param eigenID   die ID
     * @param sichtBoden    Sichtweite am Boden
     * @param sichtLuft     Sichtweite in der Luft
     * @param maxWartendeBienen maximum wartende Bienen
     * @param maxFliegendeBienen    maximum fliegende Bienen
     * @param maxTanzendeBienen     maximum tanzende Bienen
     * @param feldPosition  Koordinate des PLatzes
     */
    public Platz(
            int eigenID,
            int sichtBoden,
            int sichtLuft,
            int maxWartendeBienen,
            int maxFliegendeBienen,
            int maxTanzendeBienen,
            Koordinate feldPosition) {
        super(eigenID,
                sichtBoden,
                sichtLuft,
                maxWartendeBienen,
                maxFliegendeBienen,
                maxTanzendeBienen,
                feldPosition);
    }
    
}
