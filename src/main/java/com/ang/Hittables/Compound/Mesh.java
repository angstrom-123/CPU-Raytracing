package com.ang.Hittables.Compound;

import com.ang.Hittables.Hittable;
import com.ang.Hittables.HittableList;
import com.ang.Hittables.Primitive.Tri;
import com.ang.Material.*;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vector3;

public class Mesh extends Hittable {
    private HittableList tris;

    public Mesh(Vector3[] vd, Vector3[] nd, int[][] fi, int[][] ni, Material mat) {
        tris = new HittableList(fi.length);

        for (int i = 0; i < fi.length; i++) {
            Vector3 a = vd[fi[i][0]];
            Vector3 b = vd[fi[i][1]];
            Vector3 c = vd[fi[i][2]];

            Vector3 vna = nd[ni[i][0]];
            Vector3 vnb = nd[ni[i][1]];
            Vector3 vnc = nd[ni[i][2]];

            tris.add(new Tri(a, b, c, vna, vnb, vnc, mat));
        }
    }

    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return tris.hit(r, tInterval, rec);
    }
}
