package com.ang.AABB;

import com.ang.Hittable.Hittable;

/**
 * Sorts AABB's in an array of hittables.
 */
public class AABBSorter {
    /**
     * Sorts an array of Hittable objects by an axis length in a certain range
     * using bubble sort.
     * @param arr the array of Hittable to be sorted.
     * @param ax the axis by which to sort the array.
     * @param start the index of the first element to be sorted in the array.
     * @param end the index of the last element to be sorted in the array.
     * @return a new array of Hittable objects with the requested section sorted
     */
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
