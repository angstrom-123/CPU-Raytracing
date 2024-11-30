package com.ang.AABB;

import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

/**
 * Axis-aligned bounding box. Used for BVH optimisation. Using axis aligned as
 * computing intersection can be done using the slab method which is extremely
 * cheap and simple.
 */
public class AABB {
    public Interval x, y, z; // intervals along each axis

    /**
     * Empty constructor defining each axis interval as a new Interval().
     */
    public AABB() {
        x = new Interval();
        y = new Interval();
        z = new Interval();
    }

    /**
     * Constructs AABB about 3 intervals.
     * @param x interval representing bounds on x axis.
     * @param y interval representing bounds on y axis.
     * @param z interval representing bounds on z axis.
     */
    public AABB(Interval x, Interval y, Interval z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs an AABB using 2 opposing position vectors.
     * @param a the first corner of the box.
     * @param b the opposing corner of the box.
     * The maximum and minimum bounds are calculated for each component and 
     * converted to Interval().
     */
    public AABB(Vec3 a, Vec3 b) {
        x = (a.x() <= b.x()) 
        ? new Interval(a.x(), b.x()) 
        : new Interval(b.x(), a.x());

        y = (a.y() <= b.y()) 
        ? new Interval(a.y(), b.y()) 
        : new Interval(b.y(), a.y());

        z = (a.z() <= b.z()) 
        ? new Interval(a.z(), b.z()) 
        : new Interval(b.z(), a.z());

        if (x.size() < 1E-8) { x.expand(1E-8); }
        if (y.size() < 1E-8) { y.expand(1E-8); }
        if (z.size() < 1E-8) { z.expand(1E-8); }
    }

    /**
     * Constructs new AABB to enclose 2 AABB's.
     * @param box0 the first AABB to enclose.
     * @param box1 the secong AABB to enclose.
     */
    public AABB(AABB box0, AABB box1) {
        x = new Interval(box0.x, box1.x);
        y = new Interval(box0.y, box1.y);
        z = new Interval(box0.z, box1.z);
    }

    /**
     * Converts an integer to an axis
     * @param n integer 0, 1, or 2 to be converted to an axis
     * @return Interval() representing an axis of the AABB, defaulting to z axis
     */
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

    /**
     * Determines the largest axis of the AABB
     * @return an integer 0 or 1 or 2 representing an axis of the AABB
     */
    public int largestAxis() {
        if (x.size() > y.size() && x.size() > z.size()) {
            return 0;
        }
        if (y.size() > z.size()) {
            return 1;
        }
        return 2;
    }

    /**
     * Calculates ray / slab intersection with the AABB within an interval. If 
     * there is an intersection then the tInerval is reduced to only include 
     * points closer to the camera than the hit as any further hits become
     * impossible.
     * @param r the ray to be tested for intersection.
     * @param tInterval interval of t values along  the ray in which to test 
     *                  for intersections.
     * @param rec a HitRecord, not used in this function but necessary for 
     *            compatibility with hit() functions of hittable objects.
     * @returns {@code true} if there is an intersection within the range else
     *          {@code false}.
     */
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        Vec3 rayOrigin = r.origin();
        Vec3 rayDir = r.direction();

        for (int axis = 0; axis < 3; axis++) {
            Interval axIn = axisInterval(axis);
            double axDirInv = 1.0 / rayDir.e[axis];

            double t0 = (axIn.min - rayOrigin.e[axis]) * axDirInv; // low
            double t1 = (axIn.max - rayOrigin.e[axis]) * axDirInv; // high

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

    /**
     * Constructing a new empty AABB.
     * @return new AABB with each axis interval set as infinitely small.
     */
    public static AABB empty() {
        Interval empty = Interval.empty();
        return new AABB(empty, empty, empty);
    }

    /**
     * Constructing a new AABB spanning an infinite amount.
     * @return new AABB with each axis interval set as infinitely big.
     */
    public static AABB universe() {
        Interval universe = Interval.universe();
        return new AABB(universe, universe, universe);
    }
}
