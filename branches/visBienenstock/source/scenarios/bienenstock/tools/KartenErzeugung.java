/*
 * Dateiname      : KartenErzeugung.java
 * Erzeugt        : 16. Juni 2005
 * Letzte Änderung: 16. Juni 2005 durch Dietmar Lippold
 * Autoren        : Dietmar Lippold
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
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


package scenarios.bienenstock.tools;

import java.util.Arrays;

/**
 * Erzeugt eine Karte im gml-Format für das Bienenstock-Szenario. Diese
 * enthält nur Plätze und leere Felder.
 *
 * @author  Dietmar Lippold
 */
public class KartenErzeugung {

    /**
     * Die Anzahl der Felder in der Breite.
     */
    private static final int BREITE = 23;

    /**
     * Die Anzahl der Felder in der Höhe.
     */
    private static final int HOEHE = 15;

    /**
     * Der Name der Karte.
     */
    private static final String NAME = "\"Garten " + BREITE + "x" + HOEHE + "\"";

    /**
     * Die Kapazität für wartende Bienen.
     */
    private static final int KAP_WARTEND = -1;

    /**
     * Die Kapazität für fliegende Bienen.
     */
    private static final int KAP_FLIEGEND = -1;

    /**
     * Die Kapazität für tanzende Bienen.
     */
    private static final int KAP_TANZEND = 3;

    /**
     * Die Kapazität für abbauende Bienen auf einer Blume.
     */
    private static final int KAP_ABBAUEND = 1;

    /**
     * Die Sichtweite am Boden.
     */
    private static final int SICHT_BODEN = 1;

    /**
     * Die Sichtweite in der Luft;
     */
    private static final int SICHT_LUFT = 2;

    /**
     * Die Nummer des Volkes des Bienenstocks.
     */
    private static final int VOLKS_ID = 1;

    /**
     * Die Anfangsmenge des Nektars im Bienenstock.
     */
    private static final int STOCK_NEKTAR = 0;

    /**
     * Die maximale Menge des Nektars im Bienenstock.
     */
    private static final int MAX_NEKTAR = 10000;

    /**
     * Die Anfangsmenge des Honigs im Bienenstock.
     */
    private static final int STOCK_HONIG = 200;

    /**
     * Die maximale Menge des Honigs im Bienenstock.
     */
    private static final int MAX_HONIG = 10000;

    /**
     * Die Menge des Nektars, die pro Runde zu Honig verarbeitet werden kann.
     */
    private static final int NEKTAR_ZU_HONIG = 20;

    /**
     * Die Anfangsmenge des Nektars einer Blume.
     */
    private static final int BLUME_NEKTAR = 300;

    /**
     * Die Menge des Nektars, die pro Runde von einer Biene abgebaut werden
     * kann.
     */
    private static final int NEKTAR_PRO_RUNDE = 25;

    /**
     * Das Merkmal einer Blume.
     */
    private static final int BLUME_MERKMAL = 0;

    /**
     * Die ID des Feldes des Bienenstocks.
     */
    private static final int STOCK_FELD = 163;

    /**
     * Die IDs von Felder, auf denen sich Blumen befinden. Die Felder müssen
     * aufsteigend geordnet sein.
     */
    private static final int[] BLUMEN_FELDER = new int[] {100, 150, 170, 200};

    /**
     * Die IDs von Felder, die leer sind. Diese müssen aufsteigend geordnet
     * sein.
     */
    private static final int[] LEERE_FELDER = new int[] {60, 61, 62, 260};

    /**
     * Gibt an, ob Kanten zwischen diagonal zueinander gelegenen Feldern
     * vorhanden sein sollen.
     */
    private static final boolean DIAGONAL_KANTEN = false;

    /**
     * Gibt den Header der Datei aus.VOLKS_ID
     */
    private static void printHeader() {
        System.out.println("graph [");
        System.out.println("    label " + NAME);
        System.out.println("    directed  0");
        System.out.println();
    }

    /**
     * Gibt den Bienenstock aus.
     *
     * @param id  Die ID des Feldes.
     * @param x   Die x-Koordinate des Feldes.
     * @param y   Die y-Koordinate des Feldes.
     */
    private static void printStock(int id, int x, int y) {
        System.out.println("    node [");
        System.out.println("        id " + id);
        System.out.println("        stock [");
        System.out.println("            volksID " + VOLKS_ID);
        System.out.println("            nektar " + STOCK_NEKTAR);
        System.out.println("            nektarMax " + MAX_NEKTAR);
        System.out.println("            honig " + STOCK_HONIG);
        System.out.println("            honigMax " + MAX_HONIG);
        System.out.println("            nektarZuHonig " + NEKTAR_ZU_HONIG);
        System.out.println("        ]");
        System.out.println("        kapazitaet [");
        System.out.println("            wartend " + KAP_WARTEND);
        System.out.println("            fliegend " + KAP_FLIEGEND);
        System.out.println("            tanzend " + KAP_TANZEND);
        System.out.println("        ]");
        System.out.println("        sichtweite [");
        System.out.println("            boden " + SICHT_BODEN);
        System.out.println("            luft " + SICHT_LUFT);
        System.out.println("        ]");
        System.out.println("        koordinate [");
        System.out.println("            x " + x);
        System.out.println("            y " + y);
        System.out.println("        ]");
        System.out.println("    ]");
        System.out.println();
    }

