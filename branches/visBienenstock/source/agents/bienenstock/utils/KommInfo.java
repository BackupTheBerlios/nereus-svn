/*
 * Dateiname      : KommInfo.java
 * Erzeugt        : 21. Juli 2005
 * Letzte Änderung: 21. Juli 2005 durch Eugen Volk
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
 * Eine Datenstruktur zur Beschreibung einer empfangenen Nachricht.
 */
public class KommInfo implements Comparable{
    /** Koordinate der mitgeteilten Blume */
    private Koordinate koordinate;
    /** Nutzen der mitgeteilten Blume */
    private double nutzen;
    /** Rundennummer, in der die Nachricht empfangen wurde */
    private int rundeNr;
    
    
    /**
     * Klasse zur Speicherung der in einer Nachricht enthaltenen Informationen.
     *
     * @param koordinate Koordinate
     * @param nutzen Nutzen der Blume
     * @param rundeNr Rundennummer, in der die Nachricht gesendet wurde
     */
    public KommInfo(Koordinate koordinate, double nutzen, int rundeNr){
        this.koordinate=koordinate;
        this.nutzen=nutzen;
        this.rundeNr=rundeNr;
    }
    
    /**
     * liefert die Koordinate in der enthaltenen Nachricht.
     */
    public Koordinate getKoordinate(){
        return koordinate;
    }
    
    
    
    /**
     * liefert den Nutzen der der Blume
     * @return Nutzen der Blume
     */
    public double getNutzen(){
        return nutzen;
    }
    
    /**
     * liefert die RundenNr, in der die Eintragung vorgenommen wurde
     * @return Rundennummer
     */
    public int getRundeNr(){
        return rundeNr;
    }
    
    /**
     * setzt die Koordinate
     * @param koord Koordinate
     */
    public void setKoordinate(Koordinate koord){
        this.koordinate=koord.copy();
    }
    
    
    /**
     * liefert den Vergleich zwischen den Objekten
     *
     * @param obj Vergleichsobjekt
     * @return true,falls beide Objete die gleiche Koordinate haben.
     */
    public boolean equals(Object obj){
        Koordinate objKoord=((KommInfo)obj).getKoordinate();
        return objKoord.equals(koordinate);
    }
    
    
    /**
     * generiert einen Hashcode für das Objekt.
     *
     * @return   X-Koordinate * 1000 + Y-Koordinate
     */
    public int hashCode() {
        return ((koordinate.gibXPosition() * 1000) + koordinate.gibYPosition());
    }
    
    
    
    /**
     * Vergleichsoperator, der Nutzen der KommInfo-Objekte vergleicht
     * @param obj Vergleichsobjekt
     * @return <PRE>
     * 1, falls beide Objekte gleich sind <br>
     * -1, falls dieser Objekt > als der Vergleichsobjekt ist <br>
     * 1, falls dieser Objekt < als der Vergleichsobjekt ist.
     * </PRE>
     */
    public int compareTo(Object obj){
        Double myDouble=new Double(this.nutzen);
        double anotherWert=((KommInfo)obj).getNutzen();
        Double anotherDouble=new Double(anotherWert);
        if (myDouble.equals(anotherDouble)) return 1;
        else  return (int)((-1)* myDouble.compareTo(anotherDouble));
    }
    
    
}
