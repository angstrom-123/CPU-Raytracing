package com.ang.AABB;

import com.ang.Hittable.Hittable;
import com.ang.Hittable.HittableList;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;

/*
 * Node in binary tree used in BVH optimisation. Each node on the tree is an
 * axis-aligned bounding box in the HittableList describing the world. The nodes
 * are arranged in order of size along their longest axes.
 */
public class BVHNode extends Hittable{
    private Hittable    left;
    private Hittable    right;
    private AABB        bBox;

    // auto defines list size and start index
    public BVHNode(HittableList list) {
        this(list.hittables, 0, list.size());
    }

    // for child nodes limits scope of objects contained
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
            case 1:
                // one node remains
                left = right = objects[start];
                break;
            case 2:
                // 2 nodes remain
                left  = objects[start];
                right = objects[start + 1];
                break;
            default:
                // set left, right, and recurse down children
                objects = AABBSorter.sort(objects, axis, start, end);

                int mid = start + span / 2;
                left  = new BVHNode(objects, start, mid);
                right = new BVHNode(objects,   mid, end);
                break;
        }

        // re-init bbox to contain child nodes
        this.bBox = new AABB(left.bBox(), right.bBox());
    }

    // recursive search for hit down tree
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        if (!bBox.hit(r, tInterval, rec)) {
            return false;
        }

        // search down both branches, accounting for rays that hit both
        boolean hitLeft = left.hit(r, tInterval, rec);
        boolean hitRight;

        if (hitLeft) {
            hitRight = right.hit(r, new Interval(tInterval.min, rec.t), rec);
        }

        hitRight = right.hit(r, tInterval, rec);

        return hitLeft || hitRight;
    }
}