    /**
     * Gibt eine Blume aus.
     *
     * @param id  Die ID des Feldes.
     * @param x   Die x-Koordinate des Feldes.
     * @param y   Die y-Koordinate des Feldes.
     */
    private static void printBlume(int id, int x, int y) {
        System.out.println("    node [");
        System.out.println("        id " + id);
        System.out.println("        blume [");
        System.out.println("            merkmal " + BLUME_MERKMAL);
        System.out.println("            nektar " + BLUME_NEKTAR);
        System.out.println("            nektarProRunde " + NEKTAR_PRO_RUNDE);
        System.out.println("        ]");
        System.out.println("        kapazitaet [");
        System.out.println("            wartend " + KAP_WARTEND);
        System.out.println("            fliegend " + KAP_FLIEGEND);
        System.out.println("            tanzend " + KAP_TANZEND);
        System.out.println("            abbauend " + KAP_ABBAUEND);
        System.out.println("        ]");
        System.out.println("        sichtweite [");
        System.out.println("            boden " + SICHT_BODEN);
        System.out.println("            luft " + SICHT_LUFT);
        System.out.println("        ]");
        System.out.println("        koordinate [");
        System.out.println("            x " + x);
        System.out.println("            y " + y);
        System.out.println("        ]");
        System.out.println("    ]");
        System.out.println();
    }

    /**
     * Gibt einen Platz aus.
     *
     * @param id  Die ID des Feldes.
     * @param x   Die x-Koordinate des Feldes.
     * @param y   Die y-Koordinate des Feldes.
     */
    private static void printPlatz(int id, int x, int y) {
        System.out.println("    node [");
        System.out.println("        id " + id);
        System.out.println("        platz []");
        System.out.println("        kapazitaet [");
        System.out.println("            wartend " + KAP_WARTEND);
        System.out.println("            fliegend " + KAP_FLIEGEND);
        System.out.println("            tanzend " + KAP_TANZEND);
        System.out.println("        ]");
        System.out.println("        sichtweite [");
        System.out.println("            boden " + SICHT_BODEN);
        System.out.println("            luft " + SICHT_LUFT);
        System.out.println("        ]");
        System.out.println("        koordinate [");
        System.out.println("            x " + x);
        System.out.println("            y " + y);
        System.out.println("        ]");
        System.out.println("    ]");
        System.out.println();
    }

    /**
     * Gibt einen Weg zwischen zwei Feldern aus.
     *
     * @param source  Die ID des einen Feldes.
     * @param target  Die ID des anderen Feldes.
     */
    private static void printWeg(int source, int target) {
        System.out.println("    edge [source " + source
                           + " target " + target + "]");
    }

    /**
     * Gibt den Footer der Datei aus.
     */
    private static void printFooter() {
        System.out.println("]");
        System.out.println();
    }

    /**
     * Ermittelt, ob das Feld mit der angegebenen Id vorhanden ist.
     *
     * @param id  Die ID des Feldes.
     *
     * @return  <CODE>true</CODE> genau dann, wenn das Feld mit der
     *          angegebenen Id vorhanden ist, anderenfalls
     *          <CODE>false</CODE>.
     */
    private static boolean istVorhanden(int id) {
        return (Arrays.binarySearch(LEERE_FELDER, id) < 0);
    }

    /**
     * Ermittelt, ob das Feld mit der angegebenen Id eine Blume ist.
     *
     * @param id  Die ID des Feldes.
     *
     * @return  <CODE>true</CODE> genau dann, wenn das Feld mit der
     *          angegebenen Id eine Blume ist, anderenfalls
     *          <CODE>false</CODE>.
     */
    private static boolean istBlume(int id) {
        return (Arrays.binarySearch(BLUMEN_FELDER, id) >= 0);
    }

    /**
     * Ermittelt, ob das Feld mit der angegebenen Id der Bienenstock ist.
     *
     * @param id  Die ID des Feldes.
     *
     * @return  <CODE>true</CODE> genau dann, wenn das Feld mit der
     *          angegebenen Id der Bienenstock ist, anderenfalls
     *          <CODE>false</CODE>.
     */
    private static boolean istStock(int id) {
        return (id == STOCK_FELD);
    }

    /**
     * Gibt einen kompletten GML-Graphen aus.
     */
    private static void printGraph() {
        int id, sourceId, targetId;

        printHeader();

        // Ausgabe der Knoten.
        for (int y = 1; y <= HOEHE; y++) {
            for (int x = 1; x <= BREITE; x++) {
                id = x + (y - 1) * BREITE;
                if (istVorhanden(id)) {
                    // Das Feld ist kein leeres Feld.
                    if (istStock(id)) {
                        printStock(id, x, y);
                    } else if (istBlume(id)) {
                        printBlume(id, x, y);
                    } else {
                        printPlatz(id, x, y);
                    }
                }
            }
        }

        // Ausgabe der Kanten.
        for (int y = 1; y <= HOEHE; y++) {
            for (int x = 1; x <= BREITE; x++) {
                sourceId = x + (y - 1) * BREITE;

                for (int dy = 0; dy <= 1; dy++) {
                    for (int dx = 0; dx <= 1; dx++) {
                        if ((Math.abs(dy) + Math.abs(dx) == 1)
                            || DIAGONAL_KANTEN && (Math.abs(dy) + Math.abs(dx) == 2)) {
                            if ((y + dy >= 1) && (y + dy <= HOEHE)
                                && (x + dx >= 1) && (x + dx <= BREITE)) {

                                targetId = (x + dx) + (y + dy - 1) * BREITE;
                                if (istVorhanden(sourceId)
                                    && istVorhanden(targetId)) {

                                    printWeg(sourceId, targetId);
                                }
                            }
                        }
                    }
                }
            }
        }

        printFooter();
    }

    /**
     * Die Hauptprozedur.
     *
     * @param args  Das Array der Kommandozeilenargumente ohne Inhalt.
     */
    public static void main(String[] args) {
        printGraph();
    }
}

