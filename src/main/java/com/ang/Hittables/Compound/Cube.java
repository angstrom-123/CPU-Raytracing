package com.ang.Hittables.Compound;

import com.ang.Hittables.Hittable;
import com.ang.Hittables.HittableList;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vector3;

public class Cube extends Hittable {
    private HittableList quads;

    // defined in terms of bounding corners
    public Cube(Vector3 a, Vector3 b, Material mat) {
        Vector3 min = new Vector3(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()));
        Vector3 max = new Vector3(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()));
        
        Vector3 dx = new Vector3(max.x() - min.x(), 0, 0);
        Vector3 dy = new Vector3(0, max.y() - min.y(), 0);
        Vector3 dz = new Vector3(0, 0, max.z() - min.z());

        // need to test this still
        quads.add(new Quad(new Vector3(min.x(), max.y(), max.z()), dx, dz.negative(), mat)); // top
        quads.add(new Quad(new Vector3(min.x(), min.y(), max.z()), dx, dz.negative(), mat)); // bottom
        quads.add(new Quad(new Vector3(min.x(), min.y(), max.z()), dz.negative(), dy, mat)); // left
        quads.add(new Quad(new Vector3(max.x(), min.y(), max.z()), dz.negative(), dy, mat)); // right
        quads.add(new Quad(new Vector3(min.x(), min.y(), min.z()), dx, dy, mat)); // back
        quads.add(new Quad(new Vector3(min.x(), min.y(), max.z()), dx, dy, mat)); // front
    }

    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return quads.hit(r, tInterval, rec);
    }
}
