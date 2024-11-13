package com.ang.Utils;

import com.ang.World.Hittable;

public class AABBSorter {
    public static Hittable[] sort(Hittable[] elements, int axis, int start, int end, int length) {
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

            // might need to remove the -1
            for (int j = 0; j < sortingArray.length - i - 1; j++) {
                if (sortingArray[j].bBox().largestAxis() > sortingArray[j + 1].bBox().largestAxis()) {
                    temp = sortingArray[j];
                    sortingArray[j] = sortingArray[j + 1];
                    sortingArray[j + 1] = temp;
                    swapped = true;
                }
            }
        // for (i = 0; i < arr.length - 1; i++) {
        //     swapped = false;
        //     for (j = 0; j < (arr.length - i - 1); j++) {
        //         if (arr[j] > arr[j+1]) {
        //             temp2 = arr[j];
        //             arr[j] = arr[j+1];
        //             arr[j+1] = temp2;
        //             swapped = true;
        //             System.out.println("swapped");
        //         }
        //     }

            if (swapped == false) {
                break;
            }
        }
        

        Hittable[] outArray = new Hittable[elements.length];
        arrayIndex = 0;
        for (int i = 0; i < elements.length; i++) {
            if ((i > start) && (i < end)) {
                outArray[i] = sortingArray[arrayIndex];
                arrayIndex++;
            } else {
                outArray[i] = elements[i];
            }
        }
        return outArray;
    }
}
