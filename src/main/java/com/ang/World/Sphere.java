package com.ang.World;

import com.ang.Global;
import com.ang.Materials.Material;
import com.ang.Utils.HitRecord;
import com.ang.Utils.Interval;
import com.ang.Utils.Ray;
import com.ang.Utils.Vector3;

public class Sphere extends Hittable {
    private Vector3 centre;
    private double radius;
    private Material mat;
    private AABB bBox;

    public Sphere(Vector3 centre, double radius, Material mat) {
        this.centre = centre;
        this.radius = Math.max(radius, 0);
        this.mat = mat;

        // init bounding box
        Vector3 radiusVector = new Vector3(radius, radius, radius);
        bBox = new AABB(centre.subtract(radiusVector), centre.add(radiusVector));
    }

    @Override 
    public AABB bBox() {
        return bBox;
    }

    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        // vector centre C and unknown vector point P of intersection
        // (C - P) dot (C - P) = r^2
        // the vector equation = Origin + direction * timestep
        // sub this in for the unknown point p
        // (C - o + dt) dot (C - o + dt) = r^2
        // C - o is the vector from origin of ray (cam) to centre of sphere
        // this is called o2c, we can sub this in
        // (o2c + dt) dot (o2c + dt) = r^2
        // can be expanded to
        // (o2c)^2 + 2*o2c*dt + (dt)^2 = r^2
        // organize to solve for t
        // t^2(d^2) + 2t(o2c*d) + ((o2c)^2 - r^2) = 0

        if (!bBox.hit(r, tInterval, rec)) {
            return false;
        }

        Vector3 o2c = centre.subtract(r.origin());
        // d^2
        double a = Vector3.dot(r.direction(), r.direction());
        // 2 * o2c * d (used -2 for simplicity in solving)
        double b = -2.0 * Vector3.dot(r.direction(), o2c);
        // ((o2c)^2 - r^2)
        double c = Vector3.dot(o2c, o2c) - radius * radius;

        double discriminant = b*b - 4*a*c;
        if (discriminant < 0) {
            return false;
        }

        // plus minus within range
        double root = (-b - Math.sqrt(discriminant)) / (2*a);
        if (!tInterval.surrounds(root)) {
            root = (-b + Math.sqrt(discriminant)) / (2*a);
            if (!tInterval.surrounds(root)) {
                return false;
            }
        }

        // record hit
        rec.t = root;
        rec.p = r.at(rec.t);

        Vector3 outwardNormal = (rec.p.subtract(centre)).divide(radius);
        rec.setFaceNormal(r, outwardNormal);

        rec.mat = mat;
        
        Global.hits++;
        return true;
    }
}
