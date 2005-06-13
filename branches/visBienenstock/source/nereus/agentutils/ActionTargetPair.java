/*
 * Dateiname      : ActionTargetPair.java
 * Erzeugt        : 31. Mai 2005
 * Letzte Änderung: 31. Mai 2005 durch Eugen Volk
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
package nereus.agentutils;

/**
 * Ist eine Klasse die ActionNr und ZielObjekt als ein Wert-Paar kapselt.
 * 
 */
public class ActionTargetPair {
    /** AktionsNummer, kodiert durch  Konstanten */
    private int action;
    /** Nummer des Zielagenten */
    private int zielAgentId;
    /** ZielObjekt */
    private Object target;
    
    /**
     * Erzeugt ein Instanz von ActionTargetPair, die als Datenstruktur zur Koppelung
     * von Aktion und Ziel dient.
     */
    public ActionTargetPair() {
    }
    /**
     * Erzeugt ein Instanz von ActionTargetPair, die als Datenstruktur zur Koppelung
     * von Aktion und Ziel dient.
     * @param action Aktion, die über int Konstanten kodiert ist.
     * @param target ZielObjekt
     */
    public ActionTargetPair(int action, Object target){
        this.action=action;
        this.target=target;
    }
    
    /**
     * Erzeugt ein Instanz von ActionTargetPair, die als Datenstruktur zur Koppelung
     * von Aktion und Ziel dient.
     * @param action Aktion, die über int Konstanten kodiert ist.
     * @param zielAgentId Id des zielAgenten
     */
    public ActionTargetPair(int action, int zielAgentId){
        this.action=action;
        this.zielAgentId=zielAgentId;
    }
    
    /**
     * setzt die Aktion.
     * @param action Aktion, die über int Konstanten kodiert ist.
     */
    public void setAction(int action){
        this.action=action;
    }
    
    /**
     * setzt den ZielObjekt.
     * @param target ZielObjekt.
     */
    public void setTarget(Object target){
        this.target=target;
    }
    
    /**
     * liefert die Aktion, die über int Konstanten kodiert wurde.
     * @return Aktion (als int-Wert)
     */
    public int getAction(){
        return this.action;
    }
    
    /**
     * liefert das ZielObjekt.
     * @return ZielObjekt.
     */
    public Object getTarget(){
        return this.target;
    }
    
    /**
     * liefert den Id des Zielagenten
     * @return Id des Zeilagenten
     */
    public int getZielAgentId(){
       return this.zielAgentId;
    }
    
    /**
     * setzt den Id des ZielAgenten
     */
    public void setZielAgtenId(int zielAgentId){
        this.zielAgentId=zielAgentId;
    }
}

