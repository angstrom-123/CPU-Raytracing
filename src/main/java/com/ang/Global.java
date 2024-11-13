package com.ang;

public class Global {
    // constants
    public final static double infinity = Double.MAX_VALUE;
    public final static double pi = 3.1415926535897932385;

    public static double counter = 0;

    // util
    public static double deg2rad(double degrees) {
        return degrees * pi / 180.0;
    }

    public static double randomInRange(double min, double max) {
        return min + (max - min) * Math.random();
    }
}
