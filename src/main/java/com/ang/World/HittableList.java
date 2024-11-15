package com.ang.World;

import com.ang.Utils.AABB;
import com.ang.Utils.HitRecord;
import com.ang.Utils.Interval;
import com.ang.Utils.Ray;

public class HittableList {
    public Hittable[] hittables;
    private int arrayIndex = 0;
    private int maxHittables;
    private AABB bBox = new AABB();

    public HittableList(int maxHittables) {
        this.maxHittables = maxHittables;
        hittables = new Hittable[maxHittables];
    }

    public void add(Hittable h) {
        hittables[arrayIndex] = h;
        this.bBox = new AABB(this.bBox, h.bBox());
        arrayIndex++;
    }

    public void clear() {
        hittables = new Hittable[maxHittables];
        arrayIndex = 0;
    }

    public int size() {
        return arrayIndex;
    }

    public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
        HitRecord tempRec = new HitRecord();
        boolean hitAnything = false;
        double closestYet = tInterval.max;

        // loop over whole hittable list searching for hits
        for (int i = 0; i < hittables.length; i++) {
            if (hittables[i] == null) {
                break;
            }

            // if hit something, only searches for closer hits
            if (hittables[i].hit(r, new Interval(tInterval.min, closestYet), tempRec)) {
                // check if the normals are being calculated correctly here!!!!!!!
                hitAnything = true;
                closestYet = tempRec.t;
                rec.set(tempRec);
                rec.mat = tempRec.mat;
            }
        }

        return hitAnything;
    }

    public AABB bBox() {
        return bBox;
    }
}
