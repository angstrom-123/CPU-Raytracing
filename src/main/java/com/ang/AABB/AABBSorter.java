package com.ang.AABB;

import com.ang.Hittable.Hittable;

public class AABBSorter {
    public static Hittable[] sort(Hittable[] arr, int ax, int start, int end) {
        Hittable[] sortingArray = new Hittable[end - start];

        for (int i = 0; i < end - start; i++) {
            sortingArray[i] = arr[start + i];
        }
        
        for (int i = 0; i < sortingArray.length; i++) {
            boolean swapped = false;

            for (int j = 0; j < sortingArray.length - i - 1; j++) {
                double a = sortingArray[j].bBox().axisInterval(ax).size();
                double b = sortingArray[j + 1].bBox().axisInterval(ax).size();

                if (a > b) {
                    Hittable temp = sortingArray[j];
                    sortingArray[j] = sortingArray[j + 1];
                    sortingArray[j + 1] = temp;
                    swapped = true;
                }
            }

            if (swapped == false) {
                break;
            }
        }
        
        for (int i = 0; i < end - start; i++) {
            arr[start + i] = sortingArray[i];
        }

        return arr;
    }
}
