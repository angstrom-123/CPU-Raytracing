package com.ang.Hittable.Compound;

import com.ang.Hittable.Hittable;
import com.ang.Hittable.HittableList;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

/**
 * Compound hittables are hittables that are made of primitives (usually tris).
 * A hittable representing a cuboid, constructed of 6 quads, each of which are
 * constructed of 2 tris. The quads are held in a HittableList.
 */
public class Cuboid extends Hittable {
    private HittableList quads;

    /**
     * Constructs an axis-aligned cuboid between 2 opposing position vectors.
     * @param a the first corner of the cuboid.
     * @param b the opposing corner of the cuboid.
     * @param mat the material to be assigned to each tri in the mesh.
     * Position vectors for all other points are calculated by finding the 
     * maximum and minumum bounds of the 2 inputs.
     */
    public Cuboid(Vec3 a, Vec3 b, Material mat) {
        quads = new HittableList(6);

        Vec3 min = new Vec3(
            Math.min(a.x(), b.x()), 
            Math.min(a.y(), b.y()), 
            Math.min(a.z(), b.z()));
        Vec3 max = new Vec3(
            Math.max(a.x(), b.x()),
            Math.max(a.y(), b.y()),   
            Math.max(a.z(), b.z()));
        
        Vec3 dx = new Vec3((max.x() - min.x()), 0.0, 0.0);
        Vec3 dy = new Vec3(0.0, (max.y() - min.y()), 0.0);
        Vec3 dz = new Vec3(0.0, 0.0, (max.z() - min.z()));

        dz = dz.negative(); // z axis is negative relative to camera

        // top
        quads.add(new Quad(new Vec3(min.x(), max.y(), max.z()), dx, dz, mat));
        // bottom
        quads.add(new Quad(new Vec3(min.x(), min.y(), max.z()), dx, dz, mat));
        // left
        quads.add(new Quad(new Vec3(min.x(), min.y(), max.z()), dz, dy, mat)); 
        // right
        quads.add(new Quad(new Vec3(max.x(), min.y(), max.z()), dz, dy, mat)); 
        // front
        quads.add(new Quad(new Vec3(min.x(), min.y(), min.z()), dx, dy, mat));
        // back
        quads.add(new Quad(new Vec3(min.x(), min.y(), max.z()), dx, dy, mat)); 
    }

    /**
     * Constructs a new cuboid that doesn't have to be axis aligned by using a 
     * position vector and 3 vectors between verticies.
     * @param a the position vector of the bottom front left vertex
     * @param dx vector between { @param a } and the bottom front right vertex.
     * @param dy vector between { @param a } and the top front left vertex.
     * @param dz vector between { @param a } and the top back left vertex.
     * @param mat the material to be assigned to each tri in the mesh
     * Position vectors of all verticies of the cuboid can be calculated from 
     * the inputs. 
     */
    public Cuboid(Vec3 a, Vec3 dx, Vec3 dy, Vec3 dz, Material mat) {
        quads = new HittableList(6);

        Vec3 b = a.add(dx);                 // bottom   front   left
        Vec3 c = a.add(dx).add(dz);         // bottom   back    left
        Vec3 d = a.add(dz);                 // bottom   back    right
        Vec3 e = a.add(dy);                 // top      front   left
        Vec3 f = a.add(dx).add(dy);         // top      front   right
        Vec3 g = a.add(dx).add(dz).add(dy); // top      back    right
        Vec3 h = a.add(dz).add(dy);         // top      back    left

        // top
        quads.add(new Quad(e, f, g, h, mat));
        // bottom
        quads.add(new Quad(a, b, c, d, mat));
        // left
        quads.add(new Quad(d, a, e, h, mat));
        // right
        quads.add(new Quad(b, c, g, f, mat));
        // front
        quads.add(new Quad(a, b, f, e, mat));
        // back
        quads.add(new Quad(c, d, h, g, mat));
    }

    /**
     * Checks for hits with the HittableList quads.
     * @param r the ray for which to check for intersections.
     * @param tInterval the interval of t values along the ray for which to 
     *                  check for intersections.
     * @param rec a HitRecord to be passed to the hittable tris held in quads.
     * @return {@code true} if a hit is detected against any of the tris in the
     *         quads in the hittable list else {@code false}.
     */
    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        return quads.hit(r, tInterval, rec);
    }
}
