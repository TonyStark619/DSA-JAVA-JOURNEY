public class BinarySearch {
    public static void main(String[] args) {
        // Binary search requires a SORTED array
        int[] arr = {10, 20, 30, 40, 50, 60};
        int target = 40;
        
        int start = 0;
        int end = arr.length - 1;
        
        while (start <= end) {
            int mid = start + (end - start) / 2;
            
            if (target < arr[mid]) end = mid - 1;
            else if (target > arr[mid]) start = mid + 1;
            else {
                System.out.println("Element found at index: " + mid);
                return;
            }
        }
        System.out.println("Element not found.");
    }
}