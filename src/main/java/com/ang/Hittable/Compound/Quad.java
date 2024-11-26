package com.ang.Hittable.Compound;

import com.ang.Hittable.Hittable;
import com.ang.Hittable.HittableList;
import com.ang.Hittable.Primitive.Tri;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vector3;

public class Quad extends Hittable {
    private HittableList tris;

    // abcd must be defined counter clockwise to have correct normals
    // this is a result of winding order and is default in 3d graphics programs
    public Quad(Vector3 a, Vector3 b, Vector3 c, Vector3 d, Material mat) {
        tris = new HittableList(2);

        tris.add(new Tri(a, b, c, mat));
        tris.add(new Tri(a, c, d, mat));
    }

    // define in terms of point and directions
    public Quad(Vector3 a, Vector3 ab, Vector3 ad, Material mat) {
        tris = new HittableList(2);

        Vector3 b = a.add(ab);
        Vector3 c = b.add(ad);
        Vector3 d = a.add(ad);

        tris.add(new Tri(a, b, c, mat));
        tris.add(new Tri(a, c, d, mat));
    }

    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return tris.hit(r, tInterval, rec);
    }
}
