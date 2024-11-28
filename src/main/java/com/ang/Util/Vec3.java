package com.ang.Util;

import com.ang.Global;

public class Vec3 {
    public double[] e = new double[]{0.0, 0.0, 0.0};

    public Vec3(double x, double y, double z) {
        e[0] = x;
        e[1] = y;
        e[2] = z;
    }

    public double x() {
        return e[0];
    }

    public double y() {
        return e[1];
    }

    public double z() {
        return e[2];
    }

    // operators

    public Vec3 add(Vec3 v) {
        return new Vec3(e[0] + v.e[0], e[1] + v.e[1], e[2] + v.e[2]);
    }

    public Vec3 subtract(Vec3 v) {
        return new Vec3(e[0] - v.e[0], e[1] - v.e[1], e[2] - v.e[2]);
    }

    public Vec3 multiply(double t) {
        return new Vec3(e[0] * t, e[1] * t, e[2] * t);
    }

    public Vec3 multiply(Vec3 v) {
        return new Vec3(e[0] * v.e[0], e[1] * v.e[1], e[2] * v.e[2]);
    }

    public Vec3 divide(double t) {
        return multiply(1.0 / t);
    }

    public Vec3 negative() {
        return new Vec3(-e[0], -e[1], -e[2]);
    }

    // util

    public Vec3 unitVector() {
        return divide(length());
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    } 

    public double lengthSquared() {
        return (e[0] * e[0]) + (e[1] * e[1]) + (e[2] * e[2]);
    }

    public boolean nearZero() {
        double min = 1E-8;

        return (Math.abs(e[0]) < min) && (Math.abs(e[1]) < min) 
        && (Math.abs(e[2]) < min);
    }

    // static

    public static double dot(Vec3 u, Vec3 v) {
        return u.e[0] * v.e[0] + u.e[1] * v.e[1] + u.e[2] * v.e[2];
    }

    public static Vec3 cross(Vec3 u, Vec3 v) {
        return new Vec3(
            u.e[1] * v.e[2] - u.e[2] * v.e[1],
            u.e[2] * v.e[0] - u.e[0] * v.e[2],
            u.e[0] * v.e[1] - u.e[1] * v.e[0]);
    }

    public static Vec3 random() {
        return new Vec3(Math.random(), Math.random(), Math.random());
    }

    public static Vec3 random(double min, double max) {
        return new Vec3(
            Global.randomInRange(min, max),
            Global.randomInRange(min, max), 
            Global.randomInRange(min, max)); 
    }

    public static Vec3 randomUnitVector() {
        while (true) {
            Vec3 p = Vec3.random(-1.0, 1.0);
            double lenSquared = p.lengthSquared();
            if ((lenSquared < 1.0) && (1e-160 < lenSquared)) {
                return p.divide(Math.sqrt(lenSquared));
            }
        }
    }

    public static Vec3 randomOnHemi(Vec3 normal) {
        Vec3 vec = Vec3.randomUnitVector();
        if (Vec3.dot(vec, normal) < 0.0) { // more than 180 degrees apart
            return vec.negative();
        } 
        return vec;
    }

    // reflects vector v in surface perpendicular to vector normal
    public static Vec3 reflect(Vec3 v, Vec3 normal) {
        return v.subtract(normal.multiply(2.0 * Vec3.dot(v, normal)));
    }   

    //n1sin(theta1) = n2sin(theta2)
    public static Vec3 refract(Vec3 v, Vec3 normal, double n1Overn2) {
        double cosTheta = Math.min(Vec3.dot(v.negative(), normal), 1.0);

        // perpendicular component
        Vec3 a = v.add(normal.multiply(cosTheta)).multiply(n1Overn2);
        // parallel component
        Vec3 b = normal.multiply(Math.sqrt(Math.abs(1.0 - a.lengthSquared()))); 
       
        return a.subtract(b);
    }

    public static Vec3 randomInUnitDisk() {
        while (true) {
            Vec3 v = new Vec3(
                Global.randomInRange(-1.0, 1.0), 
                Global.randomInRange(-1.0, 1.0), 
                0.0);

            if (v.lengthSquared() < 1.0) {
                return v;
            }
        }
    }
}