package com.ang.Materials;

import com.ang.Utils.HitRecord;
import com.ang.Utils.Ray;
import com.ang.Utils.RayTracker;
import com.ang.Utils.Vector3;

public abstract class Material {
    public Material material;

    public Vector3 getAlbedo() {
        return null;
    }

    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        return false;
    }
}
