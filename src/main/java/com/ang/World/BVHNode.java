package com.ang.World;

import com.ang.Utils.AABB;
import com.ang.Utils.AABBSorter;
import com.ang.Utils.HitRecord;
import com.ang.Utils.Interval;
import com.ang.Utils.Ray;

public class BVHNode extends Hittable{
    private Hittable left;
    private Hittable right;
    private AABB bBox = null;

    // auto defines list size and start index
    public BVHNode(HittableList list) {
        this(list.hittables, 0, list.size());
    }

    public BVHNode(Hittable[] objects, int start, int end) {
        int span = end - start;

        bBox = AABB.empty();
        for (int i = start; i < end; i++) {
            this.bBox = new AABB(this.bBox, objects[i].bBox());
        }

        int axis = bBox.largestAxis();

        switch (span) {
            case 1:
                left = right = objects[start];
                // Global.count++;
                break;
            case 2:
                left = objects[start];
                right = objects[start + 1];
                // Global.count += 2;
                break;
            default:
                objects = AABBSorter.sort(objects, axis, start, end);

                // set left, right, and recurse down children
                int mid = start + span / 2;
                left = new BVHNode(objects, start, mid);
                right = new BVHNode(objects, mid, end);

                break;
        }

        // container's bBox contains child bBoxes
        bBox = new AABB(left.bBox(), right.bBox());
    }

    // recursive search down tree
    // if hit outer bBox, looks deeper to see if it hit child bBoxes
    // returns false if it missed, true if it hit any child
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        if (!bBox.hit(r, tInterval, rec)) {
            return false;
        }

        boolean hitLeft = left.hit(r, tInterval, rec);
        // boolean hitRight = right.hit(r, new Interval(), rec);
        boolean hitRight;
        if (hitLeft) {
            hitRight = right.hit(r, new Interval(tInterval.min, rec.t), rec);
        }
        hitRight = right.hit(r, tInterval, rec);

        return hitLeft || hitRight;
    }

    // private boolean boxCompare(Hittable a, Hittable b, int axisIndex) {
    //     Interval axisIntervalA = a.bBox().axisInterval(axisIndex);
    //     Interval axisIntervalB = b.bBox().axisInterval(axisIndex);

    //     return axisIntervalA.min < axisIntervalB.min;
    // }
}
