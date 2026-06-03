public class SortedArrayCheck {
    public static void main(String[] args) {
        int[] arr = {1, 2, 4, 8, 9, 12}; // Test by changing a number to make it unsorted
        
        boolean isSorted = checkSorted(arr, 0);
        System.out.println("Is the array strictly sorted? " + isSorted);
    }

    static boolean checkSorted(int[] arr, int index) {
        // Base condition: If we reach the last element, it means no unsorted pairs were found
        if (index == arr.length - 1) {
            return true;
        }

        // Check if current element is less than the next AND the rest of the array is sorted
        return arr[index] < arr[index + 1] && checkSorted(arr, index + 1);
    }
}