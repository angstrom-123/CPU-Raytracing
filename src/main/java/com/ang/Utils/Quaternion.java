package com.ang.Utils;

import com.ang.Global;

public class Quaternion {
    public double[] e = new double[4];

    public Quaternion(double w, double x, double y, double z) {
        e[0] = w;
        e[1] = x;
        e[2] = y;
        e[3] = z;
    }

    public double w() {
        return e[0];
    }

    public double x() {
        return e[1];
    }

    public double y() {
        return e[2];
    }

    public double z() {
        return e[3];
    }

    public Quaternion multiply(Quaternion q) {
        return new Quaternion(
            w() * q.w() - x() * q.x() - y() * q.y() - z() * q.z(),
            w() * q.x() + x() * q.w() + y() * q.z() - z() * q.y(),
            w() * q.y() - x() * q.z() + y() * q.w() + z() * q.x(),
            w() * q.z() + x() * q.y() - y() * q.x() + z() * q.w()
        );
    }

    public Vector3 rotate(Vector3 v) {
        Quaternion vecQuat = new Quaternion(0, v.x(), v.y(), v.z());
        Quaternion conjugate = new Quaternion(w(), -x(), -y(), -z());
        Quaternion result = this.multiply(vecQuat).multiply(conjugate);
        return new Vector3(result.x(), result.y(), result.z());
    }

    public static Quaternion fromAxisAngle(double degrees, double x, double y, double z) {
        double halfTheta = Global.deg2rad(degrees) / 2;
        double sin = Math.sin(halfTheta);
        double cos = Math.cos(halfTheta);
        return new Quaternion(cos, sin * x, sin * y, sin * z);
    }
}
