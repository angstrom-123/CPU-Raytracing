package com.ang.Hittable;

import com.ang.AABB.AABB;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;

/**
 * Base class for all hittables.
 */
public abstract class Hittable {
    /**
     * @return a new AABB (bBox function is undefined).
     */
    public AABB bBox() {
        return new AABB();
    };

    /**
     * Base hit function to be overriden by hittables.
     * @param r the ray to be tested for intersection.
     * @param tInterval interval of t values along  the ray in which to test 
     *                  for intersections.
     * @param rec a HitRecord where information about a potential hit is stored.
     * @return {@code false} (hit function is undefined).
     */
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return false;
    }
}
