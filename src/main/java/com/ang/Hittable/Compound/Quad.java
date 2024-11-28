package com.ang.Hittable.Compound;

import com.ang.Hittable.Hittable;
import com.ang.Hittable.HittableList;
import com.ang.Hittable.Primitive.Tri;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

// using counter clockwise winding order: see Tri.java
public class Quad extends Hittable {
    private HittableList tris;

    // define in terms of all verticies
    public Quad(Vec3 a, Vec3 b, Vec3 c, Vec3 d, Material mat) {
        tris = new HittableList(2);

        // 2 coplanar triangles making up quad
        tris.add(new Tri(a, b, c, mat));
        tris.add(new Tri(a, c, d, mat));
    }

    // define in terms of vertex coord and vectors along edges
    public Quad(Vec3 a, Vec3 ab, Vec3 ad, Material mat) {
        tris = new HittableList(2);

        // verticies
        Vec3 b = a.add(ab);
        Vec3 c = b.add(ad);
        Vec3 d = a.add(ad);

        // 2 coplanar triangles making up quad
        tris.add(new Tri(a, b, c, mat));
        tris.add(new Tri(a, c, d, mat));
    }

    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return tris.hit(r, tInterval, rec);
    }
}
