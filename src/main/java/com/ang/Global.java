package com.ang;

import com.ang.Thread.Master;

public class Global {
    // constants
    public final static double infinity = Double.MAX_VALUE;
    public final static double pi = 3.1415926535897932385;

    // info
    public static int imageWidth;
    public static int imageHeight;

    // thread
    public static Master master;

    // util
    public static double deg2rad(double degrees) {
        return degrees * pi / 180.0;
    }

    public static double randomInRange(double min, double max) {
        return min + (max - min) * Math.random();
    }

    public static void terminateThreads() {
        master.forceStop();
    }
}
