package com.ang.Hittable.Primitive;

import com.ang.AABB.AABB;
import com.ang.Hittable.Hittable;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

// using counter clockwise winding order for outward normals
// verticies must be defined counter clockwise to ensure correct normal vector

// examples of correct winding:
//  2         1
//  |\        |\
//  | \       | \
//  |  \      |  \
//  +---+     +---+
// 0     1   2     0

public class Tri extends Hittable{
    private Vec3     a;
    private Vec3     ab, ac;
    private Vec3     na, nb, nc;
    private Vec3     normal; 
    private AABB     bBox;
    private Material mat;

    // auto compute normals
    public Tri(Vec3 a, Vec3 b, Vec3 c, Material mat) {
        this(
            a, b, c, 
            Vec3.cross(b.subtract(a), c.subtract(a)), 
            Vec3.cross(b.subtract(a), c.subtract(a)), 
            Vec3.cross(b.subtract(a), c.subtract(a)), 
            mat);
    }

    // use pre-computed normals
    public Tri(Vec3 a, Vec3 b, Vec3 c, 
    Vec3 na, Vec3 nb, Vec3 nc, Material mat) {
        this.a = a;

        this.na = na;
        this.nb = nb;
        this.nc = nc;

        this.mat = mat;

        // calculate default normal
        ab = b.subtract(a);
        ac = c.subtract(a);

        normal = Vec3.cross(ab, ac);

        // calculate bounding box
        double minX, minY, minZ;
        double maxX, maxY, maxZ;
        
        // expand each component to account for infinitely thin edge
        minX = Math.min(Math.min(a.x(), b.x()), c.x()) - 0.1;
        minY = Math.min(Math.min(a.y(), b.y()), c.y()) - 0.1;
        minZ = Math.min(Math.min(a.z(), b.z()), c.z()) - 0.1;

        maxX = Math.max(Math.max(a.x(), b.x()), c.x()) + 0.1;
        maxY = Math.max(Math.max(a.y(), b.y()), c.y()) + 0.1;
        maxZ = Math.max(Math.max(a.z(), b.z()), c.z()) + 0.1;

        Vec3 min = new Vec3(minX, minY, minZ);
        Vec3 max = new Vec3(maxX, maxY, maxZ);

        bBox = new AABB(min, max);
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

        // calculating determinant
        Vec3 ao = r.origin().subtract(a);
        Vec3 dao = Vec3.cross(ao, r.direction());

        double determinant = -Vec3.dot(r.direction(), normal);
        double invDet = 1 / determinant;

        // calculating barycentric coords of intersection
        double dst = Vec3.dot(ao, normal) * invDet;
        double u = Vec3.dot(ac, dao) * invDet;
        double v = -Vec3.dot(ab, dao) * invDet;
        double w = 1 - u - v;

        // no colision
        if (!((determinant >= 1E-6) && (dst >= 0) 
        && (u >= 0) && (v >= 0) && (w >= 0))) {
            return false;
        }

        // record hit
        rec.t = dst;
        rec.p = r.at(rec.t);
        Vec3 outNormal = na.multiply(w).add(nb.multiply(u).add(nc.multiply(v)));
        rec.setFaceNormal(r, outNormal.unitVector());
        rec.mat = mat;

        return true;
    }
}
