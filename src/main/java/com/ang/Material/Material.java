package com.ang.Material;

import com.ang.Util.HitRecord;
import com.ang.Util.Ray;
import com.ang.Util.RayTracker;
import com.ang.Util.Vec3;

/**
 * Base class for all materials.
 */
public abstract class Material {
    public Material material;

    /**
     * Base emission function to be overriden by materials.
     * @param u first texture-space coordinate to be sampled.
     * @param v second texture-space coordinate to be sampled.
     * @param p position vector representing the point of intersection with the 
     *          hittable that has this material.
     * @return vector representing rgb colour of black (emission function is 
     *         undefined).
     */
    public Vec3 emitted(double u, double v, Vec3 p) {
        return new Vec3(0.0, 0.0, 0.0);
    }

    /** 
     * Base scatter function to be overriden by materials.
     * @param rIn the ray that is hitting the hittable with this material.
     * @param rec the HitRecord storing information about the ray's intersection
     *            with the hittable that has this material.
     * @param rt a RayTracker that will store information about the new ray that
     *           is cast from the intersection point to simulate a bounce.
     * @return {@code false} (scatter function is undefined).
     */
    public boolean scatter(Ray rIn, HitRecord rec, RayTracker rt) {
        return false;
    }
}
