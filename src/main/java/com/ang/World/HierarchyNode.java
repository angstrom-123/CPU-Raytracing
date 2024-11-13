package com.ang.World;

import com.ang.Utils.AABB;
import com.ang.Utils.AABBSorter;
import com.ang.Utils.HitRecord;
import com.ang.Utils.Interval;
import com.ang.Utils.Ray;

import com.ang.Global;

public class HierarchyNode extends Hittable{
    private Hittable left;
    private Hittable right;
    private AABB bBox;

    // auto defines list size and start index
    public HierarchyNode(HittableList list) {
        this(list.hittables, 0, list.size());
    }

    public HierarchyNode(Hittable[] objects, int start, int end) {
        int span = end - start;

        switch (span) {
            case 1:
                left = right = objects[start];
                break;
            case 2:
                left = objects[start];
                right = objects[start + 1];
                break;
            default:  
                // sort the objects by bounding box size
                Hittable[] sorted = AABBSorter.sort(objects, objects[start].bBox().largestAxis(), start, end, span);
                
                // set left, right, and recurse down children
                int mid = start + span / 2;
                left = new HierarchyNode(sorted, start, mid);
                right = new HierarchyNode(sorted, mid, end);
                break;
        }

        // container's bBox contains child bBoxes
        bBox = new AABB(left.bBox(), right.bBox());
    }

    // recursive search down tree
    // if hit outer bBox, looks deeper to see if it hit child bBoxes
    // returns false if it missed, true if it hit any child
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        Global.counter++;
        if (!bBox.hit(r, tInterval)) {
            return false;
        }

        boolean hitLeft = left.hit(r, tInterval, rec);
        boolean hitRight = right.hit(r, tInterval, rec);

        return hitLeft || hitRight;
    }

    // private boolean boxCompare(Hittable a, Hittable b, int axisIndex) {
    //     Interval axisIntervalA = a.bBox().axisInterval(axisIndex);
    //     Interval axisIntervalB = b.bBox().axisInterval(axisIndex);

    //     return axisIntervalA.min < axisIntervalB.min;
    // }
}
