package com.ang.Hittable;

import com.ang.AABB.AABB;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;

public abstract class Hittable {
    public AABB bBox() {
        return new AABB();
    };

    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return false;
    }
}
