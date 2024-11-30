package com.ang.AABB;

import com.ang.Hittable.Hittable;
import com.ang.Hittable.HittableList;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;

/**
 * Node in binary tree used in BVH (bounding volume hierarchy) optimisation. 
 * Each node on the tree is an AABB that contains 2 children AABB's. The nodes 
 * are sorted in order of size along their longest axes to more optimally split 
 * up the world. This is used to quickly check which hittable objects a ray 
 * could intersect with without having to iterate over all hittables in the 
 * scene every step (as computing those intersections is very expensive).
 */
public class BVHNode extends Hittable{
    private Hittable    left;
    private Hittable    right;
    private AABB        bBox;

    /**
     * Constructs a new BVH node to be the root of the tree.
     * @param list the HittableList to be converted to a BVH.
     */
    public BVHNode(HittableList list) {
        this(list.hittables, 0, list.size());
    }

    /**
     * Recursively constructs all other BVH nodes to sit under the root node.
     * Each individual node contains 2 sub-nodes. The nodes are sorted in order 
     * of their largest axis to split the world more efficiently.
     * @param objects array of all Hittable objects to be placed in BVH.
     * @param start the index of the first Hittable that this node can contain.
     * @param end the index of the last Hittable that this node can contain.
     */
    public BVHNode(Hittable[] objects, int start, int end) {
        int span = end - start;

        // initializes bbox to calculate longest axis
        this.bBox = AABB.empty();
        for (int i = start; i < end; i++) {
            this.bBox = new AABB(this.bBox, objects[i].bBox());
        }

        // split along longest axis
        int axis = bBox.largestAxis();

        switch (span) {
            case 1: // if only 1 node remains then both children inherit it
                left = right = objects[start];
                break;
            case 2: // if 2 nodes remain then they are given to the children 
                left  = objects[start];
                right = objects[start + 1];
                break;
            default: // if more than 2 nodes remain then the recursion continues
                objects = AABBSorter.sort(objects, axis, start, end);

                int mid = start + span / 2;
                left  = new BVHNode(objects, start, mid);
                right = new BVHNode(objects,   mid, end);
                break;
        }

        this.bBox = new AABB(left.bBox(), right.bBox());
    }

    /**
     * Recursively searches down the BVH tree for intersections with AABB's.
     * Each time an intersection is found with a node, the opposing branch can
     * be pruned.
     * @param r the ray for which the intersection should be tested
     * @param tInerval the interval of t values along the ray for which to 
     *                 check for intersections with the AABB's
     * @param rec a HitRecord to be passed to the hit() function of actual
     *            hittable objects at the bottom of the BVH tree.
     * @return {@code true} if an intersection is found within the interval else
     *         {@code false}
     */
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        if (!bBox.hit(r, tInterval, rec)) {
            return false;
        }

        boolean hitLeft = left.hit(r, tInterval, rec);
        boolean hitRight;

        if (hitLeft) {
            hitRight = right.hit(r, new Interval(tInterval.min, rec.t), rec);
        }

        hitRight = right.hit(r, tInterval, rec);

        return (hitLeft || hitRight);
    }
}
