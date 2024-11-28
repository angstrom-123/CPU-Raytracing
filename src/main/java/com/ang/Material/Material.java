package com.ang.Material;

import com.ang.Util.HitRecord;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vec3;

public abstract class Material {
    public Material material;

    public Vec3 emitted(double u, double v, Vec3 p) {
        return new Vec3(0.0, 0.0, 0.0);
    }

    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        return false;
    }
}
