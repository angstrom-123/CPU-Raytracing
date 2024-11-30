package com.ang;

import com.ang.Thread.Master;

/**
 * Holds globally accessible information and instances of classes.
 */
public class Global {
    public final static double  infinity        = Double.MAX_VALUE;
    public final static double  pi              = 3.1415926535897932385;
    public static int           imageWidth;
    public static int           imageHeight;
    public static Master        master;

    /**
     * Converts degrees into radians.
     * @param degrees angle in degrees.
     * @return { @param degrees } converted to radians.
     */
    public static double degToRad(double degrees) {
        return degrees * pi / 180.0;
    }

    /**
     * Generates a random number within a range.
     * @param min minimum value for random number.
     * @param max maximum value for random number.
     * @return random number within range ({ @param min }, { @param max }).
     */
    public static double randomInRange(double min, double max) {
        return min + (max - min) * Math.random();
    }

    /**
     * Calls upon the master to immediately terminate all threads.
     */
    public static void terminateThreads() {
        master.forceStop();
    }
}
