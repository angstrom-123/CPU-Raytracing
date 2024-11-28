package com.ang.AABB;

import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

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
    public AABB(Vec3 a, Vec3 b) {
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

        // if an interval is extremely small, it is expanded
        if (x.size() < 1E-8) {
            x.expand(1E-8);
        }
        if (y.size() < 1E-8) {
            y.expand(1E-8);
        }
        if (z.size() < 1E-8) {
            z.expand(1E-8);
        }
    }

    // define in terms of multiple bBoxes
    public AABB(AABB box0, AABB box1) {
        x = new Interval(box0.x, box1.x);
        y = new Interval(box0.y, box1.y);
        z = new Interval(box0.z, box1.z);
    }

    // converts integer 0-2 to axis x-z
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

    // returns largest axis as integer 0-2
    public int largestAxis() {
        if (x.size() > y.size() && x.size() > z.size()) {
            return 0; // x
        }
        if (y.size() > z.size()) {
            return 1; // y
        }
        return 2; // z
    }

    // ray slab intersection
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        Vec3 rayOrigin = r.origin();
        Vec3 rayDir = r.direction();

        for (int axis = 0; axis < 3; axis++) {
            // calculate t value for possible intersection points
            Interval axIn = axisInterval(axis);
            double axDirInv = 1.0 / rayDir.e[axis];

            double t0 = (axIn.min - rayOrigin.e[axis]) * axDirInv; // low
            double t1 = (axIn.max - rayOrigin.e[axis]) * axDirInv; // high

            // reduces size of interval if an intersection is found inside it
            if (t0 < t1) {
                if (t0 > tInterval.min) {
                    tInterval.setMin(t0);
                }
                if (t1 < tInterval.max) {
                    tInterval.setMax(t1);
                } 
            } else {
                if (t1 > tInterval.min) {
                    tInterval.setMin(t1);
                }
                if (t0 < tInterval.max) {
                    tInterval.setMax(t0);
                }
            }

            if (tInterval.max <= tInterval.min) {
                return false;
            }
        }

        return true;
    }

    public static AABB empty() {
        Interval empty = Interval.empty();
        return new AABB(empty, empty, empty);
    }

    public static AABB universe() {
        Interval universe = Interval.universe();
        return new AABB(universe, universe, universe);
    }
}
