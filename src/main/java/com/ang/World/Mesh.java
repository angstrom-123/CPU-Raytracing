package com.ang.World;

import com.ang.Materials.*;
import com.ang.Utils.HitRecord;
import com.ang.Utils.Interval;
import com.ang.Utils.Ray;
import com.ang.Utils.Vector3;
import com.ang.Global;

public class Mesh extends Hittable{
    private AABB bBox = AABB.empty();
    private Material mat;

    private Vector3[] vertexData;
    private Vector3[] normalData;
    private int[][] faceIndices;
    private int[][] normalIndices;

    private Tri[] tris;

    public Mesh(Vector3[] vd, Vector3[] nd, int[][] fi, int[][] ni, Material mat) {
        vertexData = vd;
        normalData = nd;
        faceIndices = fi;
        normalIndices = ni;
        this.mat = mat;

        tris = new Tri[fi.length];
        Material red = new Lambertian(new Vector3(1,0,0));

        for (int i = 0; i < fi.length; i++) {
            Vector3 a = vd[fi[i][0]];
            Vector3 b = vd[fi[i][1]];
            Vector3 c = vd[fi[i][2]];

            Vector3 vna = nd[ni[i][0]];
            Vector3 vnb = nd[ni[i][1]];
            Vector3 vnc = nd[ni[i][2]];

            Tri tri = new Tri(a, b, c, vna, vnb, vnc, mat);

            tris[i] = tri;
            this.bBox = new AABB(this.bBox, tri.bBox());
        }
    }

    @Override
    public AABB bBox() {
        return bBox;
        // return AABB.universe();
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
}
