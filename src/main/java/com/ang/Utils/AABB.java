package com.ang.Utils;

import com.ang.Global;

public class AABB {
    public Interval x, y, z;

    public AABB() {
        x = new Interval();
        y = new Interval();
        z = new Interval();
    }

    // define in terms of intersection of intervals
    public AABB(Interval x, Interval y, Interval z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // define in terms of 3d coords of corners
    public AABB(Vector3 a, Vector3 b) {
        // converting coords to intervals
        if (a.x() <= b.x()) {
            x = new Interval(a.x(), b.x());
        } else {
            x = new Interval(b.x(), a.x());
        }

        if (a.y() <= b.y()) {
            y = new Interval(a.y(), b.y());
        } else {
            y = new Interval(b.y(), a.y());
        }

        if (a.z() <= b.z()) {
            z = new Interval(a.z(), b.z());
        } else {
            z = new Interval(b.z(), a.z());
        }
    }

    // define in terms of multiple bBoxes
    public AABB(AABB box0, AABB box1) {
        x = new Interval(box0.x, box1.x);
        y = new Interval(box0.y, box1.y);
        z = new Interval(box0.z, box1.z);
    }

    public Interval axisInterval(int n) {
        switch(n) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                return z;
        }
    }

    public int largestAxis() {
        if (x.size() > y.size() && x.size() > z.size()) {
            return 0; // x
        }
        if (y.size() > z.size()) {
            return 1; // y
        }
        return 2; // z
    }

    public boolean hit(Ray r, Interval tInterval) {
        Global.counter++;

        Vector3 rayOrigin = r.origin();
        Vector3 rayDir = r.direction();

        for (int axis = 0; axis < 3; axis++) {
            Interval axIn = axisInterval(axis);
            double axDirInv = 1.0 / rayDir.e[axis];

            double t0 = (axIn.min - rayOrigin.e[axis]) * axDirInv;
            double t1 = (axIn.max - rayOrigin.e[axis]) * axDirInv;

            if (t0 < t1) {
                if (t0 > tInterval.min) {
                    // tMin = t0
                    tInterval.setMin(t0);
                }
                if (t1 < tInterval.max) {
                    // tMax = t1
                    tInterval.setMax(t1);
                } 
            } else {
                if (t1 > tInterval.min) {
                    // tMin = t1
                    tInterval.setMin(t1);
                }
                if (t0 < tInterval.max) {
                    // tMax = t0
                    tInterval.setMax(t0);
                }
            }

            if (tInterval.max <= tInterval.min) {
                return false;
            }
        }

        return true;
    }
}
