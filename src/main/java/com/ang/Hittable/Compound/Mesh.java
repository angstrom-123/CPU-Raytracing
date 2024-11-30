package com.ang.Hittable.Compound;

import com.ang.Hittable.Hittable;
import com.ang.Hittable.HittableList;
import com.ang.Hittable.Primitive.Tri;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

/**
 * Compound hittables are hittables that are made of primitives (usually tris).
 * A hittable representing a mesh that is imported from a .obj file. It is 
 * made of tris that are held in a HittableList.
 */
public class Mesh extends Hittable {
    private HittableList tris;

    /**
     * Constructs the mesh using data from a .obj file.
     * @param vd the array containing position vectors of all verticies.
     * @param nd the array containing vectors representing all vertex normals.
     * @param vi the array containing sets of 3 indices of vectors from
     *           { @param vd } to be joined into a tri.
     * @param ni the array containing sets of 3 indices of normals from
     *           { @param nd } to be assigned to the vertices in { @param vi }
     * @param mat the material to be assigned to each tri in the mesh.
     */
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

    /**
     * Checks for hits with the HittableList tris.
     * @param r the ray for which to check for intersections.
     * @param tInterval the interval of t values along the ray for which to 
     *                  check for intersections.
     * @param rec a HitRecord to be passed to the hittable tris held in quads.
     * @return {@code true} if a hit is detected against any of the tris in the
     *         in the hittable list else {@code false}.
     */
    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return tris.hit(r, tInterval, rec);
    }
}
