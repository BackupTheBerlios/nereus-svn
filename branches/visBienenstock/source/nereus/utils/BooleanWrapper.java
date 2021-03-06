/*
 * Dateiname      : BooleanWrapper.java
 * Erzeugt        : 21. Juni 2005
 * Letzte �nderung: 21 Juni 2005 durch Eugen Volk
 * Autoren        :  Eugen Volk
 *
 *
 *
 * Diese Datei geh�rt zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Eugen Volk am Institut f�r Intelligente Systeme
 * der Universit�t Stuttgart unter Betreuung von Dietmar Lippold
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


package nereus.utils;

/**
 * Eine Klasse als Wrapper f�r boolean
 * 
 */
public class BooleanWrapper {
   
     /**
     * boolean Wert
     */
    boolean wert;
    
    /** Creates a new instance of BooleanWrapper */
    public BooleanWrapper() {
    }
    
    /** 
     * Wrapper um den Boolean-Wert. 
     * @param value boolean-Wert
     */
    public BooleanWrapper(boolean value) {
        this.wert=value;
    }
    
       
    /**
     * setzt den boolean Wert.
     * @param value boolean-Wert
     */
    public void setValue(boolean value){
        this.wert=value;
    }
    
    /**
     * gibt den boolean Wert
     * @return boolean-Wert
     */
    public boolean getValue(){
        return this.wert;
    }
}
