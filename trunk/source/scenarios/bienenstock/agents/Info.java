/* Generated by Together */

package scenario.bienenstock;

public class Info {
    private double richtung;
    private double entfernung;
    private boolean richtungMitteilen;
    private boolean entfernungMitteilen;

    public Info(double richtg, double entf,
                boolean richtgMitteilen, boolean entfMitteilen) {
        richtung = richtg;
        entfernung = entf;
        richtungMitteilen = richtgMitteilen;
        entfernungMitteilen = entfMitteilen;
    }

    public boolean besitztRichtung() {
        return richtungMitteilen;
    }

    public boolean besitztEntfernung() {
        return entfernungMitteilen;
    }

    public double gibRichtung() {
        return richtung;
    }

    public double gibEntfernung() {
        return entfernung;
    }

    public Info klonen() {
        return new Info(richtung,
                        entfernung,
                        richtungMitteilen,
                        entfernungMitteilen);
    }
}
