public class RecursiveBinarySearch {
    public static void main(String[] args) {
        int[] arr = {12, 24, 36, 48, 60, 72}; // Array must be sorted
        int target = 48;
        
        int result = search(arr, target, 0, arr.length - 1);
        
        if (result != -1) {
            System.out.println("Target " + target + " found at index: " + result);
        } else {
            System.out.println("Target not found.");
        }
    }

    static int search(int[] arr, int target, int start, int end) {
        // Base condition: If start passes end, the target doesn't exist
        if (start > end) {
            return -1;
        }

        int mid = start + (end - start) / 2;

        if (arr[mid] == target) {
            return mid;
        }
        
        // Recursive calls: Return the result of the sub-problem
        if (target < arr[mid]) {
            return search(arr, target, start, mid - 1); // Search left half
        }
        return search(arr, target, mid + 1, end);     // Search right half
    }
}