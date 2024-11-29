package com.ang.Hittable.Compound;

import com.ang.Hittable.Hittable;
import com.ang.Hittable.HittableList;
import com.ang.Hittable.Primitive.Tri;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

/*
 * Implemented as a HittableList of Tri primitives loaded from an obj file.
 */
public class Mesh extends Hittable {
    private HittableList tris;

    // vertex data, vertes normal data, vertex indices, normal indices
    public Mesh(Vec3[] vd, Vec3[] nd, int[][] vi, int[][] ni, Material mat) {
        tris = new HittableList(vi.length);

        for (int i = 0; i < vi.length; i++) {
            // defines triangles with calculated normals based on data
            
            // verticies
            Vec3 a = vd[vi[i][0]];
            Vec3 b = vd[vi[i][1]];
            Vec3 c = vd[vi[i][2]];

            // vertex normals
            Vec3 vna = nd[ni[i][0]];
            Vec3 vnb = nd[ni[i][1]];
            Vec3 vnc = nd[ni[i][2]];

            tris.add(new Tri(a, b, c, vna, vnb, vnc, mat));
        }
    }

    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return tris.hit(r, tInterval, rec);
    }
}
