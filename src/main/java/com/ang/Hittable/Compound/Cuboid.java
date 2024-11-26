package com.ang.Hittable.Compound;

import com.ang.Hittable.Primitive.Tri;
import com.ang.Hittable.Hittable;
import com.ang.Hittable.HittableList;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vector3;

import com.ang.Global;
import com.ang.Hittable.Primitive.Sphere;
import com.ang.Material.Lambertian;

public class Cuboid extends Hittable {
    private HittableList quads;

    // defined in terms of bounding corners
    public Cuboid(Vector3 a, Vector3 b, Material mat) {
        quads = new HittableList(6);

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

    public Cuboid(Vector3 a, Vector3 dx, Vector3 dy, Vector3 dz, Material mat) {
        quads = new HittableList(6);

        Vector3 b = a.add(dx);
        Vector3 c = a.add(dx).add(dz);
        Vector3 d = a.add(dz);
        Vector3 e = a.add(dy);
        Vector3 f = a.add(dx).add(dy);
        Vector3 g = a.add(dx).add(dz).add(dy);
        Vector3 h = a.add(dz).add(dy);

        quads.add(new Quad(a, b, c, d, mat));
        quads.add(new Quad(e, f, g, h, mat));
        quads.add(new Quad(d, a, e, h, mat));
        quads.add(new Quad(b, c, g, f, mat));//
        quads.add(new Quad(a, b, f, e, mat));
        quads.add(new Quad(c, d, h, g, mat));
    }

    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return quads.hit(r, tInterval, rec);
    }
}
