package com.ang.Materials;

import com.ang.Utils.HitRecord;
import com.ang.Utils.Ray;
import com.ang.Utils.RayTracker;
import com.ang.Utils.Vector3;

public abstract class Material {
    public Material material;

    public Vector3 emitted(double u, double v, Vector3 p) {
        return new Vector3(0,0,0);
    }

    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        return false;
    }
}
