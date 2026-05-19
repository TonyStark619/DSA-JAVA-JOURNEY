public class LinearSearch {
    public static void main(String[] args) {
        int[] arr = {18, 12, 9, 14, 77, 50};
        int target = 14;
        int index = -1;

        // Traverse the array to find the target element
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                index = i;
                break; // Stop searching once found
            }
        }

        if (index != -1) {
            System.out.println("Element " + target + " found at index: " + index);
        } else {
            System.out.println("Element not found in the array.");
        }
    }
}