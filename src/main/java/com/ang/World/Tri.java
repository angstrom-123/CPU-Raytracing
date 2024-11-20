package com.ang.World;

import com.ang.Materials.Material;
import com.ang.Utils.HitRecord;
import com.ang.Utils.Interval;
import com.ang.Utils.Ray;
import com.ang.Utils.Vector3;

public class Tri extends Hittable{
    private Vector3 a, b, c;
    private Vector3 ab, ac, ba, bc, ca, cb;
    private Vector3 na, nb, nc;
    private Vector3 normal; 

    private AABB bBox;
    private Material mat;

    public Tri(Vector3 a, Vector3 b, Vector3 c, Vector3 na, Vector3 nb, Vector3 nc, Material mat) {
        this.a = a;
        this.b = b;
        this.c = c;

        this.na = na;
        this.nb = nb;
        this.nc = nc;

        this.mat = mat;

        ab = b.subtract(a);
        ac = c.subtract(a);

        ba = a.subtract(b);
        bc = c.subtract(b);

        ca = a.subtract(c);
        cb = b.subtract(c);

        // normal = ((na.unitVector()).add(nb.unitVector()).add(nc.unitVector())).divide(3); //.unitVector();    
        Vector3 faceNormal = Vector3.cross(ab.unitVector(), ac.unitVector()).unitVector();

        double dot = Vector3.dot(faceNormal, na.unitVector());

        if (dot < 0) {
            normal = faceNormal;
        } else {
            normal = faceNormal.negative();
        }

        // normal = faceNormal;
        // normal = (na.unitVector().add(nb.unitVector().add(nc.unitVector()))).divide(3);


        double minX, minY, minZ;
        double maxX, maxY, maxZ;
        
        minX = Math.min(Math.min(a.x(), b.x()), c.x());
        minY = Math.min(Math.min(a.y(), b.y()), c.y());
        minZ = Math.min(Math.min(a.z(), b.z()), c.z());

        maxX = Math.max(Math.max(a.x(), b.x()), c.x());
        maxY = Math.max(Math.max(a.y(), b.y()), c.y());
        maxZ = Math.max(Math.max(a.z(), b.z()), c.z());

        // bBox = new AABB(new Vector3(minX, minY, minZ), new Vector3(maxX, maxY, maxZ));
        bBox = AABB.universe();
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
        
        // normal to plane between a, o, d
        Vector3 ao = r.origin().subtract(a);
        Vector3 daoPN = Vector3.cross(ao, r.direction());

        double determinant = -Vector3.dot(r.direction(), normal);
        double invDet = 1 / determinant;

        double dst = Vector3.dot(ao, normal) * invDet;
        double u = Vector3.dot(ac, daoPN) * invDet;
        double v = -Vector3.dot(ab, daoPN) * invDet;
        double w = 1 - u - v;

        if (determinant < 1E-6 || u <= 0 || v <= 0 || w <= 0 || dst < 0) {
            return false;
        }
        rec.t = dst;
        rec.p = r.at(rec.t);

        rec.setFaceNormal(r, (na.multiply(w)).add(nb.multiply(u)).add(nc.multiply(v)));
        rec.mat = mat;

        return true;
    }
}
