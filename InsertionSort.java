public class InsertionSort {
    public static void main(String[] args) {
        int[] arr = {5, 3, 4, 1, 2};

        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j > 0; j--) {
                // If the current element is smaller than the previous, swap them
                if (arr[j] < arr[j - 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                } else {
                    // When the element is in the right spot, stop checking
                    break;
                }
            }
        }
        
        System.out.print("Array sorted using Insertion Sort: ");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}