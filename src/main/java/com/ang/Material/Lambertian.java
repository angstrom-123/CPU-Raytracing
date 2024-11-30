package com.ang.Material;

import com.ang.Texture.SolidColour;
import com.ang.Texture.Texture;
import com.ang.Util.HitRecord;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vec3;

/**
 * Diffuse material using Lambertian cosine weighting.
 */
public class Lambertian extends Material{
    private Texture tex;

    /**
     * Constructs the material with a solid base colour.
     * @param albedo the vector representing the rgb colour of the material.
     */
    public Lambertian(Vec3 albedo) {
        tex = new SolidColour(albedo);
    }

    /**
     * Constructs the material with a texture as the base colour.
     * @param tex the texture to use for the base colour of the material.
     */
    public Lambertian(Texture tex) {
        this.tex = tex;
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
        Vec3 direction = rec.normal.add(Vec3.randomUnitVector());
        
        // if scatter direction is opposite to normal it can underflow
        if (direction.nearZero()) {
            direction = rec.normal;
        }

        rt.set(tex.value(rec.u, rec.v, rec.p), new Ray(rec.p, direction));
        return true;
    }
}
