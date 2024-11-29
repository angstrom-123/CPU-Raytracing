package com.ang.Hittable.Primitive;

import com.ang.Global;
import com.ang.AABB.AABB;
import com.ang.Hittable.Hittable;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

public class Sphere extends Hittable {
    private Vec3        centre;
    private Material    mat;
    private AABB        bBox;
    private double      radius;

    // define in terms of centre and radius
    public Sphere(Vec3 centre, double radius, Material mat) {
        this.centre = centre;
        this.radius = Math.max(radius, 0);
        this.mat    = mat;

        // bounding box enclosing sphere
        Vec3 radiusVector = new Vec3(radius, radius, radius);
        bBox = new AABB(
            centre.subtract(radiusVector),
            centre.add(radiusVector));
    }

    @Override 
    public AABB bBox() {
        return bBox;
    }

    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        if (!bBox.hit(r, tInterval, rec)) {
            return false;
        }

        // using discriminant and quadratic formula to find intersection point
        Vec3 o2c = centre.subtract(r.origin());

        double a = Vec3.dot(r.direction(), r.direction());
        double b = -2.0 * Vec3.dot(r.direction(), o2c);
        double c = Vec3.dot(o2c, o2c) - radius * radius;

        double discriminant = (b * b) - (4 * a * c);
        // no intersection
        if (discriminant < 0) {
            return false;
        }

        // plus minus within t range
        double root = (-b - Math.sqrt(discriminant)) / (2 * a);
        if (!tInterval.surrounds(root)) {
            root = (-b + Math.sqrt(discriminant)) / (2 * a);
            // no intersection within range
            if (!tInterval.surrounds(root)) {
                return false;
            }
        }

        // record hit
        rec.t = root;
        rec.p = r.at(rec.t);
        Vec3 outNormal = (rec.p.subtract(centre)).divide(radius);
        rec.setFaceNormal(r, outNormal);
        getUV(outNormal, rec);
        rec.mat = mat;
        
        return true;
    }

    private static void getUV(Vec3 p, HitRecord rec) {
        // calculating u v texture space coords
        double theta = Math.acos(-p.y());
        double phi = Math.atan2(-p.z(), p.x()) + Global.pi;

        rec.u = phi / (2 * Global.pi);
        rec.v = theta / Global.pi;
    }
}
