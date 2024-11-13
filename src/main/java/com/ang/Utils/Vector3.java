package com.ang.Utils;

import com.ang.Global;

public class Vector3 {
    public double[] e = new double[]{0,0,0};

    public Vector3 (double x, double y, double z) {
        this.e[0] = x;
        this.e[1] = y;
        this.e[2] = z;
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

    // operators that don't change state of this
    public Vector3 add(Vector3 v) {
        return new Vector3(e[0]+v.e[0], e[1]+v.e[1], e[2]+v.e[2]);
    }

    public Vector3 subtract(Vector3 v) {
        return new Vector3(e[0]-v.e[0], e[1]-v.e[1], e[2]-v.e[2]);
    }

    public Vector3 multiply(double t) {
        return new Vector3(e[0] * t, e[1] * t, e[2] * t);
    }

    public Vector3 multiply(Vector3 v) {
        return new Vector3(e[0]*v.e[0], e[1]*v.e[1], e[2]*v.e[2]);
    }

    public Vector3 divide(double t) {
        return multiply(1/t);
    }

    public Vector3 negative() {
        return new Vector3(-e[0], -e[1], -e[2]);
    }

    // operaters that affect this

    public void ADD(Vector3 v) {
        e[0] += v.e[0];
        e[1] += v.e[1];
        e[2] += v.e[2];
    }

    public void SUBTRACT(Vector3 v) {
        e[0] -= v.e[0];
        e[1] -= v.e[1];
        e[2] -= v.e[2];
    }

    public void MULTIPLY(double t) {
        e[0] *= t;
        e[1] *= t;
        e[2] *= t;
    }

    public void DIVIDE(double t) {
        MULTIPLY(1/t);
    }

    public void NEGATIVE() {
        this.MULTIPLY(-1);
    }
    // util

    public Vector3 unitVector() {
        return divide(length());
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    } 

    public double lengthSquared() {
        return (e[0] * e[0]) + (e[1] * e[1]) + (e[2] * e[2]);
    }

    public boolean nearZero() {
        double min = 1e-8;
        return (Math.abs(e[0]) < min) && (Math.abs(e[1]) < min) && (Math.abs(e[2]) < min);
    }

    // static utility functions

    public static double dot(Vector3 u, Vector3 v) {
        return u.e[0] * v.e[0] + u.e[1] * v.e[1] + u.e[2] * v.e[2];
    }

    public static Vector3 cross(Vector3 u, Vector3 v) {
        return new Vector3(
            u.e[1] * v.e[2] - u.e[2] * v.e[1],
            u.e[2] * v.e[0] - u.e[0] * v.e[2],
            u.e[0] * v.e[1] - u.e[1] * v.e[0]
        );
    }

    public static Vector3 random() {
        return new Vector3(Math.random(), Math.random(), Math.random());
    }

    public static Vector3 random(double min, double max) {
        return new Vector3(Global.randomInRange(min, max), Global.randomInRange(min, max), Global.randomInRange(min, max)); 
    }

    public static Vector3 randomUnitVector() {
        while (true) {
            Vector3 p = Vector3.random(-1, 1);
            double lenS = p.lengthSquared();
            if (lenS < 1 && 1e-160 < lenS) {
                return p.divide(Math.sqrt(lenS));
            }
        }
    }

    public static Vector3 randomOnHemi(Vector3 normal) {
        Vector3 vec = Vector3.randomUnitVector();
        if (Vector3.dot(vec, normal) < 0.0) { // more than 180 degrees apart
            vec.NEGATIVE();
        } 
        return vec;
    }

    public static Vector3 reflect(Vector3 v, Vector3 normal) {
        // 2 * v dot normal gives the magnitude of v parallel to normal
        // multiplying this my normal = vector in direction of normal
        // subtracting this from incident gives new reflected direction
        // subtraction because opposite direction to incident
        return v.subtract(normal.multiply(2 * Vector3.dot(v, normal)));
        // v - normal * (2 * v.normal)
    }   

    public static Vector3 refract(Vector3 v, Vector3 normal, double neta1OverNeta2) {
        // A dot B = |A||B|cos theta = ABcos theta (unit vectors)
        // 
        double cosTheta = Math.min(Vector3.dot(v.negative(), normal), 1.0);
        Vector3 perpRay = v.add(normal.multiply(cosTheta)).multiply(neta1OverNeta2);
        Vector3 paraRay = normal.multiply(Math.sqrt(Math.abs(1.0 - perpRay.lengthSquared()))).negative(); 
        return perpRay.add(paraRay);
    }

    public static Vector3 randomInUnitDisk() {
        while (true) {
            Vector3 v = new Vector3(Global.randomInRange(-1, 1), Global.randomInRange(-1, 1), 0);
            if (v.lengthSquared() < 1) {
                return v;
            }
        }
    }
}