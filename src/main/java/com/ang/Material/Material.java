package com.ang.Material;

import com.ang.Util.HitRecord;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vector3;

public abstract class Material {
    public Material material;

    public Vector3 emitted(double u, double v, Vector3 p) {
        return new Vector3(0,0,0);
    }

    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        return false;
    }
}
