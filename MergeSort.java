import java.util.Arrays;

public class MergeSort {
    public static void main(String[] args) {
        int[] arr = {8, 3, 4, 12, 5, 6};
        arr = mergeSort(arr);
        System.out.println("Sorted Array using Merge Sort: " + Arrays.toString(arr));
    }

    static int[] mergeSort(int[] arr) {
        // Base condition: an array of length 1 is already sorted
        if (arr.length == 1) {
            return arr;
        }

        int mid = arr.length / 2;
        
        // Recursively split the array into left and right halves
        int[] left = mergeSort(Arrays.copyOfRange(arr, 0, mid));
        int[] right = mergeSort(Arrays.copyOfRange(arr, mid, arr.length));

        // Merge the two sorted halves
        return merge(left, right);
    }

    static int[] merge(int[] first, int[] second) {
        int[] mix = new int[first.length + second.length];
        int i = 0, j = 0, k = 0;

        // Compare elements and build the sorted array
        while (i < first.length && j < second.length) {
            if (first[i] < second[j]) {
                mix[k++] = first[i++];
            } else {
                mix[k++] = second[j++];
            }
        }

        // Add any remaining elements from the left side
        while (i < first.length) mix[k++] = first[i++];
        // Add any remaining elements from the right side
        while (j < second.length) mix[k++] = second[j++];

        return mix;
    }
}