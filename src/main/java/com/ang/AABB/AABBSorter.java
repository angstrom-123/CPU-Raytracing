package com.ang.AABB;

import com.ang.Hittable.Hittable;

public class AABBSorter {
    public static Hittable[] sort(Hittable[] elements, int axis, int start, int end) {
        boolean swapped;
        Hittable temp;

        Hittable[] sortingArray = new Hittable[end-start];
        int arrayIndex = 0;

        for (int i = start; i < end; i++) {
            sortingArray[arrayIndex] = elements[i];
            arrayIndex++;
        }
        
        for (int i = 0; i < sortingArray.length; i++) {
            swapped = false;

            for (int j = 0; j < sortingArray.length - i - 1; j++) {
                if (sortingArray[j].bBox().axisInterval(axis).size() > sortingArray[j + 1].bBox().axisInterval(axis).size()) {
                    temp = sortingArray[j];
                    sortingArray[j] = sortingArray[j + 1];
                    sortingArray[j + 1] = temp;
                    swapped = true;
                }
            }

            if (swapped == false) {
                break;
            }
        }
        
        for (int i = 0; i < (end-start); i++) {
            elements[start+i] = sortingArray[i];
        }

        return elements;
    }
}
