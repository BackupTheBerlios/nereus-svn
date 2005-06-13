/*
 * Dateiname      : KoordEntfernung.java
 * Erzeugt        : 11. Juni 2005
 * Letzte Änderung: 11. Juni 2005 durch Eugen Volk
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


package agents.bienenstock.utils;
import scenarios.bienenstock.agenteninfo.Koordinate;
/**
 * Eine Datenstruktur, die Koordinate und einen Wert enthält; wird verwendet
 * um die Koordinaten nach ihrem Wert zu sortieren.
 */
public class KoordWertPaar implements Comparable{
    
    
    /** Koordinate */
    private Koordinate koordinate;
    /** Wert */
    private double wert;
    
    
    /**
     * Eine Datenstruktur, die Koordinate und ihren Wert enthält.
     * @param koordinate zu speichernde Koordinate
     * @param wert zu speichernder Wert
     */
    public KoordWertPaar(Koordinate koordinate, double wert){
        this.koordinate=koordinate;
        this.wert=wert;
    }
    
    /**
     * Gibt die gespeicherte Koordinate zurück.
     * @return gespeicherte Koordinate
     */
    public Koordinate getKoordinate(){
        return this.koordinate;
    }
    /**
     * Gibt die gespeicherte Wert zurück.
     * @return gespeicherte Wert
     */
    public double getWert(){
        return this.wert;
    }
    
    /**
     * Vergleichsoperator.
     * @param obj Vergleichsobjekt
     * @return <PRE>
     * -1, falls beide Objekte gleich sind <br>
     * 1, falls dieser Objekt > als der Vergleichsobjekt ist <br>
     * -1, falls dieser Objekt < als der Vergleichsobjekt ist.
     * </PRE>
     */
    public int compareTo(Object obj){
        Double myDouble=new Double(wert);
        double anotherWert=((KoordWertPaar)obj).getWert();
        Double anotherDouble=new Double(anotherWert);
        if (myDouble.equals(anotherDouble)) return -1;
        else  return myDouble.compareTo(anotherDouble);
    }
    
    /**
     * Vergleichsoperator
     * @param obj zu vergleichender Objekt
     * @return true, falls beide Objekte gleiche Koordinate habe;
     *         false sonst.
     */
    public boolean equals(Object obj){
        return this.koordinate.equals(obj);
    }
    
}
