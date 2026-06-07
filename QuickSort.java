import java.util.Arrays;

public class QuickSort {
    public static void main(String[] args) {
        int[] arr = {5, 4, 3, 2, 1};
        sort(arr, 0, arr.length - 1);
        System.out.println("Sorted Array using Quick Sort: " + Arrays.toString(arr));
    }

    static void sort(int[] nums, int low, int high) {
        if (low >= high) {
            return;
        }

        int s = low;
        int e = high;
        int m = s + (e - s) / 2;
        int pivot = nums[m];

        while (s <= e) {
            // Find an element on the left that should be on the right
            while (nums[s] < pivot) {
                s++;
            }
            // Find an element on the right that should be on the left
            while (nums[e] > pivot) {
                e--;
            }

            // Swap them
            if (s <= e) {
                int temp = nums[s];
                nums[s] = nums[e];
                nums[e] = temp;
                s++;
                e--;
            }
        }

        // Recursively sort the two halves
        sort(nums, low, e);
        sort(nums, s, high);
    }
}