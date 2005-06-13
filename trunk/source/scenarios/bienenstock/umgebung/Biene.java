/*
 * Dateiname      : Biene.java
 * Erzeugt        : 19. Mai 2004
 * Letzte Änderung: 4. Juni 2005 durch Eugen Volk
 *
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

import scenarios.bienenstock.scenariomanagement.Konstanten;
import scenarios.bienenstock.agenteninfo.Koordinate;
import scenarios.bienenstock.agenteninfo.Info;
import nereus.utils.Id;

/**
 * ist das Abbild des Agenten im Szenario.
 *
 * Dieses Abbild wird verwendet um die Werte der Agenten für
 * etwaige Überprüfungen bereit zu halten.
 * Dadurch werden beeinflussungen der Werte durch den jeweiligen
 * Agenten ausgeschlossen.
 *
 * @author Samuel Walz
 * @author Philip Funck
 */
public class Biene {
    
    /**
     * beschreibt, ob die Biene am Boden ist oder nicht.
     */
    private boolean amBoden;
    
    /**
     * beschreibt die aktuell ausgeführte Aktion.
     *
     * Die Aktionen werden hierfür durchnummeriert und mit dieser Nummer
     * eindeutig gekennzeichnet.
     */
    private int listenKennung;
    
    /**
     * ist eine Nummer, die die Biene eindeutig kennzeichnet.
     * Ist identisch mit der Agent-ID die die Agenten im Simulator haben.
     */
    private int bienenID;
    
    /**
     * ist eine Nummer, die die Zugehörigkeit
     * einer Biene zu einem Bienenvolk eindeutig kennzeichnet.
     */
    private int bienenvolkID;
    
    /**
     * Position der Biene auf der Spielkarte.
     */
    private Koordinate position;
    
    /**
     * ist der Code, der vom Agenten benötigt wird um eine Aktion auszuführen.
     */
    private long aktionsCode;
    
    /**
     * Menge des aktuell geladenen Honigs.
     *
     */
    private int geladeneHonigmenge;
    
    /**
     * Menge des aktuell geladenen Nektars.
     */
    private int geladeneNektarmenge;
    
    /**
     * zuletzt per Tanz mitgeteilte Information der Biene.
     */
    private Info information;
    
    /** zuletzt per Tanz gesendete Information */
    private Info gesendeteInformation;
    
    /**
     * Die ID, unter der die Biene im Simulator verwaltet wird.
     */
    private Id simulatorId;
    
    /**
     * instanziiert eine neue Biene.
     *
     * Diese befindet sich am Boden, im Zustand <code>WARTEND</code>
     * und bekommt die Startwerte an Honig und Nektar,
     * sowie ihre maximal Beladungen.
     *
     * @param eigenID        ist die Identifikationsnummer der Biene,
     *                       die vom Simulator bestimmt wird.
     * @param volkID         ist die Identifikationsnummer des Bienenvolkes,
     *                       dem die Biene angehört,
     *                       die vom Simulator gestellt wird.
     * @param bienePosition  bestimmt die Position an der die Bienen entsteht.
     * @param startHonig die Menge an Honig, die die Biene bei er Geburt hat
     * @param startNektar die Menge an Nektar, di die Biene zu Begin hat
     * @param simId die ID im simulator
     */
    public Biene(int eigenID,
            int volkID,
            Koordinate bienePosition,
            int startHonig,
            int startNektar,
            Id simId) {
        
        amBoden = true;
        listenKennung = Konstanten.WARTEND;
        bienenID = eigenID;
        bienenvolkID = volkID;
        position = bienePosition;
        geladeneHonigmenge = startHonig;
        geladeneNektarmenge = startNektar;
        simulatorId = simId;
    }
    
    /**
     * gibt zurück, ob sich eine Biene am Boden befindet.
     *
     * @return boolean    wenn der wert <code>true</code> ist,
     *                    befindet sie sich am Boden.
     */
    boolean gibAmBoden() {
        return amBoden;
    }
    
    /**
     * gibt die Kennung der Liste, in der sie sich befindet.
     *
     * @return integer    eine eindeutige Nummer, die bezeichnet,
     *                    in welcher Liste sich die Biene befindet.
     */
    int gibListenKennung() {
        return listenKennung;
    }
    
    /**
     * gibt den Aktionscode der Biene zurück.
     *
     * @return long    ein eindeutiger Code,
     *                 der sie für eine Aktion authentifiziert.
     */
    long gibAktionsCode() {
        return aktionsCode;
    }
    
