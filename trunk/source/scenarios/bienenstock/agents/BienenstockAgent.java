/*
 * Erzeugt        : 20. Januar 2004
 * Letzte Änderung: 25. Januar 2005 durch Samuel Walz
 *
 * Teil des Softwarepraktikums mit dem Titel
 *
 *   "Design und Implementierung eines Szenarios für einen
 *    Multiagenten-Simulator"
 *
 * bei Dietmar Lippold (dietmar.lippold@informatik.uni-stuttgart.de)
 *
 * Autoren  : Philip Funck (mango.3@gmx.de)
 *            Samuel Walz (felix-kinkowski@gmx.net)
 *
 * copyright: Institut für Intelligente Systeme, Universität Stuttgart (2004)
 *            http://www.iis.uni-stuttgart.de
 */
package scenario.bienenstock;
import scenario.bienenstock.einfacheUmgebung.*;
import simulator.AbstractScenarioHandler;
import scenario.bienenstock.AbstrakteBiene;
import scenario.bienenstock.BienenstockSzenarioHandler;
import scenario.bienenstock.Koordinate;
import scenario.bienenstock.Info;
import utils.Id;

import java.util.Iterator;
import java.util.Random;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * 
 * @author Philip Funck
 * @author Samuel Walz
 *
 * 
 */
