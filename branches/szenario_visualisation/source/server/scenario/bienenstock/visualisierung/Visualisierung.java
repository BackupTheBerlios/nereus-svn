/*
 * Created on May 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package scenario.bienenstock.visualisierung;
import java.awt.*;
import scenario.bienenstock.visualisierungsUmgebung.*;
import java.util.LinkedList;

/**
 * @author philip
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Visualisierung {
    
    private Bienenstockvisualisierung fenster;
    private LinkedList karten;
    private boolean initiiert = false;
    private int naechste = 0;
    private long zeit = 1000L;
    
    public Visualisierung () {
        fenster = new Bienenstockvisualisierung(this);
        fenster.show();
        karten = new LinkedList();
    }
    
    public void visualisiere(VisKarte karte) {
        karten.addLast(karte);
        System.out.println("karten.size() = " + karten.size());
        if (naechste < karten.size()) {
            try {
                synchronized (this) {
                    this.wait(zeit);
                }
            } catch (InterruptedException e) {
                System.out.println("wurde unterbrochen");
            }
            fenster.visualisiere((VisKarte)karten.get(naechste));
            naechste = naechste + 1;
        }
    }
}
