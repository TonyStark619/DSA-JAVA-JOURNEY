public class MatrixBinarySearch {
    public static void main(String[] args) {
        // A 2D array sorted both row-wise and column-wise
        int[][] matrix = {
            {10, 20, 30, 40},
            {15, 25, 35, 45},
            {28, 29, 37, 49},
            {33, 34, 38, 50}
        };
        
        int target = 37;
        
        // Start at the top-right corner
        int r = 0;
        int c = matrix[0].length - 1;

        System.out.println("Searching for target: " + target);

        while (r < matrix.length && c >= 0) {
            if (matrix[r][c] == target) {
                System.out.println("Element found at Row: " + r + ", Column: " + c);
                return;
            }
            // If the current element is greater than the target, move left
            if (matrix[r][c] > target) {
                c--;
            } 
            // If the current element is less than the target, move down
            else {
                r++;
            }
        }
        
        System.out.println("Element not found.");
    }
}