package com.ang.World;

import com.ang.Materials.Material;
import com.ang.Utils.HitRecord;
import com.ang.Utils.Interval;
import com.ang.Utils.Ray;
import com.ang.Utils.Vector3;

public class Quad extends Hittable{
    private AABB bBox = AABB.empty();
    private Tri[] tris;
    private Material mat;

    public Quad(Vector3 a, Vector3 b, Vector3 c, Vector3 d, Material mat) {
        Tri t1 = new Tri(a, b, c, mat);
        Tri t2 = new Tri(a, c, d, mat);

        this.bBox = new AABB(this.bBox, t1.bBox());
        this.bBox = new AABB(this.bBox, t2.bBox());

        tris = new Tri[]{t1, t2};

        this.mat = mat;
    }

    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        if (!bBox.hit(r, tInterval, rec)) {
            return false;
        }

        HitRecord tempRec = new HitRecord();
        double closestYet = tInterval.max;
        boolean hitAnything = false;

        for (int i = 0; i < tris.length; i++) {
            if (tris[i].hit(r, new Interval(tInterval.min, closestYet), tempRec)) {
                hitAnything = true;
                closestYet = tempRec.t;
                rec.set(tempRec);
                rec.mat = tempRec.mat;
            }
        }

        return hitAnything;
    }

    @Override
    public AABB bBox() {
        return bBox;
    }
}
