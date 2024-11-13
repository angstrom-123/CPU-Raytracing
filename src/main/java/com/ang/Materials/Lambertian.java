package com.ang.Materials;

import com.ang.Utils.HitRecord;
import com.ang.Utils.Ray;
import com.ang.Utils.RayTracker;
import com.ang.Utils.Vector3;

public class Lambertian extends Material{
    private Vector3 albedo;

    public Lambertian(Vector3 albedo) {
        this.albedo = albedo;
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        Vector3 scatterDirection = rec.normal.add(Vector3.randomUnitVector());
        
        // if scatter direction is oposite to normal, it becomes almost 0,0,0
        if (scatterDirection.nearZero()) {
            scatterDirection = rec.normal;
        }

        rt.set(albedo, new Ray(rec.p, scatterDirection));
        return true;
    }
}
