public class MaxInArray {
    public static void main(String[] args) {
        // Simulating a dataset of numbers
        int[] arr = {23, 45, 12, 89, 56};
        
        // Assume the first element is the largest to start
        int max = arr[0];

        // Traverse the array starting from the second element
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i]; // Update max if a larger number is found
            }
        }
        
        System.out.println("The maximum element in the array is: " + max);
    }
}