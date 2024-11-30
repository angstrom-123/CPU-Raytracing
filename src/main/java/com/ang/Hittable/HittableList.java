package com.ang.Hittable;

import com.ang.AABB.AABB;
import com.ang.Util.HitRecord;
import com.ang.Util.Interval;
import com.ang.Util.Ray;

/**
 * Stores information about all objects in the scene and their bounding boxes.
 */
public class HittableList {
    public Hittable[]   hittables;

    private AABB        bBox            = new AABB();
    private int         arrayIndex      = 0;            // index into hittables
    private int         maxHittables    = 0;

    /**
     * Constructs a HittableList.
     * @param maxHittables the maximum amount of hittables in the list
     */
    public HittableList(int maxHittables) {
        this.maxHittables = maxHittables;
        hittables = new Hittable[maxHittables];
    }

    /**
     * Adds a hittable to the list.
     * @param h the Hittable to be added to the list.
     */
    public void add(Hittable h) {
        hittables[arrayIndex] = h;
        this.bBox = new AABB(this.bBox, h.bBox());
        arrayIndex++;
    }

    /**
     * Clears the hittable list.
     */
    public void clear() {
        hittables = new Hittable[maxHittables];
        arrayIndex = 0;
    }

    /**
     * Finds the actual amount of added hittables in the list as list.length
     * would always return the maximum amount of hittables that could exist.
     * @return the amount of defined (not null) hittables in the list.
     */
    public int size() {
        return arrayIndex;
    }

    /**
     * Checks for intersections against all hittables in the list.
     * @param r the ray for which to check for intersections.
     * @param tInterval the interval of t values along the ray for which to 
     *                  check for intersections.
     * @param rec a HitRecord to be passed to the hittable tris held in quads.
     * @return {@code true} if a hit is detected against any of the tris in the
     *         in the hittable list else {@code false}.
     */
    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        HitRecord tempRec = new HitRecord();
        boolean hitAnything = false;
        double closestYet = tInterval.max;

        for (int i = 0; i < hittables.length; i++) {
            if (hittables[i] == null) {
                break;
            }

            // if hit something, only searches for closer hits as further hits
            // become impossible for the given ray.
            Interval newBounds = new Interval(tInterval.min, closestYet);
            if (hittables[i].hit(r, newBounds, tempRec)) {
                hitAnything = true;
                closestYet = tempRec.t;
                rec.set(tempRec);
            }
        }

        return hitAnything;
    }

    /**
     * @return the AABB containing all hittables in the list.
     */
    public AABB bBox() {
        return bBox;
    }
}