public class BienenstockAgent
    extends AbstrakteBiene
    implements Runnable
{

	private int volksID;
    private long aktCode;
    private EinfacheKarte karte;
    private EinfacheBiene selbst;
    private BienenstockSzenarioHandler handler;
    private EinfachesFeld position;
    boolean erstenAktCodeBekommen = false;

    private int id = 0;
    private Koordinate posBienenstock = new Koordinate(100, 100);
    LinkedList weg = new LinkedList();
    HashMap blumen = new HashMap();
    //ein paar honigkosten
    private int honigStarten = 0;
    private int honigFliegen = 0;
    private int honigLanden = 0;
    private int honigAbbauen = 0;
    private int honigTanken = 0;
    private int honigAbliefern = 0;
    private String name = "";
    private int gesammelterNektar = 0;

    private TestAgentBlume zuBearbeitendeBlume;

    private boolean hinweg = true;

    /**
     * Zahl zur initialisierung des Zufallsgenerators. 
     */
    private long samen = System.currentTimeMillis();

    /**
     * Wird zur Wahl der initialen Flugrichtung benötigt. 
     */
    Random zufallsGenerator = new Random();

    public BienenstockAgent (String bName, AbstractScenarioHandler bHandler) {
    	super(bName, bHandler);
        name = bName;
        handler = (BienenstockSzenarioHandler)bHandler;
        volksID = 1;
        zufallsGenerator.setSeed(samen);
    }
    
    public BienenstockAgent () {
    	super();
        volksID = 1;
        zufallsGenerator.setSeed(samen);
    }
    
    public BienenstockAgent (Id bId,String bName) {
    	super(bId, bName);
    	name = bName;
    }
    
    public static boolean isRunableAgent() {
        return true;
    }

    public void setHandler(AbstractScenarioHandler bHandler) {
    	handler = (BienenstockSzenarioHandler)bHandler;
    }
    
    public boolean aktionscodeSetzen(long aktionsCode) {
        aktCode = aktionsCode;
        this.start();
        erstenAktCodeBekommen = true;
        return true;
    }



    public void run() {
        int i;
        //System.out.println("Agent " + id + " will Aktion starten");

        ausschnittHolen();
        posBienenstock = selbst.gibPosition();
        /*visualisiereBiene(selbst);
        starten();
        if (id == 12) {
            visualisiereFelder(karte);
            fliegen(2, 1);
            landen();
            for (i = 0; i < 10; i++) {
                abbauen(1000);
            }
        }
        if (id == 1) {
            fliegen(3, 1);
            landen();
            for (i = 0; i < 10; i++) {
                tanzen();
            }
        }
        if (id == 13) {
            fliegen(3, 1);
            landen();
            for(i = 0; i < 15; i++) {
                zuschauen();
            }
            starten();
        }*/


        /*while (true) {
            starten();
            visualisiereBiene(selbst);
            fliegeIntelligent();
            visualisiereBiene(selbst);
            visualisiereFelder(karte);
            landen();
            visualisiereBiene(selbst);
            aktionWaehlen();
            visualisiereBiene(selbst);
        }*/

       while (true) {
            starten();
            System.out.println(id + ": suche eine neue Blume.");
            while (!sucheBlume()) {}
            landen();
            if (blumeHatNektar()) {
                TestAgentBlume plume = new TestAgentBlume(weg, true);
                blumen.put(weg.getLast(), plume);
                zuBearbeitendeBlume = plume;
                vielAbbauen();
            }
            else {
                blumen.put(weg.getLast(), new TestAgentBlume(weg, false));
            }
            heimFliegen();
        }
    }

    private void amBienenstock() {
        if (selbst.gibGeladeneHonigmenge() < (honigTanken + honigAbliefern)) {
            tanken(1000);
        }
        if (selbst.gibGeladeneNektarmenge() > 0) {
            abliefern();
        }
        tanken(10000);
    }

    private void abliefernGehen() {
        System.out.println(id + ": kehre heim (nektar voll / honig leer)");
        visualisiereBiene(selbst);
        starten();
        hinweg = false;
        int i = 0;
        for (i = (weg.size() - 2); i >= 0; i--) {
            fliegen((Koordinate)weg.get(i));
        }
        fliegen(posBienenstock);
        landen();
        amBienenstock();
        starten();
        hinweg = true;
        for (i = 0; i < weg.size(); i++) {
            fliegen((Koordinate)weg.get(i));
        }
        landen();
    }

    private void heimFliegen() {
        System.out.println(id + ": kehre Heim (blume leer)");
        starten();
        hinweg = false;
        if (weg.size() > 0) {
            weg.removeLast();
        }
        while (weg.size() > 0) {
            fliegeIntelligent();
        }
        fliegeIntelligent();
        landen();
        amBienenstock();
    }

    private boolean weiterfliegen(int offset) {

        boolean weiter;
        if (!((honigFliegen == 0) | (honigLanden == 0) | (honigStarten == 0))) {
            weiter = (selbst.gibGeladeneHonigmenge() >
                ((weg.size() + 3)*honigFliegen + honigStarten + honigLanden
                    + honigTanken + offset));
            //System.out.println(id + ":     weiterfliegen = " + weiter);
            return weiter;
        }
        else {
            return true;
        }
    }

    private void vielAbbauen() {
        int alterNektar = 0;
        boolean nektarBekommen = true;
        while (zuBearbeitendeBlume.besitztHonig()) {
            while (nektarBekommen && weiterfliegen(honigAbbauen)) {
                alterNektar = selbst.gibGeladeneNektarmenge();
                abbauen(100000);
                nektarBekommen = !(alterNektar == selbst.gibGeladeneNektarmenge());
            }
            if ((!nektarBekommen) && (selbst.gibGeladeneNektarmenge() < 100)) {
                zuBearbeitendeBlume.hatKeinenHonigMehr();
                heimFliegen();
            }
            else {
                abliefernGehen();
            }
        }
    }

    private boolean blumeVoll(EinfachesFeld blume) {
        if (blumen.containsKey(blume.gibPosition())) {
           return ((TestAgentBlume)blumen.get(blume.gibPosition())).besitztHonig();
        }
        else {
            return true;
        }
    }

    private boolean sucheBlume() {
        //visualisiereFelder(karte);
        boolean blumeGefunden = false;
        Koordinate posBlume = new Koordinate(100, 100);;
        Iterator itNachbarn = position.gibNachbarfelder().values().iterator();
        while ((!blumeGefunden) && itNachbarn.hasNext()) {
            EinfachesFeld tmp = (EinfachesFeld)itNachbarn.next();
            if (tmp instanceof EinfacheBlume && blumeVoll(tmp)) {
                posBlume = tmp.gibPosition();
                blumeGefunden = true;
            }
        }
        if (blumeGefunden) {
            fliegen(posBlume);
            System.out.println(id + ": Habe eine Blume gefunden");
            visualisiereBiene(selbst);
            return true;
        }
        else {
            System.out.println(id + ": suche Blume");
            fliegeIntelligent();
            return false;
        }
    }

    private boolean blumeHatNektar() {
        int nektarVorher = selbst.gibGeladeneNektarmenge();
        abbauen(10000000);
        ausschnittHolen();
        return (nektarVorher < selbst.gibGeladeneNektarmenge());
    }

    private void fliegeIntelligent() {

        ausschnittHolen();
        if (position.gibPosition().equals(posBienenstock)) {
            hinweg = true;
            landen();
            amBienenstock();
            starten();
        }
        if (hinweg) {
            if (weiterfliegen(honigFliegen)) {
                fliegenZufaellig();
            }
            else {
                if (position.gibNachbarfelder().containsKey(posBienenstock)) {
                    System.out.println(id + ": waehle benachbarten Bienenstock");
                    fliegen(posBienenstock);
                    weg.clear();
                }
                else {
                    hinweg = false;
                    if (weg.isEmpty()) {
                        System.out.println(id + ": rueckweg fehlt - kein bienenstock in sicht");
                    }
                    else {
                        System.out.println(id + ": kehre um");
                        fliegen((Koordinate)weg.getLast());
                        weg.removeLast();
                    }

                }
            }
        }
        else {
            if (position.gibNachbarfelder().containsKey(posBienenstock)) {
                System.out.println(id + ": bienenstock gefunden");
                fliegen(posBienenstock);
                weg.clear();
            }
            else {
                if (weg.isEmpty()) {
                    System.out.println(id + ": Weg endet im nichts (nicht am bienenstock)");
                }
                else {
                    System.out.println(id + ": auf dem rueckweg");
                    fliegen((Koordinate)weg.getLast());
                    weg.removeLast();
                }
            }
        }
    }



    public void fliegenZufaellig () {
        double zufall = Math.random();
        boolean nichtGeflogen = true;
        Koordinate ziel = new Koordinate(100, 100);
        HashMap direkteNachbarn = position.gibNachbarfelder();
        int zielnummer = (int)((zufall * (direkteNachbarn.size() - 1)) + 1.0);
        int i = 0;
        boolean alternativesFeldNotwendig = true;

        if (!(position == null)) {
            Iterator nachbarn = direkteNachbarn.values().iterator();

            while (nichtGeflogen && nachbarn.hasNext()) {
                ziel = ((EinfachesFeld)nachbarn.next()).gibPosition();

                i = i + 1;
                if (i == zielnummer) {
                    while (weg.contains(ziel) && alternativesFeldNotwendig) {
                        if (nachbarn.hasNext()) {
                            ziel = ((EinfachesFeld)nachbarn.next()).gibPosition();
                        }
                        else {
                            nachbarn = direkteNachbarn.values().iterator();
                            ziel = ((EinfachesFeld)nachbarn.next()).gibPosition();
                            alternativesFeldNotwendig = false;
                        }
                    }

                        //System.out.println("weg.contains(ziel) = " + weg.contains(ziel));
                        fliegen(ziel);
                        nichtGeflogen = false;

                }
            }
            if (nichtGeflogen) {
                System.out.println(id + ": Konnte mich nicht fuer ein Feld entscheiden.");
            }
            //visualisiereBiene(selbst);
            //visualisiereFelder(karte);
        }
        else {
            System.out.println(id + ": Ausschnitt ist leer!");
        }
    }

    public void fliegen (int x, int y) {
        Koordinate ziel = new Koordinate(x, y);
        int honigAlt = selbst.gibGeladeneHonigmenge();
        System.out.println(id + ": fliege nach (" + ziel.gibXPosition() + ", " + ziel.gibYPosition() + " )");
        long neuerAktCode = handler.aktionFliegen(aktCode, ziel);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
            if (hinweg) {
                weg.add(ziel);
            }
        }
        ausschnittHolen();
        if ((honigFliegen == 0) && !(neuerAktCode == 0L)) {
            honigFliegen = honigAlt - selbst.gibGeladeneHonigmenge();
        }
        //visualisiereBiene(selbst);
    }

    public void fliegen (Koordinate ziel) {
        int honigAlt = selbst.gibGeladeneHonigmenge();
        System.out.println(id + ": fliege nach (" + ziel.gibXPosition() + ", " + ziel.gibYPosition() + " )");
        long neuerAktCode = handler.aktionFliegen(aktCode, ziel);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
            if (hinweg) {
                weg.add(ziel);
            }
        }
        ausschnittHolen();
        if ((honigFliegen == 0) && !(neuerAktCode == 0L)) {
            honigFliegen = honigAlt - selbst.gibGeladeneHonigmenge();
        }
        //visualisiereBiene(selbst);
    }

    public void abbauen (int menge) {
        int alterHonig = selbst.gibGeladeneHonigmenge();
        System.out.println(id + ": baue ab (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        long neuerAktCode = handler.aktionNektarAbbauen(aktCode, menge);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        ausschnittHolen();
        if ((honigAbbauen == 0) && (!(neuerAktCode == 0L))) {
            honigAbbauen = alterHonig - selbst.gibGeladeneHonigmenge();
        }
        //visualisiereBiene(selbst);
    }

    public void starten() {
        System.out.println(id + ": starte (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        int honigAlt = selbst.gibGeladeneHonigmenge();
        long neuerAktCode = handler.aktionStarten(aktCode);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        else {
            System.out.println(id + ": konnte nicht starten");
        }
        ausschnittHolen();
        if (honigStarten == 0 && (!(neuerAktCode == 0L))) {
            honigStarten = honigAlt - selbst.gibGeladeneHonigmenge();
        }
        //visualisiereBiene(selbst);
    }

    public void landen() {
        System.out.println(id + ": lande (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        int honigAlt = selbst.gibGeladeneHonigmenge();
        long neuerAktCode = handler.aktionLanden(aktCode);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        else {
            System.out.println(id + ": aktCode " + aktCode);
            System.out.println(id + ": ungueltigerAktCode!!");
        }
        ausschnittHolen();
        if (honigLanden == 0 && (!(neuerAktCode == 0L))) {
            honigLanden = honigAlt - selbst.gibGeladeneHonigmenge();
        }

        //visualisiereBiene(selbst);
    }

    public void warten() {
        System.out.println(id + ": daeumchendrehen... (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        long neuerAktCode = handler.aktionWarten(aktCode);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        else {
            System.out.println(id + ": konnte nicht warten");
        }
        ausschnittHolen();
        //visualisiereBiene(selbst);
    }

    public void tanken(int menge) {
        int honigAlt = selbst.gibGeladeneHonigmenge();
        System.out.println(id + ": tanke (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        long neuerAktCode = handler.aktionHonigTanken(aktCode, menge);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        else {
            System.out.println(id + ": konnte nicht tanken");
        }
        ausschnittHolen();
        if ((honigTanken == 0) && (!(neuerAktCode == 0L))) {
            honigTanken = honigAlt - selbst.gibGeladeneHonigmenge();
        }
        //visualisiereBiene(selbst);
    }

    public void abliefern() {
        System.out.println(id + ": liefere Nektar ab (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        int honigAlt = selbst.gibGeladeneHonigmenge();
        int nektarAlt = selbst.gibGeladeneNektarmenge();
        long neuerAktCode = handler.aktionNektarAbliefern(aktCode);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        else {
            System.out.println(id + ": konnte kein Nektar abliefern");
        }
        ausschnittHolen();
        if ((honigAbliefern == 0) && (!(neuerAktCode == 0L))) {
            honigAbliefern = honigAlt - selbst.gibGeladeneHonigmenge();
        }
        if (selbst.gibGeladeneNektarmenge() == 0) {
            gesammelterNektar = gesammelterNektar + nektarAlt;
            System.out.println(id + ": hat bisher " + gesammelterNektar + " Nektar abgeliefert.");
        }
        //visualisiereBiene(selbst);
    }

    public void ausschnittHolen() {

        EinfacheKarte map;
        if (handler != null) {
        	map = handler.infoAusschnittHolen(aktCode);
        	if (map != null) {
        		karte = map;
        		selbst = karte.gibSelbst();
        		if (id == 0) {
        		 id = selbst.gibBienenID();
        		}
        		position = (EinfachesFeld)karte.gibFelder().get(selbst.gibPosition());
        		if (!(selbst.gibAktionsCode() == aktCode)) {
        			aktCode = selbst.gibAktionsCode();
        		}
        	}
        	else {
        		System.out.println(id + ": ich darf leider nicht weiterspielen :(");
        		synchronized (selbst) {
        			try {
        				selbst.wait();
        			}
        			catch (Exception e) {
        			}
        		}
        	}
        }
        else {
        	System.out.println("ich habe den Handler nicht in der Hand!!!");
        }
        
    }

    public void tanzen() {
        System.out.println(id + ": tanze (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        long neuerAktCode = handler.aktionTanzen(aktCode, 2, 1, true, true);
        if (!(neuerAktCode == 0L)) {
            aktCode = neuerAktCode;
        }
        ausschnittHolen();
        //visualisiereBiene(selbst);
    }
    public void zuschauen() {
        //visualisiereFelder(karte);
        System.out.println(id + ": schaue zu (" + position.gibPosition().gibXPosition() + ", " + position.gibPosition().gibYPosition() + " )");
        if (!(position.gibIDsTanzendeBienen().isEmpty())) {
            int zielBiene = ((Integer)position.gibIDsTanzendeBienen().iterator().next()).intValue();
            long nAktCode = handler.aktionZuschauen(aktCode, zielBiene);
            if (!( nAktCode == 0L)) {
                aktCode = nAktCode;
            }
            ausschnittHolen();
            //visualisiereBiene(selbst);
        }
        else {
            System.out.println(id + "keine tanzenden Bienen anwesend!!!");
            long nAktCode = handler.aktionZuschauen(aktCode, 111);
            if (!( nAktCode == 0L)) {
                aktCode = nAktCode;
            }
        }
    }

    private void aktionWaehlen() {
        if (!(position == null)) {
            if (position.gibIDsTanzendeBienen().size() > 0) {
                zuschauen();
            }
            else if (position instanceof EinfacherBienenstock) {
                if (selbst.gibGeladeneNektarmenge() > 0) {
                    abliefern();
                }
                if (selbst.gibGeladeneHonigmenge() < 50) {
                    tanken(1000);
                }
            }
            else if (position instanceof EinfacheBlume) {
                abbauen(1000);
            }
            else {
                tanzen();
                tanzen();
                warten();
            }
        }
    }

    private void visualisiereBiene (EinfacheBiene ich) {
        Koordinate pos = (Koordinate)ich.gibPosition();

        System.out.print("\n"
                        + id + ": Die Biene selbst\n"

                        + id + ": Rundennummer:  " + ich.gibRundennummer() + "\n"

                        + id + ": Zustand:  " + ich.gibZustand() + "\n"

        /*System.out.print("    BienenID:  ");
        System.out.print(ich.gibBienenID());
        System.out.print("\n");*/

/*        System.out.print("    BienenvolkID:  ");
        System.out.print(ich.gibBienenvolkID());
        System.out.print("\n");*/

                        + id + ": Position:  " + pos.gibXPosition() + " , "
                        + pos.gibYPosition() + "\n"


        //System.out.println(id + ": Aktionscode:  " + ich.gibAktionsCode());

        //System.out.println("    gespeichert:  " + aktCode);

                        + id + ": geladene Nektarmenge:  "
                        + ich.gibGeladeneNektarmenge() + "\n"

                        + id + ": geladene Honigmenge:  "
                        + ich.gibGeladeneHonigmenge() + "\n");
        if (!(selbst.gibInformation() == null)) {
            System.out.println(id + ": Info");
            Info information = selbst.gibInformation();
            if (information.besitztEntfernung()) {
                System.out.println(id + ": Entfernung : " + information.gibEntfernung());
            }
            if (information.besitztRichtung()) {
                System.out.println(id + ": Richtung : " + information.gibRichtung());
            }
        }
    }

    private void visualisiereFelder (EinfacheKarte zuVis) {


        Koordinate pos;
        System.out.println(id + ": Die Felder\n");

        if (!(zuVis == null)) {
            Iterator felder = zuVis.gibFelder().values().iterator();
            while (felder.hasNext()) {
                Object tmp = felder.next();
                EinfachesFeld feld = (EinfachesFeld)tmp;

                if (tmp instanceof EinfacherBienenstock) {
                    EinfacherBienenstock stock = (EinfacherBienenstock)tmp;
                    System.out.println("Bienenstock mit volksID: " + ((EinfacherBienenstock)stock).gibVolksID());
                }
                else if (tmp instanceof EinfacheBlume) {
                    EinfacheBlume plume = (EinfacheBlume)tmp;
                    System.out.println("Blume mit Merkmal: " + ((EinfacheBlume)plume).gibMerkmal());
                    HashSet aBienen = (HashSet)((EinfacheBlume)plume).gibIDsAbbauendeBienen();
                    Iterator itABienen = aBienen.iterator() ;
                    System.out.print("IDsAbbauendeBienen: ");
                    while (itABienen.hasNext()) {
                        System.out.print(itABienen.next() + ", ");
                    }
                    System.out.print("\n");
                }
                else {
                    System.out.println("konventioneller Platz");
                }

                pos = feld.gibPosition();
                /*if (zuVis.gibSelbst().gibPosition().equals(pos)) {
                    System.out.println("Auf diesem Feld befindet sich Biene " + zuVis.gibSelbst().gibBienenID() + " selbst.");
                }*/

                System.out.println("Position:  (" + pos.gibXPosition() + ", " + pos.gibYPosition() + ")");

                if (feld.gibNachbarfelder() == null) {
                    System.out.println("hat KEINE Nachbarfelder!!!");
                }
                else {
                    System.out.print("Nachbarfelder: [");
                    Iterator nachbarn = feld.gibNachbarfelder().values().iterator();
                    while (nachbarn.hasNext()) {
                        pos = ((EinfachesFeld)nachbarn.next()).gibPosition();
                        System.out.print("(");
                        System.out.print(pos.gibXPosition());
                        System.out.print(", ");
                        System.out.print(pos.gibYPosition());
                        System.out.print("), ");
                    }
                    System.out.print("] \n");
                }

                System.out.print("ID's wartende Bienen: ");
                Iterator wBienen = feld.gibIDsWartendeBienen().iterator();
                while (wBienen.hasNext()) {
                    System.out.print(wBienen.next());
                    System.out.print(", ");
                }
                System.out.print("\n");

                System.out.print("ID's fliegende Bienen: ");
                Iterator fliBienen = feld.gibIDsFliegendeBienen().iterator();
                while (fliBienen.hasNext()) {
                    System.out.print(fliBienen.next());
                    System.out.print(", ");
                }
                System.out.print("\n");

                System.out.print("ID's tanzende Bienen: ");
                Iterator tanBienen = feld.gibIDsTanzendeBienen().iterator();
                while (tanBienen.hasNext()) {
                    System.out.print(tanBienen.next());
                    System.out.print(", ");
                }
                System.out.print("\n");
                System.out.print("\n");
            }
        }
        else {
            System.out.println(id + ": (TestAgentLeben.visFelder) zuVis == null!!!");
        }
    }
    
    public int gibVolksID () {
    	return volksID;
    }
}