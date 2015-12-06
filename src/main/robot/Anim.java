package robot;

/**
 * A class for maintaining a list of KeyInfo sequence of values.
 * A KeyInfo sequence is supplied as a normalised sequence in the range 0.0 to 1.0.
 * The start and duration values anchor this in relation to global time.
 * The current implementation uses linear interpolation between pairs of key info data.
 *
 * @author Dr Steve Maddock
 * @version 1.0 (21/11/2013)
 *          <p/>
 *          Utilised by Paul MacDonald.
 *          prmacdonald1@sheffield.ac.uk
 *          1350155458
 */

public class Anim {
    private KeyInfo[] keys;
    private double currValue;
    private double start, duration;
    private boolean preUse, postUse;

    /**
     * Constructor.
     */
    public Anim(double start, double duration, boolean preUse, boolean postUse, KeyInfo[] keyInfo) {
        this.keys = keyInfo;
        this.start = start;
        this.duration = duration;
        this.preUse = preUse;
        this.postUse = postUse;
        reset();
    }

    public static void main(String[] args) {
        // assumes keyframe frame numbers are not repeated
        KeyInfo[] k = new KeyInfo[4];
        k[0] = new KeyInfo(0.0, 1.0);
        k[1] = new KeyInfo(0.2, 10.0);
        k[2] = new KeyInfo(0.7, 20.0);
        k[3] = new KeyInfo(1.0, 22.0);

        Anim anim = new Anim(3.0, 5.0, true, true, k);

        for (int i = -5; i < 50; ++i) {
            anim.update(i / 5.0);
            System.out.println("i/10=" + i / 5.0 + ", p=" + anim.getCurrValue());
        }
    }

    public void reset() {
        if (preUse) currValue = keys[0].getValue();
        else currValue = 0;
    }

    /**
     *
     */
    public void update(double t) {
        if (t <= start) {
            if (preUse || t == start) currValue = keys[0].getValue();
            else currValue = 0;
        } else if (t >= start + duration) {
            if (postUse || t == start + duration) currValue = keys[keys.length - 1].getValue();
            else currValue = 0;
        } else {
            double normalisedTime = (t - start) / duration;
            int k1 = findKeyFrameBefore(normalisedTime);
            currValue = cosineInterpolation(normalisedTime, k1, k1 + 1);
            //currValue = bezierCurve(normalisedTime, k1, k1+1, k1+2);
        }
    }

    /**
     * @return The current parameter value
     */
    public double getCurrValue() {
        return currValue;
    }

    private int findKeyFrameBefore(double t) {
        int k1 = 0;
        while (k1 < keys.length && keys[k1].getKF() < t)
            k1++;
        return k1 - 1;
    }

    private double linearInterpolation(double t, int k1, int k2) {
        double f1 = keys[k1].getKF();
        double f2 = keys[k2].getKF();
        double fraction = (t - f1) / (f2 - f1);
        double p1 = keys[k1].getValue();
        double p2 = keys[k2].getValue();
        double p = p1 + fraction * (p2 - p1);
        System.out.println(f1 + ", " + f2 + ", " + fraction + ", " + p1 + ", " + p2 + ", " + p);
        return p;
    }

    private double cosineInterpolation(double t, int k1, int k2) {
        double f1 = keys[k1].getKF();
        double f2 = keys[k2].getKF();
        double fraction = (t - f1) / (f2 - f1);
        double p1 = keys[k1].getValue();
        double p2 = keys[k2].getValue();
        double cosFraction = (1 - Math.cos(fraction * Math.PI)) / 2;
        return (p1 * (1 - cosFraction) + p2 * cosFraction);
    }

    private double bezierCurve(double t, int k1, int k2, int k3) {
        double f1 = keys[k1].getKF();
        double f2 = keys[k2].getKF();
        double fraction = t;
        double p1 = keys[k1].getValue();
        double p2 = keys[k2].getValue();
        double p3 = keys[k3].getValue();
        return ((1 - fraction) * ((1 - fraction) * (p1 + fraction * p2)) + ((1 - fraction) * p2 + fraction * p3));
    }

    /**
     * Standard use of toString method
     *
     * @return A string representing the key data
     */
    public String toString() {
        String s = "Anim: ";
        return s;
    }

}