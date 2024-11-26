package com.ang.Hittable.Primitive;

import com.ang.AABB.AABB;
import com.ang.Hittable.Hittable;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vector3;

public class Tri extends Hittable{
    private Vector3 a;
    private Vector3 ab, ac;
    private Vector3 na, nb, nc;
    private Vector3 normal; 
    private AABB bBox;
    private Material mat;

    public Tri(Vector3 a, Vector3 b, Vector3 c, Material mat) {
        this(a, b, c, Vector3.cross(b.subtract(a), c.subtract(a)), Vector3.cross(b.subtract(a), c.subtract(a)), Vector3.cross(b.subtract(a), c.subtract(a)), mat);
    }

    public Tri(Vector3 a, Vector3 b, Vector3 c, Vector3 na, Vector3 nb, Vector3 nc, Material mat) {
        this.a = a;

        this.na = na;
        this.nb = nb;
        this.nc = nc;

        this.mat = mat;

        ab = b.subtract(a);
        ac = c.subtract(a);

        // calculate normal
        normal = Vector3.cross(ab, ac);

        double minX, minY, minZ;
        double maxX, maxY, maxZ;
        
        minX = Math.min(Math.min(a.x(), b.x()), c.x()) - 0.1;
        minY = Math.min(Math.min(a.y(), b.y()), c.y()) - 0.1;
        minZ = Math.min(Math.min(a.z(), b.z()), c.z()) - 0.1;

        maxX = Math.max(Math.max(a.x(), b.x()), c.x()) + 0.1;
        maxY = Math.max(Math.max(a.y(), b.y()), c.y()) + 0.1;
        maxZ = Math.max(Math.max(a.z(), b.z()), c.z()) + 0.1;

        Vector3 min = new Vector3(minX, minY, minZ);
        Vector3 max = new Vector3(maxX, maxY, maxZ);

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

        Vector3 ao = r.origin().subtract(a);
        Vector3 dao = Vector3.cross(ao, r.direction());

        double determinant = -Vector3.dot(r.direction(), normal);
        double invDet = 1 / determinant;

        double dst = Vector3.dot(ao, normal) * invDet;
        double u = Vector3.dot(ac, dao) * invDet;
        double v = -Vector3.dot(ab, dao) * invDet;
        double w = 1 - u - v;

        if (!(determinant >= 1E-6 && dst >= 0 && u >= 0 && v >= 0 && w >= 0)) {
            return false;
        }

        rec.t = dst;
        rec.p = r.at(rec.t);
        rec.setFaceNormal(r, ((na.multiply(w)).add(nb.multiply(u).add(nc.multiply(v)))).unitVector());
        rec.mat = mat;

        return true;
    }
}
