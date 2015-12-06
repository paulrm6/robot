package robot;

/**
 * A class for controlling a set of Anim instances
 *
 * @author Dr Steve Maddock
 * @version 1.0 (21/11/2013)
 *
 * Utilised by Paul MacDonald. (EDITED)
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 *
 * Added comments
 * Edited variables in constructor
 * Removed unused methods
 */

public class AnimationScene {

    //Set up the parameters
    public static final int ROBOT_X_PARAM = 0;
    public static final int ROBOT_Z_PARAM = 1;
    public static final int ROBOT_ROTATE = 2;
    public static final int ROBOT_HEAD = 3;
    public static final int ROBOT_TRAY_ARM = 4;
    public static final int ROBOT_LEAN_F = 5;
    public static final int ROBOT_LEAN_S = 6;
    public static final int MAX_PARAMS = 10;
    private final int DURATION = 40;
    private Anim[] param;
    private int numParams;
    private double globalStartTime, localTime, repeatTime, savedLocalTime;

    /**
     * Constructor.
     */
    public AnimationScene() {
        param = new Anim[MAX_PARAMS];
        param[ROBOT_X_PARAM] = create(0.0, DURATION, true, true,
                new double[]{0.0, 0.0, 0.05, 2, 0.15, 2, 0.2, -4, 0.3, -4, 0.40, 3, 0.45, 0, 0.55, 0,
                        0.65, 7, 0.7, 5, 0.80, -7, 0.90, -7, 0.95, -4, 1.0, 0.0});
        param[ROBOT_Z_PARAM] = create(0.0, DURATION, true, true,
                new double[]{0.0, 0.0, 0.05, 7, 0.15, 7, 0.2, 6, 0.3, 6, 0.45, -7, 0.55, -7,
                        0.65, -13, 0.75, -1, 0.80, 3, 0.90, 3, 0.95, -3, 1.0, 0.0});
        param[ROBOT_ROTATE] = create(0.0, DURATION, true, true,
                new double[]{0.0, 0.0, 0.01, 15, 0.05, 0, 0.125, 0, 0.15, -95, 0.20, -90, 0.30, -90,
                        0.35, -215, 0.40, -160, 0.43, -90, 0.55, -90, 0.65, 0, 0.67, -20, 0.70, -60,
                        0.80, 0, 0.90, 0, 0.94, -20, 0.97, 20, 1.0, 0});
        param[ROBOT_HEAD] = create(0.0, DURATION, true, true,
                new double[]{0.0, 0.0, 0.05, -15, 0.10, -15, 0.15, 0, 0.20, -15, 0.25, -15, 0.3, 0,
                        0.4, 0, 0.43, -15, 0.5, -15, 0.55, 0, 0.72, 0, 0.80, -15, 0.87, -15, 0.90, 0,
                        1.0, 0.0});
        param[ROBOT_TRAY_ARM] = create(0.0, DURATION, true, true,
                new double[]{0.0, 0.0, 0.03, 0, 0.07, -86, 0.12, -86, 0.15, 0, 0.18, 0, 0.22, -86,
                        0.27, -86, 0.3, 0, 0.42, 0, 0.46, -86, 0.51, -86, 0.55, 0, 0.78, 0, 0.82, -86,
                        0.87, -86, 0.90, 0, 1.0, 0.0});
        param[ROBOT_LEAN_F] = create(0.0, DURATION, true, true,
                new double[]{0.0, 0.0, 0.02, -15, 0.05, 0, 0.15, 0, 0.17, -15, 0.20, 0, 0.30, 0, 0.32, 10,
                        0.35, -10, 0.45, 0, 0.55, 0, 0.6, 15, 0.65, 0, 0.77, -20, 0.80, 0, 0.90, 0,
                        0.94, 10, 0.97, -10, 1.0, 0.0});
        param[ROBOT_LEAN_S] = create(0.0, DURATION, true, true,
                new double[]{0.0, 0.0, 0.02, -5, 0.04, 5, 0.05, 0, 0.17, 0, 0.18, -5, 0.20, 0, 0.30, 0,
                        0.32, 5, 0.35, 0, 0.40, -10, 0.45, 0, 0.55, 0, 0.60, 10, 0.65, 0, 0.70, 5,
                        0.75, -5, 0.80, 0, 0.90, 0, 0.94, -5, 0.97, 5, 1.0, 0.0});
        numParams = ROBOT_LEAN_S + 1;
        localTime = 0;
        savedLocalTime = 0;
        repeatTime = 45;
        globalStartTime = getSeconds();
    }

    /**
     * @param start    Specifies a start time
     * @param duration Specifies a duration (Start time+duration=finish time)
     * @param pre is the frame used before the start
     * @param post is the frame used after the finish
     * @param data     Gives the data to be parsed to the into KeyInfo
     * @return A new Anim which maintains the list of KeyInfo
     */
    public Anim create(double start, double duration, boolean pre, boolean post, double[] data) {
        KeyInfo[] k = new KeyInfo[data.length / 2];
        for (int i = 0; i < data.length / 2; ++i) {
            k[i] = new KeyInfo(data[i * 2], data[i * 2 + 1]);
        }
        return new Anim(start, duration, pre, post, k);
    }

    /**
     * Starts the animation
     */
    public void startAnimation() {
        globalStartTime = getSeconds() - savedLocalTime;
    }

    /**
     * Pauses the animation
     */
    public void pauseAnimation() {
        savedLocalTime = getSeconds() - globalStartTime;
    }

    /**
     * Resets the animation
     */
    public void reset() {
        globalStartTime = getSeconds();
        savedLocalTime = 0;
        for (int i = 0; i < numParams; ++i) {
            param[i].reset();
        }
    }

    /**
     * Gets the current system time in seconds
     *
     * @return System time in seconds
     */
    private double getSeconds() {
        return System.currentTimeMillis() / 1000.0;
    }

    /**
     * Updates the current parameter based off the time
     */
    public void update() {
        localTime = getSeconds() - globalStartTime;
        if (localTime > repeatTime) {
            globalStartTime = getSeconds();
            localTime = 0;
            savedLocalTime = 0;
        }
        for (int i = 0; i < numParams; ++i) {
            param[i].update(localTime);
        }
    }

    /**
     * A method to return the value of a given parameter
     *
     * @param i the parameter
     * @return the current value of that parameter
     */
    public double getParam(int i) {
        if (i < 0 || i >= numParams) {
            System.out.println("Error: parameter out of range");
            return 0;
        } else {
            return param[i].getCurrValue();
        }
    }

}
