package com.ang;

import com.ang.Thread.Master;

/*
 * Stores information about constants, provides simple utility functions, and 
 * allows for access to the thread master globally.
 */
public class Global {
    public final static double  infinity        = Double.MAX_VALUE;
    public final static double  pi              = 3.1415926535897932385;
    public static int           imageWidth;
    public static int           imageHeight;
    public static Master        master;

    public static double degToRad(double degrees) {
        return degrees * pi / 180.0;
    }

    public static double randomInRange(double min, double max) {
        return min + (max - min) * Math.random();
    }

    public static void terminateThreads() {
        master.forceStop();
    }
}
