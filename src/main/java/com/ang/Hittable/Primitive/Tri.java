package com.ang.Hittable.Primitive;

import com.ang.AABB.AABB;
import com.ang.Hittable.Hittable;
import com.ang.Material.Material;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;
import com.ang.Util.Vec3;

/**
 * Primitives are atomic hittables (not made of other hittables).
 * Hittable representing a triangle. 
 */
public class Tri extends Hittable{
    private Vec3        a; // position vector of a vertex
    private Vec3        ab, ac; // vectors between verticies
    private Vec3        na, nb, nc; // vertex normals
    private Vec3        normal; // naive normal of triangle
    private AABB        bBox;
    private Material    mat;       

    /**
     * Constructs a triangle without specific vertex normals. Uses cross
     * product to calculate normal vector and then constructs using this normal.
     * @param a the position vector of the first vertex.
     * @param b the position vector of the second vertex.
     * @param c the position vector of the third vertex.
     * @param mat the material to be assigned to the triangle.
     */
    public Tri(Vec3 a, Vec3 b, Vec3 c, Material mat) {
        this(
            a, b, c, 
            Vec3.cross(b.subtract(a), c.subtract(a)), 
            Vec3.cross(b.subtract(a), c.subtract(a)), 
            Vec3.cross(b.subtract(a), c.subtract(a)), 
            mat);
    }

    /**
     * Constructs a triangle using specific vertex normals. Cross product only
     * used to calculate naive normal, not for normal returned in hit(). If a 
     * hit is detected then the position vector, t value along the ray, normal 
     * vector of surface, material of surface, and uv texture-space coordinates 
     * are recorded in the HitRecord.
     * @param a the position vector of the first vertex.
     * @param b the position vector of the second vertex.
     * @param c the position vector of the third vertex.
     * @param mat the material to be assigned to the triangle.
     */
    public Tri(Vec3 a, Vec3 b, Vec3 c, 
               Vec3 na, Vec3 nb, Vec3 nc, Material mat) {
        this.a = a;

        this.na = na;
        this.nb = nb;
        this.nc = nc;

        this.mat = mat;

        // calculate naive normal
        ab = b.subtract(a);
        ac = c.subtract(a);

        normal = Vec3.cross(ab, ac);

        double minX, minY, minZ;
        double maxX, maxY, maxZ;
        
        // calculating bounds of triangle AABB. Expanding all axes by a small
        // amount to account for infinitely thin edge of axis-aligned tris.
        double expand = 1E-8;
        minX = Math.min(Math.min(a.x(), b.x()), c.x()) - expand;
        minY = Math.min(Math.min(a.y(), b.y()), c.y()) - expand;
        minZ = Math.min(Math.min(a.z(), b.z()), c.z()) - expand;

        maxX = Math.max(Math.max(a.x(), b.x()), c.x()) + expand;
        maxY = Math.max(Math.max(a.y(), b.y()), c.y()) + expand;
        maxZ = Math.max(Math.max(a.z(), b.z()), c.z()) + expand;

        Vec3 min = new Vec3(minX, minY, minZ);
        Vec3 max = new Vec3(maxX, maxY, maxZ);

        bBox = new AABB(min, max);
    }

    /**
     * @return this Tri's AABB.
     */
    @Override 
    public AABB bBox() {
        return bBox;
    }

    /**
     * Checks for an intersection between a ray and the triangle using the 
     * barycentric coordinates of the intersection point and the determinant.
     * @param r the ray to be tested for intersection.
     * @param tInterval interval of t values along  the ray in which to test 
     *                  for intersections.
     * @param rec a HitRecord where information about a potential hit is stored.
     * @returns {@code true} if there is an intersection within the range else
     *          {@code false}.
     */
    @Override
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        if (!bBox.hit(r, tInterval, rec)) {
            return false;
        }

        Vec3 ao = r.origin().subtract(a);
        Vec3 dao = Vec3.cross(ao, r.direction());

        double determinant = -Vec3.dot(r.direction(), normal);
        double invDet = 1 / determinant;

        // barycentric coords of intersection
        double dst = Vec3.dot(ao, normal) * invDet;
        double u = Vec3.dot(ac, dao) * invDet;
        double v = -Vec3.dot(ab, dao) * invDet;
        double w = 1 - u - v;

        // if determinant and barycentric coords are negative, no intersection.
        if (!((determinant >= 1E-6) && (dst >= 0) 
                && (u >= 0) && (v >= 0) && (w >= 0))) {
            return false;
        }

        rec.t = dst;
        rec.p = r.at(rec.t);
        // outward normal is an average of vertex normals weighted by point of
        // intersection, this allows for smooth shading.
        Vec3 outNormal = na.multiply(w).add(nb.multiply(u).add(nc.multiply(v)));
        rec.setFaceNormal(r, outNormal.unitVector());
        rec.mat = mat;

        return true;
    }
}
