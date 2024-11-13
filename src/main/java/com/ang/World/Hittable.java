package com.ang.World;

import com.ang.Utils.AABB;
import com.ang.Utils.HitRecord;
import com.ang.Utils.Interval;
import com.ang.Utils.Ray;

public abstract class Hittable {
    public AABB bBox() {
        return new AABB();
    };

    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return false;
    }
}
