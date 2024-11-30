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
 * Compound hittables are hittables that are made of primitives (usually tris).
 * A hittable representing a quadrilateral, constructed of 2 tris. The tris are
 * held in a HittableList.
 */
public class Quad extends Hittable {
    private HittableList tris;

    /**
     * Constructs a quad using 4 corners. All corners must be coplar for a 
     * correct quad. All corners must be defined with a counter-clockwise 
     * winding order for correct normals (corners must be passed in counter-
     * clockwise). If this is not done then the tris will have incorrect normals
     * and may be culled.
     * @param a the position vector of the first vertex.
     * @param b the position vector of the second vertex.
     * @param c the position vector of the third vertex.
     * @param d the position vector of the fourth vertex.
     * @param mat the material to be assigned to all tris in the mesh.
     */
    public Quad(Vec3 a, Vec3 b, Vec3 c, Vec3 d, Material mat) {
        tris = new HittableList(2);

        tris.add(new Tri(a, b, c, mat));
        tris.add(new Tri(a, c, d, mat));
    }

    /**
     * Constructs a quad using one corner and 2 vectors between corners. All 
     * corners must be defined with a counter-clockwise winding order for 
     * correct normals (corners must be passed in counter-clockwise). If this 
     * is not done then the tris will have incorrect normals and may be culled.
     * @param a the position vector of one vertex.
     * @param ab the vector between { @param a } and the second corner.
     * @param ad the vector between { @param a } and the fourth corner.
     * @param mat the material to be assigned to all tris in the mesh.
     * The position of the third corner can be extrapolated from these values. 
     */
    public Quad(Vec3 a, Vec3 ab, Vec3 ad, Material mat) {
        tris = new HittableList(2);

        Vec3 b = a.add(ab);
        Vec3 c = b.add(ad);
        Vec3 d = a.add(ad);

        tris.add(new Tri(a, b, c, mat));
        tris.add(new Tri(a, c, d, mat));
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
