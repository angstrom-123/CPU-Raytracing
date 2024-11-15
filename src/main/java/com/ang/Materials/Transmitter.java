package com.ang.Materials;

import com.ang.Utils.HitRecord;
import com.ang.Utils.Ray;
import com.ang.Utils.RayTracker;
import com.ang.Utils.Vector3;

public class Transmitter extends Material{
    private Vector3 albedo;

    public Transmitter(Vector3 albedo) {
        this.albedo = albedo;
    }

    @Override
    public Vector3 getAlbedo() {
        return albedo;
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        Vector3 scatterDirection = rIn.direction();

        rt.set(albedo, new Ray(rec.p, scatterDirection));
        return true;
    }
}
