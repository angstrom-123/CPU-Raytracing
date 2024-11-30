package com.ang.Material;

import com.ang.Util.HitRecord;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vec3;

/*
 * Glass-like material with transparency and refraction.
 */
public class Dielectric extends Material{
    private double refractiveIndex;
    private Vec3 albedo;

    /**
     * Constructs the material with a tint colour.
     * @param albedo the colour to tint the material with.
     * @param refractiveIndex the index of refraction of the material (air = 1).
     */
    public Dielectric(Vec3 albedo, double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
        this.albedo = albedo;
    }

    /**
     * Constructs the material with no tint.
     * @param refractiveIndex the index of refraction of the material (air = 1).
     */
    public Dielectric(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
        this.albedo = new Vec3(1.0, 1.0, 1.0); // default to white (none).
    }

    /**
     * Calculates the direction in which a ray should bounce after hitting the
     * surface of the hittable with this material at a certain point.
     * @param rIn the ray that is hitting the hittable with this material.
     * @param rec the HitRecord storing information about the ray's intersection
     *            with the hittable that has this material.
     * @param rt a RayTracker that will store information about the new ray that
     *           is cast from the intersection point to simulate a bounce.
     * @return {@code true}
     */
    @Override
    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        // refractive index is inverted if the ray is leaving the material
        double ri = (rec.frontFace) 
        ? (1.0 / refractiveIndex) 
        : (refractiveIndex);

        Vec3 dir = rIn.direction().unitVector();

        double cosTheta = Math.min(Vec3.dot(dir.negative(), rec.normal),1);
        double sinTheta = Math.sqrt(1 - cosTheta*cosTheta);

        Vec3 direction;

        // Snell's law : glancing rays reflect, mostly direct rays refract
        double choose = Math.random();
        if ((ri * sinTheta > 1.0) || (reflectance(cosTheta, ri) > choose)) {
            direction = Vec3.reflect(dir, rec.normal);
        } else {
            direction = Vec3.refract(dir, rec.normal, ri);
        }

        rt.set(albedo, new Ray(rec.p, direction));
        return true;
    }

    /**
     * Calculates the reflectance coefficient using Schlik's approximation
     * @param cosTheta the cosine of the incident angle of the ray.
     * @param ri the refractive index to use for the calculation.
     */
    private double reflectance(double cosTheta, double ri) {
        double r0 = (1 - ri) / (1 + ri);
        r0 = r0 * r0;
        return r0 + (1 - r0) * Math.pow((1 - cosTheta), 5.0);
    }
}
