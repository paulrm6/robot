package robot;

/**
 * A class for controlling a set of Anim instances
 *
 * @author    Dr Steve Maddock
 * @version   1.0 (21/11/2013)
 *
 * Utilised by Paul MacDonald.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */

public class AnimationScene {

    public static final int ROBOT_X_PARAM = 0;
    public static final int ROBOT_Z_PARAM = 1;
    public static final int ROBOT_ROTATE = 2;
    public static final int ROBOT_HEAD = 3;
    public static final int ROBOT_TRAY_ARM = 4;
    public static final int ROBOT_LEAN_F = 5;
    public static final int ROBOT_LEAN_S = 6;

    public static final int MAX_PARAMS = 10;
    private Anim[] param;
    private int numParams;
    private double globalStartTime, localTime, repeatTime, savedLocalTime;

    /**
     * Constructor.
     *
     */
    public AnimationScene() {
        param = new Anim[MAX_PARAMS];
        param[ROBOT_X_PARAM] = create(0.0, 60, true, true,
                new double[]{0.0,0.0,
                        0.05,2,//arrive at table1
                        0.15,2,//depart table1
                        0.2,-3.5,//arrive at table2
                        0.3,-3.5,//depart table2
                        0.40,3,
                        0.45,0,//arrive at table3
                        0.55,0,//depart table3
                        0.65,7,

                        1.0,0.0
                });
        param[ROBOT_Z_PARAM] = create(0.0, 60, true, true,
                new double[]{0.0,0.0,
                        0.05,7,//arrive at table1
                        0.15,7,//depart table1
                        0.2,6,//arrive at table2
                        0.3,6,//depart table2
                        0.45,-7,//arrive at table3
                        0.55,-7,//depart table3
                        0.65,-13,


                        1.0,0.0
                });
        param[ROBOT_ROTATE] = create(0.0,60,true,true,
                new double[]{0.0,0.0,
                        0.01,15,//face table1
                        0.05,0,//face table1
                        0.125,0,
                        0.15,-95,//look at table2
                        0.20,-90,//arrive at table2
                        0.30,-90,//depart table2
                        0.35,-215,//look around toward table3
                        0.40,-160,
                        0.43,-90,//arrive at table3
                        0.55,-90,//depart table3
                        0.65,0,

                        1.0,0.0
                });
        param[ROBOT_HEAD] = create(0.0,60,true,true,
                new double[]{0.0,0.0,
                        0.05,-15,
                        0.10,-15,
                        0.15,0,
                        0.20,-15,
                        0.25,-15,
                        0.3,0,
                        0.4,0,
                        0.43,-15,
                        0.5,-15,
                        0.55,0,

                        1.0,0.0
                });
        param[ROBOT_TRAY_ARM] = create(0.0,60,true,true,
                new double[]{0.0,0.0,
                        0.03,0,
                        0.07,-86,
                        0.12,-86,
                        0.15,0,
                        0.18,0,
                        0.22,-86,
                        0.27,-86,
                        0.3,0,
                        0.42,0,
                        0.46,-86,
                        0.51,-86,
                        0.55,0,

                        1.0,0.0
                });
        param[ROBOT_LEAN_F] = create(0.0,60,true,true,
                new double[]{0.0,0.0,
                        0.02,-15,//lean forward
                        0.05,0,//arrive at table1
                        0.15,0,//depart table1
                        0.17,-15,//lean forward
                        0.20,0,//arrive at table2
                        0.30,0,
                        0.32,10,
                        0.35,-10,
                        0.45,0,
                        0.55,0,
                        0.6,15,
                        0.65,0,

                        1.0,0.0
                });
        param[ROBOT_LEAN_S] = create(0.0,60,true,true,
                new double[]{0.0,0.0,
                        0.02,-3,
                        0.04,3,
                        0.05,0,
                        0.17,0,
                        0.18,-3,
                        0.20,0,
                        0.30,0,
                        0.32,3,
                        0.35,0,
                        0.40,-10,
                        0.45,0,
                        0.55,0,
                        0.60,10,
                        0.65,0,

                        1.0,0.0
                });
        numParams = ROBOT_LEAN_S+1;
        localTime = 0;
        savedLocalTime = 0;
        repeatTime = 50;
        globalStartTime = getSeconds();
    }

    public Anim create (double start, double duration, boolean pre, boolean post, double[] data) {
        KeyInfo[] k = new KeyInfo[data.length/2];
        for (int i=0; i<data.length/2; ++i) {
            k[i] = new KeyInfo(data[i*2], data[i*2+1]);
        }
        return new Anim(start, duration, pre, post, k);
    }

    public void startAnimation() {
        globalStartTime = getSeconds() - savedLocalTime;
    }

    public void pauseAnimation() {
        savedLocalTime = getSeconds() - globalStartTime;
    }

    public void reset() {
        globalStartTime = getSeconds();
        savedLocalTime = 0;
        for (int i=0; i<numParams; ++i) {
            param[i].reset();
        }
    }

    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

    /**
     *
     */
    public void update() {
        localTime = getSeconds() - globalStartTime;
        if (localTime > repeatTime) {
            globalStartTime = getSeconds();
            localTime = 0;
            savedLocalTime = 0;
        }
        for (int i=0; i<numParams; ++i) {
            param[i].update(localTime);
        }
    }

    /**
     *
     *
     * @return The current parameter value
     */
    public double getParam(int i) {
        if (i<0 || i>=numParams) {
            System.out.println("EEError: parameter out of range");
            return 0;
        }
        else {
            return param[i].getCurrValue();
        }
    }

    /**
     * Standard use of toString method
     *
     * @return A string representing the key data
     */
    public String toString() {
        String s = "Anim manager: ";
        return s;
    }

    public static void main(String[] args) {
        AnimationScene a = new AnimationScene();
        System.out.println(a.getParam(a.ROBOT_X_PARAM));
        double start = a.getSeconds();
        double t=start;
        while (t<start+20) {
            double ls = a.getSeconds();
            double lt = ls;
            while (lt < ls+0.2) lt = a.getSeconds();
            a.update();
            System.out.println(a.localTime + ", " + a.getParam(a.ROBOT_X_PARAM));
            t = a.getSeconds();
        }
    }

}