    /**
     * gibt die ID des Bienenvolks zurück, dem die Biene angehört.
     *
     * @return int    ID des Bienenvolkes.
     */
    int gibBienenvolkID() {
        return bienenvolkID;
    }
    
    /**
     * gibt die ID der Biene zurück.
     *
     * @return int    ID der Biene.
     */
    int gibBienenID() {
        return bienenID;
    }
    
    /**
     * gibt die aktuelle Position der Biene zurück.
     *
     * @return Koordinate    Position der Biene,
     *                       in Form einer <code>Koordinate</code>.
     */
    Koordinate gibPosition() {
        return position;
    }
    
    /**
     * gibt die geladene Nektarmenge der Biene zurück.
     *
     * @return int    Menge des mitgeführten Nektars.
     */
    int gibGeladeneNektarmenge() {
        return geladeneNektarmenge;
    }
    
    /**
     * gibt die geladene Honigmenge der Biene zurück.
     *
     * @return int    Menge des mitgeführten Honigs
     */
    int gibGeladeneHonigmenge() {
        return geladeneHonigmenge;
    }
    
    /**
     * gibt die zuletzt per Tanz mitgeteilte Information zurück.
     *
     * @return Info
     */
    Info gibInformation() {
        return information;
    }
    
    /**
     * gibt die zuletzt per Tanz mitzugeteilende Information zurück.
     *
     * @return Info
     */
    Info gibGesendeteInformation() {
        return this.gesendeteInformation;
    }
    
    /**
     * gibt die Id innerhalb des Simulators zurück
     *
     * @return simId Id innerhalb des Simulators
     */
    Id gibSimId() {
        return simulatorId;
    }
    
    /**
     * setzt die Attribute listenKennung und amBoden auf den Wert, der
     * durch <code>liste</code> bestimmt wird.
     *
     * <code<liste</code> ist gleich der neu zu setzenden listenKennung und
     * wird für die bestimmung des Parameters amBoden interpretiert.
     * Der Parameter <code>liste</code> darf dabei nur im Rahmen
     * der definierten Konstanten für Zustände bleiben und wird nichtmehr
     * validiert.
     *
     * @param liste    der Zustand der Biene, kodiert durch die
     *                 <code>Konstanten</code>.
     */
    void setzeZustand(int liste) {
        if (liste == Konstanten.ZUSCHAUEND) {
            listenKennung = Konstanten.WARTEND;
        } else {
            listenKennung = liste;
        }
        if (liste == Konstanten.FLIEGEND) {
            amBoden = false;
        } else {
            amBoden = true;
        }
    }
    
    /**
     * setzt den Aktionscode auf <code>aktCode</code>.
     *
     * @param aktCode    der neue Aktionscode.
     */
    void setzeAktionsCode(long aktCode) {
        aktionsCode = aktCode;
    }
    
    /**
     * setzt die ID der Biene auf <code>eigenID</code>.
     *
     * @param eigenID    die neue ID der Biene.
     */
    void setzeBienenID(int eigenID) {
        bienenID = eigenID;
    }
    
    /**
     * setzt die bienenvolkID auf <code>volkID</code>.
     *
     * @param volkID    die neue ID des Bienenvolkes.
     */
    void setzeBienenvolkID(int volkID) {
        bienenvolkID = volkID;
    }
    
    /**
     * setzt die aktuelle Position der Biene auf <code>bienePosition</code>.
     *
     * @param bienePosition    die neue Position der Biene.
     */
    void setzePosition(Koordinate bienePosition) {
        position = bienePosition;
    }
    
    /**
     * setzt die Menge an geladenem Nektar auf <code>nektarmenge</code>.
     *
     * @param nektarmenge    die neue geladene Nektarmenge.
     */
    void setzeGeladeneNektarmenge(int nektarmenge) {
        geladeneNektarmenge = nektarmenge;
    }
    
    /**
     * setzt die geladeneHonigmenge auf <code>honigmenge</code>.
     *
     * @param honigmenge    die neue Honigmenge.
     */
    void setzeGeladeneHonigmenge(int honigmenge) {
        geladeneHonigmenge = honigmenge;
    }
    
    /**
     * setzt eine neue per Tanz mitgeteilte Information.
     *
     * @param infoObjekt das zu setzende InfoObject
     */
    void setzeInformation(Info infoObjekt) {
        information = infoObjekt;
    }
    
    /**
     * setzt eine neue zu sendende bzw zu mitteilende Information.
     *
     * @param infoObjekt das zu setzende InfoObject
     */
    void setzeGesendeteInformation(Info infoObjekt){
        this.gesendeteInformation=infoObjekt;
    }
}

