package com.ang.Hittable.Primitive;

import com.ang.Global;
import com.ang.AABB.AABB;
import com.ang.Hittable.Hittable;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

/**
 * Primitives are atomic hittables (not made of other hittables).
 * Hittable representing a sphere.
 */
public class Sphere extends Hittable {
    private Vec3        centre;
    private Material    mat;
    private AABB        bBox;
    private double      radius;

    /**
     * Constructs a sphere around a point.
     * @param centre the position vector for the centre of the sphere.
     * @param radius the radius of the sphere to be constructed.
     * @param mat the material to be assigned to the sphere.
     */
    public Sphere(Vec3 centre, double radius, Material mat) {
        this.centre = centre;
        this.radius = Math.max(radius, 0); // radius must be positive
        this.mat    = mat;

        Vec3 radiusVector = new Vec3(radius, radius, radius);
        bBox = new AABB(
            centre.subtract(radiusVector),
            centre.add(radiusVector));
    }

    /**
     * @return this Sphere's AABB.
     */
    @Override 
    public AABB bBox() {
        return bBox;
    }

    /**
     * Checks for an intersection between a ray and the sphere using the 
     * discriminant to determine an intersection and the quadratic formula to
     * solve for the intersection point. If a hit is detected then the position
     * vector, t value along the ray, normal vector of surface, material of 
     * surface, and uv texture-space coordinates are recorded in the HitRecord.
     * @param r the ray to be tested for intersection.
     * @param tInterval interval of t values along  the ray in which to test 
     *                  for intersections.
     * @param rec a HitRecord where information about a potential hit is stored.
     * @returns {@code true} if there is an intersection within the range else
     *          {@code false}.
     */
    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        if (!bBox.hit(r, tInterval, rec)) {
            return false;
        }

        Vec3 o2c = centre.subtract(r.origin());

        double a = Vec3.dot(r.direction(), r.direction());
        double b = -2.0 * Vec3.dot(r.direction(), o2c);
        double c = Vec3.dot(o2c, o2c) - radius * radius;

        double discriminant = (b * b) - (4 * a * c);
        if (discriminant < 0) {
            return false;
        }

        // quadratic formula, testing positive and negative
        double root = (-b - Math.sqrt(discriminant)) / (2 * a);
        if (!tInterval.surrounds(root)) {
            root = (-b + Math.sqrt(discriminant)) / (2 * a);
            if (!tInterval.surrounds(root)) {
                return false;
            }
        }

        rec.t = root;
        rec.p = r.at(rec.t);
        Vec3 outNormal = (rec.p.subtract(centre)).divide(radius);
        rec.setFaceNormal(r, outNormal);
        getUV(outNormal, rec);
        rec.mat = mat;
        
        return true;
    }

    /**
     * Calculates the texture-space coordinates of a point on the sphere.
     * @param p the position vector on the sphere to be found in texture space.
     * @param rec the HitRecord that will record the texture-space coordinates.
     */
    private static void getUV(Vec3 p, HitRecord rec) {
        double theta = Math.acos(-p.y());
        double phi = Math.atan2(-p.z(), p.x()) + Global.pi;

        rec.u = phi / (2 * Global.pi);
        rec.v = theta / Global.pi;
    }
}
