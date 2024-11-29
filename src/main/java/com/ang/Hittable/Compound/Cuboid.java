package com.ang.Hittable.Compound;

import com.ang.Hittable.Hittable;
import com.ang.Hittable.HittableList;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

/*
 * Implemented as a HittableList of Quad compounds.
 */
public class Cuboid extends Hittable {
    private HittableList quads;

    // defined in terms of bounding corners for axis-aligned boxes
    public Cuboid(Vec3 a, Vec3 b, Material mat) {
        quads = new HittableList(6);

        // calculate bounds of cube from corners
        Vec3 min = new Vec3(
            Math.min(a.x(), b.x()), 
            Math.min(a.y(), b.y()), 
            Math.min(a.z(), b.z()));
        Vec3 max = new Vec3(
            Math.max(a.x(), b.x()),
            Math.max(a.y(), b.y()),   
            Math.max(a.z(), b.z()));
        
        // vectors between verticies
        Vec3 dx = new Vec3((max.x() - min.x()), 0.0, 0.0);
        Vec3 dy = new Vec3(0.0, (max.y() - min.y()), 0.0);
        Vec3 dz = new Vec3(0.0, 0.0, (max.z() - min.z()));

        // z coord is negative in coordinate system
        dz = dz.negative();

        // add quads to hittable list
        // top, bottom
        quads.add(new Quad(new Vec3(min.x(), max.y(), max.z()), dx, dz, mat));
        quads.add(new Quad(new Vec3(min.x(), min.y(), max.z()), dx, dz, mat));
        // left, right
        quads.add(new Quad(new Vec3(min.x(), min.y(), max.z()), dz, dy, mat)); 
        quads.add(new Quad(new Vec3(max.x(), min.y(), max.z()), dz, dy, mat)); 
        // front, back
        quads.add(new Quad(new Vec3(min.x(), min.y(), min.z()), dx, dy, mat));
        quads.add(new Quad(new Vec3(min.x(), min.y(), max.z()), dx, dy, mat)); 
    }
    /* 
     * Defined in terms of one vertex and relative vectors to other verticies.
     * Can be used for cuboids in any orientation.
     */
    public Cuboid(Vec3 a, Vec3 dx, Vec3 dy, Vec3 dz, Material mat) {
        quads = new HittableList(6);

        // calculate position vectors of verticies
        Vec3 b = a.add(dx);
        Vec3 c = a.add(dx).add(dz);
        Vec3 d = a.add(dz);
        Vec3 e = a.add(dy);
        Vec3 f = a.add(dx).add(dy);
        Vec3 g = a.add(dx).add(dz).add(dy);
        Vec3 h = a.add(dz).add(dy);

        // add quads to hittable list
        // top, bottom
        quads.add(new Quad(e, f, g, h, mat));
        quads.add(new Quad(a, b, c, d, mat));
        // left, right
        quads.add(new Quad(d, a, e, h, mat));
        quads.add(new Quad(b, c, g, f, mat));
        // front, back
        quads.add(new Quad(a, b, f, e, mat));
        quads.add(new Quad(c, d, h, g, mat));
    }

    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return quads.hit(r, tInterval, rec);
    }
}
