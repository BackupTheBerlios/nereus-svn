/* Generated by Together */

package scenario.bienenstock.einfacheUmgebung;

import scenario.bienenstock.umgebung.Biene;
import java.util.ArrayList;
import java.util.HashSet;
import scenario.bienenstock.Koordinate;

public class EinfacheBlume extends EinfachesFeld {

    private int merkmal;

    /**
     * @associates java.lang.Integer
     */
    private HashSet abbauendeBienen;

    public EinfacheBlume(Koordinate pos,
        				 HashSet wBienen,
                         HashSet fBienen,
                         HashSet tBienen,
                         int merk,
                         HashSet aBienen) {
        super(pos,
              wBienen,
              fBienen,
              tBienen);
        /*position = pos;
        wartendeBienen = wBienen;
        fliegendeBienen = fBienen;
        tanzendeBienen = tBienen;*/
        merkmal = merk;
        abbauendeBienen = aBienen;
    }

    public int gibMerkmal() {
        return merkmal;
    }

    /**
     * @associates java.lang.Integer
     */
    public HashSet gibIDsAbbauendeBienen() {
		return (HashSet)abbauendeBienen.clone();
    }


}
