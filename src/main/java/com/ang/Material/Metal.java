package com.ang.Material;

import com.ang.Util.HitRecord;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vec3;

/**
 * Reflective material.
 */
public class Metal extends Material {
    private Vec3 albedo;
    private double fuzziness;

    /**
     * Constructs the material with base colour and fuzziness 
     * @param albedo the base colour of the material.
     * @param fuzziness value to scale the random jittering of ray direction.
     */
    public Metal(Vec3 albedo, double fuzziness) {
        this.albedo     = albedo;
        this.fuzziness  = fuzziness;
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
        Vec3 direction = Vec3.reflect(rIn.direction(), rec.normal).unitVector();
        direction = direction.add(Vec3.randomUnitVector().multiply(fuzziness));
        
        rt.set(albedo, new Ray(rec.p, direction));
        return true;
    }
}
