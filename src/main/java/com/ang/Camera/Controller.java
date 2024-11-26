package com.ang.Camera;

import com.ang.Util.Ray;
import com.ang.Util.Quaternion;
import com.ang.Util.Vector3;

public class CameraController {
    private Vector3 w, u, v;
    private Vector3 lf;
    private Vector3 la;

    public CameraController(Vector3 w, Vector3 u, Vector3 v, Vector3 lf, Vector3 la) {
        set(w, u, v, lf, la);
    }

    public void set(Vector3 w, Vector3 u, Vector3 v, Vector3 lf, Vector3 la) {
        this.w = w; // back
        this.u = u; // right
        this.v = v; // up
        this.lf = lf;
        this.la = la;
    }

    public Vector3[] move(double x, double y, double z, double step) {
        Vector3 offset = (u.multiply(x)).add(v.multiply(y)).add(w.multiply(z)).unitVector();
        offset.MULTIPLY(step);
        lf = lf.add(offset);
        la = la.add(offset);
        return new Vector3[]{lf, la};
    }

    public Vector3[] turn(double x, double y, double z, double step) {
        Quaternion q = Quaternion.fromAxisAngle(step, x, y, z);
        Vector3 rotated = q.rotate(w.negative());
        Ray raycast = new Ray(lf, rotated.unitVector());
        la = raycast.at(1);
        return new Vector3[]{lf, la};
    }
}
