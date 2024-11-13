package com.ang.Materials;

import com.ang.Utils.HitRecord;
import com.ang.Utils.Ray;
import com.ang.Utils.RayTracker;
import com.ang.Utils.Vector3;

public class Dielectric extends Material{
    private double refractiveIndex;
    private Vector3 albedo;

    public Dielectric(Vector3 albedo, double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
        this.albedo = albedo;
    }

    public Dielectric(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
        this.albedo = new Vector3(1.0,1.0,1.0);
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        double ri;
        if (rec.frontFace) {
            ri = 1.0 / refractiveIndex;
        } else {
            ri = refractiveIndex;
        }
        Vector3 unitDirection = rIn.direction().unitVector();

        double cosTheta = Math.min(Vector3.dot(unitDirection.negative(), rec.normal), 1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta*cosTheta);

        Vector3 direction;

        // snells law
        // glancing rays reflect
        // mostly direct rays refract
        if (ri * sinTheta > 1.0 || reflectance(cosTheta, ri) > Math.random()) {
            // must reflect 
            direction = Vector3.reflect(unitDirection, rec.normal);
        } else {
            // can refract
            direction = Vector3.refract(unitDirection, rec.normal, ri);
        }

        rt.set(albedo, new Ray(rec.p, direction));

        return true;
    }

    private double reflectance(double cosTheta, double ri) {
        // schlick's approximation of reflectance coefficient
        double r0 = (1 - ri) / (1 + ri);
        r0 = r0 * r0;
        return r0 + (1 - r0) * Math.pow((1 - cosTheta), 5.0);
    }
}
