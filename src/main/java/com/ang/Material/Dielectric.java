package com.ang.Material;

import com.ang.Util.HitRecord;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vec3;

/*
 * Glass - like materials
 */
public class Dielectric extends Material{
    private double refractiveIndex;
    private Vec3 albedo;

    public Dielectric(Vec3 albedo, double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
        this.albedo = albedo;
    }

    public Dielectric(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
        this.albedo = new Vec3(1.0, 1.0, 1.0);
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        // calculates relative refractive index if ray is entering or leaving
        double ri = (rec.frontFace) 
        ? (1.0 / refractiveIndex) 
        : (refractiveIndex);

        Vec3 dir = rIn.direction().unitVector();

        // values for Snell's law calculation
        double cosTheta = Math.min(Vec3.dot(dir.negative(), rec.normal),1);
        double sinTheta = Math.sqrt(1 - cosTheta*cosTheta);

        Vec3 direction;

        // Snell's law : glancing rays reflect, mostly direct rays refract
        double choose = Math.random();
        if ((ri * sinTheta > 1.0) || (reflectance(cosTheta, ri) > choose)) {
            // must reflect 
            direction = Vec3.reflect(dir, rec.normal);
        } else {
            // can refract
            direction = Vec3.refract(dir, rec.normal, ri);
        }

        // updates ray colour and scatter direction for next iteration
        rt.set(albedo, new Ray(rec.p, direction));
        return true;
    }

    private double reflectance(double cosTheta, double ri) {
        // Schlick's approximation of reflectance coefficient
        double r0 = (1 - ri) / (1 + ri);
        r0 = r0 * r0;
        return r0 + (1 - r0) * Math.pow((1 - cosTheta), 5.0);
    }
}
